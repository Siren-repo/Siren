package com.devlop.siren.global.util;


import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;

public class UserInformation {
    public static boolean validAdmin(UserDetailsDto user) {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new GlobalException(ResponseCode.ErrorCode.NOT_AUTHORITY_USER);
        }
        return true;
    }

    public static boolean validStaffOrAdmin(UserDetailsDto user) {
        if (!(user.getUserRole().equals(UserRole.STAFF) || user.getUserRole().equals(UserRole.ADMIN))) {
            throw new GlobalException(ResponseCode.ErrorCode.NOT_AUTHORITY_USER);
        }
        return true;
    }
}
