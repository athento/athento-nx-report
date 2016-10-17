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
import org.nuxeo.ecm.core.work.api.Work;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.runtime.api.Framework;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
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

	public static final String CATEGORY = "Reporting";

	private ReportEngine reportEngine;
	private Report report;
	private OutputReport output;
	private DocumentModel destiny;

    private Properties properties;

	/**
	 * Constructor.
	 *
	 * @param engine is the report engine
	 * @param report is the report
	 * @param output is the output
	 * @param destiny is the folder to save the generated report
     */
	public GenerateReportWorker(ReportEngine engine, Report report, OutputReport output, DocumentModel destiny) {
		this.reportEngine = engine;
        this.report = report;
		this.output = output;
		this.destiny = destiny;
	}

	@Override
	public String getTitle() {
		return getCategory();
	}
	@Override
	public String getCategory() {
		return CATEGORY;
	}

	@Override
	public void work() throws Exception {
		initSession();

        // Load handler for report
        loadHandler(report, this.properties);

		// Get report bytes
		byte[] reportBytes = reportEngine.print(report, output,
                (Map) this.properties);

		// Save the output report into user-workspace path
		File file = File.createTempFile("report-ath-", "byte");
		try {
			// Write report into file
			FileUtils.writeByteArrayToFile(file, reportBytes);
			FileBlob blob = new FileBlob(file);
			// Check destiny folder for save the report
			if (destiny == null) {
				throw new DocumentException("Destiny folder is not found!");
			}
			DocumentModel reportDocument =
					session.createDocumentModel(destiny.getPathAsString(), IdUtils.generateStringId(), "File");
			// Set default properties
			reportDocument.setPropertyValue("dc:title", report.getDescriptor().getAlias() + "_" + System.currentTimeMillis());
			reportDocument.setPropertyValue("file:content", blob);
            // Create document
            reportDocument = this.session.createDocument(reportDocument);
			// Save document
			this.session.saveDocument(reportDocument);

            // Throw generated event
            final ReportEventContext event = new ReportEventContext(
                    this.session, this.session.getPrincipal(), report);
            event.setPrincipal(this.session.getPrincipal());

            InputStream reportIs = new ByteArrayInputStream(reportBytes);
            event.setContent(new FileBlob(reportIs));

            EventService listenerManager = Framework
                    .getService(EventService.class);
            listenerManager.fireEvent(ReportEvents.REPORT_CREATED_EVENT, event);
		} finally {
			if (file.exists()) {
				file.delete();
			}
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
