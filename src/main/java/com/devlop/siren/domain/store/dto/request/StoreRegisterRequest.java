package com.devlop.siren.domain.store.dto.request;

import com.devlop.siren.domain.store.domain.Store;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRegisterRequest {

  @NotBlank(message = "매장 이름을 입력해주세요 ")
  @Length(min = 2, max = 255, message = "매장 이름은 최소 2글자 ~ 255글자 입니다.")
  private String storeName;

  @NotBlank(message = "매장 번호를 입력 해주세요")
  private String storePhone;

  @NotBlank(message = "도시를 입력해주세요")
  private String city;

  @NotBlank(message = "도로명을 입력해주세요")
  private String street;

  @NotBlank(message = "우편번호를 입력해주세요")
  @Length(min = 5, max = 5, message = "우편번호는 숫자 5자리로 이루어져있습니다")
  @Pattern(regexp = "\\d{5}", message = "우편번호는 숫자 5자리로 이루어져있습니다")
  private String zipCode;

  private LocalTime openTime;
  private LocalTime closeTime;

  public static Store from(StoreRegisterRequest registerDto, Double lat, Double lon) {
    return Store.builder()
        .storeName(registerDto.storeName)
        .storePhone(registerDto.storePhone)
        .city(registerDto.city)
        .street(registerDto.street)
        .zipCode(registerDto.zipCode)
        .openTime(registerDto.openTime)
        .closeTime(registerDto.closeTime)
        .latitude(lat)
        .longitude(lon)
        .build();
  }
}
