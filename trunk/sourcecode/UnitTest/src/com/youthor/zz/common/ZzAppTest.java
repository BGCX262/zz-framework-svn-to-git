package com.youthor.zz.common;

import java.util.HashMap;

import com.youthor.zz.common.test.ZzTest;

public class ZzAppTest  extends ZzTest {
    public void testDispatchEvent() {
        ZzContext.getContext().addObject("zz_area", "frontend");
        ZzApp.dispatchEvent("controller_front_init_routers", new HashMap<String, Object>());
    }
}
