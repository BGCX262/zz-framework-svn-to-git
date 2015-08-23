package com.youthor.zz.common;

import com.youthor.zz.core.models.http.Request;
import com.youthor.zz.core.models.http.Response;

public class ZzHelper {
    public Request getRequest() {
        return ZzContext.getContext().getRequest();
    }

    public Response getResponse() {
        return  ZzContext.getContext().getResponse();
    }
}
