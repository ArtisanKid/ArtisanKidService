package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.UserDao;
import com.artisankid.elementwar.common.ewmodel.User;
import com.google.gson.Gson;

/**
 * Servlet implementation class UserInformationServlet
 */
@WebServlet("/user/information")
public class UserInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInformationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String openID = request.getParameter("openID");
		
		Magician magician = new MagicianDao().selectByOpenID(openID);
		
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
