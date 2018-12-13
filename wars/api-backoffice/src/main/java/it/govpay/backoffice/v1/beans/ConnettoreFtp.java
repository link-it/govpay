package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
@JsonPropertyOrder({
"host",
"porta",
"username",
"password",
})
public class ConnettoreFtp extends JSONSerializable {
  
  @JsonProperty("host")
  private String host = null;
  
  @JsonProperty("porta")
  private String porta = null;
  
  @JsonProperty("username")
  private String username = null;
  
  @JsonProperty("password")
  private String password = null;
  
  /**
   * host del server sftp
   **/
  public ConnettoreFtp host(String host) {
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
   * porta del server sftp
   **/
  public ConnettoreFtp porta(String porta) {
    this.porta = porta;
    return this;
  }

  @JsonProperty("porta")
  public String getPorta() {
    return porta;
  }
  public void setPorta(String porta) {
    this.porta = porta;
  }

  /**
   * nome dell'utenza ftp
   **/
  public ConnettoreFtp username(String username) {
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
   * password dell'utenza ftp
   **/
  public ConnettoreFtp password(String password) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ConnettoreFtp connettoreFtp = (ConnettoreFtp) o;
    return Objects.equals(host, connettoreFtp.host) &&
        Objects.equals(porta, connettoreFtp.porta) &&
        Objects.equals(this.username, connettoreFtp.username) &&
        Objects.equals(this.password, connettoreFtp.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, porta, username, password);
  }

  public static ConnettoreFtp parse(String json) throws ServiceException, ValidationException {
    return parse(json, ConnettoreFtp.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreFtp";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreFtp {\n");
    
    sb.append("    host: ").append(toIndentedString(host)).append("\n");
    sb.append("    porta: ").append(toIndentedString(porta)).append("\n");
    sb.append("    username: ").append(this.toIndentedString(this.username)).append("\n");
    sb.append("    password: ").append(this.toIndentedString(this.password)).append("\n");
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



