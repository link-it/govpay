package it.govpay.backoffice.v1.beans;

import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"ragioneSociale",
"domini",
"entrate",
"acl",
"abilitato",
})
public class OperatorePost extends it.govpay.core.beans.JSONSerializable implements IValidable{
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  @JsonProperty("domini")
  private List<String> domini = null;
  
  @JsonProperty("tipiPendenza")
  private List<String> tipiPendenza = null;
  
  @JsonProperty("acl")
  private List<AclPost> acl = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * Nome e cognome dell'operatore
   **/
  public OperatorePost ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }

  @JsonProperty("ragioneSociale")
  public String getRagioneSociale() {
    return this.ragioneSociale;
  }
  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  /**
   * domini su cui e' abilitato ad operare. Se la lista e' vuota, l'abilitazione e' per tutti i domini
   **/
  public OperatorePost domini(List<String> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<String> getDomini() {
    return this.domini;
  }
  public void setDomini(List<String> domini) {
    this.domini = domini;
  }

  /**
   * tipologie di pendenza su cui e' abilitato ad operare. Se la lista e' vuota, l'abilitazione e' per tutte le entrate
   **/
  public OperatorePost tipiPendenza(List<String> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<String> getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(List<String> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  /**
   * lista delle acl attive sull'operatore
   **/
  public OperatorePost acl(List<AclPost> acl) {
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

  /**
   * Indicazione se l'operatore è abilitato ad operare sulla piattaforma
   **/
  public OperatorePost abilitato(Boolean abilitato) {
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
    OperatorePost operatorePost = (OperatorePost) o;
    return Objects.equals(this.ragioneSociale, operatorePost.ragioneSociale) &&
        Objects.equals(this.domini, operatorePost.domini) &&
        Objects.equals(this.tipiPendenza, operatorePost.tipiPendenza) &&
        Objects.equals(this.acl, operatorePost.acl) &&
        Objects.equals(this.abilitato, operatorePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ragioneSociale, this.domini, this.tipiPendenza, this.acl, this.abilitato);
  }

  public static OperatorePost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, OperatorePost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "operatorePost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatorePost {\n");
    
    sb.append("    ragioneSociale: ").append(this.toIndentedString(this.ragioneSociale)).append("\n");
    sb.append("    domini: ").append(this.toIndentedString(this.domini)).append("\n");
    sb.append("    tipiPendenza: ").append(this.toIndentedString(this.tipiPendenza)).append("\n");
    sb.append("    acl: ").append(this.toIndentedString(this.acl)).append("\n");
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
  
	@Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("ragioneSociale", this.ragioneSociale).notNull().minLength(1).maxLength(35);
		vf.getValidator("acl", this.acl).validateObjects();
		
		if(this.domini != null && !this.domini.isEmpty()) {
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			for (String idDominio : this.domini) {
				if(!idDominio.equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR))
					validatoreId.validaIdDominio("domini", idDominio);
			}
		}
		
		if(this.tipiPendenza != null && !this.tipiPendenza.isEmpty()) {
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			for (String idTipoPendenza : this.tipiPendenza) {
				if(!idTipoPendenza.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR))
					validatoreId.validaIdTipoVersamento("tipiPendenza", idTipoPendenza);
			}
		}
		vf.getValidator("abilitato", this.abilitato).notNull();
	}
}



