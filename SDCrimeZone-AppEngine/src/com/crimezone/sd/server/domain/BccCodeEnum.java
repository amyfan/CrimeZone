package com.crimezone.sd.server.domain;

import java.util.EnumSet;

public enum BccCodeEnum {
  MURDER("1"),
  RAPE("2"),
  ROBBERY("3"),
  ASSAULT("4"),
  BURGLARY("5"),
  THEFT("6"),
  VEHICLE_THEFT("7"),
  ARSON("8"),
  OTHER_CRIMES("A"),
  CHILD_AND_FAMILY("C"),
  DEADLY_WEAPON("D"),
  EMBEZZLEMENT("E"),
  FRAUD("F"),
  GAMBLING("g"),
  MALICIOUS_MISCHIEF("M"),
  NARCOTICS("N"),
  SEX_CRIMES("S"),
  FORGERY("Y"),
  OTHER_NON_CRIMINAL_CODE("Z");

  private String code;

  private BccCodeEnum(String code) {
    this.code = code;
  }

  public String getName() {
    return code;
  }

  public static BccCodeEnum fromCode(String code) {
    for (final BccCodeEnum element : EnumSet.allOf(BccCodeEnum.class)) {
      if (element.getName().equals(code)) {
        return element;
      }
    }
    throw new IllegalArgumentException("Cannot be parsed into an enum element : '" + code + "'");
  }
}
