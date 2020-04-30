package it.govpay.backoffice.v1.beans;


import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"numeroRendicontazioni",
"importo",
"dettaglio",
"flussoRendicontazione",
"direzione",
"divisione",
})
public class StatisticaQuadraturaRendicontazione extends JSONSerializable {
  
  @JsonProperty("numeroRendicontazioni")
  private BigDecimal numeroRendicontazioni = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("dettaglio")
  private String dettaglio = null;
  
  @JsonProperty("flussoRendicontazione")
  private FlussoRendicontazioneIndex flussoRendicontazione = null;
  
  @JsonProperty("direzione")
  private String direzione = null;
  
  @JsonProperty("divisione")
  private String divisione = null;
  
  /**
   * Numero di rendicontazioni corrispondenti ai parametri impostati
   **/
  public StatisticaQuadraturaRendicontazione numeroRendicontazioni(BigDecimal numeroRendicontazioni) {
    this.numeroRendicontazioni = numeroRendicontazioni;
    return this;
  }

  @JsonProperty("numeroRendicontazioni")
  public BigDecimal getNumeroRendicontazioni() {
    return numeroRendicontazioni;
  }
  public void setNumeroRendicontazioni(BigDecimal numeroRendicontazioni) {
    this.numeroRendicontazioni = numeroRendicontazioni;
  }

  /**
   * Importo Totale corrispondente ai parametri impostati
   **/
  public StatisticaQuadraturaRendicontazione importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  /**
   * Url al dettaglio della statistica
   **/
  public StatisticaQuadraturaRendicontazione dettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
    return this;
  }

  @JsonProperty("dettaglio")
  public String getDettaglio() {
    return dettaglio;
  }
  public void setDettaglio(String dettaglio) {
    this.dettaglio = dettaglio;
  }

  /**
   **/
  public StatisticaQuadraturaRendicontazione flussoRendicontazione(FlussoRendicontazioneIndex flussoRendicontazione) {
    this.flussoRendicontazione = flussoRendicontazione;
    return this;
  }

  @JsonProperty("flussoRendicontazione")
  public FlussoRendicontazioneIndex getFlussoRendicontazione() {
    return flussoRendicontazione;
  }
  public void setFlussoRendicontazione(FlussoRendicontazioneIndex flussoRendicontazione) {
    this.flussoRendicontazione = flussoRendicontazione;
  }

  /**
   * Identificativo della direzione interna all'ente creditore
   **/
  public StatisticaQuadraturaRendicontazione direzione(String direzione) {
    this.direzione = direzione;
    return this;
  }

  @JsonProperty("direzione")
  public String getDirezione() {
    return direzione;
  }
  public void setDirezione(String direzione) {
    this.direzione = direzione;
  }

  /**
   * Identificativo della divisione interna all'ente creditore
   **/
  public StatisticaQuadraturaRendicontazione divisione(String divisione) {
    this.divisione = divisione;
    return this;
  }

  @JsonProperty("divisione")
  public String getDivisione() {
    return divisione;
  }
  public void setDivisione(String divisione) {
    this.divisione = divisione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatisticaQuadraturaRendicontazione statisticaQuadraturaRendicontazione = (StatisticaQuadraturaRendicontazione) o;
    return Objects.equals(numeroRendicontazioni, statisticaQuadraturaRendicontazione.numeroRendicontazioni) &&
        Objects.equals(importo, statisticaQuadraturaRendicontazione.importo) &&
        Objects.equals(dettaglio, statisticaQuadraturaRendicontazione.dettaglio) &&
        Objects.equals(flussoRendicontazione, statisticaQuadraturaRendicontazione.flussoRendicontazione) &&
        Objects.equals(direzione, statisticaQuadraturaRendicontazione.direzione) &&
        Objects.equals(divisione, statisticaQuadraturaRendicontazione.divisione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numeroRendicontazioni, importo, dettaglio, flussoRendicontazione, direzione, divisione);
  }

  public static StatisticaQuadraturaRendicontazione parse(String json) throws ServiceException, ValidationException { 
    return (StatisticaQuadraturaRendicontazione) parse(json, StatisticaQuadraturaRendicontazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "statisticaQuadraturaRendicontazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatisticaQuadraturaRendicontazione {\n");
    
    sb.append("    numeroRendicontazioni: ").append(toIndentedString(numeroRendicontazioni)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dettaglio: ").append(toIndentedString(dettaglio)).append("\n");
    sb.append("    flussoRendicontazione: ").append(toIndentedString(flussoRendicontazione)).append("\n");
    sb.append("    direzione: ").append(toIndentedString(direzione)).append("\n");
    sb.append("    divisione: ").append(toIndentedString(divisione)).append("\n");
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



