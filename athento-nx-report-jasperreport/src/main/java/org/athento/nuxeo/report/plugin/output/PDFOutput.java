package org.athento.nuxeo.report.plugin.output;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.model.AbstractOutput;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.plugin.JRReport;
import org.nuxeo.runtime.api.Framework;

/**
 * PDF output definition.
 * 
 * @author victorsanchez
 * 
 */
public class PDFOutput extends AbstractOutput implements OutputReport, Serializable{

	private Log LOG = LogFactory.getLog(PDFOutput.class);

	/**
	 * Print report int PDF.
	 */
	public byte[] print(Report report) throws ReportException {
		try {

			Collection<?> ds = report.getDataSource().getValues();

			JRDataSource dataSource = new JRBeanCollectionDataSource(ds);
			if (ds.isEmpty()) {
				dataSource = new JREmptyDataSource();
			}

			Map parameters = report.getParameters();

			String reportTitle = report.getDescriptor().getName();
			parameters.put("REPORT_TITLE", reportTitle);
			parameters.put("CURRENT_DATE",
					new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			String currentLocale = Framework.getProperty("report.locale", null);
			if (currentLocale != null) {
                Locale locale;
			    if (currentLocale.contains("_")) {
                    String lang = currentLocale.split("_")[0];
                    String country = currentLocale.split("_")[1];
                    locale = new Locale(lang, country);
                } else {
                    locale = new Locale(currentLocale);
                }
				LOG.info("Locale for report " + locale);
                parameters.put(JRParameter.REPORT_LOCALE, locale);
			}

			JasperReport jr = ((JRReport) report).getJasperReport();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jr,
					parameters, dataSource);

			return JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (Exception e) {
            LOG.error("Unable to get report PDF.", e);
			throw new ReportException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public String getMimetype() {
		return "application/pdf";
	}

	@Override
	public String getEncoding() {
		return null;
	}

}
