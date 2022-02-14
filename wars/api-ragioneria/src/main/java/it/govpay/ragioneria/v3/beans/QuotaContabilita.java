package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class QuotaContabilita   {
  
  @Schema(example = "0101", required = true, description = "Codice del capitolo")
 /**
   * Codice del capitolo  
  **/
  private String capitolo = null;
  
  @Schema(example = "2020", required = true, description = "Anno di esercizio")
 /**
   * Anno di esercizio  
  **/
  private BigDecimal annoEsercizio = null;
  
  @Schema(example = "10.01", required = true, description = "Importo della voce di contabilita'")
 /**
   * Importo della voce di contabilita'  
  **/
  private BigDecimal importo = null;
  
  @Schema(example = "22", description = "Codice dell’accertamento")
 /**
   * Codice dell’accertamento  
  **/
  private String accertamento = null;
  
  @Schema(description = "Dati specifici del gestionale di contabilità")
 /**
   * Dati specifici del gestionale di contabilità  
  **/
  private Object proprietaCustom = null;
  
  @Schema(example = "titolo", description = "Classificazione delle entrate in bilancio")
 /**
   * Classificazione delle entrate in bilancio  
  **/
  private String titolo = null;
  
  @Schema(example = "tipologia", description = "Classificazione delle entrate in bilancio")
 /**
   * Classificazione delle entrate in bilancio  
  **/
  private String tipologia = null;
  
  @Schema(example = "categoria", description = "Classificazione delle entrate in bilancio")
 /**
   * Classificazione delle entrate in bilancio  
  **/
  private String categoria = null;
  
  @Schema(example = "articolo", description = "Classificazione delle entrate in bilancio")
 /**
   * Classificazione delle entrate in bilancio  
  **/
  private String articolo = null;
 /**
   * Codice del capitolo
   * @return capitolo
  **/
  @JsonProperty("capitolo")
  @NotNull
 @Size(max=64)  public String getCapitolo() {
    return capitolo;
  }

  public void setCapitolo(String capitolo) {
    this.capitolo = capitolo;
  }

  public QuotaContabilita capitolo(String capitolo) {
    this.capitolo = capitolo;
    return this;
  }

 /**
   * Anno di esercizio
   * @return annoEsercizio
  **/
  @JsonProperty("annoEsercizio")
  @NotNull
  public BigDecimal getAnnoEsercizio() {
    return annoEsercizio;
  }

  public void setAnnoEsercizio(BigDecimal annoEsercizio) {
    this.annoEsercizio = annoEsercizio;
  }

  public QuotaContabilita annoEsercizio(BigDecimal annoEsercizio) {
    this.annoEsercizio = annoEsercizio;
    return this;
  }

 /**
   * Importo della voce di contabilita&#x27;
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public QuotaContabilita importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Codice dell’accertamento
   * @return accertamento
  **/
  @JsonProperty("accertamento")
 @Size(max=64)  public String getAccertamento() {
    return accertamento;
  }

  public void setAccertamento(String accertamento) {
    this.accertamento = accertamento;
  }

  public QuotaContabilita accertamento(String accertamento) {
    this.accertamento = accertamento;
    return this;
  }

 /**
   * Dati specifici del gestionale di contabilità
   * @return proprietaCustom
  **/
  @JsonProperty("proprietaCustom")
  public Object getProprietaCustom() {
    return proprietaCustom;
  }

  public void setProprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
  }

  public QuotaContabilita proprietaCustom(Object proprietaCustom) {
    this.proprietaCustom = proprietaCustom;
    return this;
  }

 /**
   * Classificazione delle entrate in bilancio
   * @return titolo
  **/
  @JsonProperty("titolo")
 @Size(max=64)  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public QuotaContabilita titolo(String titolo) {
    this.titolo = titolo;
    return this;
  }

 /**
   * Classificazione delle entrate in bilancio
   * @return tipologia
  **/
  @JsonProperty("tipologia")
 @Size(max=64)  public String getTipologia() {
    return tipologia;
  }

  public void setTipologia(String tipologia) {
    this.tipologia = tipologia;
  }

  public QuotaContabilita tipologia(String tipologia) {
    this.tipologia = tipologia;
    return this;
  }

 /**
   * Classificazione delle entrate in bilancio
   * @return categoria
  **/
  @JsonProperty("categoria")
 @Size(max=64)  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public QuotaContabilita categoria(String categoria) {
    this.categoria = categoria;
    return this;
  }

 /**
   * Classificazione delle entrate in bilancio
   * @return articolo
  **/
  @JsonProperty("articolo")
 @Size(max=64)  public String getArticolo() {
    return articolo;
  }

  public void setArticolo(String articolo) {
    this.articolo = articolo;
  }

  public QuotaContabilita articolo(String articolo) {
    this.articolo = articolo;
    return this;
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
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
