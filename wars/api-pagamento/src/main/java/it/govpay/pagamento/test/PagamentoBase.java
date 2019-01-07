package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Conto;
import it.govpay.pagamento.v2.beans.NuovoPagamentoBase;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.StatoPagamento;
import it.govpay.pagamento.v2.beans.TipoAutenticazioneSoggetto;
import java.math.BigDecimal;
import org.joda.time.LocalDate;
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

public class PagamentoBase extends NuovoPagamentoBase {
  
  @Schema(example = "f9866575-e255-4d10-9845-57ba88bec136", required = true, description = "Identificativo del pagamento assegnato da GovPay")
 /**
   * Identificativo del pagamento assegnato da GovPay  
  **/
  private String id = null;
  
  @Schema(example = "Immatricolazione AA 2017/2018 ed altre 3 pendenze", required = true, description = "Identificativo del pagamento assegnato da GovPay")
 /**
   * Identificativo del pagamento assegnato da GovPay  
  **/
  private String nome = null;
  
  @Schema(required = true, description = "")
  private StatoPagamento stato = null;
  
  @Schema(required = true, description = "Importo del pagamento. Corrisponde alla somma degli importi delle pendenze al momento della richiesta")
 /**
   * Importo del pagamento. Corrisponde alla somma degli importi delle pendenze al momento della richiesta  
  **/
  private BigDecimal importo = null;
  
  @Schema(example = "7f905970-cfe9-4ee5-a9a7-81fe14e7437e", description = "Identificativo del pagamento assegnato dal psp utilizzato")
 /**
   * Identificativo del pagamento assegnato dal psp utilizzato  
  **/
  private String idSessionePsp = null;
  
  @Schema(example = "https://www.psp.it/payment?sessionId=7f905970-cfe9-4ee5-a9a7-81fe14e7437e", description = "Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello")
 /**
   * Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello  
  **/
  private String pspRedirectUrl = null;
 /**
   * Identificativo del pagamento assegnato da GovPay
   * @return id
  **/
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PagamentoBase id(String id) {
    this.id = id;
    return this;
  }

 /**
   * Identificativo del pagamento assegnato da GovPay
   * @return nome
  **/
  @JsonProperty("nome")
  @NotNull
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public PagamentoBase nome(String nome) {
    this.nome = nome;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoPagamento getStato() {
    return stato;
  }

  public void setStato(StatoPagamento stato) {
    this.stato = stato;
  }

  public PagamentoBase stato(StatoPagamento stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Importo del pagamento. Corrisponde alla somma degli importi delle pendenze al momento della richiesta
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public PagamentoBase importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Identificativo del pagamento assegnato dal psp utilizzato
   * @return idSessionePsp
  **/
  @JsonProperty("idSessionePsp")
  public String getIdSessionePsp() {
    return idSessionePsp;
  }

  public void setIdSessionePsp(String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
  }

  public PagamentoBase idSessionePsp(String idSessionePsp) {
    this.idSessionePsp = idSessionePsp;
    return this;
  }

 /**
   * Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello
   * @return pspRedirectUrl
  **/
  @JsonProperty("pspRedirectUrl")
  public String getPspRedirectUrl() {
    return pspRedirectUrl;
  }

  public void setPspRedirectUrl(String pspRedirectUrl) {
    this.pspRedirectUrl = pspRedirectUrl;
  }

  public PagamentoBase pspRedirectUrl(String pspRedirectUrl) {
    this.pspRedirectUrl = pspRedirectUrl;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagamentoBase {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    idSessionePsp: ").append(toIndentedString(idSessionePsp)).append("\n");
    sb.append("    pspRedirectUrl: ").append(toIndentedString(pspRedirectUrl)).append("\n");
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
