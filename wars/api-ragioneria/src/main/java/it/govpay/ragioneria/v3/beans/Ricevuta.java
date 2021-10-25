package it.govpay.ragioneria.v3.beans;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Ricevuta   {
  
  @Schema(required = true, description = "")
  private Dominio dominio = null;
  
  @Schema(example = "RF23567483937849450550875", required = true, description = "Identificativo univoco di versamento")
 /**
   * Identificativo univoco di versamento  
  **/
  private String iuv = null;
  
  @Schema(example = "ab12345", required = true, description = "Corrisponde al `receiptId` oppure al `ccp` a seconda del modello di pagamento")
 /**
   * Corrisponde al `receiptId` oppure al `ccp` a seconda del modello di pagamento  
  **/
  private String idRicevuta = null;
  
  @Schema(description = "")
  private RicevutaAttestante attestante = null;
  
  @Schema(description = "")
  private Soggetto versante = null;
  
  @Schema(description = "")
  private Pendenza pendenza = null;
  
  @Schema(description = "")
  private List<Riscossione> riscossioni = null;
  
  @Schema(description = "Data di acquisizione della ricevuta")
 /**
   * Data di acquisizione della ricevuta  
  **/
  private Date data = null;
  
  @Schema(description = "Data di esecuzione della riscossione")
 /**
   * Data di esecuzione della riscossione  
  **/
  private Date dataPagamento = null;
  
  @Schema(description = "")
  private RicevutaRpt rpt = null;
  
  @Schema(description = "")
  private RicevutaRt rt = null;
 /**
   * Get dominio
   * @return dominio
  **/
  @JsonProperty("dominio")
  @NotNull
  public Dominio getDominio() {
    return dominio;
  }

  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public Ricevuta dominio(Dominio dominio) {
    this.dominio = dominio;
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

  public Ricevuta iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

 /**
   * Corrisponde al &#x60;receiptId&#x60; oppure al &#x60;ccp&#x60; a seconda del modello di pagamento
   * @return idRicevuta
  **/
  @JsonProperty("idRicevuta")
  @NotNull
  public String getIdRicevuta() {
    return idRicevuta;
  }

  public void setIdRicevuta(String idRicevuta) {
    this.idRicevuta = idRicevuta;
  }

  public Ricevuta idRicevuta(String idRicevuta) {
    this.idRicevuta = idRicevuta;
    return this;
  }

 /**
   * Get attestante
   * @return attestante
  **/
  @JsonProperty("attestante")
  public RicevutaAttestante getAttestante() {
    return attestante;
  }

  public void setAttestante(RicevutaAttestante attestante) {
    this.attestante = attestante;
  }

  public Ricevuta attestante(RicevutaAttestante attestante) {
    this.attestante = attestante;
    return this;
  }

 /**
   * Get versante
   * @return versante
  **/
  @JsonProperty("versante")
  public Soggetto getVersante() {
    return versante;
  }

  public void setVersante(Soggetto versante) {
    this.versante = versante;
  }

  public Ricevuta versante(Soggetto versante) {
    this.versante = versante;
    return this;
  }

 /**
   * Get pendenza
   * @return pendenza
  **/
  @JsonProperty("pendenza")
  public Pendenza getPendenza() {
    return pendenza;
  }

  public void setPendenza(Pendenza pendenza) {
    this.pendenza = pendenza;
  }

  public Ricevuta pendenza(Pendenza pendenza) {
    this.pendenza = pendenza;
    return this;
  }

 /**
   * Get riscossioni
   * @return riscossioni
  **/
  @JsonProperty("riscossioni")
  public List<Riscossione> getRiscossioni() {
    return riscossioni;
  }

  public void setRiscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
  }

  public Ricevuta riscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  public Ricevuta addRiscossioniItem(Riscossione riscossioniItem) {
    this.riscossioni.add(riscossioniItem);
    return this;
  }

 /**
   * Data di acquisizione della ricevuta
   * @return data
  **/
  @JsonProperty("data")
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public Ricevuta data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Data di esecuzione della riscossione
   * @return dataPagamento
  **/
  @JsonProperty("dataPagamento")
  public Date getDataPagamento() {
    return dataPagamento;
  }

  public void setDataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public Ricevuta dataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
    return this;
  }

 /**
   * Get rpt
   * @return rpt
  **/
  @JsonProperty("rpt")
  public RicevutaRpt getRpt() {
    return rpt;
  }

  public void setRpt(RicevutaRpt rpt) {
    this.rpt = rpt;
  }

  public Ricevuta rpt(RicevutaRpt rpt) {
    this.rpt = rpt;
    return this;
  }

 /**
   * Get rt
   * @return rt
  **/
  @JsonProperty("rt")
  public RicevutaRt getRt() {
    return rt;
  }

  public void setRt(RicevutaRt rt) {
    this.rt = rt;
  }

  public Ricevuta rt(RicevutaRt rt) {
    this.rt = rt;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Ricevuta {\n");
    
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    idRicevuta: ").append(toIndentedString(idRicevuta)).append("\n");
    sb.append("    attestante: ").append(toIndentedString(attestante)).append("\n");
    sb.append("    versante: ").append(toIndentedString(versante)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
    sb.append("    riscossioni: ").append(toIndentedString(riscossioni)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    dataPagamento: ").append(toIndentedString(dataPagamento)).append("\n");
    sb.append("    rpt: ").append(toIndentedString(rpt)).append("\n");
    sb.append("    rt: ").append(toIndentedString(rt)).append("\n");
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
