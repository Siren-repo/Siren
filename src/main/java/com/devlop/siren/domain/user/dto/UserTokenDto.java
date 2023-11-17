package com.devlop.siren.domain.user.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserTokenDto {
    private String accessToken;
    private final String refreshToken;

    @Builder
    public UserTokenDto(String accessToken, String refreshToken) {
        this.accessToken = Objects.requireNonNull(accessToken);
        this.refreshToken = Objects.requireNonNull(refreshToken);
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
