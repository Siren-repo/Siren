package com.devlop.siren.domain.item.dto.request;

import com.devlop.siren.domain.item.entity.option.DefaultOption;
import com.devlop.siren.domain.item.entity.option.OptionDetails;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup;
import com.devlop.siren.domain.item.entity.option.SizeType;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultOptionCreateRequest {

  private OptionDetails.EspressoDetail espresso;
  private Set<OptionDetails.SyrupDetail> syrup;
  private OptionTypeGroup.MilkType milk;
  private OptionTypeGroup.FoamType foam;
  private OptionTypeGroup.DrizzleType drizzle;

  @NotNull(message = "음료의 크기가 입력되지 않았습니다.")
  private SizeType size;

  public static DefaultOption toEntity(DefaultOptionCreateRequest defaultOptionCreateRequest) {
    return DefaultOption.builder()
        .espresso(defaultOptionCreateRequest.espresso)
        .syrup(defaultOptionCreateRequest.syrup)
        .milk(defaultOptionCreateRequest.milk)
        .size(defaultOptionCreateRequest.size)
        .foam(defaultOptionCreateRequest.foam)
        .drizzle(defaultOptionCreateRequest.drizzle)
        .build();
  }
}
