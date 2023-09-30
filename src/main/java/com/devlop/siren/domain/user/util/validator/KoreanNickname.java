package com.devlop.siren.unit.domain.user.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = KoreanNicknameValidator.class)
public @interface KoreanNickname {
    String message() default "닉네임은 한글로만 입력 가능합니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
