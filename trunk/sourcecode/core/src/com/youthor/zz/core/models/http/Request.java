package com.youthor.zz.core.models.http;

import java.util.HashMap;
import java.util.Map;

import com.youthor.zz.common.util.ObjectUtil;

public class Request {

     private String moduleName = "";
     private String frontName = "";
     private String controllerName = "";
     private String actionName = "";
     private String oriPathInfo = "";
     private boolean isDispatched = false;
     private Object httpRequest = null;

     private Map<String, Object> params = new HashMap<String , Object>();
     public Request setParam(String key, Object value) {
         this.params.put(key, value);
         return this;
     }

     public Request setParam(Map<String , Object> map) {
         this.params.putAll(map);
         return this;
     }

     public Object getParam(String key) {
         if (this.httpRequest != null && !this.params.containsKey(key)) {
             Class<?> [] classes = new Class[1];
             classes[0] = String.class;
             Object [] data = new Object[1];
             data[0] = key;
             Object value = ObjectUtil.invokeMethod(this.httpRequest, "getParameter", classes ,data);
             params.put(key, value);
             return value;
         }
         return params.get(key);
     }

    public boolean isDispatched() {
        return isDispatched;
    }

    public void setDispatched(boolean isDispatched) {
        this.isDispatched = isDispatched;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Object getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(Object httpRequest) {
        this.httpRequest = httpRequest;
    }

    public String getFrontName() {
        return frontName;
    }

    public Request setFrontName(String frontName) {
        this.frontName = frontName;
        return this;
    }

    public String getControllerName() {
        return controllerName;
    }

    public Request setControllerName(String controllerName) {
        this.controllerName = controllerName;
        return this;
    }

    public String getActionName() {
        return actionName;
    }

    public Request setActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public String getOriPathInfo() {
        return oriPathInfo;
    }

    public void setOriPathInfo(String oriPathInfo) {
        this.oriPathInfo = oriPathInfo;
    }
}
