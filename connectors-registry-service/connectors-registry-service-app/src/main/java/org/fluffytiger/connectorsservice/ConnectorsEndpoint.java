package org.fluffytiger.connectorsservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connectors")
public class ConnectorsEndpoint {
    private final ConnectorsService service;

    public ConnectorsEndpoint(ConnectorsService service) {
        this.service = service;
    }

    @GetMapping
    public ConnectorsList list() {
        return new ConnectorsList(service.getConnectors().getBody());
    }
}
