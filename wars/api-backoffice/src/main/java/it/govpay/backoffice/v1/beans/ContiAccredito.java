package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"bic",
"postale",
"mybank",
"abilitato",
"descrizione",
"intestatario",
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
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("intestatario")
  private String intestatario = null;
  
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
    return this.bic;
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
  public Boolean isPostale() {
    return this.postale;
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
  public Boolean isMybank() {
    return this.mybank;
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
  public Boolean isAbilitato() {
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * Descrizione estesa dell'Iban
   **/
  public ContiAccredito descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Intestatario del conto corrente
   **/
  public ContiAccredito intestatario(String intestatario) {
    this.intestatario = intestatario;
    return this;
  }

  @JsonProperty("intestatario")
  public String getIntestatario() {
    return intestatario;
  }
  public void setIntestatario(String intestatario) {
    this.intestatario = intestatario;
  }

  /**
   **/
  public ContiAccredito iban(String iban) {
    this.iban = iban;
    return this;
  }

  @JsonProperty("iban")
  public String getIban() {
    return this.iban;
  }
  public void setIban(String iban) {
    this.iban = iban;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ContiAccredito contiAccredito = (ContiAccredito) o;
    return Objects.equals(bic, contiAccredito.bic) &&
        Objects.equals(postale, contiAccredito.postale) &&
        Objects.equals(mybank, contiAccredito.mybank) &&
        Objects.equals(abilitato, contiAccredito.abilitato) &&
        Objects.equals(descrizione, contiAccredito.descrizione) &&
        Objects.equals(intestatario, contiAccredito.intestatario) &&
        Objects.equals(iban, contiAccredito.iban);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bic, postale, mybank, abilitato, descrizione, intestatario, iban);
  }

  public static ContiAccredito parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, ContiAccredito.class);
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
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    intestatario: ").append(toIndentedString(intestatario)).append("\n");
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



