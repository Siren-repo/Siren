package com.devlop.siren.unit.domain.user.util;

import com.devlop.siren.unit.domain.user.domain.AllergyType;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AllergyConverter implements AttributeConverter<EnumSet<AllergyType>, String> {

    @Override
    public String convertToDatabaseColumn(EnumSet<AllergyType> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        List<String> allergy = attribute.stream()
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableList());
        return String.join(",", allergy);
    }

    @Override
    public EnumSet<AllergyType> convertToEntityAttribute(String dbData) {
        EnumSet<AllergyType> attribute = EnumSet.noneOf(AllergyType.class);

        if(dbData == null || dbData.isBlank())
            return attribute;

        Arrays.stream(dbData.split(","))
            .map(String::trim)
            .map(String::toUpperCase)
            .forEach(name -> attribute.add(AllergyType.valueOf(name)));

        return attribute;
    }
}
