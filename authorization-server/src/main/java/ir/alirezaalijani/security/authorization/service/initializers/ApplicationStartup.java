package ir.alirezaalijani.security.authorization.service.initializers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final AppStartupInitializer initializer;
    private final AppStartupInitializer dataSourceInitializer;

    public ApplicationStartup(@Qualifier("applicationStartupInitializerDev") AppStartupInitializer initializer,
                              @Qualifier("dataSourceInitializer") AppStartupInitializer dataSourceInitializer) {
        this.initializer = initializer;
        this.dataSourceInitializer = dataSourceInitializer;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.initializer.init();
        this.dataSourceInitializer.init();
    }
}
