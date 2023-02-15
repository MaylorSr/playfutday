package com.salesianos.triana.playfutday.validation.validator;

import com.salesianos.triana.playfutday.data.user.service.UserService;
import com.salesianos.triana.playfutday.validation.annotation.UniqueEmail;
import com.salesianos.triana.playfutday.validation.annotation.UniquePhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserPhoneValidator implements ConstraintValidator<UniquePhone, String> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s.isEmpty()) {
            return true;
        }
        return StringUtils.hasText(s) && !userService.userPhoneUnique(s);
    }
}
