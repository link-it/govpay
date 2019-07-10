package it.govpay.pagamento.v2.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"definizione",
})
public class TipoPendenzaForm extends JSONSerializable {
  
  @JsonProperty("tipo")
  private String tipo = null;
  
  @JsonProperty("definizione")
  private Object definizione = null;
  
  /**
   * Indica il linguaggio da utilizzare per il disegno della form di inserimento della pendenza
   **/
  public TipoPendenzaForm tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   * Definizione della form nel linguaggio indicato nel field tipo
   **/
  public TipoPendenzaForm definizione(Object definizione) {
    this.definizione = definizione;
    return this;
  }

  @JsonProperty("definizione")
  public Object getDefinizione() {
    return definizione;
  }
  public void setDefinizione(Object definizione) {
    this.definizione = definizione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaForm tipoPendenzaForm = (TipoPendenzaForm) o;
    return Objects.equals(tipo, tipoPendenzaForm.tipo) &&
        Objects.equals(definizione, tipoPendenzaForm.definizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, definizione);
  }

  public static TipoPendenzaForm parse(String json) throws ServiceException, ValidationException { 
    return (TipoPendenzaForm) parse(json, TipoPendenzaForm.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaForm";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaForm {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    definizione: ").append(toIndentedString(definizione)).append("\n");
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



