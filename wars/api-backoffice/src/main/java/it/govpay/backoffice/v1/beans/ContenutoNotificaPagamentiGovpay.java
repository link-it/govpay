package it.govpay.backoffice.v1.beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Gets or Sets contenutoNotificaPagamentiGovpay
 */
public enum ContenutoNotificaPagamentiGovpay {
  
  
  
  
  SINTESI_PAGAMENTI("SINTESI_PAGAMENTI"),
  
  
  SINTESI_FLUSSI_RENDICONTAZIONE("SINTESI_FLUSSI_RENDICONTAZIONE"),
  
  
  RPP("RPP"),
  
  
  FLUSSI_RENDICONTAZIONE("FLUSSI_RENDICONTAZIONE");
  
  
  

  private String value;

  ContenutoNotificaPagamentiGovpay(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ContenutoNotificaPagamentiGovpay fromValue(String text) {
    for (ContenutoNotificaPagamentiGovpay b : ContenutoNotificaPagamentiGovpay.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



