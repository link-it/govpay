package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.converter.PagamentoPortaleConverter;
import it.govpay.bd.pagamento.filters.PagamentoPortaleFilter;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.PagamentoPortaleVersamento;
import it.govpay.orm.dao.IDBPagamentoPortaleService;
import it.govpay.orm.dao.jdbc.converter.PagamentoPortaleFieldConverter;
import it.govpay.orm.dao.jdbc.converter.PagamentoPortaleVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VistaPagamentoPortaleFieldConverter;

public class PagamentiPortaleBD extends BasicBD{


	public PagamentiPortaleBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public PagamentiPortaleBD(String idTransaction) {
		super(idTransaction);
	}
	
	public PagamentiPortaleBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public PagamentiPortaleBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public PagamentoPortaleFilter newFilter() throws ServiceException {
		return new PagamentoPortaleFilter(this.getVistaPagamentoPortaleServiceSearch());
	}
	
	public PagamentoPortaleFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new PagamentoPortaleFilter(this.getVistaPagamentoPortaleServiceSearch(),simpleSearch);
	}

	public long count(PagamentoPortaleFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();

			VistaPagamentoPortaleFieldConverter ppvFieldConverter = new VistaPagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			/*
			SELECT count(distinct id) 
				FROM
				  (
				  SELECT v_pagamenti_portale.id
				  FROM v_pagamenti_portale
				  WHERE ...restrizioni di autorizzazione o ricerca...
				  ORDER BY data_richiesta 
				  LIMIT K
				  ) a
				);
			*/
			
			sqlQueryObjectInterno.addFromTable(ppvFieldConverter.toTable(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE));
			sqlQueryObjectInterno.addSelectField("id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(ppvFieldConverter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVistaPagamentoPortaleServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
			Long count = 0L;
			for (List<Object> row : nativeQuery) {
				int pos = 0;
				count = BasicBD.getValueOrNull(row.get(pos++), Long.class);
			}
			
			return count.longValue();
		} catch (NotImplementedException | SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<PagamentoPortale> findAll(PagamentoPortaleFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			Integer offset = filter.getOffset();
			if(offset == null) offset = 0;
			Integer limit = filter.getLimit();
			if(limit == null) limit = 25;
			
			
			int limitInterno = (offset + limit) * 5;
			
			VistaPagamentoPortaleFieldConverter ppvFieldConverter = new VistaPagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			PagamentoPortaleFieldConverter ppFieldConverter = new PagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectPagamentiPortale = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			/*
			SELECT * FROM pagamenti_portale WHERE id IN (
					  SELECT distinct id 
					  FROM
					    (
					    SELECT v_pagamenti_portale.id
					    FROM v_pagamenti_portale
					    WHERE ...restrizioni di autorizzazione o ricerca...
					    ORDER BY data_richiesta 
					    LIMIT K
					    ) a
					) 
					ORDER BY data_richiesta 
					OFFSET X
					LIMIT Y;
			*/
			
			sqlQueryObjectInterno.addFromTable(ppvFieldConverter.toTable(it.govpay.orm.VistaPagamentoPortale.model().ID_SESSIONE));
			sqlQueryObjectInterno.addSelectField("id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(ppvFieldConverter.toColumn(it.govpay.orm.VistaPagamentoPortale.model().DATA_RICHIESTA, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectField("id");
			sqlQueryObjectDistinctID.setSelectDistinct(true);
			
			sqlQueryObjectPagamentiPortale.addFromTable(ppFieldConverter.toTable(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toTable(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE) + ".id");
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().COD_CANALE, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().NOME, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().IMPORTO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().VERSANTE_IDENTIFICATIVO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PORTALE, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PSP, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().STATO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().CODICE_STATO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().DESCRIZIONE_STATO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().PSP_REDIRECT_URL, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().PSP_ESITO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().JSON_REQUEST, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().DATA_RICHIESTA, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().URL_RITORNO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().COD_PSP, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().TIPO_VERSAMENTO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().MULTI_BENEFICIARIO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().TIPO, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().ACK, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().PRINCIPAL, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().TIPO_UTENZA, true));
			sqlQueryObjectPagamentiPortale.addSelectField(ppFieldConverter.toTable(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE) + ".id_applicazione"); // Id Applicazione
			
			sqlQueryObjectPagamentiPortale.addWhereINSelectSQLCondition(false, "id", sqlQueryObjectDistinctID);
			sqlQueryObjectPagamentiPortale.addOrderBy(ppFieldConverter.toColumn(it.govpay.orm.PagamentoPortale.model().DATA_RICHIESTA, true), false);
			sqlQueryObjectPagamentiPortale.setOffset(offset);
			sqlQueryObjectPagamentiPortale.setLimit(limit);
			
			String sql = sqlQueryObjectPagamentiPortale.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // ID
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE.getFieldType()); 
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().COD_CANALE.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().NOME.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().IMPORTO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().STATO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().CODICE_STATO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().PSP_ESITO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().JSON_REQUEST.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().DATA_RICHIESTA.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().URL_RITORNO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().COD_PSP.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().MULTI_BENEFICIARIO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().TIPO.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().ACK.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().PRINCIPAL.getFieldType());
			returnTypes.add(it.govpay.orm.PagamentoPortale.model().TIPO_UTENZA.getFieldType());
			returnTypes.add(Long.class); // Id Applicazione
			
			
			List<List<Object>> nativeQuery = this.getVistaPagamentoPortaleServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
			List<it.govpay.orm.PagamentoPortale> pagamentoVOLst = new ArrayList<it.govpay.orm.PagamentoPortale>();
			
			for (List<Object> row : nativeQuery) {
				int pos = 0;
				it.govpay.orm.PagamentoPortale vo = new it.govpay.orm.PagamentoPortale();
				vo.setId(BasicBD.getValueOrNull(row.get(pos++), Long.class));
				vo.setIdSessione(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setCodCanale(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setNome(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setImporto(BasicBD.getValueOrNull(row.get(pos++),Double.class));
				vo.setVersanteIdentificativo(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setIdSessionePortale(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setIdSessionePsp(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setStato(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setCodiceStato(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setDescrizioneStato(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setPspRedirectURL(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setPspEsito(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setJsonRequest(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setDataRichiesta(BasicBD.getValueOrNull(row.get(pos++), Date.class));
				vo.setUrlRitorno(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setCodPsp(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setTipoVersamento(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setMultiBeneficiario(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setTipo(BasicBD.getValueOrNull(row.get(pos++),Integer.class));
				vo.setAck(BasicBD.getValueOrNull(row.get(pos++),Boolean.class));
				vo.setPrincipal(BasicBD.getValueOrNull(row.get(pos++),String.class));
				vo.setTipoUtenza(BasicBD.getValueOrNull(row.get(pos++),String.class));
				
				Long idApplicazioneLong = BasicBD.getValueOrNull(row.get(pos++), Long.class);
				if(idApplicazioneLong != null) {
					IdApplicazione idApplicazione = new IdApplicazione();
					idApplicazione.setId(idApplicazioneLong);
					vo.setIdApplicazione(idApplicazione);
				}
				
				pagamentoVOLst.add(vo);
				
			}
			
			return PagamentoPortaleConverter.toDTO(pagamentoVOLst);
		} catch (NotImplementedException | SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<PagamentoPortale>();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void insertPagamento(PagamentoPortale pagamentoPortale) throws ServiceException {
		this.insertPagamento(pagamentoPortale,false); 
	}

	public void insertPagamento(PagamentoPortale pagamentoPortale, boolean commitParent) throws ServiceException {
		boolean oldAutocomit = this.isAutoCommit();
		try {
			if(this.isAtomica()) { // TODO
				this.setupConnection(this.getIdTransaction());
			}
			
			if(!commitParent)
				this.setAutoCommit(false);
			
			it.govpay.orm.PagamentoPortale vo = PagamentoPortaleConverter.toVO(pagamentoPortale);
			try {
				this.getPagamentoPortaleService().create(vo);
				pagamentoPortale.setId(vo.getId());

				this.insertPagPortVers(pagamentoPortale);
			} catch (NotImplementedException e) {
				throw new ServiceException();
			}
			if(!commitParent)
				this.commit();
		} catch (ServiceException e) {
			if(!commitParent)
				this.rollback();
			throw e;
		} finally {
			if(!commitParent)
				this.setAutoCommit(oldAutocomit);

			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}

	private void insertPagPortVers(PagamentoPortale pagamentoPortale) throws ServiceException, NotImplementedException {
		if(pagamentoPortale.getIdVersamento() != null) {
			for(IdVersamento idVersamento: pagamentoPortale.getIdVersamento()) {
				PagamentoPortaleVersamento pagamentoPortaleVersamento = new PagamentoPortaleVersamento();
				IdPagamentoPortale idPagamentoPortale = new IdPagamentoPortale();
				idPagamentoPortale.setId(pagamentoPortale.getId());
				pagamentoPortaleVersamento.setIdPagamentoPortale(idPagamentoPortale);
				pagamentoPortaleVersamento.setIdVersamento(idVersamento);
				this.getPagamentoPortaleVersamentoService().create(pagamentoPortaleVersamento);
			}
		}
	}

	private void deleteAllPagPortVers(PagamentoPortale pagamentoPortale)
			throws ServiceException, NotImplementedException {

		try {
			IExpression exp = this.getPagamentoPortaleVersamentoService().newExpression();
			CustomField field = new CustomField("id_pagamento_portale", Long.class, "id_pagamento_portale", new PagamentoPortaleVersamentoFieldConverter(this.getJdbcProperties().getDatabase()).toTable(it.govpay.orm.PagamentoPortaleVersamento.model()));
			exp.equals(field, pagamentoPortale.getId());
			this.getPagamentoPortaleVersamentoService().deleteAll(exp);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}

	private List<PagamentoPortaleVersamento> getAllPagPortVers(PagamentoPortale pagamentoPortale)
			throws ServiceException, NotImplementedException {

		try {
			IPaginatedExpression exp = this.getPagamentoPortaleVersamentoService().newPaginatedExpression();
			CustomField field = new CustomField("id_pagamento_portale", Long.class, "id_pagamento_portale", new PagamentoPortaleVersamentoFieldConverter(this.getJdbcProperties().getDatabase()).toTable(it.govpay.orm.PagamentoPortaleVersamento.model()));
			exp.equals(field, pagamentoPortale.getId());
			return this.getPagamentoPortaleVersamentoService().findAll(exp);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		}
	}
	
	public List<PagamentoPortaleVersamento> getAllPagPortVers(long idVersamento)
			throws ServiceException {

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IPaginatedExpression exp = this.getPagamentoPortaleVersamentoService().newPaginatedExpression();
			CustomField field = new CustomField("id_versamento", Long.class, "id_versamento", new PagamentoPortaleVersamentoFieldConverter(this.getJdbcProperties().getDatabase()).toTable(it.govpay.orm.PagamentoPortaleVersamento.model()));
			exp.equals(field, idVersamento);
			return this.getPagamentoPortaleVersamentoService().findAll(exp);
		} catch (ExpressionNotImplementedException| ExpressionException  | NotImplementedException e) {
			throw new ServiceException();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void ack(PagamentoPortale pagamento) throws ServiceException {
		it.govpay.orm.PagamentoPortale vo = PagamentoPortaleConverter.toVO(pagamento);
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			UpdateField ackField = new UpdateField(it.govpay.orm.PagamentoPortale.model().ACK, true);
			this.getPagamentoPortaleService().updateFields(this.getPagamentoPortaleService().convertToId(vo), ackField);
		} catch (NotFoundException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updatePagamento(PagamentoPortale pagamento) throws ServiceException {
		this.updatePagamento(pagamento, false);
	}

	public void updatePagamento(PagamentoPortale pagamento, boolean updateVersamenti) throws ServiceException {
		boolean oldAutocomit = this.isAutoCommit();
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction()); // TODO
			}
			
			this.setAutoCommit(false);
	
			it.govpay.orm.PagamentoPortale vo = PagamentoPortaleConverter.toVO(pagamento);
			try {
				this.getPagamentoPortaleService().update(this.getPagamentoPortaleService().convertToId(vo), vo);
				if(updateVersamenti) {
					this.deleteAllPagPortVers(pagamento);
					this.insertPagPortVers(pagamento);
				}
			} catch (NotFoundException e) {
				throw new ServiceException();
			} catch (NotImplementedException e) {
				throw new ServiceException();
			}
			this.commit();
		} catch (ServiceException e) {
			this.rollback();
			throw e;
		} finally {
			this.setAutoCommit(oldAutocomit);

			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateStatoPagamento(long idPagamento, String statoPagamentoPortale, String descrizioneStato, Boolean ack) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdPagamentoPortale idVO = new IdPagamentoPortale();
			idVO.setId(idPagamento);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			
			if(ack != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.PagamentoPortale.model().ACK, ack));
			
			if(statoPagamentoPortale != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.PagamentoPortale.model().STATO, statoPagamentoPortale.toString()));
			if(descrizioneStato != null) {
				if(descrizioneStato.length() > 1024)
					descrizioneStato = descrizioneStato.substring(0, 1021)+ "...";
				
				lstUpdateFields.add(new UpdateField(it.govpay.orm.PagamentoPortale.model().DESCRIZIONE_STATO, descrizioneStato));
			}

			this.getPagamentoPortaleService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera il pagamento identificato dalla chiave fisica
	 */
	public PagamentoPortale getPagamento(long id) throws ServiceException,NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			return PagamentoPortaleConverter.toDTO(((IDBPagamentoPortaleService)this.getPagamentoPortaleService()).get(id));
		} catch (MultipleResultException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera il pagamento identificato dal codSessione
	 */
	public PagamentoPortale getPagamentoFromCodSessione(String codSessione) throws ServiceException,NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdPagamentoPortale id = new IdPagamentoPortale();
			id.setIdSessione(codSessione);
			PagamentoPortale dto = PagamentoPortaleConverter.toDTO(this.getPagamentoPortaleService().get(id));

			return this.getPagamentoArricchito(dto);
		} catch (MultipleResultException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private PagamentoPortale getPagamentoArricchito(PagamentoPortale dto) throws ServiceException, NotImplementedException {
		List<PagamentoPortaleVersamento> allPagPortVers = this.getAllPagPortVers(dto);
		List<IdVersamento> idVersamento = new ArrayList<>();
		for(PagamentoPortaleVersamento vers: allPagPortVers) {
			idVersamento.add(vers.getIdVersamento());
		}
		dto.setIdVersamento(idVersamento);
		return dto;
	}

	/**
	 * Recupera il pagamento identificato dal codsessionepsp
	 */
	public PagamentoPortale getPagamentoFromCodSessionePsp(String codSessionePsp) throws ServiceException,NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getPagamentoPortaleService().newExpression();
			exp.equals(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PSP, codSessionePsp);
			PagamentoPortale dto = PagamentoPortaleConverter.toDTO(this.getPagamentoPortaleService().find(exp));
			return this.getPagamentoArricchito(dto);
		} catch (MultipleResultException e) {
			throw new ServiceException();
		} catch (NotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException();
		} catch (ExpressionException e) {
			throw new ServiceException();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
