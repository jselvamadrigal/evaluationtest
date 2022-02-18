package com.jselva.nisum.evaluationtest.data.validator;

import com.jselva.nisum.evaluationtest.data.annotation.ValidPassword;
import org.passay.*;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private final String REGEX_PASSWORD;
    private final String REGEX_PASSWORD_ERROR;

    public PasswordConstraintValidator(@Value("${app.password.regexp}") String regexPassword,
                                       @Value("${app.password.error}") String regexPasswordError) {
        REGEX_PASSWORD = regexPassword;
        REGEX_PASSWORD_ERROR = regexPasswordError;
    }

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(new AllowedRegexRule(REGEX_PASSWORD)));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(REGEX_PASSWORD_ERROR)
                .addConstraintViolation();
        return false;
    }
}