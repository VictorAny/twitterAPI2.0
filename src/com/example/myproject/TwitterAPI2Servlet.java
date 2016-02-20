package com.example.myproject;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class TwitterAPI2Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//This is a handler. All this is doing is setting the response content-type to be text.
		//This is needed as you can write multi-media content.
		
		//The validation team will take the raw request, which is the req parameter, and validate it.
		//The handlers team will handle fetching that data and then writting the response. 
		
		
		resp.setContentType("text/plain");
		//This then just prints hello world.
		//getWriter() function writes to the web page. This will be how you return information
		resp.getWriter().println("Hello, Twitter 2.0!");
	}
}
