package com.crimezone.sd.server.logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.crimezone.sd.server.domain.Incident;

/**
 * 
 * @author Amy
 * 
 */
public class JSONUtils {

  /**
   * TODO: Make this more generic
   * 
   * @param incidents
   * @return
   */
  public static final JSONArray convertIncidentsToJSONArray(List<Incident> incidents) {
    if (incidents == null)
      return null;
    JSONArray jsonObjs = new JSONArray();
    for (Incident incident : incidents) {
      JSONObject ret = new JSONObject();
      try {
        ret.put("address", incident.getAddress());
        ret.put("bcc", incident.getBccCode());
        ret.put("lat", incident.getLatitude());
        ret.put("lng", incident.getLongitude());
        ret.put("year", incident.getYear());
        jsonObjs.put(ret);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return jsonObjs;
  }
}