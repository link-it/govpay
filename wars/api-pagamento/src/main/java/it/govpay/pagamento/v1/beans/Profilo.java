package it.govpay.pagamento.v1.beans;

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
})
public class Profilo extends JSONSerializable {
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("domini")
  private List<DominioIndex> domini = new ArrayList<>();
  
  @JsonProperty("tipiPendenza")
  private List<TipoPendenzaIndex> tipiPendenza = new ArrayList<TipoPendenzaIndex>();
  
  @JsonProperty("acl")
  private List<AclPost> acl = new ArrayList<>();
  
  @JsonProperty("anagrafica")
  private Soggetto anagrafica = null;
  
  @JsonProperty("identityData")
  private Object identityData = null;
  
  /**
   * Nome dell'utenza
   **/
  public Profilo nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return this.nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public Profilo domini(List<DominioIndex> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<DominioIndex> getDomini() {
    return this.domini;
  }
  public void setDomini(List<DominioIndex> domini) {
    this.domini = domini;
  }

  /**
   * tipologie di pendenza su cui e' abilitato ad operare
   **/
  public Profilo tipiPendenza(List<TipoPendenzaIndex> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<TipoPendenzaIndex> getTipiPendenza() {
    return this.tipiPendenza;
  }
  public void setTipiPendenza(List<TipoPendenzaIndex> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  /**
   **/
  public Profilo acl(List<AclPost> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<AclPost> getAcl() {
    return this.acl;
  }
  public void setAcl(List<AclPost> acl) {
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
    if (o == null || this.getClass() != o.getClass()) {
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



