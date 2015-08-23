package com.youthor.zz.cms.helpers;

import com.youthor.zz.cms.models.Page;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzHelper;
import com.youthor.zz.common.c.controller.UserController;
import com.youthor.zz.common.util.StringUtil;
import com.youthor.zz.core.models.Design;
import com.youthor.zz.core.models.layout.Update;

public class PageHelper extends ZzHelper {
      public boolean renderPage(UserController action, int pageId) {
          Page page = (Page)ZzApp.getModel("cms/page");
          //for test
          page.addData("id", 1);
          page.addData("custom_theme", "rd");
          page.addData("root_template", "page_one_column");
          String layout_update_xml ="<reference name=\"content\"><block type=\"home/product\" name=\"home.product\" as=\"product\" template=\"home/product.zz\"></block></reference>";
          page.addData("layout_update_xml", layout_update_xml);
          page.addData("content", "hello cms{{block id=\"testBlock\"}}");
          page.addData("identifier", "cmstest");
          page.addData("title", "cms title");
          page.addData("description", "cms description");
          page.addData("meta_description", "cms meta description");
          //end fo test
          page.load(pageId);
          if (page.getData("id") == null) {
              return false;
          }
          this.getRequest().setParam("page", page);
          Update update = action.getLayout().getUpdate();
          String theme = (String)page.getData("custom_theme");
          Design design = action.getLayout().getDesign();
          design.setTheme(theme);
          update.addHandle("default").addHandle("cms_page");
          action.addActionLayoutHandles();
        //need to rewrite
          String rootTemplate = (String)page.getData("root_template");
          if (StringUtil.isNotBlank(rootTemplate)) {
              update.addHandle(rootTemplate);
          }
          update.setFromMap(false);
          update.loadUpdateXml(action.getFullActionName(), action.getLayout());
          String layoutUpdateXml = (String)page.getData("layout_update_xml");
          if (StringUtil.isNotBlank(layoutUpdateXml)) {
              update.addBlockXml(layoutUpdateXml);
          }
          action.getLayout().generateBlocks();
          return true;
      }
}
