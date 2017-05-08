package com.artisankid.elementwar.controller.servlet;

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
import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/16.
 */
@WebServlet("/magic/scroll/conditions")
public class ConditionsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConditionsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String conditionID = request.getParameter("conditionID");
        String formulaID = request.getParameter("formulaID");

        ConditionDao dao = new ConditionDao();
        if (conditionID != null) {
            Condition object = dao.selectByConditionID(conditionID);

            ResponseClass<Condition> commonResponse = new ResponseClass<>();
            commonResponse.setData(object);
            String json = new Gson().toJson(commonResponse);
            response.getWriter().append(json);
        } else if (formulaID != null) {
            List<Condition> objectList = dao.selectByFormulaID(formulaID);

            ResponseClass<List<Condition>> commonResponse = new ResponseClass<>();
            commonResponse.setData(objectList);
            String json = new Gson().toJson(commonResponse);
            response.getWriter().append(json);
        } else {
            List<Condition> objectList = dao.selectAll();

            ResponseClass<List<Condition>> commonResponse = new ResponseClass<>();
            commonResponse.setData(objectList);
            String json = new Gson().toJson(commonResponse);
            response.getWriter().append(json);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
