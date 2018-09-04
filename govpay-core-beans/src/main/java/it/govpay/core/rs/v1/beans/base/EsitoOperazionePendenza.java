package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"idPendenza",
"tipoOperazione",
"stato",
"esito",
"descrizioneEsito",
"numero",
"dati",
})
public class EsitoOperazionePendenza extends JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("tipoOperazione")
  private TipoOperazionePendenza tipoOperazione = null;
  
  @JsonProperty("stato")
  private StatoOperazionePendenza stato = null;
  
  @JsonProperty("esito")
  private String esito = null;
  
  @JsonProperty("descrizioneEsito")
  private String descrizioneEsito = null;
  
  @JsonProperty("numero")
  private BigDecimal numero = null;
  
  @JsonProperty("dati")
  private Object dati = null;
  
  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public EsitoOperazionePendenza idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public EsitoOperazionePendenza idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   **/
  public EsitoOperazionePendenza tipoOperazione(TipoOperazionePendenza tipoOperazione) {
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
  public EsitoOperazionePendenza stato(StatoOperazionePendenza stato) {
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
   * Esito Operazione
   **/
  public EsitoOperazionePendenza esito(String esito) {
    this.esito = esito;
    return this;
  }

  @JsonProperty("esito")
  public String getEsito() {
    return esito;
  }
  public void setEsito(String esito) {
    this.esito = esito;
  }

  /**
   * Descrizione Esito Operazione
   **/
  public EsitoOperazionePendenza descrizioneEsito(String descrizioneEsito) {
    this.descrizioneEsito = descrizioneEsito;
    return this;
  }

  @JsonProperty("descrizioneEsito")
  public String getDescrizioneEsito() {
    return descrizioneEsito;
  }
  public void setDescrizioneEsito(String descrizioneEsito) {
    this.descrizioneEsito = descrizioneEsito;
  }

  /**
   * Progressivo Operazione
   **/
  public EsitoOperazionePendenza numero(BigDecimal numero) {
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
  public EsitoOperazionePendenza dati(Object dati) {
    this.dati = dati;
    return this;
  }

  @JsonProperty("dati")
  public Object getDati() {
    return dati;
  }
  public void setDati(Object dati) {
    this.dati = dati;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoOperazionePendenza esitoOperazionePendenza = (EsitoOperazionePendenza) o;
    return Objects.equals(idA2A, esitoOperazionePendenza.idA2A) &&
        Objects.equals(idPendenza, esitoOperazionePendenza.idPendenza) &&
        Objects.equals(tipoOperazione, esitoOperazionePendenza.tipoOperazione) &&
        Objects.equals(stato, esitoOperazionePendenza.stato) &&
        Objects.equals(esito, esitoOperazionePendenza.esito) &&
        Objects.equals(descrizioneEsito, esitoOperazionePendenza.descrizioneEsito) &&
        Objects.equals(numero, esitoOperazionePendenza.numero) &&
        Objects.equals(dati, esitoOperazionePendenza.dati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, idPendenza, tipoOperazione, stato, esito, descrizioneEsito, numero, dati);
  }

  public static EsitoOperazionePendenza parse(String json) throws ServiceException, ValidationException { 
    return (EsitoOperazionePendenza) parse(json, EsitoOperazionePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "esitoOperazionePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoOperazionePendenza {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    tipoOperazione: ").append(toIndentedString(tipoOperazione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    descrizioneEsito: ").append(toIndentedString(descrizioneEsito)).append("\n");
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    dati: ").append(toIndentedString(dati)).append("\n");
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



