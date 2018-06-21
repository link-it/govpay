package it.govpay.core.rs.v1.beans.pagamenti;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"contoAccredito",
"contoAppoggio",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"abilitato",
})
public class EntrataPost extends JSONSerializable {
  
  @JsonProperty("contoAccredito")
  private String contoAccredito = null;
  
  @JsonProperty("contoAppoggio")
  private String contoAppoggio = null;
  
  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita = null;
  
  @JsonProperty("codiceContabilita")
  private String codiceContabilita = null;
  
  @JsonProperty("codificaIUV")
  private String codificaIUV = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  /**
   **/
  public EntrataPost contoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
    return this;
  }

  @JsonProperty("contoAccredito")
  public String getContoAccredito() {
    return contoAccredito;
  }
  public void setContoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
  }

  /**
   **/
  public EntrataPost contoAppoggio(String contoAppoggio) {
    this.contoAppoggio = contoAppoggio;
    return this;
  }

  @JsonProperty("contoAppoggio")
  public String getContoAppoggio() {
    return contoAppoggio;
  }
  public void setContoAppoggio(String contoAppoggio) {
    this.contoAppoggio = contoAppoggio;
  }

  /**
   **/
  public EntrataPost tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilita tipoContabilita) {
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
  public EntrataPost codificaIUV(String codificaIUV) {
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
    return Objects.equals(contoAccredito, entrataPost.contoAccredito) &&
        Objects.equals(contoAppoggio, entrataPost.contoAppoggio) &&
        Objects.equals(tipoContabilita, entrataPost.tipoContabilita) &&
        Objects.equals(codiceContabilita, entrataPost.codiceContabilita) &&
        Objects.equals(codificaIUV, entrataPost.codificaIUV) &&
        Objects.equals(abilitato, entrataPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contoAccredito, contoAppoggio, tipoContabilita, codiceContabilita, codificaIUV, abilitato);
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
    
    sb.append("    contoAccredito: ").append(toIndentedString(contoAccredito)).append("\n");
    sb.append("    contoAppoggio: ").append(toIndentedString(contoAppoggio)).append("\n");
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



