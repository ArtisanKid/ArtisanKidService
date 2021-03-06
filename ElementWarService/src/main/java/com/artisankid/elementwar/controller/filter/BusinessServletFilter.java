package com.artisankid.elementwar.controller.filter;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.ewmodel.Token;
import com.artisankid.elementwar.controller.utils.Error;
import com.artisankid.elementwar.controller.utils.ErrorEnum;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        if(request.getRequestURI().equals("/login")) {
            //通过
        } else if(request.getRequestURI().equals("/token/refresh")) {
            //通过
        } else {
            //验证openID是否有效
            String openID = request.getParameter("openID");
            if(openID == null || openID.isEmpty()) {
                String json = Error.ErrorToJSON(ErrorEnum.OpenIDCantNil);
                response.getWriter().append(json);
                return;
            }

            MagicianDao magicianDao = new MagicianDao();
            if (magicianDao.selectByOpenID(openID) == null) {
                String json = Error.ErrorToJSON(ErrorEnum.OpenIDNotExist);
                response.getWriter().append(json);
                return;
            }

            String accessToken = request.getParameter("accessToken");
            if(accessToken == null || accessToken.isEmpty()) {
                String json = Error.ErrorToJSON(ErrorEnum.AccessTokenCantNil);
                response.getWriter().append(json);
                return;
            }

            TokenDao tokenDao = new TokenDao();
            if (tokenDao.selectByAccessToken(accessToken) == null) {
                String json = Error.ErrorToJSON(ErrorEnum.AccessTokenNotExist);
                response.getWriter().append(json);
                return;
            }
        }
//        过滤器使用
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
