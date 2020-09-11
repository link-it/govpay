package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.filters.NotificaFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.ListaNotificheDTO;
import it.govpay.core.dao.pagamenti.dto.ListaNotificheDTOResponse;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class NotificheDAO extends BaseDAO{


	public ListaNotificheDTOResponse listaNotifiche(ListaNotificheDTO listaNotificheDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException, NotFoundException{ 
		NotificheBD notificheBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			notificheBD = new NotificheBD(configWrapper);
			NotificaFilter filter = notificheBD.newFilter();
			
			filter.setOffset(listaNotificheDTO.getOffset());
			filter.setLimit(listaNotificheDTO.getLimit());
			filter.setDataInizio(listaNotificheDTO.getDataDa());
			filter.setDataFine(listaNotificheDTO.getDataA());
			
			if(listaNotificheDTO.getStato() != null) {
				try {
					it.govpay.model.Notifica.StatoSpedizione statoSpedizione = listaNotificheDTO.getStato();
					filter.setStato(statoSpedizione.toString());
				} catch(Exception e) {
					return new ListaNotificheDTOResponse(0, new ArrayList<>());
				}
			}
			
			if(listaNotificheDTO.getTipo() != null) {
				try {
					it.govpay.model.Notifica.TipoNotifica tipoNotifica =  listaNotificheDTO.getTipo();
					filter.setTipo(tipoNotifica.toString());
				} catch(Exception e) {
					return new ListaNotificheDTOResponse(0, new ArrayList<>());
				}
			}
			
			long count = notificheBD.count(filter);

			if(count > 0) {
				List<Notifica> lst = notificheBD.findAll(filter);
				
				for (Notifica notifica : lst) {
					this.populateNotifica(notifica, configWrapper);
				}
				
				return new ListaNotificheDTOResponse(count, lst);
			} else {
				return new ListaNotificheDTOResponse(count, new ArrayList<>());
			}
		}finally {
			if(notificheBD != null)
				notificheBD.closeConnection();
		}
	}
	
	private void populateNotifica(Notifica notifica, BDConfigWrapper configWrapper) throws ServiceException, NotFoundException {
		notifica.getApplicazione(configWrapper);
	}
}
