package org.fluffytiger.restservice.jsonschema;

import java.beans.ConstructorProperties;

public class Field {
    private final String type;
    private final boolean optional;
    private final String field;

    @ConstructorProperties({"type", "optional", "field"})
    public Field(String type, boolean optional, String field) {
        this.type = type;
        this.optional = optional;
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getField() {
        return field;
    }
}
