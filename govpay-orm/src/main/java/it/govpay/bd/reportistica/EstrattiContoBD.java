package it.govpay.bd.reportistica;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.RendicontazionePagamento;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.bd.wrapper.PagamentoRendicontazioneBD;
import it.govpay.bd.wrapper.filters.RendicontazionePagamentoFilter;
import it.govpay.model.EstrattoConto;

public class EstrattiContoBD extends BasicBD{

	public static final String TIPO_RECORD_STORNO = "STORNO";
	private static final int LIMIT = 50;
	Logger log = LoggerWrapperFactory.getLogger(EstrattiContoBD.class);

	public EstrattiContoBD(BasicBD basicBD) {
		super(basicBD);
	}

//	public EstrattoContoFilter newFilter() throws ServiceException {
//		return new EstrattoContoFilter(this.getPagamentoService());
//	}

	public EstrattoContoFilter newFilter(boolean ignoraStatoVersamento) throws ServiceException {
		return new EstrattoContoFilter(this.getPagamentoService(),ignoraStatoVersamento);
	}

	private RendicontazionePagamentoFilter toFilter(EstrattoContoFilter filter, PagamentoRendicontazioneBD bd) throws ServiceException {
		RendicontazionePagamentoFilter renfilter = new RendicontazionePagamentoFilter(bd.getRendicontazionePagamentoServiceSearch());
		renfilter.setIgnoraStatoVersamento(filter.isIgnoraStatoVersamento());
		renfilter.setStatoVersamento(filter.getStatoVersamento());
		renfilter.setDataPagamentoMax(filter.getDataFine());
		renfilter.setDataPagamentoMin(filter.getDataInizio());
		renfilter.setCodDomini(filter.getIdDomini());
		renfilter.setIdPagamento(filter.getIdPagamento());
		renfilter.setIdVersamento(filter.getIdVersamento());
		renfilter.setOffset(filter.getOffset() != null ? filter.getOffset() : 0);
		renfilter.setLimit(filter.getLimit() != null ? filter.getLimit() : LIMIT); 
		return renfilter;
	}

	private EstrattoConto toEstrattoConto(RendicontazionePagamento rp) throws ServiceException {
		EstrattoConto estrattoConto= new EstrattoConto();

		estrattoConto.setTipo(rp.getTipo()); 
		estrattoConto.setIdPagamento(rp.getPagamento().getId()); //id_pagamento oppure id_rsr
		estrattoConto.setDataPagamento(rp.getPagamento().getDataPagamento()); // data_pagamento
		estrattoConto.setImportoDovuto(rp.getVersamento().getImportoTotale().doubleValue()); // importo dovuto
		estrattoConto.setImportoPagato(rp.getPagamento().getImportoPagato().doubleValue()); // importo pagato
		estrattoConto.setIuv(rp.getPagamento().getIuv());// iuv
		estrattoConto.setIur(rp.getPagamento().getIur()); // iur1
		estrattoConto.setIbanAccredito(rp.getPagamento().getIbanAccredito()); // iban_accredito
		estrattoConto.setIdRr(rp.getPagamento().getIdRr()); //id_rr
		estrattoConto.setIdIncasso(rp.getPagamento().getIdIncasso()); //id_incasso
		estrattoConto.setStatoPagamento(rp.getPagamento().getStato()); // stato_pagamento

		if(rp.getFr() != null){
			estrattoConto.setIdRegolamento(rp.getFr().getIur()); // iur 2
			List<String> lst = new ArrayList<String>();
			lst.add(rp.getFr().getCodFlusso());
			estrattoConto.setCodFlussoRendicontazione(lst); // cod_flusso_rendicontazione
			estrattoConto.setCodBicRiversamento(rp.getFr().getCodBicRiversamento()); //  codice_bic_riversamento
		}
		if(rp.getVersamento() != null){
			estrattoConto.setIdVersamento(rp.getVersamento().getId()); // id_versamento
			estrattoConto.setCodVersamentoEnte(rp.getVersamento().getCodVersamentoEnte()); // cod_versamento_ente
			estrattoConto.setStatoVersamento(rp.getVersamento().getStatoVersamento()); // stato_versamento
			estrattoConto.setDebitoreIdentificativo(rp.getVersamento().getAnagraficaDebitore().getCodUnivoco()); // cf_debitore
			try {
				estrattoConto.setCausale(rp.getVersamento().getCausaleVersamento() != null ? rp.getVersamento().getCausaleVersamento().getSimple() : null);
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			} // causale
		}
		if(rp.getSingoloVersamento() != null){
			estrattoConto.setIdSingoloVersamento(rp.getSingoloVersamento().getId()); // id singoloVersamento
			estrattoConto.setCodSingoloVersamentoEnte(rp.getSingoloVersamento().getCodSingoloVersamentoEnte()); // cod_singolo_versamento_ente
			estrattoConto.setStatoSingoloVersamento(rp.getSingoloVersamento().getStatoSingoloVersamento()); // stato_singolo_versamento
			estrattoConto.setNote(rp.getSingoloVersamento().getDatiAllegati()); // note
		}
		
		if(rp.getIncasso() != null) {
			estrattoConto.setDataIncasso(rp.getIncasso().getDataIncasso()); // data incasso
		}

		return estrattoConto;
	}


