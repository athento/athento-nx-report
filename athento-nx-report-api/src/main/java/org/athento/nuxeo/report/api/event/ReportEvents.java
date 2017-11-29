package org.athento.nuxeo.report.api.event;

/**
 * Report events available.
 * 
 * @author victorsanchez
 * 
 */
public interface ReportEvents {

	/** Report started. */
	String REPORT_STARTED_EVENT = "REPORT_STARTED";

	/** Report created. */
	String REPORT_CREATED_EVENT = "REPORT_CREATED";

	/** Report error. */
	String REPORT_ERROR_EVENT = "REPORT_ERROR";

}
