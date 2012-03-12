package com.crimezone.sd.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.crimezone.sd.server.logic.CrimeDataLoader;
import com.crimezone.sd.server.logic.CrimeDataReader;
import com.crimezone.sd.server.logic.JSONUtils;

@SuppressWarnings("serial")
public class SDCrimeZoneServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");

    String latString = checkNull(req.getParameter("lat"));
    String lonString = checkNull(req.getParameter("lng"));
    String radString = checkNull(req.getParameter("rad"));
    String yearString = checkNull(req.getParameter("year"));

    try {
      if (!latString.isEmpty() && !lonString.isEmpty() && !radString.isEmpty()) {
        CrimeDataReader dataReader = CrimeDataReader.getInstance();

        Integer radius = Integer.valueOf(radString);
        Double latitude = Double.valueOf(latString);
        Double longitude = Double.valueOf(lonString);

        if (!yearString.isEmpty()) {
          Integer year = Integer.valueOf(yearString);
          dataReader.findIncidentsByYearAndRadius(year, latitude, longitude, radius);
        }

        JSONArray jsonArray = JSONUtils.convertIncidentsToJSONArray(null);
        resp.getWriter().println(jsonArray.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.getWriter().println("ERROR processing request");
    }
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String action = checkNull(req.getParameter("action"));

    if (action.compareToIgnoreCase("load") == 0) {
      String serverName = req.getServerName();
      String url;
      if (serverName.compareToIgnoreCase("127.0.0.1") == 0
          || serverName.compareToIgnoreCase("localhost") == 0)
        url = String.format("http://%s:8888/resources/complete.txt", serverName);
      else
        url = "http://sdcrimezone.appspot.com:8888/resources/complete.txt";
      LoadFile(url);
    }

  }

  private void LoadFile(String url) {
    try {
      URL inputData = new URL(url);
      URLConnection urlConn = inputData.openConnection();
      InputStreamReader is = new InputStreamReader(urlConn.getInputStream(), "UTF8");
      BufferedReader in = new BufferedReader(is);
      CrimeDataLoader dataLoader = CrimeDataLoader.getInstance();
      dataLoader.deleteAllIncidents();
      dataLoader.insertIncidents(in);
    } catch (Exception e) {
    } finally {
    }
  }

  private String checkNull(String s) {
    if (s == null) {
      return "";
    }
    return s;
  }
}
