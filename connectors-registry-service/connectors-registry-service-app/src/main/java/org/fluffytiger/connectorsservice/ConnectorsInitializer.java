package org.fluffytiger.connectorsservice;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConnectorsInitializer implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectorsInitializer.class);

    private final PropertiesBindings propertiesBindings;
    private final ConnectorsService service;

    public ConnectorsInitializer(PropertiesBindings propertiesBindings, ConnectorsService service) {
        this.propertiesBindings = propertiesBindings;
        this.service = service;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int maxAttempts = Integer.parseInt(propertiesBindings.getRetry().get("max-attempts"));

        RetryTemplate template = RetryTemplate.builder()
            .maxAttempts(maxAttempts)
            .fixedBackoff(2000)
            .retryOn(RestClientException.class)
            .build();

        template.execute(ctx -> {
            try {
                HttpStatus status = service.getConnectors().getStatusCode();
                if (!status.is2xxSuccessful())
                    throw new ResourceAccessException(status.getReasonPhrase());

            } catch (RestClientException e) {
                int attemptsLeft = maxAttempts - ctx.getRetryCount() - 1;
                LOG.warn("Cannot access kafka connect service! Reason: {}. Attempts left: {}",
                    e.getMessage(), attemptsLeft);
                throw e;
            }

            LOG.info("Connection established!");
            return true;
        });

        Map<String, String> configs = getConnectorConfigs(propertiesBindings.getInit());
        for (Map.Entry<String, String> connector : configs.entrySet()) {
            HttpStatus status = service.createConnector(connector.getValue()).getStatusCode();
            if (status.is2xxSuccessful()) {
                LOG.info("Successfully registered {} connector!", connector.getKey());
            } else {
                LOG.error("Connector {} was not registered! Status code: {}", connector.getKey(), status);
            }
        }
    }

    private Map<String, String> getConnectorConfigs(Map<String, Map<String, String>> props) throws IOException {
        Map<String, String> configs = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> connector : props.entrySet()) {
            InputStream s = new ClassPathResource(String.format("connectors/%s.json", connector.getKey())).getInputStream();
            String config = StreamUtils.copyToString(s, Charset.defaultCharset());
            StringSubstitutor sub = new StringSubstitutor(connector.getValue());
            configs.put(connector.getKey(), sub.replace(config));
        }

        return configs;
    }
}
