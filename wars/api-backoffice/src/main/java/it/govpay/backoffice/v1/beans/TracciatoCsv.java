package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"responseHeader",
"freemarkerRequest",
"freemarkerResponse",
})
public class TracciatoCsv extends JSONSerializable implements IValidable {
  
  @JsonProperty("responseHeader")
  private String responseHeader = null;
  
  @JsonProperty("freemarkerRequest")
  private Object freemarkerRequest = null;
  
  @JsonProperty("freemarkerResponse")
  private Object freemarkerResponse = null;
  
  /**
   * intestazione del file CSV di risposta
   **/
  public TracciatoCsv responseHeader(String responseHeader) {
    this.responseHeader = responseHeader;
    return this;
  }

  @JsonProperty("responseHeader")
  public String getResponseHeader() {
    return responseHeader;
  }
  public void setResponseHeader(String responseHeader) {
    this.responseHeader = responseHeader;
  }

  /**
   * Template freemarker per la trasformazione di un record da CSV a JSON
   **/
  public TracciatoCsv freemarkerRequest(Object freemarkerRequest) {
    this.freemarkerRequest = freemarkerRequest;
    return this;
  }

  @JsonProperty("freemarkerRequest")
  public Object getFreemarkerRequest() {
    return freemarkerRequest;
  }
  public void setFreemarkerRequest(Object freemarkerRequest) {
    this.freemarkerRequest = freemarkerRequest;
  }

  /**
   * Template freemarker per la trasformazione di un record da JSON a CSV
   **/
  public TracciatoCsv freemarkerResponse(Object freemarkerResponse) {
    this.freemarkerResponse = freemarkerResponse;
    return this;
  }

  @JsonProperty("freemarkerResponse")
  public Object getFreemarkerResponse() {
    return freemarkerResponse;
  }
  public void setFreemarkerResponse(Object freemarkerResponse) {
    this.freemarkerResponse = freemarkerResponse;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TracciatoCsv tracciatoCsv = (TracciatoCsv) o;
    return Objects.equals(responseHeader, tracciatoCsv.responseHeader) &&
        Objects.equals(freemarkerRequest, tracciatoCsv.freemarkerRequest) &&
        Objects.equals(freemarkerResponse, tracciatoCsv.freemarkerResponse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, freemarkerRequest, freemarkerResponse);
  }

  public static TracciatoCsv parse(String json) throws ServiceException, ValidationException {
    return parse(json, TracciatoCsv.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciatoCsv";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TracciatoCsv {\n");
    
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    freemarkerRequest: ").append(toIndentedString(freemarkerRequest)).append("\n");
    sb.append("    freemarkerResponse: ").append(toIndentedString(freemarkerResponse)).append("\n");
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
	  int v = 0;
	  v = this.responseHeader != null ? v+1 : v;
	  v = this.freemarkerRequest != null ? v+1 : v;
	  v = this.freemarkerResponse != null ? v+1 : v;
		
	  if(v != 3) {
		  throw new ValidationException("I campi 'responseHeader', 'freemarkerRequest' e 'freemarkerResponse' devono essere tutti valorizzati per definire il field 'tracciatoCsv'.");
	  }
  }
}



