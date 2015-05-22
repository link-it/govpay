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

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "voci")
public class Voce extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String idPendenza;
//	private String idCondizione;
	private Condizione condPagamento;
	private String idVoce;
	private Timestamp tsDecorrenza;
	private String tiVoce;
	private String coVoce;
	private String deVoce;
	private BigDecimal imVoce;
	private String coCapBilancio;
	private String coAccertamento;
	private String stRiga;
	private int prVersione;

	public Voce() {
	}

	@Column(name = "ID_PENDENZA", nullable = false, length = 40)
	public String getIdPendenza() {
		return idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

//	@Column(name = "ID_CONDIZIONE", nullable = false, length = 40)
//	public String getIdCondizione() {
//		return idCondizione;
//	}
//
//	public void setIdCondizione(String idCondizione) {
//		this.idCondizione = idCondizione;
//	}

	@ManyToOne(targetEntity = Condizione.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CONDIZIONE", nullable = true)
	public Condizione getCondPagamento() {
		return condPagamento;
	}

	public void setCondPagamento(Condizione condPagamento) {
		this.condPagamento = condPagamento;
	}
	
	
	
	@Id
	@Column(name = "ID_VOCE", unique = true, nullable = false, length = 40)
	public String getIdVoce() {
		return idVoce;
	}

	public void setIdVoce(String idVoce) {
		this.idVoce = idVoce;
	}

	@Column(name = "TS_DECORRENZA", nullable = false)
	public Timestamp getTsDecorrenza() {
		return tsDecorrenza;
	}

	public void setTsDecorrenza(Timestamp tsDecorrenza) {
		this.tsDecorrenza = tsDecorrenza;
	}

	@Column(name = "TI_VOCE", nullable = false, length = 70)
	public String getTiVoce() {
		return tiVoce;
	}

	public void setTiVoce(String tiVoce) {
		this.tiVoce = tiVoce;
	}

	@Column(name = "CO_VOCE", nullable = false, length = 70)
	public String getCoVoce() {
		return coVoce;
	}

	public void setCoVoce(String coVoce) {
		this.coVoce = coVoce;
	}

	@Column(name = "DE_VOCE", nullable = false, length = 510)
	public String getDeVoce() {
		return deVoce;
	}

	public void setDeVoce(String deVoce) {
		this.deVoce = deVoce;
	}

	@Column(name = "IM_VOCE", nullable = false, precision = 15, scale = 2)
	public BigDecimal getImVoce() {
		return imVoce;
	}

	public void setImVoce(BigDecimal imVoce) {
		this.imVoce = imVoce;
	}

	@Column(name = "CO_CAPBILANCIO", length = 70)
	public String getCoCapBilancio() {
		return coCapBilancio;
	}

	public void setCoCapBilancio(String coCapBilancio) {
		this.coCapBilancio = coCapBilancio;
	}

	@Column(name = "CO_ACCERTAMENTO", length = 70)
	public String getCoAccertamento() {
		return coAccertamento;
	}

	public void setCoAccertamento(String coAccertamento) {
		this.coAccertamento = coAccertamento;
	}

	@Column(name = "ST_RIGA", nullable = false, length = 2)
	public String getStRiga() {
		return stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	@Column(name = "PR_VERSIONE", nullable = false)
	public int getPrVersione() {
		return prVersione;
	}

	public void setPrVersione(int prVersione) {
		this.prVersione = prVersione;
	}

}
