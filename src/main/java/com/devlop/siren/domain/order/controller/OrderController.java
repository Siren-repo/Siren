package com.devlop.siren.domain.order.controller;

import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.response.OrderCreateResponse;
import com.devlop.siren.domain.order.service.OrderUseCase;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderUseCase orderUseCase;

    @PostMapping
    public ApiResponse<OrderCreateResponse> create(@Valid @RequestBody OrderCreateRequest request,
                                                   @AuthenticationPrincipal UserDetailsDto user){
        return ApiResponse.ok(ResponseCode.Normal.CREATE, orderUseCase.create(request, user));
    }
}
