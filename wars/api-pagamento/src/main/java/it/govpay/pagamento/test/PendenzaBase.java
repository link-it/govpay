package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Dominio;
import it.govpay.pagamento.v2.beans.Segnalazione;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.StatoPendenza;
import it.govpay.pagamento.v2.beans.TassonomiaAvviso;
import it.govpay.pagamento.v2.beans.UnitaOperativa;
import it.govpay.pagamento.v2.beans.VocePendenza;
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

public class PendenzaBase  {
  
  @Schema(example = "A2A-12345", required = true, description = "Identificativo del gestionale responsabile della pendenza")
 /**
   * Identificativo del gestionale responsabile della pendenza  
  **/
  private String idA2A = null;
  
  @Schema(example = "abcdef12345", required = true, description = "Identificativo della pendenza nel gestionale responsabile")
 /**
   * Identificativo della pendenza nel gestionale responsabile  
  **/
  private String idPendenza = null;
  
  @Schema(required = true, description = "")
  private Dominio dominio = null;
  
  @Schema(description = "")
  private UnitaOperativa unitaOperativa = null;
  
  @Schema(required = true, description = "")
  private StatoPendenza stato = null;
  
  @Schema(description = "")
  private List<Segnalazione> segnalazioni = null;
  
  @Schema(example = "12345", description = "Iuv avviso, assegnato se pagabile da psp")
 /**
   * Iuv avviso, assegnato se pagabile da psp  
  **/
  private String iuvAvviso = null;
  
  @Schema(example = "12345", description = "Iuv dell'ultimo pagamento eseguito con successo")
 /**
   * Iuv dell'ultimo pagamento eseguito con successo  
  **/
  private String iuvPagamento = null;
  
  @Schema(description = "Data di pagamento della pendenza")
 /**
   * Data di pagamento della pendenza  
  **/
  private LocalDate dataPagamento = null;
  
  @Schema(example = "Sanzione CdS n. abc00000", required = true, description = "Descrizione da inserire nell'avviso di pagamento")
 /**
   * Descrizione da inserire nell'avviso di pagamento  
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
  
  @Schema(required = true, description = "Data di emissione della pendenza")
 /**
   * Data di emissione della pendenza  
  **/
  private LocalDate dataCaricamento = null;
  
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
  
  @Schema(description = "")
  private TassonomiaAvviso tassonomiaAvviso = null;
  
  @Schema(required = true, description = "")
  private List<VocePendenza> voci = new ArrayList<VocePendenza>();
 /**
   * Identificativo del gestionale responsabile della pendenza
   * @return idA2A
  **/
  @JsonProperty("idA2A")
  @NotNull
  public String getIdA2A() {
    return idA2A;
  }

  public void setIdA2A(String idA2A) {
    this.idA2A = idA2A;
  }

  public PendenzaBase idA2A(String idA2A) {
    this.idA2A = idA2A;
    return this;
  }

 /**
   * Identificativo della pendenza nel gestionale responsabile
   * @return idPendenza
  **/
  @JsonProperty("idPendenza")
  @NotNull
  public String getIdPendenza() {
    return idPendenza;
  }

  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  public PendenzaBase idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

 /**
   * Get dominio
   * @return dominio
  **/
  @JsonProperty("dominio")
  @NotNull
  public Dominio getDominio() {
    return dominio;
  }

  public void setDominio(Dominio dominio) {
    this.dominio = dominio;
  }

  public PendenzaBase dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * Get unitaOperativa
   * @return unitaOperativa
  **/
  @JsonProperty("unitaOperativa")
  public UnitaOperativa getUnitaOperativa() {
    return unitaOperativa;
  }

  public void setUnitaOperativa(UnitaOperativa unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
  }

