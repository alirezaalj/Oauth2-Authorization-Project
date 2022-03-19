package ir.alirezaalijani.security.authorization.service.domain.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AgreementValidator implements ConstraintValidator<AgreementValidate, Boolean> {
    @Override
    public void initialize(AgreementValidate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        if (value==null) return false;
        return value;
    }
}
