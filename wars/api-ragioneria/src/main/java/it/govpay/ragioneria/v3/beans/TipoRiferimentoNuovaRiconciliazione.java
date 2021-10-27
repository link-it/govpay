package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

public class TipoRiferimentoNuovaRiconciliazione  implements OneOfTipoRiferimentoNuovaRiconciliazione, IValidable  {
	
	
	@Schema(example = "/PUR/LGPE-RIVERSAMENTO/URI/2017-01-01ABI00000011234", required = true, description = "")
	  private String causale = null;
	 /**
	   * Get causale
	   * @return causale
	  **/
	  @JsonProperty("causale")
	  @NotNull
	  public String getCausale() {
	    return causale;
	  }
	
	  public void setCausale(String causale) {
	    this.causale = causale;
	  }
	
	  public TipoRiferimentoNuovaRiconciliazione causale(String causale) {
	    this.causale = causale;
	    return this;
	  }
	  
	  @Schema(example = "2017-11-21GovPAYPsp1-10:27:27.903", required = true, description = "")
	  private String idFlussoRendicontazione = null;
	 /**
	   * Get idFlussoRendicontazione
	   * @return idFlussoRendicontazione
	  **/
	  @JsonProperty("idFlussoRendicontazione")
	  @NotNull
	  public String getIdFlussoRendicontazione() {
	    return idFlussoRendicontazione;
	  }

	  public void setIdFlussoRendicontazione(String idFlussoRendicontazione) {
	    this.idFlussoRendicontazione = idFlussoRendicontazione;
	  }

	  public TipoRiferimentoNuovaRiconciliazione idFlussoRendicontazione(String idFlussoRendicontazione) {
	    this.idFlussoRendicontazione = idFlussoRendicontazione;
	    return this;
	  }
	  
	  @Schema(example = "RF23567483937849450550875", required = true, description = "")
	  private String iuv = null;
	 /**
	   * Get iuv
	   * @return iuv
	  **/
	  @JsonProperty("iuv")
	  @NotNull
	  public String getIuv() {
	    return iuv;
	  }

	  public void setIuv(String iuv) {
	    this.iuv = iuv;
	  }

	  public TipoRiferimentoNuovaRiconciliazione iuv(String iuv) {
	    this.iuv = iuv;
	    return this;
	  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoRiferimentoNuovaRiconciliazione {\n");
    
    sb.append("    causale: ").append(toIndentedString(causale)).append("\n");
    sb.append("    idFlussoRendicontazione: ").append(toIndentedString(idFlussoRendicontazione)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
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
	ValidatorFactory vf = ValidatorFactory.newInstance();
	
	if(this.causale != null) {
		vf.getValidator("causale", this.causale).notNull().minLength(1).maxLength(512);
	} else if(this.iuv != null) {
		vf.getValidator("iuv", this.iuv).notNull().minLength(1).maxLength(35);
	} else if(this.idFlussoRendicontazione != null) {
		vf.getValidator("idFlussoRendicontazione", this.idFlussoRendicontazione).notNull().minLength(1).maxLength(35);
	} else {
		throw new ValidationException("Uno dei campi tra causale, iuv o idFlusso deve essere valorizzato");
	}
  }
}
