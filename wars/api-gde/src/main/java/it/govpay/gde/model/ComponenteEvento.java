package it.govpay.gde.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Modulo interno che ha emesso l'evento
 */
public enum ComponenteEvento {
  
  API_BACKOFFICE("API_BACKOFFICE"),
  
  API_ENTE("API_ENTE"),
  
  API_PAGOPA("API_PAGOPA"),
  
  API_PAGAMENTO("API_PAGAMENTO"),
  
  API_PENDENZE("API_PENDENZE"),
  
  API_RAGIONERIA("API_RAGIONERIA"),
  
  API_BACKEND_IO("API_BACKEND_IO"),
  
  API_SECIM("API_SECIM"),
  
  API_MYPIVOT("API_MYPIVOT"),
  
  API_MAGGIOLI_JPPA("API_MAGGIOLI_JPPA"),
  
  API_GOVPAY("API_GOVPAY"),
  
  API_HYPERSIC_APK("API_HYPERSIC_APK"),
  
  API_USER("API_USER"),
  
  GOVPAY("GOVPAY");

  private String value;

  ComponenteEvento(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ComponenteEvento fromValue(String value) {
    for (ComponenteEvento b : ComponenteEvento.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

