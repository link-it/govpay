package it.govpay.pagamento.test;

import javax.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Dominio  {
  
  @Schema(example = "1.23456789E+9", required = true, description = "Codice fiscale")
 /**
   * Codice fiscale  
  **/
  private String idDominio = null;
  
  @Schema(example = "Ufficio due", required = true, description = "Ragione sociale")
 /**
   * Ragione sociale  
  **/
  private String ragioneSociale = null;
  
  @Schema(example = "Piazza della Vittoria", description = "")
  private String indirizzo = null;
  
  @Schema(example = "10/A", description = "")
  private String civico = null;
  
  @Schema(example = "0", description = "")
  private String cap = null;
  
  @Schema(example = "Roma", description = "")
  private String localita = null;
  
  @Schema(example = "Roma", description = "")
  private String provincia = null;
  
  @Schema(example = "IT", description = "")
  private String nazione = null;
  
  @Schema(example = "peo@creditore.it", description = "Posta elettronica ordinaria")
 /**
   * Posta elettronica ordinaria  
  **/
  private String email = null;
  
  @Schema(example = "peo@creditore.it", description = "Posta elettronica certificata")
 /**
   * Posta elettronica certificata  
  **/
  private String pec = null;
  
  @Schema(example = "2.123456789E+10", description = "Numero di telefono dell'help desk di primo livello")
 /**
   * Numero di telefono dell'help desk di primo livello  
  **/
  private String tel = null;
  
  @Schema(example = "2.123456789E+10", description = "Numero di fax dell'help desk di primo livello")
 /**
   * Numero di fax dell'help desk di primo livello  
  **/
  private String fax = null;
  
  @Schema(example = "http://www.comune.it/", description = "Url del sito web")
 /**
   * Url del sito web  
  **/
  private String web = null;
  
  @Schema(example = "11223344556", description = "Global location number del beneficiario")
 /**
   * Global location number del beneficiario  
  **/
  private String gln = null;
  
  @Schema(example = "12345", description = "codice cbill del beneficiario")
 /**
   * codice cbill del beneficiario  
  **/
  private String cbill = null;
  
  @Schema(example = "http://.../domini/01234567890/logo", description = "URL del logo")
 /**
   * URL del logo  
  **/
  private String logo = null;
 /**
   * Codice fiscale
   * @return idDominio
  **/
  @JsonProperty("idDominio")
  @NotNull
 @Size(min=1,max=35)  public String getIdDominio() {
    return idDominio;
  }

  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  public Dominio idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

 /**
   * Ragione sociale
   * @return ragioneSociale
  **/
  @JsonProperty("ragioneSociale")
  @NotNull
 @Size(min=1,max=70)  public String getRagioneSociale() {
    return ragioneSociale;
  }

  public void setRagioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
  }

  public Dominio ragioneSociale(String ragioneSociale) {
    this.ragioneSociale = ragioneSociale;
    return this;
  }

 /**
   * Get indirizzo
   * @return indirizzo
  **/
  @JsonProperty("indirizzo")
 @Size(min=1,max=70)  public String getIndirizzo() {
    return indirizzo;
  }

  public void setIndirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
  }

  public Dominio indirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
    return this;
  }

 /**
   * Get civico
   * @return civico
  **/
  @JsonProperty("civico")
 @Size(min=1,max=16)  public String getCivico() {
    return civico;
  }

  public void setCivico(String civico) {
    this.civico = civico;
  }

  public Dominio civico(String civico) {
    this.civico = civico;
    return this;
  }

 /**
   * Get cap
   * @return cap
  **/
  @JsonProperty("cap")
 @Size(min=1,max=16)  public String getCap() {
    return cap;
  }

  public void setCap(String cap) {
    this.cap = cap;
  }

  public Dominio cap(String cap) {
    this.cap = cap;
    return this;
  }

 /**
   * Get localita
   * @return localita
  **/
  @JsonProperty("localita")
 @Size(min=1,max=35)  public String getLocalita() {
    return localita;
  }

  public void setLocalita(String localita) {
    this.localita = localita;
  }

  public Dominio localita(String localita) {
    this.localita = localita;
    return this;
  }

 /**
   * Get provincia
   * @return provincia
  **/
  @JsonProperty("provincia")
 @Size(min=1,max=35)  public String getProvincia() {
    return provincia;
  }

  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }

  public Dominio provincia(String provincia) {
    this.provincia = provincia;
    return this;
  }

 /**
   * Get nazione
   * @return nazione
  **/
  @JsonProperty("nazione")
 @Pattern(regexp="[A-Z]{2,2}")  public String getNazione() {
    return nazione;
  }

  public void setNazione(String nazione) {
    this.nazione = nazione;
  }

  public Dominio nazione(String nazione) {
    this.nazione = nazione;
    return this;
  }

 /**
   * Posta elettronica ordinaria
   * @return email
  **/
  @JsonProperty("email")
 @Pattern(regexp="[A-Za-z0-9_]+([\\-\\+\\.'][A-Za-z0-9_]+)*@[A-Za-z0-9_]+([\\-\\.][A-Za-z0-9_]+)*\\.[A-Za-z0-9_]+([\\-\\.][A-Za-z0-9_]+)*")  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Dominio email(String email) {
    this.email = email;
    return this;
  }

 /**
   * Posta elettronica certificata
   * @return pec
  **/
  @JsonProperty("pec")
 @Pattern(regexp="[A-Za-z0-9_]+([\\-\\+\\.'][A-Za-z0-9_]+)*@[A-Za-z0-9_]+([\\-\\.][A-Za-z0-9_]+)*\\.[A-Za-z0-9_]+([\\-\\.][A-Za-z0-9_]+)*")  public String getPec() {
    return pec;
  }

  public void setPec(String pec) {
    this.pec = pec;
  }

  public Dominio pec(String pec) {
    this.pec = pec;
    return this;
  }

 /**
   * Numero di telefono dell&#x27;help desk di primo livello
   * @return tel
  **/
  @JsonProperty("tel")
  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public Dominio tel(String tel) {
    this.tel = tel;
    return this;
  }

 /**
   * Numero di fax dell&#x27;help desk di primo livello
   * @return fax
  **/
  @JsonProperty("fax")
  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public Dominio fax(String fax) {
    this.fax = fax;
    return this;
  }

 /**
   * Url del sito web
   * @return web
  **/
  @JsonProperty("web")
  public String getWeb() {
    return web;
  }

  public void setWeb(String web) {
    this.web = web;
  }

  public Dominio web(String web) {
    this.web = web;
    return this;
  }

 /**
   * Global location number del beneficiario
   * @return gln
  **/
  @JsonProperty("gln")
  public String getGln() {
    return gln;
  }

  public void setGln(String gln) {
    this.gln = gln;
  }

  public Dominio gln(String gln) {
    this.gln = gln;
    return this;
  }

 /**
   * codice cbill del beneficiario
   * @return cbill
  **/
  @JsonProperty("cbill")
  public String getCbill() {
    return cbill;
  }

  public void setCbill(String cbill) {
    this.cbill = cbill;
  }

  public Dominio cbill(String cbill) {
    this.cbill = cbill;
    return this;
  }

 /**
   * URL del logo
   * @return logo
  **/
  @JsonProperty("logo")
  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public Dominio logo(String logo) {
    this.logo = logo;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dominio {\n");
    
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
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
    sb.append("    logo: ").append(toIndentedString(logo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
