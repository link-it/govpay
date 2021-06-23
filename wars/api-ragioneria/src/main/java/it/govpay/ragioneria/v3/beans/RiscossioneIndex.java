package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"iuv",
"iur",
"indice",
"tipo",
"importo",
"data",
"vocePendenza",
})
public class RiscossioneIndex extends JSONSerializable {
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("iur")
  private String iur = null;
  
  @JsonProperty("indice")
  private BigDecimal indice = null;
  
  @JsonProperty("tipo")
  private TipoRiscossione tipo = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("vocePendenza")
  private VocePendenza vocePendenza = null;
  
  /**
   * Identificativo univoco di versamento
   **/
  public RiscossioneIndex iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Identificativo univoco di riscossione.
   **/
  public RiscossioneIndex iur(String iur) {
    this.iur = iur;
    return this;
  }

  @JsonProperty("iur")
  public String getIur() {
    return iur;
  }
  public void setIur(String iur) {
    this.iur = iur;
  }

  /**
   * indice posizionale della voce pendenza riscossa
   **/
  public RiscossioneIndex indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   **/
  public RiscossioneIndex tipo(TipoRiscossione tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoRiscossione getTipo() {
    return tipo;
  }
  public void setTipo(TipoRiscossione tipo) {
    this.tipo = tipo;
  }

  /**
   * Importo riscosso. 
   **/
  public RiscossioneIndex importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Data di esecuzione della riscossione
   **/
  public RiscossioneIndex data(Date data) {
    this.data = data;
    return this;
  }

  @JsonProperty("data")
  public Date getData() {
    return data;
  }
  public void setData(Date data) {
    this.data = data;
  }

  /**
   **/
  public RiscossioneIndex vocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
    return this;
  }

  @JsonProperty("vocePendenza")
  public VocePendenza getVocePendenza() {
    return vocePendenza;
  }
  public void setVocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiscossioneIndex riscossioneIndex = (RiscossioneIndex) o;
    return Objects.equals(iuv, riscossioneIndex.iuv) &&
        Objects.equals(iur, riscossioneIndex.iur) &&
        Objects.equals(indice, riscossioneIndex.indice) &&
        Objects.equals(tipo, riscossioneIndex.tipo) &&
        Objects.equals(importo, riscossioneIndex.importo) &&
        Objects.equals(data, riscossioneIndex.data) &&
        Objects.equals(vocePendenza, riscossioneIndex.vocePendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iuv, iur, indice, tipo, importo, data, vocePendenza);
  }

  public static RiscossioneIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, RiscossioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "riscossioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiscossioneIndex {\n");
    
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    vocePendenza: ").append(toIndentedString(vocePendenza)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}



