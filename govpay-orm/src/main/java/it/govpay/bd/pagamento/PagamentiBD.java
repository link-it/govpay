/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.bd.pagamento;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.converter.PagamentoConverter;
import it.govpay.bd.model.converter.RendicontazioneSenzaRptConverter;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.model.EstrattoConto;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.RendicontazioneSenzaRpt;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.dao.IPagamentoService;
import it.govpay.orm.dao.jdbc.JDBCRendicontazioneSenzaRPTService;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.RendicontazioneSenzaRPTFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.PagamentoFetch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.TipiDatabase;

public class PagamentiBD extends BasicBD {
	
	

	public static final String NOME_QUERY_ESTRATTI_CONTO = "estrattiConto";
	public static final String NOME_QUERY_ESTRATTI_CONTO_PER_VERSAMENTI = "estrattiContoVersamenti";
	public static final String NOME_QUERY_CSV_VERSAMENTI = "csvVersamenti";
	public static final String PLACE_HOLDER_QUERY_ESTRATTI_CONTO_PER_VERSAMENTI = "$ID_VERSAMENTI$";

	public PagamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera il pagamento identificato dalla chiave fisica
	 */
	public Pagamento getPagamento(long id) throws ServiceException {
		try {
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setId(id);
			it.govpay.orm.Pagamento pagamento = this.getPagamentoService().get(
					idPagamento);
			return PagamentoConverter.toDTO(pagamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Crea un nuovo pagamento.
	 */
	public void insertPagamento(Pagamento pagamento) throws ServiceException {
		try {
			it.govpay.orm.Pagamento vo = PagamentoConverter.toVO(pagamento);
			this.getPagamentoService().create(vo);
			pagamento.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void updatePagamento(Pagamento pagamento) throws ServiceException {
		try {
			it.govpay.orm.Pagamento vo = PagamentoConverter.toVO(pagamento);
			IdPagamento idPagamento = new IdPagamento();
			idPagamento.setId(pagamento.getId());
			this.getPagamentoService().update(idPagamento, vo);
			pagamento.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public List<Pagamento> getPagamenti(long idRpt) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getPagamentoService()
					.newPaginatedExpression();
			PagamentoFieldConverter fieldConverter = new PagamentoFieldConverter(
					this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_rpt", Long.class, "id_rpt",
					fieldConverter.toTable(it.govpay.orm.Pagamento.model())),
					idRpt);
			List<it.govpay.orm.Pagamento> singoliPagamenti = this
					.getPagamentoService().findAll(exp);
			return PagamentoConverter.toDTO(singoliPagamenti);
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	public Pagamento getPagamento(String codDominio, String iuv, String iur)
			throws ServiceException, NotFoundException, MultipleResultException {
		try {
			IExpression exp = this.getPagamentoService().newExpression();
			exp.equals(it.govpay.orm.Pagamento.model().IUR, iur);
			exp.equals(it.govpay.orm.Pagamento.model().ID_RPT.COD_DOMINIO,
					codDominio);
			exp.equals(it.govpay.orm.Pagamento.model().ID_RPT.IUV, iuv);
			it.govpay.orm.Pagamento pagamentoVO = this.getPagamentoService()
					.find(exp);
			return PagamentoConverter.toDTO(pagamentoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	public List<Pagamento> getPagamentiByFrApplicazione(Long idFrApplicazione)
			throws ServiceException {
		PagamentoFilter filter = newFilter();
		filter.setIdFrApplicazione(idFrApplicazione);
		return findAll(filter);
	}

	public List<RendicontazioneSenzaRpt> getRendicontazioniSenzaRpt(
			Long idFrApplicazione) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getRendicontazioneSenzaRPTService()
					.newPaginatedExpression();
			RendicontazioneSenzaRPTFieldConverter fieldConverter = new RendicontazioneSenzaRPTFieldConverter(
					this.getJdbcProperties().getDatabaseType());
			exp.equals(
					new CustomField(
							"id_fr_applicazione",
							Long.class,
							"id_fr_applicazione",
							fieldConverter
									.toTable(it.govpay.orm.RendicontazioneSenzaRPT
											.model())), idFrApplicazione);
			List<it.govpay.orm.RendicontazioneSenzaRPT> rendicontazioniSenzaRpt = this
					.getRendicontazioneSenzaRPTService().findAll(exp);
			return RendicontazioneSenzaRptConverter
					.toDTO(rendicontazioniSenzaRpt);
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	public long countRendicontazioniSenzaRpt(Long idFrApplicazione)
			throws ServiceException {
		try {
			IExpression exp = this.getRendicontazioneSenzaRPTService()
					.newExpression();
			RendicontazioneSenzaRPTFieldConverter fieldConverter = new RendicontazioneSenzaRPTFieldConverter(
					this.getJdbcProperties().getDatabaseType());
			exp.equals(
					new CustomField(
							"id_fr_applicazione",
							Long.class,
							"id_fr_applicazione",
							fieldConverter
									.toTable(it.govpay.orm.RendicontazioneSenzaRPT
											.model())), idFrApplicazione);
			NonNegativeNumber count = this.getRendicontazioneSenzaRPTService()
					.count(exp);

			return count != null ? count.longValue() : 0;
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	public List<RendicontazioneSenzaRpt> getRendicontazioniSenzaRpt(
			List<Long> idFrApplicazione) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getRendicontazioneSenzaRPTService()
					.newPaginatedExpression();
			RendicontazioneSenzaRPTFieldConverter fieldConverter = new RendicontazioneSenzaRPTFieldConverter(
					this.getJdbcProperties().getDatabaseType());
			exp.in(new CustomField("id_fr_applicazione", Long.class,
					"id_fr_applicazione", fieldConverter
							.toTable(it.govpay.orm.RendicontazioneSenzaRPT
									.model())), idFrApplicazione);
			List<it.govpay.orm.RendicontazioneSenzaRPT> rendicontazioniSenzaRpt = this
					.getRendicontazioneSenzaRPTService().findAll(exp);
			return RendicontazioneSenzaRptConverter
					.toDTO(rendicontazioniSenzaRpt);
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	public long countRendicontazioniSenzaRpt(List<Long> idFrApplicazione)
			throws ServiceException {
		try {
			IExpression exp = this.getRendicontazioneSenzaRPTService()
					.newExpression();
			RendicontazioneSenzaRPTFieldConverter fieldConverter = new RendicontazioneSenzaRPTFieldConverter(
					this.getJdbcProperties().getDatabaseType());
			exp.in(new CustomField("id_fr_applicazione", Long.class,
					"id_fr_applicazione", fieldConverter
							.toTable(it.govpay.orm.RendicontazioneSenzaRPT
									.model())), idFrApplicazione);
			NonNegativeNumber count = this.getRendicontazioneSenzaRPTService()
					.count(exp);

			return count != null ? count.longValue() : 0;
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	public RendicontazioneSenzaRpt getRendicontazioneSenzaRpt(
			Long idRendicontazioneSenzaRpt) throws ServiceException {
		try {
			return RendicontazioneSenzaRptConverter
					.toDTO(((JDBCRendicontazioneSenzaRPTService) this
							.getRendicontazioneSenzaRPTService())
							.get(idRendicontazioneSenzaRpt));
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (NotFoundException e) {
			throw new ServiceException();
		} catch (MultipleResultException e) {
			throw new ServiceException();
		}
	}

	public List<Pagamento> getPagamentiByRr(Long idRr) throws ServiceException {
		PagamentoFilter filter = newFilter();
		filter.setIdRr(idRr);
		return findAll(filter);
	}

	public PagamentoFilter newFilter() throws ServiceException {
		return new PagamentoFilter(this.getPagamentoService());
	}

	public List<Pagamento> findAll(PagamentoFilter filter)
			throws ServiceException {
		try {
			List<it.govpay.orm.Pagamento> pagamentoVOLst = this
					.getPagamentoService().findAll(
							filter.toPaginatedExpression());
			return PagamentoConverter.toDTO(pagamentoVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(PagamentoFilter filter) throws ServiceException {
		try {
			return this.getPagamentoService().count(filter.toExpression())
					.longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<it.govpay.model.rest.Pagamento> estrattoConto(
			PagamentoFilter filter) throws ServiceException {
		List<it.govpay.model.rest.Pagamento> pagamenti = new ArrayList<it.govpay.model.rest.Pagamento>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		IPaginatedExpression pagExpr = filter.toPaginatedExpression();

		List<IField> listaFields = new ArrayList<IField>();
		listaFields.add(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO);
		listaFields.add(it.govpay.orm.Pagamento.model().IMPORTO_PAGATO);
		listaFields.add(it.govpay.orm.Pagamento.model().IUR);
		listaFields.add(it.govpay.orm.Pagamento.model().ID_RPT.IUV);
		listaFields
				.add(it.govpay.orm.Pagamento.model().CODFLUSSO_RENDICONTAZIONE);
		listaFields
				.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE);
		listaFields
				.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE);

		IField[] fields = listaFields.toArray(new IField[listaFields.size()]);
		List<Map<String, Object>> select = new ArrayList<Map<String, Object>>();
		try {
			select = pagamentoService.select(pagExpr, fields);
			if (select != null && select.size() > 0) {
				PagamentoFetch pagFetch = new PagamentoFetch();
				TipiDatabase tipoDatabase = ConnectionManager
						.getJDBCServiceManagerProperties().getDatabase();
				for (Map<String, Object> map : select) {
					it.govpay.orm.Pagamento pagamento = (it.govpay.orm.Pagamento) pagFetch
							.fetch(tipoDatabase,
									it.govpay.orm.Pagamento.model(), map);
					it.govpay.model.rest.Pagamento dto = it.govpay.bd.model.rest.converter.PagamentoConverter
							.toRestDTO(pagamento, map);
					pagamenti.add(dto);
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return pagamenti;
	}

	public  List<EstrattoConto>  estrattoConto(String codDominio, Date dataInizio, Date dataFine, Integer offset, Integer limit)throws ServiceException {
		List<it.govpay.model.EstrattoConto> estrattiConto = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = new ArrayList<Class<?>>();
		listaFields.add(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().IMPORTO_PAGATO.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().IUR.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().ID_RPT.IUV.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().CODFLUSSO_RENDICONTAZIONE.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE.getFieldType());
		listaFields.add(it.govpay.orm.FR.model().COD_BIC_RIVERSAMENTO.getFieldType());
		listaFields.add(it.govpay.orm.FR.model().IUR.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().IBAN_ACCREDITO.getFieldType());

		List<List<Object>> select = new ArrayList<List<Object>>();
		try {
			List<Object> listaParam = new ArrayList<Object>();
			//date query 1
			listaParam.add(dataInizio);
			listaParam.add(dataFine);
			// cod dominio
			listaParam.add(codDominio);
			// data query 2
			listaParam.add(dataInizio);
			listaParam.add(dataFine);
			// cod dominio
			listaParam.add(codDominio);
			
			// offset
			listaParam.add(offset);
			
			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				listaParam.add(offset+limit);
			} else {
				listaParam.add(limit);
			}
			
			select = pagamentoService.nativeQuery(GovpayConfig.getInstance().getNativeQuery(NOME_QUERY_ESTRATTI_CONTO), listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto estrattoConto = new EstrattoConto();
					estrattoConto.setDataPagamento((Date) list.get(0));
					estrattoConto.setImportoPagato((Double) list.get(1));
					estrattoConto.setIur((String) list.get(2));
					estrattoConto.setIuv((String) list.get(3));
					estrattoConto.setCodFlussoRendicontazione((String) list.get(4));
					estrattoConto.setCodSingoloVersamentoEnte((String) list.get(5));
					estrattoConto.setNote((String) list.get(6));
					estrattoConto.setCodBicRiversamento((String) list.get(7));
					estrattoConto.setIdRegolamento((String) list.get(8));
					estrattoConto.setIbanAccredito((String) list.get(9));
					
					estrattiConto.add(estrattoConto); 
				}
			}
		} catch (NotFoundException e) {
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return estrattiConto;
	}
	
	public  List<EstrattoConto>  estrattoConto(String codDominio, List<Long> idVersamenti, Integer offset, Integer limit)throws ServiceException {
		List<it.govpay.model.EstrattoConto> estrattiConto = new ArrayList<EstrattoConto>();
		IPagamentoService pagamentoService = this.getPagamentoService();

		List<Class<?>> listaFields = new ArrayList<Class<?>>();
		listaFields.add(it.govpay.orm.Pagamento.model().DATA_PAGAMENTO.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().IMPORTO_PAGATO.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().IUR.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().ID_RPT.IUV.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().CODFLUSSO_RENDICONTAZIONE.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.NOTE.getFieldType());
		listaFields.add(it.govpay.orm.FR.model().COD_BIC_RIVERSAMENTO.getFieldType());
		listaFields.add(it.govpay.orm.FR.model().IUR.getFieldType());
		listaFields.add(it.govpay.orm.Pagamento.model().IBAN_ACCREDITO.getFieldType());

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
			
			String nativeQuery = GovpayConfig.getInstance().getNativeQuery(NOME_QUERY_ESTRATTI_CONTO_PER_VERSAMENTI);
			
			StringBuilder sb = new StringBuilder();
			
			for (int i=0; i < idVersamenti.size() ; i++) {
				if(sb.length() > 0)
					sb.append(",");
				
				sb.append("?");
			}
			
			nativeQuery = nativeQuery.replace(PLACE_HOLDER_QUERY_ESTRATTI_CONTO_PER_VERSAMENTI, sb.toString());
			
			select = pagamentoService.nativeQuery(nativeQuery, listaFields, listaParam.toArray());
			if(select != null && select.size() > 0){
				for (List<Object> list : select) {
					EstrattoConto estrattoConto = new EstrattoConto();
					estrattoConto.setDataPagamento((Date) list.get(0));
					estrattoConto.setImportoPagato((Double) list.get(1));
					estrattoConto.setIur((String) list.get(2));
					estrattoConto.setIuv((String) list.get(3));
					estrattoConto.setCodFlussoRendicontazione((String) list.get(4));
					estrattoConto.setCodSingoloVersamentoEnte((String) list.get(5));
					estrattoConto.setNote((String) list.get(6));
					estrattoConto.setCodBicRiversamento((String) list.get(7));
					estrattoConto.setIdRegolamento((String) list.get(8));
					estrattoConto.setIbanAccredito((String) list.get(9));
					
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
