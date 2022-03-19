package ir.alirezaalijani.security.authorization.service.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class CaptchaConfigData {
    private String site;
    private String secret;

    //reCAPTCHA V3
    private String siteV3;
    private String secretV3;
    private float threshold;
}
