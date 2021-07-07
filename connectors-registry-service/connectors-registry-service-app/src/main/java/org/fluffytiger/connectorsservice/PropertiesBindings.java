package org.fluffytiger.connectorsservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "connect-service")
public class PropertiesBindings {
    private String host;

    private Integer port;

    private Map<String, String> retry;

    private Map<String, Map<String, String>> init;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String, String> getRetry() {
        return retry;
    }

    public void setRetry(Map<String, String> retry) {
        this.retry = retry;
    }

    public Map<String, Map<String, String>> getInit() {
        return init;
    }

    public void setInit(Map<String, Map<String, String>> init) {
        this.init = init;
    }
}
