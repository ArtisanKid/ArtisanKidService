package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.controller.utils.Error;
import com.artisankid.elementwar.controller.utils.ErrorEnum;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.BaseScroll;
import com.google.gson.Gson;

/**
 * Servlet implementation class ScrollsServlet
 */
@WebServlet("/magic/scrolls")
public class ScrollsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScrollsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String levelID = request.getParameter("levelID");
		String reactionID = request.getParameter("reactionID");

		if((levelID == null || levelID.isEmpty())
				|| (reactionID == null || reactionID.isEmpty())) {
			String json = Error.ErrorToJSON(ErrorEnum.RequestParamLack);
			response.getWriter().append(json);
			return;
		}
		
		List<BaseScroll> scrolls = new ScrollDao().select(levelID, reactionID);
		
		ResponseClass<List<BaseScroll>> commonResponse = new ResponseClass<List<BaseScroll>>();
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
