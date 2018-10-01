package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"abilitato",
})
public class EntrataPost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("ibanAppoggio")
  private String ibanAppoggio = null;
  
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
  public EntrataPost ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return this.ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  /**
   **/
  public EntrataPost ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

  @JsonProperty("ibanAppoggio")
  public String getIbanAppoggio() {
    return this.ibanAppoggio;
  }
  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  /**
   **/
  public EntrataPost tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return this.tipoContabilita;
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
    return this.codiceContabilita;
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
    return this.codificaIUV;
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
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    EntrataPost entrataPost = (EntrataPost) o;
    return Objects.equals(this.ibanAccredito, entrataPost.ibanAccredito) &&
        Objects.equals(this.ibanAppoggio, entrataPost.ibanAppoggio) &&
        Objects.equals(this.tipoContabilita, entrataPost.tipoContabilita) &&
        Objects.equals(this.codiceContabilita, entrataPost.codiceContabilita) &&
        Objects.equals(this.codificaIUV, entrataPost.codificaIUV) &&
        Objects.equals(this.abilitato, entrataPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ibanAccredito, this.ibanAppoggio, this.tipoContabilita, this.codiceContabilita, this.codificaIUV, this.abilitato);
  }

  public static EntrataPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, EntrataPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "entrataPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntrataPost {\n");
    
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(this.toIndentedString(this.ibanAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(this.toIndentedString(this.codificaIUV)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
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



