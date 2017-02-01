package it.govpay.bd.reportistica;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.bd.wrapper.RendicontazionePagamentoBD;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoFilter;
import it.govpay.model.EstrattoConto;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class EstrattiContoBD extends BasicBD{

	private static final int LIMIT = 50;

	public EstrattiContoBD(BasicBD basicBD) {
		super(basicBD);
	}

	public EstrattoContoFilter newFilter() throws ServiceException {
		return new EstrattoContoFilter(this.getPagamentoService());
	}
	
	public EstrattoContoFilter newFilter(boolean ignoraStatoVersamento) throws ServiceException {
		return new EstrattoContoFilter(this.getPagamentoService(),ignoraStatoVersamento);
	}

	private RendicontazionePagamentoFilter toFilter(EstrattoContoFilter filter, RendicontazionePagamentoBD bd) throws ServiceException {
		RendicontazionePagamentoFilter renfilter = new RendicontazionePagamentoFilter(bd.getRendicontazionePagamentoServiceSearch());
		renfilter.setIgnoraStatoVersamento(filter.isIgnoraStatoVersamento());
		renfilter.setStatoVersamento(filter.getStatoVersamento());
		renfilter.setDataPagamentoMax(filter.getDataFine());
		renfilter.setDataPagamentoMin(filter.getDataInizio());
		renfilter.setCodDomini(filter.getIdDomini());
		renfilter.setIdSingoloVersamento(filter.getIdPagamento());
		renfilter.setIdVersamento(filter.getIdVersamento());
		renfilter.setIdSingoloVersamento(filter.getIdSingoloVersamento());
		renfilter.setOffset(filter.getOffset() != null ? filter.getOffset() : 0);
		renfilter.setLimit(filter.getLimit() != null ? filter.getLimit() : LIMIT); 
		return renfilter;
	}
	
	private EstrattoConto toEstrattoConto(RendicontazionePagamento rp) throws ServiceException {
		EstrattoConto estrattoConto= new EstrattoConto();
		

		estrattoConto.setIdPagamento(rp.getPagamento().getId()); //id_pagamento oppure id_rsr
		estrattoConto.setIdSingoloVersamento(rp.getSingoloVersamento().getId()); // id singoloVersamento
		estrattoConto.setIdVersamento(rp.getVersamento().getId()); // id_versamento
		estrattoConto.setDataPagamento(rp.getPagamento().getDataPagamento()); // data_pagamento
		estrattoConto.setImportoDovuto(rp.getVersamento().getImportoTotale().doubleValue()); // importo dovuto
		estrattoConto.setImportoPagato(rp.getPagamento().getImportoPagato().doubleValue()); // importo pagato
		estrattoConto.setIuv(rp.getPagamento().getIuv());// iuv
		estrattoConto.setIur(rp.getPagamento().getIur()); // iur1
		estrattoConto.setIdRegolamento(rp.getFr().getIur()); // iur 2
		estrattoConto.setCodFlussoRendicontazione(rp.getFr().getCodFlusso()); // cod_flusso_rendicontazione
		estrattoConto.setCodBicRiversamento(rp.getFr().getCodBicRiversamento()); //  codice_bic_riversamento
		estrattoConto.setCodVersamentoEnte(rp.getVersamento().getCodVersamentoEnte()); // cod_versamento_ente
		estrattoConto.setStatoVersamento(rp.getVersamento().getStatoVersamento()); // stato_versamento
		estrattoConto.setCodSingoloVersamentoEnte(rp.getSingoloVersamento().getCodSingoloVersamentoEnte()); // cod_singolo_versamento_ente
		estrattoConto.setStatoSingoloVersamento(rp.getSingoloVersamento().getStatoSingoloVersamento()); // stato_singolo_versamento
		estrattoConto.setIbanAccredito(rp.getPagamento().getIbanAccredito()); // iban_accredito
		estrattoConto.setDebitoreIdentificativo(rp.getVersamento().getAnagraficaDebitore().getCodUnivoco()); // cf_debitore
		estrattoConto.setNote(rp.getSingoloVersamento().getNote()); // note
		try {
			estrattoConto.setCausale(rp.getVersamento().getCausaleVersamento() != null ? rp.getVersamento().getCausaleVersamento().getSimple() : null);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		} // causale
		return estrattoConto;
	}
	
	
	public List<EstrattoConto> findAll(EstrattoContoFilter filter) throws ServiceException {
		RendicontazionePagamentoBD rpBd = new RendicontazionePagamentoBD(this);
		RendicontazionePagamentoFilter filter2 = toFilter(filter, rpBd);
		
		List<RendicontazionePagamento> listRp = rpBd.findAll(filter2);
		
		List<EstrattoConto> listaPagamenti = new ArrayList<EstrattoConto>();
		
		for(RendicontazionePagamento rp: listRp) {
			listaPagamenti.add(toEstrattoConto(rp));
		}
		
		return listaPagamenti;
	}

	public long count(EstrattoContoFilter filter) throws ServiceException {
		
		RendicontazionePagamentoBD rpBd = new RendicontazionePagamentoBD(this);
		RendicontazionePagamentoFilter filter2 = toFilter(filter, rpBd);
		
		return rpBd.count(filter2);
	}

	public List<EstrattoConto>  estrattoContoFromIdSingoliVersamenti(List<Long> idSingoliVersamenti, Integer offset, Integer limit)throws ServiceException {
		EstrattoContoFilter filter = newFilter();
		filter.setIdSingoloVersamento(idSingoliVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}
	
	public List<EstrattoConto>  estrattoContoFromIdVersamenti(List<Long> idVersamenti, Integer offset, Integer limit)throws ServiceException {
		EstrattoContoFilter filter = newFilter();
		filter.setIdVersamento(idVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);

	}

	public List<EstrattoConto> estrattoContoFromIdSingoliVersamenti(EstrattoContoFilter filter) throws ServiceException {
		try {
			int offset = 0;
			List<EstrattoConto> lstRet = new ArrayList<EstrattoConto>();
			List<EstrattoConto> lst = this.estrattoContoFromIdSingoliVersamenti(filter.getIdPagamento(), offset, LIMIT);

			while(lst != null && !lst.isEmpty()) {
				lstRet.addAll(lst);

				offset += lst.size();
				lst = this.estrattoContoFromIdSingoliVersamenti(filter.getIdPagamento(), offset, LIMIT);
			}

			return lstRet;
		} catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public List<EstrattoConto> estrattoContoFromIdVersamenti(EstrattoContoFilter filter) throws ServiceException {
		try {
			int offset = 0;
			List<EstrattoConto> lstRet = new ArrayList<EstrattoConto>();
			List<EstrattoConto> lst = this.estrattoContoFromIdVersamenti(filter.getIdVersamento(), offset, LIMIT);

			while(lst != null && !lst.isEmpty()) {
				lstRet.addAll(lst);

				offset += lst.size();
				lst = this.estrattoContoFromIdVersamenti(filter.getIdVersamento(), offset, LIMIT);
			}

			return lstRet;
		} catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public EstrattoConto getEstrattoContoByIdSingoloVersamento(long id) throws ServiceException {
		EstrattoContoFilter filter = newFilter();
		filter.setIdSingoloVersamento(Arrays.asList(id));
		filter.setOffset(0);
		filter.setLimit(LIMIT);
		List<EstrattoConto> findAll = findAll(filter);
		if(findAll != null && findAll.size() > 0) {
			return findAll.get(0);	
		} else {
			return null;
		}
	}

	public  List<EstrattoConto>  estrattoContoFromCodDominioIntervalloDate(String codDominio, Date dataInizio, Date dataFine, Integer offset, Integer limit)throws ServiceException {
		EstrattoContoFilter filter = newFilter();
		filter.setIdDomini(Arrays.asList(codDominio));
		filter.setDataInizio(dataInizio);
		filter.setDataFine(dataFine);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}

	public  List<EstrattoConto> estrattoContoFromCodDominioIdVersamenti(String codDominio, List<Long> idVersamenti, Integer offset, Integer limit)throws ServiceException {
		EstrattoContoFilter filter = newFilter();
		filter.setIdDomini(Arrays.asList(codDominio));
		filter.setIdVersamento(idVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}
	
	public  List<EstrattoConto> estrattoContoFromCodDominioIdSingoliVersamenti(String codDominio, List<Long> idSingoliVersamenti, Integer offset, Integer limit)throws ServiceException {
		EstrattoContoFilter filter = newFilter();
		filter.setIdDomini(Arrays.asList(codDominio));
		filter.setIdSingoloVersamento(idSingoliVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}


}
