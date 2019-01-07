package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.TipoContabilita;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoEntrata  {
  
  @Schema(example = "IMU", required = true, description = "")
  private String idEntrata = null;
  
  @Schema(required = true, description = "")
  private TipoContabilita tipoContabilita = null;
  
  @Schema(example = "3321", required = true, description = "Codifica del capitolo di bilancio")
 /**
   * Codifica del capitolo di bilancio  
  **/
  private String codiceContabilita = null;
  
  @Schema(description = "Indicazione l'entrata e' abilitata")
 /**
   * Indicazione l'entrata e' abilitata  
  **/
  private Boolean abilitato = true;
 /**
   * Get idEntrata
   * @return idEntrata
  **/
  @JsonProperty("idEntrata")
  @NotNull
  public String getIdEntrata() {
    return idEntrata;
  }

  public void setIdEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
  }

  public TipoEntrata idEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
    return this;
  }

 /**
   * Get tipoContabilita
   * @return tipoContabilita
  **/
  @JsonProperty("tipoContabilita")
  @NotNull
  public TipoContabilita getTipoContabilita() {
    return tipoContabilita;
  }

  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  public TipoEntrata tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

 /**
   * Codifica del capitolo di bilancio
   * @return codiceContabilita
  **/
  @JsonProperty("codiceContabilita")
  @NotNull
  public String getCodiceContabilita() {
    return codiceContabilita;
  }

  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  public TipoEntrata codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }

 /**
   * Indicazione l&#x27;entrata e&#x27; abilitata
   * @return abilitato
  **/
  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return abilitato;
  }

  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  public TipoEntrata abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoEntrata {\n");
    
    sb.append("    idEntrata: ").append(toIndentedString(idEntrata)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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
