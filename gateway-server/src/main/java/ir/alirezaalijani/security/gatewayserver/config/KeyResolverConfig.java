package ir.alirezaalijani.security.gatewayserver.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class KeyResolverConfig {

    @Bean("defaultKeyResolver")
    public KeyResolver defaultKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getURI().getHost());
    }

    @Primary
    @Bean("remoteAddressKeyResolver")
    public KeyResolver remoteAddressKeyResolver() {
        return exchange -> Mono.create(stringMonoSink -> {
            var remoteArr =exchange.getRequest().getRemoteAddress();
            if (remoteArr!=null){
                stringMonoSink.success(remoteArr.getAddress().getHostAddress());
            }else {
                stringMonoSink.success(exchange.getRequest().getURI().getHost());
            }
        });
    }

}
