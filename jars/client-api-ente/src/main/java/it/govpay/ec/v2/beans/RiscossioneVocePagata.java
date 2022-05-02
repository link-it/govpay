package it.govpay.ec.v2.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class RiscossioneVocePagata   {
  
  @Schema(example = "01234567890", required = true, description = "Identificativo Ente creditore")
 /**
   * Identificativo Ente Creditore  
  **/
  private String idDominio = null;
  
  @Schema(example = "RF23567483937849450550875", required = true, description = "Identificativo univoco di versamento")
 /**
   * Identificativo univoco di versamento  
  **/
  private String iuv = null;
  
  @Schema(example = "1234acdc", required = true, description = "Identificativo univoco di riscossione.")
 /**
   * Identificativo univoco di riscossione.  
  **/
  private String iur = null;
  
  @Schema(example = "1", required = true, description = "indice posizionale della voce pendenza riscossa")
 /**
   * indice posizionale della voce pendenza riscossa  
  **/
  private BigDecimal indice = null;
  
  @Schema(required = true, description = "")
  private TipoRiscossione tipo = null;
  
  @Schema(example = "10.01", required = true, description = "Importo riscosso.")
 /**
   * Importo riscosso.  
  **/
  private BigDecimal importo = null;
  
  @Schema(required = true, description = "Data di esecuzione della riscossione")
 /**
   * Data di esecuzione della riscossione  
  **/
  private Date data = null;
  
  @Schema(description = "")
  private Allegato allegato = null;
 /**
   * Identificativo Ente Creditore
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public RiscossioneVocePagata idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Identificativo univoco di versamento
   * @return iuv
  **/
  @JsonProperty("iuv")
  @NotNull
  public String getIuv() {
    return iuv;
  }

  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  public RiscossioneVocePagata iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

 /**
   * Identificativo univoco di riscossione.
   * @return iur
  **/
  @JsonProperty("iur")
  @NotNull
  public String getIur() {
    return iur;
  }

  public void setIur(String iur) {
    this.iur = iur;
  }

  public RiscossioneVocePagata iur(String iur) {
    this.iur = iur;
    return this;
  }

 /**
   * indice posizionale della voce pendenza riscossa
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

  public RiscossioneVocePagata indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

 /**
   * Get tipo
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  public TipoRiscossione getTipo() {
    return tipo;
  }

  public void setTipo(TipoRiscossione tipo) {
    this.tipo = tipo;
  }

  public RiscossioneVocePagata tipo(TipoRiscossione tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * Importo riscosso.
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public RiscossioneVocePagata importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Data di esecuzione della riscossione
   * @return data
  **/
  @JsonProperty("data")
  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public RiscossioneVocePagata data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Get allegato
   * @return allegato
  **/
  @JsonProperty("allegato")
  public Allegato getAllegato() {
    return allegato;
  }

  public void setAllegato(Allegato allegato) {
    this.allegato = allegato;
  }

  public RiscossioneVocePagata allegato(Allegato allegato) {
    this.allegato = allegato;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiscossioneVocePagata {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
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
