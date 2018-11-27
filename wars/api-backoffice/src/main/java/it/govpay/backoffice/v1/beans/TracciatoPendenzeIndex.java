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
"dataOraCaricamento",
"stato",
"numeroOperazioniTotali",
"numeroOperazioniEseguite",
"numeroOperazioniFallite",
"operatoreMittente",
"dataOraUltimoAggiornamento",
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
  
  @JsonProperty("numeroOperazioniTotali")
  private BigDecimal numeroOperazioniTotali = null;
  
  @JsonProperty("numeroOperazioniEseguite")
  private BigDecimal numeroOperazioniEseguite = null;
  
  @JsonProperty("numeroOperazioniFallite")
  private BigDecimal numeroOperazioniFallite = null;
  
  @JsonProperty("operatoreMittente")
  private String operatoreMittente = null;
  
  @JsonProperty("dataOraUltimoAggiornamento")
  private Date dataOraUltimoAggiornamento = null;
  
  /**
   * Identificativo numerico del tracciato
   **/
  public TracciatoPendenzeIndex id(BigDecimal id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public BigDecimal getId() {
    return this.id;
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
    return this.nomeFile;
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
    return this.dominio;
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
    return this.dataOraCaricamento;
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
    return this.stato;
  }
  public void setStato(StatoTracciatoPendenza stato) {
    this.stato = stato;
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
    return this.numeroOperazioniTotali;
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
    return this.numeroOperazioniEseguite;
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
    return this.numeroOperazioniFallite;
  }
  public void setNumeroOperazioniFallite(BigDecimal numeroOperazioniFallite) {
    this.numeroOperazioniFallite = numeroOperazioniFallite;
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
    return this.operatoreMittente;
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
    return this.dataOraUltimoAggiornamento;
  }
  public void setDataOraUltimoAggiornamento(Date dataOraUltimoAggiornamento) {
    this.dataOraUltimoAggiornamento = dataOraUltimoAggiornamento;
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
    return Objects.equals(this.id, tracciatoPendenzeIndex.id) &&
        Objects.equals(this.nomeFile, tracciatoPendenzeIndex.nomeFile) &&
        Objects.equals(this.dominio, tracciatoPendenzeIndex.dominio) &&
        Objects.equals(this.dataOraCaricamento, tracciatoPendenzeIndex.dataOraCaricamento) &&
        Objects.equals(this.stato, tracciatoPendenzeIndex.stato) &&
        Objects.equals(this.numeroOperazioniTotali, tracciatoPendenzeIndex.numeroOperazioniTotali) &&
        Objects.equals(this.numeroOperazioniEseguite, tracciatoPendenzeIndex.numeroOperazioniEseguite) &&
        Objects.equals(this.numeroOperazioniFallite, tracciatoPendenzeIndex.numeroOperazioniFallite) &&
        Objects.equals(this.operatoreMittente, tracciatoPendenzeIndex.operatoreMittente) &&
        Objects.equals(this.dataOraUltimoAggiornamento, tracciatoPendenzeIndex.dataOraUltimoAggiornamento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.nomeFile, this.dominio, this.dataOraCaricamento, this.stato, this.numeroOperazioniTotali, this.numeroOperazioniEseguite, this.numeroOperazioniFallite, this.operatoreMittente, this.dataOraUltimoAggiornamento);
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
    
    sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
    sb.append("    nomeFile: ").append(this.toIndentedString(this.nomeFile)).append("\n");
    sb.append("    dominio: ").append(this.toIndentedString(this.dominio)).append("\n");
    sb.append("    dataOraCaricamento: ").append(this.toIndentedString(this.dataOraCaricamento)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    numeroOperazioniTotali: ").append(this.toIndentedString(this.numeroOperazioniTotali)).append("\n");
    sb.append("    numeroOperazioniEseguite: ").append(this.toIndentedString(this.numeroOperazioniEseguite)).append("\n");
    sb.append("    numeroOperazioniFallite: ").append(this.toIndentedString(this.numeroOperazioniFallite)).append("\n");
    sb.append("    operatoreMittente: ").append(this.toIndentedString(this.operatoreMittente)).append("\n");
    sb.append("    dataOraUltimoAggiornamento: ").append(this.toIndentedString(this.dataOraUltimoAggiornamento)).append("\n");
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



