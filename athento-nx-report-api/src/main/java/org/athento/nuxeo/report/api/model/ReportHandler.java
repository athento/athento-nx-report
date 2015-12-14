package org.athento.nuxeo.report.api.model;

import org.athento.nuxeo.report.api.ReportException;

/**
 * Report handler.
 * 
 * @author victorsanchez
 * 
 */
public interface ReportHandler {

	/**
	 * Handler to set datasource or another features into report.
	 * 
	 * @param data
	 *            is the data of report to manage
	 * @throws ReportException
	 *             on error
	 */
	void handle(Report report, Object... data) throws ReportException;

}
