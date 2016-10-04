package it.govpay.bd.wrapper;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.bd.model.converter.FrApplicazioneConverter;
import it.govpay.bd.model.converter.FrConverter;
import it.govpay.bd.model.converter.PagamentoConverter;
import it.govpay.bd.model.converter.RptConverter;
import it.govpay.bd.model.converter.SingoloVersamentoConverter;
import it.govpay.bd.model.converter.VersamentoConverter;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoFilter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class RendicontazionePagamentoBD extends BasicBD {

	public RendicontazionePagamentoBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazionePagamentoFilter newFilter() throws ServiceException {
		return new RendicontazionePagamentoFilter(this.getRendicontazionePagamentoServiceSearch());
	}

	public long count(RendicontazionePagamentoFilter filter) throws ServiceException {
		try {
			return this.getRendicontazionePagamentoServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<RendicontazionePagamento> findAll(RendicontazionePagamentoFilter filter) throws ServiceException {
		try {
			List<RendicontazionePagamento> lst = new ArrayList<RendicontazionePagamento>();
			List<it.govpay.orm.RendicontazionePagamento> lstVO = this.getRendicontazionePagamentoServiceSearch().findAll(filter.toPaginatedExpression());

			for(it.govpay.orm.RendicontazionePagamento rendicontazionePagamentoVO : lstVO) {
				lst.add(getRendicontazionePagamento(rendicontazionePagamentoVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	private RendicontazionePagamento getRendicontazionePagamento(it.govpay.orm.RendicontazionePagamento rendicontazionePagamentoVO) throws ServiceException {
		RendicontazionePagamento rp = new RendicontazionePagamento();
		rp.setFr(FrConverter.toDTO(rendicontazionePagamentoVO.getFr()));
		rp.setFrApplicazione(FrApplicazioneConverter.toDTO(rendicontazionePagamentoVO.getFrApplicazione()));
		rp.setPagamento(PagamentoConverter.toDTO(rendicontazionePagamentoVO.getPagamento()));
		rp.setVersamento(VersamentoConverter.toDTO(rendicontazionePagamentoVO.getVersamento()));
		rp.setSingoloVersamento(SingoloVersamentoConverter.toDTO(rendicontazionePagamentoVO.getSingoloVersamento()));
		rp.setRpt(RptConverter.toDTO(rendicontazionePagamentoVO.getRpt()));
		return rp;
	}

	
}
