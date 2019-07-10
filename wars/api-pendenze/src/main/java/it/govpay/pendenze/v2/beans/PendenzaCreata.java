package it.govpay.pendenze.v2.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idDominio",
"numeroAvviso",
"pdf"
})
public class PendenzaCreata extends JSONSerializable {
  
    
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("numeroAvviso")
  private String numeroAvviso = null;
  
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
    return this.idDominio;
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
    return this.numeroAvviso;
  }
  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  /**
   * Base64 del pdf dell'avviso
   **/
  public PendenzaCreata pdf(String pdf) {
    this.pdf = pdf;
    return this;
  }

  @JsonProperty("pdf")
  public String getPdf() {
    return this.pdf;
  }
  public void setPdf(String pdf) {
    this.pdf = pdf;
  }
  
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PendenzaCreata avviso = (PendenzaCreata) o;
    return 
        Objects.equals(this.idDominio, avviso.idDominio) &&
        Objects.equals(this.numeroAvviso, avviso.numeroAvviso) &&
        Objects.equals(this.pdf, avviso.pdf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idDominio, this.numeroAvviso, this.pdf);  }

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
    sb.append("class Avviso {\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    numeroAvviso: ").append(this.toIndentedString(this.numeroAvviso)).append("\n");
    sb.append("    pdf: ").append(this.toIndentedString(this.pdf)).append("\n");
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



