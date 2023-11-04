package com.devlop.siren.domain.item.entity.option;

public class OptionTypeGroup {

    public enum Temperature{
        HOT, COLD, NONE
    }
    public enum Amount{
        LITTLE, NORMAL, MUCH, NONE;
    }
    public enum EspressoType{
        ORIGINAL, DECAFFEINE
    }
    public enum MilkType{
        ORIGINAL, LOW_FAT, FAT_FREE, SOY, OAT
    }
    public enum FoamType{
        MILK, ESPRESSO, MATCHA
    }
    public enum SyrupType{
        VANILLA, CARAMEL, HAZELNUT, CLASSIC;
    }
    public enum DrizzleType{
        CHOCOLATE, CARAMEL;
    }
}
