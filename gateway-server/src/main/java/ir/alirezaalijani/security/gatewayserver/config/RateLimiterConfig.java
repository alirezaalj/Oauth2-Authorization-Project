package ir.alirezaalijani.security.gatewayserver.config;

import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RateLimiterConfig {

    @Primary
    @Bean("defaultRateLimiter")
    public RateLimiter<RedisRateLimiter.Config> defaultRateLimiter(){
        return new RedisRateLimiter(10,20,1);
    }
}
