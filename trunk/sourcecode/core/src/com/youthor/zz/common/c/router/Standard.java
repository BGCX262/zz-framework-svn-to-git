package com.youthor.zz.common.c.router;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.util.ObjectUtil;

public class Standard extends Abstract {

    @Override
    public boolean match() {
        Object userController = ZzApp.createUserController(this.getRequest().getFrontName(), this.getRequest().getControllerName());
        if (userController == null) {
             return false;
        }
        else {
            String actionMethodName = this.getRequest().getActionName() +  "Action";
            boolean hasAction = ObjectUtil.isMethodExist(userController, actionMethodName, null);
            if (hasAction) {
                this.getRequest().setDispatched(true);
                ObjectUtil.invokeMethod(userController, "dispatch", null, null);
                return true;
            }
        }
        return false;
    }
}
