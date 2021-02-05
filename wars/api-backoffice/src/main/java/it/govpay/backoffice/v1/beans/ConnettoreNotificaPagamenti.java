package it.govpay.backoffice.v1.beans;


import java.util.List;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"codiceIPA",
"tipoConnettore",
"versioneCsv",
"webServiceUrl",
"webServiceAuth",
"emailIndirizzo",
"fileSystemPath",
"tipiPendenza",
})
public class ConnettoreNotificaPagamenti extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("codiceIPA")
  private String codiceIPA = null;
  
    
  /**
   * Gets or Sets tipoConnettore
   */
  public enum TipoConnettoreEnum {
    
    
        
            
    WEBSERVICE("WEBSERVICE"),
    
            
    EMAIL("EMAIL"),
    
            
    FILESYSTEM("FILESYSTEM");
            
        
    

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
  
    
  /**
   * Versione del CSV prodotto.
   */
  public enum VersioneCsvEnum {
    
    
        
            
    _0("1.0");
            
        
    

    private String value;

    VersioneCsvEnum(String value) {
      this.value = value;
    }

    @Override
    @com.fasterxml.jackson.annotation.JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static VersioneCsvEnum fromValue(String text) {
      for (VersioneCsvEnum b : VersioneCsvEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("versioneCsv")
  private VersioneCsvEnum versioneCsv = null;
  
  @JsonProperty("webServiceUrl")
  private String webServiceUrl = null;
  
  @JsonProperty("webServiceAuth")
  private TipoAutenticazione webServiceAuth = null;
  
  @JsonProperty("emailIndirizzo")
  private String emailIndirizzo = null;
  
  @JsonProperty("fileSystemPath")
  private String fileSystemPath = null;
  
  @JsonProperty("tipiPendenza")
  private List<TipoPendenzaProfiloIndex> tipiPendenza = null;
  
  /**
   * Indica se il connettore e' abilitato
   **/
  public ConnettoreNotificaPagamenti abilitato(Boolean abilitato) {
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
   * Codice IPA
   **/
  public ConnettoreNotificaPagamenti codiceIPA(String codiceIPA) {
    this.codiceIPA = codiceIPA;
    return this;
  }

  @JsonProperty("codiceIPA")
  public String getCodiceIPA() {
    return codiceIPA;
  }
  public void setCodiceIPA(String codiceIPA) {
    this.codiceIPA = codiceIPA;
  }

  /**
   **/
  public ConnettoreNotificaPagamenti tipoConnettore(TipoConnettoreEnum tipoConnettore) {
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
   * Versione del CSV prodotto.
   **/
  public ConnettoreNotificaPagamenti versioneCsv(VersioneCsvEnum versioneCsv) {
    this.versioneCsv = versioneCsv;
    return this;
  }

  @JsonProperty("versioneCsv")
  public VersioneCsvEnum getVersioneCsv() {
    return versioneCsv;
  }
  public void setVersioneCsv(VersioneCsvEnum versioneCsv) {
    this.versioneCsv = versioneCsv;
  }

  /**
   * Dati di integrazione al servizio web tracciati
   **/
  public ConnettoreNotificaPagamenti webServiceUrl(String webServiceUrl) {
    this.webServiceUrl = webServiceUrl;
    return this;
  }

  @JsonProperty("webServiceUrl")
  public String getWebServiceUrl() {
    return webServiceUrl;
  }
  public void setWebServiceUrl(String webServiceUrl) {
    this.webServiceUrl = webServiceUrl;
  }

  /**
   **/
  public ConnettoreNotificaPagamenti webServiceAuth(TipoAutenticazione webServiceAuth) {
    this.webServiceAuth = webServiceAuth;
    return this;
  }

  @JsonProperty("webServiceAuth")
  public TipoAutenticazione getWebServiceAuth() {
    return webServiceAuth;
  }
  public void setWebServiceAuth(TipoAutenticazione webServiceAuth) {
    this.webServiceAuth = webServiceAuth;
  }

  /**
   * Indirizzo Email al quale verra' spedito il tracciato
   **/
  public ConnettoreNotificaPagamenti emailIndirizzo(String emailIndirizzo) {
    this.emailIndirizzo = emailIndirizzo;
    return this;
  }

  @JsonProperty("emailIndirizzo")
  public String getEmailIndirizzo() {
    return emailIndirizzo;
  }
  public void setEmailIndirizzo(String emailIndirizzo) {
    this.emailIndirizzo = emailIndirizzo;
  }

  /**
   * Path nel quale verra' salvato il tracciato
   **/
  public ConnettoreNotificaPagamenti fileSystemPath(String fileSystemPath) {
    this.fileSystemPath = fileSystemPath;
    return this;
  }

  @JsonProperty("fileSystemPath")
  public String getFileSystemPath() {
    return fileSystemPath;
  }
  public void setFileSystemPath(String fileSystemPath) {
    this.fileSystemPath = fileSystemPath;
  }

  /**
   **/
  public ConnettoreNotificaPagamenti tipiPendenza(List<TipoPendenzaProfiloIndex> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<TipoPendenzaProfiloIndex> getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(List<TipoPendenzaProfiloIndex> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnettoreNotificaPagamenti connettoreNotificaPagamenti = (ConnettoreNotificaPagamenti) o;
    return Objects.equals(abilitato, connettoreNotificaPagamenti.abilitato) &&
        Objects.equals(codiceIPA, connettoreNotificaPagamenti.codiceIPA) &&
        Objects.equals(tipoConnettore, connettoreNotificaPagamenti.tipoConnettore) &&
        Objects.equals(versioneCsv, connettoreNotificaPagamenti.versioneCsv) &&
        Objects.equals(webServiceUrl, connettoreNotificaPagamenti.webServiceUrl) &&
        Objects.equals(webServiceAuth, connettoreNotificaPagamenti.webServiceAuth) &&
        Objects.equals(emailIndirizzo, connettoreNotificaPagamenti.emailIndirizzo) &&
        Objects.equals(fileSystemPath, connettoreNotificaPagamenti.fileSystemPath) &&
        Objects.equals(tipiPendenza, connettoreNotificaPagamenti.tipiPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, codiceIPA, tipoConnettore, versioneCsv, webServiceUrl, webServiceAuth, emailIndirizzo, fileSystemPath, tipiPendenza);
  }

  public static ConnettoreNotificaPagamenti parse(String json) throws ServiceException, ValidationException {
    return (ConnettoreNotificaPagamenti) parse(json, ConnettoreNotificaPagamenti.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreNotificaPagamenti";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreNotificaPagamenti {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    codiceIPA: ").append(toIndentedString(codiceIPA)).append("\n");
    sb.append("    tipoConnettore: ").append(toIndentedString(tipoConnettore)).append("\n");
    sb.append("    versioneCsv: ").append(toIndentedString(versioneCsv)).append("\n");
    sb.append("    webServiceUrl: ").append(toIndentedString(webServiceUrl)).append("\n");
    sb.append("    webServiceAuth: ").append(toIndentedString(webServiceAuth)).append("\n");
    sb.append("    emailIndirizzo: ").append(toIndentedString(emailIndirizzo)).append("\n");
    sb.append("    fileSystemPath: ").append(toIndentedString(fileSystemPath)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
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
			vf.getValidator("versioneCsv", this.versioneCsv).notNull();
			vf.getValidator("codiceIPA", this.codiceIPA).notNull().minLength(1).maxLength(4000);
			
			switch (this.tipoConnettore) {
			case EMAIL:
				vf.getValidator("emailIndirizzo", this.emailIndirizzo).notNull().minLength(1).maxLength(4000);
				break;
			case FILESYSTEM:
				vf.getValidator("fileSystemPath", this.fileSystemPath).notNull().minLength(1).maxLength(4000);
				break;
			case WEBSERVICE:
				vf.getValidator("webServiceUrl", this.webServiceUrl).notNull().minLength(1).maxLength(4000).isUrl();
				vf.getValidator("webServiceAuth", this.webServiceAuth).validateFields();
				break;
			}
			
			if(this.tipiPendenza != null && !this.tipiPendenza.isEmpty()) {
				ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
				for (TipoPendenzaProfiloIndex idTipoPendenza : this.tipiPendenza) {
					if(!idTipoPendenza.getIdTipoPendenza().equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR))
						validatoreId.validaIdTipoVersamento("tipiPendenza", idTipoPendenza.getIdTipoPendenza());
				}
			}
		}
	}
}



