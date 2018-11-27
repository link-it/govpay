package it.govpay.backoffice.v1.beans;

import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"principal",
"autore",
"data",
"testo",
"oggetto",
"tipo",
})
public class Nota extends JSONSerializable {
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("autore")
  private String autore = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("testo")
  private String testo = null;
  
  @JsonProperty("oggetto")
  private String oggetto = null;
  
  @JsonProperty("tipo")
  private TipoNota tipo = null;
  
  /**
   * Username dell'operatore che ha inserito la nota
   **/
  public Nota principal(String principal) {
    this.principal = principal;
    return this;
  }

  @JsonProperty("principal")
  public String getPrincipal() {
    return principal;
  }
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   * Denominazione dell'operatore che ha inserito la nota
   **/
  public Nota autore(String autore) {
    this.autore = autore;
    return this;
  }

  @JsonProperty("autore")
  public String getAutore() {
    return this.autore;
  }
  public void setAutore(String autore) {
    this.autore = autore;
  }

  /**
   * Data emissione della nota
   **/
  public Nota data(Date data) {
    this.data = data;
    return this;
  }

  @JsonProperty("data")
  public Date getData() {
    return this.data;
  }
  public void setData(Date data) {
    this.data = data;
  }

  /**
   * Testo della nota
   **/
  public Nota testo(String testo) {
    this.testo = testo;
    return this;
  }

  @JsonProperty("testo")
  public String getTesto() {
    return this.testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }

  /**
   * Oggetto della nota
   **/
  public Nota oggetto(String oggetto) {
    this.oggetto = oggetto;
    return this;
  }

  @JsonProperty("oggetto")
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  /**
   **/
  public Nota tipo(TipoNota tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoNota getTipo() {
    return tipo;
  }
  public void setTipo(TipoNota tipo) {
    this.tipo = tipo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Nota nota = (Nota) o;
    return Objects.equals(principal, nota.principal) &&
        Objects.equals(autore, nota.autore) &&
        Objects.equals(data, nota.data) &&
        Objects.equals(testo, nota.testo) &&
        Objects.equals(oggetto, nota.oggetto) &&
        Objects.equals(tipo, nota.tipo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal, autore, data, testo, oggetto, tipo);
  }

  public static Nota parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Nota.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nota";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Nota {\n");
    
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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



