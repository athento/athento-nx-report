package org.athento.nuxeo.report.web.restlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.ReportManager;
import org.athento.nuxeo.report.api.event.ReportEventContext;
import org.athento.nuxeo.report.api.event.ReportEvents;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportEngine;
import org.athento.nuxeo.report.api.model.ReportHandler;
import org.athento.nuxeo.report.api.xpoint.OutputDescriptor;
import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.platform.ui.web.restAPI.BaseNuxeoRestlet;
import org.nuxeo.ecm.platform.util.RepositoryLocation;
import org.nuxeo.runtime.api.Framework;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.OutputRepresentation;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Report restlet.
 * 
 * @author victorsanchez
 * 
 */
@Scope(ScopeType.EVENT)
@Name("reportRestlet")
public class ReportRestlet extends BaseNuxeoRestlet implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Log. */
	private static Log LOG = LogFactory.getLog(ReportRestlet.class);

	@In(create = true)
	protected transient NavigationContext navigationContext;

	@In(required = false)
	protected transient DocumentModel currentDocument;

	protected CoreSession documentManager;

	private transient ReportManager manager;

	/**
	 * Handle restlet.
	 */
	@Override
	public void handle(Request req, final Response res) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle report restlet...");
		}

		String engine = (String) req.getAttributes().get("engine");
		if (engine == null) {
			LOG.warn("Error handling ReportRestlet. Engine is " + "null.");
			handleError(res, "you must specify a valid report engine.");
			return;
		}

		String reportAlias = (String) req.getAttributes().get("report");
		if (reportAlias == null) {
			handleError(res, "you must specify a report alias.");
			return;
		}

		String outputId = (String) getQueryParamValue(req, "output", null);
		if (outputId == null) {
			handleError(res, "you must specify a output format.");
			return;
		}

		if (LOG.isInfoEnabled()) {
			LOG.info("Processing report '" + reportAlias + "' with engine '"
					+ engine + "' and output '" + outputId + "'");
		}

		try {

			navigationContext.setCurrentServerLocation(new RepositoryLocation(
					"default"));
			documentManager = navigationContext.getOrCreateDocumentManager();

			// Report manager instanced
			manager = Framework.getService(ReportManager.class);

			ReportEngine reportEngine = manager.getReportEngineById(engine);
			Report report = manager.getReportByAlias(reportAlias);
			if (report == null) {
				handleError(res, "Unable to found report by alias '"
						+ reportAlias + "'");
				return;
			}

			report.setPrincipal(documentManager.getPrincipal());

			// Set other parameters
			Map<String, Object> queryParams = new HashMap<String, Object>();
			Parameter[] params = getQueryParams(req);
			for (Parameter param : params) {
				queryParams.put(param.getName(), param.getValue());
			}

			// Load handler for report
			loadHandler(report, queryParams);

			OutputReport output = manager.getOutputReportByReqParam(outputId);
			if (output == null) {
				handleError(res, "Output is not defined for '" + outputId + "'");
				return;
			}

			// set the content disposition and file name
			String filename = report.getDescriptor().getName();
			OutputDescriptor outputDescriptor = manager
					.getOutputDescriptorByReqParam(outputId);
			String extension = outputDescriptor.getExtension();
			if (extension != null) {
				filename = new StringBuffer(filename).append(extension)
						.toString();
			}

			byte[] reportBytes = null;
			try {
				reportBytes = reportEngine.print(report, output, queryParams);
			} catch (ReportException e) {
				LOG.error("Unable to print the report", e);
				handleError(res,
						"Problem ocurred printing report: " + e.getMessage());
				return;
			}

			// Throw event
			final ReportEventContext event = new ReportEventContext(
					documentManager, documentManager.getPrincipal(), report);
			event.setPrincipal(documentManager.getPrincipal());

            InputStream reportIs = new ByteArrayInputStream(reportBytes);
			event.setContent(new FileBlob(reportIs));

			EventService listenerManager = Framework
					.getService(EventService.class);
			listenerManager.fireEvent(ReportEvents.REPORT_CREATED_EVENT, event);

			res.setEntity(new OutputRepresentation(null) {
				@Override
				public void write(OutputStream o) throws IOException {
					try {
						o.write(event.getContent().getByteArray());
						o.flush();
						o.close();
					} catch (Exception e) {
						handleError(res, e);
						return;
					}
				}
			});

			HttpServletResponse response = getHttpResponse(res);
			response.setHeader("Content-Disposition",
					String.format("attachment; filename=\"%s\";", filename));

		} catch (Exception e) {
			LOG.error("Unable to manage report restlet", e);
			handleError(res, e);
		}

	}

	/**
	 * Load handler for report.
	 * 
	 * @param report
	 * @param params
	 */
	private void loadHandler(Report report, Map<String, Object> params) {
		ReportDescriptor descriptor = report.getDescriptor();
		if (descriptor.isUseSeam()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Loading handler " + descriptor.getId());
			}
			if (descriptor.getHandler() != null) {
				try {
					ReportHandler handler = descriptor.getHandler()
							.newInstance();
					Map<String, Object> handleParams = new HashMap<String, Object>();
					handleParams.put("documentManager", documentManager);
					handleParams.putAll(params);
					handler.handle(report, handleParams);
				} catch (InstantiationException | IllegalAccessException
						| ReportException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * Get query params.
	 * 
	 * @param request
	 * @return
	 */
	protected Parameter[] getQueryParams(Request request) {
		Form form = request.getResourceRef().getQueryAsForm();
		Parameter[] values = new Parameter[form.size()];
		for (int i = 0; i < form.size(); i++) {
			values[i] = form.get(i);
		}
		return values;
	}
}
