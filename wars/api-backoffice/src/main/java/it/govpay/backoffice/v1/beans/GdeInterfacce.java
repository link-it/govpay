package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"apiEnte",
"apiPagamento",
"apiRagioneria",
"apiBackoffice",
"apiPagoPA",
})
public class GdeInterfacce extends JSONSerializable implements IValidable {
  
  @JsonProperty("apiEnte")
  private GdeInterfaccia apiEnte = null;
  
  @JsonProperty("apiPagamento")
  private GdeInterfaccia apiPagamento = null;
  
  @JsonProperty("apiRagioneria")
  private GdeInterfaccia apiRagioneria = null;
  
  @JsonProperty("apiBackoffice")
  private GdeInterfaccia apiBackoffice = null;
  
  @JsonProperty("apiPagoPA")
  private GdeInterfaccia apiPagoPA = null;
  
  /**
   **/
  public GdeInterfacce apiEnte(GdeInterfaccia apiEnte) {
    this.apiEnte = apiEnte;
    return this;
  }

  @JsonProperty("apiEnte")
  public GdeInterfaccia getApiEnte() {
    return apiEnte;
  }
  public void setApiEnte(GdeInterfaccia apiEnte) {
    this.apiEnte = apiEnte;
  }

  /**
   **/
  public GdeInterfacce apiPagamento(GdeInterfaccia apiPagamento) {
    this.apiPagamento = apiPagamento;
    return this;
  }

  @JsonProperty("apiPagamento")
  public GdeInterfaccia getApiPagamento() {
    return apiPagamento;
  }
  public void setApiPagamento(GdeInterfaccia apiPagamento) {
    this.apiPagamento = apiPagamento;
  }

  /**
   **/
  public GdeInterfacce apiRagioneria(GdeInterfaccia apiRagioneria) {
    this.apiRagioneria = apiRagioneria;
    return this;
  }

  @JsonProperty("apiRagioneria")
  public GdeInterfaccia getApiRagioneria() {
    return apiRagioneria;
  }
  public void setApiRagioneria(GdeInterfaccia apiRagioneria) {
    this.apiRagioneria = apiRagioneria;
  }

  /**
   **/
  public GdeInterfacce apiBackoffice(GdeInterfaccia apiBackoffice) {
    this.apiBackoffice = apiBackoffice;
    return this;
  }

  @JsonProperty("apiBackoffice")
  public GdeInterfaccia getApiBackoffice() {
    return apiBackoffice;
  }
  public void setApiBackoffice(GdeInterfaccia apiBackoffice) {
    this.apiBackoffice = apiBackoffice;
  }

  /**
   **/
  public GdeInterfacce apiPagoPA(GdeInterfaccia apiPagoPA) {
    this.apiPagoPA = apiPagoPA;
    return this;
  }

  @JsonProperty("apiPagoPA")
  public GdeInterfaccia getApiPagoPA() {
    return apiPagoPA;
  }
  public void setApiPagoPA(GdeInterfaccia apiPagoPA) {
    this.apiPagoPA = apiPagoPA;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdeInterfacce gdeInterfacce = (GdeInterfacce) o;
    return Objects.equals(apiEnte, gdeInterfacce.apiEnte) &&
        Objects.equals(apiPagamento, gdeInterfacce.apiPagamento) &&
        Objects.equals(apiRagioneria, gdeInterfacce.apiRagioneria) &&
        Objects.equals(apiBackoffice, gdeInterfacce.apiBackoffice) &&
        Objects.equals(apiPagoPA, gdeInterfacce.apiPagoPA);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiEnte, apiPagamento, apiRagioneria, apiBackoffice, apiPagoPA);
  }
  
  public static GdeInterfacce parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
	    return parse(json, GdeInterfacce.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "gdeInterfacce";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdeInterfacce {\n");
    
    sb.append("    apiEnte: ").append(toIndentedString(apiEnte)).append("\n");
    sb.append("    apiPagamento: ").append(toIndentedString(apiPagamento)).append("\n");
    sb.append("    apiRagioneria: ").append(toIndentedString(apiRagioneria)).append("\n");
    sb.append("    apiBackoffice: ").append(toIndentedString(apiBackoffice)).append("\n");
    sb.append("    apiPagoPA: ").append(toIndentedString(apiPagoPA)).append("\n");
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
		vf.getValidator("apiEnte", this.apiEnte).notNull().validateFields();
		vf.getValidator("apiPagamento", this.apiPagamento).notNull().validateFields();
		vf.getValidator("apiRagioneria", this.apiRagioneria).notNull().validateFields();
		vf.getValidator("apiBackoffice", this.apiBackoffice).notNull().validateFields();
		vf.getValidator("apiPagoPA", this.apiPagoPA).notNull().validateFields();
 	}
}



