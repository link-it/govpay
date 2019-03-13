package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"online",
"pagaTerzi",
"abilitato",
})
public class EntrataPost extends it.govpay.core.beans.JSONSerializable implements IValidable {
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("ibanAppoggio")
  private String ibanAppoggio = null;
  
  @JsonIgnore
  private TipoContabilita tipoContabilitaEnum = null;
  
  @JsonProperty("tipoContabilita")
  private String tipoContabilita = null;
  
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
   * Tipologia di codifica del capitolo di bilancio
   **/
  public EntrataPost tipoContabilitaEnum(TipoContabilita tipoContabilitaEnum) {
    this.tipoContabilitaEnum = tipoContabilitaEnum;
    return this;
  }

  @JsonIgnore
  public TipoContabilita getTipoContabilitaEnum() {
    return this.tipoContabilitaEnum;
  }
  public void setTipoContabilitaEnum(TipoContabilita tipoContabilitaEnum) {
    this.tipoContabilitaEnum = tipoContabilitaEnum;
  }
  
  /**
   * Tipologia di codifica del capitolo di bilancio
   **/
  public EntrataPost tipoContabilita(String tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonProperty("tipoContabilita")
  public String getTipoContabilita() {
    return this.tipoContabilita;
  }
  public void setTipoContabilita(String tipoContabilita) {
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
   * Indica se l'entrata spontanea e' pagabile online
   **/
  public EntrataPost online(Boolean online) {
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
  public EntrataPost pagaTerzi(Boolean pagaTerzi) {
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
        Objects.equals(online, entrataPost.online) &&
	Objects.equals(pagaTerzi, entrataPost.pagaTerzi) &&
        Objects.equals(this.abilitato, entrataPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ibanAccredito, this.ibanAppoggio, this.tipoContabilita, this.codiceContabilita, this.codificaIUV, online, pagaTerzi,  this.abilitato);
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
    sb.append("    online: ").append(toIndentedString(online)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
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
  
  @Override
  public void validate() throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	vf.getValidator("abilitato", this.abilitato).notNull();
	vf.getValidator("ibanAccredito", this.ibanAccredito).minLength(1).maxLength(255);
	vf.getValidator("ibanAppoggio", this.ibanAppoggio).minLength(1).maxLength(255);
	vf.getValidator("codiceContabilita", this.codiceContabilita).minLength(1).maxLength(255);
	vf.getValidator("codificaIUV", this.codificaIUV).minLength(1).maxLength(4);
	vf.getValidator("online", this.online).notNull();
	vf.getValidator("pagaTerzi", this.pagaTerzi).notNull();
  }
}



