/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.model;

import java.math.BigDecimal;
import java.util.Date;


public class Pagamento extends BasicModel {

	private static final long serialVersionUID = 1L;

	public enum TipoPagamento {
		ENTRATA, MBT
	}
	
	public enum TipoAllegato {
		ES, BD
	}
	
	public enum Stato {
		PAGATO, INCASSATO, PAGATO_SENZA_RPT
	}

	private Long id;
	private String codDominio;
	private String iuv;
	private String iur;
	private int indiceDati;
	
	private Stato stato;
	private TipoPagamento tipo;
	
	private BigDecimal importoPagato;
	private Date dataAcquisizione;
	private Date dataPagamento;
	private BigDecimal commissioniPsp;
	private TipoAllegato tipoAllegato;
	private byte[] allegato;
	
	private Long idRpt;
	private Long idSingoloVersamento;
	private Long idRr;
	private Long idIncasso;
	
	private Date dataAcquisizioneRevoca;
	private String causaleRevoca;
	private String datiRevoca;
	private String esitoRevoca;
	private String datiEsitoRevoca;
	private BigDecimal importoRevocato;

	public Pagamento() {
		super();
		this.dataAcquisizione = new Date();
		this.stato = Stato.PAGATO;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdRpt() {
		return this.idRpt;
	}

	public void setIdRpt(long idRpt) {
		this.idRpt = idRpt;
	}

	public Long getIdSingoloVersamento() {
		return this.idSingoloVersamento;
	}

	public void setIdSingoloVersamento(Long idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}

	public BigDecimal getImportoPagato() {
		return this.importoPagato;
	}

	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}

	public String getIur() {
		return this.iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public Date getDataPagamento() {
		return this.dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public TipoAllegato getTipoAllegato() {
		return this.tipoAllegato;
	}

	public void setTipoAllegato(TipoAllegato tipoAllegato) {
		this.tipoAllegato = tipoAllegato;
	}

	public byte[] getAllegato() {
		return this.allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
	}

	public BigDecimal getCommissioniPsp() {
		return this.commissioniPsp;
	}

	public void setCommissioniPsp(BigDecimal commissioniPsp) {
		this.commissioniPsp = commissioniPsp;
	}

	public Long getIdRr() {
		return this.idRr;
	}

	public void setIdRr(Long idRr) {
		this.idRr = idRr;
	}

	public String getCausaleRevoca() {
		return this.causaleRevoca;
	}

	public void setCausaleRevoca(String causaleRevoca) {
		this.causaleRevoca = causaleRevoca;
	}

	public String getDatiRevoca() {
		return this.datiRevoca;
	}

	public void setDatiRevoca(String datiRevoca) {
		this.datiRevoca = datiRevoca;
	}

	public String getEsitoRevoca() {
		return this.esitoRevoca;
	}

	public void setEsitoRevoca(String esitoRevoca) {
		this.esitoRevoca = esitoRevoca;
	}

	public String getDatiEsitoRevoca() {
		return this.datiEsitoRevoca;
	}

	public void setDatiEsitoRevoca(String datiEsitoRevoca) {
		this.datiEsitoRevoca = datiEsitoRevoca;
	}

	public BigDecimal getImportoRevocato() {
		return this.importoRevocato;
	}

	public void setImportoRevocato(BigDecimal importoRevocato) {
		this.importoRevocato = importoRevocato;
	}

	public Date getDataAcquisizione() {
		return this.dataAcquisizione;
	}

	public void setDataAcquisizione(Date dataAcquisizione) {
		this.dataAcquisizione = dataAcquisizione;
	}

	public Date getDataAcquisizioneRevoca() {
		return this.dataAcquisizioneRevoca;
	}

	public void setDataAcquisizioneRevoca(Date dataAcquisizioneRevoca) {
		this.dataAcquisizioneRevoca = dataAcquisizioneRevoca;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public Stato getStato() {
		return this.stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public Long getIdIncasso() {
		return this.idIncasso;
	}

	public void setIdIncasso(Long idIncasso) {
		this.idIncasso = idIncasso;
	}

	public int getIndiceDati() {
		return this.indiceDati;
	}

	public void setIndiceDati(int indiceDati) {
		this.indiceDati = indiceDati;
	}

	public TipoPagamento getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoPagamento tipo) {
		this.tipo = tipo;
	}

}

