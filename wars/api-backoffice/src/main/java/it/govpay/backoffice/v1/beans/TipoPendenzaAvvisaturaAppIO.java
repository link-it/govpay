/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

/**
 * Configurazione dell&#x27;avvisatura via app io
 **/@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"promemoriaAvviso",
"promemoriaRicevuta",
"promemoriaScadenza",
})
public class TipoPendenzaAvvisaturaAppIO extends JSONSerializable implements IValidable {

  @JsonProperty("promemoriaAvviso")
  private TipoPendenzaAvvisaturaPromemoriaAvvisoBase promemoriaAvviso = null;

  @JsonProperty("promemoriaRicevuta")
  private TipoPendenzaAvvisaturaPromemoriaRicevutaBase promemoriaRicevuta = null;

  @JsonProperty("promemoriaScadenza")
  private TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza = null;

  /**
   **/
  public TipoPendenzaAvvisaturaAppIO promemoriaAvviso(TipoPendenzaAvvisaturaPromemoriaAvvisoBase promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
    return this;
  }

  @JsonProperty("promemoriaAvviso")
  public TipoPendenzaAvvisaturaPromemoriaAvvisoBase getPromemoriaAvviso() {
    return promemoriaAvviso;
  }
  public void setPromemoriaAvviso(TipoPendenzaAvvisaturaPromemoriaAvvisoBase promemoriaAvviso) {
    this.promemoriaAvviso = promemoriaAvviso;
  }

  /**
   **/
  public TipoPendenzaAvvisaturaAppIO promemoriaRicevuta(TipoPendenzaAvvisaturaPromemoriaRicevutaBase promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
    return this;
  }

  @JsonProperty("promemoriaRicevuta")
  public TipoPendenzaAvvisaturaPromemoriaRicevutaBase getPromemoriaRicevuta() {
    return promemoriaRicevuta;
  }
  public void setPromemoriaRicevuta(TipoPendenzaAvvisaturaPromemoriaRicevutaBase promemoriaRicevuta) {
    this.promemoriaRicevuta = promemoriaRicevuta;
  }

  /**
   **/
  public TipoPendenzaAvvisaturaAppIO promemoriaScadenza(TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza) {
    this.promemoriaScadenza = promemoriaScadenza;
    return this;
  }

  @JsonProperty("promemoriaScadenza")
  public TipoPendenzaAvvisaturaPromemoriaScadenza getPromemoriaScadenza() {
    return promemoriaScadenza;
  }
  public void setPromemoriaScadenza(TipoPendenzaAvvisaturaPromemoriaScadenza promemoriaScadenza) {
    this.promemoriaScadenza = promemoriaScadenza;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaAvvisaturaAppIO tipoPendenzaAvvisaturaAppIO = (TipoPendenzaAvvisaturaAppIO) o;
    return Objects.equals(promemoriaAvviso, tipoPendenzaAvvisaturaAppIO.promemoriaAvviso) &&
        Objects.equals(promemoriaRicevuta, tipoPendenzaAvvisaturaAppIO.promemoriaRicevuta) &&
        Objects.equals(promemoriaScadenza, tipoPendenzaAvvisaturaAppIO.promemoriaScadenza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(promemoriaAvviso, promemoriaRicevuta, promemoriaScadenza);
  }

  public static TipoPendenzaAvvisaturaAppIO parse(String json) throws IOException {
    return parse(json, TipoPendenzaAvvisaturaAppIO.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaAvvisaturaAppIO";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaAvvisaturaAppIO {\n");

    sb.append("    promemoriaAvviso: ").append(toIndentedString(promemoriaAvviso)).append("\n");
    sb.append("    promemoriaRicevuta: ").append(toIndentedString(promemoriaRicevuta)).append("\n");
    sb.append("    promemoriaScadenza: ").append(toIndentedString(promemoriaScadenza)).append("\n");
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
	this.validate(true);
}

public void validate(boolean abilitatoObbligatorio) throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();

	vf.getValidator("promemoriaAvviso", this.promemoriaAvviso).validateFields();
	vf.getValidator("promemoriaRicevuta", this.promemoriaRicevuta).validateFields();
	vf.getValidator("promemoriaScadenza", this.promemoriaScadenza).validateFields();
}
}



