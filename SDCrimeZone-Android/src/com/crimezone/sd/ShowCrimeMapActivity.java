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
  private GeoUpdateHandler gps;

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
    String startLat = bun.getString("startLat");
    String startLng = bun.getString("startLng");
    String radius = bun.getString("radius");
    GeoPoint currLocation = SDCrimeZoneApplication.getGeoPoint(startLat, startLng);

    // create a map view
    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mapViewLayout);
    mapView = (MapView) findViewById(R.id.mapView);
    mapView.setBuiltInZoomControls(true);
    mapView.getOverlays().add(
        new CircleMapOverlay(this, Double.valueOf(startLat).doubleValue(), Double.valueOf(startLng).doubleValue(), 
            Float.valueOf(radius).floatValue()));
    // Either satellite or 2d
    mapView.setSatellite(false);
    mapController = mapView.getController();
    mapController.setZoom(14); // Zoon 1 is world view
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    gps = new GeoUpdateHandler(currLocation);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gps);

    Drawable drawable = this.getResources().getDrawable(R.drawable.point);
    itemizedoverlay = new CrimeMapOverlay(drawable, this);
    // create current location marker
    // createMarker();
    for (int i = 0; i < results.length(); i++) {
      JSONObject obj;
      try {
        obj = results.getJSONObject(i);
        String lng = obj.get("lng").toString();
        String lat = obj.get("lat").toString();
        //SDCrimeZoneApplication.debug(this, "mapping " + lat + ", " + lng);
        Double myLat = new Double(lat);
        Double myLng = new Double(lng);
        String crime = SDCrimeZoneApplication.bccMap.get(obj.get("bcc").toString());
        String address = obj.get("address").toString();
        int iLat = (int) (myLat.doubleValue() * 1E6);
        int iLng = (int) (myLng.doubleValue() * 1E6);
        // create the marker for Maps view
        createMarker(iLat, iLng, crime, address);
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    mapView.getOverlays().add(itemizedoverlay);

  }

  public void onPause() {
    super.onPause();
    locationManager.removeUpdates(gps);
  }

  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }

  public class GeoUpdateHandler implements LocationListener {

    public GeoUpdateHandler(GeoPoint startingLocation) {
      mapController.animateTo(startingLocation); // mapController.setCenter(point);
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  }

  private void createMarker(int lat, int lng, String title, String subtext) {

    GeoPoint p = new GeoPoint(lat, lng);

    OverlayItem overlayitem = new OverlayItem(p, title, subtext);
    itemizedoverlay.addOverlay(overlayitem);
    
  }
}
