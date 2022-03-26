package ir.alirezaalijani.security.gatewayserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationConfigsData {
    private Set<String> hosts;
    private Set<String> resourceServers;

    public Set<String> getHosts() {
        return Collections.unmodifiableSet(hosts);
    }

    public void setHosts(Set<String> hosts) {
        this.hosts = hosts;
    }

    public Set<String> getResourceServers() {
        return Collections.unmodifiableSet(resourceServers);
    }

    public void setResourceServers(Set<String> resourceServers) {
        this.resourceServers = resourceServers;
    }
}
