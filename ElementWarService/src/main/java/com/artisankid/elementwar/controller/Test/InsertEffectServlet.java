package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.EffectDao;
import com.artisankid.elementwar.common.dao.ReactionDao;
import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Reaction;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by LiXiangYu on 2017/4/16.
 */
@WebServlet("/effect/insert")
public class InsertEffectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertEffectServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String effectID = request.getParameter("effectID");

        Effect object = new Effect();
        object.setEffectID(effectID);
        object.setCname(request.getParameter("cname"));
        object.setEname(request.getParameter("ename"));
        object.setType(Effect.EffectType.enumOf(Integer.parseInt(request.getParameter("type"))));
        object.setValue(Integer.parseInt(request.getParameter("value")));
        object.setWitticism(request.getParameter("witticism"));

        EffectDao dao = new EffectDao();
        if(dao.selectByEffectID(effectID) != null) {
            dao.update(object);
        } else {
            dao.insert(object);
        }

        ResponseClass<Effect> commonResponse = new ResponseClass<>();
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
