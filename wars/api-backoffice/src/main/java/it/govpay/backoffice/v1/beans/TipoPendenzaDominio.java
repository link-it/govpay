package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import it.govpay.core.beans.JSONSerializable;

@JsonPropertyOrder({
"tipo",
"codificaIUV",
"pagaTerzi",
"idTipoPendenza",
"tipoPendenza",
})
public class TipoPendenzaDominio extends JSONSerializable {
  
    
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
  
  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;
  
  @JsonProperty("tipoPendenza")
  private TipoPendenza tipoPendenza = null;
  
  /**
   **/
  public TipoPendenzaDominio tipo(TipoEnum tipo) {
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
  public TipoPendenzaDominio codificaIUV(String codificaIUV) {
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
  public TipoPendenzaDominio pagaTerzi(Boolean pagaTerzi) {
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
   **/
  public TipoPendenzaDominio idTipoPendenza(String idTipoPendenza) {
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
  public TipoPendenzaDominio tipoPendenza(TipoPendenza tipoPendenza) {
    this.tipoPendenza = tipoPendenza;
    return this;
  }

  @JsonProperty("tipoPendenza")
  public TipoPendenza getTipoPendenza() {
    return tipoPendenza;
  }
  public void setTipoPendenza(TipoPendenza tipoPendenza) {
    this.tipoPendenza = tipoPendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaDominio tipoPendenzaDominio = (TipoPendenzaDominio) o;
    return Objects.equals(tipo, tipoPendenzaDominio.tipo) &&
        Objects.equals(codificaIUV, tipoPendenzaDominio.codificaIUV) &&
        Objects.equals(pagaTerzi, tipoPendenzaDominio.pagaTerzi) &&
        Objects.equals(idTipoPendenza, tipoPendenzaDominio.idTipoPendenza) &&
        Objects.equals(tipoPendenza, tipoPendenzaDominio.tipoPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, codificaIUV, pagaTerzi, idTipoPendenza, tipoPendenza);
  }

  public static TipoPendenzaDominio parse(String json) throws ServiceException, ValidationException{
    return (TipoPendenzaDominio) parse(json, TipoPendenzaDominio.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaDominio";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaDominio {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    tipoPendenza: ").append(toIndentedString(tipoPendenza)).append("\n");
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



