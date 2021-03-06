package com.servlets;
/*
 * author Clarissa Mercado
 * Connects CustLogin to local database and send to ApplicationForm servlet
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogIn
 */
@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpSession session = null;
	DatabaseConnection connect = new DatabaseConnection();
	Connection connection = connect.getConnection();
   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("<h1>Login page creation in process<h1/> ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//getting parameters from the login form from CustLogin.jsp
		String email=request.getParameter("email");
		String psw=request.getParameter("psw");
		System.out.println("Get email as parameter: " + email);
		
		//creating session
		session = request.getSession();
		
		//declaring objects
		Statement stmt=null;
		ResultSet rs = null;
		
		try{
			
			//String sqlQuery = "SELECT email FROM customer_info where email='"+ email +"';";
			String sqlQuery= "SELECT * FROM customer_info where email= '"+email+"' and password= '"+psw+"';";
			//running above in mySQL
			stmt = connection.createStatement();
			//saving data fetched from above in the resultset object 
			rs = stmt.executeQuery(sqlQuery);
			System.out.println("Ran query");
			
			//code by Son-Rossy
			if(rs.next()){
				//if there is a result then it means the user successfully logged in
				String userEmail=rs.getString(10);
				String first_name=rs.getString(2);
				String last_name=rs.getString(3);
				System.out.println("Email from the database: " + userEmail);
				
				//usage of session
				session.setAttribute("email", userEmail);
				session.setAttribute("first_name", first_name);
				session.setAttribute("last_name", last_name);
				
				//connect to other servlet 
				
				/*RequestDispatcher rd = request.getRequestDispatcher("ApplicationForm");
				rd.forward(request, response);*/
				
				/*//redirect to Application HTML
				String redirectURL = "ApplicationForm";*/
				response.sendRedirect("index.jsp");
				
			}
			else{
				session.setAttribute("msg", "Invalid user name or password");
				//send them back to login page
				response.sendRedirect("CustLogin.jsp");
			}
			
		}catch(Exception e){
			System.out.print("Cause: "+ e.getMessage());
			
		}
		//response.getWriter().append("<h1>Login page creation in process<h1/> ").append(request.getContextPath());
	}

}
