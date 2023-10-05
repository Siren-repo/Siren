package com.devlop.siren.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserTokenDto {
    private final String accessToken;
    private final String refreshToken;
    private final Long refreshTokenExpiredMs;

    @Builder
    public UserTokenDto(String accessToken, String refreshToken, Long refreshTokenExpiredMs) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredMs = refreshTokenExpiredMs;
    }
}
