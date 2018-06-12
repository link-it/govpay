package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"percentuale",
"descrizione",
"stato",
})
public class EsitoOperazione extends JSONSerializable {
  
  @JsonProperty("percentuale")
  private BigDecimal percentuale = null;
  
  @JsonProperty("descrizione")
  private List<DominioIndex> descrizione = new ArrayList<DominioIndex>();
  
  @JsonProperty("stato")
  private String stato = null;
  
  /**
   * Percentuale di avanzamento
   **/
  public EsitoOperazione percentuale(BigDecimal percentuale) {
    this.percentuale = percentuale;
    return this;
  }

  @JsonProperty("percentuale")
  public BigDecimal getPercentuale() {
    return percentuale;
  }
  public void setPercentuale(BigDecimal percentuale) {
    this.percentuale = percentuale;
  }

  /**
   * testo descrittivo
   **/
  public EsitoOperazione descrizione(List<DominioIndex> descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public List<DominioIndex> getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(List<DominioIndex> descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * stato dell'operazione (OK/KO/WARN)
   **/
  public EsitoOperazione stato(String stato) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoOperazione esitoOperazione = (EsitoOperazione) o;
    return Objects.equals(percentuale, esitoOperazione.percentuale) &&
        Objects.equals(descrizione, esitoOperazione.descrizione) &&
        Objects.equals(stato, esitoOperazione.stato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(percentuale, descrizione, stato);
  }

  public static EsitoOperazione parse(String json) {
    return (EsitoOperazione) parse(json, EsitoOperazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "esitoOperazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoOperazione {\n");
    
    sb.append("    percentuale: ").append(toIndentedString(percentuale)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
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



