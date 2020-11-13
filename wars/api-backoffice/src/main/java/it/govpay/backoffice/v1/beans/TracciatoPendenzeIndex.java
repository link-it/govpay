package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
"nomeFile",
"dominio",
"dataOraCaricamento",
"stato",
"descrizioneStato",
"numeroOperazioniTotali",
"numeroOperazioniEseguite",
"numeroOperazioniFallite",
"numeroAvvisiTotali",
"numeroAvvisiStampati",
"numeroAvvisiFalliti",
"operatoreMittente",
"dataOraUltimoAggiornamento",
"stampaAvvisi",
})
public class TracciatoPendenzeIndex extends JSONSerializable {
  
  @JsonProperty("id")
  private BigDecimal id = null;
  
  @JsonProperty("nomeFile")
  private String nomeFile = null;
  
  @JsonProperty("dominio")
  private DominioIndex dominio = null;
  
  @JsonProperty("dataOraCaricamento")
  private Date dataOraCaricamento = null;
  
  @JsonProperty("stato")
  private StatoTracciatoPendenza stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
  @JsonProperty("numeroOperazioniTotali")
  private BigDecimal numeroOperazioniTotali = null;
  
  @JsonProperty("numeroOperazioniEseguite")
  private BigDecimal numeroOperazioniEseguite = null;
  
  @JsonProperty("numeroOperazioniFallite")
  private BigDecimal numeroOperazioniFallite = null;
  
  @JsonProperty("numeroAvvisiTotali")
  private BigDecimal numeroAvvisiTotali = null;
  
  @JsonProperty("numeroAvvisiStampati")
  private BigDecimal numeroAvvisiStampati = null;
  
  @JsonProperty("numeroAvvisiFalliti")
  private BigDecimal numeroAvvisiFalliti = null;
  
  @JsonProperty("operatoreMittente")
  private String operatoreMittente = null;
  
  @JsonProperty("dataOraUltimoAggiornamento")
  private Date dataOraUltimoAggiornamento = null;
  
  @JsonProperty("stampaAvvisi")
  private Boolean stampaAvvisi = null;
  
  /**
   * Identificativo numerico del tracciato
   **/
  public TracciatoPendenzeIndex id(BigDecimal id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public BigDecimal getId() {
    return id;
  }
  public void setId(BigDecimal id) {
    this.id = id;
  }

  /**
   * Nome del file tracciato
   **/
  public TracciatoPendenzeIndex nomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
    return this;
  }

