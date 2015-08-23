package com.youthor.zz.core.models.util;

import junit.framework.Assert;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.test.ZzTest;


public class DigestTest extends ZzTest {

    public Digest digest = null;

    public void init() {
        this.digest = (Digest)ZzApp.getSingletonModel("core/util_digest");
    }

    public void testMd5ToHex() {
        String md5 = digest.md5ToHex("888888");
        Assert.assertEquals(32, md5.length());
    }
}
