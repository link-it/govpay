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
package it.govpay.ejb.core.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Questa classe contiene l'esito della distinta ritornato. 
 * Viene ritornata dal gateway pagamento ed utilizzata per controaggiornare
 * la distinta.
 */
public class EsitoPagamentoDistinta {
	
	public EsitoPagamentoDistinta() {
		datiSingoliPagamenti = new ArrayList<EsitoSingoloPagamento>();
		importoTotalePagato = BigDecimal.ZERO;
	}
	
		
	public class EsitoSingoloPagamento {
		
		/**
		 * Id del pagamento noto all'ente. (deve corrispondere al campo CondizionePagamento) 
		 */
		private String idPagamentoEnte;
		
		/**
		 * Id della pendenza associata alla condizione 
		 */
		private String idPendenza;
		
		/**
		 * Identificativo della riscossione specifica da parte del PSP (In NDP: Identificativo Univoco Risccossione)
		 */
		private String idRiscossionePSP;
		
		/**
		 *  Data effettiva di esecuzione del pagamento
		 */
		private Date dataPagamento;
		
		/**
		 * Importo effetttivamente pagato
		 */
		private BigDecimal importoPagato;
		
		/**
		 * Descrizione testuale dell'esito
		 */
		private String descrizioneEsito;
		/**
		 * Stato del pagamento
		 */
		private PagamentoModel.EnumStatoPagamento stato;		
		/**
		 * Stato del pagamento
		 */
		public PagamentoModel.EnumStatoPagamento getStato() {
			return stato;
		}
		public void setStato(PagamentoModel.EnumStatoPagamento stato) {
			this.stato = stato;
		}
		/**
		 * Id del pagamento noto all'ente. (deve corrispondere al campo CondizionePagamento) 
		 */
		public String getIdPagamentoEnte() {
			return idPagamentoEnte;
		}
		public void setIdPagamentoEnte(String iusv) {
			this.idPagamentoEnte = iusv;
		}
		/**
		 *  Data effettiva di esecuzione del pagamento
		 */
		public Date getDataPagamento() {
			return dataPagamento;
		}
		public void setDataPagamento(Date dataPagamento) {
			this.dataPagamento = dataPagamento;
		}
		/**
		 * Importo effetttivamente pagato
		 */
		public BigDecimal getImportoPagato() {
			return importoPagato;
		}
		public void setImportoPagato(BigDecimal importoPagato) {
			this.importoPagato = importoPagato;
		}
		/**
		 * Identificativo della riscossione specifica da parte del PSP (In NDP: Identificativo Univoco Risccossione)
		 */
		public String getIdRiscossionePSP() {
			return idRiscossionePSP;
		}
		public void setIdRiscossionePSP(String iur) {
			this.idRiscossionePSP = iur;
		}
		/**
		 * Descrizione testuale dell'esito
		 */
		public String getDescrizioneEsito() {
			return descrizioneEsito;
		}
		public void setDescrizioneEsito(String descrizioneEsito) {
			this.descrizioneEsito = descrizioneEsito;
		}
		public String getIdPendenza() {
			return idPendenza;
		}
		public void setIdPendenza(String idPendenza) {
			this.idPendenza = idPendenza;
		}
	}
	
	/**
	 * Data ora ricezione messaggio ricevuta
	 */
	private Date dataOraMessaggio;
	
	/**
	 * Identificativo messaggio Ricevuta
	 */
	private String identificativoMessaggio;
	
	/**
	 * Identificativo transazione PSP, quando applicabile, (per NDP: è il codice contesto pagamento)
	 */
	private String idTransazionePSP;
	
	/**
	 * Identificativo Univoco di versamento della distinta.
	 */
	private String iuv;
	
	/**
	 * Codice fiscale dell'ente creditore
	 */
	private String identificativoFiscaleCreditore;
	
	/**
	 * EnumStatoOperazione della distinta complessivo.
	 */
	private DistintaModel.EnumStatoDistinta stato;
	
	/**
	 * Importo totale transato
	 */
	private BigDecimal importoTotalePagato;
	
	/**
	 * Esiti dei singoli pagamenti della distinta.
	 */
	private List<EsitoSingoloPagamento> datiSingoliPagamenti;
	
	/**
	 * Descrizione dello stato a livello di distinta originale, così come arriva dal PSP
	 */
	private String descrizioneStatoPSP;
	
	
	public Date getDataOraMessaggio() {
		return dataOraMessaggio;
	}
	public void setDataOraMessaggio(Date dataOraMessaggio) {
		this.dataOraMessaggio = dataOraMessaggio;
	}
	public String getIdentificativoMessaggio() {
		return identificativoMessaggio;
	}
	public void setIdentificativoMessaggio(String identificativoMessaggio) {
		this.identificativoMessaggio = identificativoMessaggio;
	}
	public String getIdTransazionePSP() {
		return idTransazionePSP;
	}
	public void setIdTransazionePSP(String ccp) {
		this.idTransazionePSP = ccp;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getIdentificativoFiscaleCreditore() {
		return identificativoFiscaleCreditore;
	}
	public void setIdentificativoFiscaleCreditore(String identificativoCreditore) {
		this.identificativoFiscaleCreditore = identificativoCreditore;
	}
	public DistintaModel.EnumStatoDistinta getStato() {
		return stato;
	}
	public void setStato(DistintaModel.EnumStatoDistinta stato) {
		this.stato = stato;
	}
	public BigDecimal getImportoTotalePagato() {
		return importoTotalePagato;
	}
	public void setImportoTotalePagato(BigDecimal importoTotalePagato) {
		this.importoTotalePagato = importoTotalePagato;
	}
	public List<EsitoSingoloPagamento> getDatiSingoliPagamenti() {
		return datiSingoliPagamenti;
	}
	public void setDatiSingoliPagamenti(List<EsitoSingoloPagamento> datiSingoliPagamenti) {
		this.datiSingoliPagamenti = datiSingoliPagamenti;
	}
	public String getDescrizioneStatoPSP() {
		return descrizioneStatoPSP;
	}
	public void setDescrizioneStatoPSP(String descrizioneStatoPSP) {
		this.descrizioneStatoPSP = descrizioneStatoPSP;
	}

}
