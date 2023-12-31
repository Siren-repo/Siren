package com.devlop.siren.domain.item.utils;

import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Convert
@Component
public class AllergyConverter implements AttributeConverter<EnumSet<AllergyType>, String> {

  private static final String DELIMITER = ",";

  @Override
  public String convertToDatabaseColumn(EnumSet<AllergyType> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "";
    }
    List<String> allergies = convertEnumToString(attribute);
    return String.join(DELIMITER, allergies);
  }

  @Override
  public EnumSet<AllergyType> convertToEntityAttribute(String dbData) {
    if (Strings.isEmpty(dbData)) {
      return EnumSet.noneOf(AllergyType.class);
    }
    EnumSet<AllergyType> allergies = EnumSet.noneOf(AllergyType.class);
    Arrays.stream(dbData.split(","))
        .map(name -> convertStringToEnum(name.trim()))
        .forEach(e -> allergies.add(e));
    return allergies;
  }

  private AllergyType convertStringToEnum(String allergyName) {
    return Arrays.stream(AllergyType.values())
        .filter(o -> o.getAllergyName().equals(allergyName))
        .findAny()
        .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.INVALID_ALLERGY_TYPE));
  }

  private List<String> convertEnumToString(EnumSet<AllergyType> attribute) {
    return attribute.stream()
        .map(allergy -> allergy.getAllergyName())
        .collect(Collectors.toUnmodifiableList());
  }
}
