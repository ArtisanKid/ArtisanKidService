package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.LevelDao;
import com.artisankid.elementwar.common.ewmodel.Level;
import com.google.gson.Gson;

/**
 * Servlet implementation class ScrollLevelsServlet
 */
@WebServlet("/magic/scroll/levels")
public class ScrollLevelsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ScrollLevelsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Level> levels = new LevelDao().selectAll();

		ResponseClass<List<Level>> commonResponse = new ResponseClass<List<Level>>();
		commonResponse.setData(levels);
		String json = new Gson().toJson(commonResponse);
		response.getWriter().append(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
