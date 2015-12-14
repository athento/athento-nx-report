package org.athento.nuxeo.report.api;

/**
 * Report exception.
 * 
 * @author victorsanchez
 * 
 */
public class ReportException extends Exception {

	private static final long serialVersionUID = 4592750034577002715L;

	/**
	 * 
	 */
	public ReportException() {
		super();
	}

	/**
	 * 
	 * @param msg
	 */
	public ReportException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param msg
	 * @param throwable
	 */
	public ReportException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
