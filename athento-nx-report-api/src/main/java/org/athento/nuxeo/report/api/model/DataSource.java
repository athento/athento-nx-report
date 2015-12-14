package org.athento.nuxeo.report.api.model;

import java.util.Collection;

/**
 * Datasource interface.
 * 
 * @author Victor Sanchez
 *
 * @param <T>
 */
public interface DataSource<T> {

	Collection<T> getValues();
	
}
