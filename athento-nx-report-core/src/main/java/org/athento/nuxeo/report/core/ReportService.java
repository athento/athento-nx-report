package org.athento.nuxeo.report.core;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.report.api.ReportManager;
import org.athento.nuxeo.report.api.model.Report;
import org.athento.nuxeo.report.api.xpoint.OutputDescriptor;
import org.athento.nuxeo.report.api.xpoint.ReportDescriptor;
import org.athento.nuxeo.report.api.xpoint.ReportExtension;
import org.athento.nuxeo.report.api.xpoint.ReportEngineDescriptor;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.model.Extension;

/**
 * Report service.
 * 
 * @author victorsanchez
 * 
 */
public class ReportService extends DefaultComponent {

	private static Log LOG = LogFactory.getLog(ReportService.class);

	@Override
	public void activate(ComponentContext context) throws Exception {
		super.activate(context);
	}

	@Override
	public void registerExtension(Extension extension) throws Exception {
		String point = extension.getExtensionPoint();
		if (ReportExtension.XPOINT_ENGINE.equals(point)) {
			for (Object contrib : extension.getContributions()) {
				if (contrib instanceof ReportEngineDescriptor) {
					registerEngine((ReportEngineDescriptor) contrib);
				} else {
					LOG.error("Invalid contribution to extension point 'ReportEngine': "
							+ contrib.getClass().getName());
				}
			}
		} else if (ReportExtension.XPOINT_REPORT.equals(point)) {
			for (Object contrib : extension.getContributions()) {
				if (contrib instanceof ReportDescriptor) {
					registerReport((ReportDescriptor) contrib);
				} else {
					LOG.error("Invalid contribution to extension point 'Report': "
							+ contrib.getClass().getName());
				}
			}
		} else if (ReportExtension.XPOINT_OUTPUT.equals(point)) {
			for (Object contrib : extension.getContributions()) {
				if (contrib instanceof OutputDescriptor) {
					registerOutput((OutputDescriptor) contrib);
				} else {
					LOG.error("Invalid contribution to extension point 'Output': "
							+ contrib.getClass().getName());
				}
			}
		}
	}

	/**
	 * Register report engine.
	 * 
	 * @param descriptor
	 */
	private void registerEngine(ReportEngineDescriptor descriptor) {
		LOG.info("Registering engine...");
		ReportManagerImpl.getInstance().getOrRegisterReportEngine(descriptor);
	}

	/**
	 * Register report.
	 * 
	 * @param descriptor
	 */
	private void registerReport(ReportDescriptor descriptor) {
		ReportManagerImpl.getInstance().getOrRegisterReport(descriptor);
	}

	/**
	 * Register an output.
	 * 
	 * @param descriptor
	 */
	private void registerOutput(OutputDescriptor descriptor) {
		ReportManagerImpl.getInstance().getOrRegisterOutput(descriptor);
	}

	/**
	 * Get all reports.
	 * 
	 * @return
	 */
	public List<Report> getAllReports() {
		return ReportManagerImpl.getInstance().getAllReports();
	}

	/**
	 * Adapter.
	 */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == ReportManager.class) {
			return adapter.cast(ReportManagerImpl.getInstance());
		}
		return null;
	}

}
