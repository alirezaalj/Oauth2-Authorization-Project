package ir.alirezaalijani.security.authorization.service.security.config.listener;

import ir.alirezaalijani.security.authorization.service.security.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;

    public AuthenticationSuccessListener(HttpServletRequest request,
                                         LoginAttemptService loginAttemptService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        String ip;
        if (xfHeader == null) {
            ip=request.getRemoteAddr();
        } else {
            ip=xfHeader.split(",")[0];
        }
        log.info("New AuthenticationSuccess ip {} - Authentication.Name {}",ip,e.getAuthentication().getName());
        loginAttemptService.loginSucceeded(request.getRemoteAddr());
    }
}
