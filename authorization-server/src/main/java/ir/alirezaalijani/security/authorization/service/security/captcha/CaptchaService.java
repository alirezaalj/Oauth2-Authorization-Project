package ir.alirezaalijani.security.authorization.service.security.captcha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.Objects;

@Slf4j
@Service("captchaService")
public class CaptchaService extends AbstractCaptchaService {

    @Override
    public boolean processResponse(final String response) {
        if (securityCheck(response)){
            return false;
        }
        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            if (Objects.nonNull(googleResponse)){
                log.debug("Google's response: {} ", googleResponse);
                if (!googleResponse.isSuccess()) {
                    if (googleResponse.hasClientError()) {
                        reCaptchaAttemptService.loginFailed(getClientIP());
                    }
                    log.error("reCaptcha was not successfully validated");
                    return false;
                }
            }
        } catch (RestClientException rce) {
             log.error("Registration unavailable at this time.  Please try again later.",rce);
             return false;
        }
        return true;
    }
}
