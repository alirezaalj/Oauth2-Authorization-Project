package ir.alirezaalijani.security.authorization.service.domain.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainValidator implements
        ConstraintValidator<DomainValidate, String> {
    public final static Pattern PATTERN = Pattern.compile("^((?!-)[A-Za-z0-9-]"
            + "{1,63}(?<!-)\\.)"
            + "+[A-Za-z]{2,6}");

    @Override
    public void initialize(DomainValidate contactNumber) {
    }

    @Override
    public boolean isValid(String domain,
                           ConstraintValidatorContext cxt) {
        if (domain == null) {
            return false;
        }
        Matcher m = PATTERN.matcher(domain);
        return m.matches();
    }

}
