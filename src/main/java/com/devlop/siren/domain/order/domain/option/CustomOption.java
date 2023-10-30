package com.devlop.siren.domain.order.domain.option;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
public abstract class CustomOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "take_out", nullable = false)
    private Boolean takeout;
    @Column(name = "cold", nullable = false)
    @Enumerated(EnumType.STRING)
    private Temperature temperature;
    @Column(name = "additional_price")
    private int price = 0;

    public abstract int getPrice();
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    enum Temperature{
        HOT, COLD
    }
}


