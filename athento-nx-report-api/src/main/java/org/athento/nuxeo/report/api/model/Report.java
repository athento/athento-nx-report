package org.athento.nuxeo.report.api.model;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;

/**
 * Default report.
 * 
 * @author victorsanchez
 * 
 */
public class Report implements Serializable {

	private DataSource<?> dataSource;
	protected Map<String, Object> parameters;
	protected ReportDescriptor descriptor;

	protected Principal principal;

	public Report(ReportDescriptor descriptor) {
		this(descriptor, new DefaultDataSource<BasicReportData>(),
				new HashMap<String, Object>());
	}

	public Report(ReportDescriptor descriptor, DataSource<?> datasource) {
		this(descriptor, datasource, new HashMap<String, Object>());
	}

	public Report(ReportDescriptor descriptor, DataSource<?> datasource,
			Map<String, Object> params) {
		this.descriptor = descriptor;
		this.dataSource = datasource;
		this.parameters = params;
	}

	public DataSource<?> getDataSource() {
		return dataSource;
	}

	public ReportDescriptor getDescriptor() {
		return descriptor;
	}

	public <T> void setDataSource(DataSource<T> dataSource) {
		this.dataSource = dataSource;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void addParameter(String param, Serializable value) {
		parameters.put(param, value);
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

}
