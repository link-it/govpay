package it.govpay.backoffice.v1.beans;

import java.util.List;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idTracciato",
"idDominio",
"avvisaturaDigitale",
"modalitaAvvisaturaDigitale",
"inserimenti",
"annullamenti",
})
public class TracciatoPendenzePost extends JSONSerializable implements IValidable{
  
  @JsonProperty("idTracciato")
  private String idTracciato = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("avvisaturaDigitale")
  private Boolean avvisaturaDigitale = false;
  
  @JsonProperty("modalitaAvvisaturaDigitale")
  private String modalitaAvvisaturaDigitale = null;
  
  @JsonProperty("inserimenti")
  private List<PendenzaPost> inserimenti = null;
  
  @JsonProperty("annullamenti")
  private List<AnnullamentoPendenza> annullamenti = null;
  
  /**
   * Identificativo del tracciato
   **/
  public TracciatoPendenzePost idTracciato(String idTracciato) {
    this.idTracciato = idTracciato;
    return this;
  }

  @JsonProperty("idTracciato")
  public String getIdTracciato() {
    return this.idTracciato;
  }
  public void setIdTracciato(String idTracciato) {
    this.idTracciato = idTracciato;
  }

  /**
   * Identificativo del Dominio
   **/
  public TracciatoPendenzePost idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return this.idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Indica se la pendenza deve essere avvisata digitalmente
   **/
  public TracciatoPendenzePost avvisaturaDigitale(Boolean avvisaturaDigitale) {
    this.avvisaturaDigitale = avvisaturaDigitale;
    return this;
  }

  @JsonProperty("avvisaturaDigitale")
  public Boolean AvvisaturaDigitale() {
    return avvisaturaDigitale;
  }
  public void setAvvisaturaDigitale(Boolean avvisaturaDigitale) {
    this.avvisaturaDigitale = avvisaturaDigitale;
  }

  /**
   * Modalita' di avvisatura scelta per le pendenza [sincrona|asincrona]
   **/
  public TracciatoPendenzePost modalitaAvvisaturaDigitale(String modalitaAvvisaturaDigitale) {
    this.modalitaAvvisaturaDigitale = modalitaAvvisaturaDigitale;
    return this;
  }

  @JsonProperty("modalitaAvvisaturaDigitale")
  public String getModalitaAvvisaturaDigitale() {
    return modalitaAvvisaturaDigitale;
  }
  public void setModalitaAvvisaturaDigitale(String modalitaAvvisaturaDigitale) {
    this.modalitaAvvisaturaDigitale = modalitaAvvisaturaDigitale;
  }

  /**
   **/
  public TracciatoPendenzePost inserimenti(List<PendenzaPost> inserimenti) {
    this.inserimenti = inserimenti;
    return this;
  }

  @JsonProperty("inserimenti")
  public List<PendenzaPost> getInserimenti() {
    return this.inserimenti;
  }
  public void setInserimenti(List<PendenzaPost> inserimenti) {
    this.inserimenti = inserimenti;
  }

  /**
   **/
  public TracciatoPendenzePost annullamenti(List<AnnullamentoPendenza> annullamenti) {
    this.annullamenti = annullamenti;
    return this;
  }

  @JsonProperty("annullamenti")
  public List<AnnullamentoPendenza> getAnnullamenti() {
    return this.annullamenti;
  }
  public void setAnnullamenti(List<AnnullamentoPendenza> annullamenti) {
    this.annullamenti = annullamenti;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TracciatoPendenzePost tracciatoPendenzePost = (TracciatoPendenzePost) o;
    return Objects.equals(this.idTracciato, tracciatoPendenzePost.idTracciato) &&
        Objects.equals(this.idDominio, tracciatoPendenzePost.idDominio) &&
        Objects.equals(avvisaturaDigitale, tracciatoPendenzePost.avvisaturaDigitale) &&
        Objects.equals(modalitaAvvisaturaDigitale, tracciatoPendenzePost.modalitaAvvisaturaDigitale) &&
        Objects.equals(this.inserimenti, tracciatoPendenzePost.inserimenti) &&
        Objects.equals(this.annullamenti, tracciatoPendenzePost.annullamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idTracciato, this.idDominio, avvisaturaDigitale, modalitaAvvisaturaDigitale, this.inserimenti, this.annullamenti);
  }

  public static TracciatoPendenzePost parse(String json) throws ServiceException, ValidationException {
    return parse(json, TracciatoPendenzePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tracciatoPendenzePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TracciatoPendenzePost {\n");
    
    sb.append("    idTracciato: ").append(this.toIndentedString(this.idTracciato)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    avvisaturaDigitale: ").append(toIndentedString(avvisaturaDigitale)).append("\n");
    sb.append("    modalitaAvvisaturaDigitale: ").append(toIndentedString(modalitaAvvisaturaDigitale)).append("\n");
    sb.append("    inserimenti: ").append(this.toIndentedString(this.inserimenti)).append("\n");
    sb.append("    annullamenti: ").append(this.toIndentedString(this.annullamenti)).append("\n");
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
public void validate() throws org.openspcoop2.utils.json.ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		
		vf.getValidator("idTracciato", this.idTracciato).notNull();
		vf.getValidator("idDominio", this.idDominio).notNull().minLength(1).maxLength(35);
		
		vf.getValidator("modalitaAvvisaturaDigitale", this.modalitaAvvisaturaDigitale).pattern("^(asincrona|sincrona)$");
		
		vf.getValidator("inserimenti", this.inserimenti).notNull().validateObjects();
		vf.getValidator("annullamenti", this.annullamenti).notNull().validateObjects();
  }
}



