package com.devlop.siren.domain.user.controller;

import com.devlop.siren.domain.user.dto.UserTokenDto;
import com.devlop.siren.domain.user.dto.request.UserLoginRequest;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtTokenUtils utils;

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody UserRegisterRequest request){
        userService.register(request);
        return ApiResponse.ok(ResponseCode.Normal.CREATE, null);
    }

    @PostMapping("/login")
    public ApiResponse<UserTokenDto> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response){
        UserTokenDto tokenDto = userService.login(request, response);
        return ApiResponse.ok(ResponseCode.Normal.CREATE, tokenDto);
    }

    @PatchMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request){
        UserTokenDto token = utils.resolveToken(request);
        userService.logout(token);
        return ApiResponse.ok(ResponseCode.Normal.UPDATE, null);
    }

    @PatchMapping("/reissue")
    public ApiResponse<UserTokenDto> reissue(HttpServletRequest request, HttpServletResponse response){
        UserTokenDto token = utils.resolveToken(request);
        String newAccessToken = userService.reissueAccessToken(token.getRefreshToken(), response);
        token.setAccessToken(newAccessToken);
        return ApiResponse.ok(ResponseCode.Normal.UPDATE, token);
    }


}