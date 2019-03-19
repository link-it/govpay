package it.govpay.ec.v1.beans;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.v3.oas.annotations.media.Schema;

public class NotificaAnnullamento  {
  
  // @Schema(example = "ERR_001", description = "Codice Operazione")
 /**
   * Codice Operazione  
  **/
  private String codice = null;
  
  // @Schema(example = "Errore gestione nel PSP", description = "Descrizione estesa della motivazione che ha portato alla cancellazione del pagamento")
 /**
   * Descrizione estesa della motivazione che ha portato alla cancellazione del pagamento  
  **/
  private String motivazione = null;
 /**
   * Codice Operazione
   * @return codice
  **/
  @JsonProperty("codice")
  @Valid
  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public NotificaAnnullamento codice(String codice) {
    this.codice = codice;
    return this;
  }

 /**
   * Descrizione estesa della motivazione che ha portato alla cancellazione del pagamento
   * @return motivazione
  **/
  @JsonProperty("motivazione")
  @Valid
  public String getMotivazione() {
    return motivazione;
  }

  public void setMotivazione(String motivazione) {
    this.motivazione = motivazione;
  }

  public NotificaAnnullamento motivazione(String motivazione) {
    this.motivazione = motivazione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificaAnnullamento {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    motivazione: ").append(toIndentedString(motivazione)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
