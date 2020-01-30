package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"dataCreazione",
"stato",
"descrizioneStato",
"dataUltimoAggiornamento",
"dataProssimaSpedizione",
"numeroTentativi",
"idA2A",
})
public class NotificaIndex extends JSONSerializable {
  
  @JsonProperty("tipo")
  private TipoNotifica tipo = null;
  
  @JsonProperty("dataCreazione")
  private Date dataCreazione = null;
  
  @JsonProperty("stato")
  private StatoNotifica stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
  @JsonProperty("dataUltimoAggiornamento")
  private Date dataUltimoAggiornamento = null;
  
  @JsonProperty("dataProssimaSpedizione")
  private Date dataProssimaSpedizione = null;
  
  @JsonProperty("numeroTentativi")
  private BigDecimal numeroTentativi = null;
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  /**
   **/
  public NotificaIndex tipo(TipoNotifica tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoNotifica getTipo() {
    return tipo;
  }
  public void setTipo(TipoNotifica tipo) {
    this.tipo = tipo;
  }

  /**
   * Data di creazione della notifica
   **/
  public NotificaIndex dataCreazione(Date dataCreazione) {
    this.dataCreazione = dataCreazione;
    return this;
  }

  @JsonProperty("dataCreazione")
  public Date getDataCreazione() {
    return dataCreazione;
  }
  public void setDataCreazione(Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  /**
   **/
  public NotificaIndex stato(StatoNotifica stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoNotifica getStato() {
    return stato;
  }
  public void setStato(StatoNotifica stato) {
    this.stato = stato;
  }

  /**
   * Descrizione estesa dello stato
   **/
  public NotificaIndex descrizioneStato(String descrizioneStato) {
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
   * Data ultimo aggiornamento della notifica
   **/
  public NotificaIndex dataUltimoAggiornamento(Date dataUltimoAggiornamento) {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
    return this;
  }

  @JsonProperty("dataUltimoAggiornamento")
  public Date getDataUltimoAggiornamento() {
    return dataUltimoAggiornamento;
  }
  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  /**
   * Data di prossima spedizione della notifica
   **/
  public NotificaIndex dataProssimaSpedizione(Date dataProssimaSpedizione) {
    this.dataProssimaSpedizione = dataProssimaSpedizione;
    return this;
  }

  @JsonProperty("dataProssimaSpedizione")
  public Date getDataProssimaSpedizione() {
    return dataProssimaSpedizione;
  }
  public void setDataProssimaSpedizione(Date dataProssimaSpedizione) {
    this.dataProssimaSpedizione = dataProssimaSpedizione;
  }

  /**
   * Numero di tentativi di spedizione della notifica
   **/
  public NotificaIndex numeroTentativi(BigDecimal numeroTentativi) {
    this.numeroTentativi = numeroTentativi;
    return this;
  }

  @JsonProperty("numeroTentativi")
  public BigDecimal getNumeroTentativi() {
    return numeroTentativi;
  }
  public void setNumeroTentativi(BigDecimal numeroTentativi) {
    this.numeroTentativi = numeroTentativi;
  }

  /**
   * Identificativo applicazione
   **/
  public NotificaIndex idA2A(String idA2A) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificaIndex notificaIndex = (NotificaIndex) o;
    return Objects.equals(tipo, notificaIndex.tipo) &&
        Objects.equals(dataCreazione, notificaIndex.dataCreazione) &&
        Objects.equals(stato, notificaIndex.stato) &&
        Objects.equals(descrizioneStato, notificaIndex.descrizioneStato) &&
        Objects.equals(dataUltimoAggiornamento, notificaIndex.dataUltimoAggiornamento) &&
        Objects.equals(dataProssimaSpedizione, notificaIndex.dataProssimaSpedizione) &&
        Objects.equals(numeroTentativi, notificaIndex.numeroTentativi) &&
        Objects.equals(idA2A, notificaIndex.idA2A);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, dataCreazione, stato, descrizioneStato, dataUltimoAggiornamento, dataProssimaSpedizione, numeroTentativi, idA2A);
  }

  public static NotificaIndex parse(String json) throws ServiceException, ValidationException {
    return parse(json, NotificaIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "notificaIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificaIndex {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    dataUltimoAggiornamento: ").append(toIndentedString(dataUltimoAggiornamento)).append("\n");
    sb.append("    dataProssimaSpedizione: ").append(toIndentedString(dataProssimaSpedizione)).append("\n");
    sb.append("    numeroTentativi: ").append(toIndentedString(numeroTentativi)).append("\n");
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
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



