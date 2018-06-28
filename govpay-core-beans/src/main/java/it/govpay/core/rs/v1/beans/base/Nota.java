package it.govpay.core.rs.v1.beans.base;

import java.util.Date;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"autore",
"data",
"testo",
})
public class Nota extends JSONSerializable {
  
  @JsonProperty("autore")
  private String autore = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("testo")
  private String testo = null;
  
  /**
   * Username dell'operatore che ha inserito la nota
   **/
  public Nota autore(String autore) {
    this.autore = autore;
    return this;
  }

  @JsonProperty("autore")
  public String getAutore() {
    return autore;
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
    return data;
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
    return testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Nota nota = (Nota) o;
    return Objects.equals(autore, nota.autore) &&
        Objects.equals(data, nota.data) &&
        Objects.equals(testo, nota.testo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autore, data, testo);
  }

  public static Nota parse(String json) {
    return (Nota) parse(json, Nota.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nota";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Nota {\n");
    
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
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



