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
package it.govpay.pagamento.v2.beans;


import java.math.BigDecimal;
import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import it.govpay.core.exceptions.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"voce",
"importo",
})
public class VoceDescrizioneImporto extends JSONSerializable implements IValidable{
  
  @JsonProperty("voce")
  private String voce = null;
  
  @JsonProperty("importo")
  private BigDecimal importo = null;
  
  /**
   * voce importo
   **/
  public VoceDescrizioneImporto voce(String voce) {
    this.voce = voce;
    return this;
  }

  @JsonProperty("voce")
  public String getVoce() {
    return voce;
  }
  public void setVoce(String voce) {
    this.voce = voce;
  }

  /**
   * importo
   **/
  public VoceDescrizioneImporto importo(BigDecimal importo) {
    this.importo = importo;
    return this;
  }

  @JsonProperty("importo")
  public BigDecimal getImporto() {
    return importo;
  }
  public void setImporto(BigDecimal importo) {
    this.importo = importo;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoceDescrizioneImporto voceDescrizioneImporto = (VoceDescrizioneImporto) o;
    return Objects.equals(voce, voceDescrizioneImporto.voce) &&
        Objects.equals(importo, voceDescrizioneImporto.importo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(voce, importo);
  }

  public static VoceDescrizioneImporto parse(String json) throws it.govpay.core.exceptions.IOException {
    return (VoceDescrizioneImporto) parse(json, VoceDescrizioneImporto.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "voceDescrizioneImporto";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoceDescrizioneImporto {\n");
    
    sb.append("    voce: ").append(toIndentedString(voce)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
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
	// TODO Auto-generated method stub
	
}
}



