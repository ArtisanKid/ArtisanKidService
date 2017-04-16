package com.artisankid.elementwar.controller.Test;

import com.artisankid.elementwar.common.dao.ElementDao;
import com.artisankid.elementwar.common.ewmodel.Atom;
import com.artisankid.elementwar.common.ewmodel.Element;
import com.artisankid.elementwar.common.ewmodel.Element.ElementType;
import com.artisankid.elementwar.common.ewmodel.Molecule;
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
@WebServlet("/element/insert")
public class InsertElementServlet  extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertElementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String elementID = request.getParameter("elementID");
        ElementType type = ElementType.enumOf(Integer.parseInt(request.getParameter("type")));
        Element object;
        if(type == ElementType.Atom) {
            Atom _object = new Atom();
            _object.setRadius(Integer.parseInt(request.getParameter("radius")));
            _object.setColor(request.getParameter("color"));
            object = _object;
        } else {
            object = new Molecule();
        }
        object.setElementID(elementID);
        object.setType(type);
        object.setName(request.getParameter("name"));
        object.setCname(request.getParameter("cname"));
        object.setEname(request.getParameter("ename"));
        object.setDim2(request.getParameter("dim2"));
        object.setDim3(request.getParameter("dim3"));
        object.setWeight(Integer.parseInt(request.getParameter("weight")));
        object.setDetail(request.getParameter("detail"));

        ElementDao dao = new ElementDao();
        if(dao.selectByElementID(elementID) != null) {
            if(type == ElementType.Atom) {
                dao.update((Atom) object);
            } else {
                dao.update((Molecule) object);
            }
        } else {
            if(type == ElementType.Atom) {
                dao.insert((Atom) object);
            } else {
                dao.insert((Molecule) object);
            }
        }

        ResponseClass<Element> commonResponse = new ResponseClass<>();
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
