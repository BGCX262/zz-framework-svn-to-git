package com.test.home.models.dao.mysql;

import com.youthor.zz.common.m.resource.RAbstract;

public class UserDao extends RAbstract {

    public void construct() {
        this.init("home/user", "id");
    }
}
