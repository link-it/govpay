/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.ec.v2.beans;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

public class TipoRiferimentoVocePendenza  implements OneOfTipoRiferimentoVocePendenza  {
	
	public enum TipoBolloEnum {
	    _01("01");

	    private String value;

	    TipoBolloEnum(String value) {
	      this.value = value;
	    }
	    @JsonValue
	    public String getValue() {
	      return value;
	    }

	    @Override
	    public String toString() {
	      return String.valueOf(value);
	    }
	    @JsonCreator
	    public static TipoBolloEnum fromValue(String text) {
	      for (TipoBolloEnum b : TipoBolloEnum.values()) {
	        if (String.valueOf(b.value).equals(text)) {
	          return b;
	        }
	      }
	      return null;
	    }
	  }  
	  @Schema(requiredMode = RequiredMode.REQUIRED, description = "Tipologia di Bollo digitale")
	 /**
	   * Tipologia di Bollo digitale  
	  **/
	  private TipoBolloEnum tipoBollo = null;
	  
	  @Schema(example = "--- base64 ---", requiredMode = RequiredMode.REQUIRED, description = "Digest in base64 del documento informatico associato alla marca da bollo")
	 /**
	   * Digest in base64 del documento informatico associato alla marca da bollo  
	  **/
	  private String hashDocumento = null;
	  
	  @Schema(example = "RO", requiredMode = RequiredMode.REQUIRED, description = "Sigla automobilistica della provincia di residenza del soggetto pagatore")
	 /**
	   * Sigla automobilistica della provincia di residenza del soggetto pagatore  
	  **/
	  private String provinciaResidenza = null;
	  
	  @Schema(example = "9/3321", description = "Tassonomia pagoPA")
	 /**
	   * Tassonomia pagoPA  
	  **/
	  private String codiceTassonomicoPagoPA = null;
	 /**
	   * Tipologia di Bollo digitale
	   * @return tipoBollo
	  **/
	  @JsonProperty("tipoBollo")
	  public String getTipoBollo() {
	    if (tipoBollo == null) {
	      return null;
	    }
	    return tipoBollo.getValue();
	  }

	  public void setTipoBollo(TipoBolloEnum tipoBollo) {
	    this.tipoBollo = tipoBollo;
	  }

	  public TipoRiferimentoVocePendenza tipoBollo(TipoBolloEnum tipoBollo) {
	    this.tipoBollo = tipoBollo;
	    return this;
	  }

	 /**
	   * Digest in base64 del documento informatico associato alla marca da bollo
	   * @return hashDocumento
	  **/
	  @JsonProperty("hashDocumento")
	 @Size(max=70)  public String getHashDocumento() {
	    return hashDocumento;
	  }

	  public void setHashDocumento(String hashDocumento) {
	    this.hashDocumento = hashDocumento;
	  }

	  public TipoRiferimentoVocePendenza hashDocumento(String hashDocumento) {
	    this.hashDocumento = hashDocumento;
	    return this;
	  }

	 /**
	   * Sigla automobilistica della provincia di residenza del soggetto pagatore
	   * @return provinciaResidenza
	  **/
	  @JsonProperty("provinciaResidenza")
	 @Pattern(regexp="[A-Z]{2,2}")  public String getProvinciaResidenza() {
	    return provinciaResidenza;
	  }

	  public void setProvinciaResidenza(String provinciaResidenza) {
	    this.provinciaResidenza = provinciaResidenza;
	  }

	  public TipoRiferimentoVocePendenza provinciaResidenza(String provinciaResidenza) {
	    this.provinciaResidenza = provinciaResidenza;
	    return this;
	  }

	 /**
	   * Tassonomia pagoPA
	   * @return codiceTassonomicoPagoPA
	  **/
	  @JsonProperty("codiceTassonomicoPagoPA")
	 @Size(min=1,max=140)  public String getCodiceTassonomicoPagoPA() {
	    return codiceTassonomicoPagoPA;
	  }

	  public void setCodiceTassonomicoPagoPA(String codiceTassonomicoPagoPA) {
	    this.codiceTassonomicoPagoPA = codiceTassonomicoPagoPA;
	  }

	  public TipoRiferimentoVocePendenza codiceTassonomicoPagoPA(String codiceTassonomicoPagoPA) {
	    this.codiceTassonomicoPagoPA = codiceTassonomicoPagoPA;
	    return this;
	  }
	  
	  @Schema(example = "IT60X0542811101000000123456", requiredMode = RequiredMode.REQUIRED, description = "")
	  private String ibanAccredito = null;
	  
	  @Schema(example = "IT60X0542811101000000123456", description = "")
	  private String ibanAppoggio = null;
	  
	 /**
	   * Get ibanAccredito
	   * @return ibanAccredito
	  **/
	  @JsonProperty("ibanAccredito")
	 @Pattern(regexp="[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")  public String getIbanAccredito() {
	    return ibanAccredito;
	  }

	  public void setIbanAccredito(String ibanAccredito) {
	    this.ibanAccredito = ibanAccredito;
	  }

	  public TipoRiferimentoVocePendenza ibanAccredito(String ibanAccredito) {
	    this.ibanAccredito = ibanAccredito;
	    return this;
	  }

	 /**
	   * Get ibanAppoggio
	   * @return ibanAppoggio
	  **/
	  @JsonProperty("ibanAppoggio")
	 @Pattern(regexp="[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")  public String getIbanAppoggio() {
	    return ibanAppoggio;
	  }

	  public void setIbanAppoggio(String ibanAppoggio) {
	    this.ibanAppoggio = ibanAppoggio;
	  }

	  public TipoRiferimentoVocePendenza ibanAppoggio(String ibanAppoggio) {
	    this.ibanAppoggio = ibanAppoggio;
	    return this;
	  }
	  
	  @Schema(example = "SRV-12345", requiredMode = RequiredMode.REQUIRED, description = "")
	  private String codEntrata = null;
	 /**
	   * Get codEntrata
	   * @return codEntrata
	  **/
	  @JsonProperty("codEntrata")
	 @Pattern(regexp="(^[a-zA-Z0-9\\-_\\.]{1,35}$)")  public String getCodEntrata() {
	    return codEntrata;
	  }

	  public void setCodEntrata(String codEntrata) {
	    this.codEntrata = codEntrata;
	  }

	  public TipoRiferimentoVocePendenza codEntrata(String codEntrata) {
	    this.codEntrata = codEntrata;
	    return this;
	  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoRiferimentoVocePendenza {\n");
    
    sb.append("    tipoBollo: ").append(toIndentedString(tipoBollo)).append("\n");
    sb.append("    hashDocumento: ").append(toIndentedString(hashDocumento)).append("\n");
    sb.append("    provinciaResidenza: ").append(toIndentedString(provinciaResidenza)).append("\n");
    sb.append("    codiceTassonomicoPagoPA: ").append(toIndentedString(codiceTassonomicoPagoPA)).append("\n");
    
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    
    sb.append("    codEntrata: ").append(toIndentedString(codEntrata)).append("\n");
    
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
