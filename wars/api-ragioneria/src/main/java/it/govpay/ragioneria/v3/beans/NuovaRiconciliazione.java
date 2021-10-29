package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;

public class NuovaRiconciliazione extends TipoRiferimentoNuovaRiconciliazione implements IValidable {
  
  @Schema(example = "100.01", required = true, description = "Importo del riversamento. Se valorizzato, viene verificato che corrisponda a quello dei pagamenti riconciliati.")
 /**
   * Importo del riversamento. Se valorizzato, viene verificato che corrisponda a quello dei pagamenti riconciliati.  
  **/
  private BigDecimal importo = null;
  
  @Schema(example = "Thu Dec 31 01:00:00 CET 2020", description = "Data di valuta dell'incasso")
 /**
   * Data di valuta dell'incasso  
  **/
  private Date dataValuta = null;
  
  @Schema(example = "Thu Dec 31 01:00:00 CET 2020", description = "Data di contabile dell'incasso")
 /**
   * Data di contabile dell'incasso  
  **/
  private Date dataContabile = null;
  
  @Schema(example = "IT60X0542811101000000123456", description = "Identificativo del conto di tesoreria su cui sono stati incassati i fondi")
 /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi  
  **/
  private String contoAccredito = null;
  
  @Schema(example = "2017-01-01ABI00000011234", required = true, description = "Identificativo Sepa Credit Transfer")
 /**
   * Identificativo Sepa Credit Transfer  
  **/
  private String sct = null;
 /**
   * Importo del riversamento. Se valorizzato, viene verificato che corrisponda a quello dei pagamenti riconciliati.
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public NuovaRiconciliazione importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Data di valuta dell&#x27;incasso
   * @return dataValuta
  **/
  @JsonProperty("dataValuta")
  public Date getDataValuta() {
    return dataValuta;
  }

  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  public NuovaRiconciliazione dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

 /**
   * Data di contabile dell&#x27;incasso
   * @return dataContabile
  **/
  @JsonProperty("dataContabile")
  public Date getDataContabile() {
    return dataContabile;
  }

  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  public NuovaRiconciliazione dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

 /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   * @return contoAccredito
  **/
  @JsonProperty("contoAccredito")
  public String getContoAccredito() {
    return contoAccredito;
  }

  public void setContoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
  }

  public NuovaRiconciliazione contoAccredito(String contoAccredito) {
    this.contoAccredito = contoAccredito;
    return this;
  }

 /**
   * Identificativo Sepa Credit Transfer
   * @return sct
  **/
  @JsonProperty("sct")
  @NotNull
  public String getSct() {
    return sct;
  }

  public void setSct(String sct) {
    this.sct = sct;
  }

  public NuovaRiconciliazione sct(String sct) {
    this.sct = sct;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaRiconciliazione {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValuta: ").append(toIndentedString(dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(toIndentedString(dataContabile)).append("\n");
    sb.append("    contoAccredito: ").append(toIndentedString(contoAccredito)).append("\n");
    sb.append("    sct: ").append(toIndentedString(sct)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
  @Override
  public void validate() throws ValidationException {
	  super.validate();
	  
	ValidatorFactory vf = ValidatorFactory.newInstance();
	
	vf.getValidator("importo", this.importo).notNull().checkDecimalDigits();
	vf.getValidator("dataValuta", this.dataValuta);
	vf.getValidator("dataContabile", this.dataContabile);
	if(this.contoAccredito != null) {
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		validatoreId.validaIdIbanAccredito("contoAccredito", this.contoAccredito);
	}
	vf.getValidator("sct", this.sct).notNull().minLength(1).maxLength(35);
  }
}
