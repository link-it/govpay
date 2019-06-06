package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idPsp",
"tipoVersamento",
"identificativoFruitore",
"identificativoErogatore",
"idCanale",
"idStazione",
})
public class Controparte extends JSONSerializable {
  
  @JsonProperty("idPsp")
  private String idPsp = null;
  
  @JsonProperty("tipoVersamento")
  private String tipoVersamento = null;
  
  @JsonProperty("identificativoFruitore")
  private String identificativoFruitore = null;
  
  @JsonProperty("identificativoErogatore")
  private String identificativoErogatore = null;
  
  @JsonProperty("idCanale")
  private String idCanale = null;
  
  @JsonProperty("idStazione")
  private String idStazione = null;
  
  /**
   * Identificativo del PSP
   **/
  public Controparte idPsp(String idPsp) {
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
   * Tipologia di versamento realizzato
   **/
  public Controparte tipoVersamento(String tipoVersamento) {
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
   **/
  public Controparte identificativoFruitore(String identificativoFruitore) {
    this.identificativoFruitore = identificativoFruitore;
    return this;
  }

  @JsonProperty("identificativoFruitore")
  public String getIdentificativoFruitore() {
    return identificativoFruitore;
  }
  public void setIdentificativoFruitore(String identificativoFruitore) {
    this.identificativoFruitore = identificativoFruitore;
  }

  /**
   **/
  public Controparte identificativoErogatore(String identificativoErogatore) {
    this.identificativoErogatore = identificativoErogatore;
    return this;
  }

  @JsonProperty("identificativoErogatore")
  public String getIdentificativoErogatore() {
    return identificativoErogatore;
  }
  public void setIdentificativoErogatore(String identificativoErogatore) {
    this.identificativoErogatore = identificativoErogatore;
  }

  /**
   **/
  public Controparte idCanale(String idCanale) {
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
  public Controparte idStazione(String idStazione) {
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
    Controparte controparte = (Controparte) o;
    return Objects.equals(idPsp, controparte.idPsp) &&
        Objects.equals(tipoVersamento, controparte.tipoVersamento) &&
        Objects.equals(identificativoFruitore, controparte.identificativoFruitore) &&
        Objects.equals(identificativoErogatore, controparte.identificativoErogatore) &&
        Objects.equals(idCanale, controparte.idCanale) &&
        Objects.equals(idStazione, controparte.idStazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPsp, tipoVersamento, identificativoFruitore, identificativoErogatore, idCanale, idStazione);
  }

  public static Controparte parse(String json) throws ServiceException, ValidationException { 
    return (Controparte) parse(json, Controparte.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "controparte";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Controparte {\n");
    
    sb.append("    idPsp: ").append(toIndentedString(idPsp)).append("\n");
    sb.append("    tipoVersamento: ").append(toIndentedString(tipoVersamento)).append("\n");
    sb.append("    identificativoFruitore: ").append(toIndentedString(identificativoFruitore)).append("\n");
    sb.append("    identificativoErogatore: ").append(toIndentedString(identificativoErogatore)).append("\n");
    sb.append("    idCanale: ").append(toIndentedString(idCanale)).append("\n");
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



