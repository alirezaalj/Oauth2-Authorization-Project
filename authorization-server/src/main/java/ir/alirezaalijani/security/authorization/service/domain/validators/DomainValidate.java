package ir.alirezaalijani.security.authorization.service.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DomainValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainValidate {
    String message() default "Invalid domain name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
