package ir.alirezaalijani.security.authorization.service.security.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Primary
@Component
@ConditionalOnProperty(prefix = "application.security.login.fall",name = "service",havingValue = "memory")
public class LoginAttemptServiceImpl  implements LoginAttemptService{

    private final LoadingCache<String, Integer> attemptsCache;
    private final ApplicationConfigData configData;
    public LoginAttemptServiceImpl(ApplicationConfigData configData) {
        this.configData=configData;
        this.attemptsCache = buildCache();
    }

    private LoadingCache<String, Integer> buildCache(){
        TimeUnit timeUnit;
        try {
            timeUnit =TimeUnit.of(ChronoUnit.valueOf(this.configData.sec_login_attempt_expire_after_unit));
        }catch (Exception e){
            log.error("LoginAttemptService error : {}",e.getMessage());
            timeUnit=TimeUnit.DAYS;
        }
        return CacheBuilder.newBuilder().expireAfterWrite(this.configData.sec_login_attempt_expire_after_duration, timeUnit).build(new CacheLoader<>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(final String key) {
        this.attemptsCache.invalidate(key);
    }

    public void loginFailed(final String key) {

        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            log.error(e.getMessage());
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key) >= this.configData.sec_login_fall_max_attempt;
        } catch (final ExecutionException e) {
            return false;
        }
    }
}
