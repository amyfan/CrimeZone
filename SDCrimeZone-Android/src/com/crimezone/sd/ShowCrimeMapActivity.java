package com.crimezone.sd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ShowCrimeMapActivity extends MapActivity {

  private MapController mapController;
  private MapView mapView;
  private LocationManager locationManager;
  private CrimeMapOverlay itemizedoverlay;
  private static Boolean firstMapOpen = Boolean.TRUE;

  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.map); // bind the layout to the activity

    JSONArray results = null;
    Bundle bun = getIntent().getExtras();
    try {
      results = new JSONArray(bun.getString("results"));
      // this.populateGoogleMaps(results);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    // create a map view
    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mapViewLayout);
    mapView = (MapView) findViewById(R.id.mapView);
    mapView.setBuiltInZoomControls(true);
    // Either satellite or 2d
    mapView.setSatellite(true);
    mapController = mapView.getController();
    mapController.setZoom(14); // Zoon 1 is world view
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
        new GeoUpdateHandler());

    Drawable drawable = this.getResources().getDrawable(R.drawable.point);
    itemizedoverlay = new CrimeMapOverlay(drawable);
    // create current location marker
    // createMarker();
    for (int i = 0; i < results.length(); i++) {
      JSONObject obj;
      try {
        obj = results.getJSONObject(i);
        String lng = obj.get("lng").toString();
        String lat = obj.get("lat").toString();
        Double myLat = new Double(lat);
        Double myLng = new Double(lng);
        int iLat = (int) (myLat.doubleValue() * 1E6);
        int iLng = (int) (myLng.doubleValue() * 1E6);
        createMarker(iLat, iLng);
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }

  public class GeoUpdateHandler implements LocationListener {

    public void onLocationChanged(Location location) {
      if (firstMapOpen) {
        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);
        GeoPoint point = new GeoPoint(lat, lng);
        // create a marker for current location
        // createMarker();
        mapController.animateTo(point); // mapController.setCenter(point);
        firstMapOpen = Boolean.FALSE;
      }
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  }

  private void createMarker(int lat, int lng) {

    GeoPoint p = new GeoPoint(lat, lng);

    OverlayItem overlayitem = new OverlayItem(p, "", "");
    itemizedoverlay.addOverlay(overlayitem);
    mapView.getOverlays().add(itemizedoverlay);
  }
}
