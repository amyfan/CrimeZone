package com.crimezone.sd;

import java.util.EnumSet;

public enum BccCodeEnum {
  MURDER("1", "Murder"),
  RAPE("2", "Rape"),
  ROBBERY("3", "Robbery"),
  ASSAULT("4", "Assault"),
  BURGLARY("5", "Burglary"),
  THEFT("6", "Theft"),
  VEHICLE_THEFT("7", "Vehicle Theft"),
  ARSON("8", "Arson"),
  OTHER_CRIMES("A", "Other Crimes"),
  CHILD_AND_FAMILY("C", "Child & Family"),
  DEADLY_WEAPON("D", "Deadly Weapon"),
  EMBEZZLEMENT("E", "Embezzlement"),
  FRAUD("F", "Fraud"),
  GAMBLING("G", "Gambling"),
  MALICIOUS_MISCHIEF("M", "Malicious Mischief"),
  NARCOTICS("N", "Narcotics"),
  SEX_CRIMES("S", "Sex Crimes"),
  FORGERY("Y", "Forgery"),
  OTHER_NON_CRIMINAL_CODE("Z", "Other Non-criminal incidents");

  private String code;
  private String name;

  private BccCodeEnum(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public static BccCodeEnum fromCode(String code) {
    for (final BccCodeEnum element : EnumSet.allOf(BccCodeEnum.class)) {
      if (element.getCode().equals(code)) {
        return element;
      }
    }
    throw new IllegalArgumentException("Cannot be parsed into an enum element : '" + code + "'");
  }
  
  public static BccCodeEnum fromName(String name) {
    for (final BccCodeEnum element : EnumSet.allOf(BccCodeEnum.class)) {
      if (element.getName().equals(name)) {
        return element;
      }
    }
    throw new IllegalArgumentException("Cannot be parsed into an enum element : '" + name + "'");
  }
}