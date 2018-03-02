package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import java.math.BigDecimal;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"descrizione",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
})
public class TipoentrataPost extends it.govpay.rs.v1.beans.JSONSerializable {
  
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
  
  /**
   **/
  public TipoentrataPost descrizione(String descrizione) {
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
  public TipoentrataPost tipoContabilita(TipoContabilitaEnum tipoContabilita) {
	    this.tipoContabilita = tipoContabilita;
	    return this;
	  }

	  @JsonProperty("tipoContabilita")
	  public TipoContabilitaEnum getTipoContabilitaEnum() {
	    return tipoContabilita;
	  }
	  public void setTipoContabilita(TipoContabilitaEnum tipoContabilita) {
	    this.tipoContabilita = tipoContabilita;
	  }

	  /**
	   * Tipologia di codifica del capitolo di bilancio
	   **/
	  public TipoentrataPost tipoContabilita(String tipoContabilita) {
		  setTipoContabilita(tipoContabilita);
		  return this;
	  }

	  @JsonProperty("tipoContabilita")
	  public String getTipoContabilita() {
		  return tipoContabilita.toString();
	  }
	  public void setTipoContabilita(String tipoContabilita) {
		  if(tipoContabilita!=null)
			  this.tipoContabilita = TipoContabilitaEnum.fromValue(tipoContabilita);
	  }

  /**
   * Codifica del capitolo di bilancio
   **/
  public TipoentrataPost codiceContabilita(String codiceContabilita) {
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
  public TipoentrataPost codificaIUV(BigDecimal codificaIUV) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoentrataPost tipoentrataPost = (TipoentrataPost) o;
    return Objects.equals(descrizione, tipoentrataPost.descrizione) &&
        Objects.equals(tipoContabilita, tipoentrataPost.tipoContabilita) &&
        Objects.equals(codiceContabilita, tipoentrataPost.codiceContabilita) &&
        Objects.equals(codificaIUV, tipoentrataPost.codificaIUV);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, tipoContabilita, codiceContabilita, codificaIUV);
  }

  public static TipoentrataPost parse(String json) {
    return (TipoentrataPost) parse(json, TipoentrataPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoentrataPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoentrataPost {\n");
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
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



