package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"ksLocation",
"ksPassword",
"ksType",
"ksPKeyPassword",
"tsLocation",
"tsPassword",
"tsType",
"sslType",
})
public class TipoAutenticazioneSSL extends it.govpay.core.beans.JSONSerializable {
  
    
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
  
  @JsonProperty("ksType")
  private KeystoreType ksType = null;
  
  @JsonProperty("ksPKeyPassword")
  private String ksPKeyPassword = null;
  
  @JsonProperty("tsLocation")
  private String tsLocation = null;
  
  @JsonProperty("tsPassword")
  private String tsPassword = null;
  
  @JsonProperty("tsType")
  private KeystoreType tsType = null;
  
  private SslConfigType sslTypeEnum = null;
  
  @JsonProperty("sslType")
  private String sslType = null;
  
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
   **/
  public TipoAutenticazioneSSL ksType(KeystoreType ksType) {
    this.ksType = ksType;
    return this;
  }

  @JsonProperty("ksType")
  public KeystoreType getKsType() {
    return ksType;
  }
  public void setKsType(KeystoreType ksType) {
    this.ksType = ksType;
  }

  /**
   * Password della chiave privata del keystore
   **/
  public TipoAutenticazioneSSL ksPKeyPassword(String ksPKeyPassword) {
    this.ksPKeyPassword = ksPKeyPassword;
    return this;
  }

  @JsonProperty("ksPKeyPassword")
  public String getKsPKeyPassword() {
    return ksPKeyPassword;
  }
  public void setKsPKeyPassword(String ksPKeyPassword) {
    this.ksPKeyPassword = ksPKeyPassword;
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

  /**
   **/
  public TipoAutenticazioneSSL tsType(KeystoreType tsType) {
    this.tsType = tsType;
    return this;
  }

  @JsonProperty("tsType")
  public KeystoreType getTsType() {
    return tsType;
  }
  public void setTsType(KeystoreType tsType) {
    this.tsType = tsType;
  }

  /**
   **/
  public TipoAutenticazioneSSL sslTypeEnum(SslConfigType type) {
    this.sslTypeEnum = type;
    return this;
  }

  @JsonIgnore
  public SslConfigType getSslTypeEnum() {
    return sslTypeEnum;
  }
  public void setSslTypeEnum(SslConfigType type) {
    this.sslTypeEnum = type;
  }
  
  /**
   **/
  public TipoAutenticazioneSSL sslType(String sslType) {
    this.sslType = sslType;
    return this;
  }

  @JsonProperty("type")
  public String getSslType() {
    return sslType;
  }
  public void setSslType(String sslType) {
    this.sslType = sslType;
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
        Objects.equals(ksType, tipoAutenticazioneSSL.ksType) &&
        Objects.equals(ksPKeyPassword, tipoAutenticazioneSSL.ksPKeyPassword) &&
        Objects.equals(tsLocation, tipoAutenticazioneSSL.tsLocation) &&
        Objects.equals(tsPassword, tipoAutenticazioneSSL.tsPassword) &&
        Objects.equals(tsType, tipoAutenticazioneSSL.tsType) &&
        Objects.equals(sslType, tipoAutenticazioneSSL.sslType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, ksLocation, ksPassword, ksType, ksPKeyPassword, tsLocation, tsPassword, tsType, sslType);
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
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    ksLocation: ").append(toIndentedString(ksLocation)).append("\n");
    sb.append("    ksPassword: ").append(toIndentedString(ksPassword)).append("\n");
    sb.append("    ksType: ").append(toIndentedString(ksType)).append("\n");
    sb.append("    ksPKeyPassword: ").append(toIndentedString(ksPKeyPassword)).append("\n");
    sb.append("    tsLocation: ").append(toIndentedString(tsLocation)).append("\n");
    sb.append("    tsPassword: ").append(toIndentedString(tsPassword)).append("\n");
    sb.append("    tsType: ").append(toIndentedString(tsType)).append("\n");
    sb.append("    sslType: ").append(toIndentedString(sslType)).append("\n");
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



