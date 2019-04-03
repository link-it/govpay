package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"servizio",
"autorizzazioni",
})
public class AclPost extends it.govpay.core.beans.JSONSerializable  implements IValidable{
  
    
  /**
   * Servizio oggetto dell'autorizzazione.
   */
  public enum ServizioEnum {
    
    
        
            
    ANAGRAFICA_PAGOPA("Anagrafica PagoPA"),
    
            
    ANAGRAFICA_CREDITORE("Anagrafica Creditore"),
    
            
    ANAGRAFICA_APPLICAZIONI("Anagrafica Applicazioni"),
    
            
    ANAGRAFICA_RUOLI("Anagrafica Ruoli"),
    
            
    PAGAMENTI("Pagamenti"),
    
            
    PENDENZE("Pendenze"),
    
            
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

    
  private ServizioEnum servizioEnum = null;
    
  @JsonProperty("servizio")
  private String servizio = null;
    /**
   * Gets or Sets autorizzazioni
   */
  public enum AutorizzazioniEnum {
    
    
        
            
    LETTURA("R"),
    
            
    SCRITTURA("W"),
    
            
    ESECUZIONE("X");
            
        
    

    private String value;

    AutorizzazioniEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
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
   * Servizio oggetto dell'autorizzazione.
   **/
  public AclPost servizio(String servizio) {
    this.servizio = servizio;
    return this;
  }

  @JsonProperty("servizio")
  public String getServizio() {
    return this.servizio;
  }
  public void setServizio(String servizio) {
    this.servizio = servizio;
  }

  public AclPost servizioEnum(ServizioEnum servizioEnum) {
    this.servizioEnum = servizioEnum;
    return this;
  }

  public ServizioEnum getServizioEnum() {
    return servizioEnum;
  }
  public void setServizioEnum(ServizioEnum servizioEnum) {
    this.servizioEnum = servizioEnum;
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
    return Objects.equals(this.servizio, aclPost.servizio) &&
        Objects.equals(this.autorizzazioni, aclPost.autorizzazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.servizio, this.autorizzazioni);
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
  
  @Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("servizio", this.servizio).notNull();
		
		if(ServizioEnum.fromValue(this.servizio) == null)
			throw new ValidationException("Codifica inesistente per servizio. Valore fornito [" + this.servizio + "] valori possibili " + ArrayUtils.toString(ServizioEnum.values()));

		
		if(this.autorizzazioni == null || this.autorizzazioni.isEmpty())
			throw new ValidationException("Il campo " + "autorizzazioni" + " non deve essere vuoto.");
		
		for (String string : this.autorizzazioni) {
			if(AutorizzazioniEnum.fromValue(string) == null)
				throw new ValidationException("Codifica inesistente per autorizzazioni. Valore fornito [" + string + "] valori possibili " + ArrayUtils.toString(AutorizzazioniEnum.values()));
		}
		
	}
}



