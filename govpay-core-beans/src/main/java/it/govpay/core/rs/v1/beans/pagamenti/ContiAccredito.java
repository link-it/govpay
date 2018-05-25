package it.govpay.core.rs.v1.beans.pagamenti;

import java.util.Objects;
import it.govpay.core.rs.v1.beans.pagamenti.ContiAccreditoPost;
import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"bic",
"postale",
"mybank",
"abilitato",
"iban",
})
public class ContiAccredito extends JSONSerializable {
  
  @JsonProperty("bic")
  private String bic = null;
  
  @JsonProperty("postale")
  private Boolean postale = false;
  
  @JsonProperty("mybank")
  private Boolean mybank = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  @JsonProperty("iban")
  private String iban = null;
  
  /**
   **/
  public ContiAccredito bic(String bic) {
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
  public ContiAccredito postale(Boolean postale) {
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
  public ContiAccredito mybank(Boolean mybank) {
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
  public ContiAccredito abilitato(Boolean abilitato) {
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

  /**
   **/
  public ContiAccredito iban(String iban) {
    this.iban = iban;
    return this;
  }

  @JsonProperty("iban")
  public String getIban() {
    return iban;
  }
  public void setIban(String iban) {
    this.iban = iban;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContiAccredito contiAccredito = (ContiAccredito) o;
    return Objects.equals(bic, contiAccredito.bic) &&
        Objects.equals(postale, contiAccredito.postale) &&
        Objects.equals(mybank, contiAccredito.mybank) &&
        Objects.equals(abilitato, contiAccredito.abilitato) &&
        Objects.equals(iban, contiAccredito.iban);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bic, postale, mybank, abilitato, iban);
  }

  public static ContiAccredito parse(String json) {
    return (ContiAccredito) parse(json, ContiAccredito.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "contiAccredito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContiAccredito {\n");
    
    sb.append("    bic: ").append(toIndentedString(bic)).append("\n");
    sb.append("    postale: ").append(toIndentedString(postale)).append("\n");
    sb.append("    mybank: ").append(toIndentedString(mybank)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
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



