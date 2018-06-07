package it.govpay.core.rs.v1.beans.base;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.core.rs.v1.beans.JSONSerializable;

@org.codehaus.jackson.annotate.JsonPropertyOrder({
"stato",
"dettaglioStato",
"segnalazioni",
"rpt",
"rt",
"pendenza",
})
public class Rpp extends JSONSerializable {
  
  @JsonProperty("stato")
  private String stato = null;
  
  @JsonProperty("dettaglioStato")
  private String dettaglioStato = null;
  
  @JsonProperty("segnalazioni")
  private List<Segnalazione> segnalazioni = null;
  
  @JsonProperty("rpt")
  private CtRichiestaPagamentoTelematico rpt = null;
  
  @JsonProperty("rt")
  private CtRicevutaTelematica rt = null;
  
  @JsonProperty("pendenza")
  private PendenzaIndex pendenza = null;
  

  /**
   * Stato della richiesta di pagamento sulla piattaforma PagoPA.
   **/
  public Rpp stato(String stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   * Dettaglio fornito dal Nodo dei Pagamenti sullo stato della richiesta.
   **/
  public Rpp dettaglioStato(String dettaglioStato) {
    this.dettaglioStato = dettaglioStato;
    return this;
  }

  @JsonProperty("dettaglioStato")
  public String getDettaglioStato() {
    return dettaglioStato;
  }
  public void setDettaglioStato(String dettaglioStato) {
    this.dettaglioStato = dettaglioStato;
  }

  /**
   **/
  public Rpp segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return segnalazioni;
  }
  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico
   **/
  public Rpp rpt(CtRichiestaPagamentoTelematico rpt) {
    this.rpt = rpt;
    return this;
  }

  @JsonProperty("rpt")
  public CtRichiestaPagamentoTelematico getRpt() {
    return rpt;
  }
  public void setRpt(CtRichiestaPagamentoTelematico rpt) {
    this.rpt = rpt;
  }

  /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica
   **/
  public Rpp rt(CtRicevutaTelematica rt) {
    this.rt = rt;
    return this;
  }

  @JsonProperty("rt")
  public CtRicevutaTelematica getRt() {
    return rt;
  }
  public void setRt(CtRicevutaTelematica rt) {
    this.rt = rt;
  }

  /**
   **/
  public Rpp pendenza(PendenzaIndex pendenza) {
    this.pendenza = pendenza;
    return this;
  }

  @JsonProperty("pendenza")
  public PendenzaIndex getPendenza() {
    return pendenza;
  }
  public void setPendenza(PendenzaIndex pendenza) {
    this.pendenza = pendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Rpp rpp = (Rpp) o;
    return Objects.equals(stato, rpp.stato) &&
        Objects.equals(dettaglioStato, rpp.dettaglioStato) &&
        Objects.equals(segnalazioni, rpp.segnalazioni) &&
        Objects.equals(rpt, rpp.rpt) &&
        Objects.equals(rt, rpp.rt) &&
        Objects.equals(pendenza, rpp.pendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stato, dettaglioStato, segnalazioni, rpt, rt, pendenza);
  }

  public static Rpp parse(String json) {
    return (Rpp) parse(json, Rpp.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "rpp";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Rpp {\n");
    
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    dettaglioStato: ").append(toIndentedString(dettaglioStato)).append("\n");
    sb.append("    segnalazioni: ").append(toIndentedString(segnalazioni)).append("\n");
    sb.append("    rpt: ").append(toIndentedString(rpt)).append("\n");
    sb.append("    rt: ").append(toIndentedString(rt)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
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



