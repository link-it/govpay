package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"url",
"versione",
"auth",
})
public class Connector extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("versione")
  private String versione = "1.0";
  
  @JsonProperty("auth")
  private Object auth = null;
  
  /**
   * Dati di integrazione ad un servizio web
   **/
  public Connector url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Versione del servizio da utilizzare
   **/
  public Connector versione(String versione) {
    this.versione = versione;
    return this;
  }

  @JsonProperty("versione")
  public String getVersione() {
    return versione;
  }
  public void setVersione(String versione) {
    this.versione = versione;
  }

  /**
   **/
  public Connector auth(Object auth) {
    this.auth = auth;
    return this;
  }

  @JsonProperty("auth")
  public Object getAuth() {
    return auth;
  }
  public void setAuth(Object auth) {
    this.auth = auth;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Connector connector = (Connector) o;
    return Objects.equals(url, connector.url) &&
        Objects.equals(versione, connector.versione) &&
        Objects.equals(auth, connector.auth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, versione, auth);
  }

  public static Connector parse(String json) {
    return (Connector) parse(json, Connector.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connector";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Connector {\n");
    
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
    sb.append("    auth: ").append(toIndentedString(auth)).append("\n");
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



