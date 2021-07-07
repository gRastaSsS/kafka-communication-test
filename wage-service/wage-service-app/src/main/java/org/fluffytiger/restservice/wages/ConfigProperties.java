package org.fluffytiger.restservice.wages;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "wages")
public class ConfigProperties {
    private String topic;
    private Integer partitions;
    private Integer replication;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getPartitions() {
        return partitions;
    }

    public void setPartitions(Integer partitions) {
        this.partitions = partitions;
    }

    public Integer getReplication() {
        return replication;
    }

    public void setReplication(Integer replication) {
        this.replication = replication;
    }
}
