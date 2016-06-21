package org.athento.nuxeo.report.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.model.BasicReportData;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportHandler;
import org.athento.nuxeo.report.plugin.datasource.BeanListDatasource;

/**
 * Sample Report Handler.
 * 
 * @author victorsanchez
 * 
 */
public class SampleReportHandler implements ReportHandler {


	/**
	 * Handle report.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handle(Report report, Map<String, Object> params) throws ReportException {
		setDatasource(report, params);
	}

	/**
	 * Set Data source.
	 * 
	 * @param report
	 * @param params
	 */
	private void setDatasource(Report report, Map<String, Object> params) {
		// Add records
		List<BasicReportData> dataList = new ArrayList<BasicReportData>();
		SampleReportBean dataBean = new SampleReportBean();
		dataBean.setName("John Albe");
		dataBean.setAddress("Rock st., 124");
		dataBean.setAge(30);
		dataList.add(dataBean);
		SampleReportBean dataBean2 = new SampleReportBean();
		dataBean2.setName("Mary Jane Hudson");
		dataBean2.setAddress("Large 43 st., 311");
		dataBean2.setAge(24);
		dataList.add(dataBean2);
		SampleReportBean dataBean3 = new SampleReportBean();
		dataBean3.setName("Mary2 Jane Hudson");
		dataBean3.setAddress("Small st., 19");
		dataBean3.setAge(54);
		dataList.add(dataBean3);
		SampleReportBean dataBean4 = new SampleReportBean();
		dataBean4.setName("Ilde Sanz");
		dataBean4.setAddress("Portada st., 1");
		dataBean4.setAge(64);
		dataList.add(dataBean4);

		// Set data-source
		report.setDataSource(new BeanListDatasource(dataList));

	}

}
