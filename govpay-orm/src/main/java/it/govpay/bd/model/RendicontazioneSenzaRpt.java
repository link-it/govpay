/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.SingoliVersamentiBD;

import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

public class RendicontazioneSenzaRpt extends BasicModel {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private long idFrApplicazioni;
	private long idIuv;
	private Long idSingoloVersamento;
	private Date dataRendicontazione;
	private String iur;
	private BigDecimal importoPagato;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdFrApplicazioni() {
		return idFrApplicazioni;
	}
	public void setIdFrApplicazioni(long idFrApplicazioni) {
		this.idFrApplicazioni = idFrApplicazioni;
	}
	public long getIdIuv() {
		return idIuv;
	}
	public void setIdIuv(long idIuv) {
		this.idIuv = idIuv;
	}
	public Date getDataRendicontazione() {
		return dataRendicontazione;
	}
	public void setDataRendicontazione(Date dataRendicontazione) {
		this.dataRendicontazione = dataRendicontazione;
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public BigDecimal getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	public Long getIdSingoloVersamento() {
		return idSingoloVersamento;
	}
	public void setIdSingoloVersamento(Long idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}

	// Business
	
	private SingoloVersamento singoloVersamento;
	
	public SingoloVersamento getSingoloVersamento(BasicBD bd) throws ServiceException {
		if(singoloVersamento == null) {
			SingoliVersamentiBD singoliVersamentiBD = new SingoliVersamentiBD(bd);
			singoloVersamento = singoliVersamentiBD.getSingoloVersamento(idSingoloVersamento);
		}
		return singoloVersamento;
	}
	
	private Iuv iuv;
	
	public Iuv getIuv(BasicBD bd) throws ServiceException {
		if(iuv == null) {
			IuvBD iuvBD = new IuvBD(bd);
			iuv = iuvBD.getIuv(this.idIuv);
		}
		return iuv;
	}
}
