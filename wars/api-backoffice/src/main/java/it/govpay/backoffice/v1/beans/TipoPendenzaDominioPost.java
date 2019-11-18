package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@JsonPropertyOrder({
"codificaIUV",
"pagaTerzi",
"abilitato",
"form",
"validazione",
"trasformazione",
"inoltro",
"promemoriaAvviso",
"promemoriaRicevuta",
"visualizzazione",
"tracciatoCsv",
})
public class TipoPendenzaDominioPost extends JSONSerializable  implements IValidable {
  
  @JsonProperty("codificaIUV")
  private String codificaIUV = null;
  
  @JsonProperty("pagaTerzi")
  private Boolean pagaTerzi = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  @JsonProperty("form")
  private TipoPendenzaForm form = null;
  
  @JsonProperty("validazione")
  private Object validazione = null;
  
  @JsonProperty("trasformazione")
  private TipoPendenzaTrasformazione trasformazione = null;
  
  @JsonProperty("inoltro")
  private String inoltro = null;
  
  @JsonProperty("promemoriaAvviso")
  private TipoPendenzaPromemoria promemoriaAvviso = null;
  
  @JsonProperty("promemoriaRicevuta")
  private TipoPendenzaPromemoria promemoriaRicevuta = null;
  
  @JsonProperty("visualizzazione")
  private Object visualizzazione = null;
  
  @JsonProperty("tracciatoCsv")
  private TracciatoCsv tracciatoCsv = null;
  
