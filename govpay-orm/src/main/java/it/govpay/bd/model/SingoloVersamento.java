/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class SingoloVersamento extends BasicModel implements Comparable<SingoloVersamento> {
	private static final long serialVersionUID = 1L;

	public enum StatoSingoloVersamento {
		DA_PAGARE,
		PAGATO,
		RENDICONTATO;
	}


	private Long id;
	private long idTributo;
	private int indice;
	private String codSingoloVersamentoEnte;
	private String ibanAccredito;
	private BigDecimal importoSingoloVersamento;
	private BigDecimal singoloImportoPagato;
	private BigDecimal importoCommissioniPA;
	private String causaleVersamento;
	private String datiSpecificiRiscossione;
	private String esitoSingoloPagamento;
	private String iur;
	private Date dataEsitoSingoloPagamento;
	private StatoSingoloVersamento statoSingoloVersamento;
	private Integer annoRiferimento;
	@JsonIgnore
	private Versamento versamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdTributo() {
		return idTributo;
	}

	public void setIdTributo(long idTributo) {
		this.idTributo = idTributo;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}


	public String getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public BigDecimal getImportoSingoloVersamento() {
		return importoSingoloVersamento;
	}

	public void setImportoSingoloVersamento(BigDecimal importoSingoloVersamento) {
		this.importoSingoloVersamento = importoSingoloVersamento;
	}

	public BigDecimal getImportoCommissioniPA() {
		return importoCommissioniPA;
	}

	public void setImportoCommissioniPA(BigDecimal importoCommissioniPA) {
		this.importoCommissioniPA = importoCommissioniPA;
	}

	public String getCausaleVersamento() {
		return causaleVersamento;
	}

	public void setCausaleVersamento(String causaleVersamento) {
		this.causaleVersamento = causaleVersamento;
	}

	public String getDatiSpecificiRiscossione() {
		return datiSpecificiRiscossione;
	}

	public void setDatiSpecificiRiscossione(String datiSpecificiRiscossione) {
		this.datiSpecificiRiscossione = datiSpecificiRiscossione;
	}

	public String getEsitoSingoloPagamento() {
		return esitoSingoloPagamento;
	}

	public void setEsitoSingoloPagamento(String esitoSingoloPagamento) {
		this.esitoSingoloPagamento = esitoSingoloPagamento;
	}

	public String getIur() {
		return iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public Date getDataEsitoSingoloPagamento() {
		return dataEsitoSingoloPagamento;
	}

	public void setDataEsitoSingoloPagamento(
			Date dataEsitoSingoloPagamento) {
		this.dataEsitoSingoloPagamento = dataEsitoSingoloPagamento;
	}

	public StatoSingoloVersamento getStatoSingoloVersamento() {
		return statoSingoloVersamento;
	}

	public void setStatoSingoloVersamento(
			StatoSingoloVersamento statoSingoloVersamento) {
		this.statoSingoloVersamento = statoSingoloVersamento;
	}

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public BigDecimal getSingoloImportoPagato() {
		return singoloImportoPagato;
	}

	public void setSingoloImportoPagato(BigDecimal singoloImportoPagato) {
		this.singoloImportoPagato = singoloImportoPagato;
	}

	@Override
	public boolean equals(Object obj) {
		SingoloVersamento sv = null;
		if(obj instanceof SingoloVersamento) {
			sv = (SingoloVersamento) obj;
		}
		else {
			return false;
		}
		boolean equal = 
				equals(codSingoloVersamentoEnte, sv.getCodSingoloVersamentoEnte()) &&
				equals(ibanAccredito, sv.getIbanAccredito()) &&
				equals(importoSingoloVersamento, sv.getImportoSingoloVersamento()) &&
				equals(singoloImportoPagato, sv.getSingoloImportoPagato()) &&
				equals(importoCommissioniPA, sv.getImportoCommissioniPA()) &&
				equals(causaleVersamento, sv.getCausaleVersamento()) &&
				equals(datiSpecificiRiscossione, sv.getDatiSpecificiRiscossione()) &&
				equals(esitoSingoloPagamento, sv.getEsitoSingoloPagamento()) &&
				equals(iur, sv.getIur()) &&
				equals(dataEsitoSingoloPagamento, sv.getDataEsitoSingoloPagamento()) &&
				equals(statoSingoloVersamento, sv.getStatoSingoloVersamento()) &&
				equals(annoRiferimento, sv.getAnnoRiferimento()) &&
				idTributo == sv.getIdTributo() &&
				indice == sv.getIndice();
		return equal;
	}

	@Override
	public int compareTo(SingoloVersamento sv) {
		return new Integer(indice).compareTo(new Integer(sv.getIndice()));
	}

	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

}

