package it.govpay.backoffice.v1.beans;


/**
 * Modello di pagamento
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Modello di pagamento
 */
public enum ModelloPagamento {
  



  ENTE("Pagamento ad iniziativa Ente"),


  PSP("Pagamento ad iniziativa PSP"),
  
  
  MODELLO_UNICO("Modello unico");
  
  
  

  private String value;

  ModelloPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ModelloPagamento fromValue(String text) {
    for (ModelloPagamento b : ModelloPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



