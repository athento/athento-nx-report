package org.athento.nuxeo.report.plugin;

import java.io.Serializable;
import java.util.Map;

import net.sf.jasperreports.engine.JasperReport;

import org.athento.nuxeo.report.api.model.DataSource;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;

/**
 * JasperReport definition.
 * 
 * @author victorsanchez
 * 
 */
public final class JRReport extends Report {

	private JasperReport jasperReport;

	/**
	 * Constructor.
	 * 
	 * @param jasperReport
	 * @param descriptor
	 * @param parameters
	 */
	public JRReport(JasperReport jasperReport, ReportDescriptor descriptor,
			Map<String, Object> parameters) {
		super(descriptor);
		this.jasperReport = jasperReport;
		this.parameters = parameters;
	}

	/**
	 * Constructor.
	 * 
	 * @param jasperReport
	 * @param descriptor
	 * @param datasource
	 * @param parameters
	 */
	public JRReport(JasperReport jasperReport, ReportDescriptor descriptor,
			DataSource<?> datasource, Map<String, Object> parameters) {
		super(descriptor, datasource);
		this.jasperReport = jasperReport;
		this.parameters = parameters;
	}

	/**
	 * Get jasper report.
	 * 
	 * @return
	 */
	public JasperReport getJasperReport() {
		return jasperReport;
	}

}
