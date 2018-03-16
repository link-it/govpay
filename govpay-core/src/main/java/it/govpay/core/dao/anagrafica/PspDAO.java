package it.govpay.core.dao.anagrafica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.anagrafica.filters.CanaleFilter;
import it.govpay.bd.anagrafica.filters.PspFilter;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Psp;
import it.govpay.core.dao.anagrafica.dto.LeggiCanaleDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiCanaleDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiPspDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiPspDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaCanaliDTO;
import it.govpay.core.dao.anagrafica.dto.ListaCanaliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTO;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTOResponse;
import it.govpay.core.dao.anagrafica.exception.PspNonTrovatoException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Canale.TipoVersamento;

public class PspDAO extends BaseDAO{

	public PspDAO() {
	}

	public ListaPspDTOResponse listaPsp(ListaPspDTO listaPspDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try { 
			this.autorizzaRichiesta(listaPspDTO.getUser(), Servizio.ANAGRAFICA_PAGOPA, Diritti.LETTURA,bd);
			
			PspBD pspBD = new PspBD(bd);
			PspFilter filter = pspBD.newFilter();
	
			filter.setOffset(listaPspDTO.getOffset());
			filter.setLimit(listaPspDTO.getLimit());
			filter.setSearchAbilitato(listaPspDTO.getAbilitato());
			filter.setBollo(listaPspDTO.getBollo());
			filter.setStorno(listaPspDTO.getStorno()); 
			filter.setFilterSortList(listaPspDTO.getFieldSortList());
	
			long count = pspBD.count(filter);
	
			List<Psp> resList = new ArrayList<Psp>();
			if(count > 0) {
				resList = pspBD.findAll(filter);
			} 
	
			return new ListaPspDTOResponse(count, resList);
		} finally {
			bd.closeConnection();
		}
	}

	public LeggiPspDTOResponse leggiPsp(LeggiPspDTO leggiPspDTO) throws ServiceException,PspNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		LeggiPspDTOResponse response = new LeggiPspDTOResponse();
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			this.autorizzaRichiesta(leggiPspDTO.getUser(), Servizio.ANAGRAFICA_PAGOPA, Diritti.LETTURA,bd);
			PspBD pspBD = new PspBD(bd);
			Psp psp = pspBD.getPsp(leggiPspDTO.getIdPsp());
			response.setPsp(psp);
			return response;
		} catch (NotFoundException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		} finally {
			bd.closeConnection();
		}
	}

	public ListaCanaliDTOResponse listaCanali(ListaCanaliDTO listaPspDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			this.autorizzaRichiesta(listaPspDTO.getUser(), Servizio.ANAGRAFICA_PAGOPA, Diritti.LETTURA,bd);
			PspBD pspBD = new PspBD(bd);
			CanaleFilter filter = pspBD.newCanaleFilter();
	
			filter.setOffset(listaPspDTO.getOffset());
			filter.setLimit(listaPspDTO.getLimit());
			filter.setAbilitato(listaPspDTO.getAbilitato());
			filter.setModello(listaPspDTO.getModello());
			if(listaPspDTO.getTipoVersamento() != null)
				filter.setTipoVersamento(TipoVersamento.valueOf(listaPspDTO.getTipoVersamento())); 
			filter.setFilterSortList(listaPspDTO.getFieldSortList());
			filter.setCodPsp(listaPspDTO.getIdPsp());
	
			long count = pspBD.countCanali(filter);
	
			List<LeggiCanaleDTOResponse> listaRs = new ArrayList<LeggiCanaleDTOResponse>();
			List<Canale> listaCanali = new ArrayList<Canale>();
			if(count > 0) {
				listaCanali = pspBD.findAllCanali(filter);
				for (Canale canale : listaCanali) {
					LeggiCanaleDTOResponse elem = new LeggiCanaleDTOResponse();
					elem.setCanale(canale);
					elem.setPsp(canale.getPsp(bd)); 
					listaRs.add(elem );
				}
			} 
	
			return new ListaCanaliDTOResponse(count, listaRs);
		} finally {
			bd.closeConnection();
		}
	}

	public LeggiCanaleDTOResponse leggiCanale(LeggiCanaleDTO leggiCanaleDTO) throws ServiceException,PspNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		LeggiCanaleDTOResponse response = new LeggiCanaleDTOResponse();
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			this.autorizzaRichiesta(leggiCanaleDTO.getUser(), Servizio.ANAGRAFICA_PAGOPA, Diritti.LETTURA,bd);
			PspBD pspBD = new PspBD(bd);
			Canale canale = pspBD.getCanale(leggiCanaleDTO.getIdPsp(), leggiCanaleDTO.getIdCanale(), leggiCanaleDTO.getTipoVersamento());
			response.setCanale(canale);
			response.setPsp(canale.getPsp(bd)); 
			return response;
		} catch (NotFoundException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		} finally {
			bd.closeConnection();
		}
	}
}
