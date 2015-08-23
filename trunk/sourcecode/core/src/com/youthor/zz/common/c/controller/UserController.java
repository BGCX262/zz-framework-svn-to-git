package com.youthor.zz.common.c.controller;

import java.util.Map;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.core.models.http.Request;
import com.youthor.zz.core.models.http.Response;
import com.youthor.zz.common.util.ObjectUtil;
import com.youthor.zz.core.models.Layout;
import com.youthor.zz.core.models.layout.Update;

public abstract class UserController {
    protected String area = "";

    public UserController () {
        
    }

    public UserController (String area) {
        this.setArea(area);
    }

    public void preDispatch() {
        String areaInThread = (String)ZzContext.getContext().getObject("zz_area");
        if (areaInThread == null) {
            ZzContext.getContext().addObject("zz_area", this.getArea());
        }
    }

    public void postDispatch(){
         
    }

    public Layout getLayout() {
        return ZzApp.getLayout();
    }

    public Request getRequest() {
        return ZzContext.getContext().getRequest();
    }

    public Response getResponse() {
        return  ZzContext.getContext().getResponse();
    }

    public String getFullActionName() {
        return this.getRequest().getFrontName()+"_"
             + this.getRequest().getControllerName()+"_"
             + this.getRequest().getActionName();
    }

    public String getActionMethodName(String action) {
        String method = action + "Action";
        return method;
    }

    protected void forward(String action, Map<String , Object> params) {
        Request request = this.getRequest();
        if (params != null) {
            request.setParam(params);
        }
        request.setActionName(action).setDispatched(false);
    }

    protected void forward(String action, String controllerName, Map<String , Object> params) {
        this.forward(action, params);
        Request request = this.getRequest();
        request.setControllerName(controllerName);
    }

    protected void forward(String action, String controllerName,String frontName, Map<String , Object> params) {
        this.forward(action,frontName ,params);
        Request request = this.getRequest();
        request.setFrontName(frontName);
    }

    protected void redirectUrl(String url) {
        
    }

    protected void loadLayout() {
        this.loadLayout("default", true);
    }

    protected void loadLayout(String defaultHandle) {
        this.loadLayout(defaultHandle, true);
    }

    protected void loadLayout(String defaultHandle,boolean generateBlock) {
        Layout layout = this.getLayout();
        Update update = layout.getUpdate();
        update.addHandle(defaultHandle);
        this.addActionLayoutHandles();
        String uri = this.getFullActionName();
        update.loadUpdateXml(uri, layout);
        if (generateBlock) {
            layout = layout.generateBlocks();
        }
    }

    public void addActionLayoutHandles() {
        Layout layout = this.getLayout();
        Update update = layout.getUpdate();
        String uri = this.getFullActionName();
        update.addHandle(uri);
    }

    public void renderLayout() {
        Layout layout = this.getLayout();
        Update update = layout.getUpdate();
        BAbstract topBlock = update.getTopBlock();
        this.getResponse().setBody(topBlock.toHtml());
    }

     public void dispatch() {
         String actionMethodName = this.getActionMethodName(this.getRequest().getActionName());
         this.preDispatch();
         if (this.getRequest().isDispatched()) {
             ObjectUtil.invokeMethod(this, actionMethodName, null, null);
             this.postDispatch();
         }
     }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}