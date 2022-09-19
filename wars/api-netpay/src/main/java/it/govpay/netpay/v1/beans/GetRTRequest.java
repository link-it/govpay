package it.govpay.netpay.v1.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;

public class GetRTRequest  implements IValidable  {
  
  @Schema(example = "12345678901", required = true, description = "Identificativo dell'Ente Creditore per il quale si fa il pagamento")
 /**
   * Identificativo dell'Ente Creditore per il quale si fa il pagamento  
  **/
  private String domainId = null;
  
  @Schema(example = "01000000000012345", required = true, description = "IUV della richiesta di pagamento")
 /**
   * IUV della richiesta di pagamento  
  **/
  private String creditorTxId = null;
 /**
   * Identificativo dell&#x27;Ente Creditore per il quale si fa il pagamento
   * @return domainId
  **/
  @JsonProperty("domainId")
  @NotNull
 @Pattern(regexp="^([0-9]){35}$") @Size(max=35)  public String getDomainId() {
    return domainId;
  }

  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }

  public GetRTRequest domainId(String domainId) {
    this.domainId = domainId;
    return this;
  }

 /**
   * IUV della richiesta di pagamento
   * @return creditorTxId
  **/
  @JsonProperty("creditorTxId")
  @NotNull
 @Pattern(regexp="^([\\w]){1,35}$") @Size(max=35)  public String getCreditorTxId() {
    return creditorTxId;
  }

  public void setCreditorTxId(String creditorTxId) {
    this.creditorTxId = creditorTxId;
  }

  public GetRTRequest creditorTxId(String creditorTxId) {
    this.creditorTxId = creditorTxId;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRTRequest {\n");
    
    sb.append("    domainId: ").append(toIndentedString(domainId)).append("\n");
    sb.append("    creditorTxId: ").append(toIndentedString(creditorTxId)).append("\n");
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
	ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

	vf.getValidator("domainId", this.domainId).notNull();
	validatoreId.validaIdDominio("domainId", domainId);
	vf.getValidator("creditorTxId", this.creditorTxId).notNull().minLength(1).maxLength(35);
  }
  
}
