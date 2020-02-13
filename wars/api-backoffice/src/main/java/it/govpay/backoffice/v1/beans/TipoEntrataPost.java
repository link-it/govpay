package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"descrizione",
"tipoContabilita",
"codiceContabilita",
})
public class TipoEntrataPost extends it.govpay.core.beans.JSONSerializable implements IValidable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonIgnore
  private TipoContabilita tipoContabilitaEnum = null;
  
  @JsonProperty("tipoContabilita")
  private String tipoContabilita = null;
  
  @JsonProperty("codiceContabilita")
  private String codiceContabilita = null;
  
  /**
   **/
  public TipoEntrataPost descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return this.descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Tipologia di codifica del capitolo di bilancio
   **/
  public TipoEntrataPost tipoContabilitaEnum(TipoContabilita tipoContabilitaEnum) {
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
  public TipoEntrataPost tipoContabilita(String tipoContabilita) {
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
  public TipoEntrataPost codiceContabilita(String codiceContabilita) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TipoEntrataPost tipoEntrataPost = (TipoEntrataPost) o;
    return Objects.equals(this.descrizione, tipoEntrataPost.descrizione) &&
        Objects.equals(this.tipoContabilita, tipoEntrataPost.tipoContabilita) &&
        Objects.equals(this.codiceContabilita, tipoEntrataPost.codiceContabilita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.descrizione, this.tipoContabilita, this.codiceContabilita);
  }

  public static TipoEntrataPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, TipoEntrataPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoEntrataPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoEntrataPost {\n");
    
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
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
	vf.getValidator("descrizione", this.descrizione).notNull().minLength(1).maxLength(255);
	vf.getValidator("tipoContabilita", this.tipoContabilita).notNull();
	ValidatoreUtils.validaCodiceContabilita(vf, "codiceContabilita", this.codiceContabilita);
  }
}



