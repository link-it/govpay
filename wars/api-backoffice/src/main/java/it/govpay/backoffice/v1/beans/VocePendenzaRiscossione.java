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

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"indice",
"idVocePendenza",
"importo",
"descrizione",
"stato",
"descrizioneCausaleRPT",
"contabilita",
"dominio",
"hashDocumento",
"tipoBollo",
"provinciaResidenza",
"codEntrata",
"pendenza",
"codiceContabilita",
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
})
public class VocePendenzaRiscossione extends JSONSerializable {

  @JsonProperty("indice")
  private BigDecimal indice = null;

  @JsonProperty("idVocePendenza")
  private String idVocePendenza = null;

  @JsonProperty("importo")
  private BigDecimal importo = null;

  @JsonProperty("descrizione")
  private String descrizione = null;

  @JsonProperty("stato")
  private StatoVocePendenza stato = null;

  @JsonProperty("descrizioneCausaleRPT")
  private String descrizioneCausaleRPT = null;

  @JsonProperty("contabilita")
  private Contabilita contabilita = null;

  @JsonProperty("dominio")
  private DominioIndex dominio = null;

  @JsonProperty("pendenza")
  private PendenzaIndex pendenza = null;
  
  @JsonProperty("hashDocumento")
  private String hashDocumento= null;

  @JsonProperty("tipoBollo")
  private String tipoBollo= null;

  @JsonProperty("provinciaResidenza")
  private String provinciaResidenza= null;

  @JsonProperty("codEntrata")
  private String codEntrata= null;

  @JsonProperty("codiceContabilita")
  private String codiceContabilita= null;

  @JsonProperty("ibanAccredito")
  private String ibanAccredito= null;

  @JsonProperty("ibanAppoggio")
  private String ibanAppoggio= null;

  @JsonProperty("tipoContabilita")
  private TipoContabilita tipoContabilita= null;

  /**
   * indice di voce all'interno della pendenza
   **/
  public VocePendenzaRiscossione indice(BigDecimal indice) {
    this.indice = indice;
    return this;
  }

  @JsonProperty("indice")
  public BigDecimal getIndice() {
    return indice;
  }
  public void setIndice(BigDecimal indice) {
    this.indice = indice;
  }

