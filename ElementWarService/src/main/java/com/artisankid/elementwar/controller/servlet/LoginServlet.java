package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.FormulaDao;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.Formula;
import com.artisankid.elementwar.common.ewmodel.Scroll;
import com.google.gson.Gson;
/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

//    @Override
//    protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
//    	// TODO Auto-generated method stub
//    	super.service(arg0, arg1);
//    	System.out.println(arg0.getParameter("name"));
//    	System.out.println(arg0.getParameter("pwd"));
//    	
//    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		FormulaDao dao = new FormulaDao();
		ArrayList<String> reactantIDs = new ArrayList<String>();
		reactantIDs.add("2");
		reactantIDs.add("11");
		ArrayList<String> conditionIDs = new ArrayList<String>();
		conditionIDs.add("2");
		
		Formula formula = dao.select(reactantIDs, conditionIDs);
		
		ScrollDao scrollDao = new ScrollDao();
		Scroll scroll = scrollDao.selectByScrollID("1");
		
		//ArrayList<BaseUser> users = (ArrayList<BaseUser>)dao.selectByState(ConnectState.Online, 0, 10);
//		System.out.println(users); 
		
		// TODO Auto-generated method stub		
//		JSONObject object = new JSONObject();
//		object.put("username", request.getParameter("name"));
//		object.put("password", request.getParameter("pwd"));
		
    	ResponseClass<Scroll> response_1 = new ResponseClass<Scroll>();
//    	response_1.setResponseTime(123123131);
//    	response_1.setMessage("request successful");
//    	response_1.setCode(1);
//    	Level level = new Level();
//    	level.setName("firstLevel");
//    	level.setLevelID("1");
    	response_1.setData(scroll);
    	String json = new Gson().toJson(response_1);
    	response.getWriter().append(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
