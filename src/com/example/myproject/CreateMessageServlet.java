package com.example.myproject;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * Servlet implementation class CreateMessageServlet
 */
public class CreateMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Internal Post Creation Servlet
		// Creates a new post and assigns values to
		// "messageID", "user_id", "text", "tags", and "likes"
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	    String un = req.getParameter("user_id");
	    String msg = req.getParameter("text");
	    String tags = req.getParameter("tags");
	    
	    ArrayList<String> tagList = parseTags(tags);
	    
	    Entity message = new Entity("Post");
	    String likes = "0";
	    long uniqueId = datastore.allocateIds("Post", 1).getStart().getId();
	    
	    message.setProperty("messageID", uniqueId);
	    message.setProperty("user_id", un);
	    message.setProperty("text", msg);
	    message.setProperty("tags", tagList);
	    message.setProperty("likes", likes);
	    
	    datastore.put(message);

	    resp.sendRedirect("/MessageList.jsp");
	}
	
	//assumes all tags are given as a string, separated by spaces
	//returns an ArrayList of all tags present
	public ArrayList<String> parseTags(String tags)
	{
		ArrayList<String> tagList = new ArrayList<String>();
		String[] preList = tags.split(" ");
		for(int i = 0; i<preList.length;i++)
		{
			tagList.add(preList[i]);
		}
		
		return tagList;
	}

}
