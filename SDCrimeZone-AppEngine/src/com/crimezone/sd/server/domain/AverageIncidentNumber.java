package com.crimezone.sd.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AverageIncidentNumber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // internal PK
  private Integer radius;
  private Integer year;
  private String bccCode;
  private Integer numberOfIncidents;

  public AverageIncidentNumber() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getRadius() {
    return radius;
  }

  public void setRadius(Integer radius) {
    this.radius = radius;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getBccCode() {
    return bccCode;
  }

  public void setBccCode(String bccCode) {
    this.bccCode = bccCode;
  }

  public Integer getNumberOfIncidents() {
    return numberOfIncidents;
  }

  public void setNumberOfIncidents(Integer numberOfIncidents) {
    this.numberOfIncidents = numberOfIncidents;
  }

}
