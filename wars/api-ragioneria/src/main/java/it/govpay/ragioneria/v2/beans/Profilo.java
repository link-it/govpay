package it.govpay.ragioneria.v2.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"domini",
"tipiPendenza",
"acl",
"anagrafica",
"identityData",
})
public class Profilo extends JSONSerializable {
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("domini")
  private List<Dominio> domini = new ArrayList<>();
  
  @JsonProperty("tipiPendenza")
  private List<TipoPendenza> tipiPendenza = new ArrayList<>();
  
  @JsonProperty("acl")
  private List<Acl> acl = new ArrayList<>();
  
  @JsonProperty("anagrafica")
  private Soggetto anagrafica = null;
  
  @JsonProperty("identityData")
  private Object identityData = null;
  
  /**
   * Nome o principal dell'utenza
   **/
  public Profilo nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public Profilo domini(List<Dominio> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<Dominio> getDomini() {
    return domini;
  }
  public void setDomini(List<Dominio> domini) {
    this.domini = domini;
  }

  /**
   * tipologie di pendenza su cui e' abilitato ad operare
   **/
  public Profilo tipiPendenza(List<TipoPendenza> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<TipoPendenza> getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(List<TipoPendenza> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  /**
   **/
  public Profilo acl(List<Acl> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<Acl> getAcl() {
    return acl;
  }
  public void setAcl(List<Acl> acl) {
    this.acl = acl;
  }

  /**
   **/
  public Profilo anagrafica(Soggetto anagrafica) {
    this.anagrafica = anagrafica;
    return this;
  }

  @JsonProperty("anagrafica")
  public Soggetto getAnagrafica() {
    return anagrafica;
  }
  public void setAnagrafica(Soggetto anagrafica) {
    this.anagrafica = anagrafica;
  }

  /**
   **/
  public Profilo identityData(Object identityData) {
    this.identityData = identityData;
    return this;
  }

  @JsonProperty("identityData")
  public Object getIdentityData() {
    return identityData;
  }
  public void setIdentityData(Object identityData) {
    this.identityData = identityData;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profilo profilo = (Profilo) o;
    return Objects.equals(nome, profilo.nome) &&
        Objects.equals(domini, profilo.domini) &&
        Objects.equals(tipiPendenza, profilo.tipiPendenza) &&
        Objects.equals(acl, profilo.acl) &&
        Objects.equals(anagrafica, profilo.anagrafica) &&
        Objects.equals(identityData, profilo.identityData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, domini, tipiPendenza, acl, anagrafica, identityData);
  }

  public static Profilo parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Profilo.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "profilo";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profilo {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    anagrafica: ").append(toIndentedString(anagrafica)).append("\n");
    sb.append("    identityData: ").append(toIndentedString(identityData)).append("\n");
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



