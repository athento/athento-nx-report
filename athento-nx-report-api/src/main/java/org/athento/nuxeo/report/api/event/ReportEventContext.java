package org.athento.nuxeo.report.api.event;

import java.security.Principal;

import org.athento.nuxeo.report.api.model.Report;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;

/**
 * Report event context.
 * 
 * @author Victor Sanchez
 * 
 */
public class ReportEventContext extends EventContextImpl {

	/**
	 * SUID.
	 */
	private static final long serialVersionUID = -3953481683599607883L;

	private Report report;
	private DocumentModel reportDocument;
	private FileBlob content;

	/**
	 * Constructor.
	 * 
	 * @param session
	 * @param report
	 */
	public ReportEventContext(CoreSession session, Principal principal, Report report) {
		super(session, principal, report, null);
		this.report = report;
	}

	/**
	 * Get report.
	 * 
	 * @return
	 */
	public Report getReport() {
		return report;
	}

	/**
	 * Get content of report.
	 * 
	 * @return
	 */
	public FileBlob getContent() {
		return content;
	}

	/**
	 * Set content to context.
	 * 
	 * @param content
	 */
	public void setContent(FileBlob content) {
		this.content = content;
	}

	/**
	 * Get report document.
	 *
	 * @return
	 */
	public DocumentModel getReportDocument() {
		return reportDocument;
	}

	public void setReportDocument(DocumentModel reportDocument) {
		this.reportDocument = reportDocument;
	}
}
