package it.govpay.gde.entity.converter;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.gde.entity.DatiPagoPAEntity;

@Converter(autoApply = true)
public class DatiPagoPAConverter implements AttributeConverter<DatiPagoPAEntity, String> {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(DatiPagoPAEntity meta) {
    try {
      return objectMapper.writeValueAsString(meta);
    } catch (JsonProcessingException ex) {
      return null;
      // or throw an error
    }
  }

  @Override
  public DatiPagoPAEntity convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, DatiPagoPAEntity.class);
    } catch (IOException ex) {
      // logger.error("Unexpected IOEx decoding json from database: " + dbData);
      return null;
    }
  }

}