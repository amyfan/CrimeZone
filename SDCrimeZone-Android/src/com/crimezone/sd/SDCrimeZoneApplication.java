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

  public static final Map<String, Double> bccAverage2011;
  static {
    Map<String, Double> bMap = new HashMap<String, Double>();
    bMap.put("1", 0.002685299);
    bMap.put("2", 0.187970934);
    bMap.put("3", 0.840498606);
    bMap.put("4", 5.196053684);
    bMap.put("5", 7.494669681);
    bMap.put("6", 6.804547822);
    bMap.put("7", 0.005370598);
    bMap.put("8", 0.080558972);
    bMap.put("A", 1.25940526);
    bMap.put("C", 0.42964785);
    bMap.put("D", 0.02953829);
    bMap.put("E", 0.128894355);
    bMap.put("F", 2.121386259);
    bMap.put("G", 0.0);
    bMap.put("M", 2.980681959);
    bMap.put("N", 0.013426495);
    bMap.put("S", 0.238991616);
    bMap.put("Y", 0.08592957);
    bMap.put("Z", 2.704096155);
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
