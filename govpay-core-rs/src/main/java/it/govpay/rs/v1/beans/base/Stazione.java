package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.StazionePost;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"password",
"abilitato",
"idStazione",
"domini",
})
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaInflectorServerCodegen", date = "2018-02-05T15:39:23.431+01:00")
public abstract class Stazione extends it.govpay.rs.v1.beans.JSONSerializable {
  
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
  public Stazione password(String password) {
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
  public Stazione abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * Identificativo della stazione
   **/
  public Stazione idStazione(String idStazione) {
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
  public Stazione domini(String domini) {
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
    Stazione stazione = (Stazione) o;
    return Objects.equals(password, stazione.password) &&
        Objects.equals(abilitato, stazione.abilitato) &&
        Objects.equals(idStazione, stazione.idStazione) &&
        Objects.equals(domini, stazione.domini);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, abilitato, idStazione, domini);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Stazione {\n");
    
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



