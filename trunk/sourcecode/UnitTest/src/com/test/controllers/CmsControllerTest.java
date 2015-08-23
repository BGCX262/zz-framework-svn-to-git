package com.test.controllers;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.http.Request;
import com.youthor.zz.common.http.Response;
import com.youthor.zz.common.test.ZzTest;

public class CmsControllerTest extends ZzTest {
    public void testViewAction() {
         Request zzRequest = this.context.getRequest();
         zzRequest.setOriPathInfo("cms/page/view");
         ZzApp.run();
         Response response = this.context.getResponse();
         String body = response.getBody();
         System.out.println(body);
    }
}