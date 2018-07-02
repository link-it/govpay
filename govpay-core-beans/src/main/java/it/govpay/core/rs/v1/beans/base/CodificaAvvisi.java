package it.govpay.core.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * informazioni sulla codifica e decodifica degli iuv
 **/@org.codehaus.jackson.annotate.JsonPropertyOrder({
"codificaIuv",
"regExpIuv",
"generazioneIuvInterna",
})
public class CodificaAvvisi extends it.govpay.core.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("codificaIuv")
  private String codificaIuv = null;
  
  @JsonProperty("regExpIuv")
  private String regExpIuv = null;
  
  @JsonProperty("generazioneIuvInterna")
  private Boolean generazioneIuvInterna = null;
  
  /**
   * Cifra identificativa negli IUV. Deve essere un codice numerico.
   **/
  public CodificaAvvisi codificaIuv(String codificaIuv) {
    this.codificaIuv = codificaIuv;
    return this;
  }

  @JsonProperty("codificaIuv")
  public String getCodificaIuv() {
    return codificaIuv;
  }
  public void setCodificaIuv(String codificaIuv) {
    this.codificaIuv = codificaIuv;
  }

  /**
   * Espressione regolare di verifica del numero avviso
   **/
  public CodificaAvvisi regExpIuv(String regExpIuv) {
    this.regExpIuv = regExpIuv;
    return this;
  }

  @JsonProperty("regExpIuv")
  public String getRegExpIuv() {
    return regExpIuv;
  }
  public void setRegExpIuv(String regExpIuv) {
    this.regExpIuv = regExpIuv;
  }

  /**
   * Indicazione se l'applicazione genera in autonomia gli iuv
   **/
  public CodificaAvvisi generazioneIuvInterna(Boolean generazioneIuvInterna) {
    this.generazioneIuvInterna = generazioneIuvInterna;
    return this;
  }

  @JsonProperty("generazioneIuvInterna")
  public Boolean isGenerazioneIuvInterna() {
    return generazioneIuvInterna;
  }
  public void setGenerazioneIuvInterna(Boolean generazioneIuvInterna) {
    this.generazioneIuvInterna = generazioneIuvInterna;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CodificaAvvisi codificaAvvisi = (CodificaAvvisi) o;
    return Objects.equals(codificaIuv, codificaAvvisi.codificaIuv) &&
        Objects.equals(regExpIuv, codificaAvvisi.regExpIuv) &&
        Objects.equals(generazioneIuvInterna, codificaAvvisi.generazioneIuvInterna);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codificaIuv, regExpIuv, generazioneIuvInterna);
  }

  public static CodificaAvvisi parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (CodificaAvvisi) parse(json, CodificaAvvisi.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "codificaAvvisi";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CodificaAvvisi {\n");
    
    sb.append("    codificaIuv: ").append(toIndentedString(codificaIuv)).append("\n");
    sb.append("    regExpIuv: ").append(toIndentedString(regExpIuv)).append("\n");
    sb.append("    generazioneIuvInterna: ").append(toIndentedString(generazioneIuvInterna)).append("\n");
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



