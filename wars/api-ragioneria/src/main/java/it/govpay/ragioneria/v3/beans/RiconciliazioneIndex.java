package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
"idDominio",
"stato",
"descrizioneStato",
"importo",
"data",
"dataValuta",
"dataContabile",
"contoAccredito",
"sct",
"trn",
"causale",
"iuv",
"idFlussoRendicontazione",
})
public class RiconciliazioneIndex extends JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("stato")
  private StatoRiconciliazione stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("contoAccredito")
  private String contoAccredito = null;
  
  @JsonProperty("sct")
  private String sct = null;
  
  @JsonProperty("trn")
  private String trn = null;
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("idFlussoRendicontazione")
  private String idFlussoRendicontazione = null;
  
  /**
   * Identificativo della riconciliazione assegnato da GovPay
   **/
  public RiconciliazioneIndex id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Identificativo ente creditore
   **/
  public RiconciliazioneIndex idDominio(String idDominio) {
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
   **/
  public RiconciliazioneIndex stato(StatoRiconciliazione stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoRiconciliazione getStato() {
    return stato;
  }
  public void setStato(StatoRiconciliazione stato) {
    this.stato = stato;
  }

  /**
   * Dettaglio dello stato riconciliazione
   **/
  public RiconciliazioneIndex descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }

  @JsonProperty("descrizioneStato")
  public String getDescrizioneStato() {
    return descrizioneStato;
  }
  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  /**
   * Importo del riversamento. Se valorizzato, viene verificato che corrisponda a quello dei pagamenti riconciliati.
   **/
  public RiconciliazioneIndex importo(BigDecimal importo) {
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
   * Data di esecuzione della riconciliazione
   **/
  public RiconciliazioneIndex data(Date data) {
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
   * Data di valuta dell'incasso
   **/
  public RiconciliazioneIndex dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

  @JsonProperty("dataValuta")
  public Date getDataValuta() {
    return dataValuta;
  }
  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  /**
   * Data di contabile dell'incasso
   **/
  public RiconciliazioneIndex dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

  @JsonProperty("dataContabile")
  public Date getDataContabile() {
    return dataContabile;
  }
  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   **/
  public RiconciliazioneIndex contoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
    return this;
  }

  @JsonProperty("contoAccredito")
  public String getContoAccredito() {
    return contoAccredito;
  }
  public void setContoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
  }

  /**
   * Identificativo Sepa Credit Transfer
   **/
  public RiconciliazioneIndex sct(String sct) {
    this.sct = sct;
    return this;
  }

  @JsonProperty("sct")
  public String getSct() {
    return sct;
  }
  public void setSct(String sct) {
    this.sct = sct;
  }

  /**
   * Transaction reference number. Se valorizzato viene verificato che corrisponda a quello indicato nel Flusso di Rendicontazione.
   **/
  public RiconciliazioneIndex trn(String trn) {
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
   * Causale bancaria dell'SCT di riversamento fondi dal PSP al conto di accredito.
   **/
  public RiconciliazioneIndex causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }
  
  /**
   * Identificativo univoco di riscossione.
   **/
  public RiconciliazioneIndex iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return this.iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Identificativo del flusso di rendicontazione.
   **/
  public RiconciliazioneIndex idFlussoRendicontazione(String idFlusso) {
    this.idFlussoRendicontazione = idFlusso;
    return this;
  }

  @JsonProperty("idFlussoRendicontazione")
  public String getIdFlussoRendicontazione() {
    return this.idFlussoRendicontazione;
  }
  public void setIdFlussoRendicontazione(String idFlusso) {
    this.idFlussoRendicontazione = idFlusso;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiconciliazioneIndex riconciliazioneIndex = (RiconciliazioneIndex) o;
    return Objects.equals(id, riconciliazioneIndex.id) &&
        Objects.equals(idDominio, riconciliazioneIndex.idDominio) &&
        Objects.equals(stato, riconciliazioneIndex.stato) &&
        Objects.equals(descrizioneStato, riconciliazioneIndex.descrizioneStato) &&
        Objects.equals(importo, riconciliazioneIndex.importo) &&
        Objects.equals(data, riconciliazioneIndex.data) &&
        Objects.equals(dataValuta, riconciliazioneIndex.dataValuta) &&
        Objects.equals(dataContabile, riconciliazioneIndex.dataContabile) &&
        Objects.equals(contoAccredito, riconciliazioneIndex.contoAccredito) &&
        Objects.equals(sct, riconciliazioneIndex.sct) &&
        Objects.equals(trn, riconciliazioneIndex.trn) &&
        Objects.equals(causale, riconciliazioneIndex.causale) &&
        Objects.equals(this.iuv, riconciliazioneIndex.iuv) &&
        Objects.equals(this.idFlussoRendicontazione, riconciliazioneIndex.idFlussoRendicontazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idDominio, stato, descrizioneStato, importo, data, dataValuta, dataContabile, contoAccredito, sct, trn, causale, iuv, idFlussoRendicontazione);
  }

  public static RiconciliazioneIndex parse(String json) throws ServiceException, ValidationException {
    return (RiconciliazioneIndex) parse(json, RiconciliazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "riconciliazioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiconciliazioneIndex {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    contoAccredito: ").append(toIndentedString(contoAccredito)).append("\n");
    sb.append("    sct: ").append(toIndentedString(sct)).append("\n");
    sb.append("    trn: ").append(toIndentedString(trn)).append("\n");
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    idFlusso: ").append(this.toIndentedString(this.idFlussoRendicontazione)).append("\n");
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



