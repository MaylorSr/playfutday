package com.salesianos.triana.playfutday.validation.validator;

import com.salesianos.triana.playfutday.validation.annotation.StrongPassword;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    int min, max;
    boolean upper, lower;


    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        upper = constraintAnnotation.hasUpper();
        lower = constraintAnnotation.hasLower();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        List<Rule> rules = new ArrayList<>();

        rules.add(new LengthRule(min, max));


        if (upper && lower) {
            rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
            rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        }

        PasswordValidator passwordValidator = new PasswordValidator(rules);

        RuleResult result = passwordValidator.validate(new PasswordData(s));

        if (result.isValid())
            return true;

        List<String> messages = passwordValidator.getMessages(result);
        String template = messages.stream().collect(Collectors.joining(","));
        constraintValidatorContext.buildConstraintViolationWithTemplate(template)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;

    }
}
