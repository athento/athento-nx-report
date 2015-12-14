package org.athento.nuxeo.report.api.xpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.model.AbstractReportManager;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Report manager descriptor.
 * 
 * @author victorsanchez
 * 
 */
@XObject("reportEngine")
public class ReportEngineDescriptor {

	private static Log log = LogFactory.getLog(ReportEngineDescriptor.class);

	@XNode("@name")
	private String name;
	
	@XNode("@default")
	private boolean managerDefault;

	private AbstractReportManager reportManagerClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isManagerDefault() {
		return managerDefault;
	}

	public void setManagerDefault(boolean managerDefault) {
		this.managerDefault = managerDefault;
	}

	public AbstractReportManager getReportManagerClass() {
		return reportManagerClass;
	}

	@XNode("class")
	public void setClazz(Class<AbstractReportManager> clazz) {
		try {
			reportManagerClass = clazz.newInstance();
		} catch (Exception e) {
			log.error("Unable to instance Report " + "Manager class '" + clazz
					+ "'.");
		}
	}

}
