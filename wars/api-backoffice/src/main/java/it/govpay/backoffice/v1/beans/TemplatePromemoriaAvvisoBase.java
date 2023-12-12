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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Configurazione della generazione dei promemoria avviso pagamento
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"tipo",
"oggetto",
"messaggio",
})
public class TemplatePromemoriaAvvisoBase extends JSONSerializable implements IValidable {

  private TipoTemplateTrasformazione tipoEnum = null;

  @JsonProperty("tipo")
  private String tipo = null;

  @JsonProperty("oggetto")
  private Object oggetto = null;

  @JsonProperty("messaggio")
  private Object messaggio = null;

  /**
   **/
  public TemplatePromemoriaAvvisoBase tipo(TipoTemplateTrasformazione tipo) {
    this.tipoEnum = tipo;
    return this;
  }

  @JsonIgnore
  public TipoTemplateTrasformazione getTipoEnum() {
    return tipoEnum;
  }
  public void setTipo(TipoTemplateTrasformazione tipoEnum) {
    this.tipoEnum = tipoEnum;
  }

  public TemplatePromemoriaAvvisoBase tipo(String tipo) {
    this.tipo = tipo;
    return this;
  }

  @JsonProperty("tipo")
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   * Template di trasformazione da applicare per ottenere l'oggetto da inserire nella email
   **/
  public TemplatePromemoriaAvvisoBase oggetto(Object oggetto) {
    this.oggetto = oggetto;
    return this;
  }

  @JsonProperty("oggetto")
  public Object getOggetto() {
    return oggetto;
  }
  public void setOggetto(Object oggetto) {
    this.oggetto = oggetto;
  }

  /**
   * Template di trasformazione da applicare per ottenere il messaggio da inserire nella email
   **/
  public TemplatePromemoriaAvvisoBase messaggio(Object messaggio) {
    this.messaggio = messaggio;
    return this;
  }

  @JsonProperty("messaggio")
  public Object getMessaggio() {
    return messaggio;
  }
  public void setMessaggio(Object messaggio) {
    this.messaggio = messaggio;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplatePromemoriaAvvisoBase templatePromemoriaAvvisoBase = (TemplatePromemoriaAvvisoBase) o;
    return Objects.equals(tipo, templatePromemoriaAvvisoBase.tipo) &&
        Objects.equals(oggetto, templatePromemoriaAvvisoBase.oggetto) &&
        Objects.equals(messaggio, templatePromemoriaAvvisoBase.messaggio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, oggetto, messaggio);
  }

  public static TemplatePromemoriaAvvisoBase parse(String json) throws IOException {
    return parse(json, TemplatePromemoriaAvvisoBase.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "templatePromemoriaAvvisoBase";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplatePromemoriaAvvisoBase {\n");

    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
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
	this.validate("promemoriaAvviso");
}

public void validate(String fieldName) throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();

	int v = 0;
	v = this.oggetto != null ? v+1 : v;
	v = this.messaggio != null ? v+1 : v;
	v = this.tipo != null ? v+1 : v;

	if(v != 3) {
	  throw new ValidationException("I campi 'tipo', 'oggetto' e 'messaggio' devono essere tutti valorizzati per definire il field '"+fieldName+"'.");
	}

	vf.getValidator("tipo", this.tipo).minLength(1).maxLength(35);
}
}



