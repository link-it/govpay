package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

import it.govpay.core.rs.v1.beans.JSONSerializable;

@org.codehaus.jackson.annotate.JsonPropertyOrder({
"url",
"versioneApi",
"auth",
})
public class Connector extends JSONSerializable {
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("versioneApi")
  private String versioneApi = null;
  
  @JsonProperty("auth")
  private TipoAutenticazione auth = null;
  
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
   * Versione delle API di integrazione utilizzate. Elenco disponibile in /enumerazioni/versioneConnettore
   **/
  public Connector versioneApi(String versioneApi) {
    this.versioneApi = versioneApi;
    return this;
  }

  @JsonProperty("versioneApi")
  public String getVersioneApi() {
    return versioneApi;
  }
  public void setVersioneApi(String versioneApi) {
    this.versioneApi = versioneApi;
  }

  /**
   **/
  public Connector auth(TipoAutenticazione auth) {
    this.auth = auth;
    return this;
  }

  @JsonProperty("auth")
  public TipoAutenticazione getAuth() {
    return auth;
  }
  public void setAuth(TipoAutenticazione auth) {
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
        Objects.equals(versioneApi, connector.versioneApi) &&
        Objects.equals(auth, connector.auth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, versioneApi, auth);
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
    sb.append("    versioneApi: ").append(toIndentedString(versioneApi)).append("\n");
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



