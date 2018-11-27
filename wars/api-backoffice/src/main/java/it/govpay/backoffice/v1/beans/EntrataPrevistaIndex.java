package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
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
        Objects.equals(idA2A, entrataPrevistaIndex.idA2A);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, iuv, iur, idFlusso, trn, dataRegolamento, numeroPagamenti, importoTotale, importoPagato, dataPagamento, idVocePendenza, indice, idPendenza, idA2A);
  }

  public static EntrataPrevistaIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException{
    return (EntrataPrevistaIndex) parse(json, EntrataPrevistaIndex.class);
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



