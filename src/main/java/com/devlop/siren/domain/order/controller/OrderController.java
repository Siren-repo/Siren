package com.devlop.siren.domain.order.controller;

import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.request.OrderStatusRequest;
import com.devlop.siren.domain.order.dto.response.OrderDetailResponse;
import com.devlop.siren.domain.order.service.OrderService;
import com.devlop.siren.domain.order.service.OrderUseCase;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.common.response.ResponseCode.Normal;
import com.devlop.siren.global.util.Permission;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
  private final OrderUseCase orderUseCase;
  private final OrderService orderService;

  @PostMapping
  public ApiResponse<OrderDetailResponse> create(
      @Valid @RequestBody OrderCreateRequest request,
      @AuthenticationPrincipal UserDetailsDto requestUser) {
    return ApiResponse.ok(ResponseCode.Normal.CREATE, orderUseCase.create(request, requestUser));
  }

  @Permission(role = {UserRole.ADMIN, UserRole.STAFF})
  @PutMapping("/{orderId}/cancel")
  public ApiResponse<OrderDetailResponse> cancel(
      @PathVariable @NotNull Long orderId, @AuthenticationPrincipal UserDetailsDto requestUser) {
    return ApiResponse.ok(Normal.UPDATE, orderService.cancel(orderId, requestUser));
  }

  @Permission(role = {UserRole.ADMIN, UserRole.STAFF})
  @PutMapping("/status")
  public ApiResponse<OrderDetailResponse> updateStatus(
      @RequestBody @Valid OrderStatusRequest request,
      @AuthenticationPrincipal UserDetailsDto requestUser) {
    return ApiResponse.ok(Normal.UPDATE, orderService.updateStatus(request, requestUser));
  }
}
