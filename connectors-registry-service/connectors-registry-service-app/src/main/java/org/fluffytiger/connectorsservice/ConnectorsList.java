package org.fluffytiger.connectorsservice;

import java.util.Arrays;
import java.util.List;

public class ConnectorsList {
    private final List<String> connectors;

    public ConnectorsList(List<String> connectors) {
        this.connectors = connectors;
    }

    public ConnectorsList(String[] connectors) {
        this.connectors = Arrays.asList(connectors);
    }

    public List<String> getConnectors() {
        return connectors;
    }
}
