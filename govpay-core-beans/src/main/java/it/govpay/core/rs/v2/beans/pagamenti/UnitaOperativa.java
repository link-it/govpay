package it.govpay.core.rs.v2.beans.pagamenti;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.rs.v1.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
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
    return this.ragioneSociale;
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
    return this.indirizzo;
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
    return this.civico;
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
    return this.cap;
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
    return this.localita;
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
    return this.provincia;
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
    return this.nazione;
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
    return this.email;
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
    return this.pec;
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
    return this.tel;
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
    return this.fax;
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
    return this.gln;
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
    return this.web;
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
    return this.area;
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
    return this.abilitato;
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
    return this.idUnita;
  }
  public void setIdUnita(String idUnita) {
    this.idUnita = idUnita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    UnitaOperativa unitaOperativa = (UnitaOperativa) o;
    return Objects.equals(this.ragioneSociale, unitaOperativa.ragioneSociale) &&
        Objects.equals(this.indirizzo, unitaOperativa.indirizzo) &&
        Objects.equals(this.civico, unitaOperativa.civico) &&
        Objects.equals(this.cap, unitaOperativa.cap) &&
        Objects.equals(this.localita, unitaOperativa.localita) &&
        Objects.equals(this.provincia, unitaOperativa.provincia) &&
        Objects.equals(this.nazione, unitaOperativa.nazione) &&
        Objects.equals(this.email, unitaOperativa.email) &&
        Objects.equals(this.pec, unitaOperativa.pec) &&
        Objects.equals(this.tel, unitaOperativa.tel) &&
        Objects.equals(this.fax, unitaOperativa.fax) &&
        Objects.equals(this.gln, unitaOperativa.gln) &&
        Objects.equals(this.web, unitaOperativa.web) &&
        Objects.equals(this.area, unitaOperativa.area) &&
        Objects.equals(this.abilitato, unitaOperativa.abilitato) &&
        Objects.equals(this.idUnita, unitaOperativa.idUnita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ragioneSociale, this.indirizzo, this.civico, this.cap, this.localita, this.provincia, this.nazione, this.email, this.pec, this.tel, this.fax, this.gln, this.web, this.area, this.abilitato, this.idUnita);
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
    
    sb.append("    ragioneSociale: ").append(this.toIndentedString(this.ragioneSociale)).append("\n");
    sb.append("    indirizzo: ").append(this.toIndentedString(this.indirizzo)).append("\n");
    sb.append("    civico: ").append(this.toIndentedString(this.civico)).append("\n");
    sb.append("    cap: ").append(this.toIndentedString(this.cap)).append("\n");
    sb.append("    localita: ").append(this.toIndentedString(this.localita)).append("\n");
    sb.append("    provincia: ").append(this.toIndentedString(this.provincia)).append("\n");
    sb.append("    nazione: ").append(this.toIndentedString(this.nazione)).append("\n");
    sb.append("    email: ").append(this.toIndentedString(this.email)).append("\n");
    sb.append("    pec: ").append(this.toIndentedString(this.pec)).append("\n");
    sb.append("    tel: ").append(this.toIndentedString(this.tel)).append("\n");
    sb.append("    fax: ").append(this.toIndentedString(this.fax)).append("\n");
    sb.append("    gln: ").append(this.toIndentedString(this.gln)).append("\n");
    sb.append("    web: ").append(this.toIndentedString(this.web)).append("\n");
    sb.append("    area: ").append(this.toIndentedString(this.area)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
    sb.append("    idUnita: ").append(this.toIndentedString(this.idUnita)).append("\n");
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



