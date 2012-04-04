package com.crimezone.sd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class SDCrimeSummaryActivity extends Activity implements View.OnClickListener {
  
  private List<Data> myResults;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    String results = null;
    Bundle bundle = getIntent().getExtras();
    Double startLng = Double.valueOf(getIntent().getExtras().getString("startLng"));
    Double startLat = Double.valueOf(getIntent().getExtras().getString("startLat"));
    Double startRad = Double.valueOf(getIntent().getExtras().getString("radius"));
    String year = getIntent().getExtras().getString("year");
     
    try {
      results = bundle.getString("results");
      FileInputStream fstream = new FileInputStream(results);
      
      DataInputStream in = new DataInputStream(fstream);
      
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      //Read File Line By Line
      String text = "";
      char chrBuffer[] = new char[1];
      myResults = new ArrayList<Data>();
      String checkDigit = "-0123456789.";
      boolean checkedDigit = false;
      boolean gotYear = false;
      while (br.read(chrBuffer) >= 0) //loop through each line
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
          Double numMilesLng = Math.abs(69.11 * Math.cos(Double.valueOf(currData.getLat())* 0.0174532925) * (Double.valueOf(currData.getLng()) - startLng));
          Double dist = Math.sqrt(numMilesLat*numMilesLat + numMilesLng*numMilesLng);
          if (dist <= startRad) {
            System.out.println("adding: " + currData.getLat() + ", " + currData.getLng());
            myResults.add(currData);
          }
          br.read(chrBuffer);
          checkedDigit = false;
          gotYear = false;
        }
      }
      in.close();//Close the input stream
      System.out.println("My Results total = " + myResults.size());
      this.populateResultsPage(myResults);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Button viewMapButton = (Button) this.findViewById(R.id.viewCrimesListButton);
    viewMapButton.setOnClickListener(this);
  }
  
  /**
   * Handles the Crime List onclick functions. For any onclicks in this view.
   * 
   * @param v
   */
  public void onClick(View v) {
    if (v.getId() == R.id.viewCrimesListButton) {
      Intent intent = new Intent();
      Bundle bun = new Bundle();
      
      JSONArray jsonArr = new JSONArray();
      for (Data res : myResults) {
        try {
          JSONObject jsonObj = new JSONObject(new Gson().toJson(res));
          jsonArr.put(jsonObj);
        } catch (JSONException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
     
      bun.putString("results", jsonArr.toString()); // add two parameters: a string and a boolean
      bun.putString("startLat", getIntent().getExtras().getString("startLat"));
      bun.putString("startLng", getIntent().getExtras().getString("startLng"));
      bun.putString("radius", getIntent().getExtras().getString("radius"));
      bun.putString("year", getIntent().getExtras().getString("year"));
      intent.setClass(this, SDPopulateCrimeListActivity.class);
      intent.putExtras(bun);
      startActivity(intent);
    }

  }
  
  public void populateResultsPage(List<Data> results) throws JSONException {
    setContentView(R.layout.summary);
    Double radius =  Double.valueOf(getIntent().getExtras().getString("radius"));
    String year =  getIntent().getExtras().getString("year");
    LinearLayout goodLayout = (LinearLayout) this.findViewById(R.id.goodResultsList);
    LinearLayout badLayout = (LinearLayout) this.findViewById(R.id.badResultsList);
    HashMap<String, Integer> incidentMap = new HashMap<String, Integer>();

    if (results == null) {
      // TODO: display an elegent error message, if no results found
      // return user to main
    } else {
      for (int i = 0; i < results.size(); i++) {
        Data obj = results.get(i);
        String bcc = obj.getBcc();
        if (!incidentMap.containsKey(bcc)) {
          incidentMap.put(bcc, Integer.valueOf(1));
        } else {
          Integer num = incidentMap.get(bcc);
          int newNum = num.intValue() + 1;
          incidentMap.put(bcc, new Integer(newNum));
        }
      }
      
      List<String> sortedKeys=new ArrayList(SDCrimeZoneApplication.bccMap.keySet());
      Collections.sort(sortedKeys);

      for (String bcc : sortedKeys) {
        /* Create a new row to be added. */
        TextView t = new TextView(this);
        TextView n = new TextView(this);
        if (!incidentMap.containsKey(bcc)) {
          incidentMap.put(bcc, Integer.valueOf(0));
        }
        boolean goodResult = false;
        double percentage = 0.0;
        Double total = Double.valueOf(SDCrimeZoneApplication.bccAverage2011.get(bcc));

        percentage = 100.00 - (Math.abs((total.doubleValue()- incidentMap.get(bcc).doubleValue() )/(total.doubleValue())) * 100);
        t.setText(" for San Diego occured within a " + radius + " mile(s) radius."); // get incident type, based on bcc code
        LayoutParams tParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
        tParams.setMargins(10, 2, 10, 2);
        t.setSingleLine(false);
        t.setTypeface(Typeface.SANS_SERIF);
        t.setTextSize(10f);
       
        // if less than 10% of this type of crime, than show under good results
        if (percentage < 10.0) {
          n.setTextColor(Color.GREEN);
          if (incidentMap.get(bcc) == null || incidentMap.get(bcc).doubleValue() == 0.0) {
            n.setText("No " + SDCrimeZoneApplication.bccMap.get(bcc) );
            t.setText(" within " + radius + " mile(s) radius in " + year + ".");
          } else {
            n.setText(String.format("Only %.2f",percentage) + "% of " + SDCrimeZoneApplication.bccMap.get(bcc) + " crimes");
            n.setTextSize(12f);
          }
          
          if (!String.valueOf(percentage).equals("NaN")) {
            goodLayout.addView(n, tParams);
            goodLayout.addView(t, tParams);
          }
        } else {
          n.setTextColor(Color.RED);
          n.setText(String.format("%.2f",percentage) + "% of " + SDCrimeZoneApplication.bccMap.get(bcc) + " crimes");
          n.setTextSize(12f);
          if (!String.valueOf(percentage).equals("NaN")) {
            badLayout.addView(n, tParams);
            badLayout.addView(t, tParams);
          }
        }
     
      }
    }

  }

}
