package com.devlop.siren.domain.item.entity.option;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OptionTypeGroup {

  public enum Temperature {
    HOT,
    COLD,
    NONE
  }

  public enum Amount {
    LITTLE,
    NORMAL,
    MUCH,
    NONE;
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
    MATCHA
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
  }
}
