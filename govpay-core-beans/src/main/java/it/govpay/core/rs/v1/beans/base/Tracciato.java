package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

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
    return this.id;
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
    return this.codDominio;
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
    return this.tipo;
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
    return this.stato;
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
    return this.descrizioneStato;
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
    return this.dataCaricamento;
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
    return this.dataCompletamento;
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
    return this.beanDati;
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
    return this.filenameRichiesta;
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
    return this.filenameEsito;
  }
  public void setFilenameEsito(String filenameEsito) {
    this.filenameEsito = filenameEsito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Tracciato tracciato = (Tracciato) o;
    return Objects.equals(this.id, tracciato.id) &&
        Objects.equals(this.codDominio, tracciato.codDominio) &&
        Objects.equals(this.tipo, tracciato.tipo) &&
        Objects.equals(this.stato, tracciato.stato) &&
        Objects.equals(this.descrizioneStato, tracciato.descrizioneStato) &&
        Objects.equals(this.dataCaricamento, tracciato.dataCaricamento) &&
        Objects.equals(this.dataCompletamento, tracciato.dataCompletamento) &&
        Objects.equals(this.beanDati, tracciato.beanDati) &&
        Objects.equals(this.filenameRichiesta, tracciato.filenameRichiesta) &&
        Objects.equals(this.filenameEsito, tracciato.filenameEsito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.codDominio, this.tipo, this.stato, this.descrizioneStato, this.dataCaricamento, this.dataCompletamento, this.beanDati, this.filenameRichiesta, this.filenameEsito);
  }

  public static Tracciato parse(String json) throws ServiceException, ValidationException {
    return parse(json, Tracciato.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciato";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tracciato {\n");
    
    sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
    sb.append("    codDominio: ").append(this.toIndentedString(this.codDominio)).append("\n");
    sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    descrizioneStato: ").append(this.toIndentedString(this.descrizioneStato)).append("\n");
    sb.append("    dataCaricamento: ").append(this.toIndentedString(this.dataCaricamento)).append("\n");
    sb.append("    dataCompletamento: ").append(this.toIndentedString(this.dataCompletamento)).append("\n");
    sb.append("    beanDati: ").append(this.toIndentedString(this.beanDati)).append("\n");
    sb.append("    filenameRichiesta: ").append(this.toIndentedString(this.filenameRichiesta)).append("\n");
    sb.append("    filenameEsito: ").append(this.toIndentedString(this.filenameEsito)).append("\n");
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



