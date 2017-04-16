package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.EffectDao;
import com.artisankid.elementwar.common.ewmodel.Effect;
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
@WebServlet("/effects")
public class EffectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EffectServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String objectID = request.getParameter("effectID");
        if(objectID != null) {
            Effect object =  new EffectDao().selectByEffectID(objectID);

            ResponseClass<Effect> commonResponse = new ResponseClass<>();
            commonResponse.setData(object);
            String json = new Gson().toJson(commonResponse);
            response.getWriter().append(json);
        } else {
            List<Effect> objectList =  new EffectDao().selectAll();

            ResponseClass<List<Effect>> commonResponse = new ResponseClass<>();
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
