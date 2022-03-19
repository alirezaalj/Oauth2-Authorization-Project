package ir.alirezaalijani.security.authorization.service.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameValidate {
    String message() default "Username must only contain (lowercase, or uppercase . _ -) and The number of characters must be between 5 to 20.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
