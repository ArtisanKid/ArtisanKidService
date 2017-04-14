package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.BaseScroll;
import com.google.gson.Gson;

/**
 * Servlet implementation class ScrollSearchServlet
 */
@WebServlet("/magic/scroll/search")
public class ScrollSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScrollSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String levelID = request.getParameter("levelID");
		String reactionID = request.getParameter("reactionID");
		String keyword = request.getParameter("keyword");
		int offset = Integer.parseInt(request.getParameter("offset"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		if(pageSize == 0) {
			pageSize = 20;
		}
		
		List<BaseScroll> scrolls = new ScrollDao().selectByKeyword(levelID, reactionID, keyword, offset, pageSize);
		
		ResponseClass<List<BaseScroll>> commonResponse = new ResponseClass<>();
		commonResponse.setData(scrolls);
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