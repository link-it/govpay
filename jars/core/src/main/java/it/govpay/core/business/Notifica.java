package it.govpay.core.business;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Versionabile;

public class Notifica {
	
	private static Logger log = LoggerWrapperFactory.getLogger(Notifica.class);

	public Notifica() {
	}

	public boolean inserisciNotifica(it.govpay.bd.model.Notifica notifica, BasicBD bd) throws ServiceException {
		// prima di inserire le notifiche controllo che l'applicazione da utilizzare abbia il connettore per le notifiche abilitato, altrimenti non ha senso inserire.
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Applicazione applicazione = notifica.getApplicazione(configWrapper);
		
		TipoNotifica tipo = notifica.getTipo();
		
		if(applicazione.getConnettoreIntegrazione() != null) {
			
			// la versione V2 delle api client prevede solamente l'invio delle notifiche di tipo ricevuta
			if(!tipo.equals(TipoNotifica.RICEVUTA) && !applicazione.getConnettoreIntegrazione().getVersione().equals(Versionabile.Versione.GP_REST_01)) {
				log.debug(MessageFormat.format("Inserimento notifica RPT[{0}] di tipo [{1}] non effettuato, la versione [{2}] del connettore integrazione per l''applicazione [{3}] non prevede la spedizione di questa tipo di notifica.",
						notifica.getRptKey(), tipo, applicazione.getConnettoreIntegrazione().getVersione(), applicazione.getCodApplicazione()));
				return false;
			}
			
			NotificheBD notificheBD = null;
			if(bd == null) {
				notificheBD = new NotificheBD(configWrapper);
			} else {
				notificheBD = new NotificheBD(bd);
				notificheBD.setAtomica(false); // connessione condivisa
			}
			
			notificheBD.insertNotifica(notifica);
			log.debug(MessageFormat.format("Inserimento notifica RPT[{0}] effettuato, procedo allo scheduling per la spedizione verso l''applicazione [{1}].",	notifica.getRptKey(), applicazione.getCodApplicazione()));
			return true;
		} else {
			log.debug(MessageFormat.format("Inserimento notifica RPT[{0}] non effettuato, la configurazione dell''applicazione [{1}] non prevede un connettore di notifica.", notifica.getRptKey(), applicazione.getCodApplicazione()));
			return false;
		}
	}
	
	public List<it.govpay.bd.model.Notifica> findNotificheDaSpedire(Integer offset, Integer limit, String codApplicazione) throws ServiceException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		NotificheBD notificheBD = new NotificheBD(configWrapper);
		List<it.govpay.bd.model.Notifica> notifiche  = notificheBD.findNotificheDaSpedire(offset, limit, codApplicazione);
		
		if(notifiche.size() == 0) {
			return new ArrayList<>();
		}
		
		List<it.govpay.bd.model.Notifica> notificheToRet = new ArrayList<>();
		
		List<it.govpay.bd.model.Notifica> notificheAttivazione = new ArrayList<>();
		List<it.govpay.bd.model.Notifica> notificheTerminazione = new ArrayList<>();
		List<it.govpay.bd.model.Notifica> notificheDaAnnullare = new ArrayList<>();
		List<String> blackListChiaviRptAttivazione = new ArrayList<>();
		
		Map<String, List<it.govpay.bd.model.Notifica>> mappaAttivazioni = new HashMap<>();
		for(it.govpay.bd.model.Notifica notifica: notifiche) {
			notifica.getRpt(configWrapper);
			
			String key = notifica.getRptKey();
			
			List<it.govpay.bd.model.Notifica> notifichePerChiave = null;
			
			if(mappaAttivazioni.containsKey(key)) {
				notifichePerChiave = mappaAttivazioni.remove(key);
			} else {
				notifichePerChiave = new ArrayList<>();
			}
			
			switch (notifica.getTipo()) {
			case ATTIVAZIONE:
				notificheAttivazione.add(notifica);
				notifichePerChiave.add(notifica);
				break;
			case RICEVUTA:
				notificheTerminazione.add(notifica);
				// le notifiche di terminazione devono essere eseguite sempre
				break;
			case FALLIMENTO:
			case ANNULLAMENTO:
				log.warn(MessageFormat.format("Notifica RPT[{0}] di tipo [{1}] non verra'' spedita verso l''applicazione.",	notifica.getRptKey(), notifica.getTipo()));
				notificheDaAnnullare.add(notifica);
				break;
			}
			
			mappaAttivazioni.put(key, notifichePerChiave);
		}
		
		for (String key : mappaAttivazioni.keySet()) { // controllo duplicati tra le attivazioni.
			List<it.govpay.bd.model.Notifica> notifichePerChiave = mappaAttivazioni.get(key);
			
			if(notifichePerChiave.size() > 1) {
				blackListChiaviRptAttivazione.add(key);
				
				for(it.govpay.bd.model.Notifica notifica: notifichePerChiave) {
					Date prossima = new GregorianCalendar(9999,1,1).getTime();
					TipoNotifica tipoNotifica = notifica.getTipo();
					long tentativi = notifica.getTentativiSpedizione() + 1;
					
					switch (tipoNotifica) {
					case ATTIVAZIONE:
						notificheBD.updateAnnullata(notifica.getId(), MessageFormat.format("Trovata una notifica di spedizione per la stessa RPT [{0}] schedulata per l''invio, spedizione annullata",	key), tentativi, prossima);
						break;
					case RICEVUTA:
						break;
					case FALLIMENTO:
					case ANNULLAMENTO:
						// non dovrei passare mai qua dentro
						log.warn(MessageFormat.format("Notifica RPT[{0}] di tipo [{1}] non verra'' spedita verso l''applicazione.", notifica.getRptKey(),	notifica.getTipo()));
						break;
					}
					
					
				}
			} 
		}
		
		// annullo eventuali notifiche di terminazione/fallimento lette
		for(it.govpay.bd.model.Notifica notifica: notificheDaAnnullare) {
			Date prossima = new GregorianCalendar(9999,1,1).getTime();
			long tentativi = notifica.getTentativiSpedizione() + 1;
			notificheBD.updateAnnullata(notifica.getId(), MessageFormat.format("Notifica RPT[{0}] di tipo [{1}] non verra'' spedita verso l''applicazione, spedizione annullata", notifica.getRptKey(), notifica.getTipo()), tentativi, prossima);
		}
		
		for(it.govpay.bd.model.Notifica notifica: notificheAttivazione) {
			// avvio solo le notifiche che non sono in black list
			if(!blackListChiaviRptAttivazione.contains(notifica.getRptKey())) { 
				notificheToRet.add(notifica);
			} 
		}
		for(it.govpay.bd.model.Notifica notifica: notificheTerminazione) {
			notificheToRet.add(notifica);
		}
		
		return notificheToRet;
	}
}
