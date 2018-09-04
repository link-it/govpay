package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
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
    return tipoOperazione;
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
    return stato;
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
    return descrizioneStato;
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
    return enteCreditore;
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
    return soggettoPagatore;
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
    return applicazione;
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
    return identificativoPendenza;
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
    return numeroAvviso;
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
    return numero;
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
    return richiesta;
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
    return risposta;
  }
  public void setRisposta(EsitoOperazionePendenza risposta) {
    this.risposta = risposta;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperazionePendenza operazionePendenza = (OperazionePendenza) o;
    return Objects.equals(tipoOperazione, operazionePendenza.tipoOperazione) &&
        Objects.equals(stato, operazionePendenza.stato) &&
        Objects.equals(descrizioneStato, operazionePendenza.descrizioneStato) &&
        Objects.equals(enteCreditore, operazionePendenza.enteCreditore) &&
        Objects.equals(soggettoPagatore, operazionePendenza.soggettoPagatore) &&
        Objects.equals(applicazione, operazionePendenza.applicazione) &&
        Objects.equals(identificativoPendenza, operazionePendenza.identificativoPendenza) &&
        Objects.equals(numeroAvviso, operazionePendenza.numeroAvviso) &&
        Objects.equals(numero, operazionePendenza.numero) &&
        Objects.equals(richiesta, operazionePendenza.richiesta) &&
        Objects.equals(risposta, operazionePendenza.risposta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipoOperazione, stato, descrizioneStato, enteCreditore, soggettoPagatore, applicazione, identificativoPendenza, numeroAvviso, numero, richiesta, risposta);
  }

  public static OperazionePendenza parse(String json) throws ServiceException, ValidationException {
    return (OperazionePendenza) parse(json, OperazionePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operazionePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperazionePendenza {\n");
    
    sb.append("    tipoOperazione: ").append(toIndentedString(tipoOperazione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    enteCreditore: ").append(toIndentedString(enteCreditore)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    applicazione: ").append(toIndentedString(applicazione)).append("\n");
    sb.append("    identificativoPendenza: ").append(toIndentedString(identificativoPendenza)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    richiesta: ").append(toIndentedString(richiesta)).append("\n");
    sb.append("    risposta: ").append(toIndentedString(risposta)).append("\n");
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



