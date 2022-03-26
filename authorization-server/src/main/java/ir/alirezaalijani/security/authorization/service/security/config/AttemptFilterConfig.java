package ir.alirezaalijani.security.authorization.service.security.config;

import ir.alirezaalijani.security.authorization.service.security.service.attempt.LoginAttemptService;
import ir.alirezaalijani.security.authorization.service.security.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AttemptFilterConfig extends OncePerRequestFilter {
    private final LoginAttemptService loginAttemptService;

    public AttemptFilterConfig(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String ip = HttpUtil.getClientIP(request);
        if (SecurityBeanConfigs.isLoginProcess(request)) {
            String uname = request.getParameter("uname");
            if (uname != null && loginAttemptService.unameBlocked(uname)) {
                log.info("Username {} is in black list requested ip {}", uname,ip);
                response.sendError(412, "Username is limited");
            }else if (loginAttemptService.isBlocked(ip)) {
                log.info("Ip {} is in block list login process", ip);
                response.sendError(429, "Ip is limited");
            }else {
                filterChain.doFilter(request,response);
            }
        }else {
            if (!SecurityBeanConfigs.isPublicPath(request)) {
                if (loginAttemptService.isBlocked(ip)) {
                    log.info("Ip {} is in block list private path process", ip);
                    response.sendError(429, "Ip is limited");
                }else {
                    filterChain.doFilter(request, response);
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }
    }




}
