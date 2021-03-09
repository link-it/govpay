package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.govpay.backoffice.v1.beans.Allegato;
import it.govpay.backoffice.v1.beans.StatoRiscossione;
import it.govpay.backoffice.v1.beans.TipoRiscossione;
import java.math.BigDecimal;
import java.util.Date;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"iuv",
"iur",
"indice",
"pendenza",
"vocePendenza",
"rpp",
"stato",
"tipo",
"importo",
"data",
"commissioni",
"allegato",
"incasso",
})
public class Riscossione extends it.govpay.core.beans.JSONSerializable {
  
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
  
  @JsonProperty("vocePendenza")
  private VocePendenzaRiscossione vocePendenza = null;
  
  @JsonProperty("rpp")
  private String rpp = null;
  
  @JsonProperty("stato")
  private StatoRiscossione stato = null;
  
  @JsonProperty("tipo")
  private TipoRiscossione tipo = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("commissioni")
  private BigDecimal commissioni = null;
  
  @JsonProperty("allegato")
  private Allegato allegato = null;
  
  @JsonProperty("incasso")
  private String incasso = null;
  
  /**
   * Identificativo ente creditore
   **/
  public Riscossione idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return this.idDominio;
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
    return this.iuv;
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
    return this.iur;
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
    return this.indice;
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
    return this.pendenza;
  }
  public void setPendenza(String pendenza) {
    this.pendenza = pendenza;
  }

  /**
   **/
  public Riscossione vocePendenza(VocePendenzaRiscossione vocePendenza) {
    this.vocePendenza = vocePendenza;
    return this;
  }

  @JsonProperty("vocePendenza")
  public VocePendenzaRiscossione getVocePendenza() {
    return vocePendenza;
  }
  public void setVocePendenza(VocePendenzaRiscossione vocePendenza) {
    this.vocePendenza = vocePendenza;
  }

  /**
   * Url richiesta di pagamento che ha realizzato la riscossione. Se non valorizzato, si tratta di un pagamento senza RPT
   **/
  public Riscossione rpp(String rpp) {
    this.rpp = rpp;
    return this;
  }

  @JsonProperty("rpp")
  public String getRpp() {
    return this.rpp;
  }
  public void setRpp(String rpp) {
    this.rpp = rpp;
  }

  /**
   **/
  public Riscossione stato(StatoRiscossione stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoRiscossione getStato() {
    return this.stato;
  }
  public void setStato(StatoRiscossione stato) {
    this.stato = stato;
  }

  /**
   **/
  public Riscossione tipo(TipoRiscossione tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoRiscossione getTipo() {
    return this.tipo;
  }
  public void setTipo(TipoRiscossione tipo) {
    this.tipo = tipo;
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
    return this.importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
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
    return this.data;
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
    return this.commissioni;
  }
  public void setCommissioni(BigDecimal commissioni) {
    this.commissioni = commissioni;
  }

  /**
   **/
  public Riscossione allegato(Allegato allegato) {
    this.allegato = allegato;
    return this;
  }

  @JsonProperty("allegato")
  public Allegato getAllegato() {
    return this.allegato;
  }
  public void setAllegato(Allegato allegato) {
    this.allegato = allegato;
  }

  /**
   * Riferimento all'operazione di incasso
   **/
  public Riscossione incasso(String incasso) {
    this.incasso = incasso;
    return this;
  }

  @JsonProperty("incasso")
  public String getIncasso() {
    return this.incasso;
  }
  public void setIncasso(String incasso) {
    this.incasso = incasso;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Riscossione riscossione = (Riscossione) o;
    return Objects.equals(idDominio, riscossione.idDominio) &&
        Objects.equals(iuv, riscossione.iuv) &&
        Objects.equals(iur, riscossione.iur) &&
        Objects.equals(indice, riscossione.indice) &&
        Objects.equals(pendenza, riscossione.pendenza) &&
        Objects.equals(vocePendenza, riscossione.vocePendenza) &&
        Objects.equals(rpp, riscossione.rpp) &&
        Objects.equals(stato, riscossione.stato) &&
        Objects.equals(tipo, riscossione.tipo) &&
        Objects.equals(importo, riscossione.importo) &&
        Objects.equals(data, riscossione.data) &&
        Objects.equals(commissioni, riscossione.commissioni) &&
        Objects.equals(allegato, riscossione.allegato) &&
        Objects.equals(incasso, riscossione.incasso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, iuv, iur, indice, pendenza, vocePendenza, rpp, stato, tipo, importo, data, commissioni, allegato, incasso);
  }

  public static Riscossione parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Riscossione.class);
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
    sb.append("    vocePendenza: ").append(toIndentedString(vocePendenza)).append("\n");
    sb.append("    rpp: ").append(toIndentedString(rpp)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    commissioni: ").append(toIndentedString(commissioni)).append("\n");
    sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
    sb.append("    incasso: ").append(toIndentedString(incasso)).append("\n");
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



