package com.devlop.siren.domain.order.domain.option;

import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.Temperature;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class CustomOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_option_id")
    private Long id;

    @Column(name = "take_out", nullable = false)
    protected Boolean takeout;

    @Column(name = "temperature", nullable = false)
    @Enumerated(EnumType.STRING)
    protected Temperature temperature;

    @Column(name = "additional_amount")
    protected int amount = 0;

    public abstract int getAdditionalAmount();
}


