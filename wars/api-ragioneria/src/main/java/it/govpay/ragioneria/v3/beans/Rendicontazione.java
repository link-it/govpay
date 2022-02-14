package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import it.govpay.ragioneria.v3.beans.Riscossione;
import it.govpay.ragioneria.v3.beans.Segnalazione;
import it.govpay.ragioneria.v3.beans.StatoRendicontazione;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Rendicontazione   {
  
  @Schema(example = "RF23567483937849450550875", required = true, description = "Identificativo univoco di versamento")
 /**
   * Identificativo univoco di versamento  
  **/
  private String iuv = null;
  
  @Schema(example = "1234acdc", required = true, description = "Identificativo univoco di riscossione")
 /**
   * Identificativo univoco di riscossione  
  **/
  private String iur = null;
  
  @Schema(example = "1", required = true, description = "Indice dell'occorrenza del pagamento all’interno della struttura datiSingoloPagamento della Ricevuta Telematica.")
 /**
   * Indice dell'occorrenza del pagamento all’interno della struttura datiSingoloPagamento della Ricevuta Telematica.  
  **/
  private BigDecimal indice = null;
  
  @Schema(example = "10.01", required = true, description = "Importo rendicontato.")
 /**
   * Importo rendicontato.  
  **/
  private BigDecimal importo = null;
  
  @Schema(example = "0", required = true, description = "Codice di esito dell'operazione rendicontata  * 0 = Pagamento eseguito  * 3 = Pagamento revocato  * 9 = Pagamento eseguito in assenza di RPT ")
 /**
   * Codice di esito dell'operazione rendicontata  * 0 = Pagamento eseguito  * 3 = Pagamento revocato  * 9 = Pagamento eseguito in assenza di RPT   
  **/
  private BigDecimal esito = null;
  
  @Schema(example = "Mon Dec 31 01:00:00 CET 2018", required = true, description = "Data di esito")
 /**
   * Data di esito  
  **/
  private Date data = null;
  
  @Schema(required = true, description = "")
  private StatoRendicontazione stato = null;
  
  @Schema(description = "")
  private List<Segnalazione> segnalazioni = null;
  
  @Schema(description = "")
  private Riscossione riscossione = null;
 /**
   * Identificativo univoco di versamento
   * @return iuv
  **/
  @JsonProperty("iuv")
  @NotNull
  public String getIuv() {
    return iuv;
  }

  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  public Rendicontazione iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

 /**
   * Identificativo univoco di riscossione
   * @return iur
  **/
  @JsonProperty("iur")
  @NotNull
  public String getIur() {
    return iur;
  }

  public void setIur(String iur) {
    this.iur = iur;
  }

  public Rendicontazione iur(String iur) {
    this.iur = iur;
    return this;
  }

 /**
   * Indice dell&#x27;occorrenza del pagamento all’interno della struttura datiSingoloPagamento della Ricevuta Telematica.
   * @return indice
  **/
  @JsonProperty("indice")
  @NotNull
  public BigDecimal getIndice() {
    return indice;
  }

  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  public Rendicontazione indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

 /**
   * Importo rendicontato.
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

  public Rendicontazione importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Codice di esito dell&#x27;operazione rendicontata  * 0 &#x3D; Pagamento eseguito  * 3 &#x3D; Pagamento revocato  * 9 &#x3D; Pagamento eseguito in assenza di RPT 
   * @return esito
  **/
  @JsonProperty("esito")
  @NotNull
  public BigDecimal getEsito() {
    return esito;
  }

  public void setEsito(BigDecimal esito) {
    this.esito = esito;
  }

  public Rendicontazione esito(BigDecimal esito) {
    this.esito = esito;
    return this;
  }

 /**
   * Data di esito
   * @return data
  **/
  @JsonProperty("data")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  @NotNull
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public Rendicontazione data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoRendicontazione getStato() {
    return stato;
  }

  public void setStato(StatoRendicontazione stato) {
    this.stato = stato;
  }

  public Rendicontazione stato(StatoRendicontazione stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Get segnalazioni
   * @return segnalazioni
  **/
  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return segnalazioni;
  }

  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  public Rendicontazione segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  public Rendicontazione addSegnalazioniItem(Segnalazione segnalazioniItem) {
    this.segnalazioni.add(segnalazioniItem);
    return this;
  }

 /**
   * Get riscossione
   * @return riscossione
  **/
  @JsonProperty("riscossione")
  public Riscossione getRiscossione() {
    return riscossione;
  }

  public void setRiscossione(Riscossione riscossione) {
    this.riscossione = riscossione;
  }

  public Rendicontazione riscossione(Riscossione riscossione) {
    this.riscossione = riscossione;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Rendicontazione {\n");
    
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
    sb.append("    riscossione: ").append(toIndentedString(riscossione)).append("\n");
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
