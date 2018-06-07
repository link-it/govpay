package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"bic",
"postale",
"mybank",
"abilitato",
})
public class ContiAccreditoPost extends JSONSerializable {
  
  @JsonProperty("bic")
  private String bic = null;
  
  @JsonProperty("postale")
  private Boolean postale = false;
  
  @JsonProperty("mybank")
  private Boolean mybank = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  /**
   **/
  public ContiAccreditoPost bic(String bic) {
    this.bic = bic;
    return this;
  }

  @JsonProperty("bic")
  public String getBic() {
    return bic;
  }
  public void setBic(String bic) {
    this.bic = bic;
  }

  /**
   * indica se e' un c/c postale
   **/
  public ContiAccreditoPost postale(Boolean postale) {
    this.postale = postale;
    return this;
  }

  @JsonProperty("postale")
  public Boolean Postale() {
    return postale;
  }
  public void setPostale(Boolean postale) {
    this.postale = postale;
  }

  /**
   * indica se e' un iban abilitato sul circuito mybank
   **/
  public ContiAccreditoPost mybank(Boolean mybank) {
    this.mybank = mybank;
    return this;
  }

  @JsonProperty("mybank")
  public Boolean Mybank() {
    return mybank;
  }
  public void setMybank(Boolean mybank) {
    this.mybank = mybank;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public ContiAccreditoPost abilitato(Boolean abilitato) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContiAccreditoPost contiAccreditoPost = (ContiAccreditoPost) o;
    return Objects.equals(bic, contiAccreditoPost.bic) &&
        Objects.equals(postale, contiAccreditoPost.postale) &&
        Objects.equals(mybank, contiAccreditoPost.mybank) &&
        Objects.equals(abilitato, contiAccreditoPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bic, postale, mybank, abilitato);
  }

  public static ContiAccreditoPost parse(String json) {
    return (ContiAccreditoPost) parse(json, ContiAccreditoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contiAccreditoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContiAccreditoPost {\n");
    
    sb.append("    bic: ").append(toIndentedString(bic)).append("\n");
    sb.append("    postale: ").append(toIndentedString(postale)).append("\n");
    sb.append("    mybank: ").append(toIndentedString(mybank)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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



