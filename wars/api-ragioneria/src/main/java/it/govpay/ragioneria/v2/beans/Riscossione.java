package it.govpay.ragioneria.v2.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"dominio",
"iuv",
"iur",
"indice",
"stato",
"tipo",
"importo",
"data",
"vocePendenza",
"riconciliazione",
"rt",
})
public class Riscossione extends JSONSerializable {
  
  @JsonProperty("dominio")
  private Dominio dominio = null;
  
  @JsonProperty("iuv")
  private String iuv = null;
  
  @JsonProperty("iur")
  private String iur = null;
  
  @JsonProperty("indice")
  private BigDecimal indice = null;
  
  @JsonProperty("stato")
  private StatoRiscossione stato = null;
  
  @JsonProperty("tipo")
  private TipoRiscossione tipo = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("data")
  private Date data = null;
  
  @JsonProperty("vocePendenza")
  private VocePendenza vocePendenza = null;
  
  @JsonProperty("riconciliazione")
  private String riconciliazione = null;
  
  @JsonProperty("rt")
  private Object rt = null;
  
  /**
   **/
  public Riscossione dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public Dominio getDominio() {
    return dominio;
  }
  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
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
   **/
  public Riscossione stato(StatoRiscossione stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoRiscossione getStato() {
    return stato;
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
    return tipo;
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
    return importo;
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
    return data;
  }
  public void setData(Date data) {
    this.data = data;
  }

  /**
   **/
  public Riscossione vocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
    return this;
  }

  @JsonProperty("vocePendenza")
  public VocePendenza getVocePendenza() {
    return vocePendenza;
  }
  public void setVocePendenza(VocePendenza vocePendenza) {
    this.vocePendenza = vocePendenza;
  }

  /**
   * Riferimento all'operazione di riconciliazione, se avvenuta
   **/
  public Riscossione riconciliazione(String riconciliazione) {
    this.riconciliazione = riconciliazione;
    return this;
  }

  @JsonProperty("riconciliazione")
  public String getRiconciliazione() {
    return riconciliazione;
  }
  public void setRiconciliazione(String riconciliazione) {
    this.riconciliazione = riconciliazione;
  }

  /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica
   **/
  public Riscossione rt(Object rt) {
    this.rt = rt;
    return this;
  }

  @JsonProperty("rt")
  public Object getRt() {
    return rt;
  }
  public void setRt(Object rt) {
    this.rt = rt;
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
    return Objects.equals(dominio, riscossione.dominio) &&
        Objects.equals(iuv, riscossione.iuv) &&
        Objects.equals(iur, riscossione.iur) &&
        Objects.equals(indice, riscossione.indice) &&
        Objects.equals(stato, riscossione.stato) &&
        Objects.equals(tipo, riscossione.tipo) &&
        Objects.equals(importo, riscossione.importo) &&
        Objects.equals(data, riscossione.data) &&
        Objects.equals(vocePendenza, riscossione.vocePendenza) &&
        Objects.equals(riconciliazione, riscossione.riconciliazione) &&
        Objects.equals(rt, riscossione.rt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dominio, iuv, iur, indice, stato, tipo, importo, data, vocePendenza, riconciliazione, rt);
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
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    iuv: ").append(toIndentedString(iuv)).append("\n");
    sb.append("    iur: ").append(toIndentedString(iur)).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    vocePendenza: ").append(toIndentedString(vocePendenza)).append("\n");
    sb.append("    riconciliazione: ").append(toIndentedString(riconciliazione)).append("\n");
    sb.append("    rt: ").append(toIndentedString(rt)).append("\n");
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



