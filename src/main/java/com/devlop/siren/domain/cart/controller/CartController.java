package com.devlop.siren.domain.cart.controller;

import com.devlop.siren.domain.cart.dto.CartDto;
import com.devlop.siren.domain.cart.service.CartService;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.Permission;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
  private final CartService cartService;

  // 장바구니에 추가
  @PostMapping
  public ApiResponse<CartDto> add(
      @Valid @RequestBody OrderItemRequest orderItemRequest,
      @AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(ResponseCode.Normal.CREATE, cartService.add(orderItemRequest, user));
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
  @PutMapping("/remove")
  public ApiResponse<CartDto> remove(
      @Valid @RequestBody OrderItemRequest orderItemRequest,
      @AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(ResponseCode.Normal.DELETE, cartService.remove(orderItemRequest, user));
  }

  // 장바구니 안에서 특정 요소 수정
  @PutMapping("/update")
  public ApiResponse<CartDto> update(
      @Valid @RequestBody OrderItemRequest orderItemRequest,
      @AuthenticationPrincipal UserDetailsDto user) {
    return ApiResponse.ok(ResponseCode.Normal.UPDATE, cartService.update(orderItemRequest, user));
  }
}
