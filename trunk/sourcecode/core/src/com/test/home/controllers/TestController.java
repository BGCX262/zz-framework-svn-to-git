package com.test.home.controllers;

import com.test.home.models.User;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.c.controller.FrontendController;

public class TestController extends FrontendController {
     public void indexAction() {
         //User user = (User)ZzApp.getModel("home/user");
         //user.addData("id", 1);
         //user.addData("name", "zack.liu");
         //user.save();
         this.loadLayout();
         this.renderLayout();
     }
}
