package com.devlop.siren.domain.user.controller;

import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody UserRegisterRequest request){
        userService.register(request);
        return ApiResponse.ok(ResponseCode.Normal.CREATE, null);
    }


}
