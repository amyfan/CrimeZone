package com.crimezone.sd.server.domain;

import com.google.appengine.api.datastore.Text;

public interface IncidentSet {

  public Long getId();

  public void setId(Long id);

  public String getLatitude();

  public void setLatitude(String latitude);

  public String getLongitude();

  public void setLongitude(String longitude);

  public Text getIncidentSet();

  public void setIncidentSet(Text incidentSet);

  public String getKeyString();

  public void setResult(String result);
  
  public String getResult();
}
