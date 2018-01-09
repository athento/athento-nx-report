package org.athento.nuxeo.report.web.worker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.event.ReportEventContext;
import org.athento.nuxeo.report.api.event.ReportEvents;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportEngine;
import org.athento.nuxeo.report.api.model.ReportHandler;
import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.DocumentException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.work.AbstractWork;
import org.nuxeo.runtime.api.Framework;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Generate a report worker.
 *
 * @author <a href="vs@athento.com">Victor Sanchez</a>
 *
 */
public class GenerateReportWorker extends AbstractWork {

	/** Log. */
	private static final Log LOG = LogFactory.getLog(GenerateReportWorker.class);

	public static final String CATEGORY = "reporting";

	private ReportEngine reportEngine;
	private Report report;
	private OutputReport output;
	private String outputFormat;
	private DocumentModel destiny;
	private String title;
    private Properties properties;
    private String doctype;

	/**
	 * Constructor.
	 *
	 * @param engine is the report engine
	 * @param report is the report
	 * @param output is the output
	 * @param outputFormat is the output format
	 * @param destiny is the folder to save the generated report
	 * @param title is the report title
	 * @param doctype is the document type
     */
	public GenerateReportWorker(ReportEngine engine, Report report, OutputReport output, String outputFormat, DocumentModel destiny, String title, String doctype) {
		this.reportEngine = engine;
        this.report = report;
		this.output = output;
		this.outputFormat = outputFormat;
		this.destiny = destiny;
		this.title = title;
		this.doctype = doctype;
	}

	@Override
	public String getTitle() {
		return title == null ? report.getDescriptor().getAlias() + "_"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(GregorianCalendar.getInstance().getTime()) : title;
	}

	@Override
	public String getCategory() {
		return CATEGORY;
	}

	@Override
	public void work() throws Exception {
		initSession();
		File file = null;
		DocumentModel reportDocument = null;
		try {
			notifyEvent(ReportEvents.REPORT_STARTED_EVENT, null, null);
			// Load handler for report
			loadHandler(report, this.properties);

			// Get report bytes
			byte[] reportBytes = reportEngine.print(report, output,
					(Map) this.properties);

			// Save the output report into user-workspace path
			file = File.createTempFile("report-ath-", "byte");
			// Write report into file
			FileUtils.writeByteArrayToFile(file, reportBytes);
			FileBlob blob = new FileBlob(file);
			blob.setFilename(report.getDescriptor().getName() + "."
					+ (outputFormat != null ? outputFormat : "out"));
			blob.setMimeType(this.output.getMimetype());
			blob.setEncoding(this.output.getEncoding());

			// Check destiny folder for save the report
			if (destiny == null) {
				throw new DocumentException("Destiny folder is not found!");
			}
			reportDocument =
					session.createDocumentModel(destiny.getPathAsString(), IdUtils.generateStringId(), doctype);
			// Set default properties
			reportDocument.setPropertyValue("dc:title", getTitle());
			if (reportDocument.hasSchema("file")) {
                reportDocument.setPropertyValue("file:content", blob);
            } else {
			    throw new ReportException("Document type " + doctype +
                        " has no 'file' schema to save generated report.");
            }
			// Create document
			reportDocument = this.session.createDocument(reportDocument);
			// Save document
			this.session.saveDocument(reportDocument);
			// Notify report ends
			notifyEvent(ReportEvents.REPORT_CREATED_EVENT, reportDocument, reportBytes);
		} catch (Exception e) {
			LOG.error("Report exception occurred", e);
			// Notify report error
			notifyEvent(ReportEvents.REPORT_ERROR_EVENT, reportDocument, null);
		} finally {
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

    /**
     * Notify event.
     *
     * @param eventName
     * @param reportDocument
     * @param reportBytes
     * @throws IOException on error
     */
	private void notifyEvent(String eventName, DocumentModel reportDocument, byte [] reportBytes) throws IOException {
		EventService listenerManager = Framework
				.getService(EventService.class);
		// Throw generated event
		final ReportEventContext event = new ReportEventContext(
				this.session, this.session.getPrincipal(), report);
		event.getProperties().putAll(this.properties);
		event.setPrincipal(this.session.getPrincipal());
		if (reportDocument != null) {
			event.setReportDocument(reportDocument);
		}
		if (reportBytes != null) {
			InputStream reportIs = new ByteArrayInputStream(reportBytes);
			event.setContent(new FileBlob(reportIs));
		}
		listenerManager.fireEvent(eventName, event);
		if (LOG.isInfoEnabled()) {
			LOG.info("Report event launched for " + eventName);
		}
	}

	/**
     * Set report properties.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Load handler for report.
     *
     * @param report
     * @param params
     */
    private void loadHandler(Report report, Properties params) {
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
                    handleParams.put("documentManager", this.session);
                    handleParams.putAll(params);
                    handler.handle(report, handleParams);
                } catch (InstantiationException | IllegalAccessException
                        | ReportException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
