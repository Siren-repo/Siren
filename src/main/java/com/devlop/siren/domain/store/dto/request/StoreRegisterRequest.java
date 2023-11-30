package com.devlop.siren.domain.store.dto.request;

import com.devlop.siren.domain.store.domain.Store;
import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

  @NotBlank(message = "도로명 을 입력 해주세요")
  private String street;

  @NotNull(message = "우편번호를 입력해주세요")
  @Min(value = 10000, message = "우편번호는 최소 5글자 입니다.")
  @Max(value = 99999, message = "우편번호는 5글자를 넘을 수 없습니다.")
  private Integer zipCode;

  private LocalDateTime openTime;
  private LocalDateTime closeTime;

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
