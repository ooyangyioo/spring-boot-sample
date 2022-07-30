package org.yangyi.project.bean;

import java.io.Serializable;

public class MessageBean implements Serializable {
    private String header;
    private String body;
    private String tail;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }
}