  /**
   * Identificativo della voce di pedenza nel gestionale proprietario
   **/
  public VocePendenzaRiscossione idVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
    return this;
  }

  @JsonProperty("idVocePendenza")
  public String getIdVocePendenza() {
    return idVocePendenza;
  }
  public void setIdVocePendenza(String idVocePendenza) {
    this.idVocePendenza = idVocePendenza;
  }

  /**
   * Importo della voce
   **/
  public VocePendenzaRiscossione importo(BigDecimal importo) {
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

  /**
   * descrizione della voce di pagamento
   **/
  public VocePendenzaRiscossione descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  public VocePendenzaRiscossione stato(StatoVocePendenza stato) {
    this.stato = stato;
    return this;
  }

  @JsonProperty("stato")
  public StatoVocePendenza getStato() {
    return stato;
  }
  public void setStato(StatoVocePendenza stato) {
    this.stato = stato;
  }

  /**
   * Testo libero per la causale versamento
   **/
  public VocePendenzaRiscossione descrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
    return this;
  }

  @JsonProperty("descrizioneCausaleRPT")
  public String getDescrizioneCausaleRPT() {
    return descrizioneCausaleRPT;
  }
  public void setDescrizioneCausaleRPT(String descrizioneCausaleRPT) {
    this.descrizioneCausaleRPT = descrizioneCausaleRPT;
  }

  /**
   **/
  public VocePendenzaRiscossione contabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
    return this;
  }

  @JsonProperty("contabilita")
  public Contabilita getContabilita() {
    return contabilita;
  }
  public void setContabilita(Contabilita contabilita) {
    this.contabilita = contabilita;
  }

  /**
   **/
  public VocePendenzaRiscossione dominio(DominioIndex dominio) {
    this.dominio = dominio;
    return this;
  }

  @JsonProperty("dominio")
  public DominioIndex getDominio() {
    return dominio;
  }
  public void setDominio(DominioIndex dominio) {
    this.dominio = dominio;
  }

  /**
   **/
  public VocePendenzaRiscossione pendenza(PendenzaIndex pendenza) {
    this.pendenza = pendenza;
    return this;
  }

  @JsonProperty("pendenza")
  public PendenzaIndex getPendenza() {
    return pendenza;
  }
  public void setPendenza(PendenzaIndex pendenza) {
    this.pendenza = pendenza;
  }

  @JsonProperty("hashDocumento")
  public String getHashDocumento() {
    return this.hashDocumento;
  }
  public void setHashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
  }

  public VocePendenzaRiscossione tipoBollo(String tipoBollo) {
    this.tipoBollo = tipoBollo;
    return this;
  }

  @JsonProperty("tipoBollo")
  public String getTipoBollo() {
    return this.tipoBollo;
  }
  public void setTipoBollo(String tipoBollo) {
    this.tipoBollo = tipoBollo;
  }

  public VocePendenzaRiscossione codEntrata(String codEntrata) {
    this.codEntrata = codEntrata;
    return this;
  }

  @JsonProperty("codEntrata")
  public String getCodEntrata() {
    return this.codEntrata;
  }
  public void setCodEntrata(String codEntrata) {
    this.codEntrata= codEntrata;
  }

  public VocePendenzaRiscossione provinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
    return this;
  }

  @JsonProperty("provinciaResidenza")
  public String getProvinciaResidenza() {
    return this.provinciaResidenza;
  }
  public void setProvinciaResidenza(String provinciaResidenza) {
    this.provinciaResidenza = provinciaResidenza;
  }

  public VocePendenzaRiscossione codiceContabilita(String codiceContabilita) {
    this.codiceContabilita= codiceContabilita;
    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return this.codiceContabilita;
  }
  public void setCodiceContabilita(String codiceContabilita) {
    this.codiceContabilita = codiceContabilita;
  }

  public VocePendenzaRiscossione ibanAccredito(String ibanAccredito) {
    this.ibanAccredito= ibanAccredito;
    return this;
  }

  @JsonProperty("ibanAccredito")
  public String getIbanAccredito() {
    return this.ibanAccredito;
  }
  public void setIbanAccredito(String ibanAccredito) {
    this.ibanAccredito = ibanAccredito;
  }

  public VocePendenzaRiscossione tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita= tipoContabilita;
    return this;
  }

	public VocePendenzaRiscossione ibanAppoggio(String ibanAppoggio) {
		this.ibanAppoggio= ibanAppoggio;
		return this;
	}

	@JsonProperty("ibanAppoggio")
	public String getIbanAppoggio() {
		return this.ibanAppoggio;
	}
	public void setIbanAppoggio(String ibanAppoggio) {
		this.ibanAppoggio = ibanAppoggio;
	}

  @JsonProperty("tipoContabilita")
  public TipoContabilita getTipoContabilita() {
    return this.tipoContabilita;
  }
  public void setTipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita = tipoContabilita;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VocePendenzaRiscossione vocePendenzaRiscossione = (VocePendenzaRiscossione) o;
    return Objects.equals(indice, vocePendenzaRiscossione.indice) &&
        Objects.equals(idVocePendenza, vocePendenzaRiscossione.idVocePendenza) &&
        Objects.equals(importo, vocePendenzaRiscossione.importo) &&
        Objects.equals(descrizione, vocePendenzaRiscossione.descrizione) &&
        Objects.equals(stato, vocePendenzaRiscossione.stato) &&
        Objects.equals(descrizioneCausaleRPT, vocePendenzaRiscossione.descrizioneCausaleRPT) &&
        Objects.equals(contabilita, vocePendenzaRiscossione.contabilita) &&
        Objects.equals(dominio, vocePendenzaRiscossione.dominio) &&
        Objects.equals(pendenza, vocePendenzaRiscossione.pendenza) &&
        Objects.equals(hashDocumento, vocePendenzaRiscossione.hashDocumento) &&
	    Objects.equals(tipoBollo, vocePendenzaRiscossione.tipoBollo) &&
	    Objects.equals(provinciaResidenza, vocePendenzaRiscossione.provinciaResidenza) &&
	    Objects.equals(ibanAccredito, vocePendenzaRiscossione.ibanAccredito) &&
	    Objects.equals(ibanAppoggio, vocePendenzaRiscossione.ibanAppoggio) &&
	    Objects.equals(codiceContabilita, vocePendenzaRiscossione.codiceContabilita) &&
        Objects.equals(tipoContabilita, vocePendenzaRiscossione.tipoContabilita) &&
	    Objects.equals(codEntrata, vocePendenzaRiscossione.codEntrata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indice, idVocePendenza, importo, descrizione, stato, descrizioneCausaleRPT, contabilita, dominio, pendenza, hashDocumento, tipoBollo, provinciaResidenza, codiceContabilita, tipoContabilita, codEntrata);
  }

  public static VocePendenzaRiscossione parse(String json) throws IOException {
    return parse(json, VocePendenzaRiscossione.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "vocePendenzaRiscossione";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VocePendenzaRiscossione {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    contabilita: ").append(toIndentedString(contabilita)).append("\n");
    sb.append("    dominio: ").append(toIndentedString(dominio)).append("\n");
    sb.append("    pendenza: ").append(toIndentedString(pendenza)).append("\n");
    sb.append("    hashDocumento: ").append(toIndentedString(hashDocumento)).append("\n");
    sb.append("    tipoBollo: ").append(toIndentedString(tipoBollo)).append("\n");
    sb.append("    provinciaResidenza: ").append(toIndentedString(provinciaResidenza)).append("\n");
    sb.append("    codiceContabilita: ").append(toIndentedString(codiceContabilita)).append("\n");
    sb.append("    ibanAccredito: ").append(toIndentedString(ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(toIndentedString(ibanAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(toIndentedString(tipoContabilita)).append("\n");
    sb.append("    codEntrata: ").append(toIndentedString(codEntrata)).append("\n");
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



