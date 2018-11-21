package it.govpay.backoffice.v1.beans;

import java.util.Arrays;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"url",
"versioneApi",
"auth",
})
public class Connector extends JSONSerializable implements IValidable {
  
  @JsonProperty("url")
  private String url = null;
  
  @JsonProperty("versioneApi")
  private String versioneApi = null;
  
  @JsonProperty("auth")
  private TipoAutenticazione auth = null;
  
  /**
   * Dati di integrazione ad un servizio web
   **/
  public Connector url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return this.url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Versione delle API di integrazione utilizzate. Elenco disponibile in /enumerazioni/versioneConnettore
   **/
  public Connector versioneApi(String versioneApi) {
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

  /**
   **/
  public Connector auth(TipoAutenticazione auth) {
    this.auth = auth;
    return this;
  }

  @JsonProperty("auth")
  public TipoAutenticazione getAuth() {
    return this.auth;
  }
  public void setAuth(TipoAutenticazione auth) {
    this.auth = auth;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Connector connector = (Connector) o;
    return Objects.equals(this.url, connector.url) &&
        Objects.equals(this.versioneApi, connector.versioneApi) &&
        Objects.equals(this.auth, connector.auth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.url, this.versioneApi, this.auth);
  }

  public static Connector parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
    return parse(json, Connector.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connector";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Connector {\n");
    
    sb.append("    url: ").append(this.toIndentedString(this.url)).append("\n");
    sb.append("    versioneApi: ").append(this.toIndentedString(this.versioneApi)).append("\n");
    sb.append("    auth: ").append(this.toIndentedString(this.auth)).append("\n");
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
		vf.getValidator("url", this.url).pattern("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		vf.getValidator("versioneApi", this.versioneApi).notNull();
		try {
			VersioneApiEnum v = VersioneApiEnum.fromValue(this.versioneApi);
			if(v==null) throw new IllegalArgumentException();
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Il valore [" + this.versioneApi + "] del campo versioneApi corrisponde con uno dei valori consentiti: " + Arrays.asList(VersioneApiEnum.values()));
		}
		if(this.auth != null)
			vf.getValidator("auth", this.auth).validateFields();
	}
}



