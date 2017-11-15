package org.athento.nuxeo.report.core.datasource;

import org.athento.nuxeo.report.api.model.DataSource;
import org.athento.nuxeo.report.api.model.MapReportData;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Map list datasource.
 *
 * @author victorsanchez
 */
public class MapListDatasource<T extends MapReportData> implements DataSource, Serializable {

    private Collection<T> list;

    /**
     * Constructor.
     *
     * @param list
     */
    public MapListDatasource(List<T> list) {
        this.list = list;
    }

    @Override
    public Collection<T> getValues() {
        return list;
    }
}
