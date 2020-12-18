package it.govpay.core.dao.eventi;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Evento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.eventi.dto.LeggiEventoDTO;
import it.govpay.core.dao.eventi.dto.LeggiEventoDTOResponse;
import it.govpay.core.dao.eventi.dto.ListaEventiDTO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTOResponse;
import it.govpay.core.dao.eventi.dto.PutEventoDTO;
import it.govpay.core.dao.eventi.dto.PutEventoDTOResponse;
import it.govpay.core.dao.eventi.exception.EventoNonTrovatoException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.EventoContext;

public class EventiDAO extends BaseDAO {

	public ListaEventiDTOResponse listaEventi(ListaEventiDTO listaEventiDTO) throws ServiceException, NotAuthenticatedException, NotAuthorizedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);

		EventiBD eventiBD = null;
		try {
			eventiBD = new EventiBD(configWrapper, listaEventiDTO.getVista());
			
			EventiFilter filter = eventiBD.newFilter();

			filter.setCodDomini(listaEventiDTO.getCodDomini());
			filter.setOffset(listaEventiDTO.getOffset());
			filter.setLimit(listaEventiDTO.getLimit());

			filter.setCodDominio(listaEventiDTO.getIdDominio());
			filter.setIuv(listaEventiDTO.getIuv());
			filter.setCcp(listaEventiDTO.getCcp());

			filter.setCodApplicazione(listaEventiDTO.getIdA2A());
			filter.setCodVersamentoEnte(listaEventiDTO.getIdPendenza());

			filter.setIdSessione(listaEventiDTO.getIdPagamento());
			filter.setFilterSortList(listaEventiDTO.getFieldSortList());

			filter.setDatainizio(listaEventiDTO.getDataDa());
			filter.setDataFine(listaEventiDTO.getDataA());
			if(listaEventiDTO.getEsito() != null)
				filter.setEsito(listaEventiDTO.getEsito().toString());
			if(listaEventiDTO.getCategoriaEvento() != null)
				filter.setCategoria(listaEventiDTO.getCategoriaEvento().toString());
			if(listaEventiDTO.getRuolo() != null)
				filter.setRuolo(listaEventiDTO.getRuolo().toString());
			filter.setComponente(listaEventiDTO.getComponente());
			filter.setTipoEvento(listaEventiDTO.getTipoEvento());
			filter.setSottotipoEvento(listaEventiDTO.getSottotipoEvento());
			filter.setVista(listaEventiDTO.getVista()); 
			filter.setEseguiCountConLimit(listaEventiDTO.isEseguiCountConLimit());
			filter.setSeveritaDa(listaEventiDTO.getSeveritaDa());
			filter.setSeveritaA(listaEventiDTO.getSeveritaA());

			Long count = null;
			
			if(listaEventiDTO.isEseguiCount()) {
				 count = eventiBD.count(filter);
			}
			
			List<Evento> resList = new ArrayList<>();
			if(listaEventiDTO.isEseguiFindAll()) {
				eventiBD.setupConnection(configWrapper.getTransactionID());
				
				eventiBD.setAtomica(false);
				
				filter.setExpressionConstructor(eventiBD.getExpressionConstructor());
				
				filter.setFilterSortList(listaEventiDTO.getFieldSortList());
				if(!listaEventiDTO.isOrderEnabled()) {
					filter.addFilterSort(filter.getDefaultFilterSortWrapperDesc());
				}
				
				if(listaEventiDTO.getMessaggi() != null && listaEventiDTO.getMessaggi()) {
					resList = eventiBD.findAll(filter);
				} else {
					resList = eventiBD.findAllNoMessaggi(filter);
				}
			} 

			return new ListaEventiDTOResponse(count, resList);
		} finally {
			if(eventiBD != null)
				eventiBD.closeConnection();
		}
	}


	public PutEventoDTOResponse inserisciEvento(PutEventoDTO putEventoDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		PutEventoDTOResponse putEventoDTOResponse = new PutEventoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		EventiBD eventiBD = null;
		try {
			eventiBD = new EventiBD(configWrapper);

			EventoContext eventoGenerico = putEventoDTO.getEvento();

			Evento evento = eventoGenerico.toEventoDTO();
			eventiBD.insertEvento(evento);
			return putEventoDTOResponse;
		} finally {
			if(eventiBD != null)
				eventiBD.closeConnection();
		}
	}

	public LeggiEventoDTOResponse leggiEvento(LeggiEventoDTO leggiEventoDTO) throws ServiceException,EventoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		LeggiEventoDTOResponse response = new LeggiEventoDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		EventiBD eventiBD = null;
		try {
			eventiBD = new EventiBD(configWrapper);
			Evento evento = eventiBD.getEvento(leggiEventoDTO.getId());
			response.setEvento(evento);
		} catch (NotFoundException e) {
			throw new EventoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(eventiBD != null)
				eventiBD.closeConnection();
		}
		return response;
	}
}
