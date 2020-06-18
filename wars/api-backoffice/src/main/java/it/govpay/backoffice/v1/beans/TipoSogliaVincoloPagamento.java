package it.govpay.backoffice.v1.beans;


/**
 * Indica se il pagamento deve avvenire entro o oltre la soglia di giorni indicata
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Indica se il pagamento deve avvenire entro o oltre la soglia di giorni indicata
 */
public enum TipoSogliaVincoloPagamento {
  
  
  
  
  ENTRO("ENTRO"),
  
  
  OLTRE("OLTRE");
  
  
  

  private String value;

  TipoSogliaVincoloPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoSogliaVincoloPagamento fromValue(String text) {
    for (TipoSogliaVincoloPagamento b : TipoSogliaVincoloPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



