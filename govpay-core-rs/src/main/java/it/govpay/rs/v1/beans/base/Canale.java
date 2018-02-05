package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.ModelloPagamento;
import it.govpay.rs.v1.beans.base.TipoVersamento;

/**
 * Canale di pagamento.
 **/@org.codehaus.jackson.annotate.JsonPropertyOrder({
"idCanale",
"tipoVersamento",
"modelloPagamento",
"psp",
"abilitato",
})
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaInflectorServerCodegen", date = "2018-02-05T15:39:23.431+01:00")
public abstract class Canale extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("idCanale")
  private String idCanale = null;
  
  @JsonProperty("tipoVersamento")
  private TipoVersamento tipoVersamento = null;
  
  @JsonProperty("modelloPagamento")
  private ModelloPagamento modelloPagamento = null;
  
  @JsonProperty("psp")
  private String psp = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * identificativo del PSP
   **/
  public Canale idCanale(String idCanale) {
    this.idCanale = idCanale;
    return this;
  }

  @JsonProperty("idCanale")
  public String getIdCanale() {
    return idCanale;
  }
  public void setIdCanale(String idCanale) {
    this.idCanale = idCanale;
  }

  /**
   **/
  public Canale tipoVersamento(TipoVersamento tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
    return this;
  }

  @JsonProperty("tipoVersamento")
  public TipoVersamento getTipoVersamento() {
    return tipoVersamento;
  }
  public void setTipoVersamento(TipoVersamento tipoVersamento) {
    this.tipoVersamento = tipoVersamento;
  }

  /**
   **/
  public Canale modelloPagamento(ModelloPagamento modelloPagamento) {
    this.modelloPagamento = modelloPagamento;
    return this;
  }

  @JsonProperty("modelloPagamento")
  public ModelloPagamento getModelloPagamento() {
    return modelloPagamento;
  }
  public void setModelloPagamento(ModelloPagamento modelloPagamento) {
    this.modelloPagamento = modelloPagamento;
  }

  /**
   * Url per acquisire il dettaglio del PSP
   **/
  public Canale psp(String psp) {
    this.psp = psp;
    return this;
  }

  @JsonProperty("psp")
  public String getPsp() {
    return psp;
  }
  public void setPsp(String psp) {
    this.psp = psp;
  }

  /**
   **/
  public Canale abilitato(Boolean abilitato) {
    this.abilitato = abilitato;
    return this;
  }

  @JsonProperty("abilitato")
  public Boolean isAbilitato() {
    return abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Canale canale = (Canale) o;
    return Objects.equals(idCanale, canale.idCanale) &&
        Objects.equals(tipoVersamento, canale.tipoVersamento) &&
        Objects.equals(modelloPagamento, canale.modelloPagamento) &&
        Objects.equals(psp, canale.psp) &&
        Objects.equals(abilitato, canale.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idCanale, tipoVersamento, modelloPagamento, psp, abilitato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Canale {\n");
    
    sb.append("    idCanale: ").append(toIndentedString(idCanale)).append("\n");
    sb.append("    tipoVersamento: ").append(toIndentedString(tipoVersamento)).append("\n");
    sb.append("    modelloPagamento: ").append(toIndentedString(modelloPagamento)).append("\n");
    sb.append("    psp: ").append(toIndentedString(psp)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
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



