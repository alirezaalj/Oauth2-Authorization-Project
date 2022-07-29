package ir.alirezaalijani.security.authorization.service.initializers;


import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class AppStartupInitializerImpl implements AppStartupInitializer {

    private final ApplicationConfigData applicationConfigData;

    @Override
    public void init() {
        log.info("application startup at Product mode");
        if (!applicationConfigData.application_service_mail.equals(applicationConfigData.application_smtp_mail)){
            log.warn("smtp mail {} and service mail {} not the same.",applicationConfigData.application_smtp_mail,applicationConfigData.application_service_mail);
        }
    }
}
