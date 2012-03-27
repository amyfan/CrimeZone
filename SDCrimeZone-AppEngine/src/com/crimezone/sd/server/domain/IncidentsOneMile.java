package com.crimezone.sd.server.domain;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;
import com.google.appengine.api.datastore.Text;

public class IncidentsOneMile implements IncidentSet, LocationCapable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // internal PK
  private Double latitude;
  private Double longitude;
  private Text incidentSet;
  private List<String> geocells;

  public IncidentsOneMile() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Text getIncidentSet() {
    return incidentSet;
  }

  public void setIncidentSet(Text incidentSet) {
    this.incidentSet = incidentSet;
  }

  public void setGeocells(List<String> geocells) {
    this.geocells = geocells;
  }

  public List<String> getGeocells() {
    return geocells;
  }

  public Point getLocation() {
    return new Point(latitude, longitude);
  }

  public String getKeyString() {
    return Long.valueOf(id).toString();
  }
}
