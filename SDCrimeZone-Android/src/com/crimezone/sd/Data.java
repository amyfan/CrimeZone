package com.crimezone.sd;

import com.google.gson.annotations.SerializedName;

public class Data {
  @SerializedName("address")
  private String address;
  @SerializedName("bcc")
  private String bcc;
  @SerializedName("lat")
  private String lat;
  @SerializedName("year")
  private String year;
  @SerializedName("lng")
  private String lng;
  public void setAddress(String address) {
    this.address = address;
  }
  public String getAddress() {
    return address;
  }
  public void setBcc(String bcc) {
    this.bcc = bcc;
  }
  public String getBcc() {
    return bcc;
  }
  public void setLat(String lat) {
    this.lat = lat;
  }
  public String getLat() {
    return lat;
  }
  public void setYear(String year) {
    this.year = year;
  }
  public String getYear() {
    return year;
  }
  public void setLng(String lng) {
    this.lng = lng;
  }
  public String getLng() {
    return lng;
  }
  
}
