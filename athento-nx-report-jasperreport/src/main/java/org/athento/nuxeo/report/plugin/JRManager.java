package org.athento.nuxeo.report.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.ReportManager;
import org.athento.nuxeo.report.api.model.AbstractReportManager;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportHandler;
import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;
import org.nuxeo.runtime.api.Framework;

/**
 * JasperReport Manager Engine implementation.
 * 
 * @author victorsanchez
 * 
 */
public class JRManager extends AbstractReportManager {

	/** Log. */
	private static Log LOG = LogFactory.getLog(JRManager.class);

	/**
	 * Create report by alias.
	 * 
	 * @param alias
	 *            is the alias
	 * @param params
	 *            are the parameters
	 * @return the report
	 * @throws ReportException
	 *             on error
	 */
	public Report createReportByAlias(String alias, Map<String, Object> params)
			throws ReportException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Creating report '" + alias + "'");
		}
		ReportManager manager = Framework.getService(ReportManager.class);
		// Getting report from service
		Report report = manager.getReportByAlias(alias);
		if (report == null) {
			throw new ReportException("Report with alias " + alias
					+ " is not found");
		}
		return createReport(report, params);
	}

	/**
	 * Create a report by name and parameters.
	 * 
	 * @param report
	 *            is the report
	 * @param params
	 *            are the report parameters
	 * @return the report
	 * @throws ReportException
	 *             on error
	 */
	@Override
	public Report createReport(Report report, Map<String, Object> params)
			throws ReportException {
		// Report descriptor
		ReportDescriptor reportDescriptor = report.getDescriptor();
		InputStream reportStream = null;
		if (reportDescriptor.getPath() != null) {
			reportStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(reportDescriptor.getPath());
			if (reportStream == null) {
				throw new ReportException("Path " + reportDescriptor.getPath()
						+ " is not found.");
			}
		} else if (reportDescriptor.getUri() != null) {
			try {
				URI uri = new URI(reportDescriptor.getUri());
				reportStream = uri.toURL().openStream();
			} catch (URISyntaxException | IOException e) {
				throw new ReportException("URI error for the report");
			}
		} else {
			throw new ReportException(
					"Path or URI is necessary to print your report");
		}
		JasperReport jr = null;
		try {
			if (!report.getDescriptor().isCompiled()) {
				// Load report from file
				JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
				// Update report data source
				if (report.getDescriptor().getHandler() != null) {
					Class<ReportHandler> handlerClass = report.getDescriptor()
							.getHandler();
					if (handlerClass != null) {
						ReportHandler handler;
						try {
							// Check handler
							if (!report.getDescriptor().isUseSeam()) {
								handler = handlerClass.newInstance();
								// Handle report
								handler.handle(report, params);
							}
						} catch (InstantiationException
								| IllegalAccessException e) {
							LOG.error("Handler class instantiation error", e);
						}
					}
				}
				// Compile report
				jr = JasperCompileManager.compileReport(jasperDesign);
				// Debugging
				if (report.getDescriptor().getOutputFileDebug() != null) {
					JRXmlWriter.writeReport(jr, report.getDescriptor()
							.getOutputFileDebug(), report.getDescriptor()
							.getDebugEncoding());
				}
				// Add parameters to report
				report.getParameters().putAll(params);
			} else {
				// Here, report is compiled
				jr = (JasperReport) JRLoader.loadObject(reportStream);
			}
			return new JRReport(jr, report.getDescriptor(),
					report.getDataSource(), report.getParameters());
		} catch (JRException e) {
			throw new ReportException("Creating report error", e);
		} finally {
			if (reportStream != null) {
				try {
					reportStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Print with JR Engine.
	 * 
	 * @param id
	 *            is the id of report to print
	 * @param output
	 *            is the output manager
	 * @param printParams
	 *            are the print parameters to the report
	 * @return bytes of report
	 * @throws ReportException
	 *             on print error
	 */
	@Override
	public byte[] print(String id, OutputReport output,
			Map<String, Object> printParams) throws ReportException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Print JR report...");
		}
		ReportManager manager = Framework.getService(ReportManager.class);
		// Getting report from service
		Report report = manager.getReportById(id);
		if (report == null) {
			throw new ReportException("Report " + id + " is not found");
		}
		JRReport jrReport;
		jrReport = (JRReport) createReport(report, printParams);
		return output.print(jrReport);

	}

	/**
	 * Print with JR Engine.
	 * 
	 * @param report
	 *            is the report to print
	 * @param output
	 *            is the output manager
	 * @param printParams
	 *            are the print parameters to the report
	 * @return bytes of report
	 * @throws ReportException
	 *             on print error
	 */
	@Override
	public byte[] print(Report report, OutputReport output,
			Map<String, Object> printParams) throws ReportException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Print JR report...");
		}
		JRReport jrReport;
		jrReport = (JRReport) createReport(report, printParams);
		return output.print(jrReport);

	}

	/**
	 * Debug.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			JasperDesign jasperDesign = JRXmlLoader
					.load("/Users/victorsanchez/JaspersoftWorkspace/MyReports/report_conciliacion.jrxml");

			JRDataSource ds = new JRBeanCollectionDataSource(
					new ArrayList<Serializable>());
			if (!ds.next()) {
				ds = new JREmptyDataSource();
			}

			Map<String, Object> parameters = new HashMap<String, Object>();

			// Compile report
			JasperReport jr = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
					parameters, ds);

			/*
			 * JasperExportManager.exportReportToPdfFile(jasperPrint,
			 * "/Users/victorsanchez/output.pdf");
			 */

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
					"/Users/victorsanchez/output.xls"));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			exporter.setConfiguration(configuration);

			exporter.exportReport();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
