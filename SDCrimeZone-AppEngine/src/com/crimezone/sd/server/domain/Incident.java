package com.crimezone.sd.server.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;

@Entity
public class Incident implements LocationCapable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // internal PK
  private Date date;
  private String bccCode;
  private String address;
  private Double latitude;
  private Double longitude;
  private Integer year; // used for queries
  private List<String> geocells;

  public Incident() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getBccCode() {
    return bccCode;
  }

  public void setBccCode(String bccCode) {
    this.bccCode = bccCode;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
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

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String toString() {
    return ("ID: " + this.id + "; address: " + this.address + "; type: " + this.bccCode
        + "; date: " + this.date.toString());
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
