package com.artisankid.elementwar.controller.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 业务过滤器
 *
 * Created by WangShaohua on 2017/5/2.
 */
public class BusinessServletFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(BusinessServletFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //TODO 过滤逻辑
        logger.error("filter------------------------------------");

    }

    @Override
    public void destroy() {

    }
}
