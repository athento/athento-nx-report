package org.athento.nuxeo.report.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Default datasource.
 * 
 * @author victorsanchez
 * 
 * @param <T>
 */
public class DefaultDataSource<T extends BasicReportData> implements
		DataSource<T> {

	protected List<T> values;

	public DefaultDataSource() {
		this.values = new ArrayList<T>();
	}

	public DefaultDataSource(List<T> values) {
		this.values = values;
	}

	public Collection<T> getValues() {
		return values;
	}

}
