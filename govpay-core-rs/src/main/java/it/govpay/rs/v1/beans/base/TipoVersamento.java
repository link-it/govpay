package it.govpay.rs.v1.beans.base;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Tipologia di versamento:  * PO: Pagamento da PSP  * BP: Bollettino postale  * BBT: Bonifico bancario  * CP: Carta di pagamento  * AD: Addebito diretto  * OBEP: Pagamento Mybank  * OTH: Altro tipo di pagamento 
 **/


/**
 * Tipologia di versamento:  * PO: Pagamento da PSP  * BP: Bollettino postale  * BBT: Bonifico bancario  * CP: Carta di pagamento  * AD: Addebito diretto  * OBEP: Pagamento Mybank  * OTH: Altro tipo di pagamento 
 */
public enum TipoVersamento {
  
  
  
  
  PO("PO"),
  
  
  BP("BP"),
  
  
  BBT("BBT"),
  
  
  CP("CP"),
  
  
  AD("AD"),
  
  
  OBEP("OBEP"),
  
  
  OTH("OTH");
  
  
  

  private String value;

  TipoVersamento(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static TipoVersamento fromValue(String text) {
    for (TipoVersamento b : TipoVersamento.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