	public List<EstrattoConto> findAll(EstrattoContoFilter filter) throws ServiceException {
		PagamentoRendicontazioneBD rpBd = new PagamentoRendicontazioneBD(this);
		RendicontazionePagamentoFilter filter2 = toFilter(filter, rpBd);

		List<RendicontazionePagamento> listRp = rpBd.findAll(filter2);

		List<EstrattoConto> listaPagamenti = new ArrayList<EstrattoConto>();

		for(RendicontazionePagamento rp: listRp) {
			listaPagamenti.add(toEstrattoConto(rp));
		}

		return listaPagamenti;
	}

	public long count(EstrattoContoFilter filter) throws ServiceException {

		PagamentoRendicontazioneBD rpBd = new PagamentoRendicontazioneBD(this);
		RendicontazionePagamentoFilter filter2 = toFilter(filter, rpBd);

		return rpBd.count(filter2);
	}

	public List<EstrattoConto>  estrattoContoFromIdPagamenti(EstrattoContoFilter filter, Integer offset, Integer limit)throws ServiceException {
//		EstrattoContoFilter filter = newFilter();
//		filter.setIdSingoloVersamento(idSingoliVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}

	public List<EstrattoConto>  estrattoContoFromIdVersamenti(EstrattoContoFilter filter, Integer offset, Integer limit)throws ServiceException {
//		EstrattoContoFilter filter = newFilter();
//		filter.setIdVersamento(idVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);

	}

	public List<EstrattoConto> estrattoContoFromIdPagamenti(EstrattoContoFilter filter) throws ServiceException {
		try {
			int offset = 0;
			List<EstrattoConto> lstRet = this.estrattoContoFromIdPagamenti(filter, offset, Integer.MAX_VALUE);

			if(filter.isFiltraDuplicati()) {
				log.debug("Eseguo filtro duplicati ["+filter.isFiltraDuplicati()+"]"); 
				lstRet = filtraDuplicati(lstRet);
			}

			return lstRet;
		} catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<EstrattoConto> filtraDuplicati(List<EstrattoConto> lstRet) {
		List<EstrattoConto> lstRetFiltrata = new ArrayList<EstrattoConto>();
		log.debug("Filtro duplicati numero iniziale entries ["+lstRet.size()+"]"); 
		Map<String, EstrattoConto> mapFiltrata = new HashMap<String, EstrattoConto>();
		Map<String, Boolean> mapIncassati = new HashMap<String, Boolean>();
		List<String> ordineInserimentoChiavi = new ArrayList<String>();

		for (EstrattoConto ecInput : lstRet) {
			String codSingoloVersamentoEnteEcInput = ecInput.getIdPagamento().toString();
			if(!ecInput.getTipo().equals(TIPO_RECORD_STORNO)) {

				if(mapFiltrata.containsKey(codSingoloVersamentoEnteEcInput)) {
					log.debug("Entry ["+codSingoloVersamentoEnteEcInput+"] presente, modifico codice flusso");
					EstrattoConto entryDaAggiornare = mapFiltrata.remove(codSingoloVersamentoEnteEcInput);
					List<String> lstFr = entryDaAggiornare.getCodFlussoRendicontazione();
					Boolean incassatoDaAggiornare = mapIncassati.remove(codSingoloVersamentoEnteEcInput);

					// se la entry collezionata non e' incassata aggiorno il valore con un nuovo id flusso incassato oppure aggiungo l'id alla lista
					// se e' gia' stata incassata non faccio nulla.
					if(!incassatoDaAggiornare) {
						// controllo se la nuova entry e' incassata
						if(ecInput.getIdIncasso() != null) {
							incassatoDaAggiornare = true;
							// salvo solo il nuovo id flusso
							if(ecInput.getCodFlussoRendicontazione() != null && ecInput.getCodFlussoRendicontazione().size() > 0)
								lstFr = ecInput.getCodFlussoRendicontazione();
						} else {
							// collezione la lista degli identificativi flusso
							if(ecInput.getCodFlussoRendicontazione() != null && ecInput.getCodFlussoRendicontazione().size() > 0)
								lstFr.addAll(ecInput.getCodFlussoRendicontazione());
						}
					}

					entryDaAggiornare.setCodFlussoRendicontazione(lstFr);
					// riporto le modifiche
					mapFiltrata.put(codSingoloVersamentoEnteEcInput, entryDaAggiornare);
					mapIncassati.put(codSingoloVersamentoEnteEcInput, incassatoDaAggiornare);
				} else {
					log.debug("Entry ["+codSingoloVersamentoEnteEcInput+"] non presente aggiungo alla mappa"); 
					ordineInserimentoChiavi.add(codSingoloVersamentoEnteEcInput);
					mapFiltrata.put(codSingoloVersamentoEnteEcInput, ecInput);
					mapIncassati.put(codSingoloVersamentoEnteEcInput, (ecInput.getIdIncasso() != null));
				}
			}
		}

		// popolo la lista filtrata
		for (String chiave : ordineInserimentoChiavi) {
			lstRetFiltrata.add(mapFiltrata.get(chiave));
		}

		log.debug("Filtro duplicati numero finale entries ["+lstRetFiltrata.size()+"]"); 
		return lstRetFiltrata;
	}

	public List<EstrattoConto> estrattoContoFromIdVersamenti(EstrattoContoFilter filter) throws ServiceException {
		try {
			int offset = 0;
			List<EstrattoConto> lstRet = new ArrayList<EstrattoConto>();
			List<EstrattoConto> lst = this.estrattoContoFromIdVersamenti(filter, offset, LIMIT);

			while(lst != null && !lst.isEmpty()) {
				lstRet.addAll(lst);

				offset += lst.size();
				lst = this.estrattoContoFromIdVersamenti(filter, offset, LIMIT);
			}

			return lstRet;
		} catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public EstrattoConto getEstrattoContoByIdPagamenti(long id) throws ServiceException {
		EstrattoContoFilter filter = newFilter(false);
		filter.setIdPagamento(Arrays.asList(id));
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
		EstrattoContoFilter filter = newFilter(false);
		filter.setIdDomini(Arrays.asList(codDominio));
		filter.setDataInizio(dataInizio);
		filter.setDataFine(dataFine);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}

	public  List<EstrattoConto> estrattoContoFromCodDominioIdVersamenti(String codDominio, List<Long> idVersamenti, Integer offset, Integer limit)throws ServiceException {
		EstrattoContoFilter filter = newFilter(false);
		filter.setIdDomini(Arrays.asList(codDominio));
		filter.setIdVersamento(idVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}

	public  List<EstrattoConto> estrattoContoFromCodDominioIdPagamenti(String codDominio, List<Long> idSingoliVersamenti, Integer offset, Integer limit)throws ServiceException {
		EstrattoContoFilter filter = newFilter(false);
		filter.setIdDomini(Arrays.asList(codDominio));
		filter.setIdPagamento(idSingoliVersamenti);
		filter.setOffset(offset);
		filter.setLimit(limit);
		return findAll(filter);
	}


}
