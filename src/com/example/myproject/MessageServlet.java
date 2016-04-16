package com.example.myproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

import com.google.gson.Gson;

/**
 * Servlet implementation class MessageServlet
 */
public class MessageServlet extends TwitterAPI2Servlet {
	private static final long serialVersionUID = 1L;
	private String EntityKind = "Post";
	private String GETmessageIDParam = "messageID";
	private String POSTmessageUserIDParam = "user_id";
	private String POSTmessageTextParam = "text";
	private String POSTmessageLikesParam = "likes";
	
    public MessageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the parameters.
		//Search the datastore for the ID
		//If ID is found, write out the information with a sucessful response
		// else write an error resposne
		DatastoreService datastore = 
                DatastoreServiceFactory.getDatastoreService();
		response.setContentType("application/json");
		
		//Temp. Format Used by the Request Sent by MessageSearch.html
		//Finalize later
		String searchTags = request.getParameter("searchByTag");
		String searchName = request.getParameter("searchByName");
		
		if (searchTags == null || searchName == null){
			this.writeErrorResponse(response, "Failed");
			return;
		}
		
		Gson g = new Gson();
		
		
		Query q;
		ArrayList<String> returnString = new ArrayList<String>();
		String jsonReturn = "";
		boolean fail = true;
		
		//if there was a user_id in the query, first filter out all posts NOT made by that user
		if(!searchName.isEmpty())
		{
			Filter byName = new FilterPredicate("user_id",FilterOperator.EQUAL,searchName);
			q = new Query("Post").setFilter(byName);
		}
		else//no user specified, search all posts
		{
			q = new Query("Post");
		}
		PreparedQuery pq = datastore.prepare(q);

		//further filter the posts by tag
		//return the Json versions of found posts
		for(Entity result : pq.asIterable())
		{

				ArrayList<String> tags = (ArrayList<String>) result.getProperty("tags");
				if(searchTags.isEmpty() || tags.contains(searchTags))
				{
					fail = false;
					String userName = (String) result.getProperty("user_id");
					String msg = (String) result.getProperty("text");
					returnString.add(userName);
					returnString.add(msg);
					jsonReturn = jsonReturn + g.toJson(result.getProperties());
					String toJ = g.toJson(result.getProperties());
					response.getWriter().println(toJ);
				}

		}

		//No Posts found
		if(fail)
		{
			response.getWriter().println("No Messages with That Tag/Username");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, Object> requestDic = this.getRequestBodyMap(request);
		//Go through the keys and check for our required parameters.
		// Allocate a unique key for the post.
		// Set the post parameters with the required values
		// Likes should also be set to 0 for a newly created message.
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		boolean isValid = true;
				
		String[] requestParam = new String [2];
		requestParam [0] = (String)requestDic.get(this.POSTmessageUserIDParam);
		requestParam [1] = (String)requestDic.get(this.POSTmessageTextParam);
				
		long uniqueId = datastore.allocateIds(this.EntityKind, 1).getStart().getId();
				
		int likesCount = 0;
		Entity post = new Entity (this.EntityKind);
		for (int i = 0; i < requestParam.length; ++i)
		{
			if (requestParam [i] == null)
			{
				isValid = false;
				break;
			}
		}
		if (isValid)
		{
			post.setProperty(this.GETmessageIDParam, uniqueId);
			post.setProperty(this.POSTmessageUserIDParam, requestParam[0]);
			post.setProperty(this.POSTmessageTextParam, requestParam[1]);
			post.setProperty(this.POSTmessageLikesParam, likesCount);
			datastore.put(post);
			this.writeSucessfulResponse(response, "Post uploaded!");
		}
		else
			this.writeErrorResponse(response, "Invalid request parameters");
	}
}
