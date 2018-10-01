package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idFlusso",
"dataFlusso",
"trn",
"dataRegolamento",
"idPsp",
"ragioneSocialePsp",
"bicRiversamento",
"idDominio",
"ragioneSocialeDominio",
"numeroPagamenti",
"importoTotale",
"segnalazioni",
"rendicontazioni",
})
public class FlussoRendicontazione extends JSONSerializable {
  
  @JsonProperty("idFlusso")
  private String idFlusso = null;
  
  @JsonProperty("dataFlusso")
  private Date dataFlusso = null;
  
  @JsonProperty("trn")
  private String trn = null;
  
  @JsonProperty("dataRegolamento")
  private Date dataRegolamento = null;
  
  @JsonProperty("idPsp")
  private String idPsp = null;
  
  @JsonProperty("ragioneSocialePsp")
  private String ragioneSocialePsp = null;
  
  @JsonProperty("bicRiversamento")
  private String bicRiversamento = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("ragioneSocialeDominio")
  private String ragioneSocialeDominio = null;
  
  @JsonProperty("numeroPagamenti")
  private BigDecimal numeroPagamenti = null;
  
  @JsonProperty("importoTotale")
  private Double importoTotale = null;
  
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
    return this.idFlusso;
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
    return this.dataFlusso;
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
    return this.trn;
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
    return this.dataRegolamento;
  }
  public void setDataRegolamento(Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
  }

  /**
   * Identificativo del psp che ha emesso la rendicontazione
   **/
  public FlussoRendicontazione idPsp(String idPsp) {
    this.idPsp = idPsp;
    return this;
  }

  @JsonProperty("idPsp")
  public String getIdPsp() {
    return this.idPsp;
  }
  public void setIdPsp(String idPsp) {
    this.idPsp = idPsp;
  }
  
  /**
   * Ragione sociale del psp che ha emesso la rendicontazione
   **/
  public FlussoRendicontazione ragioneSocialePsp(String ragioneSocialePsp) {
    this.ragioneSocialePsp = ragioneSocialePsp;
    return this;
  }

  @JsonProperty("ragioneSocialePsp")
  public String getRagioneSocialePsp() {
    return this.ragioneSocialePsp;
  }
  public void setRagioneSocialePsp(String ragioneSocialePsp) {
    this.ragioneSocialePsp = ragioneSocialePsp;
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
    return this.bicRiversamento;
  }
  public void setBicRiversamento(String bicRiversamento) {
    this.bicRiversamento = bicRiversamento;
  }

  /**
   * Identificativo del dominio creditore del riversamento
   **/
  public FlussoRendicontazione idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return this.idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Ragione sociale del domino creditore del riversamento
   **/
  public FlussoRendicontazione ragioneSocialeDominio(String ragioneSocialeDominio) {
    this.ragioneSocialeDominio = ragioneSocialeDominio;
    return this;
  }

  @JsonProperty("ragioneSocialeDominio")
  public String getRagioneSocialeDominio() {
    return this.ragioneSocialeDominio;
  }
  public void setRagioneSocialeDominio(String ragioneSocialeDominio) {
    this.ragioneSocialeDominio = ragioneSocialeDominio;
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
    return this.numeroPagamenti;
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
    return this.importoTotale;
  }
  public void setImportoTotale(Double importoTotale) {
    this.importoTotale = importoTotale;
  }

  /**
   **/
  public FlussoRendicontazione segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return this.segnalazioni;
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
    return this.rendicontazioni;
  }
  public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
    this.rendicontazioni = rendicontazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    FlussoRendicontazione flussoRendicontazione = (FlussoRendicontazione) o;
    return Objects.equals(this.idFlusso, flussoRendicontazione.idFlusso) &&
        Objects.equals(this.dataFlusso, flussoRendicontazione.dataFlusso) &&
        Objects.equals(this.trn, flussoRendicontazione.trn) &&
        Objects.equals(this.dataRegolamento, flussoRendicontazione.dataRegolamento) &&
        Objects.equals(this.idPsp, flussoRendicontazione.idPsp) &&
        Objects.equals(this.ragioneSocialePsp, flussoRendicontazione.ragioneSocialePsp) &&
        Objects.equals(this.bicRiversamento, flussoRendicontazione.bicRiversamento) &&
        Objects.equals(this.idDominio, flussoRendicontazione.idDominio) &&
        Objects.equals(this.ragioneSocialeDominio, flussoRendicontazione.ragioneSocialeDominio) &&
        Objects.equals(this.numeroPagamenti, flussoRendicontazione.numeroPagamenti) &&
        Objects.equals(this.importoTotale, flussoRendicontazione.importoTotale) &&
        Objects.equals(this.segnalazioni, flussoRendicontazione.segnalazioni) &&
        Objects.equals(this.rendicontazioni, flussoRendicontazione.rendicontazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idFlusso, this.dataFlusso, this.trn, this.dataRegolamento, this.idPsp, this.ragioneSocialePsp, this.bicRiversamento, this.idDominio, this.ragioneSocialeDominio, this.numeroPagamenti, this.importoTotale, this.segnalazioni, this.rendicontazioni);
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
    sb.append("    ").append(this.toIndentedString(super.toString())).append("\n");
    sb.append("    idFlusso: ").append(this.toIndentedString(this.idFlusso)).append("\n");
    sb.append("    dataFlusso: ").append(this.toIndentedString(this.dataFlusso)).append("\n");
    sb.append("    trn: ").append(this.toIndentedString(this.trn)).append("\n");
    sb.append("    dataRegolamento: ").append(this.toIndentedString(this.dataRegolamento)).append("\n");
    sb.append("    idPsp: ").append(this.toIndentedString(this.idPsp)).append("\n");
    sb.append("    ragioneSocialePsp: ").append(this.toIndentedString(this.ragioneSocialePsp)).append("\n");
    sb.append("    bicRiversamento: ").append(this.toIndentedString(this.bicRiversamento)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    ragioneSocialeDominio: ").append(this.toIndentedString(this.ragioneSocialeDominio)).append("\n");
    sb.append("    numeroPagamenti: ").append(this.toIndentedString(this.numeroPagamenti)).append("\n");
    sb.append("    importoTotale: ").append(this.toIndentedString(this.importoTotale)).append("\n");
    sb.append("    segnalazioni: ").append(this.toIndentedString(this.segnalazioni)).append("\n");
    sb.append("    rendicontazioni: ").append(this.toIndentedString(this.rendicontazioni)).append("\n");
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



