package it.govpay.ragioneria.v2.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idFlusso",
"dataFlusso",
"trn",
"dataRegolamento",
"idPsp",
"bicRiversamento",
"idDominio",
"numeroPagamenti",
"importoTotale",
"stato",
"segnalazioni",
"rendicontazioni",
})
public class FlussoRendicontazione extends JSONSerializable {
  
  @JsonProperty("idFlusso")
  private String idFlusso = null;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS", locale = "it_IT", timezone = "Europe/Rome")
  @JsonProperty("dataFlusso")
  private Date dataFlusso = null;
  
  @JsonProperty("trn")
  private String trn = null;
  
  @JsonProperty("dataRegolamento")
  private Date dataRegolamento = null;
  
  @JsonProperty("idPsp")
  private String idPsp = null;
  
  @JsonProperty("bicRiversamento")
  private String bicRiversamento = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("numeroPagamenti")
  private BigDecimal numeroPagamenti = null;
  
  @JsonProperty("importoTotale")
  private Double importoTotale = null;
  
  @JsonProperty("stato")
  private StatoFlussoRendicontazione stato = null;
  
  @JsonProperty("segnalazioni")
  private List<Segnalazione> segnalazioni = null;
  
  @JsonProperty("rendicontazioni")
  private List<Rendicontazione> rendicontazioni = null;
  
  /**
   * identificativo del flusso di rendicontazione
   **/
  public FlussoRendicontazione idFlusso(String idFlusso) {
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
   * Data di emissione del flusso
   **/
  public FlussoRendicontazione dataFlusso(Date dataFlusso) {
    this.dataFlusso = dataFlusso;
    return this;
  }

  @JsonProperty("dataFlusso")
  public Date getDataFlusso() {
    return dataFlusso;
  }
  public void setDataFlusso(Date dataFlusso) {
    this.dataFlusso = dataFlusso;
  }

  /**
   * Identificativo dell'operazione di riversamento assegnato dal psp debitore
   **/
  public FlussoRendicontazione trn(String trn) {
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
  public FlussoRendicontazione dataRegolamento(Date dataRegolamento) {
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
   * Identificativo dell'istituto mittente
   **/
  public FlussoRendicontazione idPsp(String idPsp) {
    this.idPsp = idPsp;
    return this;
  }

  @JsonProperty("idPsp")
  public String getIdPsp() {
    return idPsp;
  }
  public void setIdPsp(String idPsp) {
    this.idPsp = idPsp;
  }

  /**
   * Codice Bic della banca che ha generato il riversamento
   **/
  public FlussoRendicontazione bicRiversamento(String bicRiversamento) {
    this.bicRiversamento = bicRiversamento;
    return this;
  }

  @JsonProperty("bicRiversamento")
  public String getBicRiversamento() {
    return bicRiversamento;
  }
  public void setBicRiversamento(String bicRiversamento) {
    this.bicRiversamento = bicRiversamento;
  }

  /**
   * Identificativo dominio destinatario
   **/
  public FlussoRendicontazione idDominio(String idDominio) {
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
   * numero di pagamenti oggetto della rendicontazione
   **/
  public FlussoRendicontazione numeroPagamenti(BigDecimal numeroPagamenti) {
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
  public FlussoRendicontazione importoTotale(Double importoTotale) {
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
   **/
  public FlussoRendicontazione stato(StatoFlussoRendicontazione stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoFlussoRendicontazione getStato() {
    return stato;
  }
  public void setStato(StatoFlussoRendicontazione stato) {
    this.stato = stato;
  }

  /**
   **/
  public FlussoRendicontazione segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return segnalazioni;
  }
  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  /**
   **/
  public FlussoRendicontazione rendicontazioni(List<Rendicontazione> rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
    return this;
  }

  @JsonProperty("rendicontazioni")
  public List<Rendicontazione> getRendicontazioni() {
    return rendicontazioni;
  }
  public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlussoRendicontazione flussoRendicontazione = (FlussoRendicontazione) o;
    return Objects.equals(idFlusso, flussoRendicontazione.idFlusso) &&
        Objects.equals(dataFlusso, flussoRendicontazione.dataFlusso) &&
        Objects.equals(trn, flussoRendicontazione.trn) &&
        Objects.equals(dataRegolamento, flussoRendicontazione.dataRegolamento) &&
        Objects.equals(idPsp, flussoRendicontazione.idPsp) &&
        Objects.equals(bicRiversamento, flussoRendicontazione.bicRiversamento) &&
        Objects.equals(idDominio, flussoRendicontazione.idDominio) &&
        Objects.equals(numeroPagamenti, flussoRendicontazione.numeroPagamenti) &&
        Objects.equals(importoTotale, flussoRendicontazione.importoTotale) &&
        Objects.equals(stato, flussoRendicontazione.stato) &&
        Objects.equals(segnalazioni, flussoRendicontazione.segnalazioni) &&
        Objects.equals(rendicontazioni, flussoRendicontazione.rendicontazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idFlusso, dataFlusso, trn, dataRegolamento, idPsp, bicRiversamento, idDominio, numeroPagamenti, importoTotale, stato, segnalazioni, rendicontazioni);
  }

  public static FlussoRendicontazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, FlussoRendicontazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "flussoRendicontazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlussoRendicontazione {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    idFlusso: ").append(toIndentedString(idFlusso)).append("\n");
    sb.append("    dataFlusso: ").append(toIndentedString(dataFlusso)).append("\n");
    sb.append("    trn: ").append(toIndentedString(trn)).append("\n");
    sb.append("    dataRegolamento: ").append(toIndentedString(dataRegolamento)).append("\n");
    sb.append("    idPsp: ").append(toIndentedString(idPsp)).append("\n");
    sb.append("    bicRiversamento: ").append(toIndentedString(bicRiversamento)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    numeroPagamenti: ").append(toIndentedString(numeroPagamenti)).append("\n");
    sb.append("    importoTotale: ").append(toIndentedString(importoTotale)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
    sb.append("    rendicontazioni: ").append(toIndentedString(rendicontazioni)).append("\n");
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



