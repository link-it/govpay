package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"capitolo",
"annoEsercizio",
"importo",
"accertamento",
"proprietaCustom",
"titolo",
"tipologia",
"categoria",
"articolo",
})
public class QuotaContabilita extends JSONSerializable {
  
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
  
  @JsonProperty("titolo")
  private String titolo = null;
  
  @JsonProperty("tipologia")
  private String tipologia = null;
  
  @JsonProperty("categoria")
  private String categoria = null;
  
  @JsonProperty("articolo")
  private String articolo = null;
  
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

  /**
   * Classificazione delle entrate in bilancio
   **/
  public QuotaContabilita titolo(String titolo) {
    this.titolo = titolo;
    return this;
  }

  @JsonProperty("titolo")
  public String getTitolo() {
    return titolo;
  }
  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  /**
   * Classificazione delle entrate in bilancio
   **/
  public QuotaContabilita tipologia(String tipologia) {
    this.tipologia = tipologia;
    return this;
  }

  @JsonProperty("tipologia")
  public String getTipologia() {
    return tipologia;
  }
  public void setTipologia(String tipologia) {
    this.tipologia = tipologia;
  }

  /**
   * Classificazione delle entrate in bilancio
   **/
  public QuotaContabilita categoria(String categoria) {
    this.categoria = categoria;
    return this;
  }

  @JsonProperty("categoria")
  public String getCategoria() {
    return categoria;
  }
  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  /**
   * Classificazione delle entrate in bilancio
   **/
  public QuotaContabilita articolo(String articolo) {
    this.articolo = articolo;
    return this;
  }

  @JsonProperty("articolo")
  public String getArticolo() {
    return articolo;
  }
  public void setArticolo(String articolo) {
    this.articolo = articolo;
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
        Objects.equals(proprietaCustom, quotaContabilita.proprietaCustom) &&
        Objects.equals(titolo, quotaContabilita.titolo) &&
        Objects.equals(tipologia, quotaContabilita.tipologia) &&
        Objects.equals(categoria, quotaContabilita.categoria) &&
        Objects.equals(articolo, quotaContabilita.articolo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capitolo, annoEsercizio, importo, accertamento, proprietaCustom, titolo, tipologia, categoria, articolo);
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
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    tipologia: ").append(toIndentedString(tipologia)).append("\n");
    sb.append("    categoria: ").append(toIndentedString(categoria)).append("\n");
    sb.append("    articolo: ").append(toIndentedString(articolo)).append("\n");
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
}



