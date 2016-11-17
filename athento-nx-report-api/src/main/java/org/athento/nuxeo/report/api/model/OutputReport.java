package org.athento.nuxeo.report.api.model;

import org.athento.nuxeo.report.api.ReportException;

/**
 * Output Report.
 * 
 * @author victorsanchez
 * 
 */
public interface OutputReport {

	/**
	 * Print a report.
	 * 
	 * @param report
	 * @return
	 * @throws ReportException
	 */
	byte[] print(Report report) throws ReportException;

    /**
     * Set mimetype.
     *
     * @param mimetype
     */
    void setMimetype(String mimetype);

    /**
     * Set encoding.
     *
     * @param encoding
     */
    void setEncoding(String encoding);

    /**
     * Get output mimetype.
     * 
     * @return the mimetype
     */
    String getMimetype();

    /**
     * Get output encoding.
     *
     * @return the encoding
     */
    String getEncoding();

}
