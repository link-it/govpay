package it.govpay.ec.v2.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Ricevuta di pagamento originale emessa da pagoPA
 **/
@Schema(description="Ricevuta di pagamento originale emessa da pagoPA")
public class RicevutaRt   {
  public enum TipoEnum {
    CTRICEVUTATELEMATICA("ctRicevutaTelematica"),
    CTRECEIPT("ctReceipt");

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
  @Schema(description = "Tipo XML della ricevuta")
 /**
   * Tipo XML della ricevuta  
  **/
  private TipoEnum tipo = null;
  
  @Schema(description = "ricevuta in formato XML originale codificata in base64")
 /**
   * ricevuta in formato XML originale codificata in base64  
  **/
  private byte[] xml = null;
  
  @Schema(description = "ricevuta in formato JSON")
 /**
   * ricevuta in formato JSON  
  **/
  private Object json = null;
 /**
   * Tipo XML della ricevuta
   * @return tipo
  **/
  @JsonProperty("tipo")
  public String getTipo() {
    if (tipo == null) {
      return null;
    }
    return tipo.getValue();
  }

  public void setTipo(TipoEnum tipo) {
    this.tipo = tipo;
  }

  public RicevutaRt tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * ricevuta in formato XML originale codificata in base64
   * @return xml
  **/
  @JsonProperty("xml")
  public byte[] getXml() {
    return xml;
  }

  public void setXml(byte[] xml) {
    this.xml = xml;
  }

  public RicevutaRt xml(byte[] xml) {
    this.xml = xml;
    return this;
  }

 /**
   * ricevuta in formato JSON
   * @return json
  **/
  @JsonProperty("json")
  public Object getJson() {
    return json;
  }

  public void setJson(Object json) {
    this.json = json;
  }

  public RicevutaRt json(Object json) {
    this.json = json;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RicevutaRt {\n");
    
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
