/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.converter.RptConverter;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.orm.IdRpt;
import it.govpay.orm.RPT;
import it.govpay.orm.dao.jdbc.JDBCRPTService;
import it.govpay.orm.dao.jdbc.JDBCRPTServiceSearch;
import it.govpay.orm.dao.jdbc.converter.RPTFieldConverter;
import it.govpay.orm.model.RPTModel;

public class RptBD extends BasicBD {

	public RptBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RptBD(String idTransaction) {
		super(idTransaction);
	}
	
	public RptBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public RptBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera l'RPT identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rpt getRpt(long idRpt) throws ServiceException {
		return getRpt(idRpt, false);
	}
	public Rpt getRpt(long idRpt, boolean deep) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			RPT rptVO = ((JDBCRPTServiceSearch)this.getRptService()).get(idRpt);
			Rpt rpt = RptConverter.toDTO(rptVO);
			
			popolaRpt(deep, rpt);
			
			return rpt;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private void popolaRpt(boolean deep, Rpt rpt) throws ServiceException, NotFoundException {
		if(deep) {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			versamentiBD.setAtomica(false);
			rpt.setVersamento(versamentiBD.getVersamento(rpt.getIdVersamento(), deep));
			
			PagamentiBD pagamentiBD = new PagamentiBD(this);
			pagamentiBD.setAtomica(false);
			rpt.setPagamenti(pagamentiBD.getPagamenti(rpt.getId(), deep));
			
			if(rpt.getIdPagamentoPortale() != null) {
				PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(this);
				pagamentiPortaleBD.setAtomica(false);
				rpt.setPagamentoPortale(pagamentiPortaleBD.getPagamento(rpt.getIdPagamentoPortale()));
			}
		}
	}

	/**
	 * Recupera l'RPT identificato dal msg id
	 * 
	 * @param codMsgRichiesta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Rpt getRpt(String codMsgRichiesta) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRptService().newExpression();
			exp.equals(RPT.model().COD_MSG_RICHIESTA, codMsgRichiesta);
			RPT rptVO = this.getRptService().find(exp);
			return RptConverter.toDTO(rptVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Rpt getRpt(String codDominio, String iuv) throws NotFoundException, ServiceException {
		return this.getRpt(codDominio, iuv, false);
	}
	
	public Rpt getRpt(String codDominio, String iuv, boolean deep) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRptService().newExpression();
			exp.equals(RPT.model().COD_DOMINIO, codDominio);
			exp.equals(RPT.model().IUV, iuv);
			RPT rptVO = this.getRptService().find(exp);
			Rpt dto = RptConverter.toDTO(rptVO);
			
			popolaRpt(deep, dto);
			
			return dto;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Rpt getRpt(String codDominio, String iuv, String ccp) throws NotFoundException, ServiceException {
		return this.getRpt(codDominio, iuv, ccp, false);
	}
	
	public Rpt getRpt(String codDominio, String iuv, String ccp, boolean deep) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRptService().newExpression();
			exp.equals(RPT.model().COD_DOMINIO, codDominio);
			exp.equals(RPT.model().IUV, iuv);
			exp.equals(RPT.model().CCP, ccp);
			RPT rptVO = this.getRptService().find(exp);
			Rpt dto = RptConverter.toDTO(rptVO);
			
			popolaRpt(deep, dto);
			
			return dto;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	

	/**
	 * Inserisce l'RPT.
	 * 
	 * @param rpt
	 * @param documentoXml
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void insertRpt(Rpt rpt) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			RPT rptVo = RptConverter.toVO(rpt);
			this.getRptService().create(rptVo);
			rpt.setId(rptVo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Aggiorna lo stato di una RPT identificata dall'id
	 * @param idRpt
	 * @param stato
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void updateRpt(long idRpt, Rpt.StatoRpt stato, String descrizione, String codSessione, String pspRedirectUrl, Rpt.EsitoPagamento esito) throws NotFoundException, ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(stato!= null) 
				lstUpdateFields.add(new UpdateField(RPT.model().STATO, stato.toString()));
			lstUpdateFields.add(new UpdateField(RPT.model().DESCRIZIONE_STATO, descrizione));
			lstUpdateFields.add(new UpdateField(RPT.model().DATA_AGGIORNAMENTO_STATO, new Date()));
			if(codSessione != null)
				lstUpdateFields.add(new UpdateField(RPT.model().COD_SESSIONE, codSessione));
			if(pspRedirectUrl != null)
				lstUpdateFields.add(new UpdateField(RPT.model().PSP_REDIRECT_URL, pspRedirectUrl));
			if(esito!= null) 
				lstUpdateFields.add(new UpdateField(RPT.model().COD_ESITO_PAGAMENTO, esito.getCodifica()));

			((JDBCRPTService)this.getRptService()).updateFields(idRpt, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	/**
	 * Aggiorna lo stato di blocco una RPT identificata dall'id
	 * @param idRpt
	 * @param stato
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void sbloccaRpt(long idRpt, boolean statoBlocco,String descrizione ) throws NotFoundException, ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(RPT.model().BLOCCANTE, statoBlocco));
			if(StringUtils.isNotEmpty(descrizione))
				lstUpdateFields.add(new UpdateField(RPT.model().DESCRIZIONE_STATO, descrizione));

			((JDBCRPTService)this.getRptService()).updateFields(idRpt, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateRpt(Long id, Rpt rpt) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.RPT vo = RptConverter.toVO(rpt);
			IdRpt idRpt = this.getRptService().convertToId(vo);
			this.getRptService().update(idRpt, vo);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Rpt> getRptPendenti(List<String> codDomini) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IPaginatedExpression exp = this.getRptService().newPaginatedExpression();
			exp.in(RPT.model().COD_DOMINIO, codDomini);
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_ERRORE_INVIO_A_NODO.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_RIFIUTATA_NODO.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_RIFIUTATA_PSP.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RPT_ERRORE_INVIO_A_PSP.toString());
			exp.notEquals(RPT.model().STATO, Rpt.StatoRpt.RT_ACCETTATA_PA.toString());
			
			List<RPT> findAll = this.getRptService().findAll(exp);
			return RptConverter.toDTOList(findAll);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public RptFilter newFilter() throws ServiceException {
		return new RptFilter(this.getRptService());
	}
	
	public RptFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RptFilter(this.getRptService(),simpleSearch);
	}
	
	public long count(RptFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(RptFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getRptService());
			}
			
			return this.getRptService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(RptFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			RPTFieldConverter converter = new RPTFieldConverter(this.getJdbcProperties().getDatabase());
			RPTModel model = it.govpay.orm.RPT.model();
			/*
			SELECT count(distinct id) 
				FROM
				  (
				  SELECT versamenti.id
				  FROM versamenti
				  WHERE ...restrizioni di autorizzazione o ricerca...
				  ORDER BY data_richiesta 
				  LIMIT K
				  ) a
				);
			*/
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.IUV));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.IUV), "id");
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_MSG_RICHIESTA), "data_msg_richiesta");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_MSG_RICHIESTA, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getRptService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Rpt> findAll(RptFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getRptService());
			}
			
			List<Rpt> rptLst = new ArrayList<>();
			List<it.govpay.orm.RPT> rptVOLst = this.getRptService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.RPT rptVO: rptVOLst) {
				rptLst.add(RptConverter.toDTO(rptVO));
			}
			return rptLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public Rpt getRptByCodSessione(String codDominio, String idSession) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRptService().newExpression();
			
			if(codDominio != null) exp.equals(RPT.model().COD_DOMINIO, codDominio);
			exp.equals(RPT.model().COD_SESSIONE, idSession);
			
			RPT vo = this.getRptService().find(exp);
			return RptConverter.toDTO(vo);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Rpt> ricercaRtDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza, Integer offset, Integer limit) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRptService().newExpression();
			exp.equals(RPT.model().COD_DOMINIO, codDominio).and();
			if(dataRtDa != null) {
				exp.greaterEquals(RPT.model().DATA_MSG_RICEVUTA, dataRtDa);
			}
			exp.lessEquals(RPT.model().DATA_MSG_RICEVUTA, dataRtA);
			exp.in(RPT.model().COD_ESITO_PAGAMENTO, EsitoPagamento.PAGAMENTO_ESEGUITO.getCodifica(), EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO.getCodifica());
			exp.equals(RPT.model().STATO, StatoRpt.RT_ACCETTATA_PA.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(RPT.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			
			IPaginatedExpression pagExp = this.getRptService().toPaginatedExpression(exp);
			pagExp.offset(offset).limit(limit);
			pagExp.addOrder(RPT.model().DATA_MSG_RICEVUTA, SortOrder.ASC);
			
			List<Rpt> rptLst = new ArrayList<>();
			List<it.govpay.orm.RPT> rptVOLst = this.getRptService().findAll(pagExp);
			for(it.govpay.orm.RPT rptVO: rptVOLst) {
				Rpt rpt = RptConverter.toDTO(rptVO);
				
				try {
					popolaRpt(true, rpt);
				}catch (NotFoundException e) {} // pagamentoportale puo' non esserci
				
				rptLst.add(rpt);
			}
			
			return rptLst;
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		
	}
	
	public long countRtDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getRptService().newExpression();
			exp.equals(RPT.model().COD_DOMINIO, codDominio).and();
			if(dataRtDa != null) {
				exp.greaterEquals(RPT.model().DATA_MSG_RICEVUTA, dataRtDa);
			}
			exp.lessEquals(RPT.model().DATA_MSG_RICEVUTA, dataRtA);
			exp.in(RPT.model().COD_ESITO_PAGAMENTO, EsitoPagamento.PAGAMENTO_ESEGUITO.getCodifica(), EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO.getCodifica());
			exp.equals(RPT.model().STATO, StatoRpt.RT_ACCETTATA_PA.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(RPT.model().ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			
			NonNegativeNumber count = this.getRptService().count(exp);
			
			return count.longValue();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		
	}
	
	public long updateIdTracciatoMyPivotRtDominio(String codDominio, Date dataRtDa, Date dataRtA, Long idTracciato, List<String> listaTipiPendenza) throws ServiceException{
		String nomeColonnaIDTracciatoDaAggiornare = "id_tracciato_mypivot";
		
		return _updateIdTracciatoRtDominio(codDominio, dataRtDa, dataRtA, idTracciato, listaTipiPendenza, nomeColonnaIDTracciatoDaAggiornare);
	}
	
	public long updateIdTracciatoSecimRtDominio(String codDominio, Date dataRtDa, Date dataRtA, Long idTracciato, List<String> listaTipiPendenza) throws ServiceException{
		String nomeColonnaIDTracciatoDaAggiornare = "id_tracciato_secim";
		
		return _updateIdTracciatoRtDominio(codDominio, dataRtDa, dataRtA, idTracciato, listaTipiPendenza, nomeColonnaIDTracciatoDaAggiornare);
	}

	private long _updateIdTracciatoRtDominio(String codDominio, Date dataRtDa, Date dataRtA, Long idTracciato,
			List<String> listaTipiPendenza, String nomeColonnaIDTracciatoDaAggiornare) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			
			ISQLQueryObject sqlQueryObjectUpdate = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			RPTFieldConverter converter = new RPTFieldConverter(this.getJdbcProperties().getDatabase());
			RPTModel model = it.govpay.orm.RPT.model();
			
			List<Object> lst = new ArrayList<Object>();
//			java.util.List<JDBCObject> lstObjects_rpt = new java.util.ArrayList<>();
			
			sqlQueryObjectUpdate.addUpdateTable(converter.toTable(model.IUV));
			sqlQueryObjectUpdate.addUpdateField(nomeColonnaIDTracciatoDaAggiornare, "?");
			lst.add(idTracciato);
//			lstObjects_rpt.add(new JDBCObject(idTracciato, Long.class));
			sqlQueryObjectUpdate.setANDLogicOperator(true);
			
			sqlQueryObjectUpdate.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
			lst.add(codDominio);
			
			if(dataRtDa != null) {
				sqlQueryObjectUpdate.addWhereCondition(true,converter.toColumn(model.DATA_MSG_RICEVUTA, true) + " >= ? ");
				lst.add(dataRtDa);
			}
			
			if(dataRtA != null) {
				sqlQueryObjectUpdate.addWhereCondition(true,converter.toColumn(model.DATA_MSG_RICEVUTA, true) + " <= ? ");
				lst.add(dataRtA);
			}
			
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				String [] lista = listaTipiPendenza.toArray(new String[listaTipiPendenza.size()]);
				
				String tableNameRPT = converter.toAliasTable(model);
				String tableNameVersamenti = converter.toAliasTable(model.ID_VERSAMENTO);
				String tableNameTipiVersamento = converter.toAliasTable(model.ID_VERSAMENTO.ID_TIPO_VERSAMENTO);
				
				sqlQueryObjectUpdate.addFromTable(tableNameVersamenti);
				sqlQueryObjectUpdate.addWhereCondition(tableNameVersamenti+".id="+tableNameRPT+".id_versamento");
				sqlQueryObjectUpdate.addFromTable(tableNameTipiVersamento);
				
				sqlQueryObjectUpdate.addWhereCondition(tableNameTipiVersamento+".id="+tableNameVersamenti+".id_tipo_versamento");
				sqlQueryObjectUpdate.addWhereINCondition(converter.toColumn(model.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, true), true, lista);
			}
			
			String [] esitiS = { ""+EsitoPagamento.PAGAMENTO_ESEGUITO.getCodifica(), ""+EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO.getCodifica()};
			sqlQueryObjectUpdate.addWhereINCondition(converter.toColumn(model.COD_ESITO_PAGAMENTO, true), true, esitiS);
			
			sqlQueryObjectUpdate.addWhereCondition(true,converter.toColumn(model.STATO, true) + " = ? ");
			lst.add(StatoRpt.RT_ACCETTATA_PA.toString());
			
			String sql = sqlQueryObjectUpdate.createSQLUpdate();
			Object[] parameters = lst.toArray(new Object[lst.size()]);
			int count = ((JDBCRPTService) this.getRptService()).nativeUpdate(sql, parameters);
			return count;
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
	
	
}
