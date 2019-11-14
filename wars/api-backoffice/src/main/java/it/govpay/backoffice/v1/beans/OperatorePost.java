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
"ragioneSociale",
"domini",
"tipiPendenza",
"acl",
"ruoli",
"abilitato",
})
public class OperatorePost extends it.govpay.core.beans.JSONSerializable implements IValidable{
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  @JsonProperty("domini")
  private List<Object> domini = null;
  
  @JsonProperty("tipiPendenza")
  private List<String> tipiPendenza = null;
  
  @JsonProperty("acl")
  private List<AclPost> acl = null;
  
  @JsonProperty("ruoli")
  private List<String> ruoli = null;
  
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
    return ragioneSociale;
  }
  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  /**
   * domini su cui e' abilitato ad operare. Se la lista e' vuota, l'abilitazione e' per nessun dominio
   **/
  public OperatorePost domini(List<Object> domini) {
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
    return acl;
  }
  public void setAcl(List<AclPost> acl) {
    this.acl = acl;
  }

  /**
   * lista dei ruoli attivi sull'operatore
   **/
  public OperatorePost ruoli(List<String> ruoli) {
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
   * Indicazione se l'operatore è abilitato ad operare sulla piattaforma
   **/
  public OperatorePost abilitato(Boolean abilitato) {
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
    OperatorePost operatorePost = (OperatorePost) o;
    return Objects.equals(ragioneSociale, operatorePost.ragioneSociale) &&
        Objects.equals(domini, operatorePost.domini) &&
        Objects.equals(tipiPendenza, operatorePost.tipiPendenza) &&
        Objects.equals(acl, operatorePost.acl) &&
        Objects.equals(ruoli, operatorePost.ruoli) &&
        Objects.equals(abilitato, operatorePost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ragioneSociale, domini, tipiPendenza, acl, ruoli, abilitato);
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
    
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    ruoli: ").append(toIndentedString(ruoli)).append("\n");
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
		vf.getValidator("ragioneSociale", this.ragioneSociale).notNull().minLength(1).maxLength(35);
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
				}  else if(object instanceof java.util.LinkedHashMap) {
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
				if(!idTipoPendenza.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR))
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
	}
}



