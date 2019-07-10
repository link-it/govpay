package it.govpay.pagamento.v2.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
@JsonPropertyOrder({
"descrizione",
"tipo",
"codificaIUV",
"pagaTerzi",
"abilitato",
"form",
"idTipoPendenza",
})
public class TipoPendenza extends JSONSerializable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("tipo")
  private TipoPendenzaTipologia tipo = null;
  
  @JsonProperty("codificaIUV")
  private String codificaIUV = null;
  
  @JsonProperty("pagaTerzi")
  private Boolean pagaTerzi = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  @JsonProperty("form")
  private TipoPendenzaForm form = null;
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  /**
   **/
  public TipoPendenza descrizione(String descrizione) {
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
  public TipoPendenza tipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoPendenzaTipologia getTipo() {
    return tipo;
  }
  public void setTipo(TipoPendenzaTipologia tipo) {
    this.tipo = tipo;
  }

  /**
   * Cifra identificativa negli IUV
   **/
  public TipoPendenza codificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public String getCodificaIUV() {
    return codificaIUV;
  }
  public void setCodificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   * Indica se la pendenza e' pagabile da soggetti terzi
   **/
  public TipoPendenza pagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  public Boolean PagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  /**
   * Indicazione la tipologia pendenza e' abilitata
   **/
  public TipoPendenza abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean Abilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   **/
  public TipoPendenza form(TipoPendenzaForm form) {
    this.form = form;
    return this;
  }

  @JsonProperty("form")
  public TipoPendenzaForm getForm() {
    return form;
  }
  public void setForm(TipoPendenzaForm form) {
    this.form = form;
  }

  /**
   **/
  public TipoPendenza idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenza tipoPendenza = (TipoPendenza) o;
    return Objects.equals(descrizione, tipoPendenza.descrizione) &&
        Objects.equals(tipo, tipoPendenza.tipo) &&
        Objects.equals(codificaIUV, tipoPendenza.codificaIUV) &&
        Objects.equals(pagaTerzi, tipoPendenza.pagaTerzi) &&
        Objects.equals(abilitato, tipoPendenza.abilitato) &&
        Objects.equals(form, tipoPendenza.form) &&
        Objects.equals(idTipoPendenza, tipoPendenza.idTipoPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, tipo, codificaIUV, pagaTerzi, abilitato, form, idTipoPendenza);
  }

  public static TipoPendenza parse(String json) throws ServiceException, ValidationException  {
    return (TipoPendenza) parse(json, TipoPendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenza {\n");
    
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    form: ").append(toIndentedString(form)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
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



