package it.govpay.backoffice.v1.beans;


import java.util.Arrays;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"versioneApi",
"principal",
"url",
"username",
"password",
"ruolo",
"company",
})
public class ConnettoreNetPay extends JSONSerializable implements IValidable{
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
    
  /**
   * Versione delle API di integrazione utilizzate.
   */
  public enum VersioneApiEnum {
    
    
        
            
    V1("SOAP v1");
            
        
    

    private String value;

    VersioneApiEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static VersioneApiEnum fromValue(String text) {
      for (VersioneApiEnum b : VersioneApiEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
    public static VersioneApiEnum fromName(String text) {
		for (VersioneApiEnum b : VersioneApiEnum.values()) {
			if (String.valueOf(b.toNameString()).equals(text)) {
				return b;
			}
		}
		return null;
	}
    
    public String toNameString() {
		switch(this) {
		case V1: return "SOAP_1";
		default:  return "";
		}
	}
  }

    
    
  private VersioneApiEnum versioneApiEnum = null;
  
  @JsonProperty("versioneApi")
  private String versioneApi = null;
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("username")
  private String username = null;
  
  @JsonProperty("password")
  private String password = null;
  
  @JsonProperty("ruolo")
  private String ruolo = null;
  
  @JsonProperty("company")
  private String company = null;
  
  /**
   * Indica se il connettore e' abilitato
   **/
  public ConnettoreNetPay abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean Abilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * Versione delle API di integrazione utilizzate.
   **/
  public ConnettoreNetPay versioneApi(String versioneApi) {
    this.versioneApi = versioneApi;
    return this;
  }

  @JsonProperty("versioneApi")
  public String getVersioneApi() {
    return versioneApi;
  }
  public void setVersioneApi(String versioneApi) {
    this.versioneApi = versioneApi;
  }

  public ConnettoreNetPay versioneApiEnum(VersioneApiEnum versioneApiEnum) {
    this.versioneApiEnum = versioneApiEnum;
    return this;
  }

  @JsonIgnore
  public VersioneApiEnum getVersioneApiEnum() {
    return versioneApiEnum;
  }
  public void setVersioneApiEnum(VersioneApiEnum versioneApiEnum) {
    this.versioneApiEnum = versioneApiEnum;
  }
  
  /**
   * principal autenticato dalla chiamata di Net@Pay
   **/
  public ConnettoreNetPay principal(String principal) {
    this.principal = principal;
    return this;
  }

  @JsonProperty("principal")
  public String getPrincipal() {
    return principal;
  }
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   * endpoint del servizio Net@Pay per la Notifica dei pagamenti avvenuti
   **/
  public ConnettoreNetPay url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * username per l'autenticazione HTTP-Basic del servizio Net@Pay per la Notifica dei pagamenti avvenuti
   **/
  public ConnettoreNetPay username(String username) {
    this.username = username;
    return this;
  }

  @JsonProperty("username")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * password per l'autenticazione HTTP-Basic del servizio Net@Pay per la Notifica dei pagamenti avvenuti
   **/
  public ConnettoreNetPay password(String password) {
    this.password = password;
    return this;
  }

  @JsonProperty("password")
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Identificativo del ruolo su Net@Pay, inviato all'interno di un header HTTP
   **/
  public ConnettoreNetPay ruolo(String ruolo) {
    this.ruolo = ruolo;
    return this;
  }

  @JsonProperty("ruolo")
  public String getRuolo() {
    return ruolo;
  }
  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }

  /**
   * Identificativo dell'Ente Creditore su Net@Pay, inviato all'interno di un header HTTP
   **/
  public ConnettoreNetPay company(String company) {
    this.company = company;
    return this;
  }

  @JsonProperty("company")
  public String getCompany() {
    return company;
  }
  public void setCompany(String company) {
    this.company = company;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnettoreNetPay connettoreNetPay = (ConnettoreNetPay) o;
    return Objects.equals(abilitato, connettoreNetPay.abilitato) &&
        Objects.equals(versioneApi, connettoreNetPay.versioneApi) &&
        Objects.equals(principal, connettoreNetPay.principal) &&
        Objects.equals(url, connettoreNetPay.url) &&
        Objects.equals(username, connettoreNetPay.username) &&
        Objects.equals(password, connettoreNetPay.password) &&
        Objects.equals(ruolo, connettoreNetPay.ruolo) &&
        Objects.equals(company, connettoreNetPay.company);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, versioneApi, principal, url, username, password, ruolo, company);
  }

  public static ConnettoreNetPay parse(String json) throws ServiceException, ValidationException {
    return (ConnettoreNetPay) parse(json, ConnettoreNetPay.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreNetPay";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreNetPay {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    versioneApi: ").append(toIndentedString(versioneApi)).append("\n");
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    ruolo: ").append(toIndentedString(ruolo)).append("\n");
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
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
		vf.getValidator("abilitato", this.abilitato).notNull();
		
		if(this.abilitato) {
			vf.getValidator("principal", this.principal).notNull().minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_USERNAME);
			vf.getValidator("url", this.url).notNull().pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
			vf.getValidator("username", this.username).notNull().minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_USERNAME);
			vf.getValidator("password", this.password).notNull().minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_PASSWORD_DEFAULT);
			vf.getValidator("ruolo", this.ruolo).notNull().minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_NO_WHITE_SPACES);
			vf.getValidator("company", this.company).notNull().minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_NO_WHITE_SPACES);
			vf.getValidator("versioneApi", this.versioneApi).notNull();
				try {
					VersioneApiEnum v = VersioneApiEnum.fromValue(this.versioneApi);
					if(v==null) throw new IllegalArgumentException();
				} catch (IllegalArgumentException e) {
					throw new ValidationException("Il valore [" + this.versioneApi + "] del campo versioneApi non corrisponde con uno dei valori consentiti: " + Arrays.asList(VersioneApiEnum.values()));
				}
		}
	}
}



