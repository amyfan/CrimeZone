package com.crimezone.sd;

import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SDCrimeZoneActivity extends Activity implements View.OnClickListener {

  public static Location currLocation;
  public static String selectedRadius;
  public static String selectedDate;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.createLocationManager();
    this.initializeApp();
  }

  /**
   * Take user to landing page for SD Crime Zone. Populate the dropdown boxes
   * (Distance and Date)
   */
  public void initializeApp() {
    setContentView(R.layout.main);
    // get current GPS coordinates and set as default for app
    EditText currLocationText = (EditText) this.findViewById(R.id.addressText);
    currLocationText.setText(getString(R.string.defaultLocation));
    // the distance dropdown list
    Spinner distanceList = (Spinner) this.findViewById(R.id.distanceList);
    populateSpinnerWithArray(distanceList, R.array.distanceArray);
    distanceList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selectedRadius = parent.getSelectedItem().toString();
        selectedRadius = selectedRadius.replaceFirst("\\smile", "");
      }

      public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
        selectedRadius = parent.getSelectedItem().toString();
        selectedRadius = selectedRadius.replaceFirst("\\smile", "");
      }
    });

    // the distance dropdown list
    Spinner dateList = (Spinner) this.findViewById(R.id.datesList);
    populateSpinnerWithArray(dateList, R.array.dateArray);
    dateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selectedDate = parent.getSelectedItem().toString();
      }

      public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
        selectedDate = parent.getSelectedItem().toString();
      }
    });
    Button submitButton = (Button) this.findViewById(R.id.submitButton);
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
    EditText addrText = (EditText) this.findViewById(R.id.addressText);
    if (addrText != null
        && addrText.getText().toString().equals(getString(R.string.defaultLocation))) {
      currLocation = location;
    }
    // this.debug("Location= " + String.valueOf(location.getLatitude())
    // + ", " + String.valueOf(location.getLongitude()));
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



  /**
   * Handles all the apps onclick functions
   * 
   * @param v
   */
  public void onClick(View v) {
    if (v.getId() == R.id.submitButton) {
      JSONArray results = this.sendHttpRequestToServer(v);
      SDCrimeZoneApplication.debug(this, "got results, switching layout");
      Intent intent = new Intent();
      Bundle bun = new Bundle();

      bun.putString("results", results.toString()); // add two parameters: a string and a boolean
      EditText addr = (EditText) this.findViewById(R.id.addressText);
      String currentAddress = addr.getText().toString();
      double[] latlong = { currLocation.getLatitude(), currLocation.getLongitude() };
      if (!currentAddress.equals(getString(R.string.defaultLocation))) {
        latlong = getLatLong(currentAddress);
      }
      bun.putString("startLat", String.valueOf(latlong[0]));
      bun.putString("startLng", String.valueOf(latlong[1]));

      intent.setClass(this, SDPopulateCrimeListActivity.class);
      intent.putExtras(bun);
      startActivity(intent);
    }

  }

  

  /**
   * Sends an HttpRequest to SDCrimeZone-AppEngine with lat, long, radius (and
   * date). Gets the JSON result and returns it. Should ONLY be called from main
   * page.
   * 
   **/
  private JSONArray sendHttpRequestToServer(View v) {
    // get the current GPS coordinates, distance, and dates selected

    JSONArray jsonObjs = new JSONArray();
    Random rand = new Random(); // temp: create some fake data
    for (int i = 0; i < 30; i++) {
      JSONObject ret = new JSONObject();
      try {
        ret.put("address", "4496 Park Boulevard, San Diego");
        ret.put("bcc", String.valueOf(rand.nextInt(8) + 1));
        ret.put("lng", "-117.2025731");
        ret.put("lat", "32.9035252");
        jsonObjs.put(ret);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return jsonObjs;
/*
    EditText addr = (EditText) this.findViewById(R.id.addressText);
    Spinner dist = (Spinner) this.findViewById(R.id.distanceList);
    String currentAddress = addr.getText().toString();
    double[] latlong = { currLocation.getLatitude(), currLocation.getLongitude() };
    this.debug("curr latlong = " + latlong[0] + ", " + latlong[1]);
    if (!currentAddress.equals(getString(R.string.defaultLocation))) {
      this.debug("updating address: " + currentAddress);
      latlong = getLatLong(currentAddress);
    }
    HttpResponse response;
    try {
      HttpClient hc = new DefaultHttpClient();
      HttpGet get = new HttpGet("http://sdcrimezone.appspot.com/crimeZoneServlet?lat=" + latlong[0]
          + "&lng=" + latlong[1] + "&rad=" + selectedRadius);

      this.debug("HTTPGet = http://sdcrimezone.appspot.com/crimeZoneServlet?lat="
          + String.valueOf(latlong[0]) + "&lng=" + String.valueOf(latlong[1]) + "&rad="
          + selectedRadius);

      response = hc.execute(get);

      // get the response from the Google Apps Engine server, should be in JSON
      // format
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        Reader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
            "UTF-8"));
        StringBuilder builder = new StringBuilder();
        char[] buf = new char[1000];
        int l = 0;
        while (l >= 0) {
          builder.append(buf, 0, l);
          l = in.read(buf);
        }
        JSONTokener tokener = new JSONTokener(builder.toString());
        JSONArray finalResult = new JSONArray(tokener);
        return finalResult;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
    */
  }

  private double[] getLatLong(String strAddress) {
    Geocoder coder = new Geocoder(this);
    List<Address> address;

    try {
      address = coder.getFromLocationName(strAddress, 5);
      if (address == null) {
        return null;
      }
      Address location = address.get(0);
      double[] results = { location.getLatitude(), location.getLongitude() };
      return results;
    } catch (Exception e) {
      return null;
    }

  }

}