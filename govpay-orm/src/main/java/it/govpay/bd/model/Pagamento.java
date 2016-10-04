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
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.model.BasicModel;
import it.govpay.model.Rpt;
import it.govpay.model.Rr;
import it.govpay.model.SingoloVersamento;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Pagamento extends BasicModel {

	public enum EsitoRendicontazione {
		ESEGUITO(0), REVOCATO(3), ESEGUITO_SENZA_RPT(9);

		private int codifica;

		EsitoRendicontazione(int codifica) {
			this.codifica = codifica;
		}

		public int getCodifica() {
			return codifica;
		}

		public static EsitoRendicontazione toEnum(String codifica) throws ServiceException {
			return toEnum(Integer.parseInt(codifica));
		}

		public static EsitoRendicontazione toEnum(int codifica) throws ServiceException {
			for(EsitoRendicontazione p : EsitoRendicontazione.values()){
				if(p.getCodifica() == codifica)
					return p;
			}
			throw new ServiceException("Codifica inesistente per EsitoRendicontazione. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoRendicontazione.values()));
		}
	}

	private static final long serialVersionUID = 1L;

	public enum TipoAllegato {
		ES, BD
	}

	private Long id;
	private long idRpt;
	private long idSingoloVersamento;
	private String codSingoloVersamentoEnte;
	private BigDecimal importoPagato;
	private BigDecimal commissioniPsp;
	private String iur;
	private Date dataPagamento;
	private String ibanAccredito;
	private TipoAllegato tipoAllegato;
	private byte[] allegato;

	private Long idFrApplicazione;
	private EsitoRendicontazione esitoRendicontazione;
	private Date dataRendicontazione;
	private Integer annoRiferimento;
	private String codFlussoRendicontazione;
	private Integer indice;
	private Date dataAcquisizione;

	private Long idRr;
	private String causaleRevoca;
	private String datiRevoca;
	private String esitoRevoca;
	private String datiEsitoRevoca;
	private BigDecimal importoRevocato;

	private Long idFrApplicazioneRevoca;
	private EsitoRendicontazione esitoRendicontazioneRevoca;
	private Date dataRendicontazioneRevoca;
	private String codFlussoRendicontazioneRevoca;
	private Integer annoRiferimentoRevoca;
	private Integer indiceRevoca;
	private Date dataAcquisizioneRevoca;


	public Pagamento() {
		super();
		this.dataAcquisizione = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdRpt() {
		return idRpt;
	}

	public void setIdRpt(long idRpt) {
		this.idRpt = idRpt;
	}

	public long getIdSingoloVersamento() {
		return idSingoloVersamento;
	}

	public void setIdSingoloVersamento(long idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}

	public BigDecimal getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}

	public String getIur() {
		return iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public TipoAllegato getTipoAllegato() {
		return tipoAllegato;
	}

	public void setTipoAllegato(TipoAllegato tipoAllegato) {
		this.tipoAllegato = tipoAllegato;
	}

	public byte[] getAllegato() {
		return allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
	}

	public EsitoRendicontazione getEsitoRendicontazione() {
		return esitoRendicontazione;
	}

	public void setEsitoRendicontazione(EsitoRendicontazione esitoRendicontazione) {
		this.esitoRendicontazione = esitoRendicontazione;
	}

	public Date getDataRendicontazione() {
		return dataRendicontazione;
	}

	public void setDataRendicontazione(Date dataRendicontazione) {
		this.dataRendicontazione = dataRendicontazione;
	}

	public String getCodFlussoRendicontazione() {
		return codFlussoRendicontazione;
	}

	public void setCodFlussoRendicontazione(String codFlussoRendicontazione) {
		this.codFlussoRendicontazione = codFlussoRendicontazione;
	}

	public BigDecimal getCommissioniPsp() {
		return commissioniPsp;
	}

	public void setCommissioniPsp(BigDecimal commissioniPsp) {
		this.commissioniPsp = commissioniPsp;
	}

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public Integer getIndice() {
		return indice;
	}

	public void setIndice(Integer indice) {
		this.indice = indice;
	}

	public Long getIdRr() {
		return idRr;
	}

	public void setIdRr(Long idRr) {
		this.idRr = idRr;
	}

	public String getCausaleRevoca() {
		return causaleRevoca;
	}

	public void setCausaleRevoca(String causaleRevoca) {
		this.causaleRevoca = causaleRevoca;
	}

	public String getDatiRevoca() {
		return datiRevoca;
	}

	public void setDatiRevoca(String datiRevoca) {
		this.datiRevoca = datiRevoca;
	}

	public String getEsitoRevoca() {
		return esitoRevoca;
	}

	public void setEsitoRevoca(String esitoRevoca) {
		this.esitoRevoca = esitoRevoca;
	}

	public String getDatiEsitoRevoca() {
		return datiEsitoRevoca;
	}

	public void setDatiEsitoRevoca(String datiEsitoRevoca) {
		this.datiEsitoRevoca = datiEsitoRevoca;
	}

	public BigDecimal getImportoRevocato() {
		return importoRevocato;
	}

	public void setImportoRevocato(BigDecimal importoRevocato) {
		this.importoRevocato = importoRevocato;
	}

	public Long getIdFrApplicazioneRevoca() {
		return idFrApplicazioneRevoca;
	}

	public void setIdFrApplicazioneRevoca(Long idFrApplicazioneRevoca) {
		this.idFrApplicazioneRevoca = idFrApplicazioneRevoca;
	}

	public EsitoRendicontazione getEsitoRendicontazioneRevoca() {
		return esitoRendicontazioneRevoca;
	}

	public void setEsitoRendicontazioneRevoca(EsitoRendicontazione esitoRendicontazioneRevoca) {
		this.esitoRendicontazioneRevoca = esitoRendicontazioneRevoca;
	}

	public Date getDataRendicontazioneRevoca() {
		return dataRendicontazioneRevoca;
	}

	public void setDataRendicontazioneRevoca(Date dataRendicontazioneRevoca) {
		this.dataRendicontazioneRevoca = dataRendicontazioneRevoca;
	}

	public String getCodFlussoRendicontazioneRevoca() {
		return codFlussoRendicontazioneRevoca;
	}

	public void setCodFlussoRendicontazioneRevoca(String codFlussoRendicontazioneRevoca) {
		this.codFlussoRendicontazioneRevoca = codFlussoRendicontazioneRevoca;
	}

	public Integer getAnnoRiferimentoRevoca() {
		return annoRiferimentoRevoca;
	}

	public void setAnnoRiferimentoRevoca(Integer annoRiferimentoRevoca) {
		this.annoRiferimentoRevoca = annoRiferimentoRevoca;
	}

	public Integer getIndiceRevoca() {
		return indiceRevoca;
	}

	public void setIndiceRevoca(Integer indiceRevoca) {
		this.indiceRevoca = indiceRevoca;
	}

	public Date getDataAcquisizione() {
		return dataAcquisizione;
	}

	public void setDataAcquisizione(Date dataAcquisizione) {
		this.dataAcquisizione = dataAcquisizione;
	}

	public Date getDataAcquisizioneRevoca() {
		return dataAcquisizioneRevoca;
	}

	public void setDataAcquisizioneRevoca(Date dataAcquisizioneRevoca) {
		this.dataAcquisizioneRevoca = dataAcquisizioneRevoca;
	}

	// Business

	private Rpt rpt;
	private SingoloVersamento singoloVersamento;
	private Rr rr;

	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(rpt == null) {
			RptBD rptBD = new RptBD(bd);
			rpt = rptBD.getRpt(idRpt);
		}
		return rpt;
	}

	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.idRpt = rpt.getId();
	}

	public Rr getRr(BasicBD bd) throws ServiceException {
		if(rr == null) {
			RrBD rrBD = new RrBD(bd);
			rr = rrBD.getRr(idRr);
		}
		return rr;
	}

	public void setRr(Rr rr) {
		this.rr = rr;
		this.idRr = rr.getId();
	}

	public SingoloVersamento getSingoloVersamento(BasicBD bd) throws ServiceException {
		if(singoloVersamento == null) {
			VersamentiBD singoliVersamentiBD = new VersamentiBD(bd);
			singoloVersamento = singoliVersamentiBD.getSingoloVersamento(idSingoloVersamento);
		}
		return singoloVersamento;
	}

	public void setSingoloVersamento(SingoloVersamento singoloVersamento) {
		this.singoloVersamento = singoloVersamento;
		this.idSingoloVersamento = singoloVersamento.getId();
	}

	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public Long getIdFrApplicazione() {
		return idFrApplicazione;
	}

	public void setIdFrApplicazione(Long idFrApplicazione) {
		this.idFrApplicazione = idFrApplicazione;
	}

	public String getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}



}

