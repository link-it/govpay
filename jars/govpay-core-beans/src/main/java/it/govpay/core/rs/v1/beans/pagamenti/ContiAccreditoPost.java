package it.govpay.core.rs.v1.beans.pagamenti;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
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
    return this.bic;
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
  public Boolean isPostale() {
    return this.postale;
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
  public Boolean isMybank() {
    return this.mybank;
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
    ContiAccreditoPost contiAccreditoPost = (ContiAccreditoPost) o;
    return Objects.equals(this.bic, contiAccreditoPost.bic) &&
        Objects.equals(this.postale, contiAccreditoPost.postale) &&
        Objects.equals(this.mybank, contiAccreditoPost.mybank) &&
        Objects.equals(this.abilitato, contiAccreditoPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.bic, this.postale, this.mybank, this.abilitato);
  }

  public static ContiAccreditoPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, ContiAccreditoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contiAccreditoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContiAccreditoPost {\n");
    
    sb.append("    bic: ").append(this.toIndentedString(this.bic)).append("\n");
    sb.append("    postale: ").append(this.toIndentedString(this.postale)).append("\n");
    sb.append("    mybank: ").append(this.toIndentedString(this.mybank)).append("\n");
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



