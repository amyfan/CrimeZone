package com.crimezone.sd.server.domain;

import java.util.List;

import com.beoui.geocell.model.Point;
import com.google.appengine.api.datastore.Text;

public interface IncidentSet {

  public Long getId();

  public void setId(Long id);

  public Double getLatitude();

  public void setLatitude(Double latitude);

  public Double getLongitude();

  public void setLongitude(Double longitude);

  public Text getIncidentSet();

  public void setIncidentSet(Text incidentSet);

  public void setGeocells(List<String> geocells);

  public List<String> getGeocells();

  public Point getLocation();

  public String getKeyString();
}
