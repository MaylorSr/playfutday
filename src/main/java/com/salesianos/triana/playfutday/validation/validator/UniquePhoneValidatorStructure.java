package com.salesianos.triana.playfutday.validation.validator;

import com.salesianos.triana.playfutday.data.user.service.UserService;
import com.salesianos.triana.playfutday.validation.annotation.PhoneStructure;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UniquePhoneValidatorStructure implements ConstraintValidator<PhoneStructure, String> {

    int min, max;
    boolean number;

    @Autowired
    private UserService userService;


    @Override
    public void initialize(PhoneStructure constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        number = constraintAnnotation.hasNumber();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        List<Rule> rules = new ArrayList<>();

        rules.add(new LengthRule(min, max));


        if (number)
            rules.add(new CharacterRule(EnglishCharacterData.Digit, min));


        PasswordValidator phoneValidator = new PasswordValidator(rules);

        RuleResult result = phoneValidator.validate(new PasswordData(s));

        if (result.isValid())
            return true;
        return false;
    }
}
