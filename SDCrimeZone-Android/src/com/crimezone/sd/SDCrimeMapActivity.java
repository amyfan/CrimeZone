package com.crimezone.sd;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class SDCrimeMapActivity extends MapActivity implements LocationListener {

  private static MapView mapView;
  private static MyLocationOverlay myLocation;
  private static LocationManager locationManager;
  private static String provider;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get the location manager
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Define the criteria how to select the locatioin provider -> use
    // default
    Criteria criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    Location location = locationManager.getLastKnownLocation(provider);

    // Initialize the location fields
    if (location != null) {
      System.out.println("Provider " + provider + " has been selected.");
      int lat = (int) (location.getLatitude());
      int lng = (int) (location.getLongitude());
    }
  }

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(provider, 400, 1, this);
  }

  /*
   * public void populateGoogleMaps(JSONArray results) throws JSONException {
   * 
   * setContentView(R.layout.map);
   * 
   * myLocation.enableMyLocation(); mapView.getOverlays().add(myLocation);
   * 
   * }
   */

  @Override
  protected boolean isRouteDisplayed() {
    // TODO Auto-generated method stub
    return false;
  }

  /* Remove the locationlistener updates when Activity is paused */
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  public void onLocationChanged(Location location) {
    // TODO Auto-generated method stub

  }

  public void onProviderDisabled(String provider) {
    // TODO Auto-generated method stub

  }

  public void onProviderEnabled(String provider) {
    // TODO Auto-generated method stub

  }

  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub

  }

}
