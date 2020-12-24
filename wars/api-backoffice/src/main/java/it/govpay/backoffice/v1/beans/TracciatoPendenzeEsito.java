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
"esito",
})
public class TracciatoPendenzeEsito extends JSONSerializable {
  
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
  
  @JsonProperty("esito")
  private DettaglioTracciatoPendenzeEsito esito = null;
  
  /**
   * Identificativo numerico del tracciato
   **/
  public TracciatoPendenzeEsito id(BigDecimal id) {
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
  public TracciatoPendenzeEsito nomeFile(String nomeFile) {
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
  public TracciatoPendenzeEsito dominio(DominioIndex dominio) {
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
  public TracciatoPendenzeEsito dataOraCaricamento(Date dataOraCaricamento) {
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
  public TracciatoPendenzeEsito stato(StatoTracciatoPendenza stato) {
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
  public TracciatoPendenzeEsito descrizioneStato(String descrizioneStato) {
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
  public TracciatoPendenzeEsito numeroOperazioniTotali(BigDecimal numeroOperazioniTotali) {
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
  public TracciatoPendenzeEsito numeroOperazioniEseguite(BigDecimal numeroOperazioniEseguite) {
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
  public TracciatoPendenzeEsito numeroOperazioniFallite(BigDecimal numeroOperazioniFallite) {
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
  public TracciatoPendenzeEsito numeroAvvisiTotali(BigDecimal numeroAvvisiTotali) {
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
  public TracciatoPendenzeEsito numeroAvvisiStampati(BigDecimal numeroAvvisiStampati) {
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
  public TracciatoPendenzeEsito numeroAvvisiFalliti(BigDecimal numeroAvvisiFalliti) {
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
  public TracciatoPendenzeEsito operatoreMittente(String operatoreMittente) {
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
  public TracciatoPendenzeEsito dataOraUltimoAggiornamento(Date dataOraUltimoAggiornamento) {
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
  public TracciatoPendenzeEsito stampaAvvisi(Boolean stampaAvvisi) {
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

  /**
   **/
  public TracciatoPendenzeEsito esito(DettaglioTracciatoPendenzeEsito esito) {
    this.esito = esito;
    return this;
  }

  @JsonProperty("esito")
  public DettaglioTracciatoPendenzeEsito getEsito() {
    return esito;
  }
  public void setEsito(DettaglioTracciatoPendenzeEsito esito) {
    this.esito = esito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TracciatoPendenzeEsito tracciatoPendenzeEsito = (TracciatoPendenzeEsito) o;
    return Objects.equals(id, tracciatoPendenzeEsito.id) &&
        Objects.equals(nomeFile, tracciatoPendenzeEsito.nomeFile) &&
        Objects.equals(dominio, tracciatoPendenzeEsito.dominio) &&
        Objects.equals(dataOraCaricamento, tracciatoPendenzeEsito.dataOraCaricamento) &&
        Objects.equals(stato, tracciatoPendenzeEsito.stato) &&
        Objects.equals(descrizioneStato, tracciatoPendenzeEsito.descrizioneStato) &&
        Objects.equals(numeroOperazioniTotali, tracciatoPendenzeEsito.numeroOperazioniTotali) &&
        Objects.equals(numeroOperazioniEseguite, tracciatoPendenzeEsito.numeroOperazioniEseguite) &&
        Objects.equals(numeroOperazioniFallite, tracciatoPendenzeEsito.numeroOperazioniFallite) &&
        Objects.equals(numeroAvvisiTotali, tracciatoPendenzeEsito.numeroAvvisiTotali) &&
        Objects.equals(numeroAvvisiStampati, tracciatoPendenzeEsito.numeroAvvisiStampati) &&
        Objects.equals(numeroAvvisiFalliti, tracciatoPendenzeEsito.numeroAvvisiFalliti) &&
        Objects.equals(operatoreMittente, tracciatoPendenzeEsito.operatoreMittente) &&
        Objects.equals(dataOraUltimoAggiornamento, tracciatoPendenzeEsito.dataOraUltimoAggiornamento) &&
        Objects.equals(stampaAvvisi, tracciatoPendenzeEsito.stampaAvvisi) &&
        Objects.equals(esito, tracciatoPendenzeEsito.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nomeFile, dominio, dataOraCaricamento, stato, descrizioneStato, numeroOperazioniTotali, numeroOperazioniEseguite, numeroOperazioniFallite, numeroAvvisiTotali, numeroAvvisiStampati, numeroAvvisiFalliti, operatoreMittente, dataOraUltimoAggiornamento, stampaAvvisi, esito);
  }

  public static TracciatoPendenzeEsito parse(String json) throws ServiceException, ValidationException {
    return parse(json, TracciatoPendenzeEsito.class); 
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciatoPendenzeEsito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TracciatoPendenzeEsito {\n");
    
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
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
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



