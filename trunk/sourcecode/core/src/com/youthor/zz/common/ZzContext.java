package com.youthor.zz.common;

import java.util.HashMap;

import com.youthor.zz.core.models.http.Request;
import com.youthor.zz.core.models.http.Response;

public class ZzContext {
    private static final ThreadLocal<ZzContext> zzContext = new ThreadLocal<ZzContext>();
    private HashMap<String,Object> values = new HashMap<String,Object>();

    private Request request = null;
    private Response response = null;

    public static ZzContext getContext(){
        ZzContext context = zzContext.get();
        if (context == null) {
            context = new ZzContext();
            zzContext.set(context);
        }

        return context;
    }

    public static void end() {
    	ZzContext.getContext();
        ZzContext context = zzContext.get();
        if (context != null ) {
            context.values.clear();
        }
        context.request = null;
        context.response = null;
        context = null;
    }

    public void addObject(String key, Object value) {
        values.put(key, value);
    }

    public Object getObject(String key) {
        return values.get(key);
    }

    public Request getRequest() {
        if (this.request == null) {
            this.request = (Request)ZzApp.getModel("core/http_request");
        }
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        if (this.response == null) {
            this.response = (Response)ZzApp.getModel("core/http_response");;
        }
        return this.response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}