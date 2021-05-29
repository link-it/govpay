package it.govpay.ec.v1.beans;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

//import io.swagger.v3.oas.annotations.media.Schema;

public class VocePendenza extends DatiEntrata {
  
  // @Schema(example = "1", description = "indice di voce all'interno della pendenza")
 /**
   * indice di voce all'interno della pendenza  
  **/
  private BigDecimal indice = null;
  
  // @Schema(example = "abcdef12345_1", required = true, description = "Identificativo della voce di pedenza nel gestionale proprietario")
 /**
   * Identificativo della voce di pedenza nel gestionale proprietario  
  **/
  private String idVocePendenza = null;
  
  // @Schema(example = "10.01", required = true, description = "Importo della voce")
 /**
   * Importo della voce  
  **/
  private BigDecimal importo = null;
  
  // @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "descrizione della voce di pagamento")
 /**
   * descrizione della voce di pagamento  
  **/
  private String descrizione = null;
  
  // @Schema(description = "Dati applicativi allegati dal gestionale secondo un formato proprietario.")
 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.  
  **/
  private String datiAllegati = null;
  
  // @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "Testo libero per la causale versamento")
 /**
   * Testo libero per la causale versamento  
  **/
  private String descrizioneCausaleRPT = null;
  
  private Contabilita contabilita = null;
  
 /**
   * indice di voce all&#x27;interno della pendenza
   * @return indice
  **/
  @JsonProperty("indice")
  @Valid
  public BigDecimal getIndice() {
    return indice;
  }

  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  public VocePendenza indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

 /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   * @return idVocePendenza
  **/
  @JsonProperty("idVocePendenza")
  @NotNull
  @Valid
  public String getIdVocePendenza() {
    return idVocePendenza;
  }

  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  public VocePendenza idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

 /**
   * Importo della voce
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  @Valid
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public VocePendenza importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * descrizione della voce di pagamento
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  @NotNull
  @Valid
  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public VocePendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   * @return datiAllegati
  **/
  @JsonProperty("datiAllegati")
  @Valid
  public String getDatiAllegati() {
    return datiAllegati;
  }

  public void setDatiAllegati(String datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  public VocePendenza datiAllegati(String datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }
  
  /**
   * Testo libero per la causale versamento
   * @return descrizione
  **/
  @JsonProperty("descrizioneCausaleRPT")
  @NotNull
  @Valid
  public String getDescrizioneCausaleRPT() {
    return descrizioneCausaleRPT;
  }

  public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
  }

  public VocePendenza descrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
    return this;
  }
  
  /**
   **/
  public VocePendenza contabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
    return this;
  }

  @JsonProperty("contabilita")
  public Contabilita getContabilita() {
    return contabilita;
  }
  public void setContabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenza {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    contabilita: ").append(toIndentedString(contabilita)).append("\n");
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
