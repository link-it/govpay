package it.govpay.pendenze.v2.beans;


import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"identificativo",
"descrizione",
"rata",
"soglia",
})
public class NuovoDocumento extends JSONSerializable implements IValidable {

  @JsonProperty("identificativo")
  private String identificativo = null;

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("rata")
  private BigDecimal rata = null;

  @JsonProperty("soglia")
  private VincoloPagamento soglia = null;

  /**
   * Identificativo del documento
   **/
  public NuovoDocumento identificativo(String identificativo) {
    this.identificativo = identificativo;
    return this;
  }

  @JsonProperty("identificativo")
  public String getIdentificativo() {
    return identificativo;
  }
  public void setIdentificativo(String identificativo) {
    this.identificativo = identificativo;
  }

  /**
   * descrizione del documento
   **/
  public NuovoDocumento descrizione(String descrizione) {
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
   * Rata del documento
   * minimum: 1
   **/
  public NuovoDocumento rata(BigDecimal rata) {
    this.rata = rata;
    return this;
  }

  @JsonProperty("rata")
  public BigDecimal getRata() {
    return rata;
  }
  public void setRata(BigDecimal rata) {
    this.rata = rata;
  }

  /**
   **/
  public NuovoDocumento soglia(VincoloPagamento soglia) {
    this.soglia = soglia;
    return this;
  }

  @JsonProperty("soglia")
  public VincoloPagamento getSoglia() {
    return soglia;
  }
  public void setSoglia(VincoloPagamento soglia) {
    this.soglia = soglia;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NuovoDocumento nuovoDocumento = (NuovoDocumento) o;
    return Objects.equals(identificativo, nuovoDocumento.identificativo) &&
        Objects.equals(descrizione, nuovoDocumento.descrizione) &&
        Objects.equals(rata, nuovoDocumento.rata) &&
        Objects.equals(soglia, nuovoDocumento.soglia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identificativo, descrizione, rata, soglia);
  }

  public static NuovoDocumento parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, NuovoDocumento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovoDocumento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovoDocumento {\n");

    sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    rata: ").append(toIndentedString(rata)).append("\n");
    sb.append("    soglia: ").append(toIndentedString(soglia)).append("\n");
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

		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

		validatoreId.validaIdDocumento("identificativo", this.identificativo);
		vf.getValidator("descrizione", this.descrizione).notNull().minLength(1).maxLength(255);
		if(this.rata != null) {
			ValidatoreUtils.validaRata(vf, "rata", this.rata);
		} else if(this.soglia != null) {
			vf.getValidator("soglia", this.soglia).validateFields();
		}
  }
}



