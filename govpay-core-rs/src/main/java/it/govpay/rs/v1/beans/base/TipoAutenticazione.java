package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import it.govpay.rs.v1.beans.base.TipoAutenticazioneBasic;
import it.govpay.rs.v1.beans.base.TipoAutenticazioneSSL;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"username",
"password",
"tipo",
"ksLocation",
"ksPassword",
"tsLocation",
"tsPassword",
})
public class TipoAutenticazione extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("username")
  private String username = null;
  
  @JsonProperty("password")
  private String password = null;
  
    
  /**
   * Gets or Sets tipo
   */
  public enum TipoEnum {
    
    
        
            
    CLIENT("Client"),
    
            
    SERVER("Server");
            
        
    

    private String value;

    TipoEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoEnum fromValue(String text) {
      for (TipoEnum b : TipoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("tipo")
  private TipoEnum tipo = null;
  
  @JsonProperty("ksLocation")
  private String ksLocation = null;
  
  @JsonProperty("ksPassword")
  private String ksPassword = null;
  
  @JsonProperty("tsLocation")
  private String tsLocation = null;
  
  @JsonProperty("tsPassword")
  private String tsPassword = null;
  
  /**
   **/
  public TipoAutenticazione username(String username) {
    this.username = username;
    return this;
  }

  @JsonProperty("username")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   **/
  public TipoAutenticazione password(String password) {
    this.password = password;
    return this;
  }

  @JsonProperty("password")
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   **/
  public TipoAutenticazione tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoEnum getTipo() {
    return tipo;
  }
  public void setTipo(TipoEnum tipo) {
    this.tipo = tipo;
  }

  /**
   * Location del keystore
   **/
  public TipoAutenticazione ksLocation(String ksLocation) {
    this.ksLocation = ksLocation;
    return this;
  }

  @JsonProperty("ksLocation")
  public String getKsLocation() {
    return ksLocation;
  }
  public void setKsLocation(String ksLocation) {
    this.ksLocation = ksLocation;
  }

  /**
   * Password del keystore
   **/
  public TipoAutenticazione ksPassword(String ksPassword) {
    this.ksPassword = ksPassword;
    return this;
  }

  @JsonProperty("ksPassword")
  public String getKsPassword() {
    return ksPassword;
  }
  public void setKsPassword(String ksPassword) {
    this.ksPassword = ksPassword;
  }

  /**
   * Location del truststore
   **/
  public TipoAutenticazione tsLocation(String tsLocation) {
    this.tsLocation = tsLocation;
    return this;
  }

  @JsonProperty("tsLocation")
  public String getTsLocation() {
    return tsLocation;
  }
  public void setTsLocation(String tsLocation) {
    this.tsLocation = tsLocation;
  }

  /**
   * Password del truststore
   **/
  public TipoAutenticazione tsPassword(String tsPassword) {
    this.tsPassword = tsPassword;
    return this;
  }

  @JsonProperty("tsPassword")
  public String getTsPassword() {
    return tsPassword;
  }
  public void setTsPassword(String tsPassword) {
    this.tsPassword = tsPassword;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoAutenticazione tipoAutenticazione = (TipoAutenticazione) o;
    return Objects.equals(username, tipoAutenticazione.username) &&
        Objects.equals(password, tipoAutenticazione.password) &&
        Objects.equals(tipo, tipoAutenticazione.tipo) &&
        Objects.equals(ksLocation, tipoAutenticazione.ksLocation) &&
        Objects.equals(ksPassword, tipoAutenticazione.ksPassword) &&
        Objects.equals(tsLocation, tipoAutenticazione.tsLocation) &&
        Objects.equals(tsPassword, tipoAutenticazione.tsPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, tipo, ksLocation, ksPassword, tsLocation, tsPassword);
  }

  public static TipoAutenticazione parse(String json) {
    return (TipoAutenticazione) parse(json, TipoAutenticazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazione {\n");
    
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    ksLocation: ").append(toIndentedString(ksLocation)).append("\n");
    sb.append("    ksPassword: ").append(toIndentedString(ksPassword)).append("\n");
    sb.append("    tsLocation: ").append(toIndentedString(tsLocation)).append("\n");
    sb.append("    tsPassword: ").append(toIndentedString(tsPassword)).append("\n");
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



