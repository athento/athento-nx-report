package org.athento.nuxeo.report.core.datasource;

import org.athento.nuxeo.report.api.model.DataSource;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import java.io.Serializable;
import java.util.Collection;

/**
 * Document list datasource.
 *
 * @author victorsanchez
 */
public class DocumentListDatasource implements DataSource<DocumentModel>, Serializable {

    private Collection<DocumentModel> documentList;

    /**
     * Constructor.
     *
     * @param documentModelList
     */
    public DocumentListDatasource(DocumentModelList documentModelList) {
        this.documentList = documentModelList;
    }

    @Override
    public Collection<DocumentModel> getValues() {
        return documentList;
    }
}
