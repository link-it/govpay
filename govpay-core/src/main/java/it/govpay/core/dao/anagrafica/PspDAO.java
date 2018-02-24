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
import it.govpay.model.Canale.TipoVersamento;

public class PspDAO extends BasicBD{

	public PspDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public ListaPspDTOResponse listaPsp(ListaPspDTO listaPspDTO) throws ServiceException{
		PspBD pspBD = new PspBD(this);
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
	}

	public LeggiPspDTOResponse leggiPsp(LeggiPspDTO leggiPspDTO) throws ServiceException,PspNonTrovatoException{

		LeggiPspDTOResponse response = new LeggiPspDTOResponse();

		PspBD pspBD = new PspBD(this);
		Psp psp;
		try {
			psp = pspBD.getPsp(leggiPspDTO.getIdPsp());
			response.setPsp(psp);
		} catch (NotFoundException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		}
		return response;
	}

	public ListaCanaliDTOResponse listaCanali(ListaCanaliDTO listaPspDTO) throws ServiceException{
		PspBD pspBD = new PspBD(this);
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

		List<Canale> resList = new ArrayList<Canale>();
		if(count > 0) {
			resList = pspBD.findAllCanali(filter);
		} 

		return new ListaCanaliDTOResponse(count, resList);
	}

	public LeggiCanaleDTOResponse leggiCanale(LeggiCanaleDTO leggiCanaleDTO) throws ServiceException,PspNonTrovatoException{

		LeggiCanaleDTOResponse response = new LeggiCanaleDTOResponse();

		PspBD pspBD = new PspBD(this);
		Canale canale;
		try {
			canale = pspBD.getCanale(leggiCanaleDTO.getIdPsp(), leggiCanaleDTO.getIdCanale(), leggiCanaleDTO.getTipoVersamento());
			response.setCanale(canale);
		} catch (NotFoundException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new PspNonTrovatoException(e.getMessage(), e);
		}
		return response;
	}
}
