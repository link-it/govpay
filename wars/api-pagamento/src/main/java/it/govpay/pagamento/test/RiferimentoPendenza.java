package it.govpay.pagamento.test;

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

public class RiferimentoPendenza  {
  
  @Schema(example = "A2A_12345", required = true, description = "Identificativo del gestionale responsabile della pendenza")
 /**
   * Identificativo del gestionale responsabile della pendenza  
  **/
  private String idA2A = null;
  
  @Schema(example = "abcdef12345", required = true, description = "Identificativo della pendenza nel gestionale responsabile")
 /**
   * Identificativo della pendenza nel gestionale responsabile  
  **/
  private String idPendenza = null;
 /**
   * Identificativo del gestionale responsabile della pendenza
   * @return idA2A
  **/
  @JsonProperty("idA2A")
  @NotNull
  public String getIdA2A() {
    return idA2A;
  }

  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  public RiferimentoPendenza idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

 /**
   * Identificativo della pendenza nel gestionale responsabile
   * @return idPendenza
  **/
  @JsonProperty("idPendenza")
  @NotNull
  public String getIdPendenza() {
    return idPendenza;
  }

  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  public RiferimentoPendenza idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoPendenza {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
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
