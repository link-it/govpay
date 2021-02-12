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
"emailIndirizzo",
"emailServer",
"fileSystemPath",
"tipiPendenza",
})
public class ConnettoreNotificaPagamentiMyPivot extends JSONSerializable implements IValidable {
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("codiceIPA")
  private String codiceIPA = null;
  
    
  /**
   * Gets or Sets tipoConnettore
   */
  public enum TipoConnettoreEnum {
    
    
        
            
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
  
  @JsonProperty("versioneCsv")
  private String versioneCsv = null;
  
  @JsonProperty("emailIndirizzo")
  private String emailIndirizzo = null;
  
  @JsonProperty("emailServer")
  private Mailserver emailServer = null;
  
  @JsonProperty("fileSystemPath")
  private String fileSystemPath = null;
  
  @JsonProperty("tipiPendenza")
  private List<TipoPendenzaProfiloIndex> tipiPendenza = null;
  
  /**
   * Indica se il connettore e' abilitato
   **/
  public ConnettoreNotificaPagamentiMyPivot abilitato(Boolean abilitato) {
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
  public ConnettoreNotificaPagamentiMyPivot codiceIPA(String codiceIPA) {
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
  public ConnettoreNotificaPagamentiMyPivot tipoConnettore(TipoConnettoreEnum tipoConnettore) {
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
  public ConnettoreNotificaPagamentiMyPivot versioneCsv(String versioneCsv) {
    this.versioneCsv = versioneCsv;
    return this;
  }

  @JsonProperty("versioneCsv")
  public String getVersioneCsv() {
    return versioneCsv;
  }
  public void setVersioneCsv(String versioneCsv) {
    this.versioneCsv = versioneCsv;
  }

  /**
   * Indirizzo Email al quale verra' spedito il tracciato
   **/
  public ConnettoreNotificaPagamentiMyPivot emailIndirizzo(String emailIndirizzo) {
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
   **/
  public ConnettoreNotificaPagamentiMyPivot emailServer(Mailserver emailServer) {
    this.emailServer = emailServer;
    return this;
  }

  @JsonProperty("emailServer")
  public Mailserver getEmailServer() {
    return emailServer;
  }
  public void setEmailServer(Mailserver emailServer) {
    this.emailServer = emailServer;
  }

  /**
   * Path nel quale verra' salvato il tracciato
   **/
  public ConnettoreNotificaPagamentiMyPivot fileSystemPath(String fileSystemPath) {
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
  public ConnettoreNotificaPagamentiMyPivot tipiPendenza(List<TipoPendenzaProfiloIndex> tipiPendenza) {
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
    ConnettoreNotificaPagamentiMyPivot connettoreNotificaPagamentiMyPivot = (ConnettoreNotificaPagamentiMyPivot) o;
    return Objects.equals(abilitato, connettoreNotificaPagamentiMyPivot.abilitato) &&
        Objects.equals(codiceIPA, connettoreNotificaPagamentiMyPivot.codiceIPA) &&
        Objects.equals(tipoConnettore, connettoreNotificaPagamentiMyPivot.tipoConnettore) &&
        Objects.equals(versioneCsv, connettoreNotificaPagamentiMyPivot.versioneCsv) &&
        Objects.equals(emailIndirizzo, connettoreNotificaPagamentiMyPivot.emailIndirizzo) &&
        Objects.equals(emailServer, connettoreNotificaPagamentiMyPivot.emailServer) &&
        Objects.equals(fileSystemPath, connettoreNotificaPagamentiMyPivot.fileSystemPath) &&
        Objects.equals(tipiPendenza, connettoreNotificaPagamentiMyPivot.tipiPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, codiceIPA, tipoConnettore, versioneCsv, emailIndirizzo, emailServer, fileSystemPath, tipiPendenza);
  }

  public static ConnettoreNotificaPagamentiMyPivot parse(String json) throws ServiceException, ValidationException {
    return (ConnettoreNotificaPagamentiMyPivot) parse(json, ConnettoreNotificaPagamentiMyPivot.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreNotificaPagamentiMyPivot";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreNotificaPagamentiMyPivot {\n");
    
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    codiceIPA: ").append(toIndentedString(codiceIPA)).append("\n");
    sb.append("    tipoConnettore: ").append(toIndentedString(tipoConnettore)).append("\n");
    sb.append("    versioneCsv: ").append(toIndentedString(versioneCsv)).append("\n");
    sb.append("    emailIndirizzo: ").append(toIndentedString(emailIndirizzo)).append("\n");
    sb.append("    emailServer: ").append(toIndentedString(emailServer)).append("\n");
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
			vf.getValidator("versioneCsv", this.versioneCsv).notNull().minLength(1).maxLength(255);
			vf.getValidator("codiceIPA", this.codiceIPA).notNull().minLength(1).maxLength(4000);
			
			switch (this.tipoConnettore) {
			case EMAIL:
				vf.getValidator("emailIndirizzo", this.emailIndirizzo).notNull().minLength(1).maxLength(4000);
				vf.getValidator("emailServer", this.emailServer).notNull().validateFields();
				break;
			case FILESYSTEM:
				vf.getValidator("fileSystemPath", this.fileSystemPath).notNull().minLength(1).maxLength(4000);
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



