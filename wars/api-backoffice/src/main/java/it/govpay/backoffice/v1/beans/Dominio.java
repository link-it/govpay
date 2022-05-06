package it.govpay.backoffice.v1.beans;

import java.util.List;
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
"servizioGovPay",
"servizioHyperSicAPKappa",
"servizioMaggioliJPPA",
"intermediato",
"idDominio",
"unitaOperative",
"contiAccredito",
"entrate",
"tipiPendenza",
})
public class Dominio extends it.govpay.core.beans.JSONSerializable {
  
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
  
  @JsonProperty("servizioGovPay")
  private ConnettoreNotificaPagamentiGovPay servizioGovPay = null;
  
  @JsonProperty("servizioHyperSicAPKappa")
  private ConnettoreNotificaPagamentiHyperSicAPKappa servizioHyperSicAPKappa = null;
  
  @JsonProperty("servizioMaggioliJPPA")
  private ConnettoreNotificaPagamentiMaggioliJPPA servizioMaggioliJPPA = null;
  
  @JsonProperty("intermediato")
  private Boolean intermediato = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("unitaOperative")
  private List<UnitaOperativa> unitaOperative = null;
  
  @JsonProperty("contiAccredito")
  private List<ContiAccredito> contiAccredito = null;
  
  @JsonProperty("entrate")
  private List<Entrata> entrate = null;
  
  @JsonProperty("tipiPendenza")
  private List<TipoPendenzaDominio> tipiPendenza = null;
  
