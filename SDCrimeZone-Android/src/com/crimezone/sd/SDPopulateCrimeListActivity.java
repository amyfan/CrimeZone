package com.crimezone.sd;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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
    super.onCreate(savedInstanceState);
    
    JSONArray results = null;
    Bundle bundle = getIntent().getExtras();
    try {
      results = new JSONArray(bundle.getString("results"));
      this.populateResultsPage(results);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    Button viewMapButton = (Button) this.findViewById(R.id.viewMapButton);
    viewMapButton.setOnClickListener(this);
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
      intent.setClass(this, ShowCrimeMapActivity.class);
      intent.putExtras(bun);
      startActivity(intent);
    }

  }
  
  public void populateResultsPage(JSONArray results) throws JSONException {
    setContentView(R.layout.crimes);
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

      for (String bcc : incidentMap.keySet()) {
        SDCrimeZoneApplication.debug(this, "added " + bcc);
        /**
         * Total height per row = 40
         * height of current bar = 10
         */
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        /* Create the Crime text to be in the row-content. */
        TextView t = new TextView(this);
        t.setText(SDCrimeZoneApplication.bccMap.get(bcc)); // get incident type, based on bcc code
        LayoutParams tParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        tParams.setMargins(30, 10, 10, 10);
        t.setLayoutParams(tParams);
        t.setHeight(20);
        t.setWidth(100);
        t.setTypeface(Typeface.SANS_SERIF);
        t.setTextSize(10f);
        t.setTextColor(Color.WHITE);

        /* Add text to row. */
        tr.addView(t);
        TableLayout incidentLayout = new TableLayout(this);
        TableRow incidentCol = new TableRow(this);
        
        /* Create the bar image to be added */
        ImageView i = new ImageView(this);

        Resources res = getResources();
        // get the bar .png image
        Drawable drawable = res.getDrawable(R.drawable.bar);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        // crop the image based on how many incidents of that type
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, 10 * incidentMap.get(bcc).intValue(),
            bitmap.getHeight());
        SDCrimeZoneApplication.debug(this, "height= " + bitmap.getHeight());

        i.setImageBitmap(bitmap);
        LayoutParams iParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        iParams.setMargins(5, 10, 5, 10);
        i.setLayoutParams(iParams);
        i.setScaleType(ScaleType.FIT_START);
        incidentCol.addView(i);
        //tr.addView(i);
        
        TextView numIncidents = new TextView(this);
        numIncidents.setText(incidentMap.get(bcc).toString()); // get number of incidents
        LayoutParams nParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
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
        
        /* Add image to row */
        myLayout.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT));
      }
    }

  }

}
