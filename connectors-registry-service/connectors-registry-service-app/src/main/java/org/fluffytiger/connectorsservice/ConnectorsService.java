package org.fluffytiger.connectorsservice;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Service
public class ConnectorsService {
    private final RestTemplate restTemplate;
    private final URI serviceAddress;

    public ConnectorsService(RestTemplateBuilder builder, PropertiesBindings props)
        throws MalformedURLException, URISyntaxException {

        this.restTemplate = builder
            .errorHandler(new DefaultResponseErrorHandler() {
                @Override
                protected void handleError(ClientHttpResponse response, HttpStatus code) { }
            })
            .build();

        this.serviceAddress = new URL("http", props.getHost(), props.getPort(), "/connectors").toURI();
    }

    public ResponseEntity<String> createConnector(String config) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(config, headers);
        return this.restTemplate.postForEntity(serviceAddress, request, String.class);
    }

    public ResponseEntity<List<String>> getConnectors() {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(serviceAddress, HttpMethod.GET, entity, typeRef);
    }
}
