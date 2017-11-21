package org.athento.nuxeo.report.api.model;

/**
 * Key value Report data.
 * 
 * @author victorsanchez
 * 
 */
public class KeyValueReportData extends BasicReportData {

	/**
	 * SUID.
	 */
	private static final long serialVersionUID = 52083947502893475L;

	/** Value. */
	protected String key;

	/**
	 * Constructor.
	 */
	public KeyValueReportData() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param keyValue
	 */
	public KeyValueReportData(String keyValue) {
	    if (keyValue.contains("=")) {
            this.key = keyValue.split("=")[0].trim();
            try {
				this.value = keyValue.split("=")[1].trim();
			} catch (IndexOutOfBoundsException e) {
            	this.value = "";
			}
        }
	}

	/**
	 * Get key.
	 *
	 * @return
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
