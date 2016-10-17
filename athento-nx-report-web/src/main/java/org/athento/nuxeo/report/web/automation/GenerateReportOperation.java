package org.athento.nuxeo.report.web.automation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.ReportManager;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportEngine;
import org.athento.nuxeo.report.api.model.ReportHandler;
import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;
import org.athento.nuxeo.report.web.worker.GenerateReportWorker;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.runtime.api.Framework;
import org.restlet.data.Parameter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Generate a report operation.
 */
@Operation(id = GenerateReportOperation.ID, category = "Reporting", label = "Generate a report", description = "Generate a report given a report alias, engine and output format")
public class GenerateReportOperation {

    /**
     * Log.
     */
    private static final Log LOG = LogFactory.getLog(GenerateReportOperation.class);

    /** Operation ID. */
    public static final String ID = "Athento.Report";

    /** Session. */
    @Context
    protected CoreSession session;

    /** Report service. */
    @Context
    protected ReportManager reportManager;

    /** User workspace service. */
    @Context
    protected UserWorkspaceService userWorkspaceManager;

    /** Worker service. */
    @Context
    protected WorkManager workManager;

    /** Report engine. */
    @Param(name = "engine", required = false, description = "Report engine (ie. jr", values = {"jr"})
    protected String engine = "jr";

    /** Report alias. */
    @Param(name = "alias", required = true, description = "Report alias")
    protected String alias;

    /** Report output format. */
    @Param(name = "format", required = false, description = "Report output format (ie. pdf, xls, html...)",
            values = {"pdf"})
    protected String format = "pdf";

    /** Properties. */
    @Param(name = "properties", required = false, description= "Properties for the report")
    protected Properties properties;

    /**
     * Run and save into destiny document.
     *
     * @param destiny
     * @throws Exception on error
     */
    @OperationMethod
    public void run(DocumentModel destiny) throws Exception {
        if (LOG.isInfoEnabled()) {
            LOG.info("Generating report " + alias + " ...");
        }

        // Get output format
        OutputReport output = reportManager.getOutputReportByReqParam(this.format);
        if (output == null) {
            throw new ReportException("Output " + this.format + " is undefined.");
        }

        // Get report
        Report report = reportManager.getReportByAlias(this.alias);
        if (report == null) {
            throw new ReportException("Report " + this.alias + " is undefined.");
        }

        // Print
        ReportEngine engine = reportManager.getReportEngineById(this.engine);
        if (engine == null) {
            throw new ReportException("Engine " + this.engine + " is undefined.");
        }

        // Set principal to report
        report.setPrincipal(this.session.getPrincipal());

        // Check destiny to save into user workspace
        if (destiny == null) {
            destiny = getUserWorkspaceDocument();
        }

        // Report worker
        GenerateReportWorker reportWorker = new GenerateReportWorker(engine, report, output, destiny);
        reportWorker.setProperties(this.properties);
        workManager.schedule(reportWorker, WorkManager.Scheduling.IF_NOT_RUNNING_OR_SCHEDULED);

    }

    /** Run. */
    @OperationMethod
    public void run() throws Exception {
       run(null);
    }

    /**
     * Get user workspace path.
     *
     * @return the user workspace document
     */
    private DocumentModel getUserWorkspaceDocument() {
        return userWorkspaceManager.getCurrentUserPersonalWorkspace(this.session, null);
    }

}
