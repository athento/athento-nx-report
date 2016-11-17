package org.athento.nuxeo.report.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportManager;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportEngine;
import org.athento.nuxeo.report.api.xpoint.OutputDescriptor;
import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;
import org.athento.nuxeo.report.api.xpoint.ReportEngineDescriptor;

/**
 * Report Manager implementation is an adapter for ReportService.
 * 
 * @author victorsanchez
 * 
 */
public class ReportManagerImpl implements ReportManager {

	private Log LOG = LogFactory.getLog(ReportManagerImpl.class);

	// Register info
	private Map<String, ReportEngineDescriptor> engines = new HashMap<String, ReportEngineDescriptor>();
	private Map<String, ReportDescriptor> reports = new HashMap<String, ReportDescriptor>();
	private Map<String, OutputDescriptor> outputs = new HashMap<String, OutputDescriptor>();

	// Singleton
	private static ReportManagerImpl instance = new ReportManagerImpl();

	/**
	 * Get instance (unique).
	 * 
	 * @return
	 */
	public static ReportManagerImpl getInstance() {
		return instance;
	}

	/**
	 * Get or register engine.
	 * 
	 * @param descriptor
	 * @return
	 */
	public ReportEngineDescriptor getOrRegisterReportEngine(
			ReportEngineDescriptor descriptor) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Registering report engine...");
		}
		String engineName = descriptor.getName();
		if (!engines.containsKey(engineName)) {
			engines.put(engineName, descriptor);
			LOG.info("Engines " + engines);
			return descriptor;
		} else {
			return engines.get(engineName);
		}
	}

	/**
	 * Get or register report.
	 * 
	 * @param descriptor
	 * @return
	 */
	public ReportDescriptor getOrRegisterReport(ReportDescriptor descriptor) {
		String reportId = descriptor.getId();
		if (!reports.containsKey(reportId)) {
			reports.put(reportId, descriptor);
			return descriptor;
		} else {
			return reports.get(reportId);
		}
	}

	/**
	 * Get or register output.
	 * 
	 * @param descriptor
	 * @return
	 */
	public OutputDescriptor getOrRegisterOutput(OutputDescriptor descriptor) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Registering output '" + descriptor.getId() + "'.");
		}
		String outputName = descriptor.getId();
		if (!outputs.containsKey(outputName)) {
			outputs.put(outputName, descriptor);
			return descriptor;
		} else {
			return outputs.get(outputName);
		}
	}

	/**
	 * Get output by id.
	 */
	public OutputReport getOutputReportById(String outputId) {
		if (outputId == null) {
			throw new IllegalArgumentException();
		}
		OutputDescriptor descriptor = outputs.get(outputId);
		return descriptor.getOutputReportHandler();
	}

	/**
	 * Get output by request param.
	 */
	public OutputReport getOutputReportByReqParam(String param) {
		if (param == null) {
			throw new IllegalArgumentException();
		}
		for (Entry<String, OutputDescriptor> descriptor : outputs.entrySet()) {
			if (descriptor.getValue().getReqParam().equalsIgnoreCase(param)) {
				OutputReport outputReport = descriptor.getValue().getOutputReportHandler();
				outputReport.setMimetype(descriptor.getValue().getMimetype());
				outputReport.setEncoding(descriptor.getValue().getEncoding());
				return outputReport;
			}
		}
		return null;
	}

	/**
	 * Get report engine.
	 * 
	 * @param id
	 *            of report engine
	 * @return report manager instance
	 */
	public ReportEngine getReportEngineById(String id) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Get report engine with id '" + id + "'");
		}
		if (id == null) {
			throw new IllegalArgumentException();
		}
		ReportEngineDescriptor reportDescriptor = engines.get(id);
		if (reportDescriptor != null) {
			return reportDescriptor.getReportManagerClass();
		}
		return null;
	}

	/**
	 * Get all reports.
	 */
	public List<Report> getAllReports() {
		List<Report> reports = new LinkedList<Report>();
		for (Entry<String, ReportDescriptor> entries : this.reports.entrySet()) {
			reports.add(new Report(entries.getValue()));
		}
		return reports;
	}

	/**
	 * Get report outputs.
	 */
	public List<OutputDescriptor> getReportOutputs() {
		return new ArrayList<OutputDescriptor>(outputs.values());
	}

	/**
	 * Get report by id.
	 */
	public Report getReportById(String id) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Get report with id '" + id + "'.");
		}
		if (id == null) {
			throw new IllegalArgumentException();
		}
		ReportDescriptor descriptor = reports.get(id);
		if (descriptor != null) {
			Report report = new Report(descriptor);
			return report;
		}
		return null;
	}

	/**
	 * Get report by alias.
	 */
	public Report getReportByAlias(String alias) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Get report with alias '" + alias + "'.");
		}
		if (alias == null) {
			throw new IllegalArgumentException();
		}
		for (Entry<String, ReportDescriptor> value : reports.entrySet()) {
			if (value.getValue().getAlias().equalsIgnoreCase(alias)) {
				Report report = new Report(value.getValue());
				return report;
			}
		}
		return null;
	}

	/**
	 * Get output by id.
	 */
	public OutputDescriptor getOutputDescriptorById(String id) {
		return outputs.get(id);
	}

	/**
	 * Get output descriptor by request param.
	 */
	public OutputDescriptor getOutputDescriptorByReqParam(String param) {
		if (param == null) {
			throw new IllegalArgumentException();
		}
		for (Entry<String, OutputDescriptor> descriptor : outputs.entrySet()) {
			if (descriptor.getValue().getReqParam().equalsIgnoreCase(param)) {
				return descriptor.getValue();
			}
		}
		return null;
	}

}