  /**
   * Cifra identificativa negli IUV
   **/
  public TipoPendenzaDominioPost codificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public String getCodificaIUV() {
    return codificaIUV;
  }
  public void setCodificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   * Indica se la pendenza e' pagabile da soggetti terzi
   **/
  public TipoPendenzaDominioPost pagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = it.govpay.rs.v1.beans.deserializer.BooleanDeserializer.class)
  public Boolean PagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  /**
   * Indicazione la tipologia pendenza e' abilitata
   **/
  public TipoPendenzaDominioPost abilitato(Boolean abilitato) {
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
  public TipoPendenzaDominioPost form(TipoPendenzaForm form) {
    this.form = form;
    return this;
  }

  @JsonProperty("form")
  public TipoPendenzaForm getForm() {
    return form;
  }
  public void setForm(TipoPendenzaForm form) {
    this.form = form;
  }

  /**
   * JSON Schema da utilizzare per la validazione dell'input
   **/
  public TipoPendenzaDominioPost validazione(Object validazione) {
    this.validazione = validazione;
    return this;
  }

  @JsonProperty("validazione")
  public Object getValidazione() {
    return validazione;
  }
  public void setValidazione(Object validazione) {
    this.validazione = validazione;
  }

  /**
   **/
  public TipoPendenzaDominioPost trasformazione(TipoPendenzaTrasformazione trasformazione) {
    this.trasformazione = trasformazione;
    return this;
  }

  @JsonProperty("trasformazione")
  public TipoPendenzaTrasformazione getTrasformazione() {
    return trasformazione;
  }
  public void setTrasformazione(TipoPendenzaTrasformazione trasformazione) {
    this.trasformazione = trasformazione;
  }

  /**
   * Identificativo dell'applicazione verso cui fare l'inoltro della pendenza
   **/
  public TipoPendenzaDominioPost inoltro(String inoltro) {
    this.inoltro = inoltro;
    return this;
  }

  @JsonProperty("inoltro")
  public String getInoltro() {
    return inoltro;
  }
  public void setInoltro(String inoltro) {
    this.inoltro = inoltro;
  }

  /**
   **/
  public TipoPendenzaDominioPost promemoriaAvviso(TipoPendenzaPromemoria promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
    return this;
  }

  @JsonProperty("promemoriaAvviso")
  public TipoPendenzaPromemoria getPromemoriaAvviso() {
    return promemoriaAvviso;
  }
  public void setPromemoriaAvviso(TipoPendenzaPromemoria promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
  }

  /**
   **/
  public TipoPendenzaDominioPost promemoriaRicevuta(TipoPendenzaPromemoria promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
    return this;
  }

  @JsonProperty("promemoriaRicevuta")
  public TipoPendenzaPromemoria getPromemoriaRicevuta() {
    return promemoriaRicevuta;
  }
  public void setPromemoriaRicevuta(TipoPendenzaPromemoria promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
  }

  /**
   * Definisce come visualizzare la pendenza
   **/
  public TipoPendenzaDominioPost visualizzazione(Object visualizzazione) {
    this.visualizzazione = visualizzazione;
    return this;
  }

  @JsonProperty("visualizzazione")
  public Object getVisualizzazione() {
    return visualizzazione;
  }
  public void setVisualizzazione(Object visualizzazione) {
    this.visualizzazione = visualizzazione;
  }

  /**
   **/
  public TipoPendenzaDominioPost tracciatoCsv(TracciatoCsv tracciatoCsv) {
    this.tracciatoCsv = tracciatoCsv;
    return this;
  }

  @JsonProperty("tracciatoCsv")
  public TracciatoCsv getTracciatoCsv() {
    return tracciatoCsv;
  }
  public void setTracciatoCsv(TracciatoCsv tracciatoCsv) {
    this.tracciatoCsv = tracciatoCsv;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaDominioPost tipoPendenzaDominioPost = (TipoPendenzaDominioPost) o;
    return Objects.equals(codificaIUV, tipoPendenzaDominioPost.codificaIUV) &&
        Objects.equals(pagaTerzi, tipoPendenzaDominioPost.pagaTerzi) &&
        Objects.equals(abilitato, tipoPendenzaDominioPost.abilitato) &&
        Objects.equals(form, tipoPendenzaDominioPost.form) &&
        Objects.equals(validazione, tipoPendenzaDominioPost.validazione) &&
        Objects.equals(trasformazione, tipoPendenzaDominioPost.trasformazione) &&
        Objects.equals(inoltro, tipoPendenzaDominioPost.inoltro) &&
        Objects.equals(promemoriaAvviso, tipoPendenzaDominioPost.promemoriaAvviso) &&
        Objects.equals(promemoriaRicevuta, tipoPendenzaDominioPost.promemoriaRicevuta) &&
        Objects.equals(visualizzazione, tipoPendenzaDominioPost.visualizzazione) &&
        Objects.equals(tracciatoCsv, tipoPendenzaDominioPost.tracciatoCsv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codificaIUV, pagaTerzi, abilitato, form, validazione, trasformazione, inoltro, promemoriaAvviso, promemoriaRicevuta, visualizzazione, tracciatoCsv);
  }

  public static TipoPendenzaDominioPost parse(String json) throws ServiceException, ValidationException{
    return (TipoPendenzaDominioPost) parse(json, TipoPendenzaDominioPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaDominioPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaDominioPost {\n");
    
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    form: ").append(toIndentedString(form)).append("\n");
    sb.append("    validazione: ").append(toIndentedString(validazione)).append("\n");
    sb.append("    trasformazione: ").append(toIndentedString(trasformazione)).append("\n");
    sb.append("    inoltro: ").append(toIndentedString(inoltro)).append("\n");
    sb.append("    promemoriaAvviso: ").append(toIndentedString(promemoriaAvviso)).append("\n");
    sb.append("    promemoriaRicevuta: ").append(toIndentedString(promemoriaRicevuta)).append("\n");
    sb.append("    visualizzazione: ").append(toIndentedString(visualizzazione)).append("\n");
    sb.append("    tracciatoCsv: ").append(toIndentedString(tracciatoCsv)).append("\n");
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
	vf.getValidator("codificaIUV", this.codificaIUV).minLength(1).maxLength(4).pattern("(^[0-9]{1,4}$)");
	vf.getValidator("form", this.form).validateFields();
	vf.getValidator("trasformazione", this.trasformazione).validateFields();
	
	try {
		if(this.promemoriaAvviso != null)
			this.promemoriaAvviso.validate(false);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaAvviso' non valido: " + e.getMessage());
	}
	
	try {
		if(this.promemoriaRicevuta != null)
			this.promemoriaRicevuta.validate(false);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaRicevuta' non valido: " + e.getMessage());
	}
	
	vf.getValidator("tracciatoCsv", this.tracciatoCsv).validateFields();
	
//	vf.getValidator("promemoriaAvviso", this.promemoriaAvviso).validateFields();
//	vf.getValidator("promemoriaRicevuta", this.promemoriaRicevuta).validateFields();
	
	ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
	
	if(this.inoltro != null)
		validatoreId.validaIdApplicazione("inoltro", this.inoltro);
  }
}



