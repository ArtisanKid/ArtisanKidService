package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.dao.UserDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.ewmodel.User;
import com.artisankid.elementwar.common.ewmodel.User.LoginType;
import com.artisankid.elementwar.common.ewmodel.Token;
import com.artisankid.elementwar.common.utils.TokenManager;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;
/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		user.setLoginType(LoginType.enumOf(Integer.parseInt(request.getParameter("platform"))));
		user.setUnionID(request.getParameter("unionID"));
		user.setOpenID(request.getParameter("openID"));
		user.setNickname(request.getParameter("nickname"));
		user.setPortrait(request.getParameter("portrait"));
		user.setLargePortrait(request.getParameter("largePortrait"));
		user.setSmallPortrait(request.getParameter("smallPortrait"));
		user.setMobile(request.getParameter("mobile"));
		user.setMotto(request.getParameter("motto"));

		Token userToken = new Token();
		userToken.setAccessToken(request.getParameter("accessToken"));
		userToken.setRefreshToken(request.getParameter("refreshToken"));
		Double userTokenExpiredTime = Double.parseDouble(request.getParameter("expiredTime")) * 1000;
		userToken.setExpiredTime(userTokenExpiredTime.longValue());
		user.setToken(userToken);

		UserDao userDao = new UserDao();
		User existUser = userDao.selectByUnionID(user.getUnionID(), user.getLoginType());
		String userID;
		if(existUser == null) {
			userID = userDao.insert(user);
		} else {
			user.setUserID(existUser.getUserID());
			userID = userDao.update(user);
		}

		MagicianDao magicianDao = new MagicianDao();
		Magician magician = magicianDao.selectByUserID(userID);

		String openID = magician.getOpenID();

		Token magicianToken = new Token();
		magicianToken.setAccessToken(TokenManager.CreateAccessToken(openID));
		magicianToken.setRefreshToken(TokenManager.CreateRefreshToken(openID));
		Long magicianTokenExpiredTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
		magicianToken.setExpiredTime(magicianTokenExpiredTime);

		TokenDao tokenDao = new TokenDao();
		tokenDao.insert(openID, magicianToken);

		magician.setToken(magicianToken);
		
    	ResponseClass<Magician> commonResponse = new ResponseClass<>();
		commonResponse.setData(magician);
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
