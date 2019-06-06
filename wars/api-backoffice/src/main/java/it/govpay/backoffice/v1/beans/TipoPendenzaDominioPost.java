package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@JsonPropertyOrder({
"codificaIUV",
"pagaTerzi",
"abilitato",
"schema",
"datiAllegati",
})
public class TipoPendenzaDominioPost extends JSONSerializable  implements IValidable {
  
  @JsonProperty("codificaIUV")
  private String codificaIUV = null;
  
  @JsonProperty("pagaTerzi")
  private Boolean pagaTerzi = false;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = true;
  
  @JsonProperty("schema")
  private Object schema = null;
  
  @JsonProperty("datiAllegati")
  private Object datiAllegati = null;
  
  /**
   * Cifra identificativa negli IUV
   **/
  public TipoPendenzaDominioPost codificaIUV(String codificaIUV) {
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
  public TipoPendenzaDominioPost pagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = it.govpay.rs.v1.beans.deserializer.BooleanDeserializer.class)
  public Boolean PagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(Boolean pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  /**
   * Indicazione la tipologia pendenza e' abilitata
   **/
  public TipoPendenzaDominioPost abilitato(Boolean abilitato) {
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
   * JSON Schema che descrive la struttura della tipologia di pendenza
   **/
  public TipoPendenzaDominioPost schema(Object schema) {
    this.schema = schema;
    return this;
  }

  @JsonProperty("schema")
  public Object getSchema() {
    return schema;
  }
  public void setSchema(Object schema) {
    this.schema = schema;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario per la gestione della tipologia della pendenza.
   **/
  public TipoPendenzaDominioPost datiAllegati(Object datiAllegati) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaDominioPost tipoPendenzaDominioPost = (TipoPendenzaDominioPost) o;
    return Objects.equals(codificaIUV, tipoPendenzaDominioPost.codificaIUV) &&
        Objects.equals(pagaTerzi, tipoPendenzaDominioPost.pagaTerzi) &&
        Objects.equals(abilitato, tipoPendenzaDominioPost.abilitato) &&
        Objects.equals(schema, tipoPendenzaDominioPost.schema) &&
        Objects.equals(datiAllegati, tipoPendenzaDominioPost.datiAllegati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codificaIUV, pagaTerzi, abilitato, schema, datiAllegati);
  }

  public static TipoPendenzaDominioPost parse(String json) throws ServiceException, ValidationException{
    return (TipoPendenzaDominioPost) parse(json, TipoPendenzaDominioPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaDominioPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaDominioPost {\n");
    
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
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
  
  @Override
  public void validate() throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	vf.getValidator("codificaIUV", this.codificaIUV).minLength(1).maxLength(4).pattern("(^[0-9]{1,4}$)");
//	vf.getValidator("pagaTerzi", this.pagaTerzi).notNull();
//	vf.getValidator("abilitato", this.abilitato).notNull();
  }
}



