package com.salesianos.triana.playfutday.validation.annotation;

import com.salesianos.triana.playfutday.validation.validator.FieldsPasswordMatchValidator;
import com.salesianos.triana.playfutday.validation.validator.FieldsValueMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldsPasswordMatchValidator.class)
@Documented
public @interface FieldsPasswordChange {

    String message() default "The new password are similar to old password!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String fieldMatch();
}
