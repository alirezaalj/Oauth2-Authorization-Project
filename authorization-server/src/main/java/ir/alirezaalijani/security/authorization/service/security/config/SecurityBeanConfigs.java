package ir.alirezaalijani.security.authorization.service.security.config;

import ir.alirezaalijani.security.authorization.service.security.service.LoginAttempt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class SecurityBeanConfigs {

    public static final String[] publicPaths = new String[]{
            "/",
            "/auth/login",
            "/assets/**",
            "/favicon.png",
            "/favicon.ico",
            "/error/**",
            "/api/error/**"};

    public static final String[] registerPaths= new String[]{
            "/register/sign-up",
            "/register/resend/verification-email",
            "/verification/email/{token}",
            "/verification/password/{token}",
            "/forget/password",
            "/forget/username",
            "/forget/change/password",
            "/contact"
    };

    private static final String[] recaptchaPaths= new String[]{
            "/auth/login",
            "/register/sign-up",
            "/register/resend/verification-email",
            "/forget/password",
            "/forget/username",
            "forget/change/password",
            "/contact"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static RedisOperations<String, LoginAttempt> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        var redisTemplate = new RedisTemplate<String ,LoginAttempt>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    protected static PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }

    protected static boolean isPublicPath(HttpServletRequest request) {
        if (request!=null){
            String uri = request.getRequestURI();
            for (var path : publicPaths) {
                if (pathMatcher().match(path, uri)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean isRecaptchaPath(HttpServletRequest request){
        if (request!=null){
            HttpMethod method= HttpMethod.valueOf(request.getMethod());
            if (method.equals(HttpMethod.POST)){
                String uri = request.getRequestURI();
                for (var path : recaptchaPaths) {
                    if (pathMatcher().match(path, uri)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
