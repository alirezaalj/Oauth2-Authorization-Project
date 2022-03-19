package ir.alirezaalijani.security.authorization.service.domain.validators;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellphoneValidator implements
        ConstraintValidator<ContactNumberConstraint, String> {
    private final static Pattern PATTERN = Pattern.compile("^(09|00989|\\+989)\\d{9}$");


    @Override
    public void initialize(ContactNumberConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        if (contactField!=null){
            Matcher matcher = PATTERN.matcher(contactField);
            return matcher.find();
        }
        return false;
    }

}

