package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"descrizione",
"tipoContabilita",
"codiceContabilita",
"codificaIUV",
"online",
"pagaTerzi",
})
public class TipoEntrataPost extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita = null;
  
  @JsonProperty("codiceContabilita")
  private String codiceContabilita = null;
  
  @JsonProperty("codificaIUV")
  private String codificaIUV = null;
  
  @JsonProperty("online")
  private TipoTributoOnline online = null;
  
  @JsonProperty("pagaTerzi")
  private TipoTributoPagaTerzi pagaTerzi = null;
  
  /**
   **/
  public TipoEntrataPost descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return this.descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Tipologia di codifica del capitolo di bilancio
   **/
  public TipoEntrataPost tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
    return this;
  }

  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return this.tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  /**
   * Codifica del capitolo di bilancio
   **/
  public TipoEntrataPost codiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return this.codiceContabilita;
  }
  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  /**
   * Cifra identificativa negli IUV
   **/
  public TipoEntrataPost codificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public String getCodificaIUV() {
    return this.codificaIUV;
  }
  public void setCodificaIUV(String codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   **/
  public TipoEntrataPost online(TipoTributoOnline online) {
    this.online = online;
    return this;
  }

  @JsonProperty("online")
  public TipoTributoOnline getOnline() {
    return online;
  }
  public void setOnline(TipoTributoOnline online) {
    this.online = online;
  }

  /**
   **/
  public TipoEntrataPost pagaTerzi(TipoTributoPagaTerzi pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
    return this;
  }

  @JsonProperty("pagaTerzi")
  public TipoTributoPagaTerzi getPagaTerzi() {
    return pagaTerzi;
  }
  public void setPagaTerzi(TipoTributoPagaTerzi pagaTerzi) {
    this.pagaTerzi = pagaTerzi;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    TipoEntrataPost tipoEntrataPost = (TipoEntrataPost) o;
    return Objects.equals(this.descrizione, tipoEntrataPost.descrizione) &&
        Objects.equals(this.tipoContabilita, tipoEntrataPost.tipoContabilita) &&
        Objects.equals(this.codiceContabilita, tipoEntrataPost.codiceContabilita) &&
        Objects.equals(this.codificaIUV, tipoEntrataPost.codificaIUV) &&
        Objects.equals(online, tipoEntrataPost.online) &&
	Objects.equals(pagaTerzi, tipoEntrataPost.pagaTerzi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.descrizione, this.tipoContabilita, this.codiceContabilita, this.codificaIUV, online, pagaTerzi);
  }

  public static TipoEntrataPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, TipoEntrataPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoEntrataPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoEntrataPost {\n");
    
    sb.append("    descrizione: ").append(this.toIndentedString(this.descrizione)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
    sb.append("    codificaIUV: ").append(this.toIndentedString(this.codificaIUV)).append("\n");
    sb.append("    online: ").append(toIndentedString(online)).append("\n");
    sb.append("    pagaTerzi: ").append(toIndentedString(pagaTerzi)).append("\n");
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



