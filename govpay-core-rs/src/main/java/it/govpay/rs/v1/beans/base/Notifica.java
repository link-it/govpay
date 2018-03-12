package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.Riscossione;
import java.util.ArrayList;
import java.util.List;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idA2A",
"idPendenza",
"rpt",
"rt",
"riscossioni",
})
public class Notifica extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("rpt")
  private Object rpt = null;
  
  @JsonProperty("rt")
  private Object rt = null;
  
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
    return idA2A;
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
    return idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   * richiesta di pagamento telematica in json
   **/
  public Notifica rpt(Object rpt) {
    this.rpt = rpt;
    return this;
  }

  @JsonProperty("rpt")
  public Object getRpt() {
    return rpt;
  }
  public void setRpt(Object rpt) {
    this.rpt = rpt;
  }

  /**
   * ricevuta di pagamento
   **/
  public Notifica rt(Object rt) {
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

  /**
   **/
  public Notifica riscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
    return this;
  }

  @JsonProperty("riscossioni")
  public List<Riscossione> getRiscossioni() {
    return riscossioni;
  }
  public void setRiscossioni(List<Riscossione> riscossioni) {
    this.riscossioni = riscossioni;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Notifica notifica = (Notifica) o;
    return Objects.equals(idA2A, notifica.idA2A) &&
        Objects.equals(idPendenza, notifica.idPendenza) &&
        Objects.equals(rpt, notifica.rpt) &&
        Objects.equals(rt, notifica.rt) &&
        Objects.equals(riscossioni, notifica.riscossioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, idPendenza, rpt, rt, riscossioni);
  }

  public static Notifica parse(String json) {
    return (Notifica) parse(json, Notifica.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "notifica";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Notifica {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    rpt: ").append(toIndentedString(rpt)).append("\n");
    sb.append("    rt: ").append(toIndentedString(rt)).append("\n");
    sb.append("    riscossioni: ").append(toIndentedString(riscossioni)).append("\n");
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



