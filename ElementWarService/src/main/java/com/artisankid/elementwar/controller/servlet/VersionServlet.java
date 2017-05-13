package com.artisankid.elementwar.controller.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artisankid.elementwar.controller.utils.Error;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.artisankid.elementwar.common.dao.VersionDao;
import com.artisankid.elementwar.common.ewmodel.Version;
import com.google.gson.Gson;

/**
 * Servlet implementation class VersionServlet
 */
@WebServlet("/version")
public class VersionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VersionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		VersionDao dao = new VersionDao();
		Version releaseVersion = dao.selectLatestRelease();
		Version resourceVersion = dao.selectLatestResource();

		HashMap<String, Version> versions = new HashMap<>();
		versions.put("release", releaseVersion);
		versions.put("resource", resourceVersion);

		ResponseClass<HashMap<String, Version>> commonResponse = new ResponseClass<HashMap<String, Version>>();
		commonResponse.setData(versions);
    	String json = new Gson().toJson(commonResponse);
    	response.getWriter().append(json);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
