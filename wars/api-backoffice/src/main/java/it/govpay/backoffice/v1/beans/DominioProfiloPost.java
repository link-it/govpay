package it.govpay.backoffice.v1.beans;


import java.util.List;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"unitaOperative",
})
public class DominioProfiloPost extends JSONSerializable  implements IValidable{
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("unitaOperative")
  private List<String> unitaOperative = null;
  
  /**
   * Codice fiscale del beneficiario
   **/
  public DominioProfiloPost idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   **/
  public DominioProfiloPost unitaOperative(List<String> unitaOperative) {
    this.unitaOperative = unitaOperative;
    return this;
  }

  @JsonProperty("unitaOperative")
  public List<String> getUnitaOperative() {
    return unitaOperative;
  }
  public void setUnitaOperative(List<String> unitaOperative) {
    this.unitaOperative = unitaOperative;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DominioProfiloPost dominioProfiloPost = (DominioProfiloPost) o;
    return Objects.equals(idDominio, dominioProfiloPost.idDominio) &&
        Objects.equals(unitaOperative, dominioProfiloPost.unitaOperative);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, unitaOperative);
  }

  public static DominioProfiloPost parse(String json) throws ServiceException, ValidationException {
    return (DominioProfiloPost) parse(json, DominioProfiloPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "dominioProfiloPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DominioProfiloPost {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    unitaOperative: ").append(toIndentedString(unitaOperative)).append("\n");
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
 	public void validate() throws org.openspcoop2.utils.json.ValidationException {
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		validatoreId.validaIdDominio("idDominio", this.idDominio);
		
		if(this.unitaOperative != null && !this.unitaOperative.isEmpty()) {
			for (String idUO : this.unitaOperative) {
				if(!idUO.equals(ApplicazioniController.AUTORIZZA_UO_STAR))
					validatoreId.validaIdUO("unitaOperative", idUO);
			}
		}
  }
}



