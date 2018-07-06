/**
 * 
 */
package it.govpay.core.rs.v1.beans.base;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 giu 2018 $
 * 
 */
/**
 * Servizio oggetto dell'autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pendenze e Pagamenti  * Pendenze e Pagamenti propri   * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
 */
public enum ServizioEnum {
  
  
      
          
  ANAGRAFICA_PAGOPA("Anagrafica PagoPA"),
  
          
  ANAGRAFICA_CREDITORE("Anagrafica Creditore"),
  
          
  ANAGRAFICA_APPLICAZIONI("Anagrafica Applicazioni"),
  
          
  ANAGRAFICA_RUOLI("Anagrafica Ruoli"),
  
          
  PAGAMENTI_E_PENDENZE("Pagamenti e Pendenze"),
  
          
  RENDICONTAZIONI_E_INCASSI("Rendicontazioni e Incassi"),
  
          
  GIORNALE_DEGLI_EVENTI("Giornale degli Eventi"),
  
          
  CONFIGURAZIONE_E_MANUTENZIONE("Configurazione e manutenzione");
          
      
  

  private String value;

  ServizioEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  public static ServizioEnum fromValue(String text) {
    for (ServizioEnum b : ServizioEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
