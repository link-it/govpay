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
package it.govpay.pagamento.v1.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
	"idDominio",
	"idUnitaOperativa",
	"nome",
	"causale",
	"soggettoPagatore",
	"importo",
	"numeroAvviso",
	"dataCaricamento",
	"dataValidita",
	"dataScadenza",
	"annoRiferimento",
	"cartellaPagamento",
	"datiAllegati",
	"tassonomia",
	"tassonomiaAvviso",
	"voci",
	"idA2A",
	"idPendenza",
	"idDebitore",
})
public class PendenzaPost extends JSONSerializable implements IValidable {

	@JsonProperty("idDominio")
	private String idDominio = null;

	@JsonProperty("idUnitaOperativa")
	private String idUnitaOperativa = null;

	@JsonProperty("nome")
	private String nome = null;

	@JsonProperty("causale")
	private String causale = null;

	@JsonProperty("soggettoPagatore")
	private Soggetto soggettoPagatore = null;

	@JsonProperty("importo")
	private BigDecimal importo = null;

	@JsonProperty("numeroAvviso")
	private String numeroAvviso = null;

	@JsonProperty("dataCaricamento")
	private Date dataCaricamento = null;

	@JsonProperty("dataValidita")
	private Date dataValidita = null;

	@JsonProperty("dataScadenza")
	private Date dataScadenza = null;

	@JsonProperty("annoRiferimento")
	private BigDecimal annoRiferimento = null;

	@JsonProperty("cartellaPagamento")
	private String cartellaPagamento = null;

	@JsonProperty("datiAllegati")
	private Object datiAllegati = null;

	@JsonProperty("tassonomia")
	private String tassonomia = null;

	@JsonIgnore
	private TassonomiaAvviso tassonomiaAvvisoEnum = null;

	@JsonProperty("tassonomiaAvviso")
	private String tassonomiaAvviso = null;

	@JsonProperty("voci")
	private List<VocePendenza> voci = new ArrayList<>();

	@JsonProperty("idA2A")
	private String idA2A = null;

	@JsonProperty("idPendenza")
	private String idPendenza = null;

	@JsonProperty("idDebitore")
	private String idDebitore = null;

	/**
	 * Identificativo del dominio creditore
	 **/
	public PendenzaPost idDominio(String idDominio) {
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
	 * Identificativo dell'unita' operativa
	 **/
	public PendenzaPost idUnitaOperativa(String idUnitaOperativa) {
		this.idUnitaOperativa = idUnitaOperativa;
		return this;
	}

	@JsonProperty("idUnitaOperativa")
	public String getIdUnitaOperativa() {
		return this.idUnitaOperativa;
	}
	public void setIdUnitaOperativa(String idUnitaOperativa) {
		this.idUnitaOperativa = idUnitaOperativa;
	}

	/**
	 * Nome della pendenza da visualizzare sui portali di pagamento e console di gestione.
	 **/
	public PendenzaPost nome(String nome) {
		this.nome = nome;
		return this;
	}

	@JsonProperty("nome")
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Descrizione da inserire nell'avviso di pagamento
	 **/
	public PendenzaPost causale(String causale) {
		this.causale = causale;
		return this;
	}

	@JsonProperty("causale")
	public String getCausale() {
		return this.causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}

	/**
	 **/
	public PendenzaPost soggettoPagatore(Soggetto soggettoPagatore) {
		this.soggettoPagatore = soggettoPagatore;
		return this;
	}

	@JsonProperty("soggettoPagatore")
	public Soggetto getSoggettoPagatore() {
		return this.soggettoPagatore;
	}
	public void setSoggettoPagatore(Soggetto soggettoPagatore) {
		this.soggettoPagatore = soggettoPagatore;
	}

	/**
	 * Importo della pendenza. Deve corrispondere alla somma delle singole voci.
	 **/
	public PendenzaPost importo(BigDecimal importo) {
		this.importo = importo;
		return this;
	}

	@JsonProperty("importo")
	public BigDecimal getImporto() {
		return this.importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * Identificativo univoco versamento, assegnato se pagabile da psp
	 **/
	public PendenzaPost numeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
		return this;
	}

	@JsonProperty("numeroAvviso")
	public String getNumeroAvviso() {
		return this.numeroAvviso;
	}
	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}

