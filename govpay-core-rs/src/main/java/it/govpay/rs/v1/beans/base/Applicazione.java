package it.govpay.rs.v1.beans.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"principal",
"codificaIUV",
"urlRitornoPortale",
"servizioVerifica",
"servizioNotifica",
"abilitato",
"ruoli",
"idDominio",
})
public class Applicazione extends it.govpay.rs.v1.beans.JSONSerializable {
  
  @JsonProperty("principal")
  private String principal = null;
  
  @JsonProperty("codificaIUV")
  private BigDecimal codificaIUV = null;
  
  @JsonProperty("urlRitornoPortale")
  private String urlRitornoPortale = null;
  
  @JsonProperty("servizioVerifica")
  private Connector servizioVerifica = null;
  
  @JsonProperty("servizioNotifica")
  private Connector servizioNotifica = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("ruoli")
  private List<String> ruoli = new ArrayList<String>();
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  /**
   * Identificativo di autenticazione
   **/
  public Applicazione principal(String principal) {
    this.principal = principal;
    return this;
  }

  @JsonProperty("principal")
  public String getPrincipal() {
    return principal;
  }
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   * Cifra identificativa negli IUV
   **/
  public Applicazione codificaIUV(BigDecimal codificaIUV) {
    this.codificaIUV = codificaIUV;
    return this;
  }

  @JsonProperty("codificaIUV")
  public BigDecimal getCodificaIUV() {
    return codificaIUV;
  }
  public void setCodificaIUV(BigDecimal codificaIUV) {
    this.codificaIUV = codificaIUV;
  }

  /**
   * Default url di ritorno dal pagamento
   **/
  public Applicazione urlRitornoPortale(String urlRitornoPortale) {
    this.urlRitornoPortale = urlRitornoPortale;
    return this;
  }

  @JsonProperty("urlRitornoPortale")
  public String getUrlRitornoPortale() {
    return urlRitornoPortale;
  }
  public void setUrlRitornoPortale(String urlRitornoPortale) {
    this.urlRitornoPortale = urlRitornoPortale;
  }

  /**
   **/
  public Applicazione servizioVerifica(Connector servizioVerifica) {
    this.servizioVerifica = servizioVerifica;
    return this;
  }

  @JsonProperty("servizioVerifica")
  public Connector getServizioVerifica() {
    return servizioVerifica;
  }
  public void setServizioVerifica(Connector servizioVerifica) {
    this.servizioVerifica = servizioVerifica;
  }

  /**
   **/
  public Applicazione servizioNotifica(Connector servizioNotifica) {
    this.servizioNotifica = servizioNotifica;
    return this;
  }

  @JsonProperty("servizioNotifica")
  public Connector getServizioNotifica() {
    return servizioNotifica;
  }
  public void setServizioNotifica(Connector servizioNotifica) {
    this.servizioNotifica = servizioNotifica;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public Applicazione abilitato(Boolean abilitato) {
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

  /**
   * ruoli associati all'utenza applicativa
   **/
  public Applicazione ruoli(List<String> ruoli) {
    this.ruoli = ruoli;
    return this;
  }

  @JsonProperty("ruoli")
  public List<String> getRuoli() {
    return ruoli;
  }
  public void setRuoli(List<String> ruoli) {
    this.ruoli = ruoli;
  }

  /**
   * Identificativo dell'applicazione
   **/
  public Applicazione idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Applicazione applicazione = (Applicazione) o;
    return Objects.equals(principal, applicazione.principal) &&
        Objects.equals(codificaIUV, applicazione.codificaIUV) &&
        Objects.equals(urlRitornoPortale, applicazione.urlRitornoPortale) &&
        Objects.equals(servizioVerifica, applicazione.servizioVerifica) &&
        Objects.equals(servizioNotifica, applicazione.servizioNotifica) &&
        Objects.equals(abilitato, applicazione.abilitato) &&
        Objects.equals(ruoli, applicazione.ruoli) &&
        Objects.equals(idDominio, applicazione.idDominio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal, codificaIUV, urlRitornoPortale, servizioVerifica, servizioNotifica, abilitato, ruoli, idDominio);
  }

  public static Applicazione parse(String json) {
    return (Applicazione) parse(json, Applicazione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "applicazione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Applicazione {\n");
    
    sb.append("    principal: ").append(toIndentedString(principal)).append("\n");
    sb.append("    codificaIUV: ").append(toIndentedString(codificaIUV)).append("\n");
    sb.append("    urlRitornoPortale: ").append(toIndentedString(urlRitornoPortale)).append("\n");
    sb.append("    servizioVerifica: ").append(toIndentedString(servizioVerifica)).append("\n");
    sb.append("    servizioNotifica: ").append(toIndentedString(servizioNotifica)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    ruoli: ").append(toIndentedString(ruoli)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
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



