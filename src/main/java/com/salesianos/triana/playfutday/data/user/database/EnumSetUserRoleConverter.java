package com.salesianos.triana.playfutday.data.user.database;

import com.salesianos.triana.playfutday.data.user.model.UserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

@Convert
public class EnumSetUserRoleConverter implements AttributeConverter<EnumSet<UserRole>, String> {
    private final String SEPARADOR = ", ";

    @Override
    public String convertToDatabaseColumn(EnumSet<UserRole> attribute) {

        if (!attribute.isEmpty()) {
            return attribute.stream()
                    .map(UserRole::name)
                    .collect(Collectors.joining(SEPARADOR));
        }
        return null;
    }

    @Override
    public EnumSet<UserRole> convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            if (!dbData.isBlank()) { // isBlank Java 11
                return Arrays.stream(dbData.split(SEPARADOR))
                        .map(elem -> UserRole.valueOf(elem))
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(UserRole.class)));
            }
        }
        return EnumSet.noneOf(UserRole.class);
    }
}
