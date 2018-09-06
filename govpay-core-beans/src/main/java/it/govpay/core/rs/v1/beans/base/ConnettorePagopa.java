package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"url",
"auth",
})
public class ConnettorePagopa extends JSONSerializable {
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("auth")
  private TipoAutenticazione auth = null;
  
  /**
   * Dati di integrazione ad un servizio web
   **/
  public ConnettorePagopa url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return this.url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  public ConnettorePagopa auth(TipoAutenticazione auth) {
    this.auth = auth;
    return this;
  }

  @JsonProperty("auth")
  public TipoAutenticazione getAuth() {
    return this.auth;
  }
  public void setAuth(TipoAutenticazione auth) {
    this.auth = auth;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ConnettorePagopa connettorePagopa = (ConnettorePagopa) o;
    return Objects.equals(this.url, connettorePagopa.url) &&
        Objects.equals(this.auth, connettorePagopa.auth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.url, this.auth);
  }

  public static ConnettorePagopa parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, ConnettorePagopa.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettorePagopa";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettorePagopa {\n");
    
    sb.append("    url: ").append(this.toIndentedString(this.url)).append("\n");
    sb.append("    auth: ").append(this.toIndentedString(this.auth)).append("\n");
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



