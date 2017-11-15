package org.athento.nuxeo.report.core.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportException;
import org.athento.nuxeo.report.api.model.*;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Documents report handler.
 */
public abstract class DocumentReportHandler implements ReportHandler {

    /** Log. */
    private static final Log LOG = LogFactory.getLog(DocumentReportHandler.class);

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
        if (params.get("docIds") != null) {
            List<String> documentIds = Arrays.asList(((String) params.get("docIds")).split(","));
            DocumentModelList docList = getDocuments(session, documentIds);
            // Set data-source
            report.setDataSource(new DocumentListDatasource(docList));
        } else if (params.get("query") != null) {
            DocumentModelList docList = getDocuments(session, (String) params.get("query"));
            // Set data-source
            report.setDataSource(new DocumentListDatasource(docList));
        } else if (params.get("lines") != null && ("true".equals(params.get("lines")))) {
            List<MapReportData> list = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (!param.getKey().startsWith("line")) {
                    continue;
                }
                String line = (String) params.get("line" + ++i);
                if (line == null) {
                    continue;
                }
                MapReportData mapData = new MapReportData();
                String[] properties = line.split("\n");
                for (String property : properties) {
                    KeyValueReportData keyValueReportData = new KeyValueReportData(property);
                    mapData.put(keyValueReportData.getKey(), keyValueReportData.getValue());
                }
                list.add(mapData);
            }
            // Set metadata data-source based on metadata document
            report.setDataSource(new MapListDatasource(list));
        }

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
