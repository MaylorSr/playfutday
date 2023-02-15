package com.salesianos.triana.playfutday.validation.annotation;

import com.salesianos.triana.playfutday.validation.validator.UniqueUserEmailValidator;
import com.salesianos.triana.playfutday.validation.validator.UniqueUserPhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserPhoneValidator.class)
@Documented
public @interface UniquePhone {
    String message() default "The phone of the user exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
