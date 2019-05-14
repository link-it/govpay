package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.converter.StampaConverter;
import it.govpay.bd.pagamento.filters.StampaFilter;
import it.govpay.model.Stampa;
import it.govpay.orm.dao.jdbc.JDBCStampaServiceSearch;

public class StampeBD extends BasicBD{

	public StampeBD(BasicBD basicBD) {
		super(basicBD);
	}

	public StampaFilter newFilter() throws ServiceException {
		return new StampaFilter(this.getStampaService());
	}

	public StampaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new StampaFilter(this.getStampaService(),simpleSearch);
	}
	
	public long count(StampaFilter filter) throws ServiceException {
		try {
			return this.getStampaService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Stampa> findAll(StampaFilter filter) throws ServiceException {
		try {
			List<Stampa> stampeLst = new ArrayList<>();

			List<it.govpay.orm.Stampa> stampeVOLst = this.getStampaService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Stampa incassoVO: stampeVOLst) {
				stampeLst.add(StampaConverter.toDTO(incassoVO));
			}
			return stampeLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public Stampa getStampa(long id) throws ServiceException , NotFoundException{
		try {
			it.govpay.orm.Stampa stampaVO = ((JDBCStampaServiceSearch)this.getStampaService()).get(id);
			return StampaConverter.toDTO(stampaVO);
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	public void insertStampa(Stampa stampa) throws ServiceException {
		try {
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			this.getStampaService().create(vo);
			stampa.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void updateStampa(Stampa stampa) throws NotFoundException,ServiceException {
		try {
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			this.getStampaService().update(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}  
	}
}
