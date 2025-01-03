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

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.model.exception.CodificaInesistenteException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"giorni",
"tipo",
})
public class VincoloPagamento extends JSONSerializable implements IValidable{

  @JsonProperty("giorni")
  private BigDecimal giorni = null;

  @JsonIgnore
  private TipoSogliaVincoloPagamento tipoEnum = null;

  @JsonProperty("tipo")
  private String tipo = null;

  /**
   * numero di giorni vincolo per il pagamento
   **/
  public VincoloPagamento giorni(BigDecimal giorni) {
    this.giorni = giorni;
    return this;
  }

  @JsonProperty("giorni")
  public BigDecimal getGiorni() {
    return giorni;
  }
  public void setGiorni(BigDecimal giorni) {
    this.giorni = giorni;
  }

  /**
   **/
  public VincoloPagamento tipoEnum(TipoSogliaVincoloPagamento tipo) {
    this.tipoEnum = tipo;
    return this;
  }

  public TipoSogliaVincoloPagamento getTipoEnum() {
    return tipoEnum;
  }
  public void setTipoEnum(TipoSogliaVincoloPagamento tipo) {
    this.tipoEnum = tipo;
  }

  /**
   **/
  public VincoloPagamento tipo(String tipo) {
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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VincoloPagamento vincoloPagamento = (VincoloPagamento) o;
    return Objects.equals(giorni, vincoloPagamento.giorni) &&
        Objects.equals(tipo, vincoloPagamento.tipo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(giorni, tipo);
  }

  public static VincoloPagamento parse(String json) throws IOException {
    return parse(json, VincoloPagamento.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "vincoloPagamento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VincoloPagamento {\n");

    sb.append("    giorni: ").append(toIndentedString(giorni)).append("\n");
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
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

	  	ValidatoreUtils.validaSogliaTipo(vf, "tipo", this.tipo);

		try {
			TipoSogliaVersamento tipoSoglia = TipoSogliaVersamento.toEnum(this.tipo);

			switch(tipoSoglia) {
			case ENTRO:
			case OLTRE:
				ValidatoreUtils.validaSogliaGiorni(vf, "giorni", this.giorni);
				break;
			case RIDOTTO:
			case SCONTATO:
				try {
				vf.getValidator("giorni", this.giorni).isNull();
				} catch (Exception e) {
					throw new ValidationException("Il campo giorni deve essere vuoto quando il campo tipo assume valore 'RIDOTTO' o 'SCONTATO'.");
				}
				break;
			}
		}catch (CodificaInesistenteException e) {
			throw new ValidationException(e);
		}
	}
}


