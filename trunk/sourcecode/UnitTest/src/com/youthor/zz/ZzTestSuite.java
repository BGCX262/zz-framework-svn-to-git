package com.youthor.zz;

import com.test.controllers.TestControllerTest;
import com.youthor.zz.core.models.util.DigestTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ZzTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Zz Test Framework");
        suite.addTestSuite(TestControllerTest.class);
        suite.addTestSuite(DigestTest.class);
        return suite;
    } 
}
