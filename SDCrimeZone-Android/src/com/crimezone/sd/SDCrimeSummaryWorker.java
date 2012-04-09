package com.crimezone.sd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SDCrimeSummaryWorker extends Thread {

  private SDCrimeSummaryActivity _parentActivity;
  private Intent _intent;
  private String _results;
  private String _myResults;
  private Handler _handler;
  private Runnable _runnable;

  public SDCrimeSummaryWorker(SDCrimeSummaryActivity parentActivity, Intent intent, String myResults, Handler handler, Runnable runnable) {
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
     _myResults = _intent.getExtras().getString("results");
      
      _handler.post(_runnable);
      //_parentActivity.populateResultsPage();

    } catch (Exception e) {
      e.printStackTrace();
    }

    SDCrimeZoneApplication.debug(_parentActivity, "Summary Worker Thread Exiting");
  }

}
