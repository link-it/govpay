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
package it.govpay.core.beans.tracciati;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idTracciato",
"idDominio",
"inserimenti",
"annullamenti",
})
public class DettaglioTracciatoPendenzeEsito extends JSONSerializable {
  
  @JsonProperty("idTracciato")
  private String idTracciato = null;
  
  @JsonProperty("idDominio")
  private String idDominio = null;
  
  @JsonProperty("inserimenti")
  private List<EsitoOperazionePendenza> inserimenti = null;
  
  @JsonProperty("annullamenti")
  private List<EsitoOperazionePendenza> annullamenti = null;
  
  /**
   * Identificativo del tracciato
   **/
  public DettaglioTracciatoPendenzeEsito idTracciato(String idTracciato) {
    this.idTracciato = idTracciato;
    return this;
  }

  @JsonProperty("idTracciato")
  public String getIdTracciato() {
    return this.idTracciato;
  }
  public void setIdTracciato(String idTracciato) {
    this.idTracciato = idTracciato;
  }

  /**
   * Identificativo del Dominio
   **/
  public DettaglioTracciatoPendenzeEsito idDominio(String idDominio) {
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
  public DettaglioTracciatoPendenzeEsito inserimenti(List<EsitoOperazionePendenza> inserimenti) {
    this.inserimenti = inserimenti;
    return this;
  }

  @JsonProperty("inserimenti")
  public List<EsitoOperazionePendenza> getInserimenti() {
    return this.inserimenti;
  }
  public void setInserimenti(List<EsitoOperazionePendenza> inserimenti) {
    this.inserimenti = inserimenti;
  }

  /**
   **/
  public DettaglioTracciatoPendenzeEsito annullamenti(List<EsitoOperazionePendenza> annullamenti) {
    this.annullamenti = annullamenti;
    return this;
  }

  @JsonProperty("annullamenti")
  public List<EsitoOperazionePendenza> getAnnullamenti() {
    return this.annullamenti;
  }
  public void setAnnullamenti(List<EsitoOperazionePendenza> annullamenti) {
    this.annullamenti = annullamenti;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    DettaglioTracciatoPendenzeEsito dettaglioTracciatoPendenzeEsito = (DettaglioTracciatoPendenzeEsito) o;
    return Objects.equals(this.idTracciato, dettaglioTracciatoPendenzeEsito.idTracciato) &&
        Objects.equals(this.idDominio, dettaglioTracciatoPendenzeEsito.idDominio) &&
        Objects.equals(this.inserimenti, dettaglioTracciatoPendenzeEsito.inserimenti) &&
        Objects.equals(this.annullamenti, dettaglioTracciatoPendenzeEsito.annullamenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idTracciato, this.idDominio, this.inserimenti, this.annullamenti);
  }

  public static DettaglioTracciatoPendenzeEsito parse(String json) throws IOException {
    return parse(json, DettaglioTracciatoPendenzeEsito.class); 
  }

  @Override
  public String getJsonIdFilter() {
    return "dettaglioTracciatoPendenzeEsito";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DettaglioTracciatoPendenzeEsito {\n");
    
    sb.append("    idTracciato: ").append(this.toIndentedString(this.idTracciato)).append("\n");
    sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
    sb.append("    inserimenti: ").append(this.toIndentedString(this.inserimenti)).append("\n");
    sb.append("    annullamenti: ").append(this.toIndentedString(this.annullamenti)).append("\n");
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



