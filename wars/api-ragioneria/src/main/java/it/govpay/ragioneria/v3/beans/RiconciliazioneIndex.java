package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class RiconciliazioneIndex extends TipoRiferimentoRiconciliazione  {
  
  @Schema(example = "12345", required = true, description = "Identificativo della riconciliazione assegnato da GovPay")
 /**
   * Identificativo della riconciliazione assegnato da GovPay  
  **/
  private String id = null;
  
  @Schema(description = "")
  private Dominio dominio = null;
  
  @Schema(required = true, description = "")
  private StatoRiconciliazione stato = null;
  
  @Schema(description = "Dettaglio dello stato riconciliazione")
 /**
   * Dettaglio dello stato riconciliazione  
  **/
  private String descrizioneStato = null;
  
  @Schema(example = "100.01", description = "Importo del riversamento. Se valorizzato, viene verificato che corrisponda a quello dei pagamenti riconciliati.")
 /**
   * Importo del riversamento. Se valorizzato, viene verificato che corrisponda a quello dei pagamenti riconciliati.  
  **/
  private BigDecimal importo = null;
  
  @Schema(required = true, description = "Data di esecuzione della riconciliazione")
 /**
   * Data di esecuzione della riconciliazione  
  **/
  private Date data = null;
  
  @Schema(example = "Thu Dec 31 01:00:00 CET 2020", description = "Data di valuta dell'incasso")
 /**
   * Data di valuta dell'incasso  
  **/
  private Date dataValuta = null;
  
  @Schema(example = "Thu Dec 31 01:00:00 CET 2020", description = "Data di contabile dell'incasso")
 /**
   * Data di contabile dell'incasso  
  **/
  private Date dataContabile = null;
  
  @Schema(example = "IT60X0542811101000000123456", description = "Identificativo del conto di tesoreria su cui sono stati incassati i fondi")
 /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi  
  **/
  private String contoAccredito = null;
  
  @Schema(example = "2017-01-01ABI00000011234", description = "Identificativo Sepa Credit Transfer")
 /**
   * Identificativo Sepa Credit Transfer  
  **/
  private String sct = null;
  
  @Schema(example = "2017-01-01ABI00000011234", description = "Transaction reference number. Se valorizzato viene verificato che corrisponda a quello indicato nel Flusso di Rendicontazione.")
 /**
   * Transaction reference number. Se valorizzato viene verificato che corrisponda a quello indicato nel Flusso di Rendicontazione.  
  **/
  private String trn = null;
  
  @Schema(description = "Causale bancaria dell'SCT di riversamento fondi dal PSP al conto di accredito.")
 /**
   * Causale bancaria dell'SCT di riversamento fondi dal PSP al conto di accredito.  
  **/
  private String causale = null;
 /**
   * Identificativo della riconciliazione assegnato da GovPay
   * @return id
  **/
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RiconciliazioneIndex id(String id) {
    this.id = id;
    return this;
  }

 /**
   * Get dominio
   * @return dominio
  **/
  @JsonProperty("dominio")
  public Dominio getDominio() {
    return dominio;
  }

  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public RiconciliazioneIndex dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoRiconciliazione getStato() {
    return stato;
  }

  public void setStato(StatoRiconciliazione stato) {
    this.stato = stato;
  }

  public RiconciliazioneIndex stato(StatoRiconciliazione stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Dettaglio dello stato riconciliazione
   * @return descrizioneStato
  **/
  @JsonProperty("descrizioneStato")
  public String getDescrizioneStato() {
    return descrizioneStato;
  }

  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  public RiconciliazioneIndex descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }

 /**
   * Importo del riversamento. Se valorizzato, viene verificato che corrisponda a quello dei pagamenti riconciliati.
   * @return importo
  **/
  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public RiconciliazioneIndex importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Data di esecuzione della riconciliazione
   * @return data
  **/
  @JsonProperty("data")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale = "it_IT", timezone = "Europe/Rome")
  @NotNull
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public RiconciliazioneIndex data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Data di valuta dell&#x27;incasso
   * @return dataValuta
  **/
  @JsonProperty("dataValuta")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  public Date getDataValuta() {
    return dataValuta;
  }

  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  public RiconciliazioneIndex dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

 /**
   * Data di contabile dell&#x27;incasso
   * @return dataContabile
  **/
  @JsonProperty("dataContabile")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  public Date getDataContabile() {
    return dataContabile;
  }

  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  public RiconciliazioneIndex dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

 /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   * @return contoAccredito
  **/
  @JsonProperty("contoAccredito")
  public String getContoAccredito() {
    return contoAccredito;
  }

  public void setContoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
  }

  public RiconciliazioneIndex contoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
    return this;
  }

 /**
   * Identificativo Sepa Credit Transfer
   * @return sct
  **/
  @JsonProperty("sct")
  public String getSct() {
    return sct;
  }

  public void setSct(String sct) {
    this.sct = sct;
  }

  public RiconciliazioneIndex sct(String sct) {
    this.sct = sct;
    return this;
  }

 /**
   * Transaction reference number. Se valorizzato viene verificato che corrisponda a quello indicato nel Flusso di Rendicontazione.
   * @return trn
  **/
  @JsonProperty("trn")
  public String getTrn() {
    return trn;
  }

  public void setTrn(String trn) {
    this.trn = trn;
  }

  public RiconciliazioneIndex trn(String trn) {
    this.trn = trn;
    return this;
  }

 /**
   * Causale bancaria dell&#x27;SCT di riversamento fondi dal PSP al conto di accredito.
   * @return causale
  **/
  @JsonProperty("causale")
  public String getCausale() {
    return causale;
  }

  public void setCausale(String causale) {
    this.causale = causale;
  }

  public RiconciliazioneIndex causale(String causale) {
    this.causale = causale;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiconciliazioneIndex {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
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
