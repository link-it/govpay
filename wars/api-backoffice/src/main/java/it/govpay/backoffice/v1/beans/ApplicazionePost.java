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
	"principal",
	"codificaAvvisi",
	"domini",
	"tipiPendenza",
	"acl",
	"servizioVerifica",
	"servizioNotifica",
	"abilitato",
})
public class ApplicazionePost extends it.govpay.core.beans.JSONSerializable  implements IValidable {

	@JsonProperty("principal")
	private String principal = null;

	@JsonProperty("codificaAvvisi")
	private CodificaAvvisi codificaAvvisi = null;

	@JsonProperty("domini")
	private List<String> domini = null;

	@JsonProperty("tipiPendenza")
  	private List<String> tipiPendenza = null;

	@JsonProperty("acl")
	private List<AclPost> acl = null;

	@JsonProperty("servizioVerifica")
	private Connector servizioVerifica = null;

	@JsonProperty("servizioNotifica")
	private Connector servizioNotifica = null;

	@JsonProperty("abilitato")
	private Boolean abilitato = true;

	/**
	 * Identificativo di autenticazione
	 **/
	public ApplicazionePost principal(String principal) {
		this.principal = principal;
		return this;
	}

	@JsonProperty("principal")
	public String getPrincipal() {
		return this.principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	/**
	 **/
	public ApplicazionePost codificaAvvisi(CodificaAvvisi codificaAvvisi) {
		this.codificaAvvisi = codificaAvvisi;
		return this;
	}

	@JsonProperty("codificaAvvisi")
	public CodificaAvvisi getCodificaAvvisi() {
		return this.codificaAvvisi;
	}
	public void setCodificaAvvisi(CodificaAvvisi codificaAvvisi) {
		this.codificaAvvisi = codificaAvvisi;
	}

	/**
	 * domini su cui e' abilitato ad operare
	 **/
	public ApplicazionePost domini(List<String> domini) {
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
	   * tipologie di pendenza su cui e' abilitato ad operare
	   **/
	  public ApplicazionePost tipiPendenza(List<String> tipiPendenza) {
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
	public ApplicazionePost acl(List<AclPost> acl) {
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
	 **/
	public ApplicazionePost servizioVerifica(Connector servizioVerifica) {
		this.servizioVerifica = servizioVerifica;
		return this;
	}

	@JsonProperty("servizioVerifica")
	public Connector getServizioVerifica() {
		return this.servizioVerifica;
	}
	public void setServizioVerifica(Connector servizioVerifica) {
		this.servizioVerifica = servizioVerifica;
	}

	/**
	 **/
	public ApplicazionePost servizioNotifica(Connector servizioNotifica) {
		this.servizioNotifica = servizioNotifica;
		return this;
	}

	@JsonProperty("servizioNotifica")
	public Connector getServizioNotifica() {
		return this.servizioNotifica;
	}
	public void setServizioNotifica(Connector servizioNotifica) {
		this.servizioNotifica = servizioNotifica;
	}

	/**
	 * Indicazione se il creditore è abilitato ad operare sulla piattaforma
	 **/
	public ApplicazionePost abilitato(Boolean abilitato) {
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
		ApplicazionePost applicazionePost = (ApplicazionePost) o;
		return Objects.equals(this.principal, applicazionePost.principal) &&
				Objects.equals(this.codificaAvvisi, applicazionePost.codificaAvvisi) &&
				Objects.equals(this.domini, applicazionePost.domini) &&
				Objects.equals(this.tipiPendenza, applicazionePost.tipiPendenza) &&
				Objects.equals(this.acl, applicazionePost.acl) &&
				Objects.equals(this.servizioVerifica, applicazionePost.servizioVerifica) &&
				Objects.equals(this.servizioNotifica, applicazionePost.servizioNotifica) &&
				Objects.equals(this.abilitato, applicazionePost.abilitato);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.principal, this.codificaAvvisi, this.domini, this.tipiPendenza, this.acl, this.servizioVerifica, this.servizioNotifica, this.abilitato);
	}

	public static ApplicazionePost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, org.openspcoop2.utils.json.ValidationException {
		return parse(json, ApplicazionePost.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "applicazionePost";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ApplicazionePost {\n");

		sb.append("    principal: ").append(this.toIndentedString(this.principal)).append("\n");
		sb.append("    codificaAvvisi: ").append(this.toIndentedString(this.codificaAvvisi)).append("\n");
		sb.append("    domini: ").append(this.toIndentedString(this.domini)).append("\n");
		sb.append("    tipiPendenza: ").append(this.toIndentedString(this.tipiPendenza)).append("\n");
		sb.append("    acl: ").append(this.toIndentedString(this.acl)).append("\n");
		sb.append("    servizioVerifica: ").append(this.toIndentedString(this.servizioVerifica)).append("\n");
		sb.append("    servizioNotifica: ").append(this.toIndentedString(this.servizioNotifica)).append("\n");
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
		vf.getValidator("principal", this.principal).notNull().minLength(1).maxLength(4000);
		vf.getValidator("codificaAvvisi", this.codificaAvvisi).validateFields();
		vf.getValidator("servizioVerifica", this.servizioVerifica).validateFields();
		vf.getValidator("servizioNotifica", this.servizioNotifica).validateFields();
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
				if(!idTipoPendenza.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR) && 
						!idTipoPendenza.equals(ApplicazioniController.AUTODETERMINAZIONE_TIPI_PENDENZA_VALUE))
					validatoreId.validaIdTipoVersamento("tipiPendenza", idTipoPendenza);
			}
		}
		
		vf.getValidator("abilitato", this.abilitato).notNull();
	}
}



