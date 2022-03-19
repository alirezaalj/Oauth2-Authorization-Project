package ir.alirezaalijani.security.authorization.service.config;

import ir.alirezaalijani.security.authorization.service.mail.MailSendException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@EnableAsync
@Configuration
public class CommonConfig {

    private final RetryConfigData retryConfigData;

    public CommonConfig(RetryConfigData retryConfigData) {
        this.retryConfigData = retryConfigData;
    }

    @Bean
    public RestOperations restOperations(){
        return new RestTemplate();
    }

    @Bean
    public RetryOperations retryOperations(){
        RetryTemplate retryTemplate = new RetryTemplate();
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();

        retryableExceptions.put(RestClientException.class, true); // HTTP 404, 503, etc.
//        retryableExceptions.put(WebClientException.class, true); // HTTP 404, 503, etc
        retryableExceptions.put(MailSendException.class,true);  // email
        retryableExceptions.put(MessagingException.class,true); // email

        // Retry exception blacklist
        retryableExceptions.put(HttpClientErrorException.BadRequest.class, false); // HTTP 400
        retryableExceptions.put(HttpServerErrorException.InternalServerError.class, false); // HTTP 500

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());
        exponentialBackOffPolicy.setMaxInterval(retryConfigData.getMaxIntervalMs());
        exponentialBackOffPolicy.setMultiplier(retryConfigData.getMultiplier());

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(retryConfigData.getMaxAttempts(),retryableExceptions);

        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }
}
