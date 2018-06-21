package it.govpay.core.rs.v1.beans.ragioneria;

import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@org.codehaus.jackson.annotate.JsonPropertyOrder({
"ragioneSociale",
"indirizzo",
"civico",
"cap",
"localita",
"provincia",
"nazione",
"email",
"pec",
"tel",
"fax",
"gln",
"web",
"area",
"abilitato",
"idUnita",
})
public class UnitaOperativa extends JSONSerializable {
  
  @JsonProperty("ragioneSociale")
  private String ragioneSociale = null;
  
  @JsonProperty("indirizzo")
  private String indirizzo = null;
  
  @JsonProperty("civico")
  private String civico = null;
  
  @JsonProperty("cap")
  private String cap = null;
  
  @JsonProperty("localita")
  private String localita = null;
  
  @JsonProperty("provincia")
  private String provincia = null;
  
  @JsonProperty("nazione")
  private String nazione = null;
  
  @JsonProperty("email")
  private String email = null;
  
  @JsonProperty("pec")
  private String pec = null;
  
  @JsonProperty("tel")
  private String tel = null;
  
  @JsonProperty("fax")
  private String fax = null;
  
  @JsonProperty("gln")
  private String gln = null;
  
  @JsonProperty("web")
  private String web = null;
  
  @JsonProperty("area")
  private String area = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("idUnita")
  private String idUnita = null;
  
