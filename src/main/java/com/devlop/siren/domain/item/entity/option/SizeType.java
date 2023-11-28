package com.devlop.siren.domain.item.entity.option;

import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum SizeType {
  TALL("Tall", "355ml"),
  GRANDE("Grande", "473ml"),
  VENTI("Venti", "591ml"),
  TRENTA("Trenta", "887ml");

  private final String englishName;
  private final String amount;

  SizeType(String englishName, String amount) {
    this.amount = amount;
    this.englishName = englishName;
  }

  @JsonCreator
  public static SizeType of(String size) {
    return Arrays.stream(values())
        .filter(sizeType -> sizeType.englishName.equals(size) || sizeType.name().equals(size))
        .findFirst()
        .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.INVALID_SIZE_TYPE));
  }
}
