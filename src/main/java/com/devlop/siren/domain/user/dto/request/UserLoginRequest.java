package com.devlop.siren.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "이메일은 필수로 입력해야합니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해야합니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
