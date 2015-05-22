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
package it.govpay.orm.posizionedebitoria;

import it.govpay.orm.BaseEntity;
import it.govpay.orm.pagamenti.Pagamento;
import it.govpay.orm.profilazione.Ente;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the JLTCOPD database table.
 */
@Entity
@Table(name = "condizioni")
public class Condizione extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String idCondizione;
	private String cdTrbEnte;
	private String coCip;
	private String deCanalepag;
	private Date dtFinevalidita;
	private Date dtIniziovalidita;
	private Timestamp dtPagamento;
	private Date dtScadenza;
	private String idPagamento;
	private BigDecimal imTotale;
	private String opAnnullamento;
	private int prVersione;
	private String stPagamento;
	private String stRiga;
	private String tiCip;
	private String tiPagamento;
	private Timestamp tsAnnullamento;
	private Timestamp tsDecorrenza;
	private String causalePagamento;
	private String ibanBeneficiario;
	private String ragioneSocaleBeneficiario;
	private BigDecimal imPagamento;
	private String deNotePagamento;
	private String deMezzoPagamento;
	private Ente ente;
	private Pendenza pendenza;
	private Set<Pagamento> pagamento;
	private List<Voce> vociPagamento;
	private Set<CondizioneDocumento> condizioniDocumento;
	private Long tsAnnullamentoMillis = 0L;

	@Id
	@Column(name = "ID_CONDIZIONE", unique = true, nullable = false, length = 40)
	public String getIdCondizione() {
		return this.idCondizione;
	}

	public void setIdCondizione(String idCondizione) {
		this.idCondizione = idCondizione;
	}

	@Column(name = "CD_TRB_ENTE", nullable = false, length = 100)
	public String getCdTrbEnte() {
		return this.cdTrbEnte;
	}

	public void setCdTrbEnte(String cdTrbEnte) {
		this.cdTrbEnte = cdTrbEnte;
	}

	@Column(name = "CO_CIP", length = 512)
	public String getCoCip() {
		return this.coCip;
	}

	public void setCoCip(String coCip) {
		this.coCip = coCip;
	}

	@Column(name = "DE_CANALEPAG", length = 280)
	public String getDeCanalepag() {
		return this.deCanalepag;
	}

	public void setDeCanalepag(String deCanalepag) {
		this.deCanalepag = deCanalepag;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_FINEVALIDITA", nullable = false)
	public Date getDtFinevalidita() {
		return this.dtFinevalidita;
	}

	public void setDtFinevalidita(Date dtFinevalidita) {
		this.dtFinevalidita = dtFinevalidita;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INIZIOVALIDITA")
	public Date getDtIniziovalidita() {
		return this.dtIniziovalidita;
	}

	public void setDtIniziovalidita(Date dtIniziovalidita) {
		this.dtIniziovalidita = dtIniziovalidita;
	}

	@Column(name = "DT_PAGAMENTO")
	public Timestamp getDtPagamento() {
		return this.dtPagamento;
	}

	public void setDtPagamento(Timestamp dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_SCADENZA", nullable = false)
	public Date getDtScadenza() {
		return this.dtScadenza;
	}

	public void setDtScadenza(Date dtScadenza) {
		this.dtScadenza = dtScadenza;
	}

	@Column(name = "ID_PAGAMENTO", nullable = false, length = 70)
	public String getIdPagamento() {
		return this.idPagamento;
	}

	public void setIdPagamento(String idPagamento) {
		this.idPagamento = idPagamento;
	}

	@Column(name = "IM_TOTALE", nullable = false, precision = 15, scale = 2)
	public BigDecimal getImTotale() {
		return this.imTotale;
	}

	public void setImTotale(BigDecimal imTotale) {
		this.imTotale = imTotale;
	}

	@Column(name = "OP_ANNULLAMENTO", length = 80)
	public String getOpAnnullamento() {
		return this.opAnnullamento;
	}

	public void setOpAnnullamento(String opAnnullamento) {
		this.opAnnullamento = opAnnullamento;
	}

	@Column(name = "PR_VERSIONE", nullable = false)
	public int getPrVersione() {
		return this.prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

	@Column(name = "ST_PAGAMENTO", nullable = false, length = 2)
	public String getStPagamento() {
		return this.stPagamento;
	}

	public void setStPagamento(String stPagamento) {
		this.stPagamento = stPagamento;
	}

	@Column(name = "ST_RIGA", nullable = false, length = 2)
	public String getStRiga() {
		return this.stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	@Column(name = "TI_CIP", length = 20)
	public String getTiCip() {
		return this.tiCip;
	}

	public void setTiCip(String tiCip) {
		this.tiCip = tiCip;
	}

	@Column(name = "TI_PAGAMENTO", nullable = false, length = 2)
	public String getTiPagamento() {
		return this.tiPagamento;
	}

	public void setTiPagamento(String tiPagamento) {
		this.tiPagamento = tiPagamento;
	}

	@Column(name = "TS_ANNULLAMENTO")
	public Timestamp getTsAnnullamento() {
		return this.tsAnnullamento;
	}

	public void setTsAnnullamento(Timestamp tsAnnullamento) {
		this.tsAnnullamento = tsAnnullamento;
		// Mantiene allineato ts_annullamentoMillis con ts_annullamento
		if (this.tsAnnullamento != null) {
			this.tsAnnullamentoMillis = this.tsAnnullamento.getTime();
		} else {
			this.tsAnnullamentoMillis = 0L;
		}
	}

	@Column(name = "TS_DECORRENZA", nullable = false)
	public Timestamp getTsDecorrenza() {
		return this.tsDecorrenza;
	}

	public void setTsDecorrenza(Timestamp tsDecorrenza) {
		this.tsDecorrenza = tsDecorrenza;
	}

	// uni-directional many-to-one association to Ente
	@ManyToOne
	@JoinColumn(name = "ID_ENTE", nullable = false)
	public Ente getEnte() {
		return this.ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	// bi-directional many-to-one association to Pendenza
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PENDENZA", nullable = false)
	public Pendenza getPendenza() {
		return this.pendenza;
	}

	public void setPendenza(Pendenza pendenza) {
		this.pendenza = pendenza;
	}

	@Column(name = "CAUSALE_PAGAMENTO")
	public String getCausalePagamento() {
		return causalePagamento;
	}

	public void setCausalePagamento(String causalePagamento) {
		this.causalePagamento = causalePagamento;
	}

	@Column(name = "IBAN_BENEFICIARIO")
	public String getIbanBeneficiario() {
		return ibanBeneficiario;
	}

	public void setIbanBeneficiario(String ibanBeneficiario) {
		this.ibanBeneficiario = ibanBeneficiario;
	}

	@Column(name = "RAGIONE_SOCIALE_BENEFICIARIO")
	public String getRagioneSocaleBeneficiario() {
		return ragioneSocaleBeneficiario;
	}

	public void setRagioneSocaleBeneficiario(String ragioneSocaleBeneficiario) {
		this.ragioneSocaleBeneficiario = ragioneSocaleBeneficiario;
	}

	@Column(name = "IM_PAGAMENTO")
	public BigDecimal getImPagamento() {
		return imPagamento;
	}

	public void setImPagamento(BigDecimal imPagamento) {
		this.imPagamento = imPagamento;
	}

	@Column(name = "DE_NOTEPAGAMENTO")
	public String getDeNotePagamento() {
		return deNotePagamento;
	}

	public void setDeNotePagamento(String deNotePagamento) {
		this.deNotePagamento = deNotePagamento;
	}

	@Column(name = "DE_MEZZOPAGAMENTO")
	public String getDeMezzoPagamento() {
		return deMezzoPagamento;
	}

	public void setDeMezzoPagamento(String deMezzoPagamento) {
		this.deMezzoPagamento = deMezzoPagamento;
	}

	@Column(name = "TS_ANNULLAMENTO_MILLIS")
	public Long getTsAnnullamentoMillis() {
		return tsAnnullamentoMillis;
	}

	public void setTsAnnullamentoMillis(Long tsAnnullamentoMillis) {
		this.tsAnnullamentoMillis = tsAnnullamentoMillis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCondizione == null) ? 0 : idCondizione.hashCode());
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
		Condizione other = (Condizione) obj;
		if (idCondizione == null) {
			if (other.idCondizione != null)
				return false;
		} else if (!idCondizione.equals(other.idCondizione))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Condizione [idCondizione=");
		builder.append(idCondizione);
		builder.append(", cdTrbEnte=");
		builder.append(cdTrbEnte);
		builder.append(", coCip=");
		builder.append(coCip);
		builder.append(", deCanalepag=");
		builder.append(deCanalepag);
		builder.append(", dtFinevalidita=");
		builder.append(dtFinevalidita);
		builder.append(", dtIniziovalidita=");
		builder.append(dtIniziovalidita);
		builder.append(", dtPagamento=");
		builder.append(dtPagamento);
		builder.append(", dtScadenza=");
		builder.append(dtScadenza);
		builder.append(", idPagamento=");
		builder.append(idPagamento);
		builder.append(", idPendenza=");
		builder.append(", imTotale=");
		builder.append(imTotale);
		builder.append(", opAggiornamento=");
		builder.append(getOpAggiornamento());
		builder.append(", opAnnullamento=");
		builder.append(opAnnullamento);
		builder.append(", opInserimento=");
		builder.append(getOpInserimento());
		builder.append(", prVersione=");
		builder.append(prVersione);
		builder.append(", stPagamento=");
		builder.append(stPagamento);
		builder.append(", stRiga=");
		builder.append(stRiga);
		builder.append(", tiCip=");
		builder.append(tiCip);
		builder.append(", tiPagamento=");
		builder.append(tiPagamento);
		builder.append(", tsAggiornamento=");
		builder.append(getTsAggiornamento());
		builder.append(", tsAnnullamento=");
		builder.append(tsAnnullamento);
		builder.append(", tsDecorrenza=");
		builder.append(tsDecorrenza);
		builder.append(", tsInserimento=");
		builder.append(getTsInserimento());
		builder.append(", condizioniDocumento=");
		builder.append(", getVersion()=");
		builder.append(getVersion());
		builder.append("]");
		return builder.toString();
	}

	@OneToMany(mappedBy = "condPagamento", fetch = FetchType.LAZY)
	@OrderBy("tsInserimento ASC")
	public Set<Pagamento> getPagamenti() {
		return pagamento;
	}

	public void setPagamenti(Set<Pagamento> pagamento) {
		this.pagamento = pagamento;
	}

	@OneToMany(mappedBy = "condPagamento", fetch = FetchType.LAZY)
	@OrderBy(value = "idVoce")
	public List<Voce> getVociPagamento() {
		return vociPagamento;
	}

	public void setVociPagamento(List<Voce> vociPagamento) {
		this.vociPagamento = vociPagamento;
	}

	@OneToMany(mappedBy = "condizionePagamento", fetch = FetchType.LAZY)
	public Set<CondizioneDocumento> getCondizioniDocumento() {
		return condizioniDocumento;
	}

	public void setCondizioniDocumento(Set<CondizioneDocumento> condizioniDocumento) {
		this.condizioniDocumento = condizioniDocumento;
	}

	public static class DtScadenzaComparator implements Comparator<Condizione> {
		@Override
		public int compare(Condizione cond0, Condizione cond1) {
			return cond0.dtScadenza.compareTo(cond1.dtScadenza);
		}
	}

}