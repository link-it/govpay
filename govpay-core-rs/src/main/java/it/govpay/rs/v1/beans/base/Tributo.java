package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Definisce i dettagli di incasso.
 **/@org.codehaus.jackson.annotate.JsonPropertyOrder({
"ibanAccredito",
"bicAccredito",
"ibanAppoggio",
"bicAppoggio",
"tipoContabilita",
"codiceContabilita",
})
public class Tributo extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("bicAccredito")
  private String bicAccredito = null;
  
  @JsonProperty("ibanAppoggio")
  private String ibanAppoggio = null;
  
  @JsonProperty("bicAppoggio")
  private String bicAppoggio = null;
  
    
  /**
   * Tipologia di codifica del capitolo di bilancio
   */
  public enum TipoContabilitaEnum {
    
    
        
            
    ENTRATA("Entrata"),
    
            
    SPECIALE("Speciale"),
    
            
    SIOPE("SIOPE"),
    
            
    ALTRO("Altro");
            
        
    

    private String value;

    TipoContabilitaEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoContabilitaEnum fromValue(String text) {
      for (TipoContabilitaEnum b : TipoContabilitaEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("tipoContabilita")
  private TipoContabilitaEnum tipoContabilita = null;
  
  @JsonProperty("codiceContabilita")
  private String codiceContabilita = null;
  
  /**
   **/
  public Tributo ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  /**
   **/
  public Tributo bicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
    return this;
  }

  @JsonProperty("bicAccredito")
  public String getBicAccredito() {
    return bicAccredito;
  }
  public void setBicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
  }

  /**
   **/
  public Tributo ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

  @JsonProperty("ibanAppoggio")
  public String getIbanAppoggio() {
    return ibanAppoggio;
  }
  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  /**
   **/
  public Tributo bicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
    return this;
  }

  @JsonProperty("bicAppoggio")
  public String getBicAppoggio() {
    return bicAppoggio;
  }
  public void setBicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
  }

  /**
   * Tipologia di codifica del capitolo di bilancio
   **/
  public Tributo tipoContabilita(TipoContabilitaEnum tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonProperty("tipoContabilita")
  public TipoContabilitaEnum getTipoContabilita() {
    return tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilitaEnum tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  /**
   * Codifica del capitolo di bilancio
   **/
  public Tributo codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return codiceContabilita;
  }
  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tributo tributo = (Tributo) o;
    return Objects.equals(ibanAccredito, tributo.ibanAccredito) &&
        Objects.equals(bicAccredito, tributo.bicAccredito) &&
        Objects.equals(ibanAppoggio, tributo.ibanAppoggio) &&
        Objects.equals(bicAppoggio, tributo.bicAppoggio) &&
        Objects.equals(tipoContabilita, tributo.tipoContabilita) &&
        Objects.equals(codiceContabilita, tributo.codiceContabilita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ibanAccredito, bicAccredito, ibanAppoggio, bicAppoggio, tipoContabilita, codiceContabilita);
  }

  public static Tributo parse(String json) {
    return (Tributo) parse(json, Tributo.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tributo";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tributo {\n");
    
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    bicAccredito: ").append(toIndentedString(bicAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    bicAppoggio: ").append(toIndentedString(bicAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
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



