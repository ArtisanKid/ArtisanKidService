package com.artisankid.elementwar.controller.servlet;

import com.artisankid.elementwar.common.dao.FormulaDao;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.Balance;
import com.artisankid.elementwar.common.ewmodel.BaseScroll;
import com.artisankid.elementwar.common.ewmodel.Formula;
import com.artisankid.elementwar.common.ewmodel.Scroll;
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
 * Created by LiXiangYu on 2017/5/7.
 */
@WebServlet("/magic/scroll/verify")
public class VerifyScrollServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyScrollServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String reactantCountsParam = request.getParameter("reactants");
        List<String> reactantCounts = Arrays.asList(reactantCountsParam.split(";"));
        List<Balance> reactants = new ArrayList<>();
        for(String reactantCount : reactantCounts) {
            List<String> params = Arrays.asList(reactantCount.split(":"));
            Balance balance = new Balance();
            balance.setElementID(params.get(0).toString());
            balance.setCount(Integer.parseInt(params.get(1).toString()));
            reactants.add(balance);
        }

        String conditionParam = request.getParameter("conditionIDs");
        List<String> conditionIDs = Arrays.asList(conditionParam.split(";"));

        String resultantCountParam = request.getParameter("resultants");
        List<Balance> resultants = null;
        if(resultantCountParam != null) {
            List<String> resultantCounts = Arrays.asList(resultantCountParam.split(";"));
            resultants = new ArrayList<>();
            for (String resultantCount : resultantCounts) {
                List<String> params = Arrays.asList(resultantCount.split(":"));
                Balance balance = new Balance();
                balance.setElementID(params.get(0).toString());
                balance.setCount(Integer.parseInt(params.get(1).toString()));
                resultants.add(balance);
            }
        }

        Formula formula = null;
        if(resultants != null) {
            formula = new FormulaDao().select(reactants, conditionIDs, resultants);
        } else {
            formula = new FormulaDao().select(reactants, conditionIDs);
        }

        Scroll scroll = null;
        if(formula != null) {
            scroll = new ScrollDao().selectByFormulaID(formula.getFormulaID());
        }

        ResponseClass<Scroll> commonResponse = new ResponseClass<>();
        if(formula != null) {
            commonResponse.setData(scroll);
        }
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
