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

import org.json.JSONException;

import android.app.Activity;
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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class SDPopulateCrimeListActivity extends Activity implements View.OnClickListener {
  
  private List<Data> myResults;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    String results = null;
    Bundle bundle = getIntent().getExtras();
    Double startLng = Double.valueOf(getIntent().getExtras().getString("startLng"));
    Double startLat = Double.valueOf(getIntent().getExtras().getString("startLat"));
    Double startRad = Double.valueOf(getIntent().getExtras().getString("radius")) * Double.valueOf(0.01449);
     
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
          System.out.println(text);
          Data currData = new Gson().fromJson(text, Data.class);
          if (Math.abs(Double.valueOf(currData.getLat()) - startLat + Double.valueOf(currData.getLng()) - startLng) <= startRad) {
            myResults.add(currData);
          }
          br.read(chrBuffer);
          checkedDigit = false;
          gotYear = false;
        }
      }
      in.close();//Close the input stream
      this.populateResultsPage(myResults);
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonSyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
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
      bun.putString("radius", getIntent().getExtras().getString("radius"));
      intent.setClass(this, ShowCrimeMapActivity.class);
      intent.putExtras(bun);
      startActivity(intent);
    }

  }
  
  public void populateResultsPage(List<Data> results) throws JSONException {
    setContentView(R.layout.crimes);
    Double radius =  Double.valueOf(getIntent().getExtras().getString("radius"));
    TableLayout myLayout = (TableLayout) this.findViewById(R.id.crimesLayout);
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
        
        /**
         * display averages
         */
        
//        /* Create a new row to be added. */
//        TableRow averageRow = new TableRow(this);
//        averageRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//        /* Create the Crime text to be in the row-content. */
//        TextView avgTxt = new TextView(this);
//        avgTxt.setText("(SD avg)"); // get incident type, based on bcc code
//        avgTxt.setSingleLine(false);
//        avgTxt.setWidth(100);
//        avgTxt.setTypeface(Typeface.SANS_SERIF);
//        avgTxt.setTextSize(10f);
//        avgTxt.setTextColor(Color.GRAY);
//
//        /* Add text to row. */
//        averageRow.addView(avgTxt, tParams);
//        TableLayout avgIncidentLayout = new TableLayout(this);
//        TableRow avgIncidentCol = new TableRow(this);
//        
//        /* Create the bar image to be added */
//        ImageView avgImg = new ImageView(this);
//
//        // get the bar .png image
//        Drawable drawableAvg = res.getDrawable(R.drawable.bar);
//        Bitmap bitmapAvg = ((BitmapDrawable) drawableAvg).getBitmap();
//        // crop the image based on how many incidents of that type
//        double average = SDCrimeZoneApplication.bccAverage2011.get(bcc).doubleValue() * radius.doubleValue();
//        double  widthAvg = 2.0 * average + 1;
//        // if the width is t
//        if ( widthAvg > bitmapAvg.getWidth() - 10) {
//          widthAvg = bitmapAvg.getWidth() - 10;
//        }
//        bitmapAvg = Bitmap.createBitmap(bitmapAvg, 0, 0, (int)widthAvg,
//            bitmapAvg.getHeight());
//
//        avgImg.setImageBitmap(bitmapAvg);
//        avgIncidentCol.addView(avgImg, iParams);
//        
//        TextView numAvg = new TextView(this);
//        numAvg.setText(String.valueOf(average)); // get number of incidents
//        numAvg.setLayoutParams(tParams);
//        numAvg.setHeight(20);
//        numAvg.setWidth(100);
//        numAvg.setTypeface(Typeface.SANS_SERIF);
//        numAvg.setTextSize(10f);
//        numAvg.setTextColor(Color.GRAY);
//        
//        //tr.addView(numIncidents);
//        avgIncidentCol.addView(numAvg);
//        avgIncidentLayout.addView(avgIncidentCol);
//        averageRow.addView(avgIncidentLayout);
//        
//        myLayout.addView(averageRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//            LayoutParams.WRAP_CONTENT));
      }
    }

  }

}
