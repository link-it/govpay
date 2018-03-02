package it.govpay.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"url",
"versioneApi",
"auth",
})
public class Connector extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("url")
  private String url = null;
  
    
  /**
   * Versione delle API di integrazione utilizzate
   */
  public enum VersioneApiEnum {
    
    
        
            
    REST_1_0("REST_1.0"),
    
            
    SOAP_2_0("SOAP_2.0"),
    
            
    SOAP_2_1("SOAP_2.1"),
    
            
    SOAP_2_3("SOAP_2.3"),
    
            
    SOAP_2_5("SOAP_2.5");
            
        
    

    private String value;

    VersioneApiEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static VersioneApiEnum fromValue(String text) {
      for (VersioneApiEnum b : VersioneApiEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("versioneApi")
  private VersioneApiEnum versioneApi = null;
  
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
   * Versione delle API di integrazione utilizzate
   **/
  public Connector versioneApi(VersioneApiEnum versioneApi) {
    this.versioneApi = versioneApi;
    return this;
  }

  @JsonProperty("versioneApi")
  public VersioneApiEnum getVersioneApi() {
    return versioneApi;
  }
  public void setVersioneApi(VersioneApiEnum versioneApi) {
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



