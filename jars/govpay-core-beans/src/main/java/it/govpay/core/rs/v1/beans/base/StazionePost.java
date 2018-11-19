package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"password",
"abilitato",
})
public class StazionePost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("password")
  private String password = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public StazionePost password(String password) {
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
   * Indica lo stato di abilitazione
   **/
  public StazionePost abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    StazionePost stazionePost = (StazionePost) o;
    return Objects.equals(this.password, stazionePost.password) &&
        Objects.equals(this.abilitato, stazionePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.password, this.abilitato);
  }

  public static StazionePost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, StazionePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "stazionePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StazionePost {\n");
    
    sb.append("    password: ").append(this.toIndentedString(this.password)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
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



