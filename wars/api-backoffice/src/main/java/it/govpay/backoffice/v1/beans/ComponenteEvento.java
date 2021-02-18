package it.govpay.backoffice.v1.beans;


/**
 * Modulo interno che ha emesso l&#x27;evento
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Modulo interno che ha emesso l'evento
 */
public enum ComponenteEvento {
  
  
  
  
  BACKOFFICE("API_BACKOFFICE"),
  
  
  ENTE("API_ENTE"),
  
  
  PAGOPA("API_PAGOPA"),
  
  
  PAGAMENTO("API_PAGAMENTO"),
  
  
  PENDENZE("API_PENDENZE"),
  
  
  RAGIONERIA("API_RAGIONERIA"),
  
  
  BACKEND_IO("API_BACKEND_IO"),
  
  
  SECIM("API_SECIM"),
  
  
  MYPIVOT("API_MYPIVOT");
  
  
  

  private String value;

  ComponenteEvento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ComponenteEvento fromValue(String text) {
    for (ComponenteEvento b : ComponenteEvento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



