package it.govpay.pagamento.test;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Definisce i dati di un bollo telematico
 **/
@Schema(description="Definisce i dati di un bollo telematico")
public class Bollo  {
@XmlType(name="TipoBolloEnum")
@XmlEnum(String.class)
public enum TipoBolloEnum {

@XmlEnumValue("Imposta di bollo") BOLLO(String.valueOf("Imposta di bollo"));


    private String value;

    TipoBolloEnum (String v) {
        value = v;
    }

    @com.fasterxml.jackson.annotation.JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    public static TipoBolloEnum fromValue(String v) {
        for (TipoBolloEnum b : TipoBolloEnum.values()) {
            if (String.valueOf(b.value).equals(v)) {
                return b;
            }
        }
        return null;
    }
}
  
  @Schema(required = true, description = "Tipologia di Bollo digitale")
 /**
   * Tipologia di Bollo digitale  
  **/
  private TipoBolloEnum tipoBollo = null;
  
  @Schema(required = true, description = "Digest in base64 del documento informatico associato alla marca da bollo")
 /**
   * Digest in base64 del documento informatico associato alla marca da bollo  
  **/
  private String hashDocumento = null;
  
  @Schema(required = true, description = "Sigla automobilistica della provincia di residenza del soggetto pagatore")
 /**
   * Sigla automobilistica della provincia di residenza del soggetto pagatore  
  **/
  private String provinciaResidenza = null;
 /**
   * Tipologia di Bollo digitale
   * @return tipoBollo
  **/
  @JsonProperty("tipoBollo")
  @NotNull
  public String getTipoBollo() {
    if (tipoBollo == null) {
      return null;
    }
    return tipoBollo.value();
  }

  public void setTipoBollo(TipoBolloEnum tipoBollo) {
    this.tipoBollo = tipoBollo;
  }

  public Bollo tipoBollo(TipoBolloEnum tipoBollo) {
    this.tipoBollo = tipoBollo;
    return this;
  }

 /**
   * Digest in base64 del documento informatico associato alla marca da bollo
   * @return hashDocumento
  **/
  @JsonProperty("hashDocumento")
  @NotNull
  public String getHashDocumento() {
    return hashDocumento;
  }

  public void setHashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
  }

  public Bollo hashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
    return this;
  }

 /**
   * Sigla automobilistica della provincia di residenza del soggetto pagatore
   * @return provinciaResidenza
  **/
  @JsonProperty("provinciaResidenza")
  @NotNull
  public String getProvinciaResidenza() {
    return provinciaResidenza;
  }

  public void setProvinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
  }

  public Bollo provinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Bollo {\n");
    
    sb.append("    tipoBollo: ").append(toIndentedString(tipoBollo)).append("\n");
    sb.append("    hashDocumento: ").append(toIndentedString(hashDocumento)).append("\n");
    sb.append("    provinciaResidenza: ").append(toIndentedString(provinciaResidenza)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
