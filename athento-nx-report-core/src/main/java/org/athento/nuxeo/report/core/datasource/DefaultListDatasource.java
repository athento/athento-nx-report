package org.athento.nuxeo.report.core.datasource;

import org.athento.nuxeo.report.api.model.BasicReportData;
import org.athento.nuxeo.report.api.model.DataSource;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Basic repor tdata list datasource.
 *
 * @author victorsanchez
 */
public class DefaultListDatasource<T extends BasicReportData> implements DataSource, Serializable {

    private Collection<T> list;

    /**
     * Constructor.
     *
     * @param list
     */
    public DefaultListDatasource(List<T> list) {
        this.list = list;
    }

    @Override
    public Collection<T> getValues() {
        return list;
    }
}
