package com.devlop.siren.domain.item.entity.option;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OptionTypeGroup {

  public enum Temperature {
    HOT,
    COLD,
    NONE;

    @JsonCreator
    public Temperature fromString(String value) {
      return Temperature.valueOf(value.toUpperCase());
    }
  }

  public enum Amount {
    LITTLE,
    NORMAL,
    MUCH,
    NONE;

    @JsonCreator
    public Amount fromString(String value) {
      return Amount.valueOf(value.toUpperCase());
    }
  }

  public enum EspressoType {
    ORIGINAL,
    DECAFFEINE;

    @JsonCreator
    public EspressoType fromString(String value) {
      return EspressoType.valueOf(value.toUpperCase());
    }
  }

  public enum MilkType {
    ORIGINAL,
    LOW_FAT,
    FAT_FREE,
    SOY,
    OAT;

    @JsonCreator
    public MilkType fromString(String value) {
      return MilkType.valueOf(value.toUpperCase());
    }
  }

  public enum FoamType {
    MILK,
    ESPRESSO,
    MATCHA;

    @JsonCreator
    public FoamType fromString(String value) {
      return FoamType.valueOf(value.toUpperCase());
    }
  }

  public enum SyrupType {
    VANILLA,
    CARAMEL,
    HAZELNUT,
    CLASSIC;

    @JsonCreator
    public SyrupType fromString(String value) {
      return SyrupType.valueOf(value.toUpperCase());
    }
  }

  public enum DrizzleType {
    CHOCOLATE,
    CARAMEL;

    @JsonCreator
    public DrizzleType fromString(String value) {
      return DrizzleType.valueOf(value.toUpperCase());
    }
  }
}
