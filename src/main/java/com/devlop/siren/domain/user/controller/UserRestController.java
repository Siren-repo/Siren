package com.devlop.siren.unit.domain.user.controller;

import com.devlop.siren.unit.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody UserRegisterRequest request){
        return ApiResponse.success(userService.register(request));
    }

}
