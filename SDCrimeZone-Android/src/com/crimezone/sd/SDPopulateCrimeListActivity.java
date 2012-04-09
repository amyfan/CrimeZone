package com.crimezone.sd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class SDPopulateCrimeListActivity extends Activity implements View.OnClickListener {
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    ProgressDialog dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
    
    super.onCreate(savedInstanceState);
    
    JSONArray results = null;
    Bundle bundle = getIntent().getExtras();
    Double startLng = Double.valueOf(getIntent().getExtras().getString("startLng"));
    Double startLat = Double.valueOf(getIntent().getExtras().getString("startLat"));
    Double startRad = Double.valueOf(getIntent().getExtras().getString("radius"));
     
    try {
      results = new JSONArray(getIntent().getExtras().getString("results"));
      this.populateResultsPage(results);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    Button viewMapButton = (Button) this.findViewById(R.id.viewMapButton);
    viewMapButton.setOnClickListener(this);
    dialog.dismiss();
  }
  
  /**
   * Handles the Crime List onclick functions. For any onclicks in this view.
   * 
   * @param v
   */
  public void onClick(View v) {
    if (v.getId() == R.id.viewMapButton) {
      SDCrimeZoneApplication.debug(this, "opening map");
      Intent intent = new Intent();
      Bundle bun = new Bundle();
      
      bun.putString("results", getIntent().getExtras().getString("results")); // add two parameters: a string and a boolean
      bun.putString("startLat", getIntent().getExtras().getString("startLat"));
      bun.putString("startLng", getIntent().getExtras().getString("startLng"));
      bun.putString("radius", getIntent().getExtras().getString("radius"));
      intent.setClass(this, ShowCrimeMapActivity.class);
      intent.putExtras(bun);
      startActivity(intent);
    }

  }
  
  public void populateResultsPage(JSONArray results) throws JSONException {
    setContentView(R.layout.crimes);
    Double radius =  Double.valueOf(getIntent().getExtras().getString("radius"));
    TableLayout myLayout = (TableLayout) this.findViewById(R.id.crimesLayout);
    HashMap<String, Integer> incidentMap = new HashMap<String, Integer>();

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

      for (String bcc : sortedKeys) {
        //SDCrimeZoneApplication.debug(this, "added " + bcc);
        // if the bcc code doesn't map to anything, we skip it
        /**
         * Total height per row = 40
         * height of current bar = 10
         */
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        /* Create the Crime text to be in the row-content. */
        TextView t = new TextView(this);
        if (!incidentMap.containsKey(bcc)) {
          incidentMap.put(bcc, Integer.valueOf(0));
        }
        t.setText(SDCrimeZoneApplication.bccMap.get(bcc)); // get incident type, based on bcc code
        System.out.println("bcc text = " + SDCrimeZoneApplication.bccMap.get(bcc));
        LayoutParams tParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
        tParams.setMargins(30, 10, 10, 10);
        t.setSingleLine(false);
        t.setWidth(100);
        t.setTypeface(Typeface.SANS_SERIF);
        t.setTextSize(10f);
        t.setTextColor(Color.WHITE);

        /* Add text to row. */
        tr.addView(t, tParams);
        TableLayout incidentLayout = new TableLayout(this);
        TableRow incidentCol = new TableRow(this);
        
        /* Create the bar image to be added */
        ImageView i = new ImageView(this);

        Resources res = getResources();
        // get the bar .png image
        Drawable drawable = res.getDrawable(R.drawable.bar);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        // crop the image based on how many incidents of that type
        int width = 2 * incidentMap.get(bcc).intValue() + 1;
        // if the width is t
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        if ( width > display.getWidth() - 300) {
          width = display.getWidth() - 300;
        }
        if (width > bitmap.getWidth() - 30) {
          width = bitmap.getWidth() - 30;
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
            bitmap.getHeight());
       // SDCrimeZoneApplication.debug(this, "height= " + bitmap.getHeight());

        i.setImageBitmap(bitmap);
        LayoutParams iParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iParams.setMargins(5, 15, 5, 10);
        i.setScaleType(ScaleType.FIT_START);
        incidentCol.addView(i, iParams);
        //tr.addView(i);
        
        TextView numIncidents = new TextView(this);
        numIncidents.setText(incidentMap.get(bcc).toString()); // get number of incidents
        LayoutParams nParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        nParams.setMargins(10, 10, 10, 10);
        numIncidents.setLayoutParams(tParams);
        numIncidents.setHeight(20);
        numIncidents.setWidth(100);
        numIncidents.setTypeface(Typeface.SANS_SERIF);
        numIncidents.setTextSize(10f);
        numIncidents.setTextColor(Color.WHITE);
        
        //tr.addView(numIncidents);
        incidentCol.addView(numIncidents);
        incidentLayout.addView(incidentCol);
        tr.addView(incidentLayout);
        
        myLayout.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT));
        
      }
    }

  }

}
