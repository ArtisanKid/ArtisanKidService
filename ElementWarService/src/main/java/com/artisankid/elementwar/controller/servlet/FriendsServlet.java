package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.UserDao;
import com.artisankid.elementwar.common.ewmodel.BaseUser;
import com.artisankid.elementwar.common.ewmodel.BaseUser.UserRelation;
import com.google.gson.Gson;

/**
 * Servlet implementation class FriendsServlet
 */
@WebServlet("/magician/friends")
public class FriendsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int offset = Integer.parseInt(request.getParameter("offset"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		if(pageSize == 0) {
			pageSize = 20;
		}
		
		List<BaseUser> users = new UserDao().selectByRelation(UserRelation.Friend, offset, pageSize);
		
		ResponseClass<List<BaseUser>> commonResponse = new ResponseClass<List<BaseUser>>();
		commonResponse.setData(users);
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
