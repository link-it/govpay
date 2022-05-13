package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Dominio   {
  
  @Schema(example = "1.23456789E+9", required = true, description = "Codice fiscale")
 /**
   * Codice fiscale  
  **/
  private String idDominio = null;
  
  @Schema(example = "Comune Dimostrativo", description = "Ragione sociale")
 /**
   * Ragione sociale  
  **/
  private String ragioneSociale = null;
 /**
   * Codice fiscale
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  @NotNull
 @Pattern(regexp="^([0-9]){11}$")  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public Dominio idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Ragione sociale
   * @return ragioneSociale
  **/
  @JsonProperty("ragioneSociale")
 @Size(min=1,max=70)  public String getRagioneSociale() {
    return ragioneSociale;
  }

  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  public Dominio ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dominio {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
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
