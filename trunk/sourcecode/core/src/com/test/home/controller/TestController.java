package com.test.home.controller;

import com.youthor.zz.common.c.controller.FrontendController;

public class TestController extends FrontendController {
     public void indexAction() {
         System.out.println("hello");
         this.loadLayout();
         this.renderLayout();
     }
}
