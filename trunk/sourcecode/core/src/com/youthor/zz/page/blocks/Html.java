package com.youthor.zz.page.blocks;

import java.util.ArrayList;
import java.util.List;

import com.youthor.zz.common.c.block.BAbstract;
import com.youthor.zz.core.blocks.Template;

public class Html extends Template {
    private List<String> bodyClass = new ArrayList<String>();

    public BAbstract addBodyClass(String css) {
        this.bodyClass.add(css);
        return this;
    }

    public List<String> getBodyClass() {
        return this.bodyClass;
    }
}