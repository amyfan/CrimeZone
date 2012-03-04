package com.crimezone.sd.server.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Incident {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // internal PK
  private Date date;
  private String address;
  private String crimeType;

  // ... etc.

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

  public String getCrimeType() {
    return crimeType;
  }

  public void setCrimeType(String crimeType) {
    this.crimeType = crimeType;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

}
