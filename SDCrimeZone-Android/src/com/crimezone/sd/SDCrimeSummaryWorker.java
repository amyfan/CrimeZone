package com.crimezone.sd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;

public class SDCrimeSummaryWorker extends Thread {

  private SDCrimeSummaryActivity _parentActivity;
  private Intent _intent;
  private String _results;
  private List<Data> _myResults;
  private Handler _handler;
  private Runnable _runnable;

  public SDCrimeSummaryWorker(SDCrimeSummaryActivity parentActivity, Intent intent, List<Data> myResults, Handler handler, Runnable runnable) {
    _parentActivity = parentActivity;
    _intent = intent;
    _myResults = myResults;
    _handler = handler;
    _runnable = runnable;
  }

  public void run() {
    SDCrimeZoneApplication.debug(_parentActivity, "Summary Worker Thread Started");
    
    Bundle bundle = _intent.getExtras();
    Double startLng = Double.valueOf(_intent.getExtras().getString("startLng"));
    Double startLat = Double.valueOf(_intent.getExtras().getString("startLat"));
    Double startRad = Double.valueOf(_intent.getExtras().getString("radius"));

    try {
      _results = bundle.getString("results");
      FileInputStream fstream = new FileInputStream(_results);

      DataInputStream in = new DataInputStream(fstream);

      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      // Read File Line By Line
      String text = "";
      char chrBuffer[] = new char[1];
      
      String checkDigit = "-0123456789.";
      boolean checkedDigit = false;
      boolean gotYear = false;
      while (br.read(chrBuffer) >= 0) // loop through each line
      {
        char curr = chrBuffer[0];
        if (curr == '{') {
          text = "{";
        } else {
          if (gotYear && !checkedDigit && checkDigit.indexOf(curr) >= 0) {
            text += '"';
            checkedDigit = true;
          }
          if (gotYear && checkedDigit && checkDigit.indexOf(curr) < 0) {
            text += '"';
          }
          if (gotYear && checkedDigit && curr == '"') {
            text += ',';
            checkedDigit = false;
          }
          text += curr;
          if (gotYear && checkedDigit && curr == ',') {
            checkedDigit = false;
          }
          if (!gotYear && text.lastIndexOf("year") > 0) {
            gotYear = true;
          }
        }
        if (curr == '}') {
          Data currData = new Gson().fromJson(text, Data.class);
          Double numMilesLat = Math.abs(69.11 * (Double.valueOf(currData.getLat()) - startLat));
          Double numMilesLng = Math.abs(69.11
              * Math.cos(Double.valueOf(currData.getLat()) * 0.0174532925)
              * (Double.valueOf(currData.getLng()) - startLng));
          Double dist = Math.sqrt(numMilesLat * numMilesLat + numMilesLng * numMilesLng);
          if (dist <= startRad) {
            System.out.println("adding: " + currData.getLat() + ", " + currData.getLng());
            _myResults.add(currData);
          }
          br.read(chrBuffer);
          checkedDigit = false;
          gotYear = false;
        }
      }
      in.close();// Close the input stream
      SDCrimeZoneApplication.debug(_parentActivity, "My Results total = " + _myResults.size());
      
      _handler.post(_runnable);
      //_parentActivity.populateResultsPage();

    } catch (Exception e) {
      e.printStackTrace();
    }

    SDCrimeZoneApplication.debug(_parentActivity, "Summary Worker Thread Exiting");
  }

}
