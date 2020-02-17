package it.govpay.backoffice.v1.beans;

import java.util.List;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

import it.govpay.core.beans.JSONSerializable;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"stato",
"dettaglioStato",
"bloccante",
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
  
  @JsonProperty("bloccante")
  private Boolean bloccante = true;
  
  @JsonProperty("segnalazioni")
  private List<Segnalazione> segnalazioni = null;
  
  @JsonProperty("rpt")
  @JsonRawValue
  private String rpt = null;
  
  @JsonProperty("rt")
  @JsonRawValue
  private String rt = null;
  
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
    return this.stato;
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
    return this.dettaglioStato;
  }
  public void setDettaglioStato(String dettaglioStato) {
    this.dettaglioStato = dettaglioStato;
  }

  /**
   * Indica se la richiesta di pagamento deve essere bloccata quando viene inviata mentre e' ancora in corso il tentativo precedente
   **/
  public Rpp bloccante(Boolean bloccante) {
    this.bloccante = bloccante;
    return this;
  }

  @JsonProperty("bloccante")
  public Boolean Bloccante() {
    return bloccante;
  }
  public void setBloccante(Boolean bloccante) {
    this.bloccante = bloccante;
  }

  /**
   **/
  public Rpp segnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
    return this;
  }

  @JsonProperty("segnalazioni")
  public List<Segnalazione> getSegnalazioni() {
    return this.segnalazioni;
  }
  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  /**
   * Rpt inviata a PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRichiestaPagamentoTelematico
   **/
  public Rpp rpt(String rpt) {
    this.rpt = rpt;
    return this;
  }

  @JsonProperty("rpt")
  @JsonRawValue
  public String getRpt() {
    return this.rpt;
  }
  public void setRpt(String rpt) {
    this.rpt = rpt;
  }

  /**
   * Rt inviata da PagoPa. {http://www.digitpa.gov.it/schemas/2011/Pagamenti/} ctRicevutaTelematica
   **/
  public Rpp rt(String rt) {
    this.rt = rt;
    return this;
  }

  @JsonProperty("rt")
  @JsonRawValue
  public String getRt() {
    return this.rt;
  }
  public void setRt(String rt) {
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
    return this.pendenza;
  }
  public void setPendenza(PendenzaIndex pendenza) {
    this.pendenza = pendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Rpp rpp = (Rpp) o;
    return Objects.equals(this.stato, rpp.stato) &&
        Objects.equals(this.dettaglioStato, rpp.dettaglioStato) &&
        Objects.equals(bloccante, rpp.bloccante) &&
        Objects.equals(this.segnalazioni, rpp.segnalazioni) &&
        Objects.equals(this.rpt, rpp.rpt) &&
        Objects.equals(this.rt, rpp.rt) &&
        Objects.equals(this.pendenza, rpp.pendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.stato, this.dettaglioStato, this.bloccante, this.segnalazioni, this.rpt, this.rt, this.pendenza);
  }

  public static Rpp parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Rpp.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "rpp";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Rpp {\n");
    
    sb.append("    stato: ").append(this.toIndentedString(this.stato)).append("\n");
    sb.append("    dettaglioStato: ").append(this.toIndentedString(this.dettaglioStato)).append("\n");
    sb.append("    bloccante: ").append(toIndentedString(bloccante)).append("\n");
    sb.append("    segnalazioni: ").append(this.toIndentedString(this.segnalazioni)).append("\n");
    sb.append("    rpt: ").append(this.toIndentedString(this.rpt)).append("\n");
    sb.append("    rt: ").append(this.toIndentedString(this.rt)).append("\n");
    sb.append("    pendenza: ").append(this.toIndentedString(this.pendenza)).append("\n");
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



