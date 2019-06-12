package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idPsp",
"idCanale",
"idIntermediarioPsp",
"tipoVersamento",
"modelloPagamento",
"idDominio",
"idIntermediario",
"idStazione",
})
public class DatiPagoPA extends JSONSerializable {
  
  @JsonProperty("idPsp")
  private String idPsp = null;
  
  @JsonProperty("idCanale")
  private String idCanale = null;
  
  @JsonProperty("idIntermediarioPsp")
  private String idIntermediarioPsp = null;
  
  @JsonProperty("tipoVersamento")
  private String tipoVersamento = null;
  
  @JsonProperty("modelloPagamento")
  private String modelloPagamento = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("idIntermediario")
  private String idIntermediario = null;
  
  @JsonProperty("idStazione")
  private String idStazione = null;
  
  /**
   * Identificativo del PSP
   **/
  public DatiPagoPA idPsp(String idPsp) {
    this.idPsp = idPsp;
    return this;
  }

  @JsonProperty("idPsp")
  public String getIdPsp() {
    return idPsp;
  }
  public void setIdPsp(String idPsp) {
    this.idPsp = idPsp;
  }

  /**
   **/
  public DatiPagoPA idCanale(String idCanale) {
    this.idCanale = idCanale;
    return this;
  }

  @JsonProperty("idCanale")
  public String getIdCanale() {
    return idCanale;
  }
  public void setIdCanale(String idCanale) {
    this.idCanale = idCanale;
  }

  /**
   **/
  public DatiPagoPA idIntermediarioPsp(String idIntermediarioPsp) {
    this.idIntermediarioPsp = idIntermediarioPsp;
    return this;
  }

  @JsonProperty("idIntermediarioPsp")
  public String getIdIntermediarioPsp() {
    return idIntermediarioPsp;
  }
  public void setIdIntermediarioPsp(String idIntermediarioPsp) {
    this.idIntermediarioPsp = idIntermediarioPsp;
  }

  /**
   * Tipologia di versamento realizzato
   **/
  public DatiPagoPA tipoVersamento(String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
    return this;
  }

  @JsonProperty("tipoVersamento")
  public String getTipoVersamento() {
    return tipoVersamento;
  }
  public void setTipoVersamento(String tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  /**
   * Modello di pagamento utilizzato
   **/
  public DatiPagoPA modelloPagamento(String modelloPagamento) {
    this.modelloPagamento = modelloPagamento;
    return this;
  }

  @JsonProperty("modelloPagamento")
  public String getModelloPagamento() {
    return modelloPagamento;
  }
  public void setModelloPagamento(String modelloPagamento) {
    this.modelloPagamento = modelloPagamento;
  }

  /**
   **/
  public DatiPagoPA idDominio(String idDominio) {
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
   **/
  public DatiPagoPA idIntermediario(String idIntermediario) {
    this.idIntermediario = idIntermediario;
    return this;
  }

  @JsonProperty("idIntermediario")
  public String getIdIntermediario() {
    return idIntermediario;
  }
  public void setIdIntermediario(String idIntermediario) {
    this.idIntermediario = idIntermediario;
  }

  /**
   **/
  public DatiPagoPA idStazione(String idStazione) {
    this.idStazione = idStazione;
    return this;
  }

  @JsonProperty("idStazione")
  public String getIdStazione() {
    return idStazione;
  }
  public void setIdStazione(String idStazione) {
    this.idStazione = idStazione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatiPagoPA datiPagoPA = (DatiPagoPA) o;
    return Objects.equals(idPsp, datiPagoPA.idPsp) &&
        Objects.equals(idCanale, datiPagoPA.idCanale) &&
        Objects.equals(idIntermediarioPsp, datiPagoPA.idIntermediarioPsp) &&
        Objects.equals(tipoVersamento, datiPagoPA.tipoVersamento) &&
        Objects.equals(modelloPagamento, datiPagoPA.modelloPagamento) &&
        Objects.equals(idDominio, datiPagoPA.idDominio) &&
        Objects.equals(idIntermediario, datiPagoPA.idIntermediario) &&
        Objects.equals(idStazione, datiPagoPA.idStazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPsp, idCanale, idIntermediarioPsp, tipoVersamento, modelloPagamento, idDominio, idIntermediario, idStazione);
  }

  public static DatiPagoPA parse(String json) throws ServiceException, ValidationException {
    return (DatiPagoPA) parse(json, DatiPagoPA.class); 
  }

  @Override
  public String getJsonIdFilter() {
    return "datiPagoPA";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatiPagoPA {\n");
    
    sb.append("    idPsp: ").append(toIndentedString(idPsp)).append("\n");
    sb.append("    idCanale: ").append(toIndentedString(idCanale)).append("\n");
    sb.append("    idIntermediarioPsp: ").append(toIndentedString(idIntermediarioPsp)).append("\n");
    sb.append("    tipoVersamento: ").append(toIndentedString(tipoVersamento)).append("\n");
    sb.append("    modelloPagamento: ").append(toIndentedString(modelloPagamento)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idIntermediario: ").append(toIndentedString(idIntermediario)).append("\n");
    sb.append("    idStazione: ").append(toIndentedString(idStazione)).append("\n");
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



