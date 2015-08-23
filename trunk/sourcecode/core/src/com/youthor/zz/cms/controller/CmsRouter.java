package com.youthor.zz.cms.controller;

import java.util.Map;

import com.youthor.zz.common.c.FrontController;
import com.youthor.zz.common.c.router.Abstract;

public class CmsRouter extends Abstract {

    public void initControllerRouters(Map<String, Object> para) {
        FrontController frontController = (FrontController)para.get("frontController");
        frontController.addRouter(this);
    }
    
    public boolean match() {
        this.getRequest().setFrontName("cms").setControllerName("page").setActionName("index");
        return true;
    }

}
