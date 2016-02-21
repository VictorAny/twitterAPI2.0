package com.example.myproject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Type;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;

    public UserServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		This is how you return json.
// 		Uncomment to see it in action.
//		Map<String, String> myHash = new HashMap<String,String>();
//		myHash.put("name", "Victor");
//		Gson gson = new Gson();
		// gson.toJson() converts the object to json and returns a string.
//		String returnObj = gson.toJson(myHash);
		// Then we just write said string. 
//		response.getWriter().println(returnObj);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//First thing we'll do is convert the requestBody to JSON dictionary so we can query
		//based on our keys
		HashMap requestDict = this.getRequestBodyMap(request);
		//Take it away!
		
		
	}
	
	public HashMap<String, String> getRequestBodyMap(HttpServletRequest request) throws IOException{
		Gson gson = new Gson();
		String stringBody = this.getBody(request);
		Type jsonType = new TypeToken<HashMap<String, String>>(){}.getType();
		HashMap<String, String> myMap = gson.fromJson(stringBody, jsonType);
		return myMap;
	}
	
	public String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}

}
