package ir.alirezaalijani.security.authorization.service.domain.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgreementValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgreementValidate {
    String message() default "Agreement is required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
