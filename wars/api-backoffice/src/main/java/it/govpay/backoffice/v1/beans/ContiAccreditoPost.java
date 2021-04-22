package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"bic",
"postale",
"mybank",
"abilitato",
"descrizione",
"intestatario",
"autStampaPosteItaliane",
})
public class ContiAccreditoPost extends JSONSerializable implements IValidable {
  
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
  
  @JsonProperty("autStampaPosteItaliane")
  private String autStampaPosteItaliane = null;
  
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

  /**
   * Descrizione estesa dell'Iban
   **/
  public ContiAccreditoPost descrizione(String descrizione) {
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
  public ContiAccreditoPost intestatario(String intestatario) {
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
   * numero di autorizzazione per la stampa in proprio rilasciato da poste italiane
   **/
  public ContiAccreditoPost autStampaPosteItaliane(String autStampaPosteItaliane) {
    this.autStampaPosteItaliane = autStampaPosteItaliane;
    return this;
  }

  @JsonProperty("autStampaPosteItaliane")
  public String getAutStampaPosteItaliane() {
    return autStampaPosteItaliane;
  }
  public void setAutStampaPosteItaliane(String autStampaPosteItaliane) {
    this.autStampaPosteItaliane = autStampaPosteItaliane;
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
        Objects.equals(abilitato, contiAccreditoPost.abilitato) &&
        Objects.equals(descrizione, contiAccreditoPost.descrizione) &&
        Objects.equals(intestatario, contiAccreditoPost.intestatario) &&
        Objects.equals(autStampaPosteItaliane, contiAccreditoPost.autStampaPosteItaliane);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bic, postale, mybank, abilitato, descrizione, intestatario, autStampaPosteItaliane);
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
    
    sb.append("    bic: ").append(toIndentedString(bic)).append("\n");
    sb.append("    postale: ").append(toIndentedString(postale)).append("\n");
    sb.append("    mybank: ").append(toIndentedString(mybank)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    intestatario: ").append(toIndentedString(intestatario)).append("\n");
    sb.append("    autStampaPosteItaliane: ").append(toIndentedString(autStampaPosteItaliane)).append("\n");
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
  
  @Override
  public void validate() throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	vf.getValidator("abilitato", this.abilitato).notNull();
	vf.getValidator("bic", this.bic).minLength(1).maxLength(255).pattern(CostantiValidazione.PATTERN_BIC);
	vf.getValidator("postale", this.postale).notNull();
//	vf.getValidator("mybank", this.mybank).notNull();
	vf.getValidator("descrizione", this.descrizione).minLength(1).maxLength(255);
	vf.getValidator("intestatario", this.intestatario).minLength(1).maxLength(255);
	vf.getValidator("autStampaPosteItaliane", this.autStampaPosteItaliane).maxLength(255);
  }
}



