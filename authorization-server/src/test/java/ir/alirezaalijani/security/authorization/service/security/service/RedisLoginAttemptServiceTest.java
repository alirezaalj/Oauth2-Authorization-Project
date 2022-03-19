package ir.alirezaalijani.security.authorization.service.security.service;

import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RedisLoginAttemptServiceTest {

    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private ApplicationConfigData applicationConfigData;

    @Test
    void test() {
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
}
