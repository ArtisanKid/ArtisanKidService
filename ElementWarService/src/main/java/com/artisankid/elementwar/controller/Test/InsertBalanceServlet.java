package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.BalanceDao;
import com.artisankid.elementwar.common.ewmodel.Balance;
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
@WebServlet("/balance/insert")
public class InsertBalanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertBalanceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String balanceID = request.getParameter("balanceID");

        Balance object = new Balance();
        object.setBalanceID(balanceID);
        object.setElementID(request.getParameter("elementID"));
        object.setCount(Integer.parseInt(request.getParameter("count")));

        BalanceDao dao = new BalanceDao();
        if(dao.selectByBalanceID(balanceID) != null) {
            dao.update(object);
        } else {
            dao.insert(object);
        }

        ResponseClass<Balance> commonResponse = new ResponseClass<>();
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
