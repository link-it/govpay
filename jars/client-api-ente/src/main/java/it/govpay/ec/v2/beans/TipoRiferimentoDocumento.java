package it.govpay.ec.v2.beans;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class TipoRiferimentoDocumento  implements OneOfTipoRiferimentoDocumento  {
	
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "")
	  private VincoloPagamento soglia = null;
	 /**
	   * Get soglia
	   * @return soglia
	  **/
	  @JsonProperty("soglia")
	  public VincoloPagamento getSoglia() {
	    return soglia;
	  }

	  public void setSoglia(VincoloPagamento soglia) {
	    this.soglia = soglia;
	  }

	  public TipoRiferimentoDocumento soglia(VincoloPagamento soglia) {
	    this.soglia = soglia;
	    return this;
	  }
	  
	  @Schema(example = "1", requiredMode = RequiredMode.REQUIRED, description = "Rata del documento")
	  /**
	    * Rata del documento  
	   **/
	   private BigDecimal rata = null;
	  /**
	    * Rata del documento
	    * minimum: 1
	    * @return rata
	   **/
	   @JsonProperty("rata")
	  @DecimalMin("1")  public BigDecimal getRata() {
	     return rata;
	   }

	   public void setRata(BigDecimal rata) {
	     this.rata = rata;
	   }

	   public TipoRiferimentoDocumento rata(BigDecimal rata) {
	     this.rata = rata;
	     return this;
	   }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoRiferimentoDocumento {\n");
    
    sb.append("    soglia: ").append(toIndentedString(soglia)).append("\n");
    sb.append("    rata: ").append(toIndentedString(rata)).append("\n");
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
}
