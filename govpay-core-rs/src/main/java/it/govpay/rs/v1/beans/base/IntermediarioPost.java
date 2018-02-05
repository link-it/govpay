package it.govpay.rs.v1.beans.base;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import it.govpay.rs.v1.beans.base.Connector;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"denominazione",
"pagopaPrincipal",
"pagopaConnector",
"abilitato",
})
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaInflectorServerCodegen", date = "2018-02-05T15:39:23.431+01:00")
public abstract class IntermediarioPost extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("denominazione")
  private String denominazione = null;
  
  @JsonProperty("pagopaPrincipal")
  private String pagopaPrincipal = null;
  
  @JsonProperty("pagopaConnector")
  private Connector pagopaConnector = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  /**
   * Ragione sociale dell'intermediario PagoPA
   **/
  public IntermediarioPost denominazione(String denominazione) {
    this.denominazione = denominazione;
    return this;
  }

  @JsonProperty("denominazione")
  public String getDenominazione() {
    return denominazione;
  }
  public void setDenominazione(String denominazione) {
    this.denominazione = denominazione;
  }

  /**
   * Principal autenticato le richieste ricevute da PagoPA
   **/
  public IntermediarioPost pagopaPrincipal(String pagopaPrincipal) {
    this.pagopaPrincipal = pagopaPrincipal;
    return this;
  }

  @JsonProperty("pagopaPrincipal")
  public String getPagopaPrincipal() {
    return pagopaPrincipal;
  }
  public void setPagopaPrincipal(String pagopaPrincipal) {
    this.pagopaPrincipal = pagopaPrincipal;
  }

  /**
   **/
  public IntermediarioPost pagopaConnector(Connector pagopaConnector) {
    this.pagopaConnector = pagopaConnector;
    return this;
  }

  @JsonProperty("pagopaConnector")
  public Connector getPagopaConnector() {
    return pagopaConnector;
  }
  public void setPagopaConnector(Connector pagopaConnector) {
    this.pagopaConnector = pagopaConnector;
  }

  /**
   * Indica lo stato di abilitazione
   **/
  public IntermediarioPost abilitato(Boolean abilitato) {
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
    IntermediarioPost intermediarioPost = (IntermediarioPost) o;
    return Objects.equals(denominazione, intermediarioPost.denominazione) &&
        Objects.equals(pagopaPrincipal, intermediarioPost.pagopaPrincipal) &&
        Objects.equals(pagopaConnector, intermediarioPost.pagopaConnector) &&
        Objects.equals(abilitato, intermediarioPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(denominazione, pagopaPrincipal, pagopaConnector, abilitato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntermediarioPost {\n");
    
    sb.append("    denominazione: ").append(toIndentedString(denominazione)).append("\n");
    sb.append("    pagopaPrincipal: ").append(toIndentedString(pagopaPrincipal)).append("\n");
    sb.append("    pagopaConnector: ").append(toIndentedString(pagopaConnector)).append("\n");
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



