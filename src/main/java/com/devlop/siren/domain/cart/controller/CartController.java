package com.devlop.siren.domain.cart.controller;

import com.devlop.siren.domain.cart.dto.CartDto;
import com.devlop.siren.domain.cart.dto.ItemDto;
import com.devlop.siren.domain.cart.service.CartService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;

    // 장바구니에 추가
    @PostMapping
    public CartDto save(@RequestBody ItemDto itemDto, @AuthenticationPrincipal UserDetailsDto user){
        return cartService.addToCart(itemDto, user);
    }

    // 장바구니에서 찾기
    @GetMapping
    public Map<Long, Long> find(@AuthenticationPrincipal UserDetailsDto user){
        return cartService.getCart(user);
    }

    @PutMapping
    public CartDto update(@RequestBody ItemDto itemDto, @AuthenticationPrincipal UserDetailsDto user){
        return cartService.updateCart(user, itemDto);
    }

    @DeleteMapping
    public CartDto remove(@RequestBody ItemDto itemDto, @AuthenticationPrincipal UserDetailsDto user){
        return cartService.removeFromCart(user, itemDto);
    }
}
