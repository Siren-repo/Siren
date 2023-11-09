package com.devlop.siren.domain.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class UserRoleChangeRequest {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String roleType;

    public UserRoleChangeRequest(String userEmail, String roleType) {
        this.userEmail = userEmail;
        this.roleType = roleType;
    }
}
