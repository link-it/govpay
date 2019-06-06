package it.govpay.ec.v1.beans;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.v3.oas.annotations.media.Schema;

public class Notifica  {
  
  // @Schema(example = "A2A-12345", description = "Identificativo del gestionale responsabile della pendenza")
 /**
   * Identificativo del gestionale responsabile della pendenza  
  **/
  private String idA2A = null;
  
  // @Schema(example = "abcdef12345", description = "Identificativo della pendenza nel gestionale responsabile")
 /**
   * Identificativo della pendenza nel gestionale responsabile  
  **/
  private String idPendenza = null;
  
  // @Schema(example = "{\"versioneOggetto\":\"6.2\",\"dominio\":\"--[OMISSIS]--\",\"identificativoMessaggioRichiesta\":\"3014931b62ab4333be07164c2fda6fa3\",\"dataOraMessaggioRichiesta\":\"2018-06-01\",\"autenticazioneSoggetto\":\"N_A\",\"soggettoVersante\":\"--[OMISSIS]--\",\"soggettoPagatore\":\"--[OMISSIS]--\",\"enteBeneficiario\":\"--[OMISSIS]--\",\"datiVersamento\":\"--[OMISSIS]--\"}", description = "Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico")
 /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico  
  **/
  private Object rpt = null;
  
  // @Schema(example = "{\"versioneOggetto\":\"6.2\",\"dominio\":\"--[OMISSIS]--\",\"identificativoMessaggioRicevuta\":\"3014931b62ab4333be07164c2fda6fa3\",\"dataOraMessaggioRicevuta\":\"2018-06-01\",\"riferimentoMessaggioRichiesta\":\"3014931b62ab4333be07164c2fda6fa3\",\"riferimentoDataRichiesta\":\"2018-06-01\",\"istitutoAttestante\":\"--[OMISSIS]--\",\"enteBeneficiario\":\"--[OMISSIS]--\",\"soggettoVersante\":\"--[OMISSIS]--\",\"soggettoPagatore\":\"--[OMISSIS]--\",\"datiPagamento\":\"--[OMISSIS]--\"}", description = "Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica")
 /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica  
  **/
  private Object rt = null;
  
  // @Schema(description = "")
  private List<Riscossione> riscossioni = null;
 /**
   * Identificativo del gestionale responsabile della pendenza
   * @return idA2A
  **/
  @JsonProperty("idA2A")
  @Valid
  public String getIdA2A() {
    return idA2A;
  }

  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  public Notifica idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

 /**
   * Identificativo della pendenza nel gestionale responsabile
   * @return idPendenza
  **/
  @JsonProperty("idPendenza")
  @Valid
  public String getIdPendenza() {
    return idPendenza;
  }

  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  public Notifica idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

 /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico
   * @return rpt
  **/
  @JsonProperty("rpt")
  @Valid
  public Object getRpt() {
    return rpt;
  }

  public void setRpt(Object rpt) {
    this.rpt = rpt;
  }

  public Notifica rpt(Object rpt) {
    this.rpt = rpt;
    return this;
  }

 /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica
   * @return rt
  **/
  @JsonProperty("rt")
  @Valid
  public Object getRt() {
    return rt;
  }

  public void setRt(Object rt) {
    this.rt = rt;
  }

  public Notifica rt(Object rt) {
    this.rt = rt;
    return this;
  }

 /**
   * Get riscossioni
   * @return riscossioni
  **/
  @JsonProperty("riscossioni")
  @Valid
  public List<Riscossione> getRiscossioni() {
    return riscossioni;
  }

  public void setRiscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
  }

  public Notifica riscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  public Notifica addRiscossioniItem(Riscossione riscossioniItem) {
    this.riscossioni.add(riscossioniItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Notifica {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    rpt: ").append(toIndentedString(rpt)).append("\n");
    sb.append("    rt: ").append(toIndentedString(rt)).append("\n");
    sb.append("    riscossioni: ").append(toIndentedString(riscossioni)).append("\n");
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
