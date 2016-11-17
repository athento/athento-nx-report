package org.athento.nuxeo.report.api.model;

/**
 * Abstract output.
 */
public abstract class AbstractOutput {

    String mimetype;
    String encoding;

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
