package it.govpay.core.dao.operazioni;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTO;
import it.govpay.core.dao.operazioni.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTO;
import it.govpay.core.dao.operazioni.dto.ListaOperazioniDTOResponse;
import it.govpay.core.dao.operazioni.exception.OperazioneNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class OperazioniDAO extends BaseDAO{
	
	public final static String ACQUISIZIONE_RENDICONTAZIONI = "acquisizioneRendicontazioni";
	public final static String AGGIORNAMENTO_REGISTRO_PSP = "aggiornamentoRegistroPsp";
	public final static String RECUPERO_RPT_PENDENTI = "recuperoRptPendenti";
	public final static String SPEDIZIONE_NOTIFICHE = "spedizioneNotifiche";
	public final static String RESET_CACHE_ANAGRAFICA = "resetCacheAnagrafica";
	public final static String GENERAZIONE_AVVISI_PAGAMENTO = "generaAvvisiPagamento";
	public final static String ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO = "attivazioneGenerazioneAvvisiPagamento";

	public LeggiOperazioneDTOResponse eseguiOperazione(LeggiOperazioneDTO leggiOperazioneDTO) throws ServiceException, OperazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiOperazioneDTOResponse response = new LeggiOperazioneDTOResponse();
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiOperazioneDTO.getUser(), Servizio.CONFIGURAZIONE_E_MANUTENZIONE, Diritti.ESECUZIONE, bd);
			
			String esitoOperazione = "";
			if(leggiOperazioneDTO.getIdOperazione().equals(ACQUISIZIONE_RENDICONTAZIONI)){
				esitoOperazione = it.govpay.core.business.Operazioni.acquisizioneRendicontazioni(OperazioniDAO.class.getName());
			} else if(leggiOperazioneDTO.getIdOperazione().equals(RECUPERO_RPT_PENDENTI)){
				esitoOperazione = it.govpay.core.business.Operazioni.recuperoRptPendenti(OperazioniDAO.class.getName());
			} else if(leggiOperazioneDTO.getIdOperazione().equals(RESET_CACHE_ANAGRAFICA)){
				esitoOperazione = it.govpay.core.business.Operazioni.resetCacheAnagrafica();
			} else if(leggiOperazioneDTO.getIdOperazione().equals(SPEDIZIONE_NOTIFICHE)){
				esitoOperazione = it.govpay.core.business.Operazioni.spedizioneNotifiche(OperazioniDAO.class.getName());
			}else if(leggiOperazioneDTO.getIdOperazione().equals(GENERAZIONE_AVVISI_PAGAMENTO)){
				it.govpay.core.business.Operazioni.setEseguiGenerazioneAvvisi();
				esitoOperazione = "Generazione Avvisi Pagamento schedulata";
			}else if(leggiOperazioneDTO.getIdOperazione().equals(ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO)){
				it.govpay.core.business.Operazioni.setEseguiGenerazioneAvvisi();
				esitoOperazione = "Generazione Avvisi Pagamento schedulata";
			}else {
				throw new NotFoundException("Operazione "+leggiOperazioneDTO.getIdOperazione()+" sconosciuta");
			}
			
			response.setDescrizioneStato(esitoOperazione);
			response.setStato(0);
			response.setNome(leggiOperazioneDTO.getIdOperazione()); 
		} catch (NotFoundException e) {
			throw new OperazioneNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public ListaOperazioniDTOResponse listaOperazioni(ListaOperazioniDTO listaOperazioniDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException {
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaOperazioniDTO.getUser(), Servizio.CONFIGURAZIONE_E_MANUTENZIONE, Diritti.LETTURA, bd);
			List<LeggiOperazioneDTOResponse> results = new ArrayList<LeggiOperazioneDTOResponse>();
			
			results.add(new LeggiOperazioneDTOResponse(AGGIORNAMENTO_REGISTRO_PSP));
			results.add(new LeggiOperazioneDTOResponse(ACQUISIZIONE_RENDICONTAZIONI));
			results.add(new LeggiOperazioneDTOResponse(RECUPERO_RPT_PENDENTI));
			results.add(new LeggiOperazioneDTOResponse(SPEDIZIONE_NOTIFICHE));
			results.add(new LeggiOperazioneDTOResponse(RESET_CACHE_ANAGRAFICA));
			results.add(new LeggiOperazioneDTOResponse(GENERAZIONE_AVVISI_PAGAMENTO));
			results.add(new LeggiOperazioneDTOResponse(ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO));
			
			return new ListaOperazioniDTOResponse(results .size(), results);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
}
