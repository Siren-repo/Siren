package com.devlop.siren.item.domain;

import com.devlop.siren.item.dto.DefaultOptionCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "espresso_shot_count", columnDefinition = "TINYINT")
    private Integer espressoShotCount;

    @Column(name = "vanilla_syrup_count", columnDefinition = "TINYINT")
    private Integer vanillaSyrupCount;

    @Column(name = "caramel_syrup_count", columnDefinition = "TINYINT")
    private Integer caramelSyrupCount;

    @Column(name = "hazelnut_syrup_count", columnDefinition = "TINYINT")
    private Integer hazelnutSyrupCount;

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
        this.caramelSyrupCount = this.caramelSyrupCount == null ? 0 : this.vanillaSyrupCount;
        this.hazelnutSyrupCount = this.hazelnutSyrupCount == null ? 0 : this.hazelnutSyrupCount;
    }

    public void update(DefaultOptionCreateRequest defaultOptionCreateRequest) {
        this.espressoShotCount = defaultOptionCreateRequest.getEspressoShotCount();
        this.vanillaSyrupCount = defaultOptionCreateRequest.getVanillaSyrupCount();
        this.caramelSyrupCount = defaultOptionCreateRequest.getCaramelSyrupCount();
        this.hazelnutSyrupCount = defaultOptionCreateRequest.getHazelnutSyrupCount();
        this.size = defaultOptionCreateRequest.getSize();
    }

}