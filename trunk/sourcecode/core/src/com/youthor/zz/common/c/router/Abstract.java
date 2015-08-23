package com.youthor.zz.common.c.router;

import com.youthor.zz.common.ZzContext;
import com.youthor.zz.core.models.http.Request;

public abstract class Abstract {

    public abstract boolean match();

    public Request getRequest() {
        return ZzContext.getContext().getRequest();
    }
}
