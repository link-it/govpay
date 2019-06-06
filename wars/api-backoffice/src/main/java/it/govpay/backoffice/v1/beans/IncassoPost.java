package it.govpay.backoffice.v1.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"causale",
"iuv",
"idFlusso",
"importo",
"dataValuta",
"dataContabile",
"ibanAccredito",
"sct",
})
public class IncassoPost extends it.govpay.core.beans.JSONSerializable implements IValidable{
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("idFlusso")
  private String idFlusso = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("sct")
  private String sct = null;
  
  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera.
   **/
  public IncassoPost causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return this.causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }

  /**
   * Identificativo univoco di riscossione.
   **/
  public IncassoPost iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return this.iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Identificativo del flusso di rendicontazione.
   **/
  public IncassoPost idFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
    return this;
  }

  @JsonProperty("idFlusso")
  public String getIdFlusso() {
    return this.idFlusso;
  }
  public void setIdFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
  }

  /**
   **/
  public IncassoPost importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Data di valuta dell'incasso
   **/
  public IncassoPost dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

  @JsonProperty("dataValuta")
  public Date getDataValuta() {
    return this.dataValuta;
  }
  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  /**
   * Data di contabile dell'incasso
   **/
  public IncassoPost dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

  @JsonProperty("dataContabile")
  public Date getDataContabile() {
    return this.dataContabile;
  }
  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   **/
  public IncassoPost ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return this.ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  /**
   * Identificativo Sepa Credit Transfer
   **/
  public IncassoPost sct(String sct) {
    this.sct = sct;
    return this;
  }

  @JsonProperty("sct")
  public String getSct() {
    return sct;
  }
  public void setSct(String sct) {
    this.sct = sct;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    IncassoPost incassoPost = (IncassoPost) o;
    return Objects.equals(this.causale, incassoPost.causale) &&
    	Objects.equals(this.iuv, incassoPost.iuv) &&
    	Objects.equals(this.idFlusso, incassoPost.idFlusso) &&
        Objects.equals(this.importo, incassoPost.importo) &&
        Objects.equals(this.dataValuta, incassoPost.dataValuta) &&
        Objects.equals(this.dataContabile, incassoPost.dataContabile) &&
        Objects.equals(this.ibanAccredito, incassoPost.ibanAccredito) &&
        Objects.equals(sct, incassoPost.sct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(causale, iuv, idFlusso, importo, dataValuta, dataContabile, ibanAccredito, sct);
  }

  public static IncassoPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, IncassoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "incassoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncassoPost {\n");
    
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    idFlusso: ").append(this.toIndentedString(this.idFlusso)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    dataValuta: ").append(this.toIndentedString(this.dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(this.toIndentedString(this.dataContabile)).append("\n");
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
    sb.append("    sct: ").append(toIndentedString(sct)).append("\n");
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
	
	if(this.causale != null) {
		vf.getValidator("causale", this.causale).notNull().minLength(1).maxLength(512);
	} else if(this.iuv != null) {
		vf.getValidator("iuv", this.iuv).notNull().minLength(1).maxLength(35);
	} else if(this.idFlusso != null) {
		vf.getValidator("idFlusso", this.idFlusso).notNull().minLength(1).maxLength(35);
	} else {
		throw new ValidationException("Uno dei campi tra causale, iuv o idFlusso deve essere valorizzato");
	}
	
	vf.getValidator("importo", this.importo).notNull().checkDecimalDigits();
	vf.getValidator("dataValuta", this.dataValuta);
	vf.getValidator("dataContabile", this.dataContabile);
	if(this.ibanAccredito != null) {
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		validatoreId.validaIdIbanAccredito("ibanAccredito", this.ibanAccredito);
	}
	vf.getValidator("sct", this.sct).minLength(1).maxLength(35);
  }
}



