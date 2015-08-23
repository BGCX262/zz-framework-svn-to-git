package com.youthor.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


import com.youthor.zz.common.ZzApp;

public class DispatchFilter implements Filter {

    @Override
    public void destroy() {
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
    throws IOException, ServletException {
        ZzApp.run();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
