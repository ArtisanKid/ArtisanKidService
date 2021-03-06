package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
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
import com.artisankid.elementwar.common.ewmodel.User;
import com.google.gson.Gson;

/**
 * Servlet implementation class ModifyMottoServlet
 */
@WebServlet("/user/modifyMotto")
public class ModifyMottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyMottoServlet() {
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

		String motto = request.getParameter("newMotto");
		if(motto == null || motto.isEmpty()) {
			String json = Error.ErrorToJSON(ErrorEnum.RequestParamLack);
			response.getWriter().append(json);
			return;
		}

		MagicianDao dao = new MagicianDao();
		Magician magician = dao.selectByOpenID(openID);
		magician.setMotto(motto);
		dao.update(magician);
		
		ResponseClass<Object> commonResponse = new ResponseClass<>();
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
