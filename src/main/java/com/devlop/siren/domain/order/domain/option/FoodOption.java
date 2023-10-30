package com.devlop.siren.domain.order.domain.option;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("Food")
@Table(name = "food_options")
public class FoodOption extends CustomOption{
    protected FoodOption() {
        this.setTemperature(CustomOption.Temperature.COLD);
    }
    @Override
    public int getPrice() {
        return 0;
    }
}
