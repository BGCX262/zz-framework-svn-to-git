package com.youthor.zz.common.test;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;

public abstract class ZzTest extends TestCase {

    protected ZzContext context = null;
    protected Log log = LogFactory.getLog(this.getClass().getName());
   
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String appPath="E:/workspace/youthor/zz-framework/sourcecode/WebContent/";
        ZzApp.setAppPath(appPath);
        ZzApp.setAppendPath("WEB-INF");
        ZzApp.init();
        context = ZzContext.getContext();
        String xmlFilePath = appPath + "WEB-INF/etc/log4j.xml";
        DOMConfigurator.configure(xmlFilePath);
        this.init();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ZzContext.end();
    }

    public void init () {}
}
