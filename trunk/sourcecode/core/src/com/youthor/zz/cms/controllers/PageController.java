package com.youthor.zz.cms.controllers;

import com.youthor.zz.cms.helpers.PageHelper;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.c.controller.FrontendController;
import com.youthor.zz.core.models.http.Request;

public class PageController extends FrontendController {

    public void viewAction() {
        Request request = this.getRequest();
        request.setParam("id", 1);
        int pageId = (Integer)request.getParam("id");
        PageHelper pageHelper = (PageHelper)ZzApp.helper("cms/pageHelper");
        if (pageHelper.renderPage(this, pageId)) {
            this.renderLayout();
        }
    }

}
