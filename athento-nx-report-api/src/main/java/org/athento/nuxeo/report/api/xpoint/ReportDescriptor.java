package org.athento.nuxeo.report.api.xpoint;

import org.athento.nuxeo.report.api.model.ReportHandler;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.runtime.api.Framework;

import java.io.Serializable;

/**
 * Report descriptor.
 * 
 * @author victorsanchez
 * 
 */
@XObject("report")
public class ReportDescriptor implements Serializable {

	@XNode("@id")
	private String id;

	@XNode("uri")
	private String uri;

	@XNode("path")
	private String path;

	@XNode("compiled")
	private boolean compiled;

	@XNode("name")
	private String name;

	@XNode("name@translated")
	private boolean translated = false;

	@XNode("@alias")
	private String alias;

	@XNode("@useSeam")
	private boolean useSeam;

	@XNode("debug/outputFile")
	private String outputFileDebug;

	@XNode("debug/outputFile@encoding")
	private String debugEncoding;

	@XNode("handler@class")
	private Class<ReportHandler> handler;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		if (uri != null && uri.startsWith("${")) {
			uri = StringUtils.expandVars(uri, Framework.getProperties());
		}
		this.uri = uri;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTranslated() {
		return translated;
	}

	public void setTranslated(boolean translated) {
		this.translated = translated;
	}

	public boolean isCompiled() {
		return compiled;
	}

	public void setCompiled(boolean compiled) {
		this.compiled = compiled;
	}

	public Class<ReportHandler> getHandler() {
		return handler;
	}

	public void setHandler(Class<ReportHandler> handler) {
		this.handler = handler;
	}

	public String getOutputFileDebug() {
		return outputFileDebug;
	}

	public void setOutputFileDebug(String outputFileDebug) {
		this.outputFileDebug = outputFileDebug;
	}


    public String getDebugEncoding() {
        return debugEncoding;
    }

    public void setDebugEncoding(String debugEncoding) {
        this.debugEncoding = debugEncoding;
    }

    /**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the useSeam
	 */
	public boolean isUseSeam() {
		return useSeam;
	}

	/**
	 * @param useSeam
	 *            the useSeam to set
	 */
	public void setUseSeam(boolean useSeam) {
		this.useSeam = useSeam;
	}


}
