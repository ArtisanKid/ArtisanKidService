package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.LevelDao;
import com.artisankid.elementwar.common.ewmodel.Level;
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
@WebServlet("/level/insert")
public class InsertLevelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertLevelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        Level object = new Level();
        object.setLevelID(request.getParameter("levelID"));
        object.setEname(request.getParameter("cname"));
        object.setEname(request.getParameter("ename"));

        new LevelDao().insert(object);

        ResponseClass<Level> commonResponse = new ResponseClass<>();
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
