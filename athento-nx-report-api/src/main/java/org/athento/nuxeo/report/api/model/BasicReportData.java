package org.athento.nuxeo.report.api.model;

import java.io.Serializable;

/**
 * Basic Report data.
 * 
 * @author victorsanchez
 * 
 */
public class BasicReportData implements Serializable {

	/**
	 * SUID.
	 */
	private static final long serialVersionUID = 669669955857335627L;

	/** Value. */
	protected Object value;

	/**
	 * Constructor.
	 */
	public BasicReportData() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param value
	 */
	public BasicReportData(Object value) {
		this.value = value;
	}

	/**
	 * Get value.
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Set value.
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

}
