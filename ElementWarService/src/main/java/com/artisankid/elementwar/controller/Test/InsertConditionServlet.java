package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.ConditionDao;
import com.artisankid.elementwar.common.ewmodel.Condition;
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
@WebServlet("/condition/insert")
public class InsertConditionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertConditionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String conditionID = request.getParameter("conditionID");

        Condition object = new Condition();
        object.setConditionID(conditionID);
        object.setCname(request.getParameter("cname"));
        object.setEname(request.getParameter("ename"));

        ConditionDao dao = new ConditionDao();
        if(dao.selectByConditionID(conditionID) != null) {
            dao.update(object);
        } else {
            dao.insert(object);
        }

        ResponseClass<Condition> commonResponse = new ResponseClass<>();
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
