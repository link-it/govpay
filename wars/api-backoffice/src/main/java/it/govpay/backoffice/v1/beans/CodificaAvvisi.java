/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * informazioni sulla codifica e decodifica degli iuv
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"codificaIuv",
"regExpIuv",
"generazioneIuvInterna",
})
public class CodificaAvvisi extends it.govpay.core.beans.JSONSerializable implements IValidable {

  @JsonProperty("codificaIuv")
  private String codificaIuv = null;

  @JsonProperty("regExpIuv")
  private String regExpIuv = null;

  @JsonProperty("generazioneIuvInterna")
  private Boolean generazioneIuvInterna = null;

  /**
   * Cifra identificativa negli IUV. Deve essere un codice numerico.
   **/
  public CodificaAvvisi codificaIuv(String codificaIuv) {
    this.codificaIuv = codificaIuv;
    return this;
  }

  @JsonProperty("codificaIuv")
  public String getCodificaIuv() {
    return this.codificaIuv;
  }
  public void setCodificaIuv(String codificaIuv) {
    this.codificaIuv = codificaIuv;
  }

  /**
   * Espressione regolare di verifica del numero avviso
   **/
  public CodificaAvvisi regExpIuv(String regExpIuv) {
    this.regExpIuv = regExpIuv;
    return this;
  }

  @JsonProperty("regExpIuv")
  public String getRegExpIuv() {
    return this.regExpIuv;
  }
  public void setRegExpIuv(String regExpIuv) {
    this.regExpIuv = regExpIuv;
  }

  /**
   * Indicazione se l'applicazione genera in autonomia gli iuv
   **/
  public CodificaAvvisi generazioneIuvInterna(Boolean generazioneIuvInterna) {
    this.generazioneIuvInterna = generazioneIuvInterna;
    return this;
  }

  @JsonProperty("generazioneIuvInterna")
  public Boolean isGenerazioneIuvInterna() {
    return this.generazioneIuvInterna;
  }
  public void setGenerazioneIuvInterna(Boolean generazioneIuvInterna) {
    this.generazioneIuvInterna = generazioneIuvInterna;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    CodificaAvvisi codificaAvvisi = (CodificaAvvisi) o;
    return Objects.equals(this.codificaIuv, codificaAvvisi.codificaIuv) &&
        Objects.equals(this.regExpIuv, codificaAvvisi.regExpIuv) &&
        Objects.equals(this.generazioneIuvInterna, codificaAvvisi.generazioneIuvInterna);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.codificaIuv, this.regExpIuv, this.generazioneIuvInterna);
  }

  public static CodificaAvvisi parse(String json) throws IOException {
    return parse(json, CodificaAvvisi.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "codificaAvvisi";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CodificaAvvisi {\n");

    sb.append("    codificaIuv: ").append(this.toIndentedString(this.codificaIuv)).append("\n");
    sb.append("    regExpIuv: ").append(this.toIndentedString(this.regExpIuv)).append("\n");
    sb.append("    generazioneIuvInterna: ").append(this.toIndentedString(this.generazioneIuvInterna)).append("\n");
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
public void validate() throws ValidationException {
		ValidatorFactory vf = ValidatorFactory.newInstance();
		if(this.codificaIuv != null)
			vf.getValidator("codificaIuv", this.codificaIuv).minLength(1).maxLength(15); // controllo spostato nel validatore del core.

		vf.getValidator("generazioneIuvInterna", this.generazioneIuvInterna).notNull();

		if(this.generazioneIuvInterna.booleanValue() && this.regExpIuv == null) {
			throw new ValidationException("Il campo regExpIuv non puo' essere vuoto quando e' selezionato il campo generazioneIuvInterna.");
		}

		if(this.regExpIuv != null)
			vf.getValidator("regExpIuv", this.regExpIuv).minLength(1).maxLength(1024);
	}
}



