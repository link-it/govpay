package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idDominio",
"iuv",
"iur",
"indice",
"pendenza",
"idVocePendenza",
"rpt",
"importo",
"ibanAccredito",
"data",
"commissioni",
"allegato",
})
public class Riscossione extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("iur")
  private String iur = null;
  
  @JsonProperty("indice")
  private BigDecimal indice = null;
  
  @JsonProperty("pendenza")
  private String pendenza = null;
  
  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;
  
  @JsonProperty("rpt")
  private String rpt = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("commissioni")
  private BigDecimal commissioni = null;
  
  @JsonProperty("allegato")
  private Object allegato = null;
  
  /**
   * Identificativo ente creditore
   **/
  public Riscossione idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Identificativo univoco di versamento
   **/
  public Riscossione iuv(String iuv) {
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
   * Identificativo univoco di riscossione.
   **/
  public Riscossione iur(String iur) {
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
   * indice posizionale della voce pendenza riscossa
   **/
  public Riscossione indice(BigDecimal indice) {
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
   * Url della pendenza oggetto della riscossione
   **/
  public Riscossione pendenza(String pendenza) {
    this.pendenza = pendenza;
    return this;
  }

  @JsonProperty("pendenza")
  public String getPendenza() {
    return pendenza;
  }
  public void setPendenza(String pendenza) {
    this.pendenza = pendenza;
  }

  /**
   * Identificativo della voce di pedenza,interno alla pendenza, nel gestionale proprietario a cui si riferisce la riscossione
   **/
  public Riscossione idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return idVocePendenza;
  }
  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  /**
   * Url richiesta di pagamento che ha realizzato la riscossione. Se non valorizzato, si tratta di un pagamento senza RPT
   **/
  public Riscossione rpt(String rpt) {
    this.rpt = rpt;
    return this;
  }

  @JsonProperty("rpt")
  public String getRpt() {
    return rpt;
  }
  public void setRpt(String rpt) {
    this.rpt = rpt;
  }

  /**
   * Importo riscosso. 
   **/
  public Riscossione importo(BigDecimal importo) {
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
   * Iban di accredito della riscossione comunicato nella RPT o individuato in fase di incasso
   **/
  public Riscossione ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  /**
   * Data di esecuzione della riscossione
   **/
  public Riscossione data(Date data) {
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
   * Importo delle commissioni applicate al pagamento dal PSP
   **/
  public Riscossione commissioni(BigDecimal commissioni) {
    this.commissioni = commissioni;
    return this;
  }

  @JsonProperty("commissioni")
  public BigDecimal getCommissioni() {
    return commissioni;
  }
  public void setCommissioni(BigDecimal commissioni) {
    this.commissioni = commissioni;
  }

  /**
   **/
  public Riscossione allegato(Object allegato) {
    this.allegato = allegato;
    return this;
  }

  @JsonProperty("allegato")
  public Object getAllegato() {
    return allegato;
  }
  public void setAllegato(Object allegato) {
    this.allegato = allegato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Riscossione riscossione = (Riscossione) o;
    return Objects.equals(idDominio, riscossione.idDominio) &&
        Objects.equals(iuv, riscossione.iuv) &&
        Objects.equals(iur, riscossione.iur) &&
        Objects.equals(indice, riscossione.indice) &&
        Objects.equals(pendenza, riscossione.pendenza) &&
        Objects.equals(idVocePendenza, riscossione.idVocePendenza) &&
        Objects.equals(rpt, riscossione.rpt) &&
        Objects.equals(importo, riscossione.importo) &&
        Objects.equals(ibanAccredito, riscossione.ibanAccredito) &&
        Objects.equals(data, riscossione.data) &&
        Objects.equals(commissioni, riscossione.commissioni) &&
        Objects.equals(allegato, riscossione.allegato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, iuv, iur, indice, pendenza, idVocePendenza, rpt, importo, ibanAccredito, data, commissioni, allegato);
  }

  public static Riscossione parse(String json) {
    return (Riscossione) parse(json, Riscossione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "riscossione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Riscossione {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    rpt: ").append(toIndentedString(rpt)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    commissioni: ").append(toIndentedString(commissioni)).append("\n");
    sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
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



