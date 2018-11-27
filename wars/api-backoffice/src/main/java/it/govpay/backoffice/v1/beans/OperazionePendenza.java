package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipoOperazione",
"stato",
"descrizioneStato",
"enteCreditore",
"soggettoPagatore",
"applicazione",
"identificativoPendenza",
"numeroAvviso",
"numero",
"richiesta",
"risposta",
})
public class OperazionePendenza extends JSONSerializable {
  
  @JsonProperty("tipoOperazione")
  private TipoOperazionePendenza tipoOperazione = null;
  
  @JsonProperty("stato")
  private StatoOperazionePendenza stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
  @JsonProperty("enteCreditore")
  private DominioIndex enteCreditore = null;
  
  @JsonProperty("soggettoPagatore")
  private Soggetto soggettoPagatore = null;
  
  @JsonProperty("applicazione")
  private String applicazione = null;
  
  @JsonProperty("identificativoPendenza")
  private String identificativoPendenza = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  @JsonProperty("numero")
  private BigDecimal numero = null;
  
  @JsonProperty("richiesta")
  private Object richiesta = null;
  
  @JsonProperty("risposta")
  private EsitoOperazionePendenza risposta = null;
  
  /**
   **/
  public OperazionePendenza tipoOperazione(TipoOperazionePendenza tipoOperazione) {
    this.tipoOperazione = tipoOperazione;
    return this;
  }

  @JsonProperty("tipoOperazione")
  public TipoOperazionePendenza getTipoOperazione() {
    return this.tipoOperazione;
  }
  public void setTipoOperazione(TipoOperazionePendenza tipoOperazione) {
    this.tipoOperazione = tipoOperazione;
  }

  /**
   **/
  public OperazionePendenza stato(StatoOperazionePendenza stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoOperazionePendenza getStato() {
    return this.stato;
  }
  public void setStato(StatoOperazionePendenza stato) {
    this.stato = stato;
  }

  /**
   * Descrizione dello stato operazione
   **/
  public OperazionePendenza descrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
    return this;
  }

  @JsonProperty("descrizioneStato")
  public String getDescrizioneStato() {
    return this.descrizioneStato;
  }
  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  /**
   **/
  public OperazionePendenza enteCreditore(DominioIndex enteCreditore) {
    this.enteCreditore = enteCreditore;
    return this;
  }

  @JsonProperty("enteCreditore")
  public DominioIndex getEnteCreditore() {
    return this.enteCreditore;
  }
  public void setEnteCreditore(DominioIndex enteCreditore) {
    this.enteCreditore = enteCreditore;
  }

  /**
   **/
  public OperazionePendenza soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

  @JsonProperty("soggettoPagatore")
  public Soggetto getSoggettoPagatore() {
    return this.soggettoPagatore;
  }
  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  /**
   * Applicazione che ha effettuato l'operazione
   **/
  public OperazionePendenza applicazione(String applicazione) {
    this.applicazione = applicazione;
    return this;
  }

  @JsonProperty("applicazione")
  public String getApplicazione() {
    return this.applicazione;
  }
  public void setApplicazione(String applicazione) {
    this.applicazione = applicazione;
  }

  /**
   * Identificativo della pendenza associata all'operazione
   **/
  public OperazionePendenza identificativoPendenza(String identificativoPendenza) {
    this.identificativoPendenza = identificativoPendenza;
    return this;
  }

  @JsonProperty("identificativoPendenza")
  public String getIdentificativoPendenza() {
    return this.identificativoPendenza;
  }
  public void setIdentificativoPendenza(String identificativoPendenza) {
    this.identificativoPendenza = identificativoPendenza;
  }

  /**
   * Numero Avviso generato
   **/
  public OperazionePendenza numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return this.numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Progressivo Operazione
   **/
  public OperazionePendenza numero(BigDecimal numero) {
    this.numero = numero;
    return this;
  }

  @JsonProperty("numero")
  public BigDecimal getNumero() {
    return this.numero;
  }
  public void setNumero(BigDecimal numero) {
    this.numero = numero;
  }

  /**
   **/
  public OperazionePendenza richiesta(Object richiesta) {
    this.richiesta = richiesta;
    return this;
  }

  @JsonProperty("richiesta")
  public Object getRichiesta() {
    return this.richiesta;
  }
  public void setRichiesta(Object richiesta) {
    this.richiesta = richiesta;
  }

  /**
   **/
  public OperazionePendenza risposta(EsitoOperazionePendenza risposta) {
    this.risposta = risposta;
    return this;
  }

  @JsonProperty("risposta")
  public EsitoOperazionePendenza getRisposta() {
    return this.risposta;
  }
  public void setRisposta(EsitoOperazionePendenza risposta) {
    this.risposta = risposta;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    OperazionePendenza operazionePendenza = (OperazionePendenza) o;
    return Objects.equals(this.tipoOperazione, operazionePendenza.tipoOperazione) &&
        Objects.equals(this.stato, operazionePendenza.stato) &&
        Objects.equals(this.descrizioneStato, operazionePendenza.descrizioneStato) &&
        Objects.equals(this.enteCreditore, operazionePendenza.enteCreditore) &&
        Objects.equals(this.soggettoPagatore, operazionePendenza.soggettoPagatore) &&
        Objects.equals(this.applicazione, operazionePendenza.applicazione) &&
        Objects.equals(this.identificativoPendenza, operazionePendenza.identificativoPendenza) &&
        Objects.equals(this.numeroAvviso, operazionePendenza.numeroAvviso) &&
        Objects.equals(this.numero, operazionePendenza.numero) &&
        Objects.equals(this.richiesta, operazionePendenza.richiesta) &&
        Objects.equals(this.risposta, operazionePendenza.risposta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tipoOperazione, this.stato, this.descrizioneStato, this.enteCreditore, this.soggettoPagatore, this.applicazione, this.identificativoPendenza, this.numeroAvviso, this.numero, this.richiesta, this.risposta);
  }

  public static OperazionePendenza parse(String json) throws ServiceException, ValidationException {
    return parse(json, OperazionePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operazionePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperazionePendenza {\n");
    
    sb.append("    tipoOperazione: ").append(this.toIndentedString(this.tipoOperazione)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    descrizioneStato: ").append(this.toIndentedString(this.descrizioneStato)).append("\n");
    sb.append("    enteCreditore: ").append(this.toIndentedString(this.enteCreditore)).append("\n");
    sb.append("    soggettoPagatore: ").append(this.toIndentedString(this.soggettoPagatore)).append("\n");
    sb.append("    applicazione: ").append(this.toIndentedString(this.applicazione)).append("\n");
    sb.append("    identificativoPendenza: ").append(this.toIndentedString(this.identificativoPendenza)).append("\n");
    sb.append("    numeroAvviso: ").append(this.toIndentedString(this.numeroAvviso)).append("\n");
    sb.append("    numero: ").append(this.toIndentedString(this.numero)).append("\n");
    sb.append("    richiesta: ").append(this.toIndentedString(this.richiesta)).append("\n");
    sb.append("    risposta: ").append(this.toIndentedString(this.risposta)).append("\n");
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



