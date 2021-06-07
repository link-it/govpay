package it.govpay.pendenze.v2.beans;


import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"capitolo",
"annoEsercizio",
"importo",
"accertamento",
"proprietaCustom",
})
public class QuotaContabilita extends JSONSerializable implements IValidable{
  
  @JsonProperty("capitolo")
  private String capitolo = null;
  
  @JsonProperty("annoEsercizio")
  private BigDecimal annoEsercizio = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("accertamento")
  private String accertamento = null;
  
  @JsonProperty("proprietaCustom")
  private Object proprietaCustom = null;
  
  /**
   * Codice del capitolo
   **/
  public QuotaContabilita capitolo(String capitolo) {
    this.capitolo = capitolo;
    return this;
  }

  @JsonProperty("capitolo")
  public String getCapitolo() {
    return capitolo;
  }
  public void setCapitolo(String capitolo) {
    this.capitolo = capitolo;
  }

  /**
   * Anno di esercizio
   **/
  public QuotaContabilita annoEsercizio(BigDecimal annoEsercizio) {
    this.annoEsercizio = annoEsercizio;
    return this;
  }

  @JsonProperty("annoEsercizio")
  public BigDecimal getAnnoEsercizio() {
    return annoEsercizio;
  }
  public void setAnnoEsercizio(BigDecimal annoEsercizio) {
    this.annoEsercizio = annoEsercizio;
  }

  /**
   * Importo della voce di contabilita'
   **/
  public QuotaContabilita importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Codice dell’accertamento
   **/
  public QuotaContabilita accertamento(String accertamento) {
    this.accertamento = accertamento;
    return this;
  }

  @JsonProperty("accertamento")
  public String getAccertamento() {
    return accertamento;
  }
  public void setAccertamento(String accertamento) {
    this.accertamento = accertamento;
  }

  /**
   * Dati specifici del gestionale di contabilità
   **/
  public QuotaContabilita proprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
    return this;
  }

  @JsonProperty("proprietaCustom")
  public Object getProprietaCustom() {
    return proprietaCustom;
  }
  public void setProprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuotaContabilita quotaContabilita = (QuotaContabilita) o;
    return Objects.equals(capitolo, quotaContabilita.capitolo) &&
        Objects.equals(annoEsercizio, quotaContabilita.annoEsercizio) &&
        Objects.equals(importo, quotaContabilita.importo) &&
        Objects.equals(accertamento, quotaContabilita.accertamento) &&
        Objects.equals(proprietaCustom, quotaContabilita.proprietaCustom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capitolo, annoEsercizio, importo, accertamento, proprietaCustom);
  }

  public static QuotaContabilita parse(String json) throws ServiceException, ValidationException {
    return (QuotaContabilita) parse(json, QuotaContabilita.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "quotaContabilita";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuotaContabilita {\n");
    
    sb.append("    capitolo: ").append(toIndentedString(capitolo)).append("\n");
    sb.append("    annoEsercizio: ").append(toIndentedString(annoEsercizio)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    accertamento: ").append(toIndentedString(accertamento)).append("\n");
    sb.append("    proprietaCustom: ").append(toIndentedString(proprietaCustom)).append("\n");
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
	
	vf.getValidator("capitolo", capitolo).notNull().minLength(1).maxLength(64);
	
	vf.getValidator("annoEsercizio", annoEsercizio).notNull();
	ValidatoreUtils.validaAnnoRiferimento(vf, "annoEsercizio", annoEsercizio);

	vf.getValidator("accertamento", accertamento).minLength(1).maxLength(64);
	ValidatoreUtils.validaImporto(vf, "importo", importo);
	
  }
}



