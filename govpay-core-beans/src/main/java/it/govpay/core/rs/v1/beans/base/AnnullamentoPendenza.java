package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"idPendenza",
"motivoAnnullamento",
})
public class AnnullamentoPendenza extends JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("motivoAnnullamento")
  private String motivoAnnullamento = null;
  
  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public AnnullamentoPendenza idA2A(String idA2A) {
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
   * Identificativo della pendenza nel gestionale responsabile
   **/
  public AnnullamentoPendenza idPendenza(String idPendenza) {
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
   * Descrizione dell'operazione
   **/
  public AnnullamentoPendenza motivoAnnullamento(String motivoAnnullamento) {
    this.motivoAnnullamento = motivoAnnullamento;
    return this;
  }

  @JsonProperty("motivoAnnullamento")
  public String getMotivoAnnullamento() {
    return motivoAnnullamento;
  }
  public void setMotivoAnnullamento(String motivoAnnullamento) {
    this.motivoAnnullamento = motivoAnnullamento;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnnullamentoPendenza annullamentoPendenza = (AnnullamentoPendenza) o;
    return Objects.equals(idA2A, annullamentoPendenza.idA2A) &&
        Objects.equals(idPendenza, annullamentoPendenza.idPendenza) &&
        Objects.equals(motivoAnnullamento, annullamentoPendenza.motivoAnnullamento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, idPendenza, motivoAnnullamento);
  }

  public static AnnullamentoPendenza parse(String json) throws ServiceException, ValidationException {
    return (AnnullamentoPendenza) parse(json, AnnullamentoPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "annullamentoPendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnnullamentoPendenza {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    motivoAnnullamento: ").append(toIndentedString(motivoAnnullamento)).append("\n");
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



