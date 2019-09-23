package it.govpay.ragioneria.v2.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"importo",
"dataValuta",
"dataContabile",
"contoAccredito",
"sct",
"causale",
"iuv",
"idFlusso",
})
public class NuovaRiconciliazione extends JSONSerializable  implements IValidable {
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("contoAccredito")
  private String contoAccredito = null;
  
  @JsonProperty("sct")
  private String sct = null;

  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("idFlusso")
  private String idFlusso = null;
  
  /**
   **/
  public NuovaRiconciliazione importo(BigDecimal importo) {
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
  public NuovaRiconciliazione dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

  @JsonProperty("dataValuta")
  public Date getDataValuta() {
    return dataValuta;
  }
  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  /**
   * Data di contabile dell'incasso
   **/
  public NuovaRiconciliazione dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

  @JsonProperty("dataContabile")
  public Date getDataContabile() {
    return dataContabile;
  }
  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   **/
  public NuovaRiconciliazione contoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
    return this;
  }

  @JsonProperty("contoAccredito")
  public String getContoAccredito() {
    return contoAccredito;
  }
  public void setContoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
  }

  /**
   * Identificativo Sepa Credit Transfer
   **/
  public NuovaRiconciliazione sct(String sct) {
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

  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera
   **/
  public NuovaRiconciliazione causale(String causale) {
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
  public NuovaRiconciliazione iuv(String iuv) {
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
  public NuovaRiconciliazione idFlusso(String idFlusso) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NuovaRiconciliazione nuovaRiconciliazione = (NuovaRiconciliazione) o;
    return Objects.equals(importo, nuovaRiconciliazione.importo) &&
        Objects.equals(dataValuta, nuovaRiconciliazione.dataValuta) &&
        Objects.equals(dataContabile, nuovaRiconciliazione.dataContabile) &&
        Objects.equals(contoAccredito, nuovaRiconciliazione.contoAccredito) &&
        Objects.equals(sct, nuovaRiconciliazione.sct) &&
	    Objects.equals(this.causale, nuovaRiconciliazione.causale) &&
	    Objects.equals(this.iuv, nuovaRiconciliazione.iuv) &&
    	Objects.equals(this.idFlusso, nuovaRiconciliazione.idFlusso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(importo, dataValuta, dataContabile, contoAccredito, sct, causale, iuv, idFlusso);
  }

  public static NuovaRiconciliazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, NuovaRiconciliazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovaRiconciliazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaRiconciliazione {\n");
    
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    contoAccredito: ").append(toIndentedString(contoAccredito)).append("\n");
    sb.append("    sct: ").append(toIndentedString(sct)).append("\n");
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    idFlusso: ").append(this.toIndentedString(this.idFlusso)).append("\n");
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
	if(this.contoAccredito != null) {
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		validatoreId.validaIdIbanAccredito("contoAccredito", this.contoAccredito);
	}
	vf.getValidator("sct", this.sct).minLength(1).maxLength(35);
  }
  
}



