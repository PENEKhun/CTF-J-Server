package com.penekhun.ctfjserver.Config;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class SecurityRoleConverter implements AttributeConverter<SecurityRole, String> {

    @Override
    public String convertToDatabaseColumn(SecurityRole attribute) {
        return attribute.toString();
    }

    @Override
    public SecurityRole convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(SecurityRole.class).stream()
                .filter(e->e.toString().equals(dbData))
                .findAny()
                .orElseThrow(()-> new NoSuchElementException());
    }
}

