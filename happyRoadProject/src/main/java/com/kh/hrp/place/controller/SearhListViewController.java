package com.kh.hrp.place.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.hrp.common.PageInfo;
import com.kh.hrp.common.PageInfoController;
import com.kh.hrp.place.model.service.PlaceService;
import com.kh.hrp.place.model.vo.Place;

/**
 * Servlet implementation class SearhListView
 */
@WebServlet("/search.sc")
public class SearhListViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

    public SearhListViewController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String placeTitle = request.getParameter("title");
		String listNull;
		if (placeTitle == "") {
			listNull = "0";
			PageInfo pi =  PageInfoController.pageController(0, 1, 10, 5);
			request.setAttribute("listNull", listNull);
			request.setAttribute("pi", pi);
			request.getRequestDispatcher("views/common/searchListView.jsp").forward(request, response);
		} else {
			listNull = "1";
			int searchCount = new PlaceService().selectSearchCount(placeTitle);
			int currentPage = Integer.parseInt(request.getParameter("cpage"));
			
			System.out.println("222" + searchCount);
			
			PageInfo pi = PageInfoController.pageController(searchCount, currentPage, 10, 5);
			ArrayList<Place> list = new PlaceService().selectSearchList(placeTitle, pi);
			request.setAttribute("listNull", listNull);
			request.setAttribute("list", list);
			request.setAttribute("pi", pi);
			
			request.getRequestDispatcher("views/common/searchListView.jsp").forward(request, response);
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
