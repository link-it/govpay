package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@JsonPropertyOrder({
"serverurl",
"username",
"password",
})
public class ConnettoreFtp extends JSONSerializable {
  
  @JsonProperty("serverurl")
  private String serverurl = null;
  
  @JsonProperty("username")
  private String username = null;
  
  @JsonProperty("password")
  private String password = null;
  
  /**
   * url del server sftp
   **/
  public ConnettoreFtp serverurl(String serverurl) {
    this.serverurl = serverurl;
    return this;
  }

  @JsonProperty("serverurl")
  public String getServerurl() {
    return serverurl;
  }
  public void setServerurl(String serverurl) {
    this.serverurl = serverurl;
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
    return username;
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
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnettoreFtp connettoreFtp = (ConnettoreFtp) o;
    return Objects.equals(serverurl, connettoreFtp.serverurl) &&
        Objects.equals(username, connettoreFtp.username) &&
        Objects.equals(password, connettoreFtp.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serverurl, username, password);
  }

  public static ConnettoreFtp parse(String json) throws ServiceException, ValidationException {
    return (ConnettoreFtp) parse(json, ConnettoreFtp.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "connettoreFtp";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnettoreFtp {\n");
    
    sb.append("    serverurl: ").append(toIndentedString(serverurl)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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



