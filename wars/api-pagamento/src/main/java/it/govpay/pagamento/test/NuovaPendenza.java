package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.NuovaVocePendenza;
import it.govpay.pagamento.v2.beans.RiferimentoPendenza;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.TassonomiaAvviso;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

public class NuovaPendenza extends RiferimentoPendenza {
  
  @Schema(example = "1234567890", required = true, description = "Identificativo del dominio creditore")
 /**
   * Identificativo del dominio creditore  
  **/
  private String idDominio = null;
  
  @Schema(example = "UO33132", description = "Identificativo dell'unita' operativa")
 /**
   * Identificativo dell'unita' operativa  
  **/
  private String idUnitaOperativa = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "Descrizione della pendenza")
 /**
   * Descrizione della pendenza  
  **/
  private String descrizione = null;
  
  @Schema(required = true, description = "")
  private Soggetto soggettoPagatore = null;
  
  @Schema(example = "10.01", required = true, description = "Importo della pendenza. Deve corrispondere alla somma delle singole voci.")
 /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.  
  **/
  private BigDecimal importo = null;
  
  @Schema(example = "123456789012345678", description = "Identificativo univoco versamento, assegnato se pagabile da psp")
 /**
   * Identificativo univoco versamento, assegnato se pagabile da psp  
  **/
  private String numeroAvviso = null;
  
  @Schema(description = "")
  private TassonomiaAvviso tassonomiaAvviso = null;
  
  @Schema(description = "Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.")
 /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.  
  **/
  private LocalDate dataValidita = null;
  
  @Schema(description = "Data di scadenza della pendenza, decorsa la quale non è più pagabile.")
 /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.  
  **/
  private LocalDate dataScadenza = null;
  
  @Schema(example = "2020", description = "Anno di riferimento della pendenza")
 /**
   * Anno di riferimento della pendenza  
  **/
  private BigDecimal annoRiferimento = null;
  
  @Schema(example = "ABC00000001", description = "Identificativo della cartella di pagamento a cui afferisce la pendenza")
 /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza  
  **/
  private String cartellaPagamento = null;
  
  @Schema(description = "Dati applicativi allegati dal gestionale secondo un formato proprietario.")
 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.  
  **/
  private Object datiAllegati = null;
  
  @Schema(required = true, description = "")
  private List<NuovaVocePendenza> voci = new ArrayList<NuovaVocePendenza>();
 /**
   * Identificativo del dominio creditore
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

  public NuovaPendenza idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Identificativo dell&#x27;unita&#x27; operativa
   * @return idUnitaOperativa
  **/
  @JsonProperty("idUnitaOperativa")
  public String getIdUnitaOperativa() {
    return idUnitaOperativa;
  }

  public void setIdUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
  }

  public NuovaPendenza idUnitaOperativa(String idUnitaOperativa) {
    this.idUnitaOperativa = idUnitaOperativa;
    return this;
  }

 /**
   * Descrizione della pendenza
   * @return descrizione
  **/
  @JsonProperty("descrizione")
  @NotNull
 @Size(max=140)  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public NuovaPendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

 /**
   * Get soggettoPagatore
   * @return soggettoPagatore
  **/
  @JsonProperty("soggettoPagatore")
  @NotNull
  public Soggetto getSoggettoPagatore() {
    return soggettoPagatore;
  }

  public void setSoggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
  }

  public NuovaPendenza soggettoPagatore(Soggetto soggettoPagatore) {
    this.soggettoPagatore = soggettoPagatore;
    return this;
  }

 /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   * minimum: 0
   * maximum: 99999999
   * @return importo
  **/
  @JsonProperty("importo")
  @NotNull
 @DecimalMin("0") @DecimalMax("99999999")  public BigDecimal getImporto() {
    return importo;
  }

  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  public NuovaPendenza importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   * @return numeroAvviso
  **/
  @JsonProperty("numeroAvviso")
 @Pattern(regexp="[0-9]{18}")  public String getNumeroAvviso() {
    return numeroAvviso;
  }

  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public NuovaPendenza numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

 /**
   * Get tassonomiaAvviso
   * @return tassonomiaAvviso
  **/
  @JsonProperty("tassonomiaAvviso")
  public TassonomiaAvviso getTassonomiaAvviso() {
    return tassonomiaAvviso;
  }

  public void setTassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  public NuovaPendenza tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

 /**
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   * @return dataValidita
  **/
  @JsonProperty("dataValidita")
  public LocalDate getDataValidita() {
    return dataValidita;
  }

  public void setDataValidita(LocalDate dataValidita) {
    this.dataValidita = dataValidita;
  }

  public NuovaPendenza dataValidita(LocalDate dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

 /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   * @return dataScadenza
  **/
  @JsonProperty("dataScadenza")
  public LocalDate getDataScadenza() {
    return dataScadenza;
  }

  public void setDataScadenza(LocalDate dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  public NuovaPendenza dataScadenza(LocalDate dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

 /**
   * Anno di riferimento della pendenza
   * @return annoRiferimento
  **/
  @JsonProperty("annoRiferimento")
  public BigDecimal getAnnoRiferimento() {
    return annoRiferimento;
  }

  public void setAnnoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
  }

  public NuovaPendenza annoRiferimento(BigDecimal annoRiferimento) {
    this.annoRiferimento = annoRiferimento;
    return this;
  }

 /**
   * Identificativo della cartella di pagamento a cui afferisce la pendenza
   * @return cartellaPagamento
  **/
  @JsonProperty("cartellaPagamento")
  public String getCartellaPagamento() {
    return cartellaPagamento;
  }

  public void setCartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
  }

  public NuovaPendenza cartellaPagamento(String cartellaPagamento) {
    this.cartellaPagamento = cartellaPagamento;
    return this;
  }

 /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   * @return datiAllegati
  **/
  @JsonProperty("datiAllegati")
  public Object getDatiAllegati() {
    return datiAllegati;
  }

  public void setDatiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  public NuovaPendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

 /**
   * Get voci
   * @return voci
  **/
  @JsonProperty("voci")
  @NotNull
 @Size(min=1,max=5)  public List<NuovaVocePendenza> getVoci() {
    return voci;
  }

  public void setVoci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
  }

  public NuovaPendenza voci(List<NuovaVocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  public NuovaPendenza addVociItem(NuovaVocePendenza vociItem) {
    this.voci.add(vociItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaPendenza {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    idUnitaOperativa: ").append(toIndentedString(idUnitaOperativa)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    voci: ").append(toIndentedString(voci)).append("\n");
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
