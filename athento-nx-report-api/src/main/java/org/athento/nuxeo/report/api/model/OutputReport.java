package org.athento.nuxeo.report.api.model;

import org.athento.nuxeo.report.api.ReportException;

/**
 * Output Report.
 * 
 * @author victorsanchez
 * 
 */
public interface OutputReport {

	/**
	 * Print a report.
	 * 
	 * @param report
	 * @return
	 * @throws ReportException
	 */
	byte[] print(Report report) throws ReportException;

}
