package ir.alirezaalijani.security.authorization.service.security.captcha;

import ir.alirezaalijani.security.authorization.service.security.CaptchaConfigData;
import ir.alirezaalijani.security.authorization.service.security.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Slf4j
public abstract class AbstractCaptchaService implements ICaptchaService{

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected CaptchaConfigData captchaSettings;

    @Autowired
    protected LoginAttemptService reCaptchaAttemptService;

    @Autowired
    protected RestOperations restTemplate;

    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    
    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
    
    @Override
    public String getReCaptchaSite() {
        return captchaSettings.getSite();
    }

    @Override
    public String getReCaptchaSecret() {
        return captchaSettings.getSecret();
    }
  

    public boolean securityCheck(final String response) {
        if (!responseSanityCheck(response)) {
           log.error("Response contains invalid characters");
           return true;
        }
        return false;
    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    protected String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
