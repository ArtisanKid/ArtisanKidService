package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.Scroll;
import com.google.gson.Gson;

/**
 * Servlet implementation class ScrollDetailServlet
 */
@WebServlet("/magic/scroll/detail")
public class ScrollDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScrollDetailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String scrollID = request.getParameter("scrollID");
		
		Scroll scroll = new ScrollDao().selectByScrollID(scrollID);
		
		ResponseClass<Scroll> commonResponse = new ResponseClass<Scroll>();
		commonResponse.setData(scroll);
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
