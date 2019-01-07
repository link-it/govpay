package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Acl;
import it.govpay.pagamento.v2.beans.Dominio;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.TipoEntrata;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Profilo  {
  
  @Schema(required = true, description = "Nome o principal dell'utenza")
 /**
   * Nome o principal dell'utenza  
  **/
  private String nome = null;
  
  @Schema(required = true, description = "domini su cui e' abilitato ad operare")
 /**
   * domini su cui e' abilitato ad operare  
  **/
  private List<Dominio> domini = new ArrayList<Dominio>();
  
  @Schema(required = true, description = "entrate su cui e' abilitato ad operare")
 /**
   * entrate su cui e' abilitato ad operare  
  **/
  private List<TipoEntrata> entrate = new ArrayList<TipoEntrata>();
  
  @Schema(required = true, description = "")
  private List<Acl> acl = new ArrayList<Acl>();
  
  @Schema(description = "")
  private Soggetto anagrafica = null;
  
  @Schema(description = "")
  private Object identityData = null;
 /**
   * Nome o principal dell&#x27;utenza
   * @return nome
  **/
  @JsonProperty("nome")
  @NotNull
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Profilo nome(String nome) {
    this.nome = nome;
    return this;
  }

 /**
   * domini su cui e&#x27; abilitato ad operare
   * @return domini
  **/
  @JsonProperty("domini")
  @NotNull
  public List<Dominio> getDomini() {
    return domini;
  }

  public void setDomini(List<Dominio> domini) {
    this.domini = domini;
  }

  public Profilo domini(List<Dominio> domini) {
    this.domini = domini;
    return this;
  }

  public Profilo addDominiItem(Dominio dominiItem) {
    this.domini.add(dominiItem);
    return this;
  }

 /**
   * entrate su cui e&#x27; abilitato ad operare
   * @return entrate
  **/
  @JsonProperty("entrate")
  @NotNull
  public List<TipoEntrata> getEntrate() {
    return entrate;
  }

  public void setEntrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
  }

  public Profilo entrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
    return this;
  }

  public Profilo addEntrateItem(TipoEntrata entrateItem) {
    this.entrate.add(entrateItem);
    return this;
  }

 /**
   * Get acl
   * @return acl
  **/
  @JsonProperty("acl")
  @NotNull
  public List<Acl> getAcl() {
    return acl;
  }

  public void setAcl(List<Acl> acl) {
    this.acl = acl;
  }

  public Profilo acl(List<Acl> acl) {
    this.acl = acl;
    return this;
  }

  public Profilo addAclItem(Acl aclItem) {
    this.acl.add(aclItem);
    return this;
  }

 /**
   * Get anagrafica
   * @return anagrafica
  **/
  @JsonProperty("anagrafica")
  public Soggetto getAnagrafica() {
    return anagrafica;
  }

  public void setAnagrafica(Soggetto anagrafica) {
    this.anagrafica = anagrafica;
  }

  public Profilo anagrafica(Soggetto anagrafica) {
    this.anagrafica = anagrafica;
    return this;
  }

 /**
   * Get identityData
   * @return identityData
  **/
  @JsonProperty("identityData")
  public Object getIdentityData() {
    return identityData;
  }

  public void setIdentityData(Object identityData) {
    this.identityData = identityData;
  }

  public Profilo identityData(Object identityData) {
    this.identityData = identityData;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profilo {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
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
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
