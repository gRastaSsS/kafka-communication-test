package org.fluffytiger.restservice.wages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.fluffytiger.restservice.jsonschema.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Service
public class WageEventProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ConfigProperties props;
    private Schema schema;

    public WageEventProducerService(KafkaTemplate<String, Object> kafkaTemplate, ConfigProperties props) {
        this.kafkaTemplate = kafkaTemplate;
        this.props = props;
    }

    @PostConstruct
    public void initSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream s = new ClassPathResource("wage-request-schema.json").getInputStream();
        this.schema = mapper.readValue(s, Schema.class);
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic(props.getTopic(), props.getPartitions(), props.getReplication().shortValue());
    }

    public void sendWageRequest(Wage wage) {
        String id = UUID.randomUUID().toString();

        Map<String, Object> message = Map.of(
            "schema", schema,
            "payload", Map.of(
                "name", wage.getName(),
                "surname", wage.getSurname(),
                "wage", wage.getWage(),
                "eventTime", wage.getEventTime().toString(),
                "id", id
            )
        );

        this.kafkaTemplate.send(props.getTopic(), id, message);
    }
}
