package it.govpay.core.beans.ente.v1;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"iuv",
"iur",
"indice",
"pendenza",
"idVocePendenza",
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
  
  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;
  
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
   * Identificativo della voce di pedenza,interno alla pendenza, nel gestionale proprietario a cui si riferisce la riscossione
   **/
  public Riscossione idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return this.idVocePendenza;
  }
  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
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
    return Objects.equals(this.idDominio, riscossione.idDominio) &&
        Objects.equals(this.iuv, riscossione.iuv) &&
        Objects.equals(this.iur, riscossione.iur) &&
        Objects.equals(this.indice, riscossione.indice) &&
        Objects.equals(this.pendenza, riscossione.pendenza) &&
        Objects.equals(this.idVocePendenza, riscossione.idVocePendenza) &&
        Objects.equals(this.rpp, riscossione.rpp) &&
        Objects.equals(this.stato, riscossione.stato) &&
        Objects.equals(this.tipo, riscossione.tipo) &&
        Objects.equals(this.importo, riscossione.importo) &&
        Objects.equals(this.data, riscossione.data) &&
        Objects.equals(this.commissioni, riscossione.commissioni) &&
        Objects.equals(this.allegato, riscossione.allegato) &&
        Objects.equals(this.incasso, riscossione.incasso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idDominio, this.iuv, this.iur, this.indice, this.pendenza, this.idVocePendenza, this.rpp, this.stato, this.tipo, this.importo, this.data, this.commissioni, this.allegato, this.incasso);
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
    
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    iuv: ").append(this.toIndentedString(this.iuv)).append("\n");
    sb.append("    iur: ").append(this.toIndentedString(this.iur)).append("\n");
    sb.append("    indice: ").append(this.toIndentedString(this.indice)).append("\n");
    sb.append("    pendenza: ").append(this.toIndentedString(this.pendenza)).append("\n");
    sb.append("    idVocePendenza: ").append(this.toIndentedString(this.idVocePendenza)).append("\n");
    sb.append("    rpp: ").append(this.toIndentedString(this.rpp)).append("\n");
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    tipo: ").append(this.toIndentedString(this.tipo)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    data: ").append(this.toIndentedString(this.data)).append("\n");
    sb.append("    commissioni: ").append(this.toIndentedString(this.commissioni)).append("\n");
    sb.append("    allegato: ").append(this.toIndentedString(this.allegato)).append("\n");
    sb.append("    incasso: ").append(this.toIndentedString(this.incasso)).append("\n");
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



