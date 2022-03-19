package ir.alirezaalijani.security.authorization.service.initializers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationStartupInitializerDev implements AppStartupInitializer{

    @Override
    public void init() {
        log.info("application startup at Dev mode");
    }
}
