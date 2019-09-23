package it.govpay.ragioneria.v2.beans;

import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"indice",
"idVocePendenza",
"importo",
"descrizione",
"datiAllegati",
"pendenza",
})
public class VocePendenza extends JSONSerializable {
  
  @JsonProperty("indice")
  private BigDecimal indice = null;
  
  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("datiAllegati")
  private Object datiAllegati = null;
  
  @JsonProperty("pendenza")
  private Pendenza pendenza = null;
  
  /**
   * indice di voce all'interno della pendenza
   **/
  public VocePendenza indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   **/
  public VocePendenza idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return idVocePendenza;
  }
  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  /**
   * Importo della voce
   **/
  public VocePendenza importo(BigDecimal importo) {
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
   * descrizione della voce di pagamento
   **/
  public VocePendenza descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public VocePendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

  @JsonProperty("datiAllegati")
  public Object getDatiAllegati() {
    return datiAllegati;
  }
  public void setDatiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  /**
   **/
  public VocePendenza pendenza(Pendenza pendenza) {
    this.pendenza = pendenza;
    return this;
  }

  @JsonProperty("pendenza")
  public Pendenza getPendenza() {
    return pendenza;
  }
  public void setPendenza(Pendenza pendenza) {
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
    VocePendenza vocePendenza = (VocePendenza) o;
    return Objects.equals(indice, vocePendenza.indice) &&
        Objects.equals(idVocePendenza, vocePendenza.idVocePendenza) &&
        Objects.equals(importo, vocePendenza.importo) &&
        Objects.equals(descrizione, vocePendenza.descrizione) &&
        Objects.equals(datiAllegati, vocePendenza.datiAllegati) &&
        Objects.equals(pendenza, vocePendenza.pendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indice, idVocePendenza, importo, descrizione, datiAllegati, pendenza);
  }

  public static VocePendenza parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, VocePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "vocePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenza {\n");
    
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
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



