package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.ReactionDao;
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
@WebServlet("/reaction/insert")
public class InsertReactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertReactionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        Reaction object = new Reaction();
        object.setReactionID(request.getParameter("reactionID"));
        object.setCname(request.getParameter("name"));

        new ReactionDao().insert(object);

        ResponseClass<Reaction> commonResponse = new ResponseClass<>();
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
