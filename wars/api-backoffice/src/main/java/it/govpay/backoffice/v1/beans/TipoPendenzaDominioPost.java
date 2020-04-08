package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@JsonPropertyOrder({
"codificaIUV",
"pagaTerzi",
"abilitato",
"portaleBackoffice",
"portalePagamento",
"avvisaturaMail",
"avvisaturaAppIO",
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
  
  @JsonProperty("portaleBackoffice")
  private TipoPendenzaPortaleBackofficeCaricamentoPendenze portaleBackoffice = null;
  
  @JsonProperty("portalePagamento")
  private TipoPendenzaPortalePagamentiCaricamentoPendenze portalePagamento = null;
  
  @JsonProperty("avvisaturaMail")
  private TipoPendenzaAvvisaturaMail avvisaturaMail = null;
  
  @JsonProperty("avvisaturaAppIO")
  private TipoPendenzaDominioAvvisaturaAppIO avvisaturaAppIO = null;
  
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
  public TipoPendenzaDominioPost portaleBackoffice(TipoPendenzaPortaleBackofficeCaricamentoPendenze portaleBackoffice) {
    this.portaleBackoffice = portaleBackoffice;
    return this;
  }

  @JsonProperty("portaleBackoffice")
  public TipoPendenzaPortaleBackofficeCaricamentoPendenze getPortaleBackoffice() {
    return portaleBackoffice;
  }
  public void setPortaleBackoffice(TipoPendenzaPortaleBackofficeCaricamentoPendenze portaleBackoffice) {
    this.portaleBackoffice = portaleBackoffice;
  }

  /**
   **/
  public TipoPendenzaDominioPost portalePagamento(TipoPendenzaPortalePagamentiCaricamentoPendenze portalePagamento) {
    this.portalePagamento = portalePagamento;
    return this;
  }

  @JsonProperty("portalePagamento")
  public TipoPendenzaPortalePagamentiCaricamentoPendenze getPortalePagamento() {
    return portalePagamento;
  }
  public void setPortalePagamento(TipoPendenzaPortalePagamentiCaricamentoPendenze portalePagamento) {
    this.portalePagamento = portalePagamento;
  }

  /**
   **/
  public TipoPendenzaDominioPost avvisaturaMail(TipoPendenzaAvvisaturaMail avvisaturaMail) {
    this.avvisaturaMail = avvisaturaMail;
    return this;
  }

  @JsonProperty("avvisaturaMail")
  public TipoPendenzaAvvisaturaMail getAvvisaturaMail() {
    return avvisaturaMail;
  }
  public void setAvvisaturaMail(TipoPendenzaAvvisaturaMail avvisaturaMail) {
    this.avvisaturaMail = avvisaturaMail;
  }

  /**
   **/
  public TipoPendenzaDominioPost avvisaturaAppIO(TipoPendenzaDominioAvvisaturaAppIO avvisaturaAppIO) {
    this.avvisaturaAppIO = avvisaturaAppIO;
    return this;
  }

  @JsonProperty("avvisaturaAppIO")
  public TipoPendenzaDominioAvvisaturaAppIO getAvvisaturaAppIO() {
    return avvisaturaAppIO;
  }
  public void setAvvisaturaAppIO(TipoPendenzaDominioAvvisaturaAppIO avvisaturaAppIO) {
    this.avvisaturaAppIO = avvisaturaAppIO;
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
        Objects.equals(portaleBackoffice, tipoPendenzaDominioPost.portaleBackoffice) &&
        Objects.equals(portalePagamento, tipoPendenzaDominioPost.portalePagamento) &&
        Objects.equals(avvisaturaMail, tipoPendenzaDominioPost.avvisaturaMail) &&
        Objects.equals(avvisaturaAppIO, tipoPendenzaDominioPost.avvisaturaAppIO) &&
        Objects.equals(visualizzazione, tipoPendenzaDominioPost.visualizzazione) &&
        Objects.equals(tracciatoCsv, tipoPendenzaDominioPost.tracciatoCsv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codificaIUV, pagaTerzi, abilitato, portaleBackoffice, portalePagamento, avvisaturaMail, avvisaturaAppIO, visualizzazione, tracciatoCsv);
  }

  public static TipoPendenzaDominioPost parse(String json) throws ServiceException, ValidationException{
    return parse(json, TipoPendenzaDominioPost.class);
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
    sb.append("    portaleBackoffice: ").append(toIndentedString(portaleBackoffice)).append("\n");
    sb.append("    portalePagamento: ").append(toIndentedString(portalePagamento)).append("\n");
    sb.append("    avvisaturaMail: ").append(toIndentedString(avvisaturaMail)).append("\n");
    sb.append("    avvisaturaAppIO: ").append(toIndentedString(avvisaturaAppIO)).append("\n");
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
	
	try {
		if(this.portaleBackoffice != null)
			this.portaleBackoffice.validate(false);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'portaleBackoffice' non valido: " + e.getMessage());
	}
	try {
		if(this.portalePagamento != null)
			this.portalePagamento.validate(false);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'portalePagamento' non valido: " + e.getMessage());
	}
	try {
		if(this.avvisaturaMail != null)
			this.avvisaturaMail.validate(false);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'avvisaturaMail' non valido: " + e.getMessage());
	}
	try {
		if(this.avvisaturaAppIO != null)
			this.avvisaturaAppIO.validate(false);
	}catch(ValidationException e) {
		throw new ValidationException("Field 'avvisaturaAppIO' non valido: " + e.getMessage());
	}
	
	vf.getValidator("tracciatoCsv", this.tracciatoCsv).validateFields();
  }
}



