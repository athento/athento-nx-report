package org.athento.nuxeo.report.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Report data.
 * 
 * @author victorsanchez
 * 
 */
public class MapReportData<T extends BasicReportData> extends HashMap<String, T> {

	/**
	 * SUID.
	 */
	private static final long serialVersionUID = -5723946192376434L;
	/**
	 * Constructor.
	 */
	public MapReportData() {
		super();
	}

}
