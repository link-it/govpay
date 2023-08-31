package it.govpay.backoffice.v1.beans;


import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"indice",
"idVocePendenza",
"importo",
"descrizione",
"stato",
"descrizioneCausaleRPT",
"contabilita",
"dominio",
"pendenza",
"codiceContabilita",
"tipoContabilita",
})
public class VocePendenzaRendicontazione extends JSONSerializable {

  @JsonProperty("indice")
  private BigDecimal indice = null;

  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;

  @JsonProperty("importo")
  private BigDecimal importo = null;

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("stato")
  private StatoVocePendenza stato = null;

  @JsonProperty("descrizioneCausaleRPT")
  private String descrizioneCausaleRPT = null;

  @JsonProperty("contabilita")
  private Contabilita contabilita = null;

  @JsonProperty("dominio")
  private DominioIndex dominio = null;

  @JsonProperty("pendenza")
  private PendenzaIndex pendenza = null;
  
  @JsonProperty("codiceContabilita")
  private String codiceContabilita= null;
  
  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita= null;

  /**
   * indice di voce all'interno della pendenza
   **/
  public VocePendenzaRendicontazione indice(BigDecimal indice) {
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
  public VocePendenzaRendicontazione idVocePendenza(String idVocePendenza) {
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
  public VocePendenzaRendicontazione importo(BigDecimal importo) {
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
  public VocePendenzaRendicontazione descrizione(String descrizione) {
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
   **/
  public VocePendenzaRendicontazione stato(StatoVocePendenza stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoVocePendenza getStato() {
    return stato;
  }
  public void setStato(StatoVocePendenza stato) {
    this.stato = stato;
  }

  /**
   * Testo libero per la causale versamento
   **/
  public VocePendenzaRendicontazione descrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
    return this;
  }

  @JsonProperty("descrizioneCausaleRPT")
  public String getDescrizioneCausaleRPT() {
    return descrizioneCausaleRPT;
  }
  public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
  }

  /**
   **/
  public VocePendenzaRendicontazione contabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
    return this;
  }

  @JsonProperty("contabilita")
  public Contabilita getContabilita() {
    return contabilita;
  }
  public void setContabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
  }

  /**
   **/
  public VocePendenzaRendicontazione dominio(DominioIndex dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public DominioIndex getDominio() {
    return dominio;
  }
  public void setDominio(DominioIndex dominio) {
    this.dominio = dominio;
  }

  /**
   **/
  public VocePendenzaRendicontazione pendenza(PendenzaIndex pendenza) {
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
  
  public VocePendenzaRendicontazione codiceContabilita(String codiceContabilita) {
	    this.codiceContabilita= codiceContabilita;
	    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return this.codiceContabilita;
  }
  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }
  
  public VocePendenzaRendicontazione tipoContabilita(TipoContabilita tipoContabilita) {
	    this.tipoContabilita= tipoContabilita;
	    return this;
	  }
  
  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return this.tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VocePendenzaRendicontazione vocePendenzaRendicontazione = (VocePendenzaRendicontazione) o;
    return Objects.equals(indice, vocePendenzaRendicontazione.indice) &&
        Objects.equals(idVocePendenza, vocePendenzaRendicontazione.idVocePendenza) &&
        Objects.equals(importo, vocePendenzaRendicontazione.importo) &&
        Objects.equals(descrizione, vocePendenzaRendicontazione.descrizione) &&
        Objects.equals(stato, vocePendenzaRendicontazione.stato) &&
        Objects.equals(descrizioneCausaleRPT, vocePendenzaRendicontazione.descrizioneCausaleRPT) &&
        Objects.equals(contabilita, vocePendenzaRendicontazione.contabilita) &&
        Objects.equals(dominio, vocePendenzaRendicontazione.dominio) &&
        Objects.equals(pendenza, vocePendenzaRendicontazione.pendenza) &&
        Objects.equals(codiceContabilita, vocePendenzaRendicontazione.codiceContabilita) &&
        Objects.equals(tipoContabilita, vocePendenzaRendicontazione.tipoContabilita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indice, idVocePendenza, importo, descrizione, stato, descrizioneCausaleRPT, contabilita, dominio, pendenza, codiceContabilita, tipoContabilita);
  }

  public static VocePendenzaRendicontazione parse(String json) throws IOException {
    return parse(json, VocePendenzaRendicontazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "vocePendenzaRendicontazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenzaRendicontazione {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    contabilita: ").append(toIndentedString(contabilita)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
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



