package org.athento.nuxeo.report.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.ReportManager;
import org.athento.nuxeo.report.api.model.OutputReport;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportEngine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.test.runner.RuntimeFeature;

/**
 * Test output report.
 * 
 * @author victorsanchez
 * 
 */
@RunWith(FeaturesRunner.class)
@Features(RuntimeFeature.class)
@Deploy("org.athento.nuxeo.report.core")
@LocalDeploy(value = {
		"org.athento.nuxeo.report.jasperreport:OSGI-INF/test-report-jasper-contrib.xml",
		"org.athento.nuxeo.report.jasperreport:OSGI-INF/test-report-contrib.xml" })
public class TestOutputReport {

	/** Log. */
	private static final Log LOG = LogFactory.getLog(TestOutputReport.class);

	@Inject
	ReportManager reportManager;

	/**
	 * Test for XLS output.
	 * 
	 * @throws ReportException
	 * @throws IOException
	 */
	@Test
	public void xlsOutputReport() throws ReportException, IOException {
		if (LOG.isInfoEnabled()) {
			LOG.info("Generating XLS report...");
		}
		Assert.assertNotNull(reportManager);

		// Get output for xls
		OutputReport xlsOutput = reportManager.getOutputReportByReqParam("xls");
		Assert.assertNotNull(xlsOutput);

		// Get report
		Report sampleReport = reportManager.getReportByAlias("sample");
		Assert.assertNotNull(sampleReport);

		// Print
		ReportEngine jrManager = reportManager.getReportEngineById("jr");
		Assert.assertNotNull(jrManager);
		byte[] reportBytes = jrManager.print(sampleReport, xlsOutput,
				new HashMap<String, Object>(0));

		// Output XLS
		File output = new File("/Users/victorsanchez/test/output.xls");
		FileUtils.writeByteArrayToFile(output, reportBytes);
	}

	/**
	 * Test for PDF output.
	 * @throws ReportException 
	 * @throws IOException 
	 */
	@Test
	public void pdfOutputReport() throws ReportException, IOException {
		if (LOG.isInfoEnabled()) {
			LOG.info("Generating PDF report...");
		}
		Assert.assertNotNull(reportManager);

		// Get output for xls
		OutputReport xlsOutput = reportManager.getOutputReportByReqParam("xls");
		Assert.assertNotNull(xlsOutput);

		OutputReport pdfOutput = reportManager.getOutputReportByReqParam("pdf");
		Assert.assertNotNull(pdfOutput);

		// Get report
		Report sampleReport = reportManager.getReportByAlias("sample");
		Assert.assertNotNull(sampleReport);

		// Print
		ReportEngine jrManager = reportManager.getReportEngineById("jr");
		Assert.assertNotNull(jrManager);

		byte[] pdfReportBytes = jrManager.print(sampleReport, pdfOutput,
				new HashMap<String, Object>(0));

		// Output PDF
		File outputPdf = new File("/Users/victorsanchez/test/output.pdf");
		FileUtils.writeByteArrayToFile(outputPdf, pdfReportBytes);
	}
}
