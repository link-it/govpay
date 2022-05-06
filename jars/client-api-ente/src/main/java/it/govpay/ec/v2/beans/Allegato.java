package it.govpay.ec.v2.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

public class Allegato   {
  public enum TipoEnum {
    ESITO_PAGAMENTO("Esito pagamento"),
    MARCA_DA_BOLLO("Marca da bollo");

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
    
    public static TipoEnum fromCodifica(String codifica) {
		switch (codifica) {
		case "BD":
			return MARCA_DA_BOLLO;
		case "ES":
			return ESITO_PAGAMENTO;

		}
		return null;
	}
  }  
  @Schema(required = true, description = "Tipologia di allegato")
 /**
   * Tipologia di allegato  
  **/
  private TipoEnum tipo = null;
  
  @Schema(required = true, description = "allegato codificato in base64")
 /**
   * allegato codificato in base64  
  **/
  private String testo = null;
 /**
   * Tipologia di allegato
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

  public Allegato tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

 /**
   * allegato codificato in base64
   * @return testo
  **/
  @JsonProperty("testo")
  @NotNull
  public String getTesto() {
    return testo;
  }

  public void setTesto(String testo) {
    this.testo = testo;
  }

  public Allegato testo(String testo) {
    this.testo = testo;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Allegato {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
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
