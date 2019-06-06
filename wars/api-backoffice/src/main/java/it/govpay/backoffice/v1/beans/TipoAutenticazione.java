package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
	"username",
	"password",
	"tipo",
	"ksLocation",
	"ksPassword",
	"tsLocation",
	"tsPassword",
})
public class TipoAutenticazione extends it.govpay.core.beans.JSONSerializable implements IValidable {

	@JsonProperty("username")
	private String username = null;

	@JsonProperty("password")
	private String password = null;


	/**
	 * Gets or Sets tipo
	 */
	public enum TipoEnum {




		CLIENT("Client"),


		SERVER("Server");




		private String value;

		TipoEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(this.value);
		}

		public static TipoEnum fromValue(String text) {
			for (TipoEnum b : TipoEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}



	@JsonProperty("tipo")
	private TipoEnum tipo = null;

	@JsonProperty("ksLocation")
	private String ksLocation = null;

	@JsonProperty("ksPassword")
	private String ksPassword = null;

	@JsonProperty("tsLocation")
	private String tsLocation = null;

	@JsonProperty("tsPassword")
	private String tsPassword = null;

	/**
	 **/
	public TipoAutenticazione username(String username) {
		this.username = username;
		return this;
	}

	@JsonProperty("username")
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 **/
	public TipoAutenticazione password(String password) {
		this.password = password;
		return this;
	}

	@JsonProperty("password")
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 **/
	public TipoAutenticazione tipo(TipoEnum tipo) {
		this.tipo = tipo;
		return this;
	}

	@JsonProperty("tipo")
	public TipoEnum getTipo() {
		return this.tipo;
	}
	public void setTipo(TipoEnum tipo) {
		this.tipo = tipo;
	}

	/**
	 * Location del keystore
	 **/
	public TipoAutenticazione ksLocation(String ksLocation) {
		this.ksLocation = ksLocation;
		return this;
	}

	@JsonProperty("ksLocation")
	public String getKsLocation() {
		return this.ksLocation;
	}
	public void setKsLocation(String ksLocation) {
		this.ksLocation = ksLocation;
	}

	/**
	 * Password del keystore
	 **/
	public TipoAutenticazione ksPassword(String ksPassword) {
		this.ksPassword = ksPassword;
		return this;
	}

	@JsonProperty("ksPassword")
	public String getKsPassword() {
		return this.ksPassword;
	}
	public void setKsPassword(String ksPassword) {
		this.ksPassword = ksPassword;
	}

	/**
	 * Location del truststore
	 **/
	public TipoAutenticazione tsLocation(String tsLocation) {
		this.tsLocation = tsLocation;
		return this;
	}

	@JsonProperty("tsLocation")
	public String getTsLocation() {
		return this.tsLocation;
	}
	public void setTsLocation(String tsLocation) {
		this.tsLocation = tsLocation;
	}

	/**
	 * Password del truststore
	 **/
	public TipoAutenticazione tsPassword(String tsPassword) {
		this.tsPassword = tsPassword;
		return this;
	}

	@JsonProperty("tsPassword")
	public String getTsPassword() {
		return this.tsPassword;
	}
	public void setTsPassword(String tsPassword) {
		this.tsPassword = tsPassword;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		TipoAutenticazione tipoAutenticazione = (TipoAutenticazione) o;
		return Objects.equals(this.username, tipoAutenticazione.username) &&
				Objects.equals(this.password, tipoAutenticazione.password) &&
				Objects.equals(this.tipo, tipoAutenticazione.tipo) &&
				Objects.equals(this.ksLocation, tipoAutenticazione.ksLocation) &&
				Objects.equals(this.ksPassword, tipoAutenticazione.ksPassword) &&
				Objects.equals(this.tsLocation, tipoAutenticazione.tsLocation) &&
				Objects.equals(this.tsPassword, tipoAutenticazione.tsPassword);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.username, this.password, this.tipo, this.ksLocation, this.ksPassword, this.tsLocation, this.tsPassword);
	}

	public static TipoAutenticazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
		return parse(json, TipoAutenticazione.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "tipoAutenticazione";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TipoAutenticazione {\n");

		sb.append("    username: ").append(this.toIndentedString(this.username)).append("\n");
		sb.append("    password: ").append(this.toIndentedString(this.password)).append("\n");
		sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
		sb.append("    ksLocation: ").append(this.toIndentedString(this.ksLocation)).append("\n");
		sb.append("    ksPassword: ").append(this.toIndentedString(this.ksPassword)).append("\n");
		sb.append("    tsLocation: ").append(this.toIndentedString(this.tsLocation)).append("\n");
		sb.append("    tsPassword: ").append(this.toIndentedString(this.tsPassword)).append("\n");
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
		
		// validazione credenziali httpbasic
		if(this.username != null || this.password != null) {
			vf.getValidator("username", this.username).notNull().minLength(1).maxLength(255);
			vf.getValidator("password", this.password).notNull().minLength(1).maxLength(255);
		} else {
			vf.getValidator("tipo", this.tipo).notNull();
			vf.getValidator("tsLocation", this.tsLocation).notNull().minLength(1).maxLength(255);
			vf.getValidator("tsPassword", this.tsPassword).notNull().minLength(1).maxLength(255);
			
			if(this.tipo.equals(TipoEnum.CLIENT)) {
				vf.getValidator("ksLocation", this.ksLocation).notNull().minLength(1).maxLength(255);
				vf.getValidator("ksPassword", this.ksPassword).notNull().minLength(1).maxLength(255);
			}
		}
	}
}



