package ir.alirezaalijani.security.gatewayserver.config;

import ir.alirezaalijani.security.gatewayserver.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.Set;

@Configuration
public class GatewayRouteConfig {

    private Set<String> hosts;
    private Set<String> resourceServers;

    private final RateLimiter<?> rateLimiter;
    private final KeyResolver keyResolver;

    public GatewayRouteConfig(RateLimiter<?> rateLimiter,
                              KeyResolver keyResolver,
                              ApplicationConfigsData applicationConfigsData) {
        this.rateLimiter = rateLimiter;
        this.keyResolver = keyResolver;
        this.hosts = applicationConfigsData.getHosts();
        this.resourceServers = applicationConfigsData.getResourceServers();
        resourceServers.forEach(System.out::println);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder locator) {
        return locator.routes()
                // only registered resource server allowed
                .route("oauth-route-jwks", predicate -> predicate
                        .path("/oauth2/jwks")
                        .filters(filter -> filter.setPath("/oauth2/jwks")
                                .filter((exchange, chain) -> {
                                    if (!serverExistFilter(exchange))
                                        return Mono.error(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,"You dont have access!"));
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("lb://auth-server"))

                .route("oauth2-route", predicate -> predicate
                        .path("/oauth2/**")
                        .and().asyncPredicate(this::hostExistPredicate)
                        .filters(filter -> filter.rewritePath("/oauth2(?<segment>.*)", "/oauth2${segment}")
                                .requestRateLimiter(config -> config.setRateLimiter(this.rateLimiter)
                                        .setKeyResolver(this.keyResolver)
                                        .setStatusCode(HttpStatus.TOO_MANY_REQUESTS)
                                        .setRouteId("oauth2-route")
                                )
                        )
                        .uri("lb://auth-server"))

                .route("auth-service", predicate -> predicate.path("/**")
                        .and().asyncPredicate(this::hostExistPredicate)
                        .filters(filter -> filter.requestRateLimiter(config -> config.setRateLimiter(this.rateLimiter)
                                        .setKeyResolver(this.keyResolver)
                                        .setStatusCode(HttpStatus.TOO_MANY_REQUESTS)
                                        .setRouteId("auth-service")
                                )
                        )
                        .uri("lb://auth-server"))

                .build();
    }

    private Mono<Boolean> hostExistPredicate(ServerWebExchange exchange) {
        return Mono.create(monoSink -> {
            monoSink.success(hosts.contains(HttpUtils.getHost(exchange)));
        });
    }

    private boolean serverExistFilter(ServerWebExchange exchange) {
        return resourceServers.contains(HttpUtils.getRemoteIp(exchange));
    }
}
