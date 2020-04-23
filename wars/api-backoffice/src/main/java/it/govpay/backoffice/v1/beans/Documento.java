package it.govpay.backoffice.v1.beans;


import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"identificativo",
"descrizione",
"rata",
})
public class Documento extends JSONSerializable implements IValidable {
  
  @JsonProperty("identificativo")
  private String identificativo = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("rata")
  private BigDecimal rata = null;
  
  /**
   * Identificativo del documento
   **/
  public Documento identificativo(String identificativo) {
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
  public Documento descrizione(String descrizione) {
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
   **/
  public Documento rata(BigDecimal rata) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Documento documento = (Documento) o;
    return Objects.equals(identificativo, documento.identificativo) &&
        Objects.equals(descrizione, documento.descrizione) &&
        Objects.equals(rata, documento.rata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identificativo, descrizione, rata);
  }

  public static Documento parse(String json) throws ServiceException, ValidationException {
    return (Documento) parse(json, Documento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "documento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Documento {\n");
    
    sb.append("    identificativo: ").append(toIndentedString(identificativo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    rata: ").append(toIndentedString(rata)).append("\n");
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
		vf.getValidator("rata", this.rata).notNull().min(BigDecimal.ONE); 
  }
}



