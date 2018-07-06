package it.govpay.core.rs.v1.beans.pagamenti;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Macro categoria della pendenza Agid
 */
public enum TassonomiaAvviso {
  
  
  
  
  CARTELLE_ESATTORIALI("Cartelle esattoriali"),
  
  
  DIRITTI_E_CONCESSIONI("Diritti e concessioni"),
  
  
  IMPOSTE_E_TASSE("Imposte e tasse"),
  
  
  IMU_TASI_E_ALTRE_TASSE_COMUNALI("IMU, TASI e altre tasse comunali"),
  
  
  INGRESSI_A_MOSTRE_E_MUSEI("Ingressi a mostre e musei"),
  
  
  MULTE_E_SANZIONI_AMMINISTRATIVE("Multe e sanzioni amministrative"),
  
  
  PREVIDENZA_E_INFORTUNI("Previdenza e infortuni"),
  
  
  SERVIZI_EROGATI_DAL_COMUNE("Servizi erogati dal comune"),
  
  
  SERVIZI_EROGATI_DA_ALTRI_ENTI("Servizi erogati da altri enti"),
  
  
  SERVIZI_SCOLASTICI("Servizi scolastici"),
  
  
  TASSA_AUTOMOBILISTICA("Tassa automobilistica"),
  
  
  TICKET_E_PRESTAZIONI_SANITARIE("Ticket e prestazioni sanitarie"),
  
  
  TRASPORTI_MOBILIT_E_PARCHEGGI("Trasporti, mobilità e parcheggi");
  
  
  

  private String value;

  TassonomiaAvviso(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TassonomiaAvviso fromValue(String text) {
    for (TassonomiaAvviso b : TassonomiaAvviso.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}



