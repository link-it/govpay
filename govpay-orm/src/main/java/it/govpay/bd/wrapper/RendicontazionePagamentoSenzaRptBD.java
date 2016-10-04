package it.govpay.bd.wrapper;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.RendicontazionePagamentoSenzaRpt;
import it.govpay.bd.model.converter.FrApplicazioneConverter;
import it.govpay.bd.model.converter.FrConverter;
import it.govpay.bd.model.converter.RendicontazioneSenzaRptConverter;
import it.govpay.bd.model.converter.SingoloVersamentoConverter;
import it.govpay.bd.model.converter.VersamentoConverter;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoSenzaRptFilter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class RendicontazionePagamentoSenzaRptBD extends BasicBD {

	public RendicontazionePagamentoSenzaRptBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazionePagamentoSenzaRptFilter newFilter() throws ServiceException {
		return new RendicontazionePagamentoSenzaRptFilter(this.getRendicontazionePagamentoSenzaRPTServiceSearch());
	}

	public long count(RendicontazionePagamentoSenzaRptFilter filter) throws ServiceException {
		try {
			return this.getRendicontazionePagamentoSenzaRPTServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<RendicontazionePagamentoSenzaRpt> findAll(RendicontazionePagamentoSenzaRptFilter filter) throws ServiceException {
		try {
			List<RendicontazionePagamentoSenzaRpt> lst = new ArrayList<RendicontazionePagamentoSenzaRpt>();
			List<it.govpay.orm.RendicontazionePagamentoSenzaRPT> lstVO = this.getRendicontazionePagamentoSenzaRPTServiceSearch().findAll(filter.toPaginatedExpression());

			for(it.govpay.orm.RendicontazionePagamentoSenzaRPT rendicontazionePagamentoVO : lstVO) {
				lst.add(getRendicontazionePagamentoSenzaRpt(rendicontazionePagamentoVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	private RendicontazionePagamentoSenzaRpt getRendicontazionePagamentoSenzaRpt(it.govpay.orm.RendicontazionePagamentoSenzaRPT rendicontazionePagamentoVO) throws ServiceException {
		RendicontazionePagamentoSenzaRpt rp = new RendicontazionePagamentoSenzaRpt();
		rp.setFr(FrConverter.toDTO(rendicontazionePagamentoVO.getFr()));
		rp.setFrApplicazione(FrApplicazioneConverter.toDTO(rendicontazionePagamentoVO.getFrApplicazione()));
		rp.setRendicontazioneSenzaRpt(RendicontazioneSenzaRptConverter.toDTO(rendicontazionePagamentoVO.getRendicontazioneSenzaRPT()));
		rp.setVersamento(VersamentoConverter.toDTO(rendicontazionePagamentoVO.getVersamento()));
		rp.setSingoloVersamento(SingoloVersamentoConverter.toDTO(rendicontazionePagamentoVO.getSingoloVersamento()));
		return rp;
	}
}
