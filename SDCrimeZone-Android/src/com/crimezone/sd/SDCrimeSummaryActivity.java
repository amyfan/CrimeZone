package com.crimezone.sd;

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

public class SDCrimeSummaryActivity extends Activity implements View.OnClickListener {
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    Bundle bundle = getIntent().getExtras();
    Double startLng = Double.valueOf(getIntent().getExtras().getString("startLng"));
    Double startLat = Double.valueOf(getIntent().getExtras().getString("startLat"));
    Double startRad = Double.valueOf(getIntent().getExtras().getString("radius"));
    String year = getIntent().getExtras().getString("year");
     
    try {
      JSONArray results = new JSONArray(bundle.getString("results"));
      this.populateSummaryPage(results);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Button viewCrimesDetailedListButton = (Button) this.findViewById(R.id.viewCrimesListButton);
    viewCrimesDetailedListButton.setOnClickListener(this);
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
     
      bun.putString("results", getIntent().getExtras().getString("results")); 
      bun.putString("startLat", getIntent().getExtras().getString("startLat"));
      bun.putString("startLng", getIntent().getExtras().getString("startLng"));
      bun.putString("radius", getIntent().getExtras().getString("radius"));
      bun.putString("year", getIntent().getExtras().getString("year"));
      intent.setClass(this, SDPopulateCrimeListActivity.class);
      intent.putExtras(bun);
      startActivity(intent);
    }

  }
  
  public void populateSummaryPage(JSONArray results) throws JSONException {
    setContentView(R.layout.summary);
    Double radius =  Double.valueOf(getIntent().getExtras().getString("radius"));
    String year =  getIntent().getExtras().getString("year");
    LinearLayout goodLayout = (LinearLayout) this.findViewById(R.id.goodResultsList);
    LinearLayout badLayout = (LinearLayout) this.findViewById(R.id.badResultsList);
    HashMap<String, Integer> incidentMap = new HashMap<String, Integer>();
    
    LayoutParams tParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
    tParams.setMargins(10, 2, 10, 2);

    if (results == null) {
      // TODO: display an elegent error message, if no results found
      // return user to main
    } else {
      for (int i = 0; i < results.length(); i++) {
        JSONObject obj = results.getJSONObject(i);
        String bcc = (String) obj.get("bcc");
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
      
      int countBadIncidents = 0;
      List<String> goodIncidents = new ArrayList<String>();

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
        
        
        t.setSingleLine(false);
        t.setTypeface(Typeface.SANS_SERIF);
        t.setTextSize(10f);
        
        String crimeName = "";
        if ("AMSZ".indexOf(bcc, 0) >= 0 ) {
          crimeName = SDCrimeZoneApplication.bccMap.get(bcc);
        } else {
          crimeName = SDCrimeZoneApplication.bccMap.get(bcc) + " crimes";
        }
       
        // if less than 3% of this type of crime, than show under good results
        if (percentage < 3.0) {
            n.setTextColor(Color.GREEN);
            if (incidentMap.get(bcc) == null || incidentMap.get(bcc).doubleValue() == 0.0) {
              goodIncidents.add(bcc);
            } else {
              n.setText(String.format("Only %.2f",percentage) + "% of " + crimeName);
              n.setTextSize(12f);
            }
            
            if (!String.valueOf(percentage).equals("NaN")) {
              if (incidentMap.get(bcc) != null && incidentMap.get(bcc).doubleValue() != 0.0) {
                goodLayout.addView(n, tParams);
                goodLayout.addView(t, tParams);
              }
            }
        } else {
          n.setTextColor(Color.RED);
          n.setText(String.format("%.2f",percentage) + "% of " + crimeName);
          n.setTextSize(12f);
          if (!String.valueOf(percentage).equals("NaN")) {
            badLayout.addView(n, tParams);
            badLayout.addView(t, tParams);
            countBadIncidents++;
          }
        }
     
      }
      if (goodIncidents.size() > 0) {
        TextView n = new TextView(this);
        TextView t = new TextView(this);
        n.setTextColor(Color.GREEN);
        String crimeName = "";
        for (int i = 0; i < goodIncidents.size(); i++) {
          crimeName += SDCrimeZoneApplication.bccMap.get(goodIncidents.get(i));
          if (i < goodIncidents.size()-1) {
            crimeName += ", ";
          }
        }
        n.setText("No reports of " + crimeName );
        t.setText(" within " + radius + " mile(s) radius in " + year + ".");
        t.setTypeface(Typeface.SANS_SERIF);
        t.setTextSize(10f);
        goodLayout.addView(n, tParams);
        goodLayout.addView(t, tParams);
      }
      if (countBadIncidents == 0) {
        TextView n = new TextView(this);
        n.setTextColor(Color.RED);
        n.setText("No bad stuff.");
        n.setTextSize(12f);
        badLayout.addView(n, tParams);
      }
    }

  }

}
