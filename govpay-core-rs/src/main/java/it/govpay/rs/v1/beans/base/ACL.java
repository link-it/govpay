package it.govpay.rs.v1.beans.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"servizio",
"domini",
"entrate",
"idEntrata",
"soloPropri",
"autorizzazioni",
})
public class ACL extends it.govpay.rs.v1.beans.JSONSerializable {
  
    
  /**
   * Servizio oggetto dell'autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pendenze e Pagamenti  * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
   */
  public enum ServizioEnum {
    
    
        
            
    ANAGRAFICA_PAGOPA("Anagrafica PagoPA"),
    
            
    ANAGRAFICA_CREDITORE("Anagrafica Creditore"),
    
            
    ANAGRAFICA_APPLICAZIONI("Anagrafica Applicazioni"),
    
            
    ANAGRAFICA_RUOLI("Anagrafica Ruoli"),
    
            
    PAGAMENTI_E_PENDENZE("Pagamenti e Pendenze"),
    
            
    RENDICONTAZIONI_E_INCASSI("Rendicontazioni e Incassi"),
    
            
    GIORNALE_DEGLI_EVENTI("Giornale degli Eventi"),
    
            
    STATISTICHE("Statistiche"),
    
            
    CONFIGURAZIONE_E_MANUTENZIONE("Configurazione e manutenzione");
            
        
    

    private String value;

    ServizioEnum(String value) {
      this.value = value;
    }

    @Override
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
  
  @JsonProperty("domini")
  private List<DominioACL> domini = null;
  
  @JsonProperty("entrate")
  private List<EntrataACL> entrate = null;
  
  @JsonProperty("idEntrata")
  private String idEntrata = null;
  
  @JsonProperty("soloPropri")
  private Boolean soloPropri = null;
  
    
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
  private List<AutorizzazioniEnum> autorizzazioni = new ArrayList<AutorizzazioniEnum>();
  
  /**
   * Servizio oggetto dell'autorizzazione  * Anagrafica PagoPA  * Anagrafica Creditore  * Anagrafica Applicazioni  * Anagrafica Ruoli  * Pendenze e Pagamenti  * Rendicontazioni e Incassi  * Giornale degli Eventi  * Statistiche  * Configurazione e Manutenzione
   **/
  public ACL servizio(ServizioEnum servizio) {
    this.servizio = servizio;
    return this;
  }

  @JsonProperty("servizio")
  public ServizioEnum getServizio() {
    return servizio;
  }
  public void setServizio(ServizioEnum servizio) {
    this.servizio = servizio;
  }

  /**
   * domini a cui e' limitata l'autorizzazione. Il carattere \\'*
   **/
  public ACL domini(List<DominioACL> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<DominioACL> getDomini() {
    return domini;
  }
  public void setDomini(List<DominioACL> domini) {
    this.domini = domini;
  }

  /**
   * domini a cui e' limitata l'autorizzazione. Il carattere \\'*
   **/
  public ACL entrate(List<EntrataACL> entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public List<EntrataACL> getEntrate() {
    return entrate;
  }
  public void setEntrate(List<EntrataACL> entrate) {
    this.entrate = entrate;
  }

  /**
   * tipologia di entrata a cui e' limitata l'autorizzazione
   **/
  public ACL idEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
    return this;
  }

  @JsonProperty("idEntrata")
  public String getIdEntrata() {
    return idEntrata;
  }
  public void setIdEntrata(String idEntrata) {
    this.idEntrata = idEntrata;
  }

  /**
   * indica se l'autorizzazione e' limitata agli elementi di cui si e' riferimento
   **/
  public ACL soloPropri(Boolean soloPropri) {
    this.soloPropri = soloPropri;
    return this;
  }

  @JsonProperty("soloPropri")
  public Boolean isSoloPropri() {
    return soloPropri;
  }
  public void setSoloPropri(Boolean soloPropri) {
    this.soloPropri = soloPropri;
  }

  /**
   **/
  public ACL autorizzazioni(List<AutorizzazioniEnum> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
    return this;
  }

  @JsonProperty("autorizzazioni")
  public List<AutorizzazioniEnum> getAutorizzazioni() {
    return autorizzazioni;
  }
  public void setAutorizzazioni(List<AutorizzazioniEnum> autorizzazioni) {
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
    ACL ACL = (ACL) o;
    return Objects.equals(servizio, ACL.servizio) &&
        Objects.equals(domini, ACL.domini) &&
        Objects.equals(entrate, ACL.entrate) &&
        Objects.equals(idEntrata, ACL.idEntrata) &&
        Objects.equals(soloPropri, ACL.soloPropri) &&
        Objects.equals(autorizzazioni, ACL.autorizzazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(servizio, domini, entrate, idEntrata, soloPropri, autorizzazioni);
  }

  public static ACL parse(String json) {
    return (ACL) parse(json, ACL.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ACL";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ACL {\n");
    
    sb.append("    servizio: ").append(toIndentedString(servizio)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
    sb.append("    idEntrata: ").append(toIndentedString(idEntrata)).append("\n");
    sb.append("    soloPropri: ").append(toIndentedString(soloPropri)).append("\n");
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



