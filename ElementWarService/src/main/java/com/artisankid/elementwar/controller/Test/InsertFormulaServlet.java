package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.FormulaDao;
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
@WebServlet("/formula/insert")
public class InsertFormulaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertFormulaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        /*
        * private String formulaID;//方程式ID
    private List<Reaction> reactions;//反应类型
    private List<Balance> reactants;//反应物
    private List<Condition> conditions;//条件
    private List<Balance> resultants;//生成物
    private String phenomenon;//现象
    private String principle;//原理
    private String detail;//描述
        * */

        String formulaID = request.getParameter("formulaID");

        Formula object = new Formula();
        object.setFormulaID(formulaID);
        object.setPhenomenon(request.getParameter("phenomenon"));
        object.setPrinciple(request.getParameter("principle"));
        object.setDetail(request.getParameter("detail"));

        String reactionIDs = request.getParameter("reactionIDs");
        if(reactionIDs != null) {
            List<String> objectIDList = Arrays.asList(reactionIDs.split(","));
            List<Reaction> objectList = new ArrayList<>();
            for (String objectID : objectIDList) {
                Reaction _object = new Reaction();
                _object.setReactionID(objectID);
                objectList.add(_object);
            }
            object.setReactions(objectList);
        }

        String reactantIDs = request.getParameter("reactantIDs");
        if(reactantIDs != null) {
            List<String> objectIDList = Arrays.asList(reactantIDs.split(","));
            List<Balance> objectList = new ArrayList<>();
            for (String objectID : objectIDList) {
                Balance _object = new Balance();
                _object.setBalanceID(objectID);
                objectList.add(_object);
            }
            object.setReactants(objectList);
        }

        String conditionIDs = request.getParameter("conditionIDs");
        if(conditionIDs != null) {
            List<String> objectIDList = Arrays.asList(conditionIDs.split(","));
            List<Condition> objectList = new ArrayList<>();
            for (String objectID : objectIDList) {
                Condition _object = new Condition();
                _object.setConditionID(objectID);
                objectList.add(_object);
            }
            object.setConditions(objectList);
        }

        String resultantIDs = request.getParameter("resultantIDs");
        if(resultantIDs != null) {
            List<String> objectIDList = Arrays.asList(resultantIDs.split(","));
            List<Balance> objectList = new ArrayList<>();
            for (String objectID : objectIDList) {
                Balance _object = new Balance();
                _object.setBalanceID(objectID);
                objectList.add(_object);
            }
            object.setResultants(objectList);
        }

        FormulaDao dao = new FormulaDao();
        if(dao.selectByFormulaID(formulaID) != null) {
            dao.update(object);
        } else {
            dao.insert(object);
        }

        ResponseClass<Formula> commonResponse = new ResponseClass<>();
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
