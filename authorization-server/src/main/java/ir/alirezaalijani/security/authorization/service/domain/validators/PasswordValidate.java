package ir.alirezaalijani.security.authorization.service.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidate {
    String message() default """
                Password must contain at least (one digit [0-9] and one lowercase Latin character [a-z] and
                 one uppercase Latin character [A-Z] and one special character like ! @ # & ( ).
                Password must contain a length of at least 8 characters and a maximum of 20 characters.
            """;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
