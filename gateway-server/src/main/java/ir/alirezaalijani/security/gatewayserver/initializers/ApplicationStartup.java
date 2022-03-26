package ir.alirezaalijani.security.gatewayserver.initializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    public ApplicationStartup() {

    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    }
}
