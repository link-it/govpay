package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idUnita",
"ragioneSociale",
})
public class UnitaOperativaIndex extends JSONSerializable implements IValidable{
  
  @JsonProperty("idUnita")
  private String idUnita = null;
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  /**
   * Codice fiscale dell'unita
   **/
  public UnitaOperativaIndex idUnita(String idUnita) {
    this.idUnita = idUnita;
    return this;
  }

  @JsonProperty("idUnita")
  public String getIdUnita() {
    return idUnita;
  }
  public void setIdUnita(String idUnita) {
    this.idUnita = idUnita;
  }

  /**
   * Ragione sociale dell'unita
   **/
  public UnitaOperativaIndex ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }

  @JsonProperty("ragioneSociale")
  public String getRagioneSociale() {
    return ragioneSociale;
  }
  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnitaOperativaIndex unitaOperativaIndex = (UnitaOperativaIndex) o;
    return Objects.equals(idUnita, unitaOperativaIndex.idUnita) &&
        Objects.equals(ragioneSociale, unitaOperativaIndex.ragioneSociale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUnita, ragioneSociale); 
  }

  public static UnitaOperativaIndex parse(String json) throws ServiceException, ValidationException {
    return (UnitaOperativaIndex) parse(json, UnitaOperativaIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "unitaOperativaIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnitaOperativaIndex {\n");
    
    sb.append("    idUnita: ").append(toIndentedString(idUnita)).append("\n");
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
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
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		vf.getValidator("ragioneSociale", this.ragioneSociale).minLength(1).maxLength(70);
		validatoreId.validaIdUO("idUnita", this.idUnita);
	}
}



