package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class FlussoRendicontazioneIndex   {
  
  @Schema(example = "2017-11-21GovPAYPsp1-10:27:27.903", required = true, description = "Identificativo del flusso di rendicontazione")
 /**
   * Identificativo del flusso di rendicontazione  
  **/
  private String idFlusso = null;
  
  @Schema(required = true, description = "Data di emissione del flusso")
 /**
   * Data di emissione del flusso  
  **/
  private Date dataFlusso = null;
  
  @Schema(required = true, description = "Data di acquisizione del flusso")
 /**
   * Data di acquisizione del flusso  
  **/
  private Date data = null;
  
  @Schema(example = "idriversamento12345", required = true, description = "Identificativo dell'operazione di riversamento assegnato dal psp debitore")
 /**
   * Identificativo dell'operazione di riversamento assegnato dal psp debitore  
  **/
  private String trn = null;
  
  @Schema(required = true, description = "Data dell'operazione di riversamento fondi")
 /**
   * Data dell'operazione di riversamento fondi  
  **/
  private Date dataRegolamento = null;
  
  @Schema(example = "ABI-12345", required = true, description = "Identificativo dell'istituto mittente")
 /**
   * Identificativo dell'istituto mittente  
  **/
  private String idPsp = null;
  
  @Schema(example = "BIC-12345", description = "Codice Bic della banca che ha generato il riversamento")
 /**
   * Codice Bic della banca che ha generato il riversamento  
  **/
  private String bicRiversamento = null;
  
  @Schema(required = true, description = "")
  private Dominio dominio = null;
  
  @Schema(example = "3", required = true, description = "numero di pagamenti oggetto della rendicontazione")
 /**
   * numero di pagamenti oggetto della rendicontazione  
  **/
  private BigDecimal numeroPagamenti = null;
  
  @Schema(example = "100.01", required = true, description = "somma degli importi rendicontati")
 /**
   * somma degli importi rendicontati  
  **/
  private Double importoTotale = null;
  
  @Schema(required = true, description = "")
  private StatoFlussoRendicontazione stato = null;
  
  @Schema(description = "")
  private List<Segnalazione> segnalazioni = null;
 /**
   * Identificativo del flusso di rendicontazione
   * @return idFlusso
  **/
  @JsonProperty("idFlusso")
  @NotNull
  public String getIdFlusso() {
    return idFlusso;
  }

  public void setIdFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
  }

  public FlussoRendicontazioneIndex idFlusso(String idFlusso) {
    this.idFlusso = idFlusso;
    return this;
  }

 /**
   * Data di emissione del flusso
   * @return dataFlusso
  **/
  @JsonProperty("dataFlusso")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale = "it_IT", timezone = "Europe/Rome")  
  @NotNull
  public Date getDataFlusso() {
    return dataFlusso;
  }

  public void setDataFlusso(Date dataFlusso) {
    this.dataFlusso = dataFlusso;
  }

  public FlussoRendicontazioneIndex dataFlusso(Date dataFlusso) {
    this.dataFlusso = dataFlusso;
    return this;
  }

 /**
   * Data di acquisizione del flusso
   * @return data
  **/
  @JsonProperty("data")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale = "it_IT", timezone = "Europe/Rome")
  @NotNull
  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public FlussoRendicontazioneIndex data(Date data) {
    this.data = data;
    return this;
  }

 /**
   * Identificativo dell&#x27;operazione di riversamento assegnato dal psp debitore
   * @return trn
  **/
  @JsonProperty("trn")
  @NotNull
  public String getTrn() {
    return trn;
  }

  public void setTrn(String trn) {
    this.trn = trn;
  }

  public FlussoRendicontazioneIndex trn(String trn) {
    this.trn = trn;
    return this;
  }

 /**
   * Data dell&#x27;operazione di riversamento fondi
   * @return dataRegolamento
  **/
  @JsonProperty("dataRegolamento")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  @NotNull
  public Date getDataRegolamento() {
    return dataRegolamento;
  }

  public void setDataRegolamento(Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
  }

  public FlussoRendicontazioneIndex dataRegolamento(Date dataRegolamento) {
    this.dataRegolamento = dataRegolamento;
    return this;
  }

 /**
   * Identificativo dell&#x27;istituto mittente
   * @return idPsp
  **/
  @JsonProperty("idPsp")
  @NotNull
  public String getIdPsp() {
    return idPsp;
  }

  public void setIdPsp(String idPsp) {
    this.idPsp = idPsp;
  }

  public FlussoRendicontazioneIndex idPsp(String idPsp) {
    this.idPsp = idPsp;
    return this;
  }

 /**
   * Codice Bic della banca che ha generato il riversamento
   * @return bicRiversamento
  **/
  @JsonProperty("bicRiversamento")
  public String getBicRiversamento() {
    return bicRiversamento;
  }

  public void setBicRiversamento(String bicRiversamento) {
    this.bicRiversamento = bicRiversamento;
  }

  public FlussoRendicontazioneIndex bicRiversamento(String bicRiversamento) {
    this.bicRiversamento = bicRiversamento;
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

  public FlussoRendicontazioneIndex dominio(Dominio dominio) {
    this.dominio = dominio;
    return this;
  }

 /**
   * numero di pagamenti oggetto della rendicontazione
   * @return numeroPagamenti
  **/
  @JsonProperty("numeroPagamenti")
  @NotNull
  public BigDecimal getNumeroPagamenti() {
    return numeroPagamenti;
  }

  public void setNumeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
  }

  public FlussoRendicontazioneIndex numeroPagamenti(BigDecimal numeroPagamenti) {
    this.numeroPagamenti = numeroPagamenti;
    return this;
  }

 /**
   * somma degli importi rendicontati
   * @return importoTotale
  **/
  @JsonProperty("importoTotale")
  @NotNull
  public Double getImportoTotale() {
    return importoTotale;
  }

  public void setImportoTotale(Double importoTotale) {
    this.importoTotale = importoTotale;
  }

  public FlussoRendicontazioneIndex importoTotale(Double importoTotale) {
    this.importoTotale = importoTotale;
    return this;
  }

 /**
   * Get stato
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public StatoFlussoRendicontazione getStato() {
    return stato;
  }

  public void setStato(StatoFlussoRendicontazione stato) {
    this.stato = stato;
  }

  public FlussoRendicontazioneIndex stato(StatoFlussoRendicontazione stato) {
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

  public FlussoRendicontazioneIndex segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  public FlussoRendicontazioneIndex addSegnalazioniItem(Segnalazione segnalazioniItem) {
    this.segnalazioni.add(segnalazioniItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlussoRendicontazioneIndex {\n");
    
    sb.append("    idFlusso: ").append(toIndentedString(idFlusso)).append("\n");
    sb.append("    dataFlusso: ").append(toIndentedString(dataFlusso)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    trn: ").append(toIndentedString(trn)).append("\n");
    sb.append("    dataRegolamento: ").append(toIndentedString(dataRegolamento)).append("\n");
    sb.append("    idPsp: ").append(toIndentedString(idPsp)).append("\n");
    sb.append("    bicRiversamento: ").append(toIndentedString(bicRiversamento)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    numeroPagamenti: ").append(toIndentedString(numeroPagamenti)).append("\n");
    sb.append("    importoTotale: ").append(toIndentedString(importoTotale)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
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
