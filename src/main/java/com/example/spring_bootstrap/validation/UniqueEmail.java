package com.example.spring_bootstrap.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
public @interface UniqueEmail {
    String message() default "User with this email already existed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {

    };
}
