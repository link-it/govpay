/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"iuv",
"iur",
"idFlusso",
"trn",
"dataRegolamento",
"numeroPagamenti",
"importoTotale",
"importoPagato",
"dataPagamento",
"idVocePendenza",
"indice",
"idPendenza",
"idA2A",
"idTipoPendenza",
"idEntrata",
"identificativoDebitore",
"anno",
})
public class EntrataPrevistaIndex extends JSONSerializable {

  @JsonProperty("idDominio")
  private String idDominio = null;

  @JsonProperty("iuv")
  private String iuv = null;

  @JsonProperty("iur")
  private String iur = null;

  @JsonProperty("idFlusso")
  private String idFlusso = null;

  @JsonProperty("trn")
  private String trn = null;

  @JsonProperty("dataRegolamento")
  private Date dataRegolamento = null;

  @JsonProperty("numeroPagamenti")
  private BigDecimal numeroPagamenti = null;

  @JsonProperty("importoTotale")
  private Double importoTotale = null;

  @JsonProperty("importoPagato")
  private BigDecimal importoPagato = null;

  @JsonProperty("dataPagamento")
  private Date dataPagamento = null;

  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;

  @JsonProperty("indice")
  private BigDecimal indice = null;

  @JsonProperty("idPendenza")
  private String idPendenza = null;

  @JsonProperty("idA2A")
  private String idA2A = null;

  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;

  @JsonProperty("idEntrata")
  private String idEntrata = null;

  @JsonProperty("identificativoDebitore")
  private String identificativoDebitore = null;

  @JsonProperty("anno")
  private BigDecimal anno = null;

  /**
   * Codice fiscale del dominio beneficiario
   **/
  public EntrataPrevistaIndex idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Identificativo univoco di versamento
   **/
  public EntrataPrevistaIndex iuv(String iuv) {
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
   * Identificativo univoco di riscossione
   **/
  public EntrataPrevistaIndex iur(String iur) {
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
   * Identificativo del flusso di rendicontazione
   **/
  public EntrataPrevistaIndex idFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
    return this;
  }

  @JsonProperty("idFlusso")
  public String getIdFlusso() {
    return idFlusso;
  }
  public void setIdFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
  }

  /**
   * Identificativo dell'operazione di riversamento assegnato dal psp debitore
   **/
  public EntrataPrevistaIndex trn(String trn) {
    this.trn = trn;
    return this;
  }

  @JsonProperty("trn")
  public String getTrn() {
    return trn;
  }
  public void setTrn(String trn) {
    this.trn = trn;
  }

  /**
   * Data dell'operazione di riversamento fondi
   **/
  public EntrataPrevistaIndex dataRegolamento(Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
    return this;
  }

  @JsonProperty("dataRegolamento")
  public Date getDataRegolamento() {
    return dataRegolamento;
  }
  public void setDataRegolamento(Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
  }

