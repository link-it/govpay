package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"password",
"abilitato",
"versione",
"idStazione",
"domini",
})
public class StazioneIndex extends JSONSerializable {
  
  @JsonProperty("password")
  private String password = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("versione")
  private VersioneStazione versione = null;
  
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
    return this.password;
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
  public Boolean isAbilitato() {
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   **/
  public StazioneIndex versione(VersioneStazione versione) {
    this.versione = versione;
    return this;
  }

  @JsonProperty("versione")
  public VersioneStazione getVersione() {
    return versione;
  }
  public void setVersione(VersioneStazione versione) {
    this.versione = versione;
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
    return this.idStazione;
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
    return this.domini;
  }
  public void setDomini(String domini) {
    this.domini = domini;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    StazioneIndex stazioneIndex = (StazioneIndex) o;
    return Objects.equals(password, stazioneIndex.password) &&
        Objects.equals(abilitato, stazioneIndex.abilitato) &&
        Objects.equals(versione, stazioneIndex.versione) &&
        Objects.equals(idStazione, stazioneIndex.idStazione) &&
        Objects.equals(domini, stazioneIndex.domini);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, abilitato, versione, idStazione, domini);
  }

  public static StazioneIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, StazioneIndex.class);
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
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
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



