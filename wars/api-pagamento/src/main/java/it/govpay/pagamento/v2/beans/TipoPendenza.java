package it.govpay.pagamento.v2.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
@JsonPropertyOrder({
"idTipoPendenza",
"descrizione",
"tipo",
"form",
})
public class TipoPendenza extends JSONSerializable {
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("tipo")
  private TipoPendenzaTipologia tipo = null;
  
  @JsonProperty("form")
  private TipoPendenzaForm form = null;
  
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenza tipoPendenza = (TipoPendenza) o;
    return Objects.equals(idTipoPendenza, tipoPendenza.idTipoPendenza) &&
        Objects.equals(descrizione, tipoPendenza.descrizione) &&
        Objects.equals(tipo, tipoPendenza.tipo) &&
        Objects.equals(form, tipoPendenza.form);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTipoPendenza, descrizione, tipo, form);
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
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    form: ").append(toIndentedString(form)).append("\n");
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



