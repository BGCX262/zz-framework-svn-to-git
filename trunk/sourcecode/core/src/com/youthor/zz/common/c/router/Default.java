package com.youthor.zz.common.c.router;

public class Default extends Abstract {
    public boolean match() {
        this.getRequest().setFrontName("cms").setControllerName("page").setActionName("noRoute");
        return true;
    }
}
