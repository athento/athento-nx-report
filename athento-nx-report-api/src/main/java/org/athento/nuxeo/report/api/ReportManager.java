package org.athento.nuxeo.report.api;

import java.util.List;

import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportEngine;
import org.athento.nuxeo.report.api.xpoint.OutputDescriptor;

/**
 * Report Manager.
 * 
 * @author victorsanchez
 * 
 */
public interface ReportManager {

	/**
	 * Get report by id.
	 * 
	 * @param id
	 * @return
	 */
	Report getReportById(String id);

	/**
	 * Get report by alias.
	 * 
	 * @param alias
	 * @return
	 */
	Report getReportByAlias(String path);

	/**
	 * Get output report by id.
	 * 
	 * @param outputId
	 * @return
	 */
	OutputReport getOutputReportById(String outputId);

	/**
	 * Get report manager by id of manager.
	 * 
	 * @param id
	 * @return
	 */
	ReportEngine getReportEngineById(String id);

	/**
	 * Get output report by request param.
	 * 
	 * @param param
	 * @return
	 */
	OutputReport getOutputReportByReqParam(String param);

	/**
	 * Get output descriptor by id.
	 * 
	 * @param id
	 *            the output id
	 * @return the output descriptor
	 */
	OutputDescriptor getOutputDescriptorById(String id);

	/**
	 * Get output descriptor by request param.
	 * 
	 * @param param
	 * @return
	 */
	OutputDescriptor getOutputDescriptorByReqParam(String param);

	/**
	 * Get all reports.
	 * 
	 * @return
	 */
	List<Report> getAllReports();

	/**
	 * Get output reports.
	 * 
	 * @return
	 */
	List<OutputDescriptor> getReportOutputs();

}