  @JsonProperty("nomeFile")
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  public TracciatoPendenzeIndex dominio(DominioIndex dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public DominioIndex getDominio() {
    return dominio;
  }
  public void setDominio(DominioIndex dominio) {
    this.dominio = dominio;
  }

  /**
   * Data di caricamento del tracciato
   **/
  public TracciatoPendenzeIndex dataOraCaricamento(Date dataOraCaricamento) {
    this.dataOraCaricamento = dataOraCaricamento;
    return this;
  }

  @JsonProperty("dataOraCaricamento")
  public Date getDataOraCaricamento() {
    return dataOraCaricamento;
  }
  public void setDataOraCaricamento(Date dataOraCaricamento) {
    this.dataOraCaricamento = dataOraCaricamento;
  }

  /**
   **/
  public TracciatoPendenzeIndex stato(StatoTracciatoPendenza stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoTracciatoPendenza getStato() {
    return stato;
  }
  public void setStato(StatoTracciatoPendenza stato) {
    this.stato = stato;
  }

  /**
   * Descrizione dello stato del tracciato
   **/
  public TracciatoPendenzeIndex descrizioneStato(String descrizioneStato) {
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
   * Numero totale di operazioni previste
   **/
  public TracciatoPendenzeIndex numeroOperazioniTotali(BigDecimal numeroOperazioniTotali) {
    this.numeroOperazioniTotali = numeroOperazioniTotali;
    return this;
  }

  @JsonProperty("numeroOperazioniTotali")
  public BigDecimal getNumeroOperazioniTotali() {
    return numeroOperazioniTotali;
  }
  public void setNumeroOperazioniTotali(BigDecimal numeroOperazioniTotali) {
    this.numeroOperazioniTotali = numeroOperazioniTotali;
  }

  /**
   * Numero totale di operazioni eseguite con successo
   **/
  public TracciatoPendenzeIndex numeroOperazioniEseguite(BigDecimal numeroOperazioniEseguite) {
    this.numeroOperazioniEseguite = numeroOperazioniEseguite;
    return this;
  }

  @JsonProperty("numeroOperazioniEseguite")
  public BigDecimal getNumeroOperazioniEseguite() {
    return numeroOperazioniEseguite;
  }
  public void setNumeroOperazioniEseguite(BigDecimal numeroOperazioniEseguite) {
    this.numeroOperazioniEseguite = numeroOperazioniEseguite;
  }

  /**
   * Numero totale di operazioni fallite
   **/
  public TracciatoPendenzeIndex numeroOperazioniFallite(BigDecimal numeroOperazioniFallite) {
    this.numeroOperazioniFallite = numeroOperazioniFallite;
    return this;
  }

  @JsonProperty("numeroOperazioniFallite")
  public BigDecimal getNumeroOperazioniFallite() {
    return numeroOperazioniFallite;
  }
  public void setNumeroOperazioniFallite(BigDecimal numeroOperazioniFallite) {
    this.numeroOperazioniFallite = numeroOperazioniFallite;
  }

  /**
   * Numero totale di stampe previste
   **/
  public TracciatoPendenzeIndex numeroAvvisiTotali(BigDecimal numeroAvvisiTotali) {
    this.numeroAvvisiTotali = numeroAvvisiTotali;
    return this;
  }

  @JsonProperty("numeroAvvisiTotali")
  public BigDecimal getNumeroAvvisiTotali() {
    return numeroAvvisiTotali;
  }
  public void setNumeroAvvisiTotali(BigDecimal numeroAvvisiTotali) {
    this.numeroAvvisiTotali = numeroAvvisiTotali;
  }

  /**
   * Numero totale di stampe eseguite con successo
   **/
  public TracciatoPendenzeIndex numeroAvvisiStampati(BigDecimal numeroAvvisiStampati) {
    this.numeroAvvisiStampati = numeroAvvisiStampati;
    return this;
  }

  @JsonProperty("numeroAvvisiStampati")
  public BigDecimal getNumeroAvvisiStampati() {
    return numeroAvvisiStampati;
  }
  public void setNumeroAvvisiStampati(BigDecimal numeroAvvisiStampati) {
    this.numeroAvvisiStampati = numeroAvvisiStampati;
  }

  /**
   * Numero totale di stampe non eseguite a causa di errori
   **/
  public TracciatoPendenzeIndex numeroAvvisiFalliti(BigDecimal numeroAvvisiFalliti) {
    this.numeroAvvisiFalliti = numeroAvvisiFalliti;
    return this;
  }

  @JsonProperty("numeroAvvisiFalliti")
  public BigDecimal getNumeroAvvisiFalliti() {
    return numeroAvvisiFalliti;
  }
  public void setNumeroAvvisiFalliti(BigDecimal numeroAvvisiFalliti) {
    this.numeroAvvisiFalliti = numeroAvvisiFalliti;
  }

  /**
   * Nome operatore del cruscotto che ha effettuato l'operazione di caricamento
   **/
  public TracciatoPendenzeIndex operatoreMittente(String operatoreMittente) {
    this.operatoreMittente = operatoreMittente;
    return this;
  }

  @JsonProperty("operatoreMittente")
  public String getOperatoreMittente() {
    return operatoreMittente;
  }
  public void setOperatoreMittente(String operatoreMittente) {
    this.operatoreMittente = operatoreMittente;
  }

  /**
   * Data ultimo aggiornamento stato elaborazione del tracciato
   **/
  public TracciatoPendenzeIndex dataOraUltimoAggiornamento(Date dataOraUltimoAggiornamento) {
    this.dataOraUltimoAggiornamento = dataOraUltimoAggiornamento;
    return this;
  }

  @JsonProperty("dataOraUltimoAggiornamento")
  public Date getDataOraUltimoAggiornamento() {
    return dataOraUltimoAggiornamento;
  }
  public void setDataOraUltimoAggiornamento(Date dataOraUltimoAggiornamento) {
    this.dataOraUltimoAggiornamento = dataOraUltimoAggiornamento;
  }

  /**
   * indica se sono disponibili le stampe degli avvisi caricati con il tracciato
   **/
  public TracciatoPendenzeIndex stampaAvvisi(Boolean stampaAvvisi) {
    this.stampaAvvisi = stampaAvvisi;
    return this;
  }

  @JsonProperty("stampaAvvisi")
  public Boolean StampaAvvisi() {
    return stampaAvvisi;
  }
  public void setStampaAvvisi(Boolean stampaAvvisi) {
    this.stampaAvvisi = stampaAvvisi;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TracciatoPendenzeIndex tracciatoPendenzeIndex = (TracciatoPendenzeIndex) o;
    return Objects.equals(id, tracciatoPendenzeIndex.id) &&
        Objects.equals(nomeFile, tracciatoPendenzeIndex.nomeFile) &&
        Objects.equals(dominio, tracciatoPendenzeIndex.dominio) &&
        Objects.equals(dataOraCaricamento, tracciatoPendenzeIndex.dataOraCaricamento) &&
        Objects.equals(stato, tracciatoPendenzeIndex.stato) &&
        Objects.equals(descrizioneStato, tracciatoPendenzeIndex.descrizioneStato) &&
        Objects.equals(numeroOperazioniTotali, tracciatoPendenzeIndex.numeroOperazioniTotali) &&
        Objects.equals(numeroOperazioniEseguite, tracciatoPendenzeIndex.numeroOperazioniEseguite) &&
        Objects.equals(numeroOperazioniFallite, tracciatoPendenzeIndex.numeroOperazioniFallite) &&
        Objects.equals(numeroAvvisiTotali, tracciatoPendenzeIndex.numeroAvvisiTotali) &&
        Objects.equals(numeroAvvisiStampati, tracciatoPendenzeIndex.numeroAvvisiStampati) &&
        Objects.equals(numeroAvvisiFalliti, tracciatoPendenzeIndex.numeroAvvisiFalliti) &&
        Objects.equals(operatoreMittente, tracciatoPendenzeIndex.operatoreMittente) &&
        Objects.equals(dataOraUltimoAggiornamento, tracciatoPendenzeIndex.dataOraUltimoAggiornamento) &&
        Objects.equals(stampaAvvisi, tracciatoPendenzeIndex.stampaAvvisi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nomeFile, dominio, dataOraCaricamento, stato, descrizioneStato, numeroOperazioniTotali, numeroOperazioniEseguite, numeroOperazioniFallite, numeroAvvisiTotali, numeroAvvisiStampati, numeroAvvisiFalliti, operatoreMittente, dataOraUltimoAggiornamento, stampaAvvisi);
  }

  public static TracciatoPendenzeIndex parse(String json) throws ServiceException, ValidationException { 
    return parse(json, TracciatoPendenzeIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciatoPendenzeIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TracciatoPendenzeIndex {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    dataOraCaricamento: ").append(toIndentedString(dataOraCaricamento)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    numeroOperazioniTotali: ").append(toIndentedString(numeroOperazioniTotali)).append("\n");
    sb.append("    numeroOperazioniEseguite: ").append(toIndentedString(numeroOperazioniEseguite)).append("\n");
    sb.append("    numeroOperazioniFallite: ").append(toIndentedString(numeroOperazioniFallite)).append("\n");
    sb.append("    numeroAvvisiTotali: ").append(toIndentedString(numeroAvvisiTotali)).append("\n");
    sb.append("    numeroAvvisiStampati: ").append(toIndentedString(numeroAvvisiStampati)).append("\n");
    sb.append("    numeroAvvisiFalliti: ").append(toIndentedString(numeroAvvisiFalliti)).append("\n");
    sb.append("    operatoreMittente: ").append(toIndentedString(operatoreMittente)).append("\n");
    sb.append("    dataOraUltimoAggiornamento: ").append(toIndentedString(dataOraUltimoAggiornamento)).append("\n");
    sb.append("    stampaAvvisi: ").append(toIndentedString(stampaAvvisi)).append("\n");
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



