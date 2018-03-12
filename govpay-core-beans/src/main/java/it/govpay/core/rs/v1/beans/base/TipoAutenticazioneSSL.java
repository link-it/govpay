package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"tipo",
"ksLocation",
"ksPassword",
"tsLocation",
"tsPassword",
})
public class TipoAutenticazioneSSL extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
    
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
  public TipoAutenticazioneSSL tipo(TipoEnum tipo) {
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
  public TipoAutenticazioneSSL ksLocation(String ksLocation) {
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
  public TipoAutenticazioneSSL ksPassword(String ksPassword) {
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
  public TipoAutenticazioneSSL tsLocation(String tsLocation) {
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
  public TipoAutenticazioneSSL tsPassword(String tsPassword) {
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
    TipoAutenticazioneSSL tipoAutenticazioneSSL = (TipoAutenticazioneSSL) o;
    return Objects.equals(tipo, tipoAutenticazioneSSL.tipo) &&
        Objects.equals(ksLocation, tipoAutenticazioneSSL.ksLocation) &&
        Objects.equals(ksPassword, tipoAutenticazioneSSL.ksPassword) &&
        Objects.equals(tsLocation, tipoAutenticazioneSSL.tsLocation) &&
        Objects.equals(tsPassword, tipoAutenticazioneSSL.tsPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, ksLocation, ksPassword, tsLocation, tsPassword);
  }

  public static TipoAutenticazioneSSL parse(String json) {
    return (TipoAutenticazioneSSL) parse(json, TipoAutenticazioneSSL.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneSSL";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneSSL {\n");
    
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



