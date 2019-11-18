package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"host",
"port",
"username",
"password",
"from",
"readTimeout",
"connectionTimeout",
})
public class Mailserver extends JSONSerializable implements IValidable{
  
  @JsonProperty("host")
  private String host = null;
  
  @JsonProperty("port")
  private BigDecimal port = null;
  
  @JsonProperty("username")
  private String username = null;
  
  @JsonProperty("password")
  private String password = null;
  
  @JsonProperty("from")
  private String from = null;
  
  @JsonProperty("readTimeout")
  private BigDecimal readTimeout = null;
  
  @JsonProperty("connectionTimeout")
  private BigDecimal connectionTimeout = null;
  
  /**
   * host del servizio di posta
   **/
  public Mailserver host(String host) {
    this.host = host;
    return this;
  }

  @JsonProperty("host")
  public String getHost() {
    return host;
  }
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * port del servizio di posta
   **/
  public Mailserver port(BigDecimal port) {
    this.port = port;
    return this;
  }

  @JsonProperty("port")
  public BigDecimal getPort() {
    return port;
  }
  public void setPort(BigDecimal port) {
    this.port = port;
  }

  /**
   * username del servizio di posta
   **/
  public Mailserver username(String username) {
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
   * password del servizio di posta
   **/
  public Mailserver password(String password) {
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
   * indirizzo mittente del promemoria
   **/
  public Mailserver from(String from) {
    this.from = from;
    return this;
  }

  @JsonProperty("from")
  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * Read timeout (in millisecondi)
   **/
  public Mailserver readTimeout(BigDecimal readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  @JsonProperty("readTimeout")
  public BigDecimal getReadTimeout() {
    return readTimeout;
  }
  public void setReadTimeout(BigDecimal readTimeout) {
    this.readTimeout = readTimeout;
  }

  /**
   * Connection Timeout (in millisecondi)
   **/
  public Mailserver connectionTimeout(BigDecimal connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
    return this;
  }

  @JsonProperty("connectionTimeout")
  public BigDecimal getConnectionTimeout() {
    return connectionTimeout;
  }
  public void setConnectionTimeout(BigDecimal connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Mailserver mailserver = (Mailserver) o;
    return Objects.equals(host, mailserver.host) &&
        Objects.equals(port, mailserver.port) &&
        Objects.equals(username, mailserver.username) &&
        Objects.equals(password, mailserver.password) &&
        Objects.equals(from, mailserver.from) &&
        Objects.equals(readTimeout, mailserver.readTimeout) &&
        Objects.equals(connectionTimeout, mailserver.connectionTimeout);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, port, username, password, from, readTimeout, connectionTimeout);
  }

  public static Mailserver parse(String json) throws ServiceException, ValidationException { 
    return (Mailserver) parse(json, Mailserver.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "mailserver";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Mailserver {\n");
    
    sb.append("    host: ").append(toIndentedString(host)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    readTimeout: ").append(toIndentedString(readTimeout)).append("\n");
    sb.append("    connectionTimeout: ").append(toIndentedString(connectionTimeout)).append("\n");
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
	vf.getValidator("host", this.host).notNull().minLength(1).pattern(CostantiValidazione.PATTERN_NO_WHITE_SPACES);
	vf.getValidator("port", this.port).notNull().min(BigDecimal.ONE);
	vf.getValidator("username", this.username).notNull().minLength(1).maxLength(35).pattern(CostantiValidazione.PATTERN_NO_WHITE_SPACES).pattern(CostantiValidazione.PATTERN_USERNAME);
	vf.getValidator("password", this.password).notNull().minLength(1).maxLength(35);
	vf.getValidator("from", this.from).notNull().minLength(1).pattern(CostantiValidazione.PATTERN_EMAIL);
	vf.getValidator("readTimeout", this.readTimeout).notNull().min(BigDecimal.ZERO);
	vf.getValidator("connectionTimeout", this.connectionTimeout).notNull().min(BigDecimal.ZERO);
  }
}



