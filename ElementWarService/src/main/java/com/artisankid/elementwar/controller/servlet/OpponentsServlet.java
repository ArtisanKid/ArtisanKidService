package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.UserDao;
import com.artisankid.elementwar.common.ewmodel.BaseMagician;
import com.artisankid.elementwar.common.ewmodel.BaseMagician.UserRelation;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;

/**
 * Servlet implementation class OpponentsServlet
 */
@WebServlet("/magician/opponents")
public class OpponentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpponentsServlet() {
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

		String offsetParam = request.getParameter("offset");
		int offset = 0;
		if(offsetParam != null) {
			offset = Integer.parseInt(offsetParam);
		}

		String pageSizeParam = request.getParameter("pageSize");
		int pageSize = 0;
		if(pageSizeParam != null) {
			pageSize = Integer.parseInt(pageSizeParam);
		}
		if(pageSize == 0) {
			pageSize = 20;
		}
		
		List<BaseMagician> users = new MagicianDao().selectByRelation(openID, UserRelation.Opponent, offset, pageSize);
		
		ResponseClass<List<BaseMagician>> commonResponse = new ResponseClass<>();
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
