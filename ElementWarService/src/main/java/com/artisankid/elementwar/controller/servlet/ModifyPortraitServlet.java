package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.common.ewmodel.Magician;
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
		// TODO Auto-generated method stub
		String openID = request.getParameter("openID");
		String portrait = request.getParameter("portrait");
		
		UserDao dao = new UserDao();
		Magician magician = dao.selectByOpenID(openID);
		magician.setPortrait(portrait);
		dao.update(magician);
		
		HashMap<String, String> portraits = new HashMap<>();
		//这里需要设置大中小三个头像url
		
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
