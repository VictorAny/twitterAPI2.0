package com.example.myproject;
import com.google.appengine.api.datastore.DatastoreService;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.*;

@SuppressWarnings("serial")
public class UserServlet extends TwitterAPI2Servlet{
//	private static final long serialVersionUID = 1L;
	
	public static String UserType = "User";
	public static String nameParam = "name";
	public static String userHandlerParam = "userHandler";
	public static String userIDParam = "user_id";
	public static String postCountParam = "post_count";
	
    public UserServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatastoreService datastore = 
                DatastoreServiceFactory.getDatastoreService();
		String userID = request.getQueryString();
		System.out.println(userID);
		if (userID != null){
			response.setContentType("application/json");
			Key userKey = KeyFactory.createKey(UserServlet.UserType, Integer.parseInt(userID));
			Entity userProfile;
			try {
				userProfile = datastore.get(userKey);
				Gson json = new Gson();
				String returnString = json.toJson(userProfile);
				response.getWriter().println(returnString);

			} catch (EntityNotFoundException e) {
				this.writeErrorResponse(response, "Error, user does not exist");
			}
		}else{
			//Uncomment if your datastore is empty.
			//Reminder you can view your datastore at http:localhost:xxxx/_ah/admin where xxxx is your port
			Entity testEnt = new Entity(UserServlet.UserType);
			testEnt.setProperty(UserServlet.nameParam, "Victor");
			testEnt.setProperty(UserServlet.userHandlerParam , "Coldsoldier");
			testEnt.setProperty(UserServlet.userIDParam, 12432);
			datastore.put(testEnt);
			this.writeErrorResponse(response, "Error no user ID found");
		}
	}
		

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//First thing we'll do is convert the requestBody to JSON dictionary so we can query
		//based on our keys
		HashMap<String, Object> requestDict = this.getRequestBodyMap(request);
		//Take it away!
		//String requestParam = (String)requestDict.get("name");
		if (requestDict == null){
			this.writeErrorResponse(response, "Invalid JSON String");
			return;
		}
		System.out.println("Valid json, now parsing userinformation");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
		boolean isValid = true;
		
		String[] requestParam = new String [2];
		requestParam [0] = (String)requestDict.get(UserServlet.nameParam);
		requestParam [1] = (String)requestDict.get(UserServlet.userHandlerParam);
		// Creates unique ID from database. 
		long uniqueId = datastore.allocateIds(UserServlet.UserType, 1).getStart().getId();
		// A newly created user will have no posts hence the postCount is 0.
		int postCount = 0;
		Entity user = new Entity (UserServlet.UserType, uniqueId);
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
		user.setProperty(UserServlet.nameParam, requestParam[0]);
		user.setProperty(UserServlet.userHandlerParam, requestParam[1]);
		user.setProperty(UserServlet.userIDParam, uniqueId);
		user.setProperty(UserServlet.postCountParam, postCount);
		datastore.put(user);
		this.writeSucessfulResponse(response, Long.toString(uniqueId));
		}
		else
			this.writeErrorResponse(response, "Invalid request parameters");
	}
	

	

}
