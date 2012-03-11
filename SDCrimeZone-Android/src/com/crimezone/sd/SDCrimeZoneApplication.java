package com.crimezone.sd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class SDCrimeZoneApplication extends Application{
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
  
  public static void debug(Context context, String out) {
    Log.v(context.getString(R.string.app_name), out);
  }

}
