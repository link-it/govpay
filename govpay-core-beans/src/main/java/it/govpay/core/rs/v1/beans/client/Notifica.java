package it.govpay.core.rs.v1.beans.client;

import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.rs.v1.beans.base.Riscossione;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"idPendenza",
"rpt",
"rt",
"riscossioni",
})
public class Notifica extends JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("rpt")
  private CtRichiestaPagamentoTelematico rpt = null;
  
  @JsonProperty("rt")
  private CtRicevutaTelematica rt = null;
  
  @JsonProperty("riscossioni")
  private List<Riscossione> riscossioni = null;
  
  /**
   * codice identificativo del gestionale titolare della pendenza
   **/
  public Notifica idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

  @JsonProperty("idA2A")
  public String getIdA2A() {
    return this.idA2A;
  }
  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  /**
   * codice identificativo della pendenza oggetto del tentativo di pagamento
   **/
  public Notifica idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return this.idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   * richiesta di pagamento telematica in json
   **/
  public Notifica rpt(CtRichiestaPagamentoTelematico rpt) {
    this.rpt = rpt;
    return this;
  }

  @JsonProperty("rpt")
  public CtRichiestaPagamentoTelematico getRpt() {
    return this.rpt;
  }
  public void setRpt(CtRichiestaPagamentoTelematico rpt) {
    this.rpt = rpt;
  }

  /**
   * ricevuta di pagamento
   **/
  public Notifica rt(CtRicevutaTelematica rt) {
    this.rt = rt;
    return this;
  }

  @JsonProperty("rt")
  public CtRicevutaTelematica getRt() {
    return this.rt;
  }
  public void setRt(CtRicevutaTelematica rt) {
    this.rt = rt;
  }

  /**
   **/
  public Notifica riscossioni(List<Riscossione> riscossioni) {
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
    Notifica notifica = (Notifica) o;
    return Objects.equals(this.idA2A, notifica.idA2A) &&
        Objects.equals(this.idPendenza, notifica.idPendenza) &&
        Objects.equals(this.rpt, notifica.rpt) &&
        Objects.equals(this.rt, notifica.rt) &&
        Objects.equals(this.riscossioni, notifica.riscossioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idA2A, this.idPendenza, this.rpt, this.rt, this.riscossioni);
  }

  public static Notifica parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Notifica.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "notifica";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Notifica {\n");
    
    sb.append("    idA2A: ").append(this.toIndentedString(this.idA2A)).append("\n");
    sb.append("    idPendenza: ").append(this.toIndentedString(this.idPendenza)).append("\n");
    sb.append("    rpt: ").append(this.toIndentedString(this.rpt)).append("\n");
    sb.append("    rt: ").append(this.toIndentedString(this.rt)).append("\n");
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



