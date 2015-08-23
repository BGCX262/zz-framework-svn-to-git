package com.youthor.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.xml.DOMConfigurator;

import com.youthor.zz.common.ZzBenchmark;
import com.youthor.zz.common.ZzApp;
import com.youthor.zz.common.ZzContext;
import com.youthor.zz.core.models.http.Request;
import com.youthor.zz.core.models.http.Response;
import com.youthor.zz.common.util.StringUtil;

public class FrontFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        String appPath = servletContext.getRealPath("/") ;
        ZzApp.setAppPath(appPath);
        String log4jFileFile = filterConfig.getInitParameter("log4j");
        String appendPath = filterConfig.getInitParameter("append_path");
        if (StringUtil.isBlank(appendPath)) {
            appendPath = "";
        }
        ZzApp.setAppendPath(appendPath);
        String filePath = appPath + log4jFileFile;
        DOMConfigurator.configure(filePath);
        String suffix = filterConfig.getInitParameter("suffix");
        ZzApp.setSuffix(suffix);

        ZzApp.init();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
    throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        
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
        Request zzRequest = new Request();
        zzRequest.setOriPathInfo(uri);
        zzRequest.setHttpRequest(request);
        
        String [] oriUriArr = StringUtil.split(uri, "/");
        
        if (oriUriArr != null) {
            if (oriUriArr.length >= 3) {
                zzRequest.setFrontName(oriUriArr[0]);
                zzRequest.setControllerName(oriUriArr[1]);
                zzRequest.setActionName(oriUriArr[2]);
            }
            if (oriUriArr.length == 2) {
                zzRequest.setFrontName(oriUriArr[0]);
                zzRequest.setControllerName(oriUriArr[1]);
                zzRequest.setActionName("index");
        }
        if (oriUriArr.length == 1) {
            zzRequest.setFrontName(oriUriArr[0]);
            zzRequest.setControllerName("index");
            zzRequest.setActionName("index");
            }
        }

        int l=oriUriArr.length;
        for (int i=3; i<l; i+=2) {
            if ((i + 1) < oriUriArr.length) {
                zzRequest.setParam(oriUriArr[i], oriUriArr[i + 1]);
            }
        }

        context.setRequest(zzRequest);
        Response zzResponse = context.getResponse();
        zzResponse.setHttpResponse(response);
        
        ZzBenchmark.start("Whole Request");
        chain.doFilter(request, response);
        ZzBenchmark.stop("Whole Request");
        System.out.println("time = " + ZzBenchmark.fetchTime("Whole Request") + " memory = " + ZzBenchmark.fetchMemory("Whole Request"));

        PrintWriter out = response.getWriter();
        out.println(zzResponse.getBody());
        ZzContext.end();
    }

    @Override
    public void destroy() {
        ZzApp.clear();
        
    }
}
