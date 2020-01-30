package it.govpay.pagamento.v2.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"id",
"location",
"redirect",
"idSession",
})
public class PagamentoCreato extends JSONSerializable {
  
  @JsonProperty("id")
  private String id = null;
  
  @JsonProperty("location")
  private String location = null;
  
  @JsonProperty("redirect")
  private String redirect = null;
  
  @JsonProperty("idSession")
  private String idSession = null;
  
  /**
   * identificativo del pagamento
   **/
  public PagamentoCreato id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Url del dettaglio del pagamento
   **/
  public PagamentoCreato location(String location) {
    this.location = location;
    return this;
  }

  @JsonProperty("location")
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * url a cui redirigere il navigatore per proseguire nel pagamento.
   **/
  public PagamentoCreato redirect(String redirect) {
    this.redirect = redirect;
    return this;
  }

  @JsonProperty("redirect")
  public String getRedirect() {
    return redirect;
  }
  public void setRedirect(String redirect) {
    this.redirect = redirect;
  }

  /**
   * identificativo della sessione di pagamento per la riconciliazione in al ritorno dal psp.
   **/
  public PagamentoCreato idSession(String idSession) {
    this.idSession = idSession;
    return this;
  }

  @JsonProperty("idSession")
  public String getIdSession() {
    return idSession;
  }
  public void setIdSession(String idSession) {
    this.idSession = idSession;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagamentoCreato pagamentoCreato = (PagamentoCreato) o;
    return Objects.equals(id, pagamentoCreato.id) &&
        Objects.equals(location, pagamentoCreato.location) &&
        Objects.equals(redirect, pagamentoCreato.redirect) &&
        Objects.equals(idSession, pagamentoCreato.idSession);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, location, redirect, idSession);
  }

  public static PagamentoCreato parse(String json) throws ServiceException, ValidationException { 
    return parse(json, PagamentoCreato.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pagamentoCreato";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagamentoCreato {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    redirect: ").append(toIndentedString(redirect)).append("\n");
    sb.append("    idSession: ").append(toIndentedString(idSession)).append("\n");
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



