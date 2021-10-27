package it.govpay.ragioneria.v3.beans;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Segnalazione   {
  
  @Schema(description = "")
  private Date data = null;
  
  @Schema(example = "0013", required = true, description = "")
  private String codice = null;
  
  @Schema(example = "Pagamento rendicontato senza RPT", required = true, description = "")
  private String descrizione = null;
  
  @Schema(example = "Acquisita rendicontazione senza RPT nel flusso ABC-1234-DEFG", description = "")
  private String dettaglio = null;
 /**
   * Get data
   * @return data
  **/
  @JsonProperty("data")
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public Segnalazione data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Get codice
   * @return codice
  **/
  @JsonProperty("codice")
  @NotNull
  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public Segnalazione codice(String codice) {
    this.codice = codice;
    return this;
  }

 /**
   * Get descrizione
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Segnalazione descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * Get dettaglio
   * @return dettaglio
  **/
  @JsonProperty("dettaglio")
  public String getDettaglio() {
    return dettaglio;
  }

  public void setDettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
  }

  public Segnalazione dettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Segnalazione {\n");
    
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    dettaglio: ").append(toIndentedString(dettaglio)).append("\n");
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
