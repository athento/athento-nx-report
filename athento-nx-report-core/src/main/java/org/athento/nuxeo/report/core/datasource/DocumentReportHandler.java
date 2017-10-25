package org.athento.nuxeo.report.core.datasource;

import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.model.ReportHandler;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Documents report handler.
 */
public abstract class DocumentReportHandler implements ReportHandler {

    /**
     * Handler report.
     *
     * @param report
     * @param data
     * @throws ReportException
     */
    @Override
    public void handle(Report report, Map<String, Object> data) throws ReportException {
        CoreSession coreSession = (CoreSession) data.get("documentManager");
        setDatasource(report, coreSession, data);
    }

    /**
     * Set data-source.
     *
     * @param report
     * @param params
     */
    public void setDatasource(Report report, CoreSession session,
                              Map<String, Object> params) {
        DocumentModelList docList = new DocumentModelListImpl();
        if (params.get("docIds") != null) {
            List<String> documentIds = Arrays.asList(((String) params.get("docIds")).split(","));
            docList = getDocuments(session, documentIds);
        } else if (params.get("query") != null) {
            docList = getDocuments(session, (String) params.get("query"));
        }
        // Set data-source
        report.setDataSource(new DocumentListDatasource(docList));

    }

    /**
     * Get documents from query.
     *
     * @param session
     * @param query
     * @return
     */
    protected DocumentModelList getDocuments(CoreSession session, String query) {
        return session.query(query);
    }

    /**
     * Get documents from list ids.
     *
     * @param session
     * @param docList
     * @return
     */
    protected DocumentModelList getDocuments(CoreSession session, List<String> docList) {
        String idList = StringUtils.join(docList, "','");
        String query = "SELECT * FROM Document WHERE ecm:uuid IN ('" + idList + "')";
        return session.query(query);
    }

}
