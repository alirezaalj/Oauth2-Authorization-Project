package ir.alirezaalijani.security.authorization.service.security.service;

import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import ir.alirezaalijani.security.authorization.service.security.service.attempt.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LoginAttemptServiceTest {

    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private ApplicationConfigData applicationConfigData;

    @Test
    void ipTest() {
        var ip1 = "127.0.0.1";
        loginAttemptService.loginSucceeded(ip1);
        assertFalse(loginAttemptService.isBlocked(ip1));
        for (int i = 0; i < applicationConfigData.sec_login_fall_max_attempt; i++) {
            loginAttemptService.loginFailed(ip1);
        }
        assertTrue(loginAttemptService.isBlocked(ip1));
        loginAttemptService.loginSucceeded(ip1);

        var ip2 = "127.0.0.2";
        loginAttemptService.loginSucceeded(ip2);
        loginAttemptService.loginFailed(ip2);
        assertFalse(loginAttemptService.isBlocked(ip2));
        loginAttemptService.loginSucceeded(ip2);
        assertFalse(loginAttemptService.isBlocked(ip2));
    }

    @Test
    void unameTest(){
        var user1 = "admin";
        loginAttemptService.unameSucceeded(user1);
        assertFalse(loginAttemptService.unameBlocked(user1));
        for (int i = 0; i < 6; i++) {
            loginAttemptService.unameFailed(user1);
        }
        assertTrue(loginAttemptService.unameBlocked(user1));
        loginAttemptService.unameSucceeded(user1);

        var user2 = "user";
        loginAttemptService.unameSucceeded(user2);
        loginAttemptService.unameFailed(user2);
        assertFalse(loginAttemptService.unameBlocked(user2));
        loginAttemptService.unameSucceeded(user2);
        assertFalse(loginAttemptService.unameBlocked(user2));
    }
}
