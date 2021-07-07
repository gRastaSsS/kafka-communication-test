package org.fluffytiger.transformers.multiplication;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.transforms.util.Requirements;
import org.apache.kafka.connect.transforms.util.SimpleConfig;

import java.util.HashMap;
import java.util.Map;

public abstract class MultiplicationTransform<R extends ConnectRecord<R>> implements Transformation<R> {
    public static final String OVERVIEW_DOC = "Multiply specified double field in a record";

    private static final String PURPOSE = "multiplying double value in record";

    private static final ConfigDef CONFIG_DEF = new ConfigDef()
        .define(Config.FIELD_NAME, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH,
            "Field name")
        .define(Config.MULTIPLIER, ConfigDef.Type.DOUBLE, ConfigDef.Importance.HIGH,
            "Multiplier value");

    private String fieldName = null;
    private double multiplier = 0.0;

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void configure(Map<String, ?> props) {
        SimpleConfig config = new SimpleConfig(CONFIG_DEF, props);
        this.fieldName = config.getString(Config.FIELD_NAME);
        this.multiplier = config.getDouble(Config.MULTIPLIER);
    }

    @Override
    public R apply(R record) {
        if (operatingSchema(record) == null) {
            return applySchemaless(record);
        } else {
            return applyWithSchema(record);
        }
    }

    private R applySchemaless(R record) {
        Map<String, Object> value = Requirements.requireMap(operatingValue(record), PURPOSE);

        Map<String, Object> updatedValue = new HashMap<>(value);

        updatedValue.put(fieldName, ((Double) value.get(fieldName)) * multiplier);

        return newRecord(record, updatedValue);
    }

    private R applyWithSchema(R record) {
        Struct value = Requirements.requireStruct(operatingValue(record), PURPOSE);

        Struct updatedValue = new Struct(value.schema());

        for (Field field : value.schema().fields()) {
            String name = field.name();

            if (this.fieldName.equals(name)) {
                updatedValue.put(name, ((Double) value.get(field)) * multiplier);
            } else {
                updatedValue.put(name, value.get(field));
            }
        }

        return newRecord(record, updatedValue);
    }

    @Override
    public void close() { }

    protected abstract Schema operatingSchema(R record);

    protected abstract Object operatingValue(R record);

    protected abstract R newRecord(R record, Object updatedValue);
}
