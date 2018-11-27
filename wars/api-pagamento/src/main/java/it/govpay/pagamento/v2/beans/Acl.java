package it.govpay.pagamento.v2.beans;

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
"id",
})
public class Acl extends JSONSerializable {
  
  @JsonProperty("ruolo")
  private String ruolo = null;
  
  @JsonProperty("principal")
  private String principal = null;
  
    
  /**
   * Servizio oggetto dell'autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pendenze e Pagamenti  * Pendenze e Pagamenti propri   * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
   */
  public enum ServizioEnum {
    
    
        
            
    ANAGRAFICA_PAGOPA("Anagrafica PagoPA"),
    
            
    ANAGRAFICA_CREDITORE("Anagrafica Creditore"),
    
            
    ANAGRAFICA_APPLICAZIONI("Anagrafica Applicazioni"),
    
            
    ANAGRAFICA_RUOLI("Anagrafica Ruoli"),
    
            
    PAGAMENTI_E_PENDENZE("Pagamenti e Pendenze"),
    
            
    RENDICONTAZIONI_E_INCASSI("Rendicontazioni e Incassi"),
    
            
    GIORNALE_DEGLI_EVENTI("Giornale degli Eventi"),
    
            
    CONFIGURAZIONE_E_MANUTENZIONE("Configurazione e manutenzione");
            
        
    

    private String value;

    ServizioEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(this.value);
    }

    public static ServizioEnum fromValue(String text) {
      for (ServizioEnum b : ServizioEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("servizio")
  private ServizioEnum servizio = null;
    /**
   * Gets or Sets autorizzazioni
   */
  public enum AutorizzazioniEnum {
    
    
        
            
    LETTURA("Lettura"),
    
            
    SCRITTURA("Scrittura"),
    
            
    ESECUZIONE("Esecuzione");
            
        
    

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
  
  @JsonProperty("id")
  private String id = null;
  
  /**
   * ruolo a cui si applica l'acl
   **/
  public Acl ruolo(String ruolo) {
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
  public Acl principal(String principal) {
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
   * Servizio oggetto dell'autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pendenze e Pagamenti  * Pendenze e Pagamenti propri   * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
   **/
  public Acl servizio(ServizioEnum servizio) {
    this.servizio = servizio;
    return this;
  }

  @JsonProperty("servizio")
  public ServizioEnum getServizio() {
    return this.servizio;
  }
  public void setServizio(ServizioEnum servizio) {
    this.servizio = servizio;
  }

  /**
   **/
  public Acl autorizzazioni(List<String> autorizzazioni) {
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

  /**
   * identificativo dell'acl
   **/
  public Acl id(String id) {
    this.id = id;
    return this;
  }

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Acl acl = (Acl) o;
    return Objects.equals(this.ruolo, acl.ruolo) &&
        Objects.equals(this.principal, acl.principal) &&
        Objects.equals(this.servizio, acl.servizio) &&
        Objects.equals(this.autorizzazioni, acl.autorizzazioni) &&
        Objects.equals(this.id, acl.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ruolo, this.principal, this.servizio, this.autorizzazioni, this.id);
  }

  public static Acl parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Acl.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "acl";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Acl {\n");
    
    sb.append("    ruolo: ").append(this.toIndentedString(this.ruolo)).append("\n");
    sb.append("    principal: ").append(this.toIndentedString(this.principal)).append("\n");
    sb.append("    servizio: ").append(this.toIndentedString(this.servizio)).append("\n");
    sb.append("    autorizzazioni: ").append(this.toIndentedString(this.autorizzazioni)).append("\n");
    sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
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