  public PendenzaBase unitaOperativa(UnitaOperativa unitaOperativa) {
    this.unitaOperativa = unitaOperativa;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoPendenza getStato() {
    return stato;
  }

  public void setStato(StatoPendenza stato) {
    this.stato = stato;
  }

  public PendenzaBase stato(StatoPendenza stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Get segnalazioni
   * @return segnalazioni
  **/
  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return segnalazioni;
  }

  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  public PendenzaBase segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  public PendenzaBase addSegnalazioniItem(Segnalazione segnalazioniItem) {
    this.segnalazioni.add(segnalazioniItem);
    return this;
  }

 /**
   * Iuv avviso, assegnato se pagabile da psp
   * @return iuvAvviso
  **/
  @JsonProperty("iuvAvviso")
  public String getIuvAvviso() {
    return iuvAvviso;
  }

  public void setIuvAvviso(String iuvAvviso) {
    this.iuvAvviso = iuvAvviso;
  }

  public PendenzaBase iuvAvviso(String iuvAvviso) {
    this.iuvAvviso = iuvAvviso;
    return this;
  }

 /**
   * Iuv dell&#x27;ultimo pagamento eseguito con successo
   * @return iuvPagamento
  **/
  @JsonProperty("iuvPagamento")
  public String getIuvPagamento() {
    return iuvPagamento;
  }

  public void setIuvPagamento(String iuvPagamento) {
    this.iuvPagamento = iuvPagamento;
  }

  public PendenzaBase iuvPagamento(String iuvPagamento) {
    this.iuvPagamento = iuvPagamento;
    return this;
  }

 /**
   * Data di pagamento della pendenza
   * @return dataPagamento
  **/
  @JsonProperty("dataPagamento")
  public LocalDate getDataPagamento() {
    return dataPagamento;
  }

  public void setDataPagamento(LocalDate dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public PendenzaBase dataPagamento(LocalDate dataPagamento) {
    this.dataPagamento = dataPagamento;
    return this;
  }

 /**
   * Descrizione da inserire nell&#x27;avviso di pagamento
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

  public PendenzaBase descrizione(String descrizione) {
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

  public PendenzaBase soggettoPagatore(Soggetto soggettoPagatore) {
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

  public PendenzaBase importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

 /**
   * Identificativo univoco versamento, assegnato se pagabile da psp
   * @return numeroAvviso
  **/
  @JsonProperty("numeroAvviso")
 @Pattern(regexp="[0-9]18")  public String getNumeroAvviso() {
    return numeroAvviso;
  }

  public void setNumeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
  }

  public PendenzaBase numeroAvviso(String numeroAvviso) {
    this.numeroAvviso = numeroAvviso;
    return this;
  }

 /**
   * Data di emissione della pendenza
   * @return dataCaricamento
  **/
  @JsonProperty("dataCaricamento")
  @NotNull
  public LocalDate getDataCaricamento() {
    return dataCaricamento;
  }

  public void setDataCaricamento(LocalDate dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
  }

  public PendenzaBase dataCaricamento(LocalDate dataCaricamento) {
    this.dataCaricamento = dataCaricamento;
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

  public PendenzaBase dataValidita(LocalDate dataValidita) {
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

  public PendenzaBase dataScadenza(LocalDate dataScadenza) {
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

  public PendenzaBase annoRiferimento(BigDecimal annoRiferimento) {
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

  public PendenzaBase cartellaPagamento(String cartellaPagamento) {
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

  public PendenzaBase datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
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

  public PendenzaBase tassonomiaAvviso(TassonomiaAvviso tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

 /**
   * Get voci
   * @return voci
  **/
  @JsonProperty("voci")
  @NotNull
 @Size(min=1,max=5)  public List<VocePendenza> getVoci() {
    return voci;
  }

  public void setVoci(List<VocePendenza> voci) {
    this.voci = voci;
  }

  public PendenzaBase voci(List<VocePendenza> voci) {
    this.voci = voci;
    return this;
  }

  public PendenzaBase addVociItem(VocePendenza vociItem) {
    this.voci.add(vociItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendenzaBase {\n");
    
    sb.append("    idA2A: ").append(toIndentedString(idA2A)).append("\n");
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    unitaOperativa: ").append(toIndentedString(unitaOperativa)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
    sb.append("    iuvAvviso: ").append(toIndentedString(iuvAvviso)).append("\n");
    sb.append("    iuvPagamento: ").append(toIndentedString(iuvPagamento)).append("\n");
    sb.append("    dataPagamento: ").append(toIndentedString(dataPagamento)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    soggettoPagatore: ").append(toIndentedString(soggettoPagatore)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    numeroAvviso: ").append(toIndentedString(numeroAvviso)).append("\n");
    sb.append("    dataCaricamento: ").append(toIndentedString(dataCaricamento)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    annoRiferimento: ").append(toIndentedString(annoRiferimento)).append("\n");
    sb.append("    cartellaPagamento: ").append(toIndentedString(cartellaPagamento)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
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
