package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.NuovaVocePendenza;
import it.govpay.pagamento.v2.beans.StatoVocePendenza;
import java.math.BigDecimal;
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

public class VocePendenza extends NuovaVocePendenza {
  
  @Schema(example = "1", required = true, description = "indice di voce all'interno della pendenza")
 /**
   * indice di voce all'interno della pendenza  
  **/
  private BigDecimal indice = null;
  
  @Schema(required = true, description = "")
  private StatoVocePendenza stato = null;
 /**
   * indice di voce all&#x27;interno della pendenza
   * @return indice
  **/
  @JsonProperty("indice")
  @NotNull
  public BigDecimal getIndice() {
    return indice;
  }

  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  public VocePendenza indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoVocePendenza getStato() {
    return stato;
  }

  public void setStato(StatoVocePendenza stato) {
    this.stato = stato;
  }

  public VocePendenza stato(StatoVocePendenza stato) {
    this.stato = stato;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenza {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
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
