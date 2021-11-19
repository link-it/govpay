package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class NuovoDocumento extends TipoRiferimentoDocumento  {
  
  @Schema(example = "abcdef12345_1", required = true, description = "Identificativo del documento")
 /**
   * Identificativo del documento  
  **/
  private String identificativo = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "descrizione del documento")
 /**
   * descrizione del documento  
  **/
  private String descrizione = null;
 /**
   * Identificativo del documento
   * @return identificativo
  **/
  @JsonProperty("identificativo")
  public String getIdentificativo() {
    return identificativo;
  }

  public void setIdentificativo(String identificativo) {
    this.identificativo = identificativo;
  }

  public NuovoDocumento identificativo(String identificativo) {
    this.identificativo = identificativo;
    return this;
  }

 /**
   * descrizione del documento
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public NuovoDocumento descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovoDocumento {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
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