  /**
   * Ragione sociale del beneficiario
   **/
  public Dominio ragioneSociale(String ragioneSociale) {
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
  public Dominio indirizzo(String indirizzo) {
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
  public Dominio civico(String civico) {
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
  public Dominio cap(String cap) {
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
  public Dominio localita(String localita) {
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
  public Dominio provincia(String provincia) {
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
  public Dominio nazione(String nazione) {
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
  public Dominio email(String email) {
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
  public Dominio pec(String pec) {
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
  public Dominio tel(String tel) {
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
  public Dominio fax(String fax) {
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
  public Dominio web(String web) {
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
  public Dominio gln(String gln) {
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
  public Dominio cbill(String cbill) {
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
  public Dominio iuvPrefix(String iuvPrefix) {
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
  public Dominio stazione(String stazione) {
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
  public Dominio auxDigit(String auxDigit) {
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
  public Dominio segregationCode(String segregationCode) {
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
  public Dominio logo(String logo) {
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
  public Dominio abilitato(Boolean abilitato) {
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
  public Dominio autStampaPosteItaliane(String autStampaPosteItaliane) {
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
  public Dominio area(String area) {
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
  public Dominio servizioMyPivot(ConnettoreNotificaPagamentiMyPivot servizioMyPivot) {
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
  public Dominio servizioSecim(ConnettoreNotificaPagamentiSecim servizioSecim) {
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
   **/
  public Dominio servizioGovPay(ConnettoreNotificaPagamentiGovPay servizioGovPay) {
    this.servizioGovPay = servizioGovPay;
    return this;
  }

  @JsonProperty("servizioGovPay")
  public ConnettoreNotificaPagamentiGovPay getServizioGovPay() {
    return servizioGovPay;
  }
  public void setServizioGovPay(ConnettoreNotificaPagamentiGovPay servizioGovPay) {
    this.servizioGovPay = servizioGovPay;
  }

  /**
   **/
  public Dominio servizioHyperSicAPKappa(ConnettoreNotificaPagamentiHyperSicAPKappa servizioHyperSicAPKappa) {
    this.servizioHyperSicAPKappa = servizioHyperSicAPKappa;
    return this;
  }

  @JsonProperty("servizioHyperSicAPKappa")
  public ConnettoreNotificaPagamentiHyperSicAPKappa getServizioHyperSicAPKappa() {
    return servizioHyperSicAPKappa;
  }
  public void setServizioHyperSicAPKappa(ConnettoreNotificaPagamentiHyperSicAPKappa servizioHyperSicAPKappa) {
    this.servizioHyperSicAPKappa = servizioHyperSicAPKappa;
  }

  /**
   **/
  public Dominio servizioMaggioliJPPA(ConnettoreNotificaPagamentiMaggioliJPPA servizioMaggioliJPPA) {
    this.servizioMaggioliJPPA = servizioMaggioliJPPA;
    return this;
  }

  @JsonProperty("servizioMaggioliJPPA")
  public ConnettoreNotificaPagamentiMaggioliJPPA getServizioMaggioliJPPA() {
    return servizioMaggioliJPPA;
  }
  public void setServizioMaggioliJPPA(ConnettoreNotificaPagamentiMaggioliJPPA servizioMaggioliJPPA) {
    this.servizioMaggioliJPPA = servizioMaggioliJPPA;
  }

  /**
   * Indica se il creditore viene configurato per utilizzare una  stazione di intermediazione
   **/
  public Dominio intermediato(Boolean intermediato) {
    this.intermediato = intermediato;
    return this;
  }

  @JsonProperty("intermediato")
  public Boolean Intermediato() {
    return intermediato;
  }
  public void setIntermediato(Boolean intermediato) {
    this.intermediato = intermediato;
  }

  /**
   * Codice fiscale del beneficiario
   **/
  public Dominio idDominio(String idDominio) {
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
   **/
  public Dominio unitaOperative(List<UnitaOperativa> unitaOperative) {
    this.unitaOperative = unitaOperative;
    return this;
  }

  @JsonProperty("unitaOperative")
  public List<UnitaOperativa> getUnitaOperative() {
    return this.unitaOperative;
  }
  public void setUnitaOperative(List<UnitaOperativa> unitaOperative) {
    this.unitaOperative = unitaOperative;
  }

  /**
   **/
  public Dominio contiAccredito(List<ContiAccredito> contiAccredito) {
    this.contiAccredito = contiAccredito;
    return this;
  }

  @JsonProperty("contiAccredito")
  public List<ContiAccredito> getContiAccredito() {
    return this.contiAccredito;
  }
  public void setContiAccredito(List<ContiAccredito> contiAccredito) {
    this.contiAccredito = contiAccredito;
  }

  /**
   **/
  public Dominio entrate(List<Entrata> entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public List<Entrata> getEntrate() {
    return this.entrate;
  }
  public void setEntrate(List<Entrata> entrate) {
    this.entrate = entrate;
  }

  /**
   **/
  public Dominio tipiPendenza(List<TipoPendenzaDominio> tipiPendenza) {
    this.tipiPendenza = tipiPendenza;
    return this;
  }

  @JsonProperty("tipiPendenza")
  public List<TipoPendenzaDominio> getTipiPendenza() {
    return tipiPendenza;
  }
  public void setTipiPendenza(List<TipoPendenzaDominio> tipiPendenza) {
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
    Dominio dominio = (Dominio) o;
    return Objects.equals(this.ragioneSociale, dominio.ragioneSociale) &&
        Objects.equals(this.indirizzo, dominio.indirizzo) &&
        Objects.equals(this.civico, dominio.civico) &&
        Objects.equals(this.cap, dominio.cap) &&
        Objects.equals(this.localita, dominio.localita) &&
        Objects.equals(this.provincia, dominio.provincia) &&
        Objects.equals(this.nazione, dominio.nazione) &&
        Objects.equals(this.email, dominio.email) &&
        Objects.equals(this.pec, dominio.pec) &&
        Objects.equals(this.tel, dominio.tel) &&
        Objects.equals(this.fax, dominio.fax) &&
        Objects.equals(this.web, dominio.web) &&
        Objects.equals(this.gln, dominio.gln) &&
        Objects.equals(this.cbill, dominio.cbill) &&
        Objects.equals(this.iuvPrefix, dominio.iuvPrefix) &&
        Objects.equals(this.stazione, dominio.stazione) &&
        Objects.equals(this.auxDigit, dominio.auxDigit) &&
        Objects.equals(this.segregationCode, dominio.segregationCode) &&
        Objects.equals(this.logo, dominio.logo) &&
        Objects.equals(this.abilitato, dominio.abilitato) &&
        Objects.equals(this.autStampaPosteItaliane, dominio.autStampaPosteItaliane) &&
        Objects.equals(area, dominio.area) &&
        Objects.equals(servizioMyPivot, dominio.servizioMyPivot) &&
        Objects.equals(servizioSecim, dominio.servizioSecim) &&
        Objects.equals(servizioGovPay, dominio.servizioGovPay) &&
        Objects.equals(servizioHyperSicAPKappa, dominio.servizioHyperSicAPKappa) &&
        Objects.equals(servizioMaggioliJPPA, dominio.servizioMaggioliJPPA) &&
        Objects.equals(intermediato, dominio.intermediato) &&
        Objects.equals(idDominio, dominio.idDominio) &&
        Objects.equals(unitaOperative, dominio.unitaOperative) &&
        Objects.equals(contiAccredito, dominio.contiAccredito) &&
        Objects.equals(entrate, dominio.entrate) &&
        Objects.equals(tipiPendenza, dominio.tipiPendenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ragioneSociale, indirizzo, civico, cap, localita, provincia, nazione, email, pec, tel, fax, web, gln, cbill, iuvPrefix, stazione, auxDigit, segregationCode, logo, abilitato, autStampaPosteItaliane, area, servizioMyPivot, servizioSecim, servizioGovPay, servizioHyperSicAPKappa, servizioMaggioliJPPA, intermediato, idDominio, unitaOperative, contiAccredito, entrate, tipiPendenza);
  }

  public static Dominio parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, Dominio.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "dominio";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dominio {\n");
    
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
    sb.append("    autStampaPosteItaliane: ").append(this.toIndentedString(this.autStampaPosteItaliane)).append("\n");
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
    sb.append("    servizioMyPivot: ").append(toIndentedString(servizioMyPivot)).append("\n");
    sb.append("    servizioSecim: ").append(toIndentedString(servizioSecim)).append("\n");
    sb.append("    servizioGovPay: ").append(toIndentedString(servizioGovPay)).append("\n");
    sb.append("    servizioHyperSicAPKappa: ").append(toIndentedString(servizioHyperSicAPKappa)).append("\n");
    sb.append("    servizioMaggioliJPPA: ").append(toIndentedString(servizioMaggioliJPPA)).append("\n");
    sb.append("    intermediato: ").append(toIndentedString(intermediato)).append("\n");
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