  /**
   * Ragione sociale dell'unita
   **/
  public UnitaOperativa ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }

  @JsonProperty("ragioneSociale")
  public String getRagioneSociale() {
    return ragioneSociale;
  }
  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  /**
   * Indirizzo dell'unita
   **/
  public UnitaOperativa indirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
    return this;
  }

  @JsonProperty("indirizzo")
  public String getIndirizzo() {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
  }

  /**
   * Numero civico dell'unita
   **/
  public UnitaOperativa civico(String civico) {
    this.civico = civico;
    return this;
  }

  @JsonProperty("civico")
  public String getCivico() {
    return civico;
  }
  public void setCivico(String civico) {
    this.civico = civico;
  }

  /**
   * Codice avviamento postale dell'unita
   **/
  public UnitaOperativa cap(String cap) {
    this.cap = cap;
    return this;
  }

  @JsonProperty("cap")
  public String getCap() {
    return cap;
  }
  public void setCap(String cap) {
    this.cap = cap;
  }

  /**
   * Località dell'unita
   **/
  public UnitaOperativa localita(String localita) {
    this.localita = localita;
    return this;
  }

  @JsonProperty("localita")
  public String getLocalita() {
    return localita;
  }
  public void setLocalita(String localita) {
    this.localita = localita;
  }

  /**
   * Provincia del beneficiario
   **/
  public UnitaOperativa provincia(String provincia) {
    this.provincia = provincia;
    return this;
  }

  @JsonProperty("provincia")
  public String getProvincia() {
    return provincia;
  }
  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }

  /**
   * Nazione del beneficiario
   **/
  public UnitaOperativa nazione(String nazione) {
    this.nazione = nazione;
    return this;
  }

  @JsonProperty("nazione")
  public String getNazione() {
    return nazione;
  }
  public void setNazione(String nazione) {
    this.nazione = nazione;
  }

  /**
   * Posta elettronica ordinaria del beneficiario
   **/
  public UnitaOperativa email(String email) {
    this.email = email;
    return this;
  }

  @JsonProperty("email")
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Posta elettronica certificata del beneficiario
   **/
  public UnitaOperativa pec(String pec) {
    this.pec = pec;
    return this;
  }

  @JsonProperty("pec")
  public String getPec() {
    return pec;
  }
  public void setPec(String pec) {
    this.pec = pec;
  }

  /**
   * Numero di telefono dell'help desk del beneficiario
   **/
  public UnitaOperativa tel(String tel) {
    this.tel = tel;
    return this;
  }

  @JsonProperty("tel")
  public String getTel() {
    return tel;
  }
  public void setTel(String tel) {
    this.tel = tel;
  }

  /**
   * Numero di fax dell'help desk del beneficiario
   **/
  public UnitaOperativa fax(String fax) {
    this.fax = fax;
    return this;
  }

  @JsonProperty("fax")
  public String getFax() {
    return fax;
  }
  public void setFax(String fax) {
    this.fax = fax;
  }

  /**
   * Global location number del beneficiario
   **/
  public UnitaOperativa gln(String gln) {
    this.gln = gln;
    return this;
  }

  @JsonProperty("gln")
  public String getGln() {
    return gln;
  }
  public void setGln(String gln) {
    this.gln = gln;
  }

  /**
   * Url del sito web
   **/
  public UnitaOperativa web(String web) {
    this.web = web;
    return this;
  }

  @JsonProperty("web")
  public String getWeb() {
    return web;
  }
  public void setWeb(String web) {
    this.web = web;
  }

  /**
   * Nome dell'area di competenza dell'unita' operativa
   **/
  public UnitaOperativa area(String area) {
    this.area = area;
    return this;
  }

  @JsonProperty("area")
  public String getArea() {
    return area;
  }
  public void setArea(String area) {
    this.area = area;
  }

  /**
   * Indica lo stato di abilitazione
   **/
  public UnitaOperativa abilitato(Boolean abilitato) {
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
   * Codice fiscale dell'unita
   **/
  public UnitaOperativa idUnita(String idUnita) {
    this.idUnita = idUnita;
    return this;
  }

  @JsonProperty("idUnita")
  public String getIdUnita() {
    return idUnita;
  }
  public void setIdUnita(String idUnita) {
    this.idUnita = idUnita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnitaOperativa unitaOperativa = (UnitaOperativa) o;
    return Objects.equals(ragioneSociale, unitaOperativa.ragioneSociale) &&
        Objects.equals(indirizzo, unitaOperativa.indirizzo) &&
        Objects.equals(civico, unitaOperativa.civico) &&
        Objects.equals(cap, unitaOperativa.cap) &&
        Objects.equals(localita, unitaOperativa.localita) &&
        Objects.equals(provincia, unitaOperativa.provincia) &&
        Objects.equals(nazione, unitaOperativa.nazione) &&
        Objects.equals(email, unitaOperativa.email) &&
        Objects.equals(pec, unitaOperativa.pec) &&
        Objects.equals(tel, unitaOperativa.tel) &&
        Objects.equals(fax, unitaOperativa.fax) &&
        Objects.equals(gln, unitaOperativa.gln) &&
        Objects.equals(web, unitaOperativa.web) &&
        Objects.equals(area, unitaOperativa.area) &&
        Objects.equals(abilitato, unitaOperativa.abilitato) &&
        Objects.equals(idUnita, unitaOperativa.idUnita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ragioneSociale, indirizzo, civico, cap, localita, provincia, nazione, email, pec, tel, fax, gln, web, area, abilitato, idUnita);
  }

  public static UnitaOperativa parse(String json) {
    return (UnitaOperativa) parse(json, UnitaOperativa.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "unitaOperativa";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnitaOperativa {\n");
    
    sb.append("    ragioneSociale: ").append(toIndentedString(ragioneSociale)).append("\n");
    sb.append("    indirizzo: ").append(toIndentedString(indirizzo)).append("\n");
    sb.append("    civico: ").append(toIndentedString(civico)).append("\n");
    sb.append("    cap: ").append(toIndentedString(cap)).append("\n");
    sb.append("    localita: ").append(toIndentedString(localita)).append("\n");
    sb.append("    provincia: ").append(toIndentedString(provincia)).append("\n");
    sb.append("    nazione: ").append(toIndentedString(nazione)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    pec: ").append(toIndentedString(pec)).append("\n");
    sb.append("    tel: ").append(toIndentedString(tel)).append("\n");
    sb.append("    fax: ").append(toIndentedString(fax)).append("\n");
    sb.append("    gln: ").append(toIndentedString(gln)).append("\n");
    sb.append("    web: ").append(toIndentedString(web)).append("\n");
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    idUnita: ").append(toIndentedString(idUnita)).append("\n");
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



