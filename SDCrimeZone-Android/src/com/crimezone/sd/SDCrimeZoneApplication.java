package com.crimezone.sd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.android.maps.GeoPoint;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class SDCrimeZoneApplication extends Application {
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

  public static final Map<String, Integer> bccAverage2011;
  static {
    Map<String, Integer> bMap = new HashMap<String, Integer>();
    bMap.put("1", 1);
    bMap.put("2", 70);
    bMap.put("3", 313);
    bMap.put("4", 1935);
    bMap.put("5", 2791);
    bMap.put("6", 2534);
    bMap.put("7", 2);
    bMap.put("8", 30);
    bMap.put("A", 469);
    bMap.put("C", 160);
    bMap.put("D", 11);
    bMap.put("E", 48);
    bMap.put("F", 790);
    bMap.put("G", 0);
    bMap.put("M", 1110);
    bMap.put("N", 5);
    bMap.put("S", 89);
    bMap.put("Y", 32);
    bMap.put("Z", 1007);
    bccAverage2011 = Collections.unmodifiableMap(bMap);
  }

  public static void debug(Context context, String out) {
    Log.v(context.getString(R.string.app_name), out);
  }

  public static GeoPoint getGeoPoint(String lat, String lng) {
    Double dlat = new Double(lat);
    Double dlng = new Double(lng);
    int mlat = (int) (dlat.floatValue() * 1E6);
    int mlng = (int) (dlng.floatValue() * 1E6);
    return new GeoPoint(mlat, mlng);
  }

}
