package it.govpay.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"descrizione",
"ibanAccredito",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"entrata",
})
public class Entrata extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
    
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
  
  @JsonProperty("codificaIUV")
  private BigDecimal codificaIUV = null;
  
  @JsonProperty("entrata")
  private String entrata = null;
  
  /**
   **/
  public Entrata descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  public Entrata ibanAccredito(String ibanAccredito) {
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
   * Tipologia di codifica del capitolo di bilancio
   **/
  public Entrata tipoContabilita(TipoContabilitaEnum tipoContabilita) {
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
  public Entrata codiceContabilita(String codiceContabilita) {
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

  /**
   * Cifra identificativa negli IUV
   **/
  public Entrata codificaIUV(BigDecimal codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public BigDecimal getCodificaIUV() {
    return codificaIUV;
  }
  public void setCodificaIUV(BigDecimal codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   **/
  public Entrata entrata(String entrata) {
    this.entrata = entrata;
    return this;
  }

  @JsonProperty("entrata")
  public String getEntrata() {
    return entrata;
  }
  public void setEntrata(String entrata) {
    this.entrata = entrata;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entrata entrata = (Entrata) o;
    return Objects.equals(descrizione, entrata.descrizione) &&
        Objects.equals(ibanAccredito, entrata.ibanAccredito) &&
        Objects.equals(tipoContabilita, entrata.tipoContabilita) &&
        Objects.equals(codiceContabilita, entrata.codiceContabilita) &&
        Objects.equals(codificaIUV, entrata.codificaIUV) &&
        Objects.equals(entrata, entrata.entrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, ibanAccredito, tipoContabilita, codiceContabilita, codificaIUV, entrata);
  }

  public static Entrata parse(String json) {
    return (Entrata) parse(json, Entrata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "entrata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Entrata {\n");
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    entrata: ").append(toIndentedString(entrata)).append("\n");
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



