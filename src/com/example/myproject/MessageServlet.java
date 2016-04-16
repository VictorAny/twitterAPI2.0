package com.example.myproject;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

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
				
		String uniqueId = Long.toString(datastore.allocateIds(this.EntityKind, 1).getStart().getId());
				
		int likesCount = 0;
		Entity post = new Entity (this.EntityKind, uniqueId);
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
