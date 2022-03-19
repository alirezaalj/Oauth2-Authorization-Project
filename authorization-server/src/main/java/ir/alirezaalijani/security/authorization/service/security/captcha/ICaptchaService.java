package ir.alirezaalijani.security.authorization.service.security.captcha;


public interface ICaptchaService {
    
    default boolean processResponse(final String response){return false;}
    
    default boolean processResponse(final String response, String action){return false;}

    String getReCaptchaSite();

    String getReCaptchaSecret();
}
