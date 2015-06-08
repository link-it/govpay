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
public class EsitoRevocaDistinta {
	
	public EsitoRevocaDistinta() {
		datiSingoleRevoche = new ArrayList<EsitoSingolaRevoca>();
		importoTotaleRevocato = BigDecimal.ZERO;
	}
	
		
	public class EsitoSingolaRevoca {
		
		/**
		 * Id del pagamento noto all'ente. (deve corrispondere al campo CondizionePagamento) 
		 */
		private String idPagamentoEnte;
		
		/**
		 * Identificativo della riscossione specifica da parte del PSP (In NDP: Identificativo Univoco Risccossione)
		 */
		private String idRiscossionePSP;
		
		/**
		 * Importo effetttivamente revocato
		 */
		private BigDecimal importoRevocato;
		
		/**
		 * Stato del pagamento
		 */
		private RevocaDistintaModel.EnumStatoRevoca stato;
		
		/**
		 * Importo effetttivamente revocato
		 */
		private String causaleEsito;
		
		/**
		 * Importo effetttivamente revocato
		 */
		private String datiAggiuntiviEsito;

		public String getIdPagamentoEnte() {
			return idPagamentoEnte;
		}

		public void setIdPagamentoEnte(String idPagamentoEnte) {
			this.idPagamentoEnte = idPagamentoEnte;
		}

		public String getIdRiscossionePSP() {
			return idRiscossionePSP;
		}

		public void setIdRiscossionePSP(String idRiscossionePSP) {
			this.idRiscossionePSP = idRiscossionePSP;
		}

		public BigDecimal getImportoRevocato() {
			return importoRevocato;
		}

		public void setImportoRevocato(BigDecimal importoRevocato) {
			this.importoRevocato = importoRevocato;
		}

		public RevocaDistintaModel.EnumStatoRevoca getStato() {
			return stato;
		}

		public void setStato(RevocaDistintaModel.EnumStatoRevoca stato) {
			this.stato = stato;
		}

		public String getCausaleEsito() {
			return causaleEsito;
		}

		public void setCausaleEsito(String causaleEsito) {
			this.causaleEsito = causaleEsito;
		}

		public String getDatiAggiuntiviEsito() {
			return datiAggiuntiviEsito;
		}

		public void setDatiAggiuntiviEsito(String datiAggiuntiviEsito) {
			this.datiAggiuntiviEsito = datiAggiuntiviEsito;
		}
		
	}
	
	/**
	 * Data ora ricezione messaggio ricevuta
	 */
	private Date dataOraEsitoRevoca;
	
	/**
	 * Identificativo messaggio Ricevuta
	 */
	private String identificativoEsitoRevoca;
	
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
	private RevocaDistintaModel.EnumStatoRevoca stato;
	
	/**
	 * Importo totale transato
	 */
	private BigDecimal importoTotaleRevocato;
	
	/**
	 * Esiti dei singoli pagamenti della distinta.
	 */
	private List<EsitoSingolaRevoca> datiSingoleRevoche;
	
	/**
	 * Descrizione dello stato a livello di distinta originale, così come arriva dal PSP
	 */
	private String descrizioneStatoPSP;

	public Date getDataOraEsitoRevoca() {
		return dataOraEsitoRevoca;
	}

	public void setDataOraEsitoRevoca(Date dataOraEsitoRevoca) {
		this.dataOraEsitoRevoca = dataOraEsitoRevoca;
	}

	public String getIdentificativoEsitoRevoca() {
		return identificativoEsitoRevoca;
	}

	public void setIdentificativoEsitoRevoca(String identificativoEsitoRevoca) {
		this.identificativoEsitoRevoca = identificativoEsitoRevoca;
	}

	public String getIdTransazionePSP() {
		return idTransazionePSP;
	}

	public void setIdTransazionePSP(String idTransazionePSP) {
		this.idTransazionePSP = idTransazionePSP;
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

	public void setIdentificativoFiscaleCreditore(
			String identificativoFiscaleCreditore) {
		this.identificativoFiscaleCreditore = identificativoFiscaleCreditore;
	}

	public RevocaDistintaModel.EnumStatoRevoca getStato() {
		return stato;
	}

	public void setStato(RevocaDistintaModel.EnumStatoRevoca stato) {
		this.stato = stato;
	}

	public BigDecimal getImportoTotaleRevocato() {
		return importoTotaleRevocato;
	}

	public void setImportoTotaleRevocato(BigDecimal importoTotaleRevocato) {
		this.importoTotaleRevocato = importoTotaleRevocato;
	}

	public List<EsitoSingolaRevoca> getDatiSingoleRevoche() {
		return datiSingoleRevoche;
	}

	public void setDatiSingoleRevoche(List<EsitoSingolaRevoca> datiSingoleRevoche) {
		this.datiSingoleRevoche = datiSingoleRevoche;
	}

	public String getDescrizioneStatoPSP() {
		return descrizioneStatoPSP;
	}

	public void setDescrizioneStatoPSP(String descrizioneStatoPSP) {
		this.descrizioneStatoPSP = descrizioneStatoPSP;
	}
	
}
