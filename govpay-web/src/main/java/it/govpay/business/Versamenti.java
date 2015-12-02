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
package it.govpay.business;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;

public class Versamenti {

	private BasicBD bd;
	Logger log = LogManager.getLogger();
	
	public Versamenti(BasicBD bd) {
		this.bd = bd;
	}

	public String generaIuv(long idApplicazione, String codDominio, TipoIUV type, int auxDigit) throws GovPayException {
		
		log.info("Generazione IUV [idApplicazione: " + idApplicazione + "][CodDominio: " + codDominio + "][IuvType: " + type + "]");
		try {
			bd.setAutoCommit(false);
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Stazione stazione = AnagraficaManager.getStazione(bd, AnagraficaManager.getApplicazione(bd, idApplicazione).getIdStazione()); 
			
			Iuv iuv = versamentiBD.generaIuv(idApplicazione, auxDigit, stazione.getApplicationCode(), codDominio,  type);
			bd.commit();	
			
			log.info("Generato iuv " + iuv.getIuv());
			
			return iuv.getIuv();

		} catch (Exception e) {
			bd.rollback();
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} 
	}


	/**
	 * Aggiorna o inserisce un pagamento nel repository dei pagamenti in attesa.
	 * 
	 * Se il pagamento non esiste, lo inserisce in stato "DA_PAGARE"
	 * Se il pagamento esiste in stato "DA_PAGARE", lo aggiorna 
	 * Se il pagamento esiste in stato diverso da "DA_PAGARE", ritorna ERRORE_AUTORIZZAZIONE
	 * 
	 * @param ente
	 * @param applicazione
	 * @param versamento
	 * @throws GovPayException
	 */
	public void caricaPagamento(Versamento versamento) throws GovPayException {
	
		// Controllo che lo IUV indicato sia stato generato per applicazione richiedente
		Autorizzazione autorizzazione = new Autorizzazione(bd);
		autorizzazione.authApplicazione(versamento.getIdApplicazione(), versamento.getCodDominio(), versamento.getIuv());
		
		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			autorizzazione.authApplicazione(versamento.getIdApplicazione(), sv.getIdTributo());
		}
		
		// Controllo se il versamento e' gia' nel db. Se non esiste lo inserisco come DA_PAGARE e esco
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamentoOld = null;
		
		try {
			bd.setAutoCommit(false);
			try{
				versamentoOld = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getIuv());
			} catch (NotFoundException nfe) {
				versamento.setStato(StatoVersamento.IN_ATTESA);
				versamentiBD.insertVersamento(versamento);
				bd.commit();
				return;
			}
			
			// Il Versamento esiste. 
			// Se lo stato e' DA_PAGARE, lo aggiorno
			if(versamentoOld.getStato().equals(StatoVersamento.IN_ATTESA)) {
				versamento.setId(versamentoOld.getId());
				versamentiBD.replaceVersamento(versamento);
				bd.commit();
			} else {
				// Lo stato non consente un aggiornamento dei dati.
				throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE, "Non e' consentito aggiornare un pagamento in stato \"" + versamentoOld.getStato() + "\"");
			}
		} catch(Exception e) {
			bd.rollback();
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}

	/**
	 * Annulla un pagamento nel repository dei pagamenti in attesa.
	 * 
	 * Se il pagamento non esiste, ritorna IUV_NON_TROVATO
	 * Se il pagamento esiste in stato "DA_PAGARE", lo annulla 
	 * Se il pagamento esiste in stato "ANNULLATO", non fa niente 
	 * Se il pagamento esiste in stato diverso da "DA_PAGARE" o "ANNULLATO", ritorna ERRORE_AUTORIZZAZIONE
	 * 
	 * @param applicazione
	 * @param iuv
	 * @throws GovPayException
	 */
	public void annullaPagamento(long idApplicazione, String codDominio, String iuv) throws GovPayException {
		// Controllo che lo IUV indicato sia stato generato per applicazione richiedente
		Autorizzazione autorizzazione = new Autorizzazione(bd);
		autorizzazione.authApplicazione(idApplicazione, codDominio, iuv);
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamento = null;
		
		try {
			bd.setAutoCommit(false);
			try{
				versamento = versamentiBD.getVersamento(idApplicazione, iuv);
				if(versamento.getStato().equals(StatoVersamento.IN_ATTESA)) {
					versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ANNULLATO);
					bd.commit();
					return;
				} else if(versamento.getStato().equals(StatoVersamento.ANNULLATO)) {
					bd.commit();
					return;
				} else {
					bd.commit();
					throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE, "Non e' consentito annullare un pagamento in stato diverso da \"DA_PAGARE\"");
				}
			} catch (NotFoundException nfe) {
				throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
			}
		} catch(Exception e) {
			bd.rollback();
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
}
