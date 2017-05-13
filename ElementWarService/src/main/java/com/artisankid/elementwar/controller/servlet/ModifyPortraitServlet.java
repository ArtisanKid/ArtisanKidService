package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.controller.utils.Error;
import com.artisankid.elementwar.controller.utils.ErrorEnum;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.UserDao;
import com.google.gson.Gson;

/**
 * Servlet implementation class ModifyPortraitServlet
 */
@WebServlet("/user/modifyPortrait")
public class ModifyPortraitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyPortraitServlet() {
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

		//TODO:newPortrait是一个图片的二进制数据对象，需要处理才能使用
		String portrait = request.getParameter("newPortrait");
		if(portrait == null) {
			portrait = request.getParameter("newPortraitURL");
		}
		if(portrait == null) {
			portrait = request.getParameter("newPortraitID");
		}

		if(portrait == null || portrait.isEmpty()) {
			String json = Error.ErrorToJSON(ErrorEnum.RequestParamLack);
			response.getWriter().append(json);
			return;
		}

		MagicianDao dao = new MagicianDao();
		Magician magician = dao.selectByOpenID(openID);
		magician.setPortrait(portrait);
		magician.setLargePortrait(portrait);
		magician.setSmallPortrait(portrait);
		dao.update(magician);
		
		HashMap<String, String> portraits = new HashMap<>();
		portraits.put("portrait", portrait);
		portraits.put("largePortrait", portrait);
		portraits.put("smallPortrait", portrait);
		
		ResponseClass<HashMap<String, String>> commonResponse = new ResponseClass<>();
		commonResponse.setData(portraits);
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
