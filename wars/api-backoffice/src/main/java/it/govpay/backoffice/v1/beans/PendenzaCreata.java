package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idA2A",
"idPendenza",
"idDominio",
"idUnitaOperativa",
"numeroAvviso",
"pdf",
})
public class PendenzaCreata extends JSONSerializable {
  
  @JsonProperty("idA2A")
  private String idA2A = null;
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idUnitaOperativa")
  private String idUnitaOperativa = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  @JsonProperty("pdf")
  private String pdf = null;
  
  /**
   * Identificativo del gestionale responsabile della pendenza
   **/
  public PendenzaCreata idA2A(String idA2A) {
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
  public PendenzaCreata idPendenza(String idPendenza) {
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
   * Identificativo del creditore dell'avviso
   **/
  public PendenzaCreata idDominio(String idDominio) {
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
   * Identificativo dell'unita' operativa dell'avviso
   **/
  public PendenzaCreata idUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
    return this;
  }

  @JsonProperty("idUnitaOperativa")
  public String getIdUnitaOperativa() {
    return idUnitaOperativa;
  }
  public void setIdUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
  }

  /**
   * Numero identificativo dell'avviso di pagamento
   **/
  public PendenzaCreata numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

  @JsonProperty("numeroAvviso")
  public String getNumeroAvviso() {
    return numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Stampa pdf dell'avviso
   **/
  public PendenzaCreata pdf(String pdf) {
    this.pdf = pdf;
    return this;
  }

  @JsonProperty("pdf")
  public String getPdf() {
    return pdf;
  }
  public void setPdf(String pdf) {
    this.pdf = pdf;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendenzaCreata pendenzaCreata = (PendenzaCreata) o;
    return Objects.equals(idA2A, pendenzaCreata.idA2A) &&
        Objects.equals(idPendenza, pendenzaCreata.idPendenza) &&
        Objects.equals(idDominio, pendenzaCreata.idDominio) &&
        Objects.equals(idUnitaOperativa, pendenzaCreata.idUnitaOperativa) &&
        Objects.equals(numeroAvviso, pendenzaCreata.numeroAvviso) &&
        Objects.equals(pdf, pendenzaCreata.pdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idA2A, idPendenza, idDominio, idUnitaOperativa, numeroAvviso, pdf);
  }

  public static PendenzaCreata parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PendenzaCreata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "avviso";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaCreata {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    pdf: ").append(toIndentedString(pdf)).append("\n");
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



