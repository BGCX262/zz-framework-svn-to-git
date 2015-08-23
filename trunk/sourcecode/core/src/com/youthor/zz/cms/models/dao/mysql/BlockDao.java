package com.youthor.zz.cms.models.dao.mysql;

import com.youthor.zz.common.m.resource.RAbstract;

public class BlockDao extends RAbstract {

    protected void construct() {
       this.init("cms/block", "id");
    }

}