package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.dao.ElementDao;
import com.artisankid.elementwar.common.ewmodel.*;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/16.
 */
@WebServlet("/card/insert")
public class InsertCardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertCardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String cardID = request.getParameter("cardID");

        Card object = new Card();
        object.setCardID(cardID);
        object.setWitticism(request.getParameter("witticism"));
        object.setDetail(request.getParameter("detail"));

        String elementID = request.getParameter("elementID");
        Element element = new Element();
        element.setElementID(elementID);
        object.setElement(element);

        String effectIDs = request.getParameter("effectIDs");
        if(effectIDs != null) {
            List<String> effectIDList = Arrays.asList(effectIDs.split(","));
            List<Effect> effectList = new ArrayList<>();
            for (String effectID : effectIDList) {
                Effect effect = new Effect();
                effect.setEffectID(effectID);
                effectList.add(effect);
            }
            object.setEffects(effectList);
        }

        CardDao dao = new CardDao();
        if(dao.selectByCardID(cardID) != null) {
            dao.update(object);
        } else {
            dao.insert(object);
        }

        ResponseClass<Card> commonResponse = new ResponseClass<>();
        commonResponse.setData(object);
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
