package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Il campo 'pendenza' e' obbligatorio quando il valore del campo 'stato' e' uguale a 'NON_ESEGUITA'. Valorizzare il campo 'descrizioneStato' con una descrizione estesa dello stato della pendenza quando il valore del campo 'stato' e' diverso da 'NON_ESEGUITA'.
 **/
@Schema(description="Il campo 'pendenza' e' obbligatorio quando il valore del campo 'stato' e' uguale a 'NON_ESEGUITA'. Valorizzare il campo 'descrizioneStato' con una descrizione estesa dello stato della pendenza quando il valore del campo 'stato' e' diverso da 'NON_ESEGUITA'.")
public class PendenzaVerificata   {
  
  @Schema(required = true, description = "")
  private StatoPendenzaVerificata stato = null;
  
  @Schema(description = "Descrizione in dettaglio dello stato della pendenza.")
 /**
   * Descrizione in dettaglio dello stato della pendenza.  
  **/
  private String descrizioneStato = null;
  
  @Schema(description = "")
  private NuovaPendenza pendenza = null;
 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  public StatoPendenzaVerificata getStato() {
    return stato;
  }

  public void setStato(StatoPendenzaVerificata stato) {
    this.stato = stato;
  }

  public PendenzaVerificata stato(StatoPendenzaVerificata stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Descrizione in dettaglio dello stato della pendenza.
   * @return descrizioneStato
  **/
  @JsonProperty("descrizioneStato")
  public String getDescrizioneStato() {
    return descrizioneStato;
  }

  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public PendenzaVerificata descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }

 /**
   * Get pendenza
   * @return pendenza
  **/
  @JsonProperty("pendenza")
  public NuovaPendenza getPendenza() {
    return pendenza;
  }

  public void setPendenza(NuovaPendenza pendenza) {
    this.pendenza = pendenza;
  }

  public PendenzaVerificata pendenza(NuovaPendenza pendenza) {
    this.pendenza = pendenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaVerificata {\n");
    
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
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
