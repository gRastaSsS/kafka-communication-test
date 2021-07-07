package org.fluffytiger.restservice.jsonschema;

import java.beans.ConstructorProperties;
import java.util.List;

public class Schema {
    private final String type;
    private final List<Field> fields;

    @ConstructorProperties({"type", "fields"})
    public Schema(String type, List<Field> fields) {
        this.type = type;
        this.fields = fields;
    }

    public String getType() {
        return type;
    }

    public List<Field> getFields() {
        return fields;
    }
}
