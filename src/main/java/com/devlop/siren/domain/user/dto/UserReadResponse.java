package com.devlop.siren.domain.user.dto;

import com.devlop.siren.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class UserReadResponse {
    private String email;
    private String nickName;
    private String phone;
    private String userRole;
    @Builder
    public UserReadResponse(String email, String nickName, String phone, String userRole) {
        this.email = email;
        this.nickName = nickName;
        this.phone = phone;
        this.userRole = userRole;
    }
    public static UserReadResponse of(User user){
        return UserReadResponse.builder()
                .email(user.getEmail())
                .userRole(user.getRole().getDesc())
                .nickName(user.getNickName())
                .phone(user.getPhone())
                .build();
    }
}
