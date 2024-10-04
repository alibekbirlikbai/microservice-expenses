package com.example.transaction.model.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LongToStringConverter implements AttributeConverter<Long, String> {

    @Override
    public String convertToDatabaseColumn(Long attribute) {
      return String.format("%010d", attribute);
    }

    @Override
    public Long convertToEntityAttribute(String dbData) {
        return Long.parseLong(dbData);
    }
}
