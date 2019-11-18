package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;

@JsonPropertyOrder({
"descrizione",
"tipo",
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
public class TipoPendenzaPost extends JSONSerializable implements IValidable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  private TipoPendenzaTipologia tipoEnum = null;
  
  @JsonProperty("tipo")
  private String tipo = null;
  
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
   **/
  public TipoPendenzaPost descrizione(String descrizione) {
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
   **/
  public TipoPendenzaPost TipoEnum(TipoPendenzaTipologia tipoEnum) {
    this.tipoEnum = tipoEnum;
    return this;
  }

  @JsonIgnore
  public TipoPendenzaTipologia getTipoEnum() {
    return tipoEnum;
  }
  public void setTipoEnum(TipoPendenzaTipologia tipoEnum) {
    this.tipoEnum = tipoEnum;
  }
  
  public TipoPendenzaPost tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   * Cifra identificativa negli IUV
   **/
  public TipoPendenzaPost codificaIUV(String codificaIUV) {
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
  public TipoPendenzaPost pagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  public Boolean PagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  /**
   * Indicazione la tipologia pendenza e' abilitata
   **/
  public TipoPendenzaPost abilitato(Boolean abilitato) {
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
  public TipoPendenzaPost form(TipoPendenzaForm form) {
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
  public TipoPendenzaPost validazione(Object validazione) {
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
  public TipoPendenzaPost trasformazione(TipoPendenzaTrasformazione trasformazione) {
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
  public TipoPendenzaPost inoltro(String inoltro) {
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
  public TipoPendenzaPost promemoriaAvviso(TipoPendenzaPromemoria promemoriaAvviso) {
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
  public TipoPendenzaPost promemoriaRicevuta(TipoPendenzaPromemoria promemoriaRicevuta) {
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
  public TipoPendenzaPost visualizzazione(Object visualizzazione) {
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
  public TipoPendenzaPost tracciatoCsv(TracciatoCsv tracciatoCsv) {
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
    TipoPendenzaPost tipoPendenzaPost = (TipoPendenzaPost) o;
    return Objects.equals(descrizione, tipoPendenzaPost.descrizione) &&
        Objects.equals(tipo, tipoPendenzaPost.tipo) &&
        Objects.equals(codificaIUV, tipoPendenzaPost.codificaIUV) &&
        Objects.equals(pagaTerzi, tipoPendenzaPost.pagaTerzi) &&
        Objects.equals(abilitato, tipoPendenzaPost.abilitato) &&
        Objects.equals(form, tipoPendenzaPost.form) &&
        Objects.equals(validazione, tipoPendenzaPost.validazione) &&
        Objects.equals(trasformazione, tipoPendenzaPost.trasformazione) &&
        Objects.equals(inoltro, tipoPendenzaPost.inoltro) &&
        Objects.equals(promemoriaAvviso, tipoPendenzaPost.promemoriaAvviso) &&
        Objects.equals(promemoriaRicevuta, tipoPendenzaPost.promemoriaRicevuta) &&
        Objects.equals(visualizzazione, tipoPendenzaPost.visualizzazione) &&
        Objects.equals(tracciatoCsv, tipoPendenzaPost.tracciatoCsv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, tipo, codificaIUV, pagaTerzi, abilitato, form, validazione, trasformazione, inoltro, promemoriaAvviso, promemoriaRicevuta, visualizzazione, tracciatoCsv);
  }

  public static TipoPendenzaPost parse(String json) throws ServiceException, ValidationException  {
    return (TipoPendenzaPost) parse(json, TipoPendenzaPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaPost {\n");
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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
	vf.getValidator("descrizione", this.descrizione).notNull().minLength(1).maxLength(255);
	vf.getValidator("codificaIUV", this.codificaIUV).minLength(1).maxLength(4).pattern("(^[0-9]{1,4}$)");
	vf.getValidator("tipo", this.tipo).notNull();
	vf.getValidator("pagaTerzi", this.pagaTerzi).notNull();
	vf.getValidator("abilitato", this.abilitato).notNull();
	vf.getValidator("form", this.form).validateFields();
	vf.getValidator("trasformazione", this.trasformazione).validateFields();
	
	try {
		vf.getValidator("promemoriaAvviso", this.promemoriaAvviso).validateFields();
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaAvviso' non valido: " + e.getMessage());
	}
	
	try {
		vf.getValidator("promemoriaRicevuta", this.promemoriaRicevuta).validateFields();
	}catch(ValidationException e) {
		throw new ValidationException("Field 'promemoriaRicevuta' non valido: " + e.getMessage());
	}
	
	vf.getValidator("tracciatoCsv", this.tracciatoCsv).validateFields();
	
	ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
	
	if(this.inoltro != null)
		validatoreId.validaIdApplicazione("inoltro", this.inoltro);
  }
}



