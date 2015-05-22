/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.orm.pagamenti;

import it.govpay.orm.BaseEntity;
import it.govpay.orm.configurazione.CfgGatewayPagamento;
import it.govpay.orm.posizionedebitoria.CondizioneDocumento;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "documenti_pagamento")
//@NamedQueries({
//	@NamedQuery(name = "countByIdAndStatus", query = "select count(docs) from DocumentoDiPagamento docs "
//			+ "where docs.id=:codiceAutorizzazione and docs.stato=:statoDocumento "),
//	@NamedQuery(name = "getDocumentoDiPagamentoByIdDocumento", query = "select docs from DocumentoDiPagamento docs "
//			+ "where docs.id=:codiceAutorizzazione") 
//})
public class DocumentoDiPagamento extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/*** Persistent Properties ***/
	private String id;
	private String opAnnullamento;
	private String intestatario;
	private String stato;
	private Timestamp tsAnnullamento;
	private Timestamp tsEmissione;
	private BigDecimal importo;
	private BigDecimal importoCommissioni;
	private DistintaPagamento distinta;
	private CfgGatewayPagamento cfgGatewayPagamento;
	private Set<CondizioneDocumento> condizioni;
	private Long idDocumentoRepository;
	private Date dtScadenzaDoc;
	private String codFiscalePagante;
	private String emailVersante;

	@Id
	@Column(unique = true, nullable = false, length = 82)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "OP_ANNULLAMENTO", length = 160)
	public String getOpAnnullamento() {
		return this.opAnnullamento;
	}

	public void setOpAnnullamento(String opAnnullamento) {
		this.opAnnullamento = opAnnullamento;
	}

	@Column(nullable = false, length = 2)
	public String getStato() {
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Column(name = "TS_ANNULLAMENTO")
	public Timestamp getTsAnnullamento() {
		return this.tsAnnullamento;
	}

	public void setTsAnnullamento(Timestamp tsAnnullamento) {
		this.tsAnnullamento = tsAnnullamento;
	}

	@Column(name = "TS_EMISSIONE", nullable = false)
	public Timestamp getTsEmissione() {
		return this.tsEmissione;
	}

	public void setTsEmissione(Timestamp tsEmissione) {
		this.tsEmissione = tsEmissione;
	}

	// bi-directional many-to-one association to CondizioneDocumento
	@OneToMany(mappedBy = "documento", cascade = { CascadeType.ALL })
	public Set<CondizioneDocumento> getCondizioni() {
		return this.condizioni;
	}

	public void setCondizioni(Set<CondizioneDocumento> condizioni) {
		this.condizioni = condizioni;
	}

	public String getIntestatario() {
		return intestatario;
	}

	public void setIntestatario(String intestatario) {
		this.intestatario = intestatario;
	}

	@ManyToOne(targetEntity = CfgGatewayPagamento.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CFG_GATEWAY_PAGAMENTO")
	public CfgGatewayPagamento getCfgGatewayPagamento() {
		return cfgGatewayPagamento;
	}

	public void setCfgGatewayPagamento(CfgGatewayPagamento cfgGatewayPagamento) {
		this.cfgGatewayPagamento = cfgGatewayPagamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentoDiPagamento other = (DocumentoDiPagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DocumentoDiPagamento [id=");
		builder.append(id);
		builder.append(", opAggiornamento=");
		builder.append(getOpAggiornamento());
		builder.append(", opAnnullamento=");
		builder.append(opAnnullamento);
		builder.append(", opInserimento=");
		builder.append(getOpInserimento());
		builder.append(", stato=");
		builder.append(stato);
		builder.append(", tipo=");
		builder.append(", intestatario=");
		builder.append(intestatario);
		builder.append(", tsAggiornamento=");
		builder.append(getTsAggiornamento());
		builder.append(", tsAnnullamento=");
		builder.append(tsAnnullamento);
		builder.append(", tsEmissione=");
		builder.append(tsEmissione);
		builder.append(", tsInserimento=");
		builder.append(getTsInserimento());
		builder.append(", condizioni=");
		builder.append(condizioni);
		builder.append(", getVersion()=");
		builder.append(getVersion());
		builder.append("]");
		return builder.toString();
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DISTINTA_PAGAMENTO")
	public DistintaPagamento getDistinta() {
		return distinta;
	}

	public void setDistinta(DistintaPagamento distinta) {
		this.distinta = distinta;
	}


	@Column(name = "IMPORTO")
	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	@Column(name = "IMPORTO_COMMISSIONI")
	public BigDecimal getImportoCommissioni() {
		return importoCommissioni;
	}

	public void setImportoCommissioni(BigDecimal importoCommissioni) {
		this.importoCommissioni = importoCommissioni;
	}

	@Column(name = "ID_DOCUMENTO_REPOSITORY")
	public Long getIdDocumentoRepository() {
		return idDocumentoRepository;
	}

	public void setIdDocumentoRepository(Long idDocumentoRepository) {
		this.idDocumentoRepository = idDocumentoRepository;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_SCADENZA_DOC")
	public Date getDtScadenzaDoc() {
		return dtScadenzaDoc;
	}

	public void setDtScadenzaDoc(Date dtScadenzaDoc) {
		this.dtScadenzaDoc = dtScadenzaDoc;
	}

	@Column(name = "CO_PAGANTE")
	public String getCodFiscalePagante() {
		return codFiscalePagante;
	}

	public void setCodFiscalePagante(String codFiscalePagante) {
		this.codFiscalePagante = codFiscalePagante;
	}

	@Column(name = "EMAIL_VERSANTE", nullable = false)
	public String getEmailVersante() {
		return emailVersante;
	}

	public void setEmailVersante(String emailVersante) {
		this.emailVersante = emailVersante;
	}

	public static class TsEmissioneComparatorAsc implements Comparator<DocumentoDiPagamento> {
		@Override
		public int compare(DocumentoDiPagamento doc0, DocumentoDiPagamento doc1) {
			return doc0.tsEmissione.compareTo(doc1.tsEmissione);
		}
	}

	public static class TsEmissioneComparatorDesc implements Comparator<DocumentoDiPagamento> {
		@Override
		public int compare(DocumentoDiPagamento doc0, DocumentoDiPagamento doc1) {
			return doc1.tsEmissione.compareTo(doc0.tsEmissione);
		}
	}
}