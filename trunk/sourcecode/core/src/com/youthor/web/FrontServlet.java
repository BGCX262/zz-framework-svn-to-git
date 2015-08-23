package com.youthor.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzConstant;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.common.ZzObject;
import com.youthor.zz.core.models.http.Request;
import com.youthor.zz.core.models.http.Response;
import com.youthor.zz.common.util.StringUtil;

public class FrontServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    private static final long serialVersionUID = 2834711038717133362L;
    private Log log = LogFactory.getLog(this.getClass().getName());  
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String appPath = this.getServletContext().getRealPath("/") ;
        ZzApp.setAppPath(appPath);
        
        String file = this.getServletConfig().getInitParameter("log4j");
        String appendPath = this.getServletConfig().getInitParameter("append_path");
        if (StringUtil.isBlank(appendPath)) {
            appendPath = "";
        }
        ZzApp.setAppendPath(appendPath);
        String filePath = appPath + file;
        DOMConfigurator.configure(filePath);
        
        String suffix = this.getServletConfig().getInitParameter("suffix");
        ZzApp.setSuffix(suffix);
        
        ZzApp.init();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        ZzContext context = ZzContext.getContext();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String suffix = ZzApp.getSuffix();
        if (!StringUtil.isBlank(suffix) && !suffix.startsWith("/")) {
            uri = request.getRequestURI();
            if (uri != null && uri.endsWith(suffix)) {
                uri = uri.substring(contextPath.length(), uri.length() - suffix.length());
            }
        }
        else {
           String needTrim = contextPath + suffix;
           uri = uri.substring(needTrim.length());
        }
        if (uri != null && uri.startsWith("/")) {
            uri = uri.substring(1, uri.length());
        }
        Request zzRequest = (Request)ZzApp.getModel("core/http_request");
        zzRequest.setOriPathInfo(uri);
        zzRequest.setHttpRequest(request);
        context.setRequest(zzRequest);
        Response zzResponse = context.getResponse();
        zzResponse.setHttpResponse(response);
        
        //3 session的处理
        HttpSession session = request.getSession();
        
        if (session.getAttribute(ZzConstant.zzSessionFlag) == null) {
            session.setAttribute(ZzConstant.zzSessionFlag, new HashMap<String, ZzObject>());
        }
        context.addObject(ZzConstant.zzSessionFlag, session.getAttribute(ZzConstant.zzSessionFlag));

        ZzApp.run();

        PrintWriter out = response.getWriter();
        out.println(zzResponse.getBody());
        ZzContext.end();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
