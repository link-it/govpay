package it.govpay.backoffice.v1.beans;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"tipoConnettore",
"versione",
"principal",
"emailIndirizzi",
"emailSubject",
"emailAllegato",
"downloadBaseUrl",
"tipiPendenza",
"url",
"versioneApi",
"auth",
"contenuti",
})
public class ConnettoreNotificaPagamentiMaggioliJPPA extends JSONSerializable implements IValidable{
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
    
  /**
   * Gets or Sets tipoConnettore
   */
  public enum TipoConnettoreEnum {
    
    
        
            
    EMAIL("EMAIL");
            
        
    

    private String value;

    TipoConnettoreEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoConnettoreEnum fromValue(String text) {
      for (TipoConnettoreEnum b : TipoConnettoreEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("tipoConnettore")
  private TipoConnettoreEnum tipoConnettore = null;
  
  @JsonProperty("versione")
  private String versione = null;
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("emailIndirizzi")
  private List<String> emailIndirizzi = null;
  
  @JsonProperty("emailSubject")
  private String emailSubject = null;
  
  @JsonProperty("emailAllegato")
  private Boolean emailAllegato = null;
  
  @JsonProperty("downloadBaseUrl")
  private String downloadBaseUrl = null;
  
  @JsonProperty("tipiPendenza")
  private List<Object> tipiPendenza = null;
  
  @JsonProperty("url")
  private String url = null;
  
    
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
  
  @JsonProperty("auth")
  private TipoAutenticazione auth = null;
  
  @JsonProperty("contenuti")
  private List<ContenutoNotificaPagamentiGovpay> contenuti = null;
  
  /**
   * Indica se il connettore e' abilitato
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA abilitato(Boolean abilitato) {
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
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA tipoConnettore(TipoConnettoreEnum tipoConnettore) {
    this.tipoConnettore = tipoConnettore;
    return this;
  }

  @JsonProperty("tipoConnettore")
  public TipoConnettoreEnum getTipoConnettore() {
    return tipoConnettore;
  }
  public void setTipoConnettore(TipoConnettoreEnum tipoConnettore) {
    this.tipoConnettore = tipoConnettore;
  }

  /**
   * Versione del servizio.
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA versione(String versione) {
    this.versione = versione;
    return this;
  }

  @JsonProperty("versione")
  public String getVersione() {
    return versione;
  }
  public void setVersione(String versione) {
    this.versione = versione;
  }

  /**
   * principal autenticato dalla chiamata di Maggioli
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA principal(String principal) {
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
   * Indirizzi Email al quale verra' spedito il tracciato
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA emailIndirizzi(List<String> emailIndirizzi) {
    this.emailIndirizzi = emailIndirizzi;
    return this;
  }

  @JsonProperty("emailIndirizzi")
  public List<String> getEmailIndirizzi() {
    return emailIndirizzi;
  }
  public void setEmailIndirizzi(List<String> emailIndirizzi) {
    this.emailIndirizzi = emailIndirizzi;
  }

  /**
   * Subject da inserire nella mail
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA emailSubject(String emailSubject) {
    this.emailSubject = emailSubject;
    return this;
  }

  @JsonProperty("emailSubject")
  public String getEmailSubject() {
    return emailSubject;
  }
  public void setEmailSubject(String emailSubject) {
    this.emailSubject = emailSubject;
  }

  /**
   * Indica se inviare il tracciato come allegato all'email oppure se inserire nel messaggio il link al download
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA emailAllegato(Boolean emailAllegato) {
    this.emailAllegato = emailAllegato;
    return this;
  }

  @JsonProperty("emailAllegato")
  public Boolean EmailAllegato() {
    return emailAllegato;
  }
  public void setEmailAllegato(Boolean emailAllegato) {
    this.emailAllegato = emailAllegato;
  }

  /**
   * URL base del link dove scaricare il tracciato
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA downloadBaseUrl(String downloadBaseUrl) {
    this.downloadBaseUrl = downloadBaseUrl;
    return this;
  }

  @JsonProperty("downloadBaseUrl")
  public String getDownloadBaseUrl() {
    return downloadBaseUrl;
  }
  public void setDownloadBaseUrl(String downloadBaseUrl) {
    this.downloadBaseUrl = downloadBaseUrl;
  }

  /**
   * tipi pendenza da includere nel tracciato
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA tipiPendenza(List<Object> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<Object> getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(List<Object> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  /**
   * URL Base del servizio rest di ricezione dei dati
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA url(String url) {
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
   * Versione delle API di integrazione utilizzate.
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA versioneApi(String versioneApi) {
    this.versioneApi = versioneApi;
    return this;
  }

  @JsonProperty("versioneApi")
  public String getVersioneApi() {
    return this.versioneApi;
  }
  public void setVersioneApi(String versioneApi) {
    this.versioneApi = versioneApi;
  }

  public ConnettoreNotificaPagamentiMaggioliJPPA versioneApiEnum(VersioneApiEnum versioneApiEnum) {
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
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA auth(TipoAutenticazione auth) {
    this.auth = auth;
    return this;
  }

  @JsonProperty("auth")
  public TipoAutenticazione getAuth() {
    return auth;
  }
  public void setAuth(TipoAutenticazione auth) {
    this.auth = auth;
  }

  /**
   * Lista dei contenuti da inviare al servizio REST
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA contenuti(List<ContenutoNotificaPagamentiGovpay> contenuti) {
    this.contenuti = contenuti;
    return this;
  }

  @JsonProperty("contenuti")
  public List<ContenutoNotificaPagamentiGovpay> getContenuti() {
    return contenuti;
  }
  public void setContenuti(List<ContenutoNotificaPagamentiGovpay> contenuti) {
    this.contenuti = contenuti;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnettoreNotificaPagamentiMaggioliJPPA connettoreNotificaPagamentiMaggioliJPPA = (ConnettoreNotificaPagamentiMaggioliJPPA) o;
    return Objects.equals(abilitato, connettoreNotificaPagamentiMaggioliJPPA.abilitato) &&
        Objects.equals(tipoConnettore, connettoreNotificaPagamentiMaggioliJPPA.tipoConnettore) &&
        Objects.equals(versione, connettoreNotificaPagamentiMaggioliJPPA.versione) &&
        Objects.equals(principal, connettoreNotificaPagamentiMaggioliJPPA.principal) &&
        Objects.equals(emailIndirizzi, connettoreNotificaPagamentiMaggioliJPPA.emailIndirizzi) &&
        Objects.equals(emailSubject, connettoreNotificaPagamentiMaggioliJPPA.emailSubject) &&
        Objects.equals(emailAllegato, connettoreNotificaPagamentiMaggioliJPPA.emailAllegato) &&
        Objects.equals(downloadBaseUrl, connettoreNotificaPagamentiMaggioliJPPA.downloadBaseUrl) &&
        Objects.equals(tipiPendenza, connettoreNotificaPagamentiMaggioliJPPA.tipiPendenza) &&
        Objects.equals(url, connettoreNotificaPagamentiMaggioliJPPA.url) &&
        Objects.equals(versioneApi, connettoreNotificaPagamentiMaggioliJPPA.versioneApi) &&
        Objects.equals(auth, connettoreNotificaPagamentiMaggioliJPPA.auth) &&
        Objects.equals(contenuti, connettoreNotificaPagamentiMaggioliJPPA.contenuti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, tipoConnettore, versione, principal, emailIndirizzi, emailSubject, emailAllegato, downloadBaseUrl, tipiPendenza, url, versioneApi, auth, contenuti);
  }

  public static ConnettoreNotificaPagamentiMaggioliJPPA parse(String json) throws ServiceException, ValidationException {
    return (ConnettoreNotificaPagamentiMaggioliJPPA) parse(json, ConnettoreNotificaPagamentiMaggioliJPPA.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreNotificaPagamentiMaggioliJPPA";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreNotificaPagamentiMaggioliJPPA {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    tipoConnettore: ").append(toIndentedString(tipoConnettore)).append("\n");
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    emailIndirizzi: ").append(toIndentedString(emailIndirizzi)).append("\n");
    sb.append("    emailSubject: ").append(toIndentedString(emailSubject)).append("\n");
    sb.append("    emailAllegato: ").append(toIndentedString(emailAllegato)).append("\n");
    sb.append("    downloadBaseUrl: ").append(toIndentedString(downloadBaseUrl)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    versioneApi: ").append(toIndentedString(versioneApi)).append("\n");
    sb.append("    auth: ").append(toIndentedString(auth)).append("\n");
    sb.append("    contenuti: ").append(toIndentedString(contenuti)).append("\n");
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
 			vf.getValidator("tipoConnettore", this.tipoConnettore).notNull();
 			vf.getValidator("principal", this.principal).notNull().minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_USERNAME);
 			
 			switch (this.tipoConnettore) {
 			case EMAIL:
 				vf.getValidator("versione", this.versione).notNull().minLength(1).maxLength(255);
 				if(this.emailIndirizzi != null && !this.emailIndirizzi.isEmpty()) {
 					for (String indirizzo : emailIndirizzi) {
 						vf.getValidator("emailIndirizzi", indirizzo).minLength(1).pattern(CostantiValidazione.PATTERN_EMAIL);
 					}
 					String v = StringUtils.join(this.emailIndirizzi, ",");
 					vf.getValidator("emailIndirizzi", v).maxLength(4000);
 				} else {
 					throw new ValidationException("Il campo emailIndirizzi non deve essere vuoto.");
 				}
 				vf.getValidator("emailSubject", this.emailSubject).minLength(1).maxLength(4000);
 				vf.getValidator("emailAllegato", this.emailAllegato).notNull();
 				if(!this.emailAllegato) {
 					vf.getValidator("downloadBaseUrl", this.downloadBaseUrl).notNull().pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
 				}
 				vf.getValidator("url", this.url).notNull().pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
				vf.getValidator("versioneApi", this.versioneApi).notNull();
				try {
					VersioneApiEnum v = VersioneApiEnum.fromValue(this.versioneApi);
					if(v==null) throw new IllegalArgumentException();
				} catch (IllegalArgumentException e) {
					throw new ValidationException("Il valore [" + this.versioneApi + "] del campo versioneApi non corrisponde con uno dei valori consentiti: " + Arrays.asList(VersioneApiEnum.values()));
				}
				
				vf.getValidator("auth", this.auth).validateFields();
 				break;
 			}
 			
 			if(this.tipiPendenza != null && !this.tipiPendenza.isEmpty()) {
 				ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
 				for (Object object : this.tipiPendenza) {
 					if(object instanceof String) {
 						String idTipoPendenza = (String) object;
 						if(!idTipoPendenza.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR))
 							validatoreId.validaIdTipoVersamento("tipiPendenza", idTipoPendenza);
 					} else if(object instanceof TipoPendenzaProfiloIndex) {
 						TipoPendenzaProfiloIndex tipoPendenzaProfiloPost = (TipoPendenzaProfiloIndex) object;
 						if(!tipoPendenzaProfiloPost.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR))
 							tipoPendenzaProfiloPost.validate();
 					} else if(object instanceof java.util.LinkedHashMap) {
 						java.util.LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) object;
 						
 						TipoPendenzaProfiloIndex tipoPendenzaProfiloPost = new TipoPendenzaProfiloIndex();
 						if(map.containsKey("idTipoPendenza"))
 							tipoPendenzaProfiloPost.setIdTipoPendenza((String) map.get("idTipoPendenza"));
 						if(map.containsKey("descrizione")) {
 							tipoPendenzaProfiloPost.setDescrizione((String) map.get("descrizione"));
 						}
 						
 						if(tipoPendenzaProfiloPost.getIdTipoPendenza() == null)
 							validatoreId.validaIdDominio("idTipoPendenza", tipoPendenzaProfiloPost.getIdTipoPendenza());
 						if(!tipoPendenzaProfiloPost.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR))
 							tipoPendenzaProfiloPost.validate();
 					} else {
 						throw new ValidationException("Tipo non valido per il campo domini");
 					}
 				}
 			} else {
 				throw new ValidationException("Indicare almeno un valore nel campo tipiPendenza");
 			}
 		}
 	}
}



