package com.artisankid.elementwar.controller.servlet;

import com.artisankid.elementwar.common.dao.FormulaDao;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.Balance;
import com.artisankid.elementwar.common.ewmodel.BaseScroll;
import com.artisankid.elementwar.common.ewmodel.Formula;
import com.artisankid.elementwar.common.ewmodel.Scroll;
import com.artisankid.elementwar.controller.utils.Error;
import com.artisankid.elementwar.controller.utils.ErrorEnum;
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
        String conditionParam = request.getParameter("conditionIDs");
        String resultantCountParam = request.getParameter("resultants");

        if((reactantCountsParam == null || reactantCountsParam.isEmpty())
                || (conditionParam == null || conditionParam.isEmpty())
                || (resultantCountParam == null || resultantCountParam.isEmpty())) {
            String json = Error.ErrorToJSON(ErrorEnum.RequestParamLack);
            response.getWriter().append(json);
            return;
        }

        List<Balance> reactants = convertToBalances(reactantCountsParam);
        List<String> conditionIDs = Arrays.asList(conditionParam.split(";"));

        List<Balance> resultants = null;
        if(resultantCountParam != null) {
            resultants = convertToBalances(resultantCountParam);
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

    List<Balance> convertToBalances(String param) {
        List<String> counts = Arrays.asList(param.split(";"));
        List<Balance> balances = new ArrayList<>();
        for(String reactantCount : counts) {
            List<String> params = Arrays.asList(reactantCount.split(":"));
            Balance balance = new Balance();
            balance.setElementID(params.get(0).toString());
            balance.setCount(Integer.parseInt(params.get(1).toString()));
            balances.add(balance);
        }
        return balances;
    }
}
