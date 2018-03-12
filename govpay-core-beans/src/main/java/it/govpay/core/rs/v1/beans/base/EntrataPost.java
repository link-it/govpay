package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import java.math.BigDecimal;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"ibanAccreditoBancario",
"ibanAccreditoPostale",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"abilitato",
})
public class EntrataPost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("ibanAccreditoBancario")
  private String ibanAccreditoBancario = null;
  
  @JsonProperty("ibanAccreditoPostale")
  private String ibanAccreditoPostale = null;
  
    
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
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  /**
   **/
  public EntrataPost ibanAccreditoBancario(String ibanAccreditoBancario) {
    this.ibanAccreditoBancario = ibanAccreditoBancario;
    return this;
  }

  @JsonProperty("ibanAccreditoBancario")
  public String getIbanAccreditoBancario() {
    return ibanAccreditoBancario;
  }
  public void setIbanAccreditoBancario(String ibanAccreditoBancario) {
    this.ibanAccreditoBancario = ibanAccreditoBancario;
  }

  /**
   **/
  public EntrataPost ibanAccreditoPostale(String ibanAccreditoPostale) {
    this.ibanAccreditoPostale = ibanAccreditoPostale;
    return this;
  }

  @JsonProperty("ibanAccreditoPostale")
  public String getIbanAccreditoPostale() {
    return ibanAccreditoPostale;
  }
  public void setIbanAccreditoPostale(String ibanAccreditoPostale) {
    this.ibanAccreditoPostale = ibanAccreditoPostale;
  }

  /**
   * Tipologia di codifica del capitolo di bilancio
   **/
  public EntrataPost tipoContabilita(TipoContabilitaEnum tipoContabilita) {
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
  public EntrataPost codiceContabilita(String codiceContabilita) {
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
  public EntrataPost codificaIUV(BigDecimal codificaIUV) {
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
   * Indicazione l'entrata e' abilitata
   **/
  public EntrataPost abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntrataPost entrataPost = (EntrataPost) o;
    return Objects.equals(ibanAccreditoBancario, entrataPost.ibanAccreditoBancario) &&
        Objects.equals(ibanAccreditoPostale, entrataPost.ibanAccreditoPostale) &&
        Objects.equals(tipoContabilita, entrataPost.tipoContabilita) &&
        Objects.equals(codiceContabilita, entrataPost.codiceContabilita) &&
        Objects.equals(codificaIUV, entrataPost.codificaIUV) &&
        Objects.equals(abilitato, entrataPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ibanAccreditoBancario, ibanAccreditoPostale, tipoContabilita, codiceContabilita, codificaIUV, abilitato);
  }

  public static EntrataPost parse(String json) {
    return (EntrataPost) parse(json, EntrataPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "entrataPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntrataPost {\n");
    
    sb.append("    ibanAccreditoBancario: ").append(toIndentedString(ibanAccreditoBancario)).append("\n");
    sb.append("    ibanAccreditoPostale: ").append(toIndentedString(ibanAccreditoPostale)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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



