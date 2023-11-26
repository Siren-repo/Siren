package com.devlop.siren.fixture;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import com.devlop.siren.domain.item.entity.option.OptionDetails;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup;
import com.devlop.siren.domain.item.entity.option.SizeType;
import java.util.Set;

public class ItemFixture {
  public static ItemCreateRequest get(CategoryCreateRequest categoryCreateRequest, Integer price) {
    return new ItemCreateRequest(
        categoryCreateRequest,
        "아메리카노",
        price,
        "아메리카노입니다",
        false,
        true,
        "이미지 없음",
        new DefaultOptionCreateRequest(
            new OptionDetails.EspressoDetail(OptionTypeGroup.EspressoType.ORIGINAL, 2),
            Set.of(new OptionDetails.SyrupDetail(OptionTypeGroup.SyrupType.VANILLA, 2)),
            OptionTypeGroup.MilkType.ORIGINAL,
            OptionTypeGroup.FoamType.MILK,
            OptionTypeGroup.DrizzleType.CHOCOLATE,
            SizeType.TALL),
        "우유, 대두",
        new NutritionCreateRequest(0, 2, 3, 0, 1, 2, 2, 0, 0, 0));
  }
}
