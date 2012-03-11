package com.crimezone.sd;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class SDCrimeZoneActivity extends Activity implements View.OnClickListener {

  public static Location currLocation;
  public static String selectedRadius;
  public static String selectedDate;

  public static final Map<String, String> bccMap;
  static {
    Map<String, String> aMap = new HashMap<String, String>();
    aMap.put("1", "Murder");
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
      JSONArray results = this.sendHttpRequestToServer(v);
      this.debug("got results, switching layout");
      // switch to the results page
      try {
        populateResultsPage(results);
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  private void populateResultsPage(JSONArray results) throws JSONException {
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
        this.debug("added " + bcc);
        /**
         * Total height per row = 40
         * height of current bar = 10
         */
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        /* Create the Crime text to be in the row-content. */
        TextView t = new TextView(this);
        t.setText(bccMap.get(bcc)); // get incident type, based on bcc code
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
        this.debug("height= " + bitmap.getHeight());

        i.setImageBitmap(bitmap);
        LayoutParams iParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        iParams.setMargins(5, 15, 5, 15);
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
        numIncidents.setTextColor(Color.RED);
        
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