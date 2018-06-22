package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import java.util.ArrayList;
import java.util.List;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"servizio",
"autorizzazioni",
})
public class AclPost extends it.govpay.core.rs.v1.beans.JSONSerializable {
 
    
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
    @JsonValue
    public String toString() {
      return String.valueOf(value);
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
    @JsonValue
    public String toString() {
      return String.valueOf(value);
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
  private List<String> autorizzazioni = new ArrayList<String>();
  
  /**
   * Servizio oggetto dell'autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pendenze e Pagamenti  * Pendenze e Pagamenti propri   * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
   **/
  public AclPost servizio(ServizioEnum servizio) {
    this.servizio = servizio;
    return this;
  }

  public ServizioEnum getServizioEnum() {
    return servizio;
  }
  public void setServizio(ServizioEnum servizio) {
    this.servizio = servizio;
  }

  public AclPost servizio(String servizio) {
	    this.setServizio(servizio);
	    return this;
	  }

  @JsonProperty("servizio")
  public String getServizio() {
	  if(servizio !=null)
		  return servizio.toString();
	  return null;
  }
  public void setServizio(String servizio) {
	  if(servizio !=null)
		  this.servizio = ServizioEnum.fromValue(servizio);
  }

  /**
   **/
  public AclPost autorizzazioni(List<String> autorizzazioni) {
    this.setAutorizzazioni(autorizzazioni);
    return this;
  }

  @JsonProperty("autorizzazioni")
  public List<String> getAutorizzazioni() {
	  if(autorizzazioni != null)
		  return autorizzazioni;
	  else return null;
  }
  public void setAutorizzazioni(List<String> autorizzazioni) {
	  if(autorizzazioni != null)
		  this.autorizzazioni = autorizzazioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AclPost aclPost = (AclPost) o;
    return Objects.equals(servizio, aclPost.servizio) &&
        Objects.equals(autorizzazioni, aclPost.autorizzazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(servizio, autorizzazioni);
  }

  public static AclPost parse(String json) {
    return (AclPost) parse(json, AclPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "aclPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AclPost {\n");
    
    sb.append("    servizio: ").append(toIndentedString(servizio)).append("\n");
    sb.append("    autorizzazioni: ").append(toIndentedString(autorizzazioni)).append("\n");
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



