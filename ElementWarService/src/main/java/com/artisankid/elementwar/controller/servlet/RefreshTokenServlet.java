package com.artisankid.elementwar.controller.servlet;

import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.dao.UserDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.ewmodel.Token;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by LiXiangYu on 2017/4/15.
 */
public class RefreshTokenServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RefreshTokenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accessToken = request.getParameter("accessToken");
        String refreshToken = request.getParameter("refreshToken");

        TokenDao tokenDao = new TokenDao();
        Token token = tokenDao.selectByAccessToken(accessToken);

        if(token == null) {
            ResponseClass<Token> commonResponse = new ResponseClass<>();
            commonResponse.setCode(100901);
            commonResponse.setMessage("access token无效");
            String json = new Gson().toJson(commonResponse);
            response.getWriter().append(json);
            return;
        }

        if(!token.getRefreshToken().equals(refreshToken)) {
            ResponseClass<Token> commonResponse = new ResponseClass<>();
            commonResponse.setCode(100903);
            commonResponse.setMessage("refresh token无效");
            String json = new Gson().toJson(commonResponse);
            response.getWriter().append(json);
            return;
        }

        if(token.getRefreshTokenExpiredTime() <= new Date().getTime()) {
            ResponseClass<Token> commonResponse = new ResponseClass<>();
            commonResponse.setCode(100902);
            commonResponse.setMessage("refresh token过期");
            String json = new Gson().toJson(commonResponse);
            response.getWriter().append(json);
            return;
        }

        token.setAccessToken("这里是一个新的token");
        Long tokenExpiredTime = new Date().getTime() + 24 * 60 * 60 * 1000;
        token.setExpiredTime(tokenExpiredTime);
        tokenDao.update(token);

        ResponseClass<Token> commonResponse = new ResponseClass<>();
        commonResponse.setData(token);
        String json = new Gson().toJson(commonResponse);
        response.getWriter().append(json);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}