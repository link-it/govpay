package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Bollo;
import it.govpay.pagamento.v2.beans.Entrata;
import it.govpay.pagamento.v2.beans.RiferimentoEntrata;
import it.govpay.pagamento.v2.beans.TipoContabilita;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoRiferimentoVocePendenza  {
  
  @Schema(required = true, description = "")
  private String codEntrata = null;
  
  @Schema(example = "IT60X0542811101000000123456", required = true, description = "")
  private String ibanAccredito = null;
  
  @Schema(example = "IT60X0542811101000000123456", description = "")
  private String ibanAppoggio = null;
  
  @Schema(required = true, description = "")
  private TipoContabilita tipoContabilita = null;
  
  @Schema(example = "3321", required = true, description = "Codifica del capitolo di bilancio")
 /**
   * Codifica del capitolo di bilancio  
  **/
  private String codiceContabilita = null;
@XmlType(name="TipoBolloEnum")
@XmlEnum(String.class)
public enum TipoBolloEnum {

@XmlEnumValue("Imposta di bollo") BOLLO(String.valueOf("Imposta di bollo"));


    private String value;

    TipoBolloEnum (String v) {
        value = v;
    }

    @com.fasterxml.jackson.annotation.JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    public static TipoBolloEnum fromValue(String v) {
        for (TipoBolloEnum b : TipoBolloEnum.values()) {
            if (String.valueOf(b.value).equals(v)) {
                return b;
            }
        }
        return null;
    }
}
  
  @Schema(required = true, description = "Tipologia di Bollo digitale")
 /**
   * Tipologia di Bollo digitale  
  **/
  private TipoBolloEnum tipoBollo = null;
  
  @Schema(required = true, description = "Digest in base64 del documento informatico associato alla marca da bollo")
 /**
   * Digest in base64 del documento informatico associato alla marca da bollo  
  **/
  private String hashDocumento = null;
  
  @Schema(required = true, description = "Sigla automobilistica della provincia di residenza del soggetto pagatore")
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
  public String getCodEntrata() {
    return codEntrata;
  }

  public void setCodEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
  }

  public TipoRiferimentoVocePendenza codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }

 /**
   * Get ibanAccredito
   * @return ibanAccredito
  **/
  @JsonProperty("ibanAccredito")
  @NotNull
  public String getIbanAccredito() {
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
  public String getIbanAppoggio() {
    return ibanAppoggio;
  }

  public void setIbanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
  }

  public TipoRiferimentoVocePendenza ibanAppoggio(String ibanAppoggio) {
    this.ibanAppoggio = ibanAppoggio;
    return this;
  }

 /**
   * Get tipoContabilita
   * @return tipoContabilita
  **/
  @JsonProperty("tipoContabilita")
  @NotNull
  public TipoContabilita getTipoContabilita() {
    return tipoContabilita;
  }

  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  public TipoRiferimentoVocePendenza tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

 /**
   * Codifica del capitolo di bilancio
   * @return codiceContabilita
  **/
  @JsonProperty("codiceContabilita")
  @NotNull
  public String getCodiceContabilita() {
    return codiceContabilita;
  }

  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  public TipoRiferimentoVocePendenza codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }

 /**
   * Tipologia di Bollo digitale
   * @return tipoBollo
  **/
  @JsonProperty("tipoBollo")
  @NotNull
  public String getTipoBollo() {
    if (tipoBollo == null) {
      return null;
    }
    return tipoBollo.value();
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
  @NotNull
  public String getHashDocumento() {
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
  @NotNull
  public String getProvinciaResidenza() {
    return provinciaResidenza;
  }

  public void setProvinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
  }

  public TipoRiferimentoVocePendenza provinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoRiferimentoVocePendenza {\n");
    
    sb.append("    codEntrata: ").append(toIndentedString(codEntrata)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
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
