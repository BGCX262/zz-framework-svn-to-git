package com.youthor.zz.cms.models.dao.mysql;

import com.youthor.zz.common.m.resource.RAbstract;

public class PageDao extends RAbstract {

    protected void construct() {
        this.init("cms/page", "id");
     }
}
