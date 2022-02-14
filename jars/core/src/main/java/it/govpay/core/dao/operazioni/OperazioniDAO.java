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
	
	public final static String ACQUISIZIONE_RENDICONTAZIONI = "acquisizioneRendicontazioni";
	public final static String RECUPERO_RPT_PENDENTI = "recuperoRptPendenti";
	public final static String SPEDIZIONE_NOTIFICHE = "spedizioneNotifiche";
	public final static String RESET_CACHE_ANAGRAFICA = "resetCacheAnagrafica";
	public final static String GENERAZIONE_AVVISI_PAGAMENTO = "generaAvvisiPagamento";
	public final static String ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO = "attivazioneGenerazioneAvvisiPagamento";
	public final static String ELABORAZIONE_TRACCIATI_PENDENZE = "elaborazioneTracciatiPendenze";
	public final static String SPEDIZIONE_PROMEMORIA = "spedizionePromemoria";
	public final static String SPEDIZIONE_NOTIFICHE_APP_IO = "spedizioneNotificheAppIO";
	public final static String GESTIONE_PROMEMORIA = "gestionePromemoria";
	public final static String ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "elaborazioneTracciatiNotificaPagamenti";
	public final static String SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "spedizioneTracciatiNotificaPagamenti";
	public final static String ELABORAZIONE_RICONCILIAZIONI = "elaborazioneRiconciliazioni";

	public LeggiOperazioneDTOResponse eseguiOperazione(LeggiOperazioneDTO leggiOperazioneDTO) throws ServiceException, OperazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiOperazioneDTOResponse response = new LeggiOperazioneDTOResponse();
		
		try {
			IContext ctx = ContextThreadLocal.get();
			String esitoOperazione = "";
			if(leggiOperazioneDTO.getIdOperazione().equals(ACQUISIZIONE_RENDICONTAZIONI)){
				esitoOperazione = it.govpay.core.business.Operazioni.acquisizioneRendicontazioni(ctx);
			} else if(leggiOperazioneDTO.getIdOperazione().equals(RECUPERO_RPT_PENDENTI)){
				esitoOperazione = it.govpay.core.business.Operazioni.recuperoRptPendenti(ctx);
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
			} else if(leggiOperazioneDTO.getIdOperazione().equals(GENERAZIONE_AVVISI_PAGAMENTO)){
				it.govpay.core.business.Operazioni.setEseguiGenerazioneAvvisi();
				esitoOperazione = "Generazione Avvisi Pagamento schedulata";
			} else if(leggiOperazioneDTO.getIdOperazione().equals(ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO)){
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
			} else {
				throw new NotFoundException("Operazione "+leggiOperazioneDTO.getIdOperazione()+" sconosciuta");
			}
			
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
			results.add(new LeggiOperazioneDTOResponse(RECUPERO_RPT_PENDENTI));
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
			
			return new ListaOperazioniDTOResponse((long) results.size(), results);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
}
