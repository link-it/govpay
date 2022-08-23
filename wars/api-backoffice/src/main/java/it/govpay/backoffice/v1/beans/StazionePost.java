package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"password",
"abilitato",
"versione",
})
public class StazionePost extends it.govpay.core.beans.JSONSerializable implements IValidable{
  
  @JsonProperty("password")
  private String password = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonIgnore
  private VersioneStazione versioneEnum = null;
  
  @JsonProperty("versione")
  private String versione = null;
  
  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public StazionePost password(String password) {
    this.password = password;
    return this;
  }

  @JsonProperty("password")
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Indica lo stato di abilitazione
   **/
  public StazionePost abilitato(Boolean abilitato) {
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
  public StazionePost versioneEnum(VersioneStazione versione) {
    this.versioneEnum = versione;
    return this;
  }

  @JsonIgnore
  public VersioneStazione getVersioneEnum() {
    return versioneEnum;
  }
  public void setVersioneEnum(VersioneStazione versione) {
    this.versioneEnum = versione;
  }
  
  /**
   **/
  public StazionePost versione(String versione) {
    this.versione = versione;
    return this;
  }

  @JsonProperty("versione")
  public String getVersione() {
    return versione;
  }
  public void setVersione(String versione) {
    this.versione = versione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    StazionePost stazionePost = (StazionePost) o;
    return Objects.equals(password, stazionePost.password) &&
        Objects.equals(abilitato, stazionePost.abilitato) &&
        Objects.equals(versione, stazionePost.versione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, abilitato, versione);
  }

  public static StazionePost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, StazionePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "stazionePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StazionePost {\n");
    
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
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
		vf.getValidator("password", this.password).notNull().minLength(1).maxLength(35);
		vf.getValidator("abilitato", this.abilitato).notNull();
		vf.getValidator("versione", this.versione).notNull();
	}
}



