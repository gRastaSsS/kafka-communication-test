package org.fluffytiger.transformers.multiplication;

import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;

public class MultiplicationTransformKey<R extends ConnectRecord<R>> extends MultiplicationTransform<R> {
    @Override
    protected Schema operatingSchema(R record) {
        return record.keySchema();
    }

    @Override
    protected Object operatingValue(R record) {
        return record.key();
    }

    @Override
    protected R newRecord(R record, Object updatedValue) {
        return record.newRecord(
            record.topic(), record.kafkaPartition(),
            record.keySchema(), updatedValue,
            record.valueSchema(), record.value(),
            record.timestamp()
        );
    }
}
