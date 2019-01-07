package it.govpay.pagamento.test;

import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PagamentoCreato  {
  
  @Schema(example = "12345", required = true, description = "identificativo del pagamento")
 /**
   * identificativo del pagamento  
  **/
  private String id = null;
  
  @Schema(example = "/pagamenti/12345", description = "Url del dettaglio del pagamento")
 /**
   * Url del dettaglio del pagamento  
  **/
  private String href = null;
  
  @Schema(required = true, description = "url a cui redirigere il navigatore per proseguire nel pagamento.")
 /**
   * url a cui redirigere il navigatore per proseguire nel pagamento.  
  **/
  private String redirect = null;
  
  @Schema(example = "bdd07392-d90d-11e7-9296-cec278b6b50a", description = "identificativo della sessione di pagamento assegnato da pagoPA")
 /**
   * identificativo della sessione di pagamento assegnato da pagoPA  
  **/
  private String idSessionePsp = null;
 /**
   * identificativo del pagamento
   * @return id
  **/
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PagamentoCreato id(String id) {
    this.id = id;
    return this;
  }

 /**
   * Url del dettaglio del pagamento
   * @return href
  **/
  @JsonProperty("href")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public PagamentoCreato href(String href) {
    this.href = href;
    return this;
  }

 /**
   * url a cui redirigere il navigatore per proseguire nel pagamento.
   * @return redirect
  **/
  @JsonProperty("redirect")
  @NotNull
  public String getRedirect() {
    return redirect;
  }

  public void setRedirect(String redirect) {
    this.redirect = redirect;
  }

  public PagamentoCreato redirect(String redirect) {
    this.redirect = redirect;
    return this;
  }

 /**
   * identificativo della sessione di pagamento assegnato da pagoPA
   * @return idSessionePsp
  **/
  @JsonProperty("idSessionePsp")
  public String getIdSessionePsp() {
    return idSessionePsp;
  }

  public void setIdSessionePsp(String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
  }

  public PagamentoCreato idSessionePsp(String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagamentoCreato {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    redirect: ").append(toIndentedString(redirect)).append("\n");
    sb.append("    idSessionePsp: ").append(toIndentedString(idSessionePsp)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
