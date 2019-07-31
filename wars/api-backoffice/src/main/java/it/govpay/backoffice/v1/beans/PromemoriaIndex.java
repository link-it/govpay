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
})
public class PromemoriaIndex extends JSONSerializable {
  
  @JsonProperty("tipo")
  private TipoPromemoria tipo = null;
  
  @JsonProperty("dataCreazione")
  private Date dataCreazione = null;
  
  @JsonProperty("stato")
  private StatoPromemoria stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
  @JsonProperty("dataUltimoAggiornamento")
  private Date dataUltimoAggiornamento = null;
  
  @JsonProperty("dataProssimaSpedizione")
  private Date dataProssimaSpedizione = null;
  
  @JsonProperty("numeroTentativi")
  private BigDecimal numeroTentativi = null;
  
  /**
   **/
  public PromemoriaIndex tipo(TipoPromemoria tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoPromemoria getTipo() {
    return tipo;
  }
  public void setTipo(TipoPromemoria tipo) {
    this.tipo = tipo;
  }

  /**
   * Data di creazione del promemoria
   **/
  public PromemoriaIndex dataCreazione(Date dataCreazione) {
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
  public PromemoriaIndex stato(StatoPromemoria stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoPromemoria getStato() {
    return stato;
  }
  public void setStato(StatoPromemoria stato) {
    this.stato = stato;
  }

  /**
   * Descrizione estesa dello stato
   **/
  public PromemoriaIndex descrizioneStato(String descrizioneStato) {
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
   * Data ultimo aggiornamento del promemoria
   **/
  public PromemoriaIndex dataUltimoAggiornamento(Date dataUltimoAggiornamento) {
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
   * Data di prossima spedizione del promemoria
   **/
  public PromemoriaIndex dataProssimaSpedizione(Date dataProssimaSpedizione) {
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
   * Numero di tentativi di spedizione del promemoria
   **/
  public PromemoriaIndex numeroTentativi(BigDecimal numeroTentativi) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PromemoriaIndex promemoriaIndex = (PromemoriaIndex) o;
    return Objects.equals(tipo, promemoriaIndex.tipo) &&
        Objects.equals(dataCreazione, promemoriaIndex.dataCreazione) &&
        Objects.equals(stato, promemoriaIndex.stato) &&
        Objects.equals(descrizioneStato, promemoriaIndex.descrizioneStato) &&
        Objects.equals(dataUltimoAggiornamento, promemoriaIndex.dataUltimoAggiornamento) &&
        Objects.equals(dataProssimaSpedizione, promemoriaIndex.dataProssimaSpedizione) &&
        Objects.equals(numeroTentativi, promemoriaIndex.numeroTentativi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, dataCreazione, stato, descrizioneStato, dataUltimoAggiornamento, dataProssimaSpedizione, numeroTentativi);
  }

  public static PromemoriaIndex parse(String json) throws ServiceException, ValidationException {
    return (PromemoriaIndex) parse(json, PromemoriaIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "promemoriaIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PromemoriaIndex {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    dataUltimoAggiornamento: ").append(toIndentedString(dataUltimoAggiornamento)).append("\n");
    sb.append("    dataProssimaSpedizione: ").append(toIndentedString(dataProssimaSpedizione)).append("\n");
    sb.append("    numeroTentativi: ").append(toIndentedString(numeroTentativi)).append("\n");
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



