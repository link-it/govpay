package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"descrizione",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"idEntrata",
})
public class TipoEntrata extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
    
  /**
   * Tipologia di codifica del capitolo di bilancio
   */
  public enum TipoContabilitaEnum {
    
    
        
            
    ENTRATA("ENTRATA"),
    
            
    SPECIALE("SPECIALE"),
    
            
    SIOPE("SIOPE"),
    
            
    ALTRO("ALTRO");
            
        
    

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
  private String codificaIUV = null;
  
  @JsonProperty("idEntrata")
  private String idEntrata = null;
  
  /**
   **/
  public TipoEntrata descrizione(String descrizione) {
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
  public TipoEntrata tipoContabilitaEnum(TipoContabilitaEnum tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonIgnore
  public TipoContabilitaEnum getTipoContabilitaEnum() {
    return tipoContabilita;
  }
  public void setTipoContabilitaEnum(TipoContabilitaEnum tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  /**
   * Tipologia di codifica del capitolo di bilancio
   **/
  public TipoEntrata tipoContabilita(String tipoContabilita) {
	  if(tipoContabilita != null) {
	    this.tipoContabilita = TipoContabilitaEnum.fromValue(tipoContabilita);
	  }
	    return this;
  }

  @JsonProperty("tipoContabilita")
  public String getTipoContabilita() {
	  if(this.tipoContabilita!=null)
    return tipoContabilita.toString();
	  return null;
  }
  public void setTipoContabilita(String tipoContabilita) {
	  if(tipoContabilita != null) {
		    this.tipoContabilita = TipoContabilitaEnum.fromValue(tipoContabilita);
	  }
  }

  /**
   * Codifica del capitolo di bilancio
   **/
  public TipoEntrata codiceContabilita(String codiceContabilita) {
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
  public TipoEntrata codificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public String getCodificaIUV() {
    return codificaIUV;
  }
  public void setCodificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   **/
  public TipoEntrata idEntrata(String idEntrata) {
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
    TipoEntrata tipoEntrata = (TipoEntrata) o;
    return Objects.equals(descrizione, tipoEntrata.descrizione) &&
        Objects.equals(tipoContabilita, tipoEntrata.tipoContabilita) &&
        Objects.equals(codiceContabilita, tipoEntrata.codiceContabilita) &&
        Objects.equals(codificaIUV, tipoEntrata.codificaIUV) &&
        Objects.equals(idEntrata, tipoEntrata.idEntrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, tipoContabilita, codiceContabilita, codificaIUV, idEntrata);
  }

  public static TipoEntrata parse(String json) {
    return (TipoEntrata) parse(json, TipoEntrata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoEntrata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoEntrata {\n");
    
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



