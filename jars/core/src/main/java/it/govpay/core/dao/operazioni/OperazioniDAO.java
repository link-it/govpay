/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.operazioni;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;

import it.govpay.bd.BasicBD;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTO;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTOResponse;
import it.govpay.core.dao.operazioni.exception.OperazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class OperazioniDAO extends BaseDAO{
	
	public static final String ACQUISIZIONE_RENDICONTAZIONI = "acquisizioneRendicontazioni";
	public static final String SPEDIZIONE_NOTIFICHE = "spedizioneNotifiche";
	public static final String RESET_CACHE_ANAGRAFICA = "resetCacheAnagrafica";
	public static final String GENERAZIONE_AVVISI_PAGAMENTO = "generaAvvisiPagamento";
	public static final String ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO = "attivazioneGenerazioneAvvisiPagamento";
	public static final String ELABORAZIONE_TRACCIATI_PENDENZE = "elaborazioneTracciatiPendenze";
	public static final String SPEDIZIONE_PROMEMORIA = "spedizionePromemoria";
	public static final String SPEDIZIONE_NOTIFICHE_APP_IO = "spedizioneNotificheAppIO";
	public static final String GESTIONE_PROMEMORIA = "gestionePromemoria";
	public static final String ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "elaborazioneTracciatiNotificaPagamenti";
	public static final String SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "spedizioneTracciatiNotificaPagamenti";
	public static final String ELABORAZIONE_RICONCILIAZIONI = "elaborazioneRiconciliazioni";
	public static final String CHIUSURA_RPT_SCADUTE = "chiusuraRptScadute";
	public static final String RECUPERO_RT = "recuperoRT";

	public LeggiOperazioneDTOResponse eseguiOperazione(LeggiOperazioneDTO leggiOperazioneDTO) throws OperazioneNonTrovataException{
		LeggiOperazioneDTOResponse response = new LeggiOperazioneDTOResponse();
		
		log.info("Richiesta operazione [{}]...", leggiOperazioneDTO.getIdOperazione());
		
		try {
			IContext ctx = ContextThreadLocal.get();
			String esitoOperazione = "";
			if(leggiOperazioneDTO.getIdOperazione().equals(ACQUISIZIONE_RENDICONTAZIONI)){
				esitoOperazione = it.govpay.core.business.Operazioni.acquisizioneRendicontazioni(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(CHIUSURA_RPT_SCADUTE)){
				esitoOperazione = it.govpay.core.business.Operazioni.chiusuraRptScadute(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(RESET_CACHE_ANAGRAFICA)){
				esitoOperazione = it.govpay.core.business.Operazioni.resetCacheAnagrafica(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(SPEDIZIONE_NOTIFICHE)){
				esitoOperazione = it.govpay.core.business.Operazioni.spedizioneNotifiche(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(SPEDIZIONE_NOTIFICHE_APP_IO)){
				esitoOperazione = it.govpay.core.business.Operazioni.spedizioneNotificheAppIO(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(SPEDIZIONE_PROMEMORIA)){
				esitoOperazione = it.govpay.core.business.Operazioni.spedizionePromemoria(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(GESTIONE_PROMEMORIA)){
				esitoOperazione = it.govpay.core.business.Operazioni.gestionePromemoria(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(GENERAZIONE_AVVISI_PAGAMENTO) ||
					leggiOperazioneDTO.getIdOperazione().equals(ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO)){
				it.govpay.core.business.Operazioni.setEseguiGenerazioneAvvisi();
				esitoOperazione = "Generazione Avvisi Pagamento schedulata";
			} else if(leggiOperazioneDTO.getIdOperazione().equals(ELABORAZIONE_TRACCIATI_PENDENZE)){
				it.govpay.core.business.Operazioni.setEseguiElaborazioneTracciati();
				esitoOperazione = "Elaborazione Tacciati schedulata";
			} else if(leggiOperazioneDTO.getIdOperazione().equals(ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI)){
				it.govpay.core.business.Operazioni.setEseguiElaborazioneTracciatiNotificaPagamenti();
				esitoOperazione = "Elaborazione Tacciati Notifica Pagamenti schedulata";
			} else if(leggiOperazioneDTO.getIdOperazione().equals(SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI)){
				it.govpay.core.business.Operazioni.setEseguiInvioTracciatiNotificaPagamenti();
				esitoOperazione = "Spedizione Tacciati Notifica Pagamenti schedulata";
			} else if(leggiOperazioneDTO.getIdOperazione().equals(ELABORAZIONE_RICONCILIAZIONI)){
				it.govpay.core.business.Operazioni.setEseguiElaborazioneRiconciliazioni();
				esitoOperazione = "Elaborazione Riconciliazioni schedulata";
			} else if(leggiOperazioneDTO.getIdOperazione().equals(RECUPERO_RT)){
				it.govpay.core.business.Operazioni.setEseguiRecuperoRT();
				esitoOperazione = "Recupero RT schedulato";
			} else {
				throw new NotFoundException("Operazione "+leggiOperazioneDTO.getIdOperazione()+" sconosciuta");
			}
			
			log.info("Operazione [{}] completata con esito [{}]", leggiOperazioneDTO.getIdOperazione(), esitoOperazione);
			
			response.setDescrizioneStato(esitoOperazione);
			response.setStato(0);
			response.setNome(leggiOperazioneDTO.getIdOperazione()); 
		} catch (NotFoundException e) {
			throw new OperazioneNonTrovataException(e.getMessage(), e);
		} 
		return response;
	}

	public ListaOperazioniDTOResponse listaOperazioni(ListaOperazioniDTO listaOperazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException {
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			List<LeggiOperazioneDTOResponse> results = new ArrayList<>();
			
			results.add(new LeggiOperazioneDTOResponse(ACQUISIZIONE_RENDICONTAZIONI));
			results.add(new LeggiOperazioneDTOResponse(CHIUSURA_RPT_SCADUTE));
			results.add(new LeggiOperazioneDTOResponse(GESTIONE_PROMEMORIA));
			results.add(new LeggiOperazioneDTOResponse(SPEDIZIONE_NOTIFICHE));
			results.add(new LeggiOperazioneDTOResponse(SPEDIZIONE_NOTIFICHE_APP_IO));
			results.add(new LeggiOperazioneDTOResponse(SPEDIZIONE_PROMEMORIA));
			results.add(new LeggiOperazioneDTOResponse(RESET_CACHE_ANAGRAFICA));
			results.add(new LeggiOperazioneDTOResponse(GENERAZIONE_AVVISI_PAGAMENTO));
			results.add(new LeggiOperazioneDTOResponse(ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO));
			results.add(new LeggiOperazioneDTOResponse(ELABORAZIONE_TRACCIATI_PENDENZE));
			results.add(new LeggiOperazioneDTOResponse(ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI));
			results.add(new LeggiOperazioneDTOResponse(SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI));
			results.add(new LeggiOperazioneDTOResponse(ELABORAZIONE_RICONCILIAZIONI));
			results.add(new LeggiOperazioneDTOResponse(RECUPERO_RT));
			
			return new ListaOperazioniDTOResponse((long) results.size(), results);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
}
