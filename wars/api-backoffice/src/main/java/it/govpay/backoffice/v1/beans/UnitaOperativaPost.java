/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
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
"area",
"abilitato",
})
public class UnitaOperativaPost extends it.govpay.core.beans.JSONSerializable implements IValidable {

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

  @JsonProperty("abilitato")
  private Boolean abilitato = null;

  /**
   * Ragione sociale dell'unita
   **/
  public UnitaOperativaPost ragioneSociale(String ragioneSociale) {
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
  public UnitaOperativaPost indirizzo(String indirizzo) {
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
  public UnitaOperativaPost civico(String civico) {
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
  public UnitaOperativaPost cap(String cap) {
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
  public UnitaOperativaPost localita(String localita) {
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
  public UnitaOperativaPost provincia(String provincia) {
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
  public UnitaOperativaPost nazione(String nazione) {
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
  public UnitaOperativaPost email(String email) {
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
  public UnitaOperativaPost pec(String pec) {
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
  public UnitaOperativaPost tel(String tel) {
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
  public UnitaOperativaPost fax(String fax) {
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
  public UnitaOperativaPost web(String web) {
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
  public UnitaOperativaPost area(String area) {
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
  public UnitaOperativaPost abilitato(Boolean abilitato) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    UnitaOperativaPost unitaOperativaPost = (UnitaOperativaPost) o;
    return Objects.equals(this.ragioneSociale, unitaOperativaPost.ragioneSociale) &&
        Objects.equals(this.indirizzo, unitaOperativaPost.indirizzo) &&
        Objects.equals(this.civico, unitaOperativaPost.civico) &&
        Objects.equals(this.cap, unitaOperativaPost.cap) &&
        Objects.equals(this.localita, unitaOperativaPost.localita) &&
        Objects.equals(this.provincia, unitaOperativaPost.provincia) &&
        Objects.equals(this.nazione, unitaOperativaPost.nazione) &&
        Objects.equals(this.email, unitaOperativaPost.email) &&
        Objects.equals(this.pec, unitaOperativaPost.pec) &&
        Objects.equals(this.tel, unitaOperativaPost.tel) &&
        Objects.equals(this.fax, unitaOperativaPost.fax) &&
        Objects.equals(this.web, unitaOperativaPost.web) &&
        Objects.equals(this.area, unitaOperativaPost.area) &&
        Objects.equals(this.abilitato, unitaOperativaPost.abilitato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ragioneSociale, this.indirizzo, this.civico, this.cap, this.localita, this.provincia, this.nazione, this.email, this.pec, this.tel, this.fax, this.web, this.area, this.abilitato);
  }

  public static UnitaOperativaPost parse(String json) throws IOException {
    return parse(json, UnitaOperativaPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "unitaOperativaPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnitaOperativaPost {\n");

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
    sb.append("    area: ").append(this.toIndentedString(this.area)).append("\n");
    sb.append("    abilitato: ").append(this.toIndentedString(this.abilitato)).append("\n");
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
	public void validate() throws it.govpay.core.exceptions.ValidationException {
			ValidatorFactory vf = ValidatorFactory.newInstance();

			vf.getValidator("ragioneSociale", this.ragioneSociale).notNull().minLength(1).maxLength(70);
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
			vf.getValidator("abilitato", this.abilitato).notNull();
	  }
}



