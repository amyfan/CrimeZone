package com.crimezone.sd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class SDCrimeZoneActivity extends Activity implements View.OnClickListener {

  public static Location currLocation;
  
  public static final Map<String,String> bccMap;
  static {
    Map<String, String> aMap = new HashMap<String, String>();
    aMap.put("1","Murder");
    aMap.put("2", "Rape");
    aMap.put("3", "Robbery");
    aMap.put("4", "Assault");
    aMap.put("5", "Burglary");
    aMap.put("6", "Theft");
    aMap.put("7", "Vehicle Theft");
    aMap.put("8", "Arson");
    aMap.put("A", "Other Crimes");
    aMap.put("C", "Child & Family");
    aMap.put("D", "Deadly Weapon");
    aMap.put("E", "Embezzlement");
    aMap.put("F", "Fraud");
    aMap.put("G", "Gambling");
    aMap.put("M", "Malicious Mischief");
    aMap.put("N", "Narcotics");
    aMap.put("S", "Sex Crimes");
    aMap.put("Y", "Forgery");
    aMap.put("Z", "Other Non-criminal incidents");
    bccMap = Collections.unmodifiableMap(aMap);
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.createLocationManager();
    this.initializeApp();
  }

  /**
   * initializeApp Take user to landing page for SD Crime Zone. Populate the
   * dropdown boxes (Distance and Date)
   */
  public void initializeApp() {
    setContentView(R.layout.main);
    // get current GPS coordinates and set as default for app
    EditText currLocationText = (EditText) this.findViewById(R.id.addressText);
    currLocationText.setText("Current Location");
    // the distance dropdown list
    Spinner distanceList = (Spinner) this.findViewById(R.id.distanceList);
    populateSpinnerWithArray(distanceList, R.array.distanceArray);
    // the distance dropdown list
    Spinner dateList = (Spinner) this.findViewById(R.id.datesList);
    populateSpinnerWithArray(dateList, R.array.dateArray);
    Button submitButton = (Button)this.findViewById(R.id.submitButton);
    submitButton.setOnClickListener(this);
  }

  public void createLocationManager() {
    // Acquire a reference to the system Location Manager
    LocationManager locationManager = (LocationManager) this
        .getSystemService(Context.LOCATION_SERVICE);

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
      public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        updateAddressWithCurrentLocation(location);
      }

      public void onStatusChanged(String provider, int status, Bundle extras) {
      }

      public void onProviderEnabled(String provider) {
      }

      public void onProviderDisabled(String provider) {
      }
    };

    // Register the listener with the Location Manager to receive location
    // updates
    locationManager
        .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
  }

  public void updateAddressWithCurrentLocation(Location location) {
    currLocation = location;
    //this.debug("Location= " + String.valueOf(location.getLatitude())
    //    + ", " + String.valueOf(location.getLongitude()));
  }

  /**
   * populateSpinnerWithArray Populates dropdown boxes with options, based on
   * string arrays.
   * 
   * @param spinner
   * @param stringArrayResId
   */
  public static void populateSpinnerWithArray(Spinner spinner, int stringArrayResId) {
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(),
        android.R.layout.simple_spinner_item, spinner.getContext().getResources()
            .getStringArray(stringArrayResId));
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
  }
  
  private void debug(String out) {
    Log.v(getString(R.string.app_name), out);
  }

  /**
   * Handles all the apps onclick functions
   * 
   * @param v
   */
  public void onClick(View v) {
    if (v.getId() == R.id.submitButton) {
      ArrayList<JSONObject> results = this.sendHttpRequestToServer(v);
      this.debug("got results, switching layout");
      //switch to the results page
      try {
        populateResultsPage(results);
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
  }
  
  private void populateResultsPage(ArrayList<JSONObject> results) throws JSONException {
    setContentView(R.layout.crimes);
    TableLayout myLayout = (TableLayout)this.findViewById(R.id.crimesLayout);
    HashMap<String, Integer> incidentMap = new HashMap<String, Integer>();
    for (JSONObject obj: results) {
      String bcc = (String) obj.get("bcc");
      if (!incidentMap.containsKey(bcc)) {
        incidentMap.put(bcc, Integer.valueOf(1));
      } else {
        Integer num = incidentMap.get(bcc);
        int newNum = num.intValue() + 1;
        incidentMap.put(bcc, new Integer(newNum));
      }
    }
    
    for (String bcc: incidentMap.keySet()) {
      this.debug("added " + bcc);
      /* Create a new row to be added. */
      TableRow tr = new TableRow(this);
      tr.setLayoutParams(new LayoutParams(
          LayoutParams.FILL_PARENT,
          LayoutParams.WRAP_CONTENT));
           /* Create the Crime text to be in the row-content. */
           TextView t = new TextView(this);
           t.setText(bccMap.get(bcc));  //get incident type, based on bcc code
           LayoutParams params = new LayoutParams(
               LayoutParams.FILL_PARENT,
               LayoutParams.WRAP_CONTENT);
           params.setMargins(30, 15, 30, 15);
           t.setLayoutParams(params);

           /* Add text to row. */
           tr.addView(t);
           /* Create the bar image to be added */
           ImageView i = new ImageView(this);
           
           Resources res = getResources();
           //get the bar .png image
           Drawable drawable = res.getDrawable(R.drawable.bar);
           Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
           //crop the image based on how many incidents of that type
           bitmap = Bitmap.createBitmap(bitmap,0,0,10*incidentMap.get(bcc).intValue(),bitmap.getHeight());
           this.debug("height= " + bitmap.getHeight());

           i.setImageBitmap (bitmap);
           i.setLayoutParams(new LayoutParams(
               LayoutParams.FILL_PARENT,
               LayoutParams.WRAP_CONTENT));
           /* Add image to row */
           tr.addView(i);
           myLayout.addView(tr, new TableLayout.LayoutParams(
               LayoutParams.FILL_PARENT,
               LayoutParams.WRAP_CONTENT));
    }
    
  }

  private ArrayList<JSONObject> sendHttpRequestToServer(View v) {
    // get the current GPS coordinates, distance, and dates selected
    ArrayList<JSONObject> jsonObjs = new ArrayList<JSONObject>();
    Random rand = new Random();
    //temp: create some fake data
    for (int i = 0; i < 30; i++) {
      JSONObject ret = new JSONObject();
      try {
        ret.put("address", "4496 Park Boulevard, San Diego");
        ret.put("bcc", String.valueOf(rand.nextInt(8) + 1));
        jsonObjs.add(ret);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    
   
    
    
    return jsonObjs;
  }
}