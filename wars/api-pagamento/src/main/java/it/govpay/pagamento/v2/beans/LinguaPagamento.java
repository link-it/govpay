package it.govpay.pagamento.v2.beans;


/**
 * Indica la lingua da utilizzare durante il pagamento sul Checkout
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Indica la lingua da utilizzare durante il pagamento sul Checkout
 */
public enum LinguaPagamento {
  
  
  
  
  IT("it"),
  
  
  EN("en"),
  
  
  FR("fr"),
  
  
  SL("sl"),
  
  
  DE("de");
  
  
  

  private String value;

  LinguaPagamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static LinguaPagamento fromValue(String text) {
    for (LinguaPagamento b : LinguaPagamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



