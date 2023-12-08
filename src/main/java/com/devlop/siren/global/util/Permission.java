package com.devlop.siren.global.util;

import com.devlop.siren.domain.user.domain.UserRole;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
  UserRole authority() default UserRole.CUSTOMER;
}
