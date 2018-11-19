package it.govpay.core.rs.v1.beans.pendenze;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"domini",
"entrate",
"acl",
})
public class Profilo extends JSONSerializable {
  
  @JsonProperty("nome")
  private String nome = null;
  
  @JsonProperty("domini")
  private List<DominioIndex> domini = new ArrayList<>();
  
  @JsonProperty("entrate")
  private List<TipoEntrata> entrate = new ArrayList<>();
  
  @JsonProperty("acl")
  private List<AclPost> acl = new ArrayList<>();
  
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
   * entrate su cui e' abilitato ad operare
   **/
  public Profilo entrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public List<TipoEntrata> getEntrate() {
    return this.entrate;
  }
  public void setEntrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Profilo profilo = (Profilo) o;
    return Objects.equals(this.nome, profilo.nome) &&
        Objects.equals(this.domini, profilo.domini) &&
        Objects.equals(this.entrate, profilo.entrate) &&
        Objects.equals(this.acl, profilo.acl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.nome, this.domini, this.entrate, this.acl);
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
    
    sb.append("    nome: ").append(this.toIndentedString(this.nome)).append("\n");
    sb.append("    domini: ").append(this.toIndentedString(this.domini)).append("\n");
    sb.append("    entrate: ").append(this.toIndentedString(this.entrate)).append("\n");
    sb.append("    acl: ").append(this.toIndentedString(this.acl)).append("\n");
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



