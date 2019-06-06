package it.govpay.ec.v1.beans;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

//import io.swagger.v3.oas.annotations.media.Schema;

public class DatiEntrata  {
  
  // @Schema(required = true, description = "")
  private String codEntrata = null;
  
  // @Schema(example = "IT60X0542811101000000123456", required = true, description = "")
  private String ibanAccredito = null;
  
  // @Schema(example = "DABAIE2D", description = "")
  private String bicAccredito = null;
  
  // @Schema(example = "IT60X0542811101000000123456", description = "")
  private String ibanAppoggio = null;
  
  // @Schema(example = "DABAIE2D", description = "")
  private String bicAppoggio = null;
  
  // @Schema(required = true, description = "")
  private TipoContabilita tipoContabilita = null;
  
  // @Schema(example = "3321", required = true, description = "Codifica del capitolo di bilancio")
 /**
   * Codifica del capitolo di bilancio  
  **/
  private String codiceContabilita = null;
  public enum TipoBolloEnum {
	IMPOSTA_BOLLO("01");

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
  // @Schema(required = true, description = "Tipologia di Bollo digitale")
 /**
   * Tipologia di Bollo digitale  
  **/
//  private TipoBolloEnum tipoBollo = null;
  private String tipoBollo = null;
  
  // @Schema(required = true, description = "Digest in base64 del documento informatico associato alla marca da bollo")
 /**
   * Digest in base64 del documento informatico associato alla marca da bollo  
  **/
  private String hashDocumento = null;
  
  // @Schema(required = true, description = "Sigla automobilistica della provincia di residenza del soggetto pagatore")
 /**
   * Sigla automobilistica della provincia di residenza del soggetto pagatore  
  **/
  private String provinciaResidenza = null;
 /**
   * Get codEntrata
   * @return codEntrata
  **/
  @JsonProperty("codEntrata")
  @NotNull
  @Valid
  public String getCodEntrata() {
    return codEntrata;
  }

  public void setCodEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
  }

  public DatiEntrata codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }

 /**
   * Get ibanAccredito
   * @return ibanAccredito
  **/
  @JsonProperty("ibanAccredito")
  @NotNull
  @Valid
  public String getIbanAccredito() {
    return ibanAccredito;
  }

  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  public DatiEntrata ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

 /**
   * Get bicAccredito
   * @return bicAccredito
  **/
  @JsonProperty("bicAccredito")
  @Valid
  public String getBicAccredito() {
    return bicAccredito;
  }

  public void setBicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
  }

  public DatiEntrata bicAccredito(String bicAccredito) {
    this.bicAccredito = bicAccredito;
    return this;
  }

 /**
   * Get ibanAppoggio
   * @return ibanAppoggio
  **/
  @JsonProperty("ibanAppoggio")
  @Valid
  public String getIbanAppoggio() {
    return ibanAppoggio;
  }

  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  public DatiEntrata ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

 /**
   * Get bicAppoggio
   * @return bicAppoggio
  **/
  @JsonProperty("bicAppoggio")
  @Valid
  public String getBicAppoggio() {
    return bicAppoggio;
  }

  public void setBicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
  }

  public DatiEntrata bicAppoggio(String bicAppoggio) {
    this.bicAppoggio = bicAppoggio;
    return this;
  }

 /**
   * Get tipoContabilita
   * @return tipoContabilita
  **/
  @JsonProperty("tipoContabilita")
  @NotNull
  @Valid
  public TipoContabilita getTipoContabilita() {
    return tipoContabilita;
  }

  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  public DatiEntrata tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

 /**
   * Codifica del capitolo di bilancio
   * @return codiceContabilita
  **/
  @JsonProperty("codiceContabilita")
  @NotNull
  @Valid
  public String getCodiceContabilita() {
    return codiceContabilita;
  }

  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  public DatiEntrata codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }

 /**
   * Tipologia di Bollo digitale
   * @return tipoBollo
  **/
//  @JsonProperty("tipoBollo")
//  @NotNull
//  @Valid
//  public String getTipoBollo() {
//    if (tipoBollo == null) {
//      return null;
//    }
//    return tipoBollo.getValue();
//  }
//
//  public void setTipoBollo(TipoBolloEnum tipoBollo) {
//    this.tipoBollo = tipoBollo;
//  }

//  public DatiEntrata tipoBollo(TipoBolloEnum tipoBollo) {
//    this.tipoBollo = tipoBollo;
//    return this;
//  }
  
	public DatiEntrata tipoBollo(String tipoBollo) {
		this.tipoBollo = tipoBollo;
		return this;
	}

	@JsonProperty("tipoBollo")
	public String getTipoBollo() {
		return this.tipoBollo;
	}
	public void setTipoBollo(String tipoBollo) {
		this.tipoBollo = tipoBollo;
	}

 /**
   * Digest in base64 del documento informatico associato alla marca da bollo
   * @return hashDocumento
  **/
  @JsonProperty("hashDocumento")
  @NotNull
  @Valid
  public String getHashDocumento() {
    return hashDocumento;
  }

  public void setHashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
  }

  public DatiEntrata hashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
    return this;
  }

 /**
   * Sigla automobilistica della provincia di residenza del soggetto pagatore
   * @return provinciaResidenza
  **/
  @JsonProperty("provinciaResidenza")
  @NotNull
  @Valid
  public String getProvinciaResidenza() {
    return provinciaResidenza;
  }

  public void setProvinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
  }

  public DatiEntrata provinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatiEntrata {\n");
    
    sb.append("    codEntrata: ").append(toIndentedString(codEntrata)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    bicAccredito: ").append(toIndentedString(bicAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    bicAppoggio: ").append(toIndentedString(bicAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    tipoBollo: ").append(toIndentedString(tipoBollo)).append("\n");
    sb.append("    hashDocumento: ").append(toIndentedString(hashDocumento)).append("\n");
    sb.append("    provinciaResidenza: ").append(toIndentedString(provinciaResidenza)).append("\n");
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