  /**
   * numero di pagamenti oggetto della rendicontazione
   **/
  public EntrataPrevistaIndex numeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
    return this;
  }

  @JsonProperty("numeroPagamenti")
  public BigDecimal getNumeroPagamenti() {
    return numeroPagamenti;
  }
  public void setNumeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
  }

  /**
   * somma degli importi rendicontati
   **/
  public EntrataPrevistaIndex importoTotale(Double importoTotale) {
    this.importoTotale = importoTotale;
    return this;
  }

  @JsonProperty("importoTotale")
  public Double getImportoTotale() {
    return importoTotale;
  }
  public void setImportoTotale(Double importoTotale) {
    this.importoTotale = importoTotale;
  }

  /**
   * Importo Pagato.
   **/
  public EntrataPrevistaIndex importoPagato(BigDecimal importoPagato) {
    this.importoPagato = importoPagato;
    return this;
  }

  @JsonProperty("importoPagato")
  public BigDecimal getImportoPagato() {
    return importoPagato;
  }
  public void setImportoPagato(BigDecimal importoPagato) {
    this.importoPagato = importoPagato;
  }

  /**
   * Data di pagamento della pendenza
   **/
  public EntrataPrevistaIndex dataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
    return this;
  }

  @JsonProperty("dataPagamento")
  public Date getDataPagamento() {
    return dataPagamento;
  }
  public void setDataPagamento(Date dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   **/
  public EntrataPrevistaIndex idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return idVocePendenza;
  }
  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  /**
   * indice di voce all'interno della pendenza
   **/
  public EntrataPrevistaIndex indice(BigDecimal indice) {
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
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public EntrataPrevistaIndex idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   * Identificativo applicazione
   **/
  public EntrataPrevistaIndex idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della tipologia pendenza
   **/
  public EntrataPrevistaIndex idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  /**
   * Identificativo dell'entrata
   **/
  public EntrataPrevistaIndex idEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
    return this;
  }

  @JsonProperty("idEntrata")
  public String getIdEntrata() {
    return idEntrata;
  }
  public void setIdEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
  }

  /**
   * Codice fiscale o partita iva del soggetto
   **/
  public EntrataPrevistaIndex identificativoDebitore(String identificativoDebitore) {
    this.identificativoDebitore = identificativoDebitore;
    return this;
  }

  @JsonProperty("identificativoDebitore")
  public String getIdentificativoDebitore() {
    return identificativoDebitore;
  }
  public void setIdentificativoDebitore(String identificativoDebitore) {
    this.identificativoDebitore = identificativoDebitore;
  }

  /**
   * Anno di riferimento della pendenza
   **/
  public EntrataPrevistaIndex anno(BigDecimal anno) {
    this.anno = anno;
    return this;
  }

  @JsonProperty("anno")
  public BigDecimal getAnno() {
    return anno;
  }
  public void setAnno(BigDecimal anno) {
    this.anno = anno;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntrataPrevistaIndex entrataPrevistaIndex = (EntrataPrevistaIndex) o;
    return Objects.equals(idDominio, entrataPrevistaIndex.idDominio) &&
        Objects.equals(iuv, entrataPrevistaIndex.iuv) &&
        Objects.equals(iur, entrataPrevistaIndex.iur) &&
        Objects.equals(idFlusso, entrataPrevistaIndex.idFlusso) &&
        Objects.equals(trn, entrataPrevistaIndex.trn) &&
        Objects.equals(dataRegolamento, entrataPrevistaIndex.dataRegolamento) &&
        Objects.equals(numeroPagamenti, entrataPrevistaIndex.numeroPagamenti) &&
        Objects.equals(importoTotale, entrataPrevistaIndex.importoTotale) &&
        Objects.equals(importoPagato, entrataPrevistaIndex.importoPagato) &&
        Objects.equals(dataPagamento, entrataPrevistaIndex.dataPagamento) &&
        Objects.equals(idVocePendenza, entrataPrevistaIndex.idVocePendenza) &&
        Objects.equals(indice, entrataPrevistaIndex.indice) &&
        Objects.equals(idPendenza, entrataPrevistaIndex.idPendenza) &&
        Objects.equals(idA2A, entrataPrevistaIndex.idA2A) &&
        Objects.equals(idTipoPendenza, entrataPrevistaIndex.idTipoPendenza) &&
        Objects.equals(idEntrata, entrataPrevistaIndex.idEntrata) &&
        Objects.equals(identificativoDebitore, entrataPrevistaIndex.identificativoDebitore) &&
        Objects.equals(anno, entrataPrevistaIndex.anno);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, iuv, iur, idFlusso, trn, dataRegolamento, numeroPagamenti, importoTotale, importoPagato, dataPagamento, idVocePendenza, indice, idPendenza, idA2A, idTipoPendenza, idEntrata, identificativoDebitore, anno);
  }

  public static EntrataPrevistaIndex parse(String json) throws IOException{
    return parse(json, EntrataPrevistaIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "entrataPrevistaIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntrataPrevistaIndex {\n");

    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    idFlusso: ").append(toIndentedString(idFlusso)).append("\n");
    sb.append("    trn: ").append(toIndentedString(trn)).append("\n");
    sb.append("    dataRegolamento: ").append(toIndentedString(dataRegolamento)).append("\n");
    sb.append("    numeroPagamenti: ").append(toIndentedString(numeroPagamenti)).append("\n");
    sb.append("    importoTotale: ").append(toIndentedString(importoTotale)).append("\n");
    sb.append("    importoPagato: ").append(toIndentedString(importoPagato)).append("\n");
    sb.append("    dataPagamento: ").append(toIndentedString(dataPagamento)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    idEntrata: ").append(toIndentedString(idEntrata)).append("\n");
    sb.append("    identificativoDebitore: ").append(toIndentedString(identificativoDebitore)).append("\n");
    sb.append("    anno: ").append(toIndentedString(anno)).append("\n");
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



