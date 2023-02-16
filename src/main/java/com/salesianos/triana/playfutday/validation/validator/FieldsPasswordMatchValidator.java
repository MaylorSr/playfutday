package com.salesianos.triana.playfutday.validation.validator;

import com.salesianos.triana.playfutday.validation.annotation.FieldsPasswordChange;
import com.salesianos.triana.playfutday.validation.annotation.FieldsValueMatch;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsPasswordMatchValidator implements ConstraintValidator<FieldsPasswordChange, Object> {
    private String oldPasswordField;
    private String newPasswordField;

    @Override
    public void initialize(FieldsPasswordChange constraintAnnotation) {
        this.oldPasswordField = constraintAnnotation.field();
        this.newPasswordField = constraintAnnotation.fieldMatch();

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {

        String oldPassword = (String) PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(oldPasswordField);

        String newPassword = (String) PropertyAccessorFactory
                .forBeanPropertyAccess(o).getPropertyValue(newPasswordField);

        return StringUtils.hasText(oldPassword) && !oldPassword.equals(newPassword);
    }

}
