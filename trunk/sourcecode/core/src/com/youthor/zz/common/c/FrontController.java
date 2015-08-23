package com.youthor.zz.common.c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.common.c.router.Abstract;
import com.youthor.zz.common.c.router.Default;
import com.youthor.zz.common.c.router.Standard;
import com.youthor.zz.core.models.http.Request;
import com.youthor.zz.core.models.http.Response;
import com.youthor.zz.common.util.StringUtil;


public class FrontController {
    
    private List<Abstract> routers = new ArrayList<Abstract>();
    
    
    public FrontController init() {
        Standard  standard = new Standard();
        this.routers.add(standard);
        Map<String, Object> dataMap = new HashMap<String , Object>();
        dataMap.put("frontController", this);
        ZzApp.dispatchEvent("frontController_init_routes", dataMap);
        Default defalut = new Default();
        this.routers.add(defalut);
        return this;
    }
    
    public FrontController dispatch() {
        Request request = this.getRequest();
        String uri = request.getOriPathInfo();
        String [] oriUriArr = StringUtil.split(uri, "/");
        
        if (oriUriArr != null) {
            if (oriUriArr.length >= 3) {
                request.setFrontName(oriUriArr[0]);
                request.setControllerName(oriUriArr[1]);
                request.setActionName(oriUriArr[2]);
            }
            if (oriUriArr.length == 2) {
                request.setFrontName(oriUriArr[0]);
                request.setControllerName(oriUriArr[1]);
                request.setActionName("index");
        }
        if (oriUriArr.length == 1) {
            request.setFrontName(oriUriArr[0]);
            request.setControllerName("index");
            request.setActionName("index");
            }
        }

        int l=oriUriArr.length;
        for (int i=3; i<l; i+=2) {
            if ((i + 1) < oriUriArr.length) {
                request.setParam(oriUriArr[i], oriUriArr[i + 1]);
            }
        }
        
        int i = 0;
        while (!request.isDispatched() && i++<100) {
            for(Abstract router : this.routers ) {
                if (router.match()) {
                    break;
                }
            }
        }
        return this;
    }
    
    public Request getRequest()
    {
        return ZzContext.getContext().getRequest();
    }

    public Response getResponse()
    {
        return ZzContext.getContext().getResponse();
    }

    public List<Abstract> getRouters() {
        return routers;
    }
    
    public void addRouter(Abstract abstractRouter) {
        this.routers.add(abstractRouter);
    }

}
