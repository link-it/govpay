package it.govpay.core.rs.v1.beans.pagamenti;

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
"web",
"gln",
"cbill",
"iuvPrefix",
"stazione",
"auxDigit",
"segregationCode",
"logo",
"abilitato",
"idDominio",
"unitaOperative",
"contiAccredito",
"entrate",
})
public class DominioIndex extends JSONSerializable {
  
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
  
  @JsonProperty("gln")
  private String gln = null;
  
  @JsonProperty("cbill")
  private String cbill = null;
  
  @JsonProperty("iuvPrefix")
  private String iuvPrefix = null;
  
  @JsonProperty("stazione")
  private String stazione = null;
  
  @JsonProperty("auxDigit")
  private String auxDigit = null;
  
  @JsonProperty("segregationCode")
  private String segregationCode = null;
  
  @JsonProperty("logo")
  private String logo = null;
  
  @JsonProperty("abilitato")
  private Boolean abilitato = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("unitaOperative")
  private String unitaOperative = null;
  
  @JsonProperty("contiAccredito")
  private String contiAccredito = null;
  
  @JsonProperty("entrate")
  private String entrate = null;
  
  /**
   * Ragione sociale del beneficiario
   **/
  public DominioIndex ragioneSociale(String ragioneSociale) {
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
   * Indirizzo del beneficiario
   **/
  public DominioIndex indirizzo(String indirizzo) {
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
   * Numero civico del beneficiario
   **/
  public DominioIndex civico(String civico) {
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
   * Codice avviamento postale del beneficiario
   **/
  public DominioIndex cap(String cap) {
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
   * Località del beneficiario
   **/
  public DominioIndex localita(String localita) {
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
  public DominioIndex provincia(String provincia) {
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
  public DominioIndex nazione(String nazione) {
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
  public DominioIndex email(String email) {
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
  public DominioIndex pec(String pec) {
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
  public DominioIndex tel(String tel) {
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
  public DominioIndex fax(String fax) {
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
  public DominioIndex web(String web) {
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
   * Global location number del beneficiario
   **/
  public DominioIndex gln(String gln) {
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
   * codice cbill del beneficiario
   **/
  public DominioIndex cbill(String cbill) {
    this.cbill = cbill;
    return this;
  }

  @JsonProperty("cbill")
  public String getCbill() {
    return cbill;
  }
  public void setCbill(String cbill) {
    this.cbill = cbill;
  }

  /**
   * Prefisso negli IUV generati da GovPay
   **/
  public DominioIndex iuvPrefix(String iuvPrefix) {
    this.iuvPrefix = iuvPrefix;
    return this;
  }

  @JsonProperty("iuvPrefix")
  public String getIuvPrefix() {
    return iuvPrefix;
  }
  public void setIuvPrefix(String iuvPrefix) {
    this.iuvPrefix = iuvPrefix;
  }

  /**
   * Codice stazione PagoPA che intermedia il beneficiario
   **/
  public DominioIndex stazione(String stazione) {
    this.stazione = stazione;
    return this;
  }

  @JsonProperty("stazione")
  public String getStazione() {
    return stazione;
  }
  public void setStazione(String stazione) {
    this.stazione = stazione;
  }

  /**
   * Valore della prima cifra dei Numero Avviso generati da GovPay
   **/
  public DominioIndex auxDigit(String auxDigit) {
    this.auxDigit = auxDigit;
    return this;
  }

  @JsonProperty("auxDigit")
  public String getAuxDigit() {
    return auxDigit;
  }
  public void setAuxDigit(String auxDigit) {
    this.auxDigit = auxDigit;
  }

  /**
   * Codice di segregazione utilizzato in caso di beneficiario pluri-intermediato (auxDigit = 3)
   **/
  public DominioIndex segregationCode(String segregationCode) {
    this.segregationCode = segregationCode;
    return this;
  }

  @JsonProperty("segregationCode")
  public String getSegregationCode() {
    return segregationCode;
  }
  public void setSegregationCode(String segregationCode) {
    this.segregationCode = segregationCode;
  }

  /**
   * Base64 del logo del beneficiario
   **/
  public DominioIndex logo(String logo) {
    this.logo = logo;
    return this;
  }

  @JsonProperty("logo")
  public String getLogo() {
    return logo;
  }
  public void setLogo(String logo) {
    this.logo = logo;
  }

  /**
   * Indicazione se il creditore è abilitato ad operare sulla piattaforma
   **/
  public DominioIndex abilitato(Boolean abilitato) {
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
   * Codice fiscale del beneficiario
   **/
  public DominioIndex idDominio(String idDominio) {
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

  /**
   * Url alle unità operative censite per il dominio
   **/
  public DominioIndex unitaOperative(String unitaOperative) {
    this.unitaOperative = unitaOperative;
    return this;
  }

  @JsonProperty("unitaOperative")
  public String getUnitaOperative() {
    return unitaOperative;
  }
  public void setUnitaOperative(String unitaOperative) {
    this.unitaOperative = unitaOperative;
  }

  /**
   * Conti di accredito censiti per il dominio beneficiario
   **/
  public DominioIndex contiAccredito(String contiAccredito) {
    this.contiAccredito = contiAccredito;
    return this;
  }

  @JsonProperty("contiAccredito")
  public String getContiAccredito() {
    return contiAccredito;
  }
  public void setContiAccredito(String contiAccredito) {
    this.contiAccredito = contiAccredito;
  }

  /**
   * Tipologie di entrata censite per il dominio beneficiario
   **/
  public DominioIndex entrate(String entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public String getEntrate() {
    return entrate;
  }
  public void setEntrate(String entrate) {
    this.entrate = entrate;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DominioIndex dominioIndex = (DominioIndex) o;
    return Objects.equals(ragioneSociale, dominioIndex.ragioneSociale) &&
        Objects.equals(indirizzo, dominioIndex.indirizzo) &&
        Objects.equals(civico, dominioIndex.civico) &&
        Objects.equals(cap, dominioIndex.cap) &&
        Objects.equals(localita, dominioIndex.localita) &&
        Objects.equals(provincia, dominioIndex.provincia) &&
        Objects.equals(nazione, dominioIndex.nazione) &&
        Objects.equals(email, dominioIndex.email) &&
        Objects.equals(pec, dominioIndex.pec) &&
        Objects.equals(tel, dominioIndex.tel) &&
        Objects.equals(fax, dominioIndex.fax) &&
        Objects.equals(web, dominioIndex.web) &&
        Objects.equals(gln, dominioIndex.gln) &&
        Objects.equals(cbill, dominioIndex.cbill) &&
        Objects.equals(iuvPrefix, dominioIndex.iuvPrefix) &&
        Objects.equals(stazione, dominioIndex.stazione) &&
        Objects.equals(auxDigit, dominioIndex.auxDigit) &&
        Objects.equals(segregationCode, dominioIndex.segregationCode) &&
        Objects.equals(logo, dominioIndex.logo) &&
        Objects.equals(abilitato, dominioIndex.abilitato) &&
        Objects.equals(idDominio, dominioIndex.idDominio) &&
        Objects.equals(unitaOperative, dominioIndex.unitaOperative) &&
        Objects.equals(contiAccredito, dominioIndex.contiAccredito) &&
        Objects.equals(entrate, dominioIndex.entrate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ragioneSociale, indirizzo, civico, cap, localita, provincia, nazione, email, pec, tel, fax, web, gln, cbill, iuvPrefix, stazione, auxDigit, segregationCode, logo, abilitato, idDominio, unitaOperative, contiAccredito, entrate);
  }

  public static DominioIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException {
    return (DominioIndex) parse(json, DominioIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "dominioIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DominioIndex {\n");
    
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
    sb.append("    gln: ").append(toIndentedString(gln)).append("\n");
    sb.append("    cbill: ").append(toIndentedString(cbill)).append("\n");
    sb.append("    iuvPrefix: ").append(toIndentedString(iuvPrefix)).append("\n");
    sb.append("    stazione: ").append(toIndentedString(stazione)).append("\n");
    sb.append("    auxDigit: ").append(toIndentedString(auxDigit)).append("\n");
    sb.append("    segregationCode: ").append(toIndentedString(segregationCode)).append("\n");
    sb.append("    logo: ").append(toIndentedString(logo)).append("\n");
    sb.append("    abilitato: ").append(toIndentedString(abilitato)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    unitaOperative: ").append(toIndentedString(unitaOperative)).append("\n");
    sb.append("    contiAccredito: ").append(toIndentedString(contiAccredito)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
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



