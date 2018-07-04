package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"testo",
})
public class Allegato extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
    
  /**
   * Tipologia di allegato
   */
  public enum TipoEnum {
    
    
        
            
    ESITO_PAGAMENTO("Esito pagamento"),
    
            
    MARCA_DA_BOLLO("Marca da bollo");
            
        
    

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
  
  @JsonProperty("testo")
  private String testo = null;
  
  /**
   * Tipologia di allegato
   **/
  public Allegato tipo(TipoEnum tipo) {
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
   * allegato codificato in base64
   **/
  public Allegato testo(String testo) {
    this.testo = testo;
    return this;
  }

  @JsonProperty("testo")
  public String getTesto() {
    return testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Allegato allegato = (Allegato) o;
    return Objects.equals(tipo, allegato.tipo) &&
        Objects.equals(testo, allegato.testo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, testo);
  }

  public static Allegato parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (Allegato) parse(json, Allegato.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "allegato";
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}



