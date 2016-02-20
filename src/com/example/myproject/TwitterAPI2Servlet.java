package com.example.myproject;

import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class TwitterAPI2Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//This is a handler. All this is doing is setting the response content-type to be text.
		//This is needed as you can write multi-media content.
		
		//The validation team will take the raw request, which is the req parameter, and validate it.
		//The handlers team will handle fetching that data and then writting the response. 
		
		DatastoreService datastore =
                DatastoreServiceFactory.getDatastoreService();
		//Should be put in POST requst
		//Also note that user_id becomes exact key, rather than key_name
//		Entity user = new Entity("User","user_id");
//		user.setProperty("name", "Steven");
//		user.setProperty("user_id", 1);
//		datastore.put(user);
		
		String userID = req.getQueryString();
		if (userID != null){
			resp.setContentType("text/plain");
			Key userKey = KeyFactory.createKey("User", userID);
			Entity userProfile;
			try {
				userProfile = datastore.get(userKey);
				resp.getWriter().println("User found.");

			} catch (EntityNotFoundException e) {
				resp.getWriter().println("User does not exist");
			}
		}else{
			resp.getWriter().println("Error, userID not valid");
		}
	}
}
