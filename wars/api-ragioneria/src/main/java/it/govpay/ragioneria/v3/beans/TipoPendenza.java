package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class TipoPendenza   {
  
  @Schema(example = "REFNIDO", required = true, description = "")
  private String idTipoPendenza = null;
  
  @Schema(example = "Refezione scolastica Asilo Nido", required = true, description = "")
  private String descrizione = null;
 /**
   * Get idTipoPendenza
   * @return idTipoPendenza
  **/
  @JsonProperty("idTipoPendenza")
  @NotNull
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }

  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  public TipoPendenza idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
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

  public TipoPendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenza {\n");
    
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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
