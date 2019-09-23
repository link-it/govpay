package it.govpay.pendenze.v2.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idUnita",
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
"web",
"area",
})
public class UnitaOperativa extends JSONSerializable {
  
  @JsonProperty("idUnita")
  private String idUnita = null;
  
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
  
  @JsonProperty("web")
  private String web = null;
  
  @JsonProperty("area")
  private String area = null;
  
  /**
   * Codice fiscale
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

  /**
   * Ragione sociale
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
   * Posta elettronica ordinaria
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
   * Posta elettronica certificata
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
   * Numero di telefono dell'help desk di primo livello
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
   * Numero di fax dell'help desk di primo livello
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
   * Nome dell'area di competenza
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnitaOperativa unitaOperativa = (UnitaOperativa) o;
    return Objects.equals(idUnita, unitaOperativa.idUnita) &&
        Objects.equals(ragioneSociale, unitaOperativa.ragioneSociale) &&
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
        Objects.equals(web, unitaOperativa.web) &&
        Objects.equals(area, unitaOperativa.area);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUnita, ragioneSociale, indirizzo, civico, cap, localita, provincia, nazione, email, pec, tel, fax, web, area);
  }

  public static UnitaOperativa parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, UnitaOperativa.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "unitaOperativa";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnitaOperativa {\n");
    
    sb.append("    idUnita: ").append(toIndentedString(idUnita)).append("\n");
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
    sb.append("    web: ").append(toIndentedString(web)).append("\n");
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
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



