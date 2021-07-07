package org.fluffytiger.transformers;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.fluffytiger.transformers.multiplication.Config;
import org.fluffytiger.transformers.multiplication.MultiplicationTransform;
import org.fluffytiger.transformers.multiplication.MultiplicationTransformValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MultiplicationTransformTests {
    private MultiplicationTransform<SinkRecord> valueTransform = null;

    @BeforeEach
    public void setup() {
        valueTransform = new MultiplicationTransformValue<>();
    }

    @AfterEach
    public void tearDown() {
        valueTransform.close();
    }

    @Test
    public void testMultiplication() {
        valueTransform.configure(Map.of(
            Config.FIELD_NAME, "wage",
            Config.MULTIPLIER, 5
        ));

        Schema valueSchema = SchemaBuilder.struct().name("name").version(1).doc("doc")
            .field("name", Schema.STRING_SCHEMA)
            .field("surname", Schema.STRING_SCHEMA)
            .field("wage", Schema.FLOAT64_SCHEMA)
            .build();

        Struct value = new Struct(valueSchema)
            .put("name", "Alice")
            .put("surname", "Alice")
            .put("wage", 10.0);

        SinkRecord updatedRecord = valueTransform.apply(new SinkRecord(
            "topic", 1, null, null,
            valueSchema, value,
            1
        ));

        Assertions.assertEquals(50.0, ((Struct) updatedRecord.value()).getFloat64("wage"));
        Assertions.assertEquals("Alice", ((Struct) updatedRecord.value()).getString("name"));
    }
}
