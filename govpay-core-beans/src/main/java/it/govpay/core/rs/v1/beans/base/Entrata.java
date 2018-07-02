package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"abilitato",
"idEntrata",
"tipoEntrata",
})
public class Entrata extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
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
  
  @JsonProperty("idEntrata")
  private String idEntrata = null;
  
  @JsonProperty("tipoEntrata")
  private TipoEntrata tipoEntrata = null;
  
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
   **/
  public Entrata ibanAppoggio(String ibanAppoggio) {
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
  public Entrata tipoContabilita(TipoContabilita tipoContabilita) {
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
  public Entrata codificaIUV(String codificaIUV) {
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
  public Entrata abilitato(Boolean abilitato) {
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

  /**
   **/
  public Entrata idEntrata(String idEntrata) {
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

  /**
   **/
  public Entrata tipoEntrata(TipoEntrata tipoEntrata) {
    this.tipoEntrata = tipoEntrata;
    return this;
  }

  @JsonProperty("tipoEntrata")
  public TipoEntrata getTipoEntrata() {
    return tipoEntrata;
  }
  public void setTipoEntrata(TipoEntrata tipoEntrata) {
    this.tipoEntrata = tipoEntrata;
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
    return Objects.equals(ibanAccredito, entrata.ibanAccredito) &&
        Objects.equals(ibanAppoggio, entrata.ibanAppoggio) &&
        Objects.equals(tipoContabilita, entrata.tipoContabilita) &&
        Objects.equals(codiceContabilita, entrata.codiceContabilita) &&
        Objects.equals(codificaIUV, entrata.codificaIUV) &&
        Objects.equals(abilitato, entrata.abilitato) &&
        Objects.equals(idEntrata, entrata.idEntrata) &&
        Objects.equals(tipoEntrata, entrata.tipoEntrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ibanAccredito, ibanAppoggio, tipoContabilita, codiceContabilita, codificaIUV, abilitato, idEntrata, tipoEntrata);
  }

  public static Entrata parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
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
    
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    idEntrata: ").append(toIndentedString(idEntrata)).append("\n");
    sb.append("    tipoEntrata: ").append(toIndentedString(tipoEntrata)).append("\n");
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



