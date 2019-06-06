package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import it.govpay.core.beans.JSONSerializable;
@JsonPropertyOrder({
"descrizione",
"tipo",
"codificaIUV",
"pagaTerzi",
"abilitato",
"schema",
"datiAllegati",
"idTipoPendenza",
})
public class TipoPendenza extends JSONSerializable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
    
  /**
   * Gets or Sets tipo
   */
  public enum TipoEnum {
    
    
        
            
    SPONTANEA("spontanea"),
    
            
    DOVUTA("dovuta");
            
        
    

    private String value;

    TipoEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TipoEnum fromValue(String text) {
      for (TipoEnum b : TipoEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    
    
  @JsonProperty("tipo")
  private TipoEnum tipo = null;
  
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
  public TipoPendenza tipo(TipoEnum tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public TipoEnum getTipo() {
    return tipo;
  }
  public void setTipo(TipoEnum tipo) {
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
   * JSON Schema che descrive la struttura della tipologia di pendenza
   **/
  public TipoPendenza schema(Object schema) {
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
  public TipoPendenza datiAllegati(Object datiAllegati) {
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
        Objects.equals(schema, tipoPendenza.schema) &&
        Objects.equals(datiAllegati, tipoPendenza.datiAllegati) &&
        Objects.equals(idTipoPendenza, tipoPendenza.idTipoPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, tipo, codificaIUV, pagaTerzi, abilitato, schema, datiAllegati, idTipoPendenza);
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
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
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



