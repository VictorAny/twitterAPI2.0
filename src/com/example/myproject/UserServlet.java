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

    public UserServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatastoreService datastore = 
                DatastoreServiceFactory.getDatastoreService();
		String userID = request.getQueryString();
		if (userID != null){
			response.setContentType("application/json");
			Key userKey = KeyFactory.createKey("User", userID);
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
			//Uncommet if your datastore is empty.
			//Reminder you can view your datastore at http:localhost:xxxx/_ah/admin where xxxx is your port
//			Entity testEnt = new Entity("User", "123");
//			testEnt.setProperty("name", "Victor");
//			testEnt.setProperty("userName", "Coldsoldier");
//			testEnt.setProperty("user_id", 12432);
//			datastore.put(testEnt);
			this.writeErrorResponse(response, "Error no user ID found");
		}
	}
		

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//First thing we'll do is convert the requestBody to JSON dictionary so we can query
		//based on our keys
		HashMap<String, Object> requestDict = this.getRequestBodyMap(request);
		//Take it away!
		String requestParam = (String)requestDict.get("name");
		
		
	}
	

	

}
