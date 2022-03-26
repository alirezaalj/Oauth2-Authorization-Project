package ir.alirezaalijani.security.authorization.service.security.service.attempt;

import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "application.security.login.fall",name = "service",havingValue = "redis")
public class RedisLoginAttemptServiceImpl implements LoginAttemptService{

    private static final String LOGIN_ATTEMPT_HASH_KEY ="LOGIN_ATTEMPT";
    private static final String LOGIN_UNAME_ATTEMPT_HASH_KEY="LOGIN_UNAME_ATTEMPT";

    private final HashOperations<String,String,LoginAttempt> hashOperations;
    private final ApplicationConfigData configData;
    private long expUnit;

    public RedisLoginAttemptServiceImpl(RedisOperations<String, LoginAttempt> redisOperations,
                                        ApplicationConfigData configData) {
        this.hashOperations=redisOperations.opsForHash();
        this.configData = configData;
        this.expUnit=TimeUnit.HOURS.toMillis(24);
        TimeUnit timeUnit;
        try {
            timeUnit =TimeUnit.of(ChronoUnit.valueOf(this.configData.sec_login_attempt_expire_after_unit));
            this.expUnit = timeUnit.toMillis(this.configData.sec_login_attempt_expire_after_duration);
        }catch (Exception e){
            log.error("LoginAttemptService error : {}",e.getMessage());
        }
    }

    @Override
    public void loginSucceeded(String key) {
        hashOperations.delete(LOGIN_ATTEMPT_HASH_KEY,key);
    }

    @Override
    public void loginFailed(String key){
        var attempt = hashOperations.get(LOGIN_ATTEMPT_HASH_KEY,key);
        if (attempt!=null){
            attempt.increment(1);
        }else {
            attempt=new LoginAttempt(key,1,0);
        }
        attempt.setExpireAt(System.currentTimeMillis()+expUnit);
        hashOperations.put(LOGIN_ATTEMPT_HASH_KEY,key,attempt);
    }

    @Override
    public boolean isBlocked(String key) {
        if (hashOperations.hasKey(LOGIN_ATTEMPT_HASH_KEY,key)){
            LoginAttempt attempt=hashOperations.get(LOGIN_ATTEMPT_HASH_KEY,key);
            if (attempt!=null){
                if (attempt.isExpired()){
                    hashOperations.delete(LOGIN_ATTEMPT_HASH_KEY,key);
                    return false;
                }
                return attempt.getAttempts() >= this.configData.sec_login_fall_max_attempt;
            }
        }
        return false;
    }

    @Override
    public void unameFailed(String authenticationName) {
        var attempt = hashOperations.get(LOGIN_UNAME_ATTEMPT_HASH_KEY,authenticationName);
        if (attempt!=null){
            attempt.increment(1);
        }else {
            attempt=new LoginAttempt(authenticationName,1,0);
        }
        attempt.setExpireAt(System.currentTimeMillis()+TimeUnit.MINUTES.toMillis(5));
        hashOperations.put(LOGIN_UNAME_ATTEMPT_HASH_KEY,authenticationName,attempt);
    }

    @Override
    public void unameSucceeded(String authenticationName) {
        hashOperations.delete(LOGIN_UNAME_ATTEMPT_HASH_KEY,authenticationName);
    }

    @Override
    public boolean unameBlocked(String authenticationName) {
        if (hashOperations.hasKey(LOGIN_UNAME_ATTEMPT_HASH_KEY,authenticationName)){
            LoginAttempt attempt=hashOperations.get(LOGIN_UNAME_ATTEMPT_HASH_KEY,authenticationName);
            if (attempt!=null){
                if (attempt.isExpired()){
                    hashOperations.delete(LOGIN_UNAME_ATTEMPT_HASH_KEY,authenticationName);
                    return false;
                }
                return attempt.getAttempts() >= 5;
            }
        }
        return false;
    }

}
