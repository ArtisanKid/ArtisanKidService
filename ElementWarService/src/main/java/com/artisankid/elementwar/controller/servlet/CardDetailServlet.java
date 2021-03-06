package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.controller.utils.Error;
import com.artisankid.elementwar.controller.utils.ErrorEnum;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.ewmodel.Card;
import com.google.gson.Gson;

/**
 * Servlet implementation class CardDetailServlet
 */
@WebServlet("/magic/card/detail")
public class CardDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CardDetailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String cardID = request.getParameter("cardID");

		if(cardID == null || cardID.isEmpty()) {
			String json = Error.ErrorToJSON(ErrorEnum.RequestParamLack);
			response.getWriter().append(json);
			return;
		}

		Card card = new CardDao().selectByCardID(cardID);
		
		ResponseClass<Card> commonResponse = new ResponseClass<>();
		commonResponse.setData(card);
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