	/**
	 * Data di emissione della pendenza
	 **/
	public PendenzaPost dataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
		return this;
	}

	@JsonProperty("dataCaricamento")
	public Date getDataCaricamento() {
		return this.dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}

	/**
	 * Data di validita dei dati della pendenza, decorsa la quale la pendenza può subire variazioni.
	 **/
	public PendenzaPost dataValidita(Date dataValidita) {
		this.dataValidita = dataValidita;
		return this;
	}

	@JsonProperty("dataValidita")
	public Date getDataValidita() {
		return this.dataValidita;
	}
	public void setDataValidita(Date dataValidita) {
		this.dataValidita = dataValidita;
	}

	/**
	 * Data di scadenza della pendenza, decorsa la quale non è più pagabile.
	 **/
	public PendenzaPost dataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
		return this;
	}

	@JsonProperty("dataScadenza")
	public Date getDataScadenza() {
		return this.dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * Anno di riferimento della pendenza
	 **/
	public PendenzaPost annoRiferimento(BigDecimal annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
		return this;
	}

	@JsonProperty("annoRiferimento")
	public BigDecimal getAnnoRiferimento() {
		return this.annoRiferimento;
	}
	public void setAnnoRiferimento(BigDecimal annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	/**
	 * Identificativo della cartella di pagamento a cui afferisce la pendenza
	 **/
	public PendenzaPost cartellaPagamento(String cartellaPagamento) {
		this.cartellaPagamento = cartellaPagamento;
		return this;
	}

	@JsonProperty("cartellaPagamento")
	public String getCartellaPagamento() {
		return this.cartellaPagamento;
	}
	public void setCartellaPagamento(String cartellaPagamento) {
		this.cartellaPagamento = cartellaPagamento;
	}

	/**
	 * Dati applicativi allegati dal gestionale secondo un formato proprietario.
	 **/
	public PendenzaPost datiAllegati(Object datiAllegati) {
		this.datiAllegati = datiAllegati;
		return this;
	}

	@JsonProperty("datiAllegati")
	public Object getDatiAllegati() {
		return this.datiAllegati;
	}
	public void setDatiAllegati(Object datiAllegati) {
		this.datiAllegati = datiAllegati;
	}

	/**
	 * Macro categoria della pendenza secondo la classificazione del creditore
	 **/
	public PendenzaPost tassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
		return this;
	}

	@JsonProperty("tassonomia")
	public String getTassonomia() {
		return this.tassonomia;
	}
	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}

	/**
	 **/
	public PendenzaPost tassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
		this.tassonomiaAvvisoEnum = tassonomiaAvviso;
		return this;
	}

	public TassonomiaAvviso getTassonomiaAvvisoEnum() {
		return this.tassonomiaAvvisoEnum;
	}
	public void setTassonomiaAvvisoEnum(TassonomiaAvviso tassonomiaAvviso) {
		this.tassonomiaAvvisoEnum = tassonomiaAvviso;
	}

	/**
	 **/
	public PendenzaPost tassonomiaAvviso(String tassonomiaAvviso) {
		this.setTassonomiaAvviso(tassonomiaAvviso);
		return this;
	}

	@JsonProperty("tassonomiaAvviso")
	public String getTassonomiaAvviso() {
		return this.tassonomiaAvviso;
	}
	public void setTassonomiaAvviso(String tassonomiaAvviso) {
		this.tassonomiaAvviso = tassonomiaAvviso;
	}

	/**
	 **/
	public PendenzaPost voci(List<VocePendenza> voci) {
		this.voci = voci;
		return this;
	}

	@JsonProperty("voci")
	public List<VocePendenza> getVoci() {
		return this.voci;
	}
	public void setVoci(List<VocePendenza> voci) {
		this.voci = voci;
	}

	/**
	 * Identificativo del gestionale responsabile della pendenza
	 **/
	public PendenzaPost idA2A(String idA2A) {
		this.idA2A = idA2A;
		return this;
	}

	@JsonProperty("idA2A")
	public String getIdA2A() {
		return this.idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}

	/**
	 * Identificativo della pendenza nel gestionale responsabile
	 **/
	public PendenzaPost idPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
		return this;
	}

	@JsonProperty("idPendenza")
	public String getIdPendenza() {
		return this.idPendenza;
	}
	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

  /**
   * Identificativo del soggetto debitore  della pendenza riferita dall'avviso
   **/
  public PendenzaPost idDebitore(String idDebitore) {
    this.idDebitore = idDebitore;
    return this;
  }

  @JsonProperty("idDebitore")
  public String getIdDebitore() {
    return idDebitore;
  }
  public void setIdDebitore(String idDebitore) {
    this.idDebitore = idDebitore;
  }

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		PendenzaPost pendenzaPost = (PendenzaPost) o;
		return Objects.equals(this.idDominio, pendenzaPost.idDominio) &&
				Objects.equals(this.idUnitaOperativa, pendenzaPost.idUnitaOperativa) &&
			    Objects.equals(this.nome, pendenzaPost.nome) &&
				Objects.equals(this.causale, pendenzaPost.causale) &&
				Objects.equals(this.soggettoPagatore, pendenzaPost.soggettoPagatore) &&
				Objects.equals(this.importo, pendenzaPost.importo) &&
				Objects.equals(this.numeroAvviso, pendenzaPost.numeroAvviso) &&
				Objects.equals(this.dataCaricamento, pendenzaPost.dataCaricamento) &&
				Objects.equals(this.dataValidita, pendenzaPost.dataValidita) &&
				Objects.equals(this.dataScadenza, pendenzaPost.dataScadenza) &&
				Objects.equals(this.annoRiferimento, pendenzaPost.annoRiferimento) &&
				Objects.equals(this.cartellaPagamento, pendenzaPost.cartellaPagamento) &&
				Objects.equals(this.datiAllegati, pendenzaPost.datiAllegati) &&
				Objects.equals(this.tassonomia, pendenzaPost.tassonomia) &&
				Objects.equals(this.tassonomiaAvviso, pendenzaPost.tassonomiaAvviso) &&
				Objects.equals(this.voci, pendenzaPost.voci) &&
				Objects.equals(this.idA2A, pendenzaPost.idA2A) &&
				Objects.equals(this.idPendenza, pendenzaPost.idPendenza) &&
				Objects.equals(idDebitore, pendenzaPost.idDebitore) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.idDominio, this.idUnitaOperativa, this.nome, this.causale, this.soggettoPagatore, this.importo, this.numeroAvviso, this.dataCaricamento, this.dataValidita, this.dataScadenza, this.annoRiferimento, this.cartellaPagamento, this.datiAllegati, this.tassonomia, this.tassonomiaAvviso, this.voci, this.idA2A, this.idPendenza, this.idDebitore);
	}

	public static PendenzaPost parse(String json) throws it.govpay.core.exceptions.IOException {
		return parse(json, PendenzaPost.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "pendenzaPost";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PendenzaPost {\n");

		sb.append("    idDominio: ").append(this.toIndentedString(this.idDominio)).append("\n");
		sb.append("    idUnitaOperativa: ").append(this.toIndentedString(this.idUnitaOperativa)).append("\n");
	    sb.append("    nome: ").append(this.toIndentedString(this.nome)).append("\n");
		sb.append("    causale: ").append(this.toIndentedString(this.causale)).append("\n");
		sb.append("    soggettoPagatore: ").append(this.toIndentedString(this.soggettoPagatore)).append("\n");
		sb.append("    importo: ").append(this.toIndentedString(this.importo)).append("\n");
		sb.append("    numeroAvviso: ").append(this.toIndentedString(this.numeroAvviso)).append("\n");
		sb.append("    dataCaricamento: ").append(this.toIndentedString(this.dataCaricamento)).append("\n");
		sb.append("    dataValidita: ").append(this.toIndentedString(this.dataValidita)).append("\n");
		sb.append("    dataScadenza: ").append(this.toIndentedString(this.dataScadenza)).append("\n");
		sb.append("    annoRiferimento: ").append(this.toIndentedString(this.annoRiferimento)).append("\n");
		sb.append("    cartellaPagamento: ").append(this.toIndentedString(this.cartellaPagamento)).append("\n");
		sb.append("    datiAllegati: ").append(this.toIndentedString(this.datiAllegati)).append("\n");
		sb.append("    tassonomia: ").append(this.toIndentedString(this.tassonomia)).append("\n");
		sb.append("    tassonomiaAvviso: ").append(this.toIndentedString(this.tassonomiaAvviso)).append("\n");
		sb.append("    voci: ").append(this.toIndentedString(this.voci)).append("\n");
		sb.append("    idA2A: ").append(this.toIndentedString(this.idA2A)).append("\n");
		sb.append("    idPendenza: ").append(this.toIndentedString(this.idPendenza)).append("\n");
		sb.append("    idDebitore: ").append(toIndentedString(idDebitore)).append("\n");
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

		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

		// Pendenza passata per riferimento idA2A/idPendenza
		if(this.idA2A != null && this.idDominio == null) {
			validatoreId.validaIdApplicazione("idA2A", this.idA2A);
			validatoreId.validaIdPendenza("idPendenza", this.idPendenza);
			try {
				vf.getValidator("idDominio", this.idDominio).isNull();
				vf.getValidator("idUnitaOperativa", this.idUnitaOperativa).isNull();
				vf.getValidator("nome", this.nome).isNull();
				vf.getValidator("causale", this.causale).isNull();
				vf.getValidator("soggettoPagatore", this.soggettoPagatore).isNull();
				vf.getValidator("importo", this.importo).isNull();
				vf.getValidator("numeroAvviso", this.numeroAvviso).isNull();
				vf.getValidator("dataValidita", this.dataValidita).isNull();
				vf.getValidator("dataScadenza", this.dataScadenza).isNull();
				vf.getValidator("annoRiferimento", this.annoRiferimento).isNull();
				vf.getValidator("cartellaPagamento", this.cartellaPagamento).isNull();
				vf.getValidator("voci", this.voci).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Pendenza riferita per identificativo A2A. " + ve.getMessage());
			}
			return;
		} else if(this.idA2A == null && this.idDominio != null) {
			vf.getValidator("numeroAvviso", this.numeroAvviso).notNull().pattern("[0-9]{18}");
			validatoreId.validaIdDominio("idDominio", this.idDominio);
			try {
				vf.getValidator("idUnitaOperativa", this.idUnitaOperativa).isNull();
				vf.getValidator("nome", this.nome).isNull();
				vf.getValidator("causale", this.causale).isNull();
				vf.getValidator("soggettoPagatore", this.soggettoPagatore).isNull();
				vf.getValidator("importo", this.importo).isNull();
				vf.getValidator("dataValidita", this.dataValidita).isNull();
				vf.getValidator("dataScadenza", this.dataScadenza).isNull();
				vf.getValidator("annoRiferimento", this.annoRiferimento).isNull();
				vf.getValidator("cartellaPagamento", this.cartellaPagamento).isNull();
				vf.getValidator("voci", this.voci).isNull();
				vf.getValidator("idA2A", this.idA2A).isNull();
				vf.getValidator("idPendenza", this.idPendenza).isNull();
			} catch (ValidationException ve) {
				throw new ValidationException("Pendenza riferita per numero avviso. " + ve.getMessage());
			}
		} else {

			validatoreId.validaIdApplicazione("idA2A", this.idA2A);
			validatoreId.validaIdPendenza("idPendenza", this.idPendenza);
			validatoreId.validaIdDominio("idDominio", this.idDominio);
			validatoreId.validaIdUO("idUnitaOperativa", this.idUnitaOperativa, false);

			ValidatoreUtils.validaNomePendenza(vf, "nome", nome);
			ValidatoreUtils.validaCausale(vf, "causale", causale);

			vf.getValidator("soggettoPagatore", this.soggettoPagatore).notNull().validateFields();

			ValidatoreUtils.validaImporto(vf, "importo", importo);
			ValidatoreUtils.validaNumeroAvviso(vf, "numeroAvviso", numeroAvviso);
			ValidatoreUtils.validaData(vf, "dataValidita", this.dataValidita);
			ValidatoreUtils.validaData(vf, "dataScadenza", this.dataScadenza);
			ValidatoreUtils.validaAnnoRiferimento(vf, "annoRiferimento", annoRiferimento);
			ValidatoreUtils.validaCartellaPagamento(vf, "cartellaPagamento", cartellaPagamento);
			ValidatoreUtils.validaTassonomia(vf, "tassonomia", tassonomia);

			vf.getValidator("voci", this.voci).notNull().minItems(1).maxItems(5).validateObjects();
		}
	}
}



