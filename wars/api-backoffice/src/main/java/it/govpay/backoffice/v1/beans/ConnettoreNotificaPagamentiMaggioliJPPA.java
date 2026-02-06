/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.backoffice.v1.beans;


import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"abilitato",
"inviaTracciatoEsito",
"fileSystemPath",
"emailIndirizzi",
"emailSubject",
"emailAllegato",
"downloadBaseUrl",
"url",
"auth",
"dataUltimaRT",
})
public class ConnettoreNotificaPagamentiMaggioliJPPA extends JSONSerializable implements IValidable{
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("inviaTracciatoEsito")
  private Boolean inviaTracciatoEsito = null;
  
  @JsonProperty("fileSystemPath")
  private String fileSystemPath = null;
  
  @JsonProperty("emailIndirizzi")
  private List<String> emailIndirizzi = null;
  
  @JsonProperty("emailSubject")
  private String emailSubject = null;
  
  @JsonProperty("emailAllegato")
  private Boolean emailAllegato = null;
  
  @JsonProperty("downloadBaseUrl")
  private String downloadBaseUrl = null;
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("auth")
  private TipoAutenticazione auth = null;
  
  @JsonProperty("dataUltimaRT")
  private Date dataUltimaRT = null;
  
  /**
   * Indica se il connettore e' abilitato
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean getAbilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * Indica se inviare il tracciato di esito
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA inviaTracciatoEsito(Boolean inviaTracciatoEsito) {
    this.inviaTracciatoEsito = inviaTracciatoEsito;
    return this;
  }

  @JsonProperty("inviaTracciatoEsito")
  public Boolean getInviaTracciatoEsito() {
    return inviaTracciatoEsito;
  }
  public void setInviaTracciatoEsito(Boolean inviaTracciatoEsito) {
    this.inviaTracciatoEsito = inviaTracciatoEsito;
  }

  /**
   * Path nel quale verra' salvato il tracciato
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA fileSystemPath(String fileSystemPath) {
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
  public Boolean getEmailAllegato() {
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
   * Date time dell'ultima RT processata con successo
   **/
  public ConnettoreNotificaPagamentiMaggioliJPPA dataUltimaRT(Date dataUltimaRT) {
    this.dataUltimaRT = dataUltimaRT;
    return this;
  }

  @JsonProperty("dataUltimaRT")
  public Date getDataUltimaRT() {
    return dataUltimaRT;
  }
  public void setDataUltimaRT(Date dataUltimaRT) {
    this.dataUltimaRT = dataUltimaRT;
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
        Objects.equals(inviaTracciatoEsito, connettoreNotificaPagamentiMaggioliJPPA.inviaTracciatoEsito) &&
        Objects.equals(fileSystemPath, connettoreNotificaPagamentiMaggioliJPPA.fileSystemPath) &&
        Objects.equals(emailIndirizzi, connettoreNotificaPagamentiMaggioliJPPA.emailIndirizzi) &&
        Objects.equals(emailSubject, connettoreNotificaPagamentiMaggioliJPPA.emailSubject) &&
        Objects.equals(emailAllegato, connettoreNotificaPagamentiMaggioliJPPA.emailAllegato) &&
        Objects.equals(downloadBaseUrl, connettoreNotificaPagamentiMaggioliJPPA.downloadBaseUrl) &&
        Objects.equals(url, connettoreNotificaPagamentiMaggioliJPPA.url) &&
        Objects.equals(auth, connettoreNotificaPagamentiMaggioliJPPA.auth) &&
        Objects.equals(dataUltimaRT, connettoreNotificaPagamentiMaggioliJPPA.dataUltimaRT);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitato, inviaTracciatoEsito, fileSystemPath, emailIndirizzi, emailSubject, emailAllegato, downloadBaseUrl, url, auth, dataUltimaRT);
  }

  public static ConnettoreNotificaPagamentiMaggioliJPPA parse(String json) throws IOException {
    return parse(json, ConnettoreNotificaPagamentiMaggioliJPPA.class);
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
    sb.append("    inviaTracciatoEsito: ").append(toIndentedString(inviaTracciatoEsito)).append("\n");
    sb.append("    fileSystemPath: ").append(toIndentedString(fileSystemPath)).append("\n");
    sb.append("    emailIndirizzi: ").append(toIndentedString(emailIndirizzi)).append("\n");
    sb.append("    emailSubject: ").append(toIndentedString(emailSubject)).append("\n");
    sb.append("    emailAllegato: ").append(toIndentedString(emailAllegato)).append("\n");
    sb.append("    downloadBaseUrl: ").append(toIndentedString(downloadBaseUrl)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    auth: ").append(toIndentedString(auth)).append("\n");
    sb.append("    dataUltimaRT: ").append(toIndentedString(dataUltimaRT)).append("\n");
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

 		if(Boolean.TRUE.equals(this.abilitato)) {
 			// Quando abilitato e' true, URL e auth sono obbligatori
 			vf.getValidator("url", this.url).notNull().pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");

			if (this.auth == null) {
				throw new ValidationException("Il campo auth non deve essere nullo.");
 			}

			// validazione credenziali httpbasic
			if(this.auth.getUsername() == null) {
				throw new ValidationException("Il campo username non deve essere nullo.");
			}

			if (this.auth.getPassword() == null) {
				throw new ValidationException("Il campo password non deve essere nullo.");
			}

			vf.getValidator("auth.username", this.auth.getUsername()).notNull().minLength(1).maxLength(255);
			vf.getValidator("auth.password", this.auth.getPassword()).notNull().minLength(1).maxLength(255);

 			vf.getValidator("inviaTracciatoEsito", this.inviaTracciatoEsito).notNull();

 			if(Boolean.TRUE.equals(this.inviaTracciatoEsito)) {
 				vf.getValidator("fileSystemPath", this.fileSystemPath).notNull().minLength(1).maxLength(4000);
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
 				if(Boolean.FALSE.equals(this.emailAllegato)) {
 					vf.getValidator("downloadBaseUrl", this.downloadBaseUrl).notNull().pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
 				}
 			}
 		} else {
 			// Quando abilitato e' false, URL e auth sono opzionali ma se forniti devono essere validi
 			if(this.url != null) {
 				vf.getValidator("url", this.url).pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
 			}

 			if(this.auth != null) {
 				if(this.auth.getUsername() != null) {
 					vf.getValidator("auth.username", this.auth.getUsername()).minLength(1).maxLength(255);
 				}
 				if(this.auth.getPassword() != null) {
 					vf.getValidator("auth.password", this.auth.getPassword()).minLength(1).maxLength(255);
 				}
 			}
 		}
 	}
}



