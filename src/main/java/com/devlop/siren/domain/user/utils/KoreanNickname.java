package com.devlop.siren.domain.user.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = KoreanNicknameValidator.class)
public @interface KoreanNickname {
  String message() default "닉네임은 한글로만 설정할 수 있습니다";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
