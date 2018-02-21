package it.govpay.rs.v1.beans.base;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"bicAccredito",
"ibanAppoggio",
"bicAppoggio",
"postale",
"mybank",
"abilitato",
"ibanAccredito",
})
public class IbanAccredito extends it.govpay.rs.v1.beans.JSONSerializable {
  
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
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  /**
   **/
  public IbanAccredito bicAccredito(String bicAccredito) {
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
  public IbanAccredito ibanAppoggio(String ibanAppoggio) {
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
  public IbanAccredito bicAppoggio(String bicAppoggio) {
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
  public IbanAccredito postale(Boolean postale) {
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
  public IbanAccredito mybank(Boolean mybank) {
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
  public IbanAccredito abilitato(Boolean abilitato) {
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
   **/
  public IbanAccredito ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IbanAccredito ibanAccredito = (IbanAccredito) o;
    return Objects.equals(bicAccredito, ibanAccredito.bicAccredito) &&
        Objects.equals(ibanAppoggio, ibanAccredito.ibanAppoggio) &&
        Objects.equals(bicAppoggio, ibanAccredito.bicAppoggio) &&
        Objects.equals(postale, ibanAccredito.postale) &&
        Objects.equals(mybank, ibanAccredito.mybank) &&
        Objects.equals(abilitato, ibanAccredito.abilitato) &&
        Objects.equals(ibanAccredito, ibanAccredito.ibanAccredito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bicAccredito, ibanAppoggio, bicAppoggio, postale, mybank, abilitato, ibanAccredito);
  }

  public static IbanAccredito parse(String json) {
    return (IbanAccredito) parse(json, IbanAccredito.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "ibanAccredito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IbanAccredito {\n");
    
    sb.append("    bicAccredito: ").append(toIndentedString(bicAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    bicAppoggio: ").append(toIndentedString(bicAppoggio)).append("\n");
    sb.append("    postale: ").append(toIndentedString(postale)).append("\n");
    sb.append("    mybank: ").append(toIndentedString(mybank)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
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



