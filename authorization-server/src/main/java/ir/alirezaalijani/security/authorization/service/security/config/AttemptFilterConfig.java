package ir.alirezaalijani.security.authorization.service.security.config;

import ir.alirezaalijani.security.authorization.service.security.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String ip = getClientIP(request);
        if (!SecurityBeanConfigs.isPublicPath(request)){
            if (loginAttemptService.isBlocked(ip)) {
                log.info("Ip {} is in block list " , ip);
                response.sendError(429,"Ip is limited");
            }else {
                filterChain.doFilter(request,response);
            }
        }else {
            filterChain.doFilter(request,response);
        }
    }

    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
