package com.devlop.siren.domain.item.entity.option;

import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "default_options")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DefaultOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long defaultOptionId;

    @Setter
    @Column(name = "espresso_shot_count", columnDefinition = "TINYINT")
    private Integer espressoShotCount;

    @Setter
    @Column(name = "vanilla_syrup_count", columnDefinition = "TINYINT")
    private Integer vanillaSyrupCount;

    @Setter
    @Column(name = "caramel_syrup_count", columnDefinition = "TINYINT")
    private Integer caramelSyrupCount;

    @Setter
    @Column(name = "hazelnut_syrup_count", columnDefinition = "TINYINT")
    private Integer hazelnutSyrupCount;

    @Setter
    @Column(name = "size")
    @NotNull
    @Enumerated(EnumType.STRING)
    private SizeType size;

    @Builder
    public DefaultOption(Integer espressoShotCount, Integer vanillaSyrupCount, Integer caramelSyrupCount, Integer hazelnutSyrupCount, SizeType size) {
        this.espressoShotCount = espressoShotCount;
        this.vanillaSyrupCount = vanillaSyrupCount;
        this.caramelSyrupCount = caramelSyrupCount;
        this.hazelnutSyrupCount = hazelnutSyrupCount;
        this.size = size;
    }

    @PrePersist
    public void prePersist() {
        this.espressoShotCount = this.espressoShotCount == null ? 0 : this.espressoShotCount;
        this.vanillaSyrupCount = this.vanillaSyrupCount == null ? 0 : this.vanillaSyrupCount;
        this.caramelSyrupCount = this.caramelSyrupCount == null ? 0 : this.caramelSyrupCount;
        this.hazelnutSyrupCount = this.hazelnutSyrupCount == null ? 0 : this.hazelnutSyrupCount;
    }

    public void update(DefaultOptionCreateRequest defaultOptionCreateRequest) {
        setEspressoShotCount(defaultOptionCreateRequest.getEspressoShotCount());
        setVanillaSyrupCount(defaultOptionCreateRequest.getVanillaSyrupCount());
        setCaramelSyrupCount(defaultOptionCreateRequest.getCaramelSyrupCount());
        setHazelnutSyrupCount(defaultOptionCreateRequest.getHazelnutSyrupCount());
        setSize(defaultOptionCreateRequest.getSize());
    }

}