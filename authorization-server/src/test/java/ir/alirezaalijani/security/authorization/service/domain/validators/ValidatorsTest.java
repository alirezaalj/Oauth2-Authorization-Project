package ir.alirezaalijani.security.authorization.service.domain.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorsTest {

    @Test
    void usernameTest(){
        var validator=new UsernameValidator();
        assertFalse(validator.isValid("@ali =155",null));
        assertFalse(validator.isValid("Alitrza..",null));
        assertFalse(validator.isValid(".Alitrza",null));
        assertFalse(validator.isValid("../Alitrza",null));

        assertTrue(validator.isValid("Alirzea_Alijani",null));
        assertTrue(validator.isValid("Alirzea.Alijani15",null));
    }

    @Test
    void passwordTest(){
        var validator=new PasswordValidator();
        assertFalse(validator.isValid("alireza",null));
        assertFalse(validator.isValid("ALIREZA",null));
        assertFalse(validator.isValid("Alireza12345",null));
        assertFalse(validator.isValid("13131546546",null));

        assertTrue(validator.isValid("Alirezai12345@$",null));
        assertTrue(validator.isValid("1SomPass152#$%",null));
    }
}
