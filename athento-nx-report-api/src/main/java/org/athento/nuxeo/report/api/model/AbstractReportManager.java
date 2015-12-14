package org.athento.nuxeo.report.api.model;

import java.util.Map;

import org.athento.nuxeo.report.api.ReportException;

/**
 * Abstract report manager.
 * 
 * @author victorsanchez
 * 
 */
public abstract class AbstractReportManager implements ReportEngine {

	/**
	 * Print report.
	 * 
	 * @param report
	 * @param output
	 * @param printParams
	 * @return
	 * @throws ReportException
	 */
	public byte[] print(Report report, OutputReport output,
			Map<String, String> printParams) throws ReportException {
		return new byte[0];
	}

	/**
	 * Make report given a report instance.
	 * 
	 * @param report
	 *            to create
	 * @param params
	 *            to create
	 * @return
	 * @throws Exception
	 */
	public abstract Report createReport(Report report,
			Map<String, String> params) throws Exception;

}
