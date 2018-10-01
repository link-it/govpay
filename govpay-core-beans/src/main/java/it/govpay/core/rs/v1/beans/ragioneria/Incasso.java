package it.govpay.core.rs.v1.beans.ragioneria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"idIncasso",
"causale",
"importo",
"dataValuta",
"dataContabile",
"ibanAccredito",
"riscossioni",
})
public class Incasso extends JSONSerializable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idIncasso")
  private String idIncasso = null;
  
  @JsonProperty("causale")
  private String causale = null;
  
  @JsonProperty("importo")
  private Double importo = null;
  
  @JsonProperty("dataValuta")
  private Date dataValuta = null;
  
  @JsonProperty("dataContabile")
  private Date dataContabile = null;
  
  @JsonProperty("ibanAccredito")
  private String ibanAccredito = null;
  
  @JsonProperty("riscossioni")
  private List<Riscossione> riscossioni = new ArrayList<>();
  
  /**
   * Identificativo ente creditore
   **/
  public Incasso idDominio(String idDominio) {
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
   * Identificativo dell'incasso
   **/
  public Incasso idIncasso(String idIncasso) {
    this.idIncasso = idIncasso;
    return this;
  }

  @JsonProperty("idIncasso")
  public String getIdIncasso() {
    return this.idIncasso;
  }
  public void setIdIncasso(String idIncasso) {
    this.idIncasso = idIncasso;
  }

  /**
   * Causale dell'operazione di riversamento dal PSP alla Banca Tesoriera
   **/
  public Incasso causale(String causale) {
    this.causale = causale;
    return this;
  }

  @JsonProperty("causale")
  public String getCausale() {
    return this.causale;
  }
  public void setCausale(String causale) {
    this.causale = causale;
  }

  /**
   **/
  public Incasso importo(Double importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public Double getImporto() {
    return this.importo;
  }
  public void setImporto(Double importo) {
    this.importo = importo;
  }

  /**
   * Data di valuta dell'incasso
   **/
  public Incasso dataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
    return this;
  }

  @JsonProperty("dataValuta")
  public Date getDataValuta() {
    return this.dataValuta;
  }
  public void setDataValuta(Date dataValuta) {
    this.dataValuta = dataValuta;
  }

  /**
   * Data di contabile dell'incasso
   **/
  public Incasso dataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
    return this;
  }

  @JsonProperty("dataContabile")
  public Date getDataContabile() {
    return this.dataContabile;
  }
  public void setDataContabile(Date dataContabile) {
    this.dataContabile = dataContabile;
  }

  /**
   * Identificativo del conto di tesoreria su cui sono stati incassati i fondi
   **/
  public Incasso ibanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return this.ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  /**
   **/
  public Incasso riscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  @JsonProperty("riscossioni")
  public List<Riscossione> getRiscossioni() {
    return this.riscossioni;
  }
  public void setRiscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Incasso incasso = (Incasso) o;
    return Objects.equals(this.idDominio, incasso.idDominio) &&
        Objects.equals(this.idIncasso, incasso.idIncasso) &&
        Objects.equals(this.causale, incasso.causale) &&
        Objects.equals(this.importo, incasso.importo) &&
        Objects.equals(this.dataValuta, incasso.dataValuta) &&
        Objects.equals(this.dataContabile, incasso.dataContabile) &&
        Objects.equals(this.ibanAccredito, incasso.ibanAccredito) &&
        Objects.equals(this.riscossioni, incasso.riscossioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idDominio, this.idIncasso, this.causale, this.importo, this.dataValuta, this.dataContabile, this.ibanAccredito, this.riscossioni);
  }

  public static Incasso parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Incasso.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "incasso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Incasso {\n");
    sb.append("    ").append(this.toIndentedString(super.toString())).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    idIncasso: ").append(this.toIndentedString(this.idIncasso)).append("\n");
    sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
    sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
    sb.append("    dataValuta: ").append(this.toIndentedString(this.dataValuta)).append("\n");
    sb.append("    dataContabile: ").append(this.toIndentedString(this.dataContabile)).append("\n");
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
    sb.append("    riscossioni: ").append(this.toIndentedString(this.riscossioni)).append("\n");
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



