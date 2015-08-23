package com.youthor.zz.core.models.http;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private String body;
    private Map<String, String> headers = new HashMap<String, String>();
    private Object httpResponse = null;

    public Object getHttpResponse() {
        return this.httpResponse;
    }

    public void setHttpResponse(Object httpResponse) {
        this.httpResponse = httpResponse;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }
}
