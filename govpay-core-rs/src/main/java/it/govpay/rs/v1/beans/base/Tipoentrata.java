package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import it.govpay.rs.v1.beans.base.TipoentrataPost;
import java.math.BigDecimal;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"descrizione",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"idEntrata",
})
public class Tipoentrata extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
    
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
  
  @JsonProperty("idEntrata")
  private String idEntrata = null;
  
  /**
   **/
  public Tipoentrata descrizione(String descrizione) {
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
   * Tipologia di codifica del capitolo di bilancio
   **/
  public Tipoentrata tipoContabilita(TipoContabilitaEnum tipoContabilita) {
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
  public Tipoentrata codiceContabilita(String codiceContabilita) {
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
  public Tipoentrata codificaIUV(BigDecimal codificaIUV) {
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
  public Tipoentrata idEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
    return this;
  }

  @JsonProperty("idEntrata")
  public String getIdEntrata() {
    return idEntrata;
  }
  public void setIdEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tipoentrata tipoentrata = (Tipoentrata) o;
    return Objects.equals(descrizione, tipoentrata.descrizione) &&
        Objects.equals(tipoContabilita, tipoentrata.tipoContabilita) &&
        Objects.equals(codiceContabilita, tipoentrata.codiceContabilita) &&
        Objects.equals(codificaIUV, tipoentrata.codificaIUV) &&
        Objects.equals(idEntrata, tipoentrata.idEntrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, tipoContabilita, codiceContabilita, codificaIUV, idEntrata);
  }

  public static Tipoentrata parse(String json) {
    return (Tipoentrata) parse(json, Tipoentrata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoentrata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tipoentrata {\n");
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    idEntrata: ").append(toIndentedString(idEntrata)).append("\n");
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



