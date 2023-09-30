package com.devlop.siren.unit.domain.user.dto.request;

import com.devlop.siren.unit.domain.user.domain.AllergyType;
import com.devlop.siren.unit.domain.user.domain.UserRole;
import com.devlop.siren.unit.domain.user.util.AllergyConverter;
import com.devlop.siren.unit.domain.user.util.validator.KoreanNickname;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.EnumSet;

@Getter
public class UserRegisterRequest {

    @NotBlank(message = "이메일은 필수로 입력해야합니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해야합니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    @KoreanNickname
    private String password;

    @NotBlank(message = "비밀번호는 필수로 입력해야합니다")
    @Size(max = 6, message = "닉네임은 한글로 6자까지 가능합니다")
    private String nickName;

    @NotBlank(message = "핸드폰번호는 필수로 입력해야합니다")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(\\d{3,4})-(\\d{4})$", message = "핸드폰번호 형식에 맞지 않습니다")
    private String phone;

    private String allergies;
}
