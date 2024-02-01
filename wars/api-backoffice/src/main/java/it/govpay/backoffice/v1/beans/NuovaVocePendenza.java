/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"indice",
"idVocePendenza",
"importo",
"descrizione",
"stato",
"descrizioneCausaleRPT",
"contabilita",
"idDominio",
"datiAllegati",
"hashDocumento",
"tipoBollo",
"provinciaResidenza",
"codEntrata",
"codiceContabilita",
"ibanAccredito",
"ibanAppoggio",
"tipoContabilita",
})
public class NuovaVocePendenza extends JSONSerializable implements IValidable {

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

  @JsonProperty("idDominio")
  private String idDominio = null;

  @JsonProperty("datiAllegati")
  private Object datiAllegati = null;

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
  public NuovaVocePendenza indice(BigDecimal indice) {
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
  public NuovaVocePendenza idVocePendenza(String idVocePendenza) {
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
  public NuovaVocePendenza importo(BigDecimal importo) {
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
  public NuovaVocePendenza descrizione(String descrizione) {
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
  public NuovaVocePendenza stato(StatoVocePendenza stato) {
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
  public NuovaVocePendenza descrizioneCausaleRPT(String descrizioneCausaleRPT) {
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
  public NuovaVocePendenza contabilita(Contabilita contabilita) {
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
   * Identificativo del dominio creditore
   **/
  public NuovaVocePendenza idDominio(String idDominio) {
    this.idDominio = idDominio;
    return this;
  }

  @JsonProperty("idDominio")
  public String getIdDominio() {
    return idDominio;
  }
  public void setIdDominio(String idDominio) {
    this.idDominio = idDominio;
  }

  /**
   * Dati applicativi allegati dal gestionale secondo un formato proprietario.
   **/
  public NuovaVocePendenza datiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
    return this;
  }

  @JsonProperty("datiAllegati")
  public Object getDatiAllegati() {
    return datiAllegati;
  }
  public void setDatiAllegati(Object datiAllegati) {
    this.datiAllegati = datiAllegati;
  }

  public NuovaVocePendenza hashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
    return this;
  }

  @JsonProperty("hashDocumento")
  public String getHashDocumento() {
    return this.hashDocumento;
  }
  public void setHashDocumento(String hashDocumento) {
    this.hashDocumento = hashDocumento;
  }

  public NuovaVocePendenza tipoBollo(String tipoBollo) {
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

  public NuovaVocePendenza codEntrata(String codEntrata) {
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

  public NuovaVocePendenza provinciaResidenza(String provinciaResidenza) {
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

  public NuovaVocePendenza codiceContabilita(String codiceContabilita) {
    this.codiceContabilita= codiceContabilita;
    return this;
  }

  @JsonProperty("codiceContabilita")
  public String getCodiceContabilita() {
    return this.codiceContabilita;
  }
  public void setCodiceContabilita(String CodiceContabilita) {
    this.codiceContabilita = CodiceContabilita;
  }

  public NuovaVocePendenza ibanAccredito(String ibanAccredito) {
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

  public NuovaVocePendenza tipoContabilita(TipoContabilita tipoContabilita) {
    this.tipoContabilita= tipoContabilita;
    return this;
  }

	public NuovaVocePendenza ibanAppoggio(String ibanAppoggio) {
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
    NuovaVocePendenza nuovaVocePendenza = (NuovaVocePendenza) o;
    return Objects.equals(indice, nuovaVocePendenza.indice) &&
        Objects.equals(idVocePendenza, nuovaVocePendenza.idVocePendenza) &&
        Objects.equals(importo, nuovaVocePendenza.importo) &&
        Objects.equals(descrizione, nuovaVocePendenza.descrizione) &&
        Objects.equals(stato, nuovaVocePendenza.stato) &&
        Objects.equals(descrizioneCausaleRPT, nuovaVocePendenza.descrizioneCausaleRPT) &&
        Objects.equals(contabilita, nuovaVocePendenza.contabilita) &&
        Objects.equals(idDominio, nuovaVocePendenza.idDominio) &&
        Objects.equals(datiAllegati, nuovaVocePendenza.datiAllegati) &&
        Objects.equals(this.hashDocumento, nuovaVocePendenza.hashDocumento) &&
        Objects.equals(this.tipoBollo, nuovaVocePendenza.tipoBollo) &&
        Objects.equals(this.provinciaResidenza, nuovaVocePendenza.provinciaResidenza) &&
        Objects.equals(this.codiceContabilita, nuovaVocePendenza.codiceContabilita) &&
        Objects.equals(this.ibanAccredito, nuovaVocePendenza.ibanAccredito) &&
        Objects.equals(this.ibanAppoggio, nuovaVocePendenza.ibanAppoggio) &&
        Objects.equals(this.tipoContabilita, nuovaVocePendenza.tipoContabilita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indice, idVocePendenza, importo, descrizione, stato, descrizioneCausaleRPT, contabilita, idDominio, datiAllegati, this.hashDocumento, this.tipoBollo, this.provinciaResidenza, this.codiceContabilita, this.ibanAccredito, this.tipoContabilita);
  }

  public static NuovaVocePendenza parse(String json) throws IOException {
    return parse(json, NuovaVocePendenza.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "nuovaVocePendenza";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NuovaVocePendenza {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    indice: ").append(toIndentedString(indice)).append("\n");
    sb.append("    idVocePendenza: ").append(toIndentedString(idVocePendenza)).append("\n");
    sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    descrizioneCausaleRPT: ").append(toIndentedString(descrizioneCausaleRPT)).append("\n");
    sb.append("    contabilita: ").append(toIndentedString(contabilita)).append("\n");
    sb.append("    idDominio: ").append(toIndentedString(idDominio)).append("\n");
    sb.append("    datiAllegati: ").append(toIndentedString(datiAllegati)).append("\n");
    sb.append("    hashDocumento: ").append(this.toIndentedString(this.hashDocumento)).append("\n");
    sb.append("    tipoBollo: ").append(this.toIndentedString(this.tipoBollo)).append("\n");
    sb.append("    provinciaResidenza: ").append(this.toIndentedString(this.provinciaResidenza)).append("\n");
    sb.append("    codiceContabilita: ").append(this.toIndentedString(this.codiceContabilita)).append("\n");
    sb.append("    ibanAccredito: ").append(this.toIndentedString(this.ibanAccredito)).append("\n");
    sb.append("    ibanAppoggio: ").append(this.toIndentedString(this.ibanAppoggio)).append("\n");
    sb.append("    tipoContabilita: ").append(this.toIndentedString(this.tipoContabilita)).append("\n");
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
		ValidatoreIdentificativi vi = ValidatoreIdentificativi.newInstance();

		vi.validaIdVocePendenza("idVocePendenza", this.idVocePendenza);
		ValidatoreUtils.validaImporto(vf, "importo", this.importo);
		ValidatoreUtils.validaDescrizione(vf, "descrizione", this.descrizione);
		ValidatoreUtils.validaDescrizioneCausaleRPT(vf, "descrizioneCausaleRPT", this.descrizioneCausaleRPT);
		vf.getValidator("contabilita", this.contabilita).validateFields();
		if(this.idDominio != null) {
			vi.validaIdDominio("idDominio", this.idDominio);
		}

		if(this.codEntrata != null) {
			vi.validaIdEntrata("codEntrata", this.codEntrata);
			try {
				vf.getValidator("tipoBollo", this.tipoBollo).isNull();
				vf.getValidator("hashDocumento", this.hashDocumento).isNull();
				vf.getValidator("provinciaResidenza", this.provinciaResidenza).isNull();
				vf.getValidator("ibanAccredito", this.ibanAccredito).isNull();
				vf.getValidator("ibanAppoggio", this.ibanAppoggio).isNull();
				vf.getValidator("tipoContabilita", this.tipoContabilita).isNull();
				vf.getValidator("codiceContabilita", this.codiceContabilita).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato codEntrata. " + ve.getMessage());
			}

			return;
		}

		else if(this.tipoBollo != null) {
			ValidatoreUtils.validaTipoBollo(vf, "tipoBollo", this.tipoBollo);
			ValidatoreUtils.validaHashDocumento(vf, "hashDocumento", this.hashDocumento);
			ValidatoreUtils.validaProvinciaResidenza(vf, "provinciaResidenza", this.provinciaResidenza);

			try {
				vf.getValidator("ibanAccredito", this.ibanAccredito).isNull();
				vf.getValidator("ibanAppoggio", this.ibanAppoggio).isNull();
				vf.getValidator("tipoContabilita", this.tipoContabilita).isNull();
				vf.getValidator("codiceContabilita", this.codiceContabilita).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato tipoBollo. " + ve.getMessage());
			}

			return;
		} else if(this.ibanAccredito != null) {
			vi.validaIdIbanAccredito("ibanAccredito", this.ibanAccredito, true);
			vi.validaIdIbanAccredito("ibanAppoggio", this.ibanAppoggio);

			ValidatoreUtils.validaTipoContabilita(vf, "tipoContabilita", this.tipoContabilita);
			ValidatoreUtils.validaCodiceContabilita(vf, "codiceContabilita", this.codiceContabilita);

			try {
				vf.getValidator("hashDocumento", this.hashDocumento).isNull();
				vf.getValidator("provinciaResidenza", this.provinciaResidenza).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Valorizzato ibanAccredito. " + ve.getMessage());
			}
		}

		else {
			throw new ValidationException("Uno dei campi tra ibanAccredito, tipoBollo o codEntrata deve essere valorizzato");
		}

	}
}



