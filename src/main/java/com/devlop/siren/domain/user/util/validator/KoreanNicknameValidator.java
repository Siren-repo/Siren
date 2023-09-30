package com.devlop.siren.unit.domain.user.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class KoreanNicknameValidator implements ConstraintValidator<KoreanNickname, String> {
    private static final Pattern KOREAN_PATTERN = Pattern.compile("^[가-힣]*$");
    @Override
    public void initialize(KoreanNickname koreanNickname) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return KOREAN_PATTERN.matcher(value).matches();
    }
}
