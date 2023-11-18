package com.devlop.siren.domain.item.entity.option;

import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Table(name = "default_options")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DefaultOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long defaultOptionId;

    @Column(name = "espresso")
    @Embedded
    private OptionDetails.EspressoDetail espresso;

    @ElementCollection
    @CollectionTable(name = "default_option_syrup",
            joinColumns = @JoinColumn(name = "default_option_id"))
    @Column(name = "syrup")
    private Set<OptionDetails.SyrupDetail> syrup = new HashSet<>();

    @Column(name = "milk")
    @Enumerated(EnumType.STRING)
    private OptionTypeGroup.MilkType milk;

    @Column(name = "foam")
    @Enumerated(EnumType.STRING)
    private OptionTypeGroup.FoamType foam;

    @Column(name = "drizzle")
    @Enumerated(EnumType.STRING)
    private OptionTypeGroup.DrizzleType drizzle;

    @Column(name = "size")
    @NotNull
    @Enumerated(EnumType.STRING)
    private SizeType size;

    @Builder
    public DefaultOption(OptionDetails.EspressoDetail espresso, Set<OptionDetails.SyrupDetail> syrup
            , OptionTypeGroup.MilkType milk, OptionTypeGroup.FoamType foam, OptionTypeGroup.DrizzleType drizzle, SizeType size) {
        this.espresso = espresso;
        this.syrup = syrup;
        this.milk = milk;
        this.foam = foam;
        this.drizzle = drizzle;
        this.size = size;
    }

    public void update(DefaultOptionCreateRequest defaultOptionCreateRequest) {
        setEspresso(defaultOptionCreateRequest.getEspresso());
        setSyrup(defaultOptionCreateRequest.getSyrup());
        setMilk(defaultOptionCreateRequest.getMilk());
        setSize(defaultOptionCreateRequest.getSize());
        setDrizzle(defaultOptionCreateRequest.getDrizzle());
        setFoam(defaultOptionCreateRequest.getFoam());
    }

    private void setEspresso(OptionDetails.EspressoDetail espresso) {
        this.espresso = espresso;
    }

    private void setSyrup(Set<OptionDetails.SyrupDetail> syrup) {
        this.syrup = syrup;
    }

    private void setMilk(OptionTypeGroup.MilkType milk) {
        this.milk = milk;
    }

    private void setSize(SizeType size) {
        this.size = size;
    }

    public void setFoam(OptionTypeGroup.FoamType foam) {
        this.foam = foam;
    }

    public void setDrizzle(OptionTypeGroup.DrizzleType drizzle) {
        this.drizzle = drizzle;
    }
}