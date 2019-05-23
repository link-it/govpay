package it.govpay.core.dao.eventi;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTOResponse;
import it.govpay.core.dao.eventi.dto.PutEventoDTO;
import it.govpay.core.dao.eventi.dto.PutEventoDTOResponse;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.EventoContext;
import it.govpay.model.Evento.CategoriaEvento;

public class EventiDAO extends BaseDAO {

	public ListaEventiDTOResponse listaEventi(ListaEventiDTO listaEventiDTO) throws ServiceException, NotAuthenticatedException, NotAuthorizedException {
		
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			return listaEventi(listaEventiDTO, bd);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaEventiDTOResponse listaEventi(ListaEventiDTO listaEventiDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		EventiBD eventiBD = new EventiBD(bd);
		EventiFilter filter = eventiBD.newFilter();
		
		filter.setCodDomini(listaEventiDTO.getCodDomini());
		filter.setOffset(listaEventiDTO.getOffset());
		filter.setLimit(listaEventiDTO.getLimit());
		
		filter.setCodDominio(listaEventiDTO.getIdDominio());
		filter.setIuv(listaEventiDTO.getIuv());
		
		if(listaEventiDTO.getIdA2A()!=null && listaEventiDTO.getIdPendenza() != null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento;
			try {
				versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(bd, listaEventiDTO.getIdA2A()).getId(), listaEventiDTO.getIdPendenza());
				filter.setCodDominio(versamento.getUo(bd).getDominio(bd).getCodDominio());
				filter.setIuv(versamento.getIuvVersamento());
			} catch (NotFoundException e) {
				return new ListaEventiDTOResponse(0, new ArrayList<>());
			}
		} else {
			filter.setCodApplicazione(listaEventiDTO.getIdA2A());
			filter.setCodVersamentoEnte(listaEventiDTO.getIdPendenza());
		}
		
		filter.setIdSessione(listaEventiDTO.getIdPagamento());
		filter.setFilterSortList(listaEventiDTO.getFieldSortList());
		

		long count = eventiBD.count(filter);

		List<Evento> resList = new ArrayList<>();
		if(count > 0) {
			resList = eventiBD.findAll(filter);
		} 

		return new ListaEventiDTOResponse(count, resList);
	}

	
	public PutEventoDTOResponse inserisciEvento(PutEventoDTO putEventoDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		PutEventoDTOResponse putEventoDTOResponse = new PutEventoDTOResponse();
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			EventiBD eventiBD = new EventiBD(bd);
			
			Evento evento = new Evento();
			
			EventoContext eventoGenerico = putEventoDTO.getEvento();
			 
			evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA_COOPERAZIONE);
			evento.setCcp(eventoGenerico.getCcp());
			evento.setCodDominio(eventoGenerico.getCodDominio());
			evento.setData(eventoGenerico.getDataRichiesta());
			evento.setClassnameDettaglio(EventoContext.class.getName());
			evento.setDettaglio(""); 
			evento.setIdPagamentoPortale(eventoGenerico.getIdPagamentoPortale()); 
			evento.setIdVersamento(eventoGenerico.getIdVersamento());
			evento.setIuv(eventoGenerico.getIuv());
			evento.setSottotipoEvento(eventoGenerico.getSottotipoEvento());
			if(eventoGenerico.getTipoEvento() != null)
				evento.setTipoEvento(eventoGenerico.getTipoEvento());
			if(eventoGenerico.getDataRisposta() != null) {
				if(eventoGenerico.getDataRichiesta() != null) {
					evento.setIntervallo(eventoGenerico.getDataRisposta().getTime() - eventoGenerico.getDataRichiesta().getTime());
				} else {
					evento.setIntervallo(0l);
				}
			} else {
				evento.setIntervallo(0l);
			}
			
			eventiBD.insertEvento(evento);
			return putEventoDTOResponse;
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
}
