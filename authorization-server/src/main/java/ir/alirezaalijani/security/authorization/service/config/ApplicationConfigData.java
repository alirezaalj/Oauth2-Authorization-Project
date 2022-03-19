package ir.alirezaalijani.security.authorization.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfigData {
    @Value("${application.info.name:Auth Service}")
    public String application_name;
    @Value("${application.info.host:http://localhost:9000}")
    public String application_host;
    @Value("${application.info.contact-email:contact@alirezaalijani.ir.com}")
    public String application_contact_mail;
    @Value("${application.security.encryption.token.secret-key:defKey}")
    public String sec_enc_token_key;
    @Value("${application.security.encryption.token.salt:5c0744940b5c369b}")
    public String sec_enc_token_salt;
    @Value("${application.security.login.theme:default}")
    public String sec_login_theme;
    @Value("${application.security.login.validator.validate-url}")
    public String sec_login_email_validate_url;
    @Value("${application.security.login.fall.max-attempt:7}")
    public long sec_login_fall_max_attempt;
    @Value("${application.security.login.fall.expire-after.duration:1}")
    public long sec_login_attempt_expire_after_duration;
    @Value("${application.security.login.fall.expire-after.unit:DAYS}")
    public String sec_login_attempt_expire_after_unit;

}
