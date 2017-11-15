package org.athento.nuxeo.report.api.model;

import org.athento.nuxeo.report.api.ReportException;

import java.io.Serializable;
import java.util.Map;

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
	 * @param params
	 *             of report to manage
	 * @throws ReportException
	 *             on error
	 */
	void handle(Report report, Map<String, Object> params) throws ReportException;

}
