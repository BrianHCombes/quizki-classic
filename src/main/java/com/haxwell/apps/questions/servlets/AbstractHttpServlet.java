package com.haxwell.apps.questions.servlets;

import java.io.IOException;	
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AbstractHttpServlet
 */
public abstract class AbstractHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(AbstractHttpServlet.class.getName());

//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//	}
	
    protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response, String jsp) throws IOException, ServletException
	{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
		dispatcher.forward(request,response);
	}
    
    protected void redirectToJSP(HttpServletRequest request, HttpServletResponse response, String jsp) throws IOException, ServletException
	{
    	String path = /*Constants.APP_URL_ROOT + */ jsp;
    	
    	log.log(Level.INFO, "Attempting redirect to : " + path);
    	
    	response.sendRedirect(path);
	}
}