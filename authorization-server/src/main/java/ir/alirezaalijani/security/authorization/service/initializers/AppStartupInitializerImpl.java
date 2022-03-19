package ir.alirezaalijani.security.authorization.service.initializers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AppStartupInitializerImpl implements AppStartupInitializer {

    @Override
    public void init() {
        log.info("application startup at Product mode");
    }
}
