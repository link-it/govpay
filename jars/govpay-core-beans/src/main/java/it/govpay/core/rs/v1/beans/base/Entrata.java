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
"online",
"pagaTerzi",
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
  
  @JsonProperty("online")
  private Boolean online = false;
  
  @JsonProperty("pagaTerzi")
  private Boolean pagaTerzi = false;
  
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
    return this.ibanAccredito;
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
    return this.ibanAppoggio;
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
    return this.tipoContabilita;
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
    return this.codiceContabilita;
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
    return this.codificaIUV;
  }
  public void setCodificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   * Indica se l'entrata spontanea e' pagabile online
   **/
  public Entrata online(Boolean online) {
    this.online = online;
    return this;
  }

  @JsonProperty("online")
  public Boolean Online() {
    return online;
  }
  public void setOnline(Boolean online) {
    this.online = online;
  }

  /**
   * Indica se l'entrata e' pagabile da soggetti terzi
   **/
  public Entrata pagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  public Boolean PagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
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
    return this.abilitato;
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
    return this.idEntrata;
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
    return this.tipoEntrata;
  }
  public void setTipoEntrata(TipoEntrata tipoEntrata) {
    this.tipoEntrata = tipoEntrata;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Entrata entrata = (Entrata) o;
    return Objects.equals(this.ibanAccredito, entrata.ibanAccredito) &&
        Objects.equals(this.ibanAppoggio, entrata.ibanAppoggio) &&
        Objects.equals(this.tipoContabilita, entrata.tipoContabilita) &&
        Objects.equals(this.codiceContabilita, entrata.codiceContabilita) &&
        Objects.equals(this.codificaIUV, entrata.codificaIUV) &&
        Objects.equals(online, entrata.online) &&
	Objects.equals(pagaTerzi, entrata.pagaTerzi) &&
        Objects.equals(this.abilitato, entrata.abilitato) &&
        Objects.equals(this.idEntrata, entrata.idEntrata) &&
        Objects.equals(this.tipoEntrata, entrata.tipoEntrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ibanAccredito, this.ibanAppoggio, this.tipoContabilita, this.codiceContabilita, this.codificaIUV,  online, pagaTerzi, this.abilitato, this.idEntrata, this.tipoEntrata);
  }

  public static Entrata parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Entrata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "entrata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Entrata {\n");
    
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(this.toIndentedString(this.ibanAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(this.toIndentedString(this.codificaIUV)).append("\n");
    sb.append("    online: ").append(toIndentedString(online)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
    sb.append("    idEntrata: ").append(this.toIndentedString(this.idEntrata)).append("\n");
    sb.append("    tipoEntrata: ").append(this.toIndentedString(this.tipoEntrata)).append("\n");
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



