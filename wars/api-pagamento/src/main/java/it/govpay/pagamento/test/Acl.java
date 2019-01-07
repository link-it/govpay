package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.TipoAutorizzazione;
import it.govpay.pagamento.v2.beans.TipoServizio;
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

public class Acl  {
  
  @Schema(example = "Operatore", description = "ruolo a cui si applica l'acl")
 /**
   * ruolo a cui si applica l'acl  
  **/
  private String ruolo = null;
  
  @Schema(example = "user001", description = "principal a cui si applica l'acl")
 /**
   * principal a cui si applica l'acl  
  **/
  private String principal = null;
  
  @Schema(required = true, description = "")
  private TipoServizio servizi = null;
  
  @Schema(required = true, description = "")
  private TipoAutorizzazione autorizzazioni = null;
 /**
   * ruolo a cui si applica l&#x27;acl
   * @return ruolo
  **/
  @JsonProperty("ruolo")
  public String getRuolo() {
    return ruolo;
  }

  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }

  public Acl ruolo(String ruolo) {
    this.ruolo = ruolo;
    return this;
  }

 /**
   * principal a cui si applica l&#x27;acl
   * @return principal
  **/
  @JsonProperty("principal")
  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  public Acl principal(String principal) {
    this.principal = principal;
    return this;
  }

 /**
   * Get servizi
   * @return servizi
  **/
  @JsonProperty("servizi")
  @NotNull
  public TipoServizio getServizi() {
    return servizi;
  }

  public void setServizi(TipoServizio servizi) {
    this.servizi = servizi;
  }

  public Acl servizi(TipoServizio servizi) {
    this.servizi = servizi;
    return this;
  }

 /**
   * Get autorizzazioni
   * @return autorizzazioni
  **/
  @JsonProperty("autorizzazioni")
  @NotNull
  public TipoAutorizzazione getAutorizzazioni() {
    return autorizzazioni;
  }

  public void setAutorizzazioni(TipoAutorizzazione autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
  }

  public Acl autorizzazioni(TipoAutorizzazione autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Acl {\n");
    
    sb.append("    ruolo: ").append(toIndentedString(ruolo)).append("\n");
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    servizi: ").append(toIndentedString(servizi)).append("\n");
    sb.append("    autorizzazioni: ").append(toIndentedString(autorizzazioni)).append("\n");
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
