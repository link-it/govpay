package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"password",
"abilitato",
"idStazione",
"domini",
})
public class StazioneIndex extends JSONSerializable {
  
  @JsonProperty("password")
  private String password = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("idStazione")
  private String idStazione = null;
  
  @JsonProperty("domini")
  private String domini = null;
  
  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public StazioneIndex password(String password) {
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
   * Indica lo stato di abilitazione
   **/
  public StazioneIndex abilitato(Boolean abilitato) {
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
   * Identificativo della stazione
   **/
  public StazioneIndex idStazione(String idStazione) {
    this.idStazione = idStazione;
    return this;
  }

  @JsonProperty("idStazione")
  public String getIdStazione() {
    return idStazione;
  }
  public void setIdStazione(String idStazione) {
    this.idStazione = idStazione;
  }

  /**
   * Url alla lista dei domini della stazione
   **/
  public StazioneIndex domini(String domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public String getDomini() {
    return domini;
  }
  public void setDomini(String domini) {
    this.domini = domini;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StazioneIndex stazioneIndex = (StazioneIndex) o;
    return Objects.equals(password, stazioneIndex.password) &&
        Objects.equals(abilitato, stazioneIndex.abilitato) &&
        Objects.equals(idStazione, stazioneIndex.idStazione) &&
        Objects.equals(domini, stazioneIndex.domini);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, abilitato, idStazione, domini);
  }

  public static StazioneIndex parse(String json) {
    return (StazioneIndex) parse(json, StazioneIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "stazioneIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StazioneIndex {\n");
    
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    idStazione: ").append(toIndentedString(idStazione)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
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



