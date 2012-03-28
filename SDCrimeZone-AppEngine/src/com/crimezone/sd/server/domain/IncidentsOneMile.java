package com.crimezone.sd.server.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Text;

public class IncidentsOneMile implements IncidentSet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // internal PK
  private String latitude;
  private String longitude;
  private Text incidentSet;
  private String result;

  public IncidentsOneMile() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public Text getIncidentSet() {
    return incidentSet;
  }

  public void setIncidentSet(Text incidentSet) {
    this.incidentSet = incidentSet;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getResult() {
    return result;
  }

  public String getKeyString() {
    return Long.valueOf(id).toString();
  }
}
