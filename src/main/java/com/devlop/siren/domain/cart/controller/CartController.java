package com.devlop.siren.domain.cart.controller;

import com.devlop.siren.domain.cart.dto.CartDto;
import com.devlop.siren.domain.cart.dto.ItemDto;
import com.devlop.siren.domain.cart.service.CartService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
  private final CartService cartService;

  // 장바구니에 추가
  @PostMapping
  public ApiResponse<CartDto> add(
          @Valid @RequestBody ItemDto itemDto, @AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(ResponseCode.Normal.CREATE, cartService.add(itemDto, user));
  }

  // 장바구니에서 찾기
  @GetMapping
  public ApiResponse<CartDto> find(@AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, cartService.retrieve(user));
  }

  // 장바구니 안의 모든 요소들 삭제
  @DeleteMapping("/all")
  public ApiResponse<?> removeAll(@AuthenticationPrincipal UserDetailsDto user) {
    cartService.removeAll(user);
    return ApiResponse.ok(ResponseCode.Normal.DELETE, null);
  }

  // 장바구니 안의 특정 요소 삭제
  @DeleteMapping("/{itemId}")
  public ApiResponse<CartDto> remove(
          @Min(1L) @PathVariable Long itemId, @AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(ResponseCode.Normal.DELETE, cartService.remove(itemId, user));
  }

  // 장바구니 안에서 특정 요소 수정
  @PutMapping
  public ApiResponse<CartDto> update(
      @Valid @RequestBody ItemDto itemDto, @AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(ResponseCode.Normal.DELETE, cartService.update(itemDto, user));
  }
}
