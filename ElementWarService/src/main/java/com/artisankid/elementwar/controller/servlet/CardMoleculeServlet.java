package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.ewmodel.BaseCard;
import com.artisankid.elementwar.common.ewmodel.Element.ElementType;
import com.google.gson.Gson;

/**
 * Servlet implementation class CardMoleculeServlet
 */
@WebServlet("/magic/card/molecules")
public class CardMoleculeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CardMoleculeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

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
		
		List<BaseCard> cards = new CardDao().select(ElementType.Atom, offset, pageSize);
		
		ResponseClass<List<BaseCard>> commonResponse = new ResponseClass<>();
		commonResponse.setData(cards);
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
