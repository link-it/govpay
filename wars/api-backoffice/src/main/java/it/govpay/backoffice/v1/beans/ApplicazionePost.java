package it.govpay.backoffice.v1.beans;

import java.util.LinkedHashMap;
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
"apiPagamenti",
"apiPendenze",
"apiRagioneria",
"acl",
"ruoli",
"servizioIntegrazione",
"abilitato",
})
public class ApplicazionePost extends it.govpay.core.beans.JSONSerializable  implements IValidable {
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("codificaAvvisi")
  private CodificaAvvisi codificaAvvisi = null;
  
  @JsonProperty("domini")
  private List<Object> domini = null;
  
  @JsonProperty("tipiPendenza")
  private List<String> tipiPendenza = null;
  
  @JsonProperty("apiPagamenti")
  private Boolean apiPagamenti = false;
  
  @JsonProperty("apiPendenze")
  private Boolean apiPendenze = false;
  
  @JsonProperty("apiRagioneria")
  private Boolean apiRagioneria = false;
  
  @JsonProperty("acl")
  private List<AclPost> acl = null;
  
  @JsonProperty("ruoli")
  private List<String> ruoli = null;
  
  @JsonProperty("servizioIntegrazione")
  private Connector servizioIntegrazione = null;
  
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
    return principal;
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
    return codificaAvvisi;
  }
  public void setCodificaAvvisi(CodificaAvvisi codificaAvvisi) {
    this.codificaAvvisi = codificaAvvisi;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public ApplicazionePost domini(List<Object> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<Object> getDomini() {
    return domini;
  }
  public void setDomini(List<Object> domini) {
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
   * Indicazione l'applicazione e' abitata all'utilizzo delle API-Pagamento
   **/
  public ApplicazionePost apiPagamenti(Boolean apiPagamenti) {
    this.apiPagamenti = apiPagamenti;
    return this;
  }

  @JsonProperty("apiPagamenti")
  public Boolean ApiPagamenti() {
    return apiPagamenti;
  }
  public void setApiPagamenti(Boolean apiPagamenti) {
    this.apiPagamenti = apiPagamenti;
  }

  /**
   * Indicazione l'applicazione e' abitata all'utilizzo delle API-Pendenze
   **/
  public ApplicazionePost apiPendenze(Boolean apiPendenze) {
    this.apiPendenze = apiPendenze;
    return this;
  }

  @JsonProperty("apiPendenze")
  public Boolean ApiPendenze() {
    return apiPendenze;
  }
  public void setApiPendenze(Boolean apiPendenze) {
    this.apiPendenze = apiPendenze;
  }

  /**
   * Indicazione l'applicazione e' abitata all'utilizzo delle API-Ragioneria
   **/
  public ApplicazionePost apiRagioneria(Boolean apiRagioneria) {
    this.apiRagioneria = apiRagioneria;
    return this;
  }

  @JsonProperty("apiRagioneria")
  public Boolean ApiRagioneria() {
    return apiRagioneria;
  }
  public void setApiRagioneria(Boolean apiRagioneria) {
    this.apiRagioneria = apiRagioneria;
  }

  /**
   * lista delle acl attive sull'applicazione
   **/
  public ApplicazionePost acl(List<AclPost> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<AclPost> getAcl() {
    return acl;
  }
  public void setAcl(List<AclPost> acl) {
    this.acl = acl;
  }

  /**
   * lista dei ruoli attivi sull'applicazione
   **/
  public ApplicazionePost ruoli(List<String> ruoli) {
    this.ruoli = ruoli;
    return this;
  }

  @JsonProperty("ruoli")
  public List<String> getRuoli() {
    return ruoli;
  }
  public void setRuoli(List<String> ruoli) {
    this.ruoli = ruoli;
  }

  /**
   **/
  public ApplicazionePost servizioIntegrazione(Connector servizioIntegrazione) {
    this.servizioIntegrazione = servizioIntegrazione;
    return this;
  }

  @JsonProperty("servizioIntegrazione")
  public Connector getServizioIntegrazione() {
    return servizioIntegrazione;
  }
  public void setServizioIntegrazione(Connector servizioIntegrazione) {
    this.servizioIntegrazione = servizioIntegrazione;
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
    ApplicazionePost applicazionePost = (ApplicazionePost) o;
    return Objects.equals(principal, applicazionePost.principal) &&
        Objects.equals(codificaAvvisi, applicazionePost.codificaAvvisi) &&
        Objects.equals(domini, applicazionePost.domini) &&
        Objects.equals(tipiPendenza, applicazionePost.tipiPendenza) &&
        Objects.equals(apiPagamenti, applicazionePost.apiPagamenti) &&
        Objects.equals(apiPendenze, applicazionePost.apiPendenze) &&
        Objects.equals(apiRagioneria, applicazionePost.apiRagioneria) &&
        Objects.equals(acl, applicazionePost.acl) &&
        Objects.equals(ruoli, applicazionePost.ruoli) &&
        Objects.equals(servizioIntegrazione, applicazionePost.servizioIntegrazione) &&
        Objects.equals(abilitato, applicazionePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal, codificaAvvisi, domini, tipiPendenza, apiPagamenti, apiPendenze, apiRagioneria, acl, ruoli, servizioIntegrazione, abilitato);
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
    
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    codificaAvvisi: ").append(toIndentedString(codificaAvvisi)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
    sb.append("    apiPagamenti: ").append(toIndentedString(apiPagamenti)).append("\n");
    sb.append("    apiPendenze: ").append(toIndentedString(apiPendenze)).append("\n");
    sb.append("    apiRagioneria: ").append(toIndentedString(apiRagioneria)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    ruoli: ").append(toIndentedString(ruoli)).append("\n");
    sb.append("    servizioIntegrazione: ").append(toIndentedString(servizioIntegrazione)).append("\n");
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

	@Override
	public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("principal", this.principal).notNull().minLength(1).maxLength(4000);
		vf.getValidator("codificaAvvisi", this.codificaAvvisi).validateFields();
		vf.getValidator("servizioIntegrazione", this.servizioIntegrazione).validateFields();
		vf.getValidator("acl", this.acl).validateObjects();
		
		if(this.domini != null && !this.domini.isEmpty()) {
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			for (Object object : this.domini) {
				if(object instanceof String) {
					String idDominio = (String) object;
					if(!idDominio.equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR))
						validatoreId.validaIdDominio("domini", idDominio);
				} else if(object instanceof DominioProfiloPost) {
					DominioProfiloPost dominioProfiloPost = (DominioProfiloPost) object;
					if(!dominioProfiloPost.getIdDominio().equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR))
						dominioProfiloPost.validate();
				} else if(object instanceof java.util.LinkedHashMap) {
					java.util.LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) object;
					
					DominioProfiloPost dominioProfiloPost = new DominioProfiloPost();
					if(map.containsKey("idDominio"))
						dominioProfiloPost.setIdDominio((String) map.get("idDominio"));
					if(map.containsKey("unitaOperative")) {
						Object objectUnita = map.get("unitaOperative");
						
						dominioProfiloPost.setUnitaOperative(null);
						if(objectUnita != null) {
							if(objectUnita instanceof List) {
								
								List<?> unitaOperativeTmp = (List<?>) objectUnita;
								if (unitaOperativeTmp.stream().allMatch(String.class::isInstance)) {
									@SuppressWarnings("unchecked")
									List<String> unitaOperative = (List<String>) objectUnita;
									dominioProfiloPost.setUnitaOperative(unitaOperative);
								} else {
									throw new ValidationException("Tipo non valido per il campo unitaOperative");
								}
							} else {
								throw new ValidationException("Tipo non valido per il campo unitaOperative");
							}
						}
					}
					
					if(dominioProfiloPost.getIdDominio() == null)
						validatoreId.validaIdDominio("idDominio", dominioProfiloPost.getIdDominio());
					
//					DominioProfiloPost dominioProfiloPost = (DominioProfiloPost) object;
					if(!dominioProfiloPost.getIdDominio().equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR))
						dominioProfiloPost.validate();
				} else {
					throw new ValidationException("Tipo non valido per il campo domini");
				}
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
		
		if(this.ruoli != null && !this.ruoli.isEmpty()) {
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			for (String idRuolo : this.ruoli) {
				validatoreId.validaIdRuolo("ruoli", idRuolo);
			}
		}
		
		vf.getValidator("abilitato", this.abilitato).notNull();
		vf.getValidator("apiPagamenti", this.apiPagamenti).notNull();
		vf.getValidator("apiPendenze", this.apiPendenze).notNull();
		vf.getValidator("apiRagioneria", this.apiRagioneria).notNull();
	}
}



