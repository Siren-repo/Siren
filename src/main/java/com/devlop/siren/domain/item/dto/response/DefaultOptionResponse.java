package com.devlop.siren.domain.item.dto.response;

import com.devlop.siren.domain.item.entity.option.DefaultOption;
import com.devlop.siren.domain.item.entity.option.OptionDetails;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup;
import com.devlop.siren.domain.item.entity.option.SizeType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultOptionResponse {

  private OptionDetails.EspressoDetail espresso;
  private Set<OptionDetails.SyrupDetail> syrup;
  private OptionTypeGroup.MilkType milk;
  private OptionTypeGroup.FoamType foam;
  private OptionTypeGroup.DrizzleType drizzle;
  private SizeType size;

  public static DefaultOptionResponse from(DefaultOption defaultOption) {
    return new DefaultOptionResponse(
        defaultOption.getEspresso(),
        defaultOption.getSyrup(),
        defaultOption.getMilk(),
        defaultOption.getFoam(),
        defaultOption.getDrizzle(),
        defaultOption.getSize());
  }
}
