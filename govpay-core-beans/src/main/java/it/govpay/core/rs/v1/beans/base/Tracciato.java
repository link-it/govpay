package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@JsonPropertyOrder({
"id",
"codDominio",
"tipo",
"stato",
"descrizioneStato",
"dataCaricamento",
"dataCompletamento",
"beanDati",
"filenameRichiesta",
"filenameEsito",
})
public class Tracciato extends JSONSerializable {
  
  @JsonProperty("id")
  private BigDecimal id = null;
  
  @JsonProperty("codDominio")
  private String codDominio = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("stato")
  private String stato = null;
  
  @JsonProperty("descrizioneStato")
  private String descrizioneStato = null;
  
  @JsonProperty("dataCaricamento")
  private Date dataCaricamento = null;
  
  @JsonProperty("dataCompletamento")
  private Date dataCompletamento = null;
  
  @JsonProperty("beanDati")
  @JsonRawValue
  private Object beanDati = null;
  
  @JsonProperty("filenameRichiesta")
  private String filenameRichiesta = null;
  
  @JsonProperty("filenameEsito")
  private String filenameEsito = null;
  
  /**
   * Identificativo numerico del tracciato
   **/
  public Tracciato id(BigDecimal id) {
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
   * Dominio creatore al tracciato
   **/
  public Tracciato codDominio(String codDominio) {
    this.codDominio = codDominio;
    return this;
  }

  @JsonProperty("codDominio")
  public String getCodDominio() {
    return codDominio;
  }
  public void setCodDominio(String codDominio) {
    this.codDominio = codDominio;
  }

  /**
   * Tipo di tracciato
   **/
  public Tracciato tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   * Stato del tracciato
   **/
  public Tracciato stato(String stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   * Descrizione dello stato del tracciato
   **/
  public Tracciato descrizioneStato(String descrizioneStato) {
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
   * Data di caricamento del tracciato
   **/
  public Tracciato dataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
    return this;
  }

  @JsonProperty("dataCaricamento")
  public Date getDataCaricamento() {
    return dataCaricamento;
  }
  public void setDataCaricamento(Date dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  /**
   * Data di completamento del caricamento del tracciato
   **/
  public Tracciato dataCompletamento(Date dataCompletamento) {
    this.dataCompletamento = dataCompletamento;
    return this;
  }

  @JsonProperty("dataCompletamento")
  public Date getDataCompletamento() {
    return dataCompletamento;
  }
  public void setDataCompletamento(Date dataCompletamento) {
    this.dataCompletamento = dataCompletamento;
  }

  /**
   * Dati applicativi del tracciato
   **/
  public Tracciato beanDati(Object beanDati) {
    this.beanDati = beanDati;
    return this;
  }

  @JsonProperty("beanDati")
  @JsonRawValue
  public Object getBeanDati() {
    return beanDati;
  }
  public void setBeanDati(Object beanDati) {
    this.beanDati = beanDati;
  }

  /**
   * Nome del file di richiesta del tracciato
   **/
  public Tracciato filenameRichiesta(String filenameRichiesta) {
    this.filenameRichiesta = filenameRichiesta;
    return this;
  }

  @JsonProperty("filenameRichiesta")
  public String getFilenameRichiesta() {
    return filenameRichiesta;
  }
  public void setFilenameRichiesta(String filenameRichiesta) {
    this.filenameRichiesta = filenameRichiesta;
  }

  /**
   * Nome del file di esito del tracciato
   **/
  public Tracciato filenameEsito(String filenameEsito) {
    this.filenameEsito = filenameEsito;
    return this;
  }

  @JsonProperty("filenameEsito")
  public String getFilenameEsito() {
    return filenameEsito;
  }
  public void setFilenameEsito(String filenameEsito) {
    this.filenameEsito = filenameEsito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tracciato tracciato = (Tracciato) o;
    return Objects.equals(id, tracciato.id) &&
        Objects.equals(codDominio, tracciato.codDominio) &&
        Objects.equals(tipo, tracciato.tipo) &&
        Objects.equals(stato, tracciato.stato) &&
        Objects.equals(descrizioneStato, tracciato.descrizioneStato) &&
        Objects.equals(dataCaricamento, tracciato.dataCaricamento) &&
        Objects.equals(dataCompletamento, tracciato.dataCompletamento) &&
        Objects.equals(beanDati, tracciato.beanDati) &&
        Objects.equals(filenameRichiesta, tracciato.filenameRichiesta) &&
        Objects.equals(filenameEsito, tracciato.filenameEsito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codDominio, tipo, stato, descrizioneStato, dataCaricamento, dataCompletamento, beanDati, filenameRichiesta, filenameEsito);
  }

  public static Tracciato parse(String json) throws ServiceException {
    return (Tracciato) parse(json, Tracciato.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciato";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tracciato {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codDominio: ").append(toIndentedString(codDominio)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    dataCaricamento: ").append(toIndentedString(dataCaricamento)).append("\n");
    sb.append("    dataCompletamento: ").append(toIndentedString(dataCompletamento)).append("\n");
    sb.append("    beanDati: ").append(toIndentedString(beanDati)).append("\n");
    sb.append("    filenameRichiesta: ").append(toIndentedString(filenameRichiesta)).append("\n");
    sb.append("    filenameEsito: ").append(toIndentedString(filenameEsito)).append("\n");
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



