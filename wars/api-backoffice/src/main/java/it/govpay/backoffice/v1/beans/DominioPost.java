package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@JsonPropertyOrder({
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
})
public class DominioPost extends it.govpay.core.beans.JSONSerializable implements IValidable{
  
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
  
  /**
   * Ragione sociale del beneficiario
   **/
  public DominioPost ragioneSociale(String ragioneSociale) {
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
  public DominioPost indirizzo(String indirizzo) {
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
  public DominioPost civico(String civico) {
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
  public DominioPost cap(String cap) {
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
  public DominioPost localita(String localita) {
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
  public DominioPost provincia(String provincia) {
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
  public DominioPost nazione(String nazione) {
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
  public DominioPost email(String email) {
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
  public DominioPost pec(String pec) {
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
  public DominioPost tel(String tel) {
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
  public DominioPost fax(String fax) {
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
  public DominioPost web(String web) {
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
  public DominioPost gln(String gln) {
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
  public DominioPost cbill(String cbill) {
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
  public DominioPost iuvPrefix(String iuvPrefix) {
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
  public DominioPost stazione(String stazione) {
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
  public DominioPost auxDigit(String auxDigit) {
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
  public DominioPost segregationCode(String segregationCode) {
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
  public DominioPost logo(String logo) {
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
  public DominioPost abilitato(Boolean abilitato) {
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
  public DominioPost autStampaPosteItaliane(String autStampaPosteItaliane) {
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
  public DominioPost area(String area) {
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
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    DominioPost dominioPost = (DominioPost) o;
    return Objects.equals(this.ragioneSociale, dominioPost.ragioneSociale) &&
        Objects.equals(this.indirizzo, dominioPost.indirizzo) &&
        Objects.equals(this.civico, dominioPost.civico) &&
        Objects.equals(this.cap, dominioPost.cap) &&
        Objects.equals(this.localita, dominioPost.localita) &&
        Objects.equals(this.provincia, dominioPost.provincia) &&
        Objects.equals(this.nazione, dominioPost.nazione) &&
        Objects.equals(this.email, dominioPost.email) &&
        Objects.equals(this.pec, dominioPost.pec) &&
        Objects.equals(this.tel, dominioPost.tel) &&
        Objects.equals(this.fax, dominioPost.fax) &&
        Objects.equals(this.web, dominioPost.web) &&
        Objects.equals(this.gln, dominioPost.gln) &&
        Objects.equals(this.cbill, dominioPost.cbill) &&
        Objects.equals(this.iuvPrefix, dominioPost.iuvPrefix) &&
        Objects.equals(this.stazione, dominioPost.stazione) &&
        Objects.equals(this.auxDigit, dominioPost.auxDigit) &&
        Objects.equals(this.segregationCode, dominioPost.segregationCode) &&
        Objects.equals(this.logo, dominioPost.logo) &&
        Objects.equals(this.abilitato, dominioPost.abilitato) &&
        Objects.equals(this.autStampaPosteItaliane, dominioPost.autStampaPosteItaliane)&&
        Objects.equals(this.area, dominioPost.area);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ragioneSociale, this.indirizzo, this.civico, this.cap, this.localita, this.provincia, this.nazione, this.email, this.pec, this.tel, this.fax, this.web, this.gln, this.cbill, this.iuvPrefix, this.stazione, this.auxDigit, this.segregationCode, this.logo, this.abilitato, this.autStampaPosteItaliane, this.area);
  }

  public static DominioPost parse(String json) throws org.openspcoop2.generic_project.exception.ServiceException, ValidationException {
    return parse(json, DominioPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "dominioPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DominioPost {\n");
    
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
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n").append("\n");
    sb.append("    autStampaPosteItaliane: ").append(this.toIndentedString(this.autStampaPosteItaliane)).append("\n");
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
  
  @Override
	public void validate() throws org.openspcoop2.utils.json.ValidationException {
			ValidatorFactory vf = ValidatorFactory.newInstance();
	
			vf.getValidator("ragioneSociale", this.ragioneSociale).notNull().minLength(1).maxLength(70);
			vf.getValidator("gln", this.gln).notNull().length(13).pattern("(^([0-9]){13}$)");
			vf.getValidator("auxDigit", this.auxDigit).notNull().length(1).pattern("(^([0-3]){1}$)");;
			vf.getValidator("stazione", this.stazione).notNull();
			vf.getValidator("segregationCode", this.segregationCode).length(2).pattern("(^[0-4][0-9]$)");
			vf.getValidator("cbill", this.cbill).length(5).pattern("(^([0-9]){5}$)");
			vf.getValidator("autStampaPosteItaliane", this.autStampaPosteItaliane).maxLength(255);
			vf.getValidator("iuvPrefix", this.iuvPrefix).maxLength(255);
			vf.getValidator("abilitato", this.abilitato).notNull();
			
			// uo associata al dominio
			vf.getValidator("indirizzo", this.indirizzo).minLength(1).maxLength(70);
			vf.getValidator("civico", this.civico).minLength(1).maxLength(16);
			vf.getValidator("cap", this.cap).minLength(1).maxLength(16);
			vf.getValidator("localita", this.localita).minLength(1).maxLength(35);
			vf.getValidator("provincia", this.provincia).minLength(1).maxLength(35);
			vf.getValidator("nazione", this.nazione).length(2).pattern("(^[A-Z]{2,2}$)");
			vf.getValidator("email", this.email).minLength(1).maxLength(255).pattern("(^([\\w\\.\\-_]+)?\\w+@[\\w-_]+(\\.\\w+){1,}$)");
			vf.getValidator("pec", this.pec).minLength(1).maxLength(255).pattern("(^([\\w\\.\\-_]+)?\\w+@[\\w-_]+(\\.\\w+){1,}$)");
			vf.getValidator("tel", this.tel).minLength(1).maxLength(255);
			vf.getValidator("fax", this.fax).minLength(1).maxLength(255);
			vf.getValidator("web", this.web).minLength(1).maxLength(255);
			vf.getValidator("area", this.area).minLength(1).maxLength(255);
	  }
}



