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
}
