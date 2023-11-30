package com.devlop.siren.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @NotNull(message = "아이템 번호가 입력 되지 않았습니다.")
    private Long itemId;
    @NotNull(message = "아이템 수량이 입력 되지 않았습니다.")
    private Long quantity;
}
