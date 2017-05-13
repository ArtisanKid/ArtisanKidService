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
import com.artisankid.elementwar.common.dao.EpicDao;
import com.artisankid.elementwar.common.ewmodel.Epic;
import com.google.gson.Gson;

/**
 * Servlet implementation class EpicDetailApi
 */
@WebServlet("/epic/detail")
public class EpicDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EpicDetailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String epicID = request.getParameter("epicID");
		if(epicID == null || epicID.isEmpty()) {
			String json = Error.ErrorToJSON(ErrorEnum.RequestParamLack);
			response.getWriter().append(json);
			return;
		}

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
		
		List<Epic> epics = new EpicDao().selectByEpicID(epicID, offset, pageSize);
		
		ResponseClass<List<Epic>> commonResponse = new ResponseClass<>();
		commonResponse.setData(epics);
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
