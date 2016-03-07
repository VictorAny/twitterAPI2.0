package com.example.myproject;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MessageServlet
 */
public class MessageServlet extends TwitterAPI2Servlet {
	private static final long serialVersionUID = 1L;
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
		
	}

}
