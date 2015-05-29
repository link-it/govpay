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
package it.govpay.web.console.pagamenti.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.SoggettoModel;
import it.govpay.ejb.core.model.TributoModel;

public class PagamentoModel {
	
	public class DettaglioPagamento {
		private String identificativo;
		private BigDecimal importoDovuto;
		private BigDecimal importoVersato;
		private String causale;
		private int annoRiferimento;
		private String ibanAccredito;
		private Date dataRicevuta;
		private String stato;
		private String iur;
		
		public String getIdentificativo() {
			return identificativo;
		}
		public void setIdentificativo(String identificativo) {
			this.identificativo = identificativo;
		}
		public BigDecimal getImportoDovuto() {
			return importoDovuto;
		}
		public void setImportoDovuto(BigDecimal importoDovuto) {
			this.importoDovuto = importoDovuto;
		}
		public BigDecimal getImportoVersato() {
			return importoVersato;
		}
		public void setImportoVersato(BigDecimal importoVersato) {
			this.importoVersato = importoVersato;
		}
		public String getCausale() {
			return causale;
		}
		public void setCausale(String causale) {
			this.causale = causale;
		}
		public int getAnnoRiferimento() {
			return annoRiferimento;
		}
		public void setAnnoRiferimento(int annoRiferimento) {
			this.annoRiferimento = annoRiferimento;
		}
		public String getIbanAccredito() {
			return ibanAccredito;
		}
		public void setIbanAccredito(String ibanAccredito) {
			this.ibanAccredito = ibanAccredito;
		}
		public Date getDataRicevuta() {
			return dataRicevuta;
		}
		public void setDataRicevuta(Date dataRicevuta) {
			this.dataRicevuta = dataRicevuta;
		}
		public String getStato() {
			return stato;
		}
		public void setStato(String stato) {
			this.stato = stato;
		}
		public String getIur() {
			return iur;
		}
		public void setIur(String iur) {
			this.iur = iur;
		}

	}

	private GatewayPagamentoModel gatewayPagamento;
	private EnteCreditoreModel enteCreditore;
	private TributoModel tributo;
	private String iuv;
	private String ccp;
	private String stato;
	private String autenticazione;
	private String tipoFirma;
	private String ibanAddebito;
	private SoggettoModel debitore;
	private SoggettoModel versante;
	private BigDecimal importoDovuto;
	private BigDecimal importoVersato;
	private byte[] rpt;
	private byte[] rt;
	private List<DettaglioPagamento> dettagliPagamento;
	
	public GatewayPagamentoModel getGatewayPagamento() {
		return gatewayPagamento;
	}
	public void setGatewayPagamento(GatewayPagamentoModel gatewayPagamento) {
		this.gatewayPagamento = gatewayPagamento;
	}
	public EnteCreditoreModel getEnteCreditore() {
		return enteCreditore;
	}
	public void setEnteCreditore(EnteCreditoreModel enteCreditore) {
		this.enteCreditore = enteCreditore;
	}
	public TributoModel getTributo() {
		return tributo;
	}
	public void setTributo(TributoModel tributo) {
		this.tributo = tributo;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getAutenticazione() {
		return autenticazione;
	}
	public void setAutenticazione(String autenticazione) {
		this.autenticazione = autenticazione;
	}
	public String getTipoFirma() {
		return tipoFirma;
	}
	public void setTipoFirma(String tipoFirma) {
		this.tipoFirma = tipoFirma;
	}
	public String getIbanAddebito() {
		return ibanAddebito;
	}
	public void setIbanAddebito(String ibanAddebito) {
		this.ibanAddebito = ibanAddebito;
	}
	public SoggettoModel getDebitore() {
		return debitore;
	}
	public void setDebitore(SoggettoModel debitore) {
		this.debitore = debitore;
	}
	public SoggettoModel getVersante() {
		return versante;
	}
	public void setVersante(SoggettoModel versante) {
		this.versante = versante;
	}
	public BigDecimal getImportoDovuto() {
		return importoDovuto;
	}
	public void setImportoDovuto(BigDecimal importoDovuto) {
		this.importoDovuto = importoDovuto;
	}
	public BigDecimal getImportoVersato() {
		return importoVersato;
	}
	public void setImportoVersato(BigDecimal importoVersato) {
		this.importoVersato = importoVersato;
	}
	public List<DettaglioPagamento> getDettagliPagamento() {
		return dettagliPagamento;
	}
	public void setDettagliPagamento(List<DettaglioPagamento> dettagliPagamento) {
		this.dettagliPagamento = dettagliPagamento;
	}
	public byte[] getRpt() {
		return rpt;
	}
	public void setRpt(byte[] rpt) {
		this.rpt = rpt;
	}
	public byte[] getRt() {
		return rt;
	}
	public void setRt(byte[] rt) {
		this.rt = rt;
	}

}
