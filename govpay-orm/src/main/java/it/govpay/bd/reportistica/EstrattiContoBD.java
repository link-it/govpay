package it.govpay.bd.reportistica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.nativequeries.NativeQueries;
import it.govpay.bd.reportistica.converter.EstrattoContoConverter;
import it.govpay.bd.reportistica.filters.EstrattoContoFilter;
import it.govpay.model.EstrattoConto;
import it.govpay.orm.dao.IPagamentoService;

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

	public List<EstrattoConto> findAll(EstrattoContoFilter filter) throws ServiceException {
		String statoVersamento = filter.getStatoVersamento();
		Date dataFine = filter.getDataFine();
		Date dataInizio = filter.getDataInizio();
		List<Long> idDomini = filter.getIdDomini();
		Integer offset = filter.getOffset() != null ? filter.getOffset() : 0;
		Integer limit = filter.getLimit() != null ? filter.getLimit() : LIMIT; 

		List<EstrattoConto> listaPagamenti = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = getElencoSelectFieldsEstrattoConto();

		List<List<Object>> select = new ArrayList<List<Object>>();
		try {
			// prelevo la query
			String nativeQuery = NativeQueries.getInstance().getEstrattiContoQuery();

			StringBuilder sbPlaceHolder1 = new StringBuilder();

			if(idDomini != null && idDomini.size() > 0){
				sbPlaceHolder1.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_FROM_DOMINI);
			}
			if(!filter.isIgnoraStatoVersamento())
				sbPlaceHolder1.append(" where v.stato_versamento != 'NON_ESEGUITO' ");
			else 
				sbPlaceHolder1.append(" where v.stato_versamento is not null ");
			List<Object> listaParam = new ArrayList<Object>();
			// placeholder 1 : parametri statoVersamento, dataInizio, dataFine, codDominio
			if(StringUtils.isNotEmpty(statoVersamento)){
				listaParam.add(statoVersamento);
				sbPlaceHolder1.append("and v.stato_versamento = ? ");
			}
			if(dataInizio != null && dataFine != null){
				listaParam.add(dataInizio);
				listaParam.add(dataFine);
				sbPlaceHolder1.append("and p.data_acquisizione between ? and ? ");
			}else{				
				if(dataInizio!= null){
					listaParam.add(dataInizio);
					sbPlaceHolder1.append("and p.data_acquisizione > ? ");
				} 
				if(dataFine!= null){
					listaParam.add(dataFine);
					sbPlaceHolder1.append("and p.data_acquisizione < ? ");
				}
			}
			StringBuilder sb = new StringBuilder();
			if(idDomini != null && idDomini.size() > 0){
				listaParam.addAll(idDomini);
				
				for (int i=0; i < idDomini.size() ; i++) {
					if(i > 0)
						sb.append(",");

					sb.append("?");
				}
				
				sbPlaceHolder1.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_ID_DOMINI.replace(EstrattoContoCostanti.PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_DOMINI, sb.toString()));
			}
			// Sostituzione del placeHolder 1 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, sbPlaceHolder1.toString());

			StringBuilder sbPlaceHolder2 = new StringBuilder();
			
			if(idDomini != null && idDomini.size() > 0){
				sbPlaceHolder2.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_FROM_DOMINI);
			}
			
			if(!filter.isIgnoraStatoVersamento())
				sbPlaceHolder2.append(" where v.stato_versamento != 'NON_ESEGUITO' ");
			else 
				sbPlaceHolder2.append(" where v.stato_versamento is not null ");
			// placeholder 2 : parametri statoVersamento, dataInizio, dataFine
			if(StringUtils.isNotEmpty(statoVersamento)){
				listaParam.add(statoVersamento);
				sbPlaceHolder2.append("and v.stato_versamento = ? ");
			}
			if(dataInizio != null && dataFine != null){
				listaParam.add(dataInizio);
				listaParam.add(dataFine);
				sbPlaceHolder2.append("and rsr.rendicontazione_data between ? and ? ");
			}else{				
				if(dataInizio!= null){
					listaParam.add(dataInizio);
					sbPlaceHolder2.append("and rsr.rendicontazione_data > ? ");
				} 

				if(dataFine!= null){
					listaParam.add(dataFine);
					sbPlaceHolder2.append("and rsr.rendicontazione_data < ? ");
				}
			}
			if(idDomini != null && idDomini.size() > 0){
				listaParam.addAll(idDomini);
				sbPlaceHolder2.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_ID_DOMINI.replace(EstrattoContoCostanti.PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_DOMINI, sb.toString()));
			}

			// Sostituzione del placeHolder 2 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, sbPlaceHolder2.toString());

			listaParam.add(offset);

			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				listaParam.add(offset+limit);
			} else {
				listaParam.add(limit);
			}

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto pagamento = EstrattoContoConverter.getEstrattoContoFromResultSet(list);
					listaPagamenti.add(pagamento); 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return listaPagamenti;
	}

	public long count(EstrattoContoFilter filter) throws ServiceException {
		String statoVersamento = filter.getStatoVersamento();
		Date dataFine = filter.getDataFine();
		Date dataInizio = filter.getDataInizio();
		List<Long> idDomini = filter.getIdDomini();

		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = new ArrayList<Class<?>>();

		listaFields.add(Long.class); // count

		List<List<Object>> select = new ArrayList<List<Object>>();
		try {
			// prelevo la query
			String nativeQuery = NativeQueries.getInstance().getEstrattiContoCountQuery();

			StringBuilder sbPlaceHolder1 = new StringBuilder();
			
			if(idDomini != null && idDomini.size() > 0){
				sbPlaceHolder1.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_FROM_DOMINI);
			}

			if(!filter.isIgnoraStatoVersamento())
				sbPlaceHolder1.append(" where v.stato_versamento != 'NON_ESEGUITO' ");
			else 
				sbPlaceHolder1.append(" where v.stato_versamento is not null ");
			List<Object> listaParam = new ArrayList<Object>();
			// placeholder 1 : parametri statoVersamento, dataInizio, dataFine, codDominio
			if(StringUtils.isNotEmpty(statoVersamento)){
				listaParam.add(statoVersamento);
				sbPlaceHolder1.append("and v.stato_versamento = ? ");
			}
			if(dataInizio != null && dataFine != null){
				listaParam.add(dataInizio);
				listaParam.add(dataFine);
				sbPlaceHolder1.append("and p.data_acquisizione between ? and ? ");
			}else{				
				if(dataInizio!= null){
					listaParam.add(dataInizio);
					sbPlaceHolder1.append("and p.data_acquisizione > ? ");
				} 
				if(dataFine!= null){
					listaParam.add(dataFine);
					sbPlaceHolder1.append("and p.data_acquisizione < ? ");
				}
			}
			StringBuilder sb = new StringBuilder();
			if(idDomini != null && idDomini.size() > 0){
				listaParam.addAll(idDomini);
				
				for (int i=0; i < idDomini.size() ; i++) {
					if(i > 0)
						sb.append(",");

					sb.append("?");
				}
				
				sbPlaceHolder1.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_ID_DOMINI.replace(EstrattoContoCostanti.PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_DOMINI, sb.toString()));
			}
			// Sostituzione del placeHolder 1 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, sbPlaceHolder1.toString());

			StringBuilder sbPlaceHolder2 = new StringBuilder();
			
			if(idDomini != null && idDomini.size() > 0){
				sbPlaceHolder2.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_FROM_DOMINI);
			}

			if(!filter.isIgnoraStatoVersamento())
				sbPlaceHolder2.append(" where v.stato_versamento != 'NON_ESEGUITO' ");
			else 
				sbPlaceHolder2.append(" where v.stato_versamento is not null ");
			// placeholder 2 : parametri statoVersamento, dataInizio, dataFine
			if(StringUtils.isNotEmpty(statoVersamento)){
				listaParam.add(statoVersamento);
				sbPlaceHolder2.append("and v.stato_versamento = ? ");
			}
			if(dataInizio != null && dataFine != null){
				listaParam.add(dataInizio);
				listaParam.add(dataFine);
				sbPlaceHolder2.append("and rsr.rendicontazione_data between ? and ? ");
			}else{				
				if(dataInizio!= null){
					listaParam.add(dataInizio);
					sbPlaceHolder2.append("and rsr.rendicontazione_data > ? ");
				} 

				if(dataFine!= null){
					listaParam.add(dataFine);
					sbPlaceHolder2.append("and rsr.rendicontazione_data < ? ");
				}
			}
			if(idDomini != null && idDomini.size() > 0){
				listaParam.addAll(idDomini);
				sbPlaceHolder2.append(EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_ID_DOMINI.replace(EstrattoContoCostanti.PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_DOMINI, sb.toString()));
			}

			// Sostituzione del placeHolder 2 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, sbPlaceHolder2.toString());

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					return (Long) list.get(0);
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		return 0;
	}

	public List<EstrattoConto>  estrattoContoFromIdSingoliVersamenti(List<Long> idSingoliVersamenti, Integer offset, Integer limit)throws ServiceException {
		List<EstrattoConto> listaPagamenti = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = getElencoSelectFieldsEstrattoConto();

		List<List<Object>> select = new ArrayList<List<Object>>();
		try {
			List<Object> listaParam = new ArrayList<Object>();
			listaParam.addAll(idSingoliVersamenti);
			listaParam.addAll(idSingoliVersamenti);
			listaParam.add(offset);

			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				listaParam.add(offset+limit);
			} else {
				listaParam.add(limit);
			}

			String nativeQuery = NativeQueries.getInstance().getEstrattiContoQuery();

			StringBuilder sb = new StringBuilder();

			sb.append("where sv.id in (");
			for (int i=0; i < idSingoliVersamenti.size() ; i++) {
				if(i > 0)
					sb.append(",");

				sb.append("?");
			}
			sb.append(")");

			// Sostituzione del placeHolder 1 e 2 sv.id in () 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, sb.toString());
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, sb.toString());

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto pagamento = EstrattoContoConverter.getEstrattoContoFromResultSet(list);

					listaPagamenti.add(pagamento); 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return listaPagamenti;
	}
	
	public List<EstrattoConto>  estrattoContoFromIdVersamenti(List<Long> idVersamenti, Integer offset, Integer limit)throws ServiceException {
		List<EstrattoConto> listaPagamenti = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = getElencoSelectFieldsEstrattoConto();

		List<List<Object>> select = new ArrayList<List<Object>>();
		try {
			List<Object> listaParam = new ArrayList<Object>();
			listaParam.addAll(idVersamenti);
			listaParam.addAll(idVersamenti);
			listaParam.add(offset);

			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				listaParam.add(offset+limit);
			} else {
				listaParam.add(limit);
			}

			String nativeQuery = NativeQueries.getInstance().getEstrattiContoQuery();

			StringBuilder sb = new StringBuilder();

			sb.append("where v.id in (");
			for (int i=0; i < idVersamenti.size() ; i++) {
				if(i > 0)
					sb.append(",");

				sb.append("?");
			}
			sb.append(")");

			// Sostituzione del placeHolder 1 e 2 sv.id in () 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, sb.toString());
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, sb.toString());

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto pagamento = EstrattoContoConverter.getEstrattoContoFromResultSet(list);

					listaPagamenti.add(pagamento); 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return listaPagamenti;
	}

	private List<Class<?>> getElencoSelectFieldsEstrattoConto() {
		List<Class<?>> listaFields = new ArrayList<Class<?>>();

		listaFields.add(Long.class); // id_pagamento oppure id_rsr
		listaFields.add(Long.class); // id singoloVersamento
		listaFields.add(Long.class); // id_versamento
		listaFields.add(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO.getFieldType()); // data_pagamento
		listaFields.add(it.govpay.orm.SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType()); // importo_dovuto
		listaFields.add(it.govpay.orm.Pagamento.model().IMPORTO_PAGATO.getFieldType()); // importo_pagato
		listaFields.add(it.govpay.orm.Pagamento.model().ID_RPT.IUV.getFieldType()); // iuv
		listaFields.add(it.govpay.orm.Pagamento.model().IUR.getFieldType());  // iur1
		listaFields.add(it.govpay.orm.FR.model().IUR.getFieldType()); // iur2
		listaFields.add(it.govpay.orm.Pagamento.model().CODFLUSSO_RENDICONTAZIONE.getFieldType()); // cod_flusso_rendicontazione
		listaFields.add(it.govpay.orm.FR.model().COD_BIC_RIVERSAMENTO.getFieldType()); // codice_bic_riversamento
		listaFields.add(it.govpay.orm.Versamento.model().COD_VERSAMENTO_ENTE.getFieldType()); // cod_versamento_ente
		listaFields.add(it.govpay.orm.Versamento.model().STATO_VERSAMENTO.getFieldType()); // stato_versamento
		listaFields.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()); // cod_singolo_versamento_ente
		listaFields.add(it.govpay.orm.SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType()); // stato_singolo_versamento
		listaFields.add(it.govpay.orm.Pagamento.model().IBAN_ACCREDITO.getFieldType()); // iban_accredito
		listaFields.add(it.govpay.orm.Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType()); // cf_debitore
		listaFields.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE.getFieldType()); //note
		listaFields.add(it.govpay.orm.Versamento.model().CAUSALE_VERSAMENTO.getFieldType()); // causale

		return listaFields;
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
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = getElencoSelectFieldsEstrattoConto();

		List<List<Object>> select = new ArrayList<List<Object>>();
		try {
			List<Object> listaParam = new ArrayList<Object>();
			listaParam.add(id);
			listaParam.add(id);
			listaParam.add(0);
			listaParam.add(LIMIT);

			String nativeQuery = NativeQueries.getInstance().getEstrattiContoQuery();

			StringBuilder sb = new StringBuilder();
			sb.append("where sv.id = ? ");

			// Sostituzione del placeHolder 1 e 2 sv.id = ? 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, sb.toString());
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, sb.toString());

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto pagamento = EstrattoContoConverter.getEstrattoContoFromResultSet(list);
					return pagamento; 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return null;
	}

	public  List<EstrattoConto>  estrattoContoFromCodDominioIntervalloDate(String codDominio, Date dataInizio, Date dataFine, Integer offset, Integer limit)throws ServiceException {
		List<it.govpay.model.EstrattoConto> estrattiConto = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = getElencoSelectFieldsEstrattoConto();

		List<List<Object>> select = new ArrayList<List<Object>>();
		try {
			List<Object> listaParam = new ArrayList<Object>();

			// placeholder 1 data e cod dominio
			// cod dominio
			listaParam.add(codDominio);
			//date query 1
			listaParam.add(dataInizio);
			listaParam.add(dataFine);

			// placeholder 2 data
			// cod dominio
			listaParam.add(codDominio);
			// data query 2
			listaParam.add(dataInizio);
			listaParam.add(dataFine);

			// offset
			listaParam.add(offset);

			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				listaParam.add(offset+limit);
			} else {
				listaParam.add(limit);
			}

			String nativeQuery = NativeQueries.getInstance().getEstrattiContoQuery();

			// Sostituzione del placeHolder 1 e 2 sv.id in () 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_PAGAMENTI);
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_RSR);

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto estrattoConto = EstrattoContoConverter.getEstrattoContoFromResultSet(list);
					estrattiConto.add(estrattoConto); 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return estrattiConto;
	}

	public  List<EstrattoConto> estrattoContoFromCodDominioIdVersamenti(String codDominio, List<Long> idVersamenti, Integer offset, Integer limit)throws ServiceException {
		List<it.govpay.model.EstrattoConto> estrattiConto = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = getElencoSelectFieldsEstrattoConto();
		List<List<Object>> select = new ArrayList<List<Object>>();
		try {

			List<Object> listaParam = new ArrayList<Object>();
			// query 1
			listaParam.addAll(idVersamenti);
			listaParam.add(codDominio);
			// query 2
			listaParam.addAll(idVersamenti);
			listaParam.add(codDominio);

			listaParam.add(offset);

			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				listaParam.add(offset+limit);
			} else {
				listaParam.add(limit);
			}

			String nativeQuery = NativeQueries.getInstance().getEstrattiContoQuery();
			
			StringBuilder sb = new StringBuilder();
			for (int i=0; i < idVersamenti.size() ; i++) {
				if(i > 0)
					sb.append(",");

				sb.append("?");
			}
			String clausolaIdVersamenti = EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_ID_VERSAMENTI.replace(EstrattoContoCostanti.PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_VERSAMENTI, sb.toString());

			// Sostituzione del placeHolder 1 e 2 sv.id in () 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, clausolaIdVersamenti);
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, clausolaIdVersamenti);

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto estrattoConto = EstrattoContoConverter.getEstrattoContoFromResultSet(list);
					estrattiConto.add(estrattoConto); 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return estrattiConto;
	}
	
	public  List<EstrattoConto> estrattoContoFromCodDominioIdSingoliVersamenti(String codDominio, List<Long> idSingoliVersamenti, Integer offset, Integer limit)throws ServiceException {
		List<it.govpay.model.EstrattoConto> estrattiConto = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = getElencoSelectFieldsEstrattoConto();
		List<List<Object>> select = new ArrayList<List<Object>>();
		try {

			List<Object> listaParam = new ArrayList<Object>();
			// query 1
			listaParam.addAll(idSingoliVersamenti);
			listaParam.add(codDominio);
			// query 2
			listaParam.addAll(idSingoliVersamenti);
			listaParam.add(codDominio);

			listaParam.add(offset);

			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				listaParam.add(offset+limit);
			} else {
				listaParam.add(limit);
			}

			String nativeQuery = NativeQueries.getInstance().getEstrattiContoQuery();
			
			StringBuilder sb = new StringBuilder();
			for (int i=0; i < idSingoliVersamenti.size() ; i++) {
				if(i > 0)
					sb.append(",");

				sb.append("?");
			}
			String clausolaIdVersamenti = EstrattoContoCostanti.PLACE_HOLDER_QUERY_CLAUSOLA_COD_DOMINIO_ID_SINGOLI_VERSAMENTI.replace(EstrattoContoCostanti.PLACE_HOLDER_QUERY_ESTRATTI_CONTO_ID_VERSAMENTI, sb.toString());

			// Sostituzione del placeHolder 1 e 2 sv.id in () 
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER1_QUERY_ESTRATTI_CONTO, clausolaIdVersamenti);
			nativeQuery = nativeQuery.replace(EstrattoContoCostanti.PLACE_HOLDER2_QUERY_ESTRATTI_CONTO, clausolaIdVersamenti);

			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto estrattoConto = EstrattoContoConverter.getEstrattoContoFromResultSet(list);
					estrattiConto.add(estrattoConto); 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return estrattiConto;
	}


}
