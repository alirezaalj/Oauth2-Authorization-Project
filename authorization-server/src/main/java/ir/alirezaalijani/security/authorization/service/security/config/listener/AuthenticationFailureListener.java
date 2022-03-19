package ir.alirezaalijani.security.authorization.service.security.config.listener;

import ir.alirezaalijani.security.authorization.service.security.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;

    public AuthenticationFailureListener(HttpServletRequest request,
                                         LoginAttemptService loginAttemptService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent event) {
        
        final String xfHeader = request.getHeader("X-Forwarded-For");
        final String ip;
        if (xfHeader == null) {
            ip = request.getRemoteAddr();
        } else {
            ip=xfHeader.split(",")[0];
        }
        log.error("New AuthenticationFailure ip {} - Authentication.Name {}",ip,event.getAuthentication().getName());
        loginAttemptService.loginFailed(ip);
    }

}
