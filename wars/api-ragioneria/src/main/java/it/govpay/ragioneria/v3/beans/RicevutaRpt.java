package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Richiesta di pagamento originale inviata a pagoPA
 **/
@Schema(description="Richiesta di pagamento originale inviata a pagoPA")
public class RicevutaRpt   {
  public enum TipoEnum {
    CTRICHIESTAPAGAMENTOTELEMATICO("ctRichiestaPagamentoTelematico"),
    CTPAYMENTPA("ctPaymentPA");

    private String value;

    TipoEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static TipoEnum fromValue(String text) {
      for (TipoEnum b : TipoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }  
  @Schema(required = true, description = "Tipo XML della richiesta")
 /**
   * Tipo XML della richiesta  
  **/
  private TipoEnum tipo = null;
  
  @Schema(required = true, description = "Richiesta in formato XML originale codificata in base64")
 /**
   * Richiesta in formato XML originale codificata in base64  
  **/
  private byte[] xml = null;
  
  @Schema(required = true, description = "Richiesta in formato JSON")
 /**
   * Richiesta in formato JSON  
  **/
  private Object json = null;
 /**
   * Tipo XML della richiesta
   * @return tipo
  **/
  @JsonProperty("tipo")
  @NotNull
  public String getTipo() {
    if (tipo == null) {
      return null;
    }
    return tipo.getValue();
  }

  public void setTipo(TipoEnum tipo) {
    this.tipo = tipo;
  }

  public RicevutaRpt tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * Richiesta in formato XML originale codificata in base64
   * @return xml
  **/
  @JsonProperty("xml")
  @NotNull
  public byte[] getXml() {
    return xml;
  }

  public void setXml(byte[] xml) {
    this.xml = xml;
  }

  public RicevutaRpt xml(byte[] xml) {
    this.xml = xml;
    return this;
  }

 /**
   * Richiesta in formato JSON
   * @return json
  **/
  @JsonProperty("json")
  @NotNull
  public Object getJson() {
    return json;
  }

  public void setJson(Object json) {
    this.json = json;
  }

  public RicevutaRpt json(Object json) {
    this.json = json;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RicevutaRpt {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    xml: ").append(toIndentedString(xml)).append("\n");
    sb.append("    json: ").append(toIndentedString(json)).append("\n");
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
