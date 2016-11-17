package org.athento.nuxeo.report.plugin.output;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.model.AbstractOutput;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.plugin.JRReport;

/**
 * JXLS output definition.
 * 
 * @author victorsanchez
 * 
 */
public class JXLSOutput extends AbstractOutput implements OutputReport {

	private Log log = LogFactory.getLog(JXLSOutput.class);

	/**
	 * Print report in XLS.
	 */
	public byte[] print(Report report) throws ReportException {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		File tmpFile = null;
		try {
			Collection<?> ds = report.getDataSource().getValues();

			JRDataSource dataSource = new JRBeanCollectionDataSource(ds);

			if (ds.isEmpty()) {
				dataSource = new JREmptyDataSource();
			}

			Map<String, Object> parameters = report.getParameters();

			String reportTitle = report.getDescriptor().getName();
			parameters.put("REPORT_TITLE", reportTitle);
			parameters.put("CURRENT_DATE",
					new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

			JasperReport jr = ((JRReport) report).getJasperReport();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
					parameters, dataSource);

			tmpFile = File.createTempFile("temp-report-platform", ".xls");

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
					tmpFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setDetectCellType(true);
			configuration.setOnePagePerSheet(false);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setShowGridLines(true);
			configuration.setWhitePageBackground(false);
			configuration.setCollapseRowSpan(true);
			exporter.setConfiguration(configuration);

			exporter.exportReport();

			fis = new FileInputStream(tmpFile);
			baos = new ByteArrayOutputStream();

			IOUtils.copy(fis, baos);

			return baos.toByteArray();
		} catch (Exception e) {
			log.error("Unable to get report XLS.", e);
			throw new ReportException(e.getMessage(), e.getCause());
		} finally {
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(fis);
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}

	@Override
	public String getMimetype() {
		return "application/excel";
	}

	@Override
	public String getEncoding() {
		return null;
	}

}
