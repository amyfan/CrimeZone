package com.crimezone.sd.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crimezone.sd.server.logic.CrimeDataLoader;

public class SDCrimeZoneServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Got request test: " + req.toString());
        
        
        String lat = checkNull(req.getParameter("lat"));
        String lng = checkNull(req.getParameter("lng"));
        String rad = checkNull(req.getParameter("rad"));
        
        resp.getWriter().println("lat = " + lat + ", lng = " + lng + ", rad = " + rad);

        String url = "http://localhost:8888/resources/testnext5.txt";
        resp.getWriter().println(url);
        
        URL inputData = new URL(url);
        URLConnection  urlConn = inputData.openConnection();
        InputStreamReader is = new InputStreamReader(urlConn.getInputStream(), "UTF8");
        BufferedReader in = new BufferedReader(is);
        CrimeDataLoader dataLoader = CrimeDataLoader.getInstance();
        dataLoader.insertIncidents(in);

    }
	
	private String checkNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}
}
