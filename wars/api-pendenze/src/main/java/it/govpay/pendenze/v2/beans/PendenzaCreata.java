package it.govpay.pendenze.v2.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"numeroAvviso",
"UUID",
"pdf",
})
public class PendenzaCreata extends JSONSerializable {
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
  @JsonProperty("UUID")
  private String UUID = null;
  
  @JsonProperty("pdf")
  private String pdf = null;
  
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
   * Parametro di randomizzazione delle URL di pagamento statiche
   **/
  public PendenzaCreata UUID(String UUID) {
    this.UUID = UUID;
    return this;
  }

  @JsonProperty("UUID")
  public String getUUID() {
    return UUID;
  }
  public void setUUID(String UUID) {
    this.UUID = UUID;
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
    return Objects.equals(idDominio, pendenzaCreata.idDominio) &&
        Objects.equals(numeroAvviso, pendenzaCreata.numeroAvviso) &&
        Objects.equals(UUID, pendenzaCreata.UUID) &&
        Objects.equals(pdf, pendenzaCreata.pdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDominio, numeroAvviso, UUID, pdf);
  }

  public static PendenzaCreata parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, PendenzaCreata.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "pendenzaCreata";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaCreata {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    UUID: ").append(toIndentedString(UUID)).append("\n");
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



