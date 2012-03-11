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
        
        String action = checkNull(req.getParameter("action"));
        
        if (action.compareToIgnoreCase("load") == 0) {
          String serverName = req.getServerName();
          String url;
          if (serverName.compareToIgnoreCase("127.0.0.1") == 0 || serverName.compareToIgnoreCase("localhost") == 0)
            url = String.format("http://%s:8888/resources/complete.txt", serverName);
          else
            url = "http://sdcrimezone.appspot.com:8888/resources/complete.txt";
          LoadFile(url);
        }
        
    }
	
	private void LoadFile(String url) {
	  try {
          URL inputData = new URL(url);
          URLConnection  urlConn = inputData.openConnection();
          InputStreamReader is = new InputStreamReader(urlConn.getInputStream(), "UTF8");
          BufferedReader in = new BufferedReader(is);
          CrimeDataLoader dataLoader = CrimeDataLoader.getInstance();
          dataLoader.deleteAllIncidents();
          dataLoader.insertIncidents(in);
	  }
	  catch (Exception e) { }
	  finally { }
	}
	
	private String checkNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}
}
