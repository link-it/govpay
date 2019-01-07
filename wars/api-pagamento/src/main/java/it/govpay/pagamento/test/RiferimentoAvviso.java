package it.govpay.pagamento.test;

import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RiferimentoAvviso  {
  
  @Schema(example = "1.23456789E+9", required = true, description = "Codice fiscale dell'ente creditore")
 /**
   * Codice fiscale dell'ente creditore  
  **/
  private String idDominio = null;
  
  @Schema(example = "123456789012345678", required = true, description = "Numero avviso della pendenza, assegnato se pagabile da psp")
 /**
   * Numero avviso della pendenza, assegnato se pagabile da psp  
  **/
  private String numeroAvviso = null;
  
  @Schema(example = "RSSMRA30A01H501I", description = "Identificativo del soggetto debitore  della pendenza riferita dall'avviso")
 /**
   * Identificativo del soggetto debitore  della pendenza riferita dall'avviso  
  **/
  private String idDebitore = null;
 /**
   * Codice fiscale dell&#x27;ente creditore
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  @NotNull
  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public RiferimentoAvviso idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Numero avviso della pendenza, assegnato se pagabile da psp
   * @return numeroAvviso
  **/
  @JsonProperty("numeroAvviso")
  @NotNull
  public String getNumeroAvviso() {
    return numeroAvviso;
  }

  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public RiferimentoAvviso numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

 /**
   * Identificativo del soggetto debitore  della pendenza riferita dall&#x27;avviso
   * @return idDebitore
  **/
  @JsonProperty("idDebitore")
  public String getIdDebitore() {
    return idDebitore;
  }

  public void setIdDebitore(String idDebitore) {
    this.idDebitore = idDebitore;
  }

  public RiferimentoAvviso idDebitore(String idDebitore) {
    this.idDebitore = idDebitore;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoAvviso {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    idDebitore: ").append(toIndentedString(idDebitore)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
