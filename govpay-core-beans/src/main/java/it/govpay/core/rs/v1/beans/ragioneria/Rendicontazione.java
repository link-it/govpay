package it.govpay.core.rs.v1.beans.ragioneria;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"iuv",
"iur",
"indice",
"importo",
"esito",
"data",
"segnalazioni",
"riscossione",
})
public class Rendicontazione extends JSONSerializable {
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("iur")
  private String iur = null;
  
  @JsonProperty("indice")
  private BigDecimal indice = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("esito")
  private BigDecimal esito = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("segnalazioni")
  private List<Segnalazione> segnalazioni = null;
  
  @JsonProperty("riscossione")
  private RiscossioneIndex riscossione = null;
  
  /**
   * Identificativo univoco di versamento
   **/
  public Rendicontazione iuv(String iuv) {
    this.iuv = iuv;
    return this;
  }

  @JsonProperty("iuv")
  public String getIuv() {
    return iuv;
  }
  public void setIuv(String iuv) {
    this.iuv = iuv;
  }

  /**
   * Identificativo univoco di riscossione
   **/
  public Rendicontazione iur(String iur) {
    this.iur = iur;
    return this;
  }

  @JsonProperty("iur")
  public String getIur() {
    return iur;
  }
  public void setIur(String iur) {
    this.iur = iur;
  }

  /**
   * Indice dell'occorrenza del pagamento all’interno della struttura datiSingoloPagamento della Ricevuta Telematica.
   **/
  public Rendicontazione indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   * Importo rendicontato.
   **/
  public Rendicontazione importo(BigDecimal importo) {
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
   * Codice di esito dell'operazione rendicontata  * 0 = Pagamento eseguito  * 3 = Pagamento revocato  * 9 = Pagamento eseguito in assenza di RPT 
   **/
  public Rendicontazione esito(BigDecimal esito) {
    this.esito = esito;
    return this;
  }

  @JsonProperty("esito")
  public BigDecimal getEsito() {
    return esito;
  }
  public void setEsito(BigDecimal esito) {
    this.esito = esito;
  }

  /**
   * Data di esito
   **/
  public Rendicontazione data(Date data) {
    this.data = data;
    return this;
  }

  @JsonProperty("data")
  public Date getData() {
    return data;
  }
  public void setData(Date data) {
    this.data = data;
  }

  /**
   **/
  public Rendicontazione segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return segnalazioni;
  }
  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  /**
   **/
  public Rendicontazione riscossione(RiscossioneIndex riscossione) {
    this.riscossione = riscossione;
    return this;
  }

  @JsonProperty("riscossione")
  public RiscossioneIndex getRiscossione() {
    return riscossione;
  }
  public void setRiscossione(RiscossioneIndex riscossione) {
    this.riscossione = riscossione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Rendicontazione rendicontazione = (Rendicontazione) o;
    return Objects.equals(iuv, rendicontazione.iuv) &&
        Objects.equals(iur, rendicontazione.iur) &&
        Objects.equals(indice, rendicontazione.indice) &&
        Objects.equals(importo, rendicontazione.importo) &&
        Objects.equals(esito, rendicontazione.esito) &&
        Objects.equals(data, rendicontazione.data) &&
        Objects.equals(segnalazioni, rendicontazione.segnalazioni) &&
        Objects.equals(riscossione, rendicontazione.riscossione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iuv, iur, indice, importo, esito, data, segnalazioni, riscossione);
  }

  public static Rendicontazione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (Rendicontazione) parse(json, Rendicontazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "rendicontazione";
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
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
    sb.append("    riscossione: ").append(toIndentedString(riscossione)).append("\n");
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



