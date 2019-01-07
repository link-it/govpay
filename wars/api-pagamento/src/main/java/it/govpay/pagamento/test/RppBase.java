package it.govpay.pagamento.test;

import it.govpay.pagamento.v2.beans.Segnalazione;
import java.util.ArrayList;
import java.util.List;
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

public class RppBase  {
  
  @Schema(example = "RT_ACCETTATA_PA", required = true, description = "Stato della richiesta di pagamento sulla piattaforma PagoPA.")
 /**
   * Stato della richiesta di pagamento sulla piattaforma PagoPA.  
  **/
  private String stato = null;
  
  @Schema(description = "Dettaglio fornito dal Nodo dei Pagamenti sullo stato della richiesta.")
 /**
   * Dettaglio fornito dal Nodo dei Pagamenti sullo stato della richiesta.  
  **/
  private String dettaglioStato = null;
  
  @Schema(description = "")
  private List<Segnalazione> segnalazioni = null;
  
  @Schema(required = true, description = "Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico")
 /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico  
  **/
  private Object rpt = null;
  
  @Schema(description = "Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica")
 /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica  
  **/
  private Object rt = null;
 /**
   * Stato della richiesta di pagamento sulla piattaforma PagoPA.
   * @return stato
  **/
  @JsonProperty("stato")
  @NotNull
  public String getStato() {
    return stato;
  }

  public void setStato(String stato) {
    this.stato = stato;
  }

  public RppBase stato(String stato) {
    this.stato = stato;
    return this;
  }

 /**
   * Dettaglio fornito dal Nodo dei Pagamenti sullo stato della richiesta.
   * @return dettaglioStato
  **/
  @JsonProperty("dettaglioStato")
  public String getDettaglioStato() {
    return dettaglioStato;
  }

  public void setDettaglioStato(String dettaglioStato) {
    this.dettaglioStato = dettaglioStato;
  }

  public RppBase dettaglioStato(String dettaglioStato) {
    this.dettaglioStato = dettaglioStato;
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

  public RppBase segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  public RppBase addSegnalazioniItem(Segnalazione segnalazioniItem) {
    this.segnalazioni.add(segnalazioniItem);
    return this;
  }

 /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico
   * @return rpt
  **/
  @JsonProperty("rpt")
  @NotNull
  public Object getRpt() {
    return rpt;
  }

  public void setRpt(Object rpt) {
    this.rpt = rpt;
  }

  public RppBase rpt(Object rpt) {
    this.rpt = rpt;
    return this;
  }

 /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica
   * @return rt
  **/
  @JsonProperty("rt")
  public Object getRt() {
    return rt;
  }

  public void setRt(Object rt) {
    this.rt = rt;
  }

  public RppBase rt(Object rt) {
    this.rt = rt;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RppBase {\n");
    
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    dettaglioStato: ").append(toIndentedString(dettaglioStato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
    sb.append("    rpt: ").append(toIndentedString(rpt)).append("\n");
    sb.append("    rt: ").append(toIndentedString(rt)).append("\n");
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
