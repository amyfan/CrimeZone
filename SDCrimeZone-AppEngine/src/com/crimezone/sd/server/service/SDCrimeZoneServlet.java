package com.crimezone.sd.server.service;

import java.io.Console;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SDCrimeZoneServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Got request test: " + req.toString());
        
        
        String lat = checkNull(req.getParameter("lat"));
        String lng = checkNull(req.getParameter("lng"));
        String rad = checkNull(req.getParameter("rad"));
        
        resp.getWriter().println("lat = " + lat + ", lng = " + lng + ", rad = " + rad);
        
    }
	
	private String checkNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}
}
