package com.salesianos.triana.playfutday.validation.annotation;

import com.salesianos.triana.playfutday.validation.validator.UniquePhoneValidatorStructure;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePhoneValidatorStructure.class)
@Documented
public @interface PhoneStructure {
    String message() default "The structure of phone are not correct!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 9;

    int max() default 9;

    boolean hasNumber() default true;
}
