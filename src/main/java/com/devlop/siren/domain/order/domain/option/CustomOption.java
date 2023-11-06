package com.devlop.siren.domain.order.domain.option;

import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.*;

import javax.persistence.*;

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


