package it.govpay.ragioneria.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

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
"segnalazioni",
})
public class FlussoRendicontazioneIndex extends JSONSerializable {
  
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
  
  @JsonProperty("bicRiversamento")
  private String bicRiversamento = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("numeroPagamenti")
  private BigDecimal numeroPagamenti = null;
  
  @JsonProperty("importoTotale")
  private Double importoTotale = null;
  
  @JsonProperty("segnalazioni")
  private List<Segnalazione> segnalazioni = null;
  
  /**
   * identificativo del flusso di rendicontazione
   **/
  public FlussoRendicontazioneIndex idFlusso(String idFlusso) {
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
  public FlussoRendicontazioneIndex dataFlusso(Date dataFlusso) {
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
  public FlussoRendicontazioneIndex trn(String trn) {
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
  public FlussoRendicontazioneIndex dataRegolamento(Date dataRegolamento) {
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
   **/
  public FlussoRendicontazioneIndex idPsp(String idPsp) {
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
   * Codice Bic della banca che ha generato il riversamento
   **/
  public FlussoRendicontazioneIndex bicRiversamento(String bicRiversamento) {
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
   **/
  public FlussoRendicontazioneIndex dominio(String idDominio) {
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
   * numero di pagamenti oggetto della rendicontazione
   **/
  public FlussoRendicontazioneIndex numeroPagamenti(BigDecimal numeroPagamenti) {
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
  public FlussoRendicontazioneIndex importoTotale(Double importoTotale) {
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
  public FlussoRendicontazioneIndex segnalazioni(List<Segnalazione> segnalazioni) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    FlussoRendicontazioneIndex flussoRendicontazioneIndex = (FlussoRendicontazioneIndex) o;
    return Objects.equals(this.idFlusso, flussoRendicontazioneIndex.idFlusso) &&
        Objects.equals(this.dataFlusso, flussoRendicontazioneIndex.dataFlusso) &&
        Objects.equals(this.trn, flussoRendicontazioneIndex.trn) &&
        Objects.equals(this.dataRegolamento, flussoRendicontazioneIndex.dataRegolamento) &&
        Objects.equals(this.idPsp, flussoRendicontazioneIndex.idPsp) &&
        Objects.equals(this.bicRiversamento, flussoRendicontazioneIndex.bicRiversamento) &&
        Objects.equals(this.idDominio, flussoRendicontazioneIndex.idDominio) &&
        Objects.equals(this.numeroPagamenti, flussoRendicontazioneIndex.numeroPagamenti) &&
        Objects.equals(this.importoTotale, flussoRendicontazioneIndex.importoTotale) &&
        Objects.equals(this.segnalazioni, flussoRendicontazioneIndex.segnalazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idFlusso, this.dataFlusso, this.trn, this.dataRegolamento, this.idPsp, this.bicRiversamento, this.idDominio, this.numeroPagamenti, this.importoTotale, this.segnalazioni);
  }

  public static FlussoRendicontazioneIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, FlussoRendicontazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "flussoRendicontazioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlussoRendicontazioneIndex {\n");
    
    sb.append("    idFlusso: ").append(this.toIndentedString(this.idFlusso)).append("\n");
    sb.append("    dataFlusso: ").append(this.toIndentedString(this.dataFlusso)).append("\n");
    sb.append("    trn: ").append(this.toIndentedString(this.trn)).append("\n");
    sb.append("    dataRegolamento: ").append(this.toIndentedString(this.dataRegolamento)).append("\n");
    sb.append("    idPsp: ").append(this.toIndentedString(this.idPsp)).append("\n");
    sb.append("    bicRiversamento: ").append(this.toIndentedString(this.bicRiversamento)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    numeroPagamenti: ").append(this.toIndentedString(this.numeroPagamenti)).append("\n");
    sb.append("    importoTotale: ").append(this.toIndentedString(this.importoTotale)).append("\n");
    sb.append("    segnalazioni: ").append(this.toIndentedString(this.segnalazioni)).append("\n");
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



