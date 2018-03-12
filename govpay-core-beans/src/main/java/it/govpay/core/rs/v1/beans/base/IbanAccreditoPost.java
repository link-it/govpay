package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"bicAccredito",
"ibanAppoggio",
"bicAppoggio",
"postale",
"mybank",
"abilitato",
})
public class IbanAccreditoPost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("bicAccredito")
  private String bicAccredito = null;
  
  @JsonProperty("ibanAppoggio")
  private String ibanAppoggio = null;
  
  @JsonProperty("bicAppoggio")
  private String bicAppoggio = null;
  
  @JsonProperty("postale")
  private Boolean postale = false;
  
  @JsonProperty("mybank")
  private Boolean mybank = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  /**
   **/
  public IbanAccreditoPost bicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
    return this;
  }

  @JsonProperty("bicAccredito")
  public String getBicAccredito() {
    return bicAccredito;
  }
  public void setBicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
  }

  /**
   **/
  public IbanAccreditoPost ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

  @JsonProperty("ibanAppoggio")
  public String getIbanAppoggio() {
    return ibanAppoggio;
  }
  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  /**
   **/
  public IbanAccreditoPost bicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
    return this;
  }

  @JsonProperty("bicAppoggio")
  public String getBicAppoggio() {
    return bicAppoggio;
  }
  public void setBicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
  }

  /**
   * indica se e' un c/c postale
   **/
  public IbanAccreditoPost postale(Boolean postale) {
    this.postale = postale;
    return this;
  }

  @JsonProperty("postale")
  public Boolean isPostale() {
    return postale;
  }
  public void setPostale(Boolean postale) {
    this.postale = postale;
  }

  /**
   * indica se e' un iban abilitato sul circuito mybank
   **/
  public IbanAccreditoPost mybank(Boolean mybank) {
    this.mybank = mybank;
    return this;
  }

  @JsonProperty("mybank")
  public Boolean isMybank() {
    return mybank;
  }
  public void setMybank(Boolean mybank) {
    this.mybank = mybank;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public IbanAccreditoPost abilitato(Boolean abilitato) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IbanAccreditoPost ibanAccreditoPost = (IbanAccreditoPost) o;
    return Objects.equals(bicAccredito, ibanAccreditoPost.bicAccredito) &&
        Objects.equals(ibanAppoggio, ibanAccreditoPost.ibanAppoggio) &&
        Objects.equals(bicAppoggio, ibanAccreditoPost.bicAppoggio) &&
        Objects.equals(postale, ibanAccreditoPost.postale) &&
        Objects.equals(mybank, ibanAccreditoPost.mybank) &&
        Objects.equals(abilitato, ibanAccreditoPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bicAccredito, ibanAppoggio, bicAppoggio, postale, mybank, abilitato);
  }

  public static IbanAccreditoPost parse(String json) {
    return (IbanAccreditoPost) parse(json, IbanAccreditoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ibanAccreditoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IbanAccreditoPost {\n");
    
    sb.append("    bicAccredito: ").append(toIndentedString(bicAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    bicAppoggio: ").append(toIndentedString(bicAppoggio)).append("\n");
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



