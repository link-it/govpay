package it.govpay.pagamento.v2.beans;


/**
 * modalita&#x27; di autenticazione del soggetto versante
 **/
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * modalita' di autenticazione del soggetto versante
 */
public enum TipoAutenticazioneSoggetto {
  
  
  
  
  CNS("CNS"),
  
  
  USR("USR"),
  
  
  OTH("OTH"),
  
  
  N_A("N/A");
  
  
  

  private String value;

  TipoAutenticazioneSoggetto(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TipoAutenticazioneSoggetto fromValue(String text) {
    for (TipoAutenticazioneSoggetto b : TipoAutenticazioneSoggetto.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



