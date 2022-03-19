package ir.alirezaalijani.security.authorization.service.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Test
    void emailVerificationTest(){

    }

    @Test
    void userExistByEmailAndEmailVerifiedTest(){
    }
}
