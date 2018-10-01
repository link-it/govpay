package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
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
      return String.valueOf(this.value);
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
    return this.tipo;
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
    return this.ksLocation;
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
    return this.ksPassword;
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
    return this.tsLocation;
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
    return this.tsPassword;
  }
  public void setTsPassword(String tsPassword) {
    this.tsPassword = tsPassword;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TipoAutenticazioneSSL tipoAutenticazioneSSL = (TipoAutenticazioneSSL) o;
    return Objects.equals(this.tipo, tipoAutenticazioneSSL.tipo) &&
        Objects.equals(this.ksLocation, tipoAutenticazioneSSL.ksLocation) &&
        Objects.equals(this.ksPassword, tipoAutenticazioneSSL.ksPassword) &&
        Objects.equals(this.tsLocation, tipoAutenticazioneSSL.tsLocation) &&
        Objects.equals(this.tsPassword, tipoAutenticazioneSSL.tsPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tipo, this.ksLocation, this.ksPassword, this.tsLocation, this.tsPassword);
  }

  public static TipoAutenticazioneSSL parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, TipoAutenticazioneSSL.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneSSL";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneSSL {\n");
    
    sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
    sb.append("    ksLocation: ").append(this.toIndentedString(this.ksLocation)).append("\n");
    sb.append("    ksPassword: ").append(this.toIndentedString(this.ksPassword)).append("\n");
    sb.append("    tsLocation: ").append(this.toIndentedString(this.tsLocation)).append("\n");
    sb.append("    tsPassword: ").append(this.toIndentedString(this.tsPassword)).append("\n");
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



