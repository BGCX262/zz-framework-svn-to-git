package com.test.controllers;



import org.apache.log4j.xml.DOMConfigurator;

import junit.framework.Assert;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;

import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.common.http.Request;
import com.youthor.zz.common.http.Response;
import com.youthor.zz.common.test.ZzTest;
import com.youthor.zz.core.models.layout.Update;

public class TestControllerTest extends ZzTest {
    
    @SuppressWarnings("unchecked")
    public void testIndexAction() {
        Request zzRequest = this.context.getRequest();
        zzRequest.setOriPathInfo("home/test/index");
        ZzApp.run();
        Update update = (Update)context.getObject("zz_update");
        BAbstract topBlock = update.getTopBlock();
        Assert.assertNotNull(topBlock);
        Assert.assertEquals(topBlock.getName(), "root");
        Assert.assertEquals(topBlock.getClass().getName(), "com.youthor.zz.page.blocks.Html");
        Assert.assertEquals(topBlock.getData("template"), "page/3columns.vm");
        
        
        BAbstract block = topBlock.getChild("content");
        Assert.assertNotNull(block);
        Assert.assertEquals(block.getName(), "content");
        Assert.assertEquals(block.getClass().getName(), "com.youthor.zz.core.blocks.text.List");
        Assert.assertNull(block.getData("template"));
        
//        Abstract blocksub = block.getChild("alias_1");
//        Assert.assertNotNull(blocksub);
//        Assert.assertEquals(blocksub.getName(), "home.product");
//        Assert.assertEquals(blocksub.getClass().getName(), "com.test.home.blocks.Product");
//        Assert.assertEquals(blocksub.getData("template"), "catalog/product/compare/list.phtml");
        
//        Abstract headBlock = topBlock.getChild("head");
//        Map<String, ZzObject> skinMap= (Map<String, ZzObject>)headBlock.getData("skin_items");
//        Assert.assertEquals(skinMap.size(), 20);
       
        Response response = this.context.getResponse();
        String body = response.getBody();
       System.out.println(body);
       // Assert.assertEquals("hello product", body);
        
        
    }
    
    public static void main(String [] args) {
    	ZzContext context = ZzContext.getContext();
    	 String appPath="C:/java/sourcecode/WebContent/";
         ZzApp.setAppPath(appPath);
         ZzApp.setAppendPath("WEB-INF");
         ZzApp.init();
         context = ZzContext.getContext();
         //String xmlFilePath = appPath + "WEB-INF/etc/log4j.xml";
         //DOMConfigurator.configure(xmlFilePath);
         
    	Request zzRequest = context.getRequest();
        zzRequest.setOriPathInfo("home/test/index");
        ZzApp.run();
        Response response = context.getResponse();
        String body = response.getBody();
        System.out.println(body);
        
    }
}
