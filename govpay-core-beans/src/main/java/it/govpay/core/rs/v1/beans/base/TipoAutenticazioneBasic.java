package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"username",
"password",
})
public class TipoAutenticazioneBasic extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("username")
  private String username = null;
  
  @JsonProperty("password")
  private String password = null;
  
  /**
   **/
  public TipoAutenticazioneBasic username(String username) {
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
  public TipoAutenticazioneBasic password(String password) {
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
    TipoAutenticazioneBasic tipoAutenticazioneBasic = (TipoAutenticazioneBasic) o;
    return Objects.equals(this.username, tipoAutenticazioneBasic.username) &&
        Objects.equals(this.password, tipoAutenticazioneBasic.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.username, this.password);
  }

  public static TipoAutenticazioneBasic parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, TipoAutenticazioneBasic.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoAutenticazioneBasic";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoAutenticazioneBasic {\n");
    
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



