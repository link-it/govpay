package it.govpay.bd.viste;

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.viste.filters.RendicontazioneFilter;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.bd.viste.model.converter.RendicontazioneConverter;
import it.govpay.orm.IdRendicontazione;

public class RendicontazioniBD extends BasicBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioneFilter newFilter() throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch());
	}
	
	public RendicontazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch(),simpleSearch);
	}

	public List<Rendicontazione> findAll(RendicontazioneFilter filter) throws ServiceException {
		try {
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = this.getVistaRendicontazioneServiceSearch().findAll(filter.toPaginatedExpression());
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public long count(RendicontazioneFilter filter) throws ServiceException {
		try {
			return this.getVistaRendicontazioneServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera la rendicontazione identificata dalla chiave fisica
	 */
	public Rendicontazione getRendicontazione(long id) throws ServiceException {
		try {
			IdRendicontazione idRendicontazione = new IdRendicontazione();
			idRendicontazione.setId(id);
			it.govpay.orm.VistaRendicontazione rendicontazione = this.getVistaRendicontazioneServiceSearch().get(idRendicontazione);
			return RendicontazioneConverter.toDTO(rendicontazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
}
