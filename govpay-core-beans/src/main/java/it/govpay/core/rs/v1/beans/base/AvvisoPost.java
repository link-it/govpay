package it.govpay.core.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idPendenza",
"importo",
"dataValidita",
"dataScadenza",
"descrizione",
"tassonomiaAvviso",
"tassonomia",
"pagatore",
})
public class AvvisoPost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idPendenza")
  private String idPendenza = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  @JsonProperty("dataValidita")
  private Date dataValidita = null;
  
  @JsonProperty("dataScadenza")
  private Date dataScadenza = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("tassonomiaAvviso")
  private String tassonomiaAvviso = null;
  
  @JsonProperty("tassonomia")
  private String tassonomia = null;
  
  @JsonProperty("pagatore")
  private Object pagatore = null;
  
  /**
   * Identificativo della pendenza nel gestionale chiamante
   **/
  public AvvisoPost idPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
    return this;
  }

  @JsonProperty("idPendenza")
  public String getIdPendenza() {
    return idPendenza;
  }
  public void setIdPendenza(String idPendenza) {
    this.idPendenza = idPendenza;
  }

  /**
   * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
   **/
  public AvvisoPost importo(BigDecimal importo) {
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
   * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
   **/
  public AvvisoPost dataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
    return this;
  }

  @JsonProperty("dataValidita")
  public Date getDataValidita() {
    return dataValidita;
  }
  public void setDataValidita(Date dataValidita) {
    this.dataValidita = dataValidita;
  }

  /**
   * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
   **/
  public AvvisoPost dataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
    return this;
  }

  @JsonProperty("dataScadenza")
  public Date getDataScadenza() {
    return dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   * Descrizione da inserire nell'avviso di pagamento
   **/
  public AvvisoPost descrizione(String descrizione) {
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
   * Macro categoria della pendenza secondo la classificazione AgID
   **/
  public AvvisoPost tassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
    return this;
  }

  @JsonProperty("tassonomiaAvviso")
  public String getTassonomiaAvviso() {
    return tassonomiaAvviso;
  }
  public void setTassonomiaAvviso(String tassonomiaAvviso) {
    this.tassonomiaAvviso = tassonomiaAvviso;
  }

  /**
   * Macro categoria della pendenza secondo la classificazione del creditore
   **/
  public AvvisoPost tassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
    return this;
  }

  @JsonProperty("tassonomia")
  public String getTassonomia() {
    return tassonomia;
  }
  public void setTassonomia(String tassonomia) {
    this.tassonomia = tassonomia;
  }

  /**
   * dati anagrafici del pagatore.
   **/
  public AvvisoPost pagatore(Object pagatore) {
    this.pagatore = pagatore;
    return this;
  }

  @JsonProperty("pagatore")
  public Object getPagatore() {
    return pagatore;
  }
  public void setPagatore(Object pagatore) {
    this.pagatore = pagatore;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvvisoPost avvisoPost = (AvvisoPost) o;
    return Objects.equals(idPendenza, avvisoPost.idPendenza) &&
        Objects.equals(importo, avvisoPost.importo) &&
        Objects.equals(dataValidita, avvisoPost.dataValidita) &&
        Objects.equals(dataScadenza, avvisoPost.dataScadenza) &&
        Objects.equals(descrizione, avvisoPost.descrizione) &&
        Objects.equals(tassonomiaAvviso, avvisoPost.tassonomiaAvviso) &&
        Objects.equals(tassonomia, avvisoPost.tassonomia) &&
        Objects.equals(pagatore, avvisoPost.pagatore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPendenza, importo, dataValidita, dataScadenza, descrizione, tassonomiaAvviso, tassonomia, pagatore);
  }

  public static AvvisoPost parse(String json) {
    return (AvvisoPost) parse(json, AvvisoPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "avvisoPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvvisoPost {\n");
    
    sb.append("    idPendenza: ").append(toIndentedString(idPendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    dataValidita: ").append(toIndentedString(dataValidita)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tassonomiaAvviso: ").append(toIndentedString(tassonomiaAvviso)).append("\n");
    sb.append("    tassonomia: ").append(toIndentedString(tassonomia)).append("\n");
    sb.append("    pagatore: ").append(toIndentedString(pagatore)).append("\n");
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



