package org.fluffytiger.transformers.multiplication;

import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;

public class MultiplicationTransformValue<R extends ConnectRecord<R>> extends MultiplicationTransform<R> {
    @Override
    protected Schema operatingSchema(R record) {
        return record.valueSchema();
    }

    @Override
    protected Object operatingValue(R record) {
        return record.value();
    }

    @Override
    protected R newRecord(R record, Object updatedValue) {
        return record.newRecord(
            record.topic(), record.kafkaPartition(),
            record.keySchema(), record.key(),
            record.valueSchema(), updatedValue,
            record.timestamp()
        );
    }
}
