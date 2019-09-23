package it.govpay.pagamento.v1.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"ruolo",
"principal",
"servizio",
"autorizzazioni",
})
public class AclPost extends JSONSerializable {
  
  @JsonProperty("ruolo")
  private String ruolo = null;
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("servizio")
  private String servizio = null;
    /**
   * Gets or Sets autorizzazioni
   */
  public enum AutorizzazioniEnum {
    
    
        
            
    LETTURA("Lettura"),
    
            
    SCRITTURA("Scrittura");
            
        
    

    private String value;

    AutorizzazioniEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(this.value);
    }

    public static AutorizzazioniEnum fromValue(String text) {
      for (AutorizzazioniEnum b : AutorizzazioniEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

      
  @JsonProperty("autorizzazioni")
  private List<String> autorizzazioni = new ArrayList<>();
  
  /**
   * ruolo a cui si applica l'acl
   **/
  public AclPost ruolo(String ruolo) {
    this.ruolo = ruolo;
    return this;
  }

  @JsonProperty("ruolo")
  public String getRuolo() {
    return this.ruolo;
  }
  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }

  /**
   * principal a cui si applica l'acl
   **/
  public AclPost principal(String principal) {
    this.principal = principal;
    return this;
  }

  @JsonProperty("principal")
  public String getPrincipal() {
    return this.principal;
  }
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   * Servizio oggetto dell'autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pagamenti  * Pendenze  * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
   **/
  public AclPost servizio(String servizio) {
    this.servizio = servizio;
    return this;
  }

  @JsonProperty("servizio")
  public String getServizio() {
    return servizio;
  }
  public void setServizio(String servizio) {
    this.servizio = servizio;
  }

  /**
   **/
  public AclPost autorizzazioni(List<String> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
    return this;
  }

  @JsonProperty("autorizzazioni")
  public List<String> getAutorizzazioni() {
    return this.autorizzazioni;
  }
  public void setAutorizzazioni(List<String> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    AclPost aclPost = (AclPost) o;
    return Objects.equals(this.ruolo, aclPost.ruolo) &&
        Objects.equals(this.principal, aclPost.principal) &&
        Objects.equals(this.servizio, aclPost.servizio) &&
        Objects.equals(this.autorizzazioni, aclPost.autorizzazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ruolo, this.principal, this.servizio, this.autorizzazioni);
  }

  public static AclPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, AclPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "aclPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AclPost {\n");
    
    sb.append("    ruolo: ").append(this.toIndentedString(this.ruolo)).append("\n");
    sb.append("    principal: ").append(this.toIndentedString(this.principal)).append("\n");
    sb.append("    servizio: ").append(this.toIndentedString(this.servizio)).append("\n");
    sb.append("    autorizzazioni: ").append(this.toIndentedString(this.autorizzazioni)).append("\n");
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



