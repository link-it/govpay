package it.govpay.ragioneria.v3.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
  * Identificativo del flusso di rendicontazione.
 **/
@Schema(description="Identificativo del flusso di rendicontazione.")
public class RiferimentoIdentificativoFlussoRendicontazione  implements OneOfTipoRiferimentoNuovaRiconciliazione, OneOfTipoRiferimentoRiconciliazione  {
  
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

  public RiferimentoIdentificativoFlussoRendicontazione idFlussoRendicontazione(String idFlussoRendicontazione) {
    this.idFlussoRendicontazione = idFlussoRendicontazione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoIdentificativoFlussoRendicontazione {\n");
    
    sb.append("    idFlussoRendicontazione: ").append(toIndentedString(idFlussoRendicontazione)).append("\n");
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
