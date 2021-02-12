package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
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
"web",
"gln",
"cbill",
"iuvPrefix",
"stazione",
"auxDigit",
"segregationCode",
"logo",
"abilitato",
"autStampaPosteItaliane",
"area",
"servizioMyPivot",
"servizioSecim",
"idDominio",
"unitaOperative",
"contiAccredito",
"entrate",
"tipiPendenza",
})
public class DominioIndex extends it.govpay.core.beans.JSONSerializable {
   
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
  
  @JsonProperty("autStampaPosteItaliane")
  private String autStampaPosteItaliane = null;
  
  @JsonProperty("area")
  private String area = null;
  
  @JsonProperty("servizioMyPivot")
  private ConnettoreNotificaPagamentiMyPivot servizioMyPivot = null;
  
  @JsonProperty("servizioSecim")
  private ConnettoreNotificaPagamentiSecim servizioSecim = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("unitaOperative")
  private String unitaOperative = null;
  
  @JsonProperty("contiAccredito")
  private String contiAccredito = null;
  
  @JsonProperty("entrate")
  private String entrate = null;
  
  @JsonProperty("tipiPendenza")
  private String tipiPendenza = null;
  
  /**
   * Ragione sociale del beneficiario
   **/
  public DominioIndex ragioneSociale(String ragioneSociale) {
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
   * Indirizzo del beneficiario
   **/
  public DominioIndex indirizzo(String indirizzo) {
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
   * Numero civico del beneficiario
   **/
  public DominioIndex civico(String civico) {
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
   * Codice avviamento postale del beneficiario
   **/
  public DominioIndex cap(String cap) {
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
   * Località del beneficiario
   **/
  public DominioIndex localita(String localita) {
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
  public DominioIndex provincia(String provincia) {
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
  public DominioIndex nazione(String nazione) {
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
  public DominioIndex email(String email) {
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
  public DominioIndex pec(String pec) {
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
  public DominioIndex tel(String tel) {
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
  public DominioIndex fax(String fax) {
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
   * Url del sito web
   **/
  public DominioIndex web(String web) {
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
   * Global location number del beneficiario
   **/
  public DominioIndex gln(String gln) {
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
   * codice cbill del beneficiario
   **/
  public DominioIndex cbill(String cbill) {
    this.cbill = cbill;
    return this;
  }

  @JsonProperty("cbill")
  public String getCbill() {
    return this.cbill;
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
    return this.iuvPrefix;
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
    return this.stazione;
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
    return this.auxDigit;
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
    return this.segregationCode;
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
    return this.logo;
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
    return this.abilitato;
  }
  public void setAbilitato(Boolean abilitato) {
    this.abilitato = abilitato;
  }

  /**
   * numero di autorizzazione per la stampa in proprio rilasciato da poste italiane
   **/
  public DominioIndex autStampaPosteItaliane(String autStampaPosteItaliane) {
    this.autStampaPosteItaliane = autStampaPosteItaliane;
    return this;
  }

  @JsonProperty("autStampaPosteItaliane")
  public String getAutStampaPosteItaliane() {
    return autStampaPosteItaliane;
  }
  public void setAutStampaPosteItaliane(String autStampaPosteItaliane) {
    this.autStampaPosteItaliane = autStampaPosteItaliane;
  }

  /**
   * Nome dell'area di competenza del dominio
   **/
  public DominioIndex area(String area) {
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
   **/
  public DominioIndex servizioMyPivot(ConnettoreNotificaPagamentiMyPivot servizioMyPivot) {
    this.servizioMyPivot = servizioMyPivot;
    return this;
  }

  @JsonProperty("servizioMyPivot")
  public ConnettoreNotificaPagamentiMyPivot getServizioMyPivot() {
    return servizioMyPivot;
  }
  public void setServizioMyPivot(ConnettoreNotificaPagamentiMyPivot servizioMyPivot) {
    this.servizioMyPivot = servizioMyPivot;
  }

  /**
   **/
  public DominioIndex servizioSecim(ConnettoreNotificaPagamentiSecim servizioSecim) {
    this.servizioSecim = servizioSecim;
    return this;
  }

  @JsonProperty("servizioSecim")
  public ConnettoreNotificaPagamentiSecim getServizioSecim() {
    return servizioSecim;
  }
  public void setServizioSecim(ConnettoreNotificaPagamentiSecim servizioSecim) {
    this.servizioSecim = servizioSecim;
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
    return this.idDominio;
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
    return this.unitaOperative;
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
    return this.contiAccredito;
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
    return this.entrate;
  }
  public void setEntrate(String entrate) {
    this.entrate = entrate;
  }

  /**
   * Tipologie di pendenza censite per il dominio beneficiario
   **/
  public DominioIndex tipiPendenza(String tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public String getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(String tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    DominioIndex dominioIndex = (DominioIndex) o;
    return Objects.equals(this.ragioneSociale, dominioIndex.ragioneSociale) &&
        Objects.equals(this.indirizzo, dominioIndex.indirizzo) &&
        Objects.equals(this.civico, dominioIndex.civico) &&
        Objects.equals(this.cap, dominioIndex.cap) &&
        Objects.equals(this.localita, dominioIndex.localita) &&
        Objects.equals(this.provincia, dominioIndex.provincia) &&
        Objects.equals(this.nazione, dominioIndex.nazione) &&
        Objects.equals(this.email, dominioIndex.email) &&
        Objects.equals(this.pec, dominioIndex.pec) &&
        Objects.equals(this.tel, dominioIndex.tel) &&
        Objects.equals(this.fax, dominioIndex.fax) &&
        Objects.equals(this.web, dominioIndex.web) &&
        Objects.equals(this.gln, dominioIndex.gln) &&
        Objects.equals(this.cbill, dominioIndex.cbill) &&
        Objects.equals(this.iuvPrefix, dominioIndex.iuvPrefix) &&
        Objects.equals(this.stazione, dominioIndex.stazione) &&
        Objects.equals(this.auxDigit, dominioIndex.auxDigit) &&
        Objects.equals(this.segregationCode, dominioIndex.segregationCode) &&
        Objects.equals(this.logo, dominioIndex.logo) &&
        Objects.equals(this.abilitato, dominioIndex.abilitato) &&
        Objects.equals(autStampaPosteItaliane, dominioIndex.autStampaPosteItaliane) &&
        Objects.equals(area, dominioIndex.area) &&
        Objects.equals(servizioMyPivot, dominioIndex.servizioMyPivot) &&
        Objects.equals(servizioSecim, dominioIndex.servizioSecim) &&
        Objects.equals(idDominio, dominioIndex.idDominio) &&
        Objects.equals(unitaOperative, dominioIndex.unitaOperative) &&
        Objects.equals(contiAccredito, dominioIndex.contiAccredito) &&
        Objects.equals(entrate, dominioIndex.entrate) &&
        Objects.equals(tipiPendenza, dominioIndex.tipiPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ragioneSociale, indirizzo, civico, cap, localita, provincia, nazione, email, pec, tel, fax, web, gln, cbill, iuvPrefix, stazione, auxDigit, segregationCode, logo, abilitato, autStampaPosteItaliane, area, servizioMyPivot, servizioSecim, idDominio, unitaOperative, contiAccredito, entrate, tipiPendenza);
  }

  public static DominioIndex parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, DominioIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "dominioIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DominioIndex {\n");
    
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
    sb.append("    web: ").append(this.toIndentedString(this.web)).append("\n");
    sb.append("    gln: ").append(this.toIndentedString(this.gln)).append("\n");
    sb.append("    cbill: ").append(this.toIndentedString(this.cbill)).append("\n");
    sb.append("    iuvPrefix: ").append(this.toIndentedString(this.iuvPrefix)).append("\n");
    sb.append("    stazione: ").append(this.toIndentedString(this.stazione)).append("\n");
    sb.append("    auxDigit: ").append(this.toIndentedString(this.auxDigit)).append("\n");
    sb.append("    segregationCode: ").append(this.toIndentedString(this.segregationCode)).append("\n");
    sb.append("    logo: ").append(this.toIndentedString(this.logo)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
    sb.append("    autStampaPosteItaliane: ").append(toIndentedString(autStampaPosteItaliane)).append("\n");
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
    sb.append("    servizioMyPivot: ").append(toIndentedString(servizioMyPivot)).append("\n");
    sb.append("    servizioSecim: ").append(toIndentedString(servizioSecim)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    unitaOperative: ").append(toIndentedString(unitaOperative)).append("\n");
    sb.append("    contiAccredito: ").append(toIndentedString(contiAccredito)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
    sb.append("    tipiPendenza: ").append(toIndentedString(tipiPendenza)).append("\n");
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



