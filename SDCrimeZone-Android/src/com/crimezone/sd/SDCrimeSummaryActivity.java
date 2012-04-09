package com.crimezone.sd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;

public class SDCrimeSummaryActivity extends Activity implements View.OnClickListener {
  
  private List<Data> myResults = new ArrayList<Data>();
  
      // Need handler for callbacks to the UI thread
    Handler _handler = new Handler();
    ProgressDialog _loadingDialog;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   
    
    String year = getIntent().getExtras().getString("year");
    
    _loadingDialog = ProgressDialog.show(this, "Detecting Crime", "Loading. Please wait...", true);

    // Create runnable for posting
    Runnable runnableForPosting = new Runnable() {
        public void run() {
            try {
              populateResultsPage();
            } catch (JSONException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
        }
    };
    
    
    SDCrimeSummaryWorker workerThread = new SDCrimeSummaryWorker(this, getIntent(), myResults, _handler, runnableForPosting);
    workerThread.start();
  }
  
  /**
   * Handles the Crime List onclick functions. For any onclicks in this view.
   * 
   * @param v
   */
  public void onClick(View v) {
    
    _loadingDialog.show();
    
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
     
      bun.putString("results", jsonArr.toString()); 
      bun.putString("startLat", getIntent().getExtras().getString("startLat"));
      bun.putString("startLng", getIntent().getExtras().getString("startLng"));
      bun.putString("radius", getIntent().getExtras().getString("radius"));
      bun.putString("year", getIntent().getExtras().getString("year"));
      intent.setClass(this, SDPopulateCrimeListActivity.class);
      intent.putExtras(bun);
      
      
       _loadingDialog.hide();
      startActivity(intent);
      
     
    }

  }
  
  public void populateResultsPage() throws JSONException {
    
    setContentView(R.layout.summary);
    
    Button viewCrimesDetailedListButton = (Button) this.findViewById(R.id.viewCrimesListButton);
    viewCrimesDetailedListButton.setOnClickListener(this);
    
    Double radius =  Double.valueOf(getIntent().getExtras().getString("radius"));
    String year =  getIntent().getExtras().getString("year");
    LinearLayout goodLayout = (LinearLayout) this.findViewById(R.id.goodResultsList);
    LinearLayout badLayout = (LinearLayout) this.findViewById(R.id.badResultsList);
    HashMap<String, Integer> incidentMap = new HashMap<String, Integer>();

    if (myResults == null) {
      // TODO: display an elegent error message, if no results found
      // return user to main
    } else {
      for (int i = 0; i < myResults.size(); i++) {
        Data obj = myResults.get(i);
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
      
      TextView goodTitle = new TextView(this);
      goodTitle.setText("Here's the good news:"); // get incident type, based on bcc code
      LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
      titleParams.setMargins(10, 2, 10, 2);
      goodTitle.setSingleLine(false);
      goodTitle.setTypeface(Typeface.SANS_SERIF);
      goodTitle.setTextSize(20f);
      goodTitle.setTextColor(Color.GREEN);
      goodLayout.addView(goodTitle, titleParams);
      
      
      TextView badTitle = new TextView(this);
      badTitle.setText("Here's some other news:"); // get incident type, based on bcc code
      //LayoutParams badTitleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
      badTitle.setSingleLine(false);
      badTitle.setTypeface(Typeface.SANS_SERIF);
      badTitle.setTextSize(20f);
      badTitle.setTextColor(Color.RED);
      
      int badItems = 0;
      boolean badTitleAdded = false;

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
            
            if (!badTitleAdded) {
              badLayout.addView(badTitle, titleParams);
              badTitleAdded = true;
            }
            badLayout.addView(n, tParams);
            badLayout.addView(t, tParams);
            badItems++;
          }
        }
     
      }
      
      if (badItems == 0) { //Hide bad view if there is nothing bad
        findViewById(R.id.badSummaryLayout).setVisibility(View.GONE);
      }
    }

    _loadingDialog.hide();
  }

}
