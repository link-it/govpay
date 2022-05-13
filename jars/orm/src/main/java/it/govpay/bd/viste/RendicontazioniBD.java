package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.viste.filters.RendicontazioneFilter;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.bd.viste.model.converter.RendicontazioneConverter;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.orm.FR;
import it.govpay.orm.IdIncasso;
import it.govpay.orm.VistaRendicontazione;
import it.govpay.orm.dao.jdbc.JDBCVistaRendicontazioneServiceSearch;
import it.govpay.orm.dao.jdbc.converter.FRFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VistaRendicontazioneFieldConverter;
import it.govpay.orm.model.FRModel;
import it.govpay.orm.model.VistaRendicontazioneModel;

public class RendicontazioniBD extends BasicBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public RendicontazioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public RendicontazioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public RendicontazioneFilter newFilter() throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch());
	}
	
	public RendicontazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch(),simpleSearch);
	}

	public List<Rendicontazione> findAll(RendicontazioneFilter filter) throws ServiceException {
		return filter.isRicercaFR() ? this._findAllFR(filter) : this._findAll(filter);
	}

	public List<Rendicontazione> _findAll(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaRendicontazioneServiceSearch());
			}
			
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = this.getVistaRendicontazioneServiceSearch().findAll(filter.toPaginatedExpression());
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public long count(RendicontazioneFilter filter) throws ServiceException {
		if(filter.isRicercaFR())
			return this._countConLimit(filter);
		else 
			return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaRendicontazioneServiceSearch());
			}
			
			return this.getVistaRendicontazioneServiceSearch().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VistaRendicontazioneFieldConverter converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.RND_IUV));
			if(filter.isRicercaFR()) {
				sqlQueryObjectInterno.addSelectField(converter.toTable(model.FR_ID), "fr_id");
				sqlQueryObjectInterno.addSelectField(converter.toTable(model.FR_DATA_ORA_FLUSSO), "fr_data_ora_flusso");
			} else {
				sqlQueryObjectInterno.addSelectField(converter.toTable(model.RND_IUV), "id");
				sqlQueryObjectInterno.addSelectField(converter.toTable(model.RND_DATA), "rnd_data");
			}
			
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			if(filter.isRicercaFR()) {
				sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.FR_DATA_ORA_FLUSSO, true), false);
			} else {
				sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.RND_DATA, true), false);
			}
			
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			if(filter.isRicercaFR()) {
				sqlQueryObjectDistinctID.addSelectCountField("fr_id","id",true);
			} else {
				sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			}
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVistaRendicontazioneServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
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
	
	public List<Rendicontazione> _findAllFR(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
//				filter.setExpressionConstructor(this.getVistaRendicontazioneServiceSearch());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VistaRendicontazioneFieldConverter converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.FR_COD_FLUSSO));
			
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_ID, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_COD_BIC_RIVERSAMENTO, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_COD_DOMINIO, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_COD_FLUSSO, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_COD_PSP, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_DATA_ACQUISIZIONE, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_DATA_ORA_FLUSSO, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_DATA_REGOLAMENTO, true));
			if(!this.getJdbcProperties().getDatabase().equals(TipiDatabase.ORACLE))
				sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_DESCRIZIONE_STATO, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_IMPORTO_TOTALE_PAGAMENTI, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_IUR, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_NUMERO_PAGAMENTI, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_OBSOLETO, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_RAGIONE_SOCIALE_DOMINIO, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_RAGIONE_SOCIALE_PSP, true));
			sqlQueryObjectInterno.addSelectField(converter.toColumn(model.FR_STATO, true));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.FR_COD_FLUSSO) + ".fr_id_incasso");
			
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.FR_DATA_ACQUISIZIONE, true), false);
//			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.FR_COD_FLUSSO, true), false);
			
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			
			sqlQueryObjectDistinctID.setSelectDistinct(true);
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_ID, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_COD_BIC_RIVERSAMENTO, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_COD_DOMINIO, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_COD_FLUSSO, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_COD_PSP, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_DATA_ACQUISIZIONE, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_DATA_ORA_FLUSSO, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_DATA_REGOLAMENTO, false));
			if(!this.getJdbcProperties().getDatabase().equals(TipiDatabase.ORACLE))
				sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_DESCRIZIONE_STATO, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_IMPORTO_TOTALE_PAGAMENTI, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_IUR, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_NUMERO_PAGAMENTI, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_OBSOLETO, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_RAGIONE_SOCIALE_DOMINIO, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_RAGIONE_SOCIALE_PSP, false));
			sqlQueryObjectDistinctID.addSelectField(converter.toColumn(model.FR_STATO, false));
			sqlQueryObjectDistinctID.addSelectField("fr_id_incasso");
			
			if(filter.getOffset() != null)
				sqlQueryObjectDistinctID.setOffset(filter.getOffset());
			if(filter.getLimit() != null)
				sqlQueryObjectDistinctID.setLimit(filter.getLimit());
			
			sqlQueryObjectDistinctID.addOrderBy(converter.toColumn(model.FR_DATA_ACQUISIZIONE, false), false);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // ID
			returnTypes.add(model.FR_COD_BIC_RIVERSAMENTO.getFieldType()); 
			returnTypes.add(model.FR_COD_DOMINIO.getFieldType());
			returnTypes.add(model.FR_COD_FLUSSO.getFieldType());
			returnTypes.add(model.FR_COD_PSP.getFieldType());
			returnTypes.add(model.FR_DATA_ACQUISIZIONE.getFieldType());
			returnTypes.add(model.FR_DATA_ORA_FLUSSO.getFieldType());
			returnTypes.add(model.FR_DATA_REGOLAMENTO.getFieldType());
			if(!this.getJdbcProperties().getDatabase().equals(TipiDatabase.ORACLE))
				returnTypes.add(model.FR_DESCRIZIONE_STATO.getFieldType());
			returnTypes.add(model.FR_IMPORTO_TOTALE_PAGAMENTI.getFieldType());
			returnTypes.add(model.FR_IUR.getFieldType());
			returnTypes.add(model.FR_NUMERO_PAGAMENTI.getFieldType());
			returnTypes.add(model.FR_OBSOLETO.getFieldType());
			returnTypes.add(model.FR_RAGIONE_SOCIALE_DOMINIO.getFieldType());
			returnTypes.add(model.FR_RAGIONE_SOCIALE_PSP.getFieldType());
			returnTypes.add(model.FR_STATO.getFieldType());
			returnTypes.add(Long.class); // ID_INCASSO
			
			List<List<Object>> nativeQuery = this.getVistaRendicontazioneServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
			
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = new ArrayList<>();
			for (List<Object> row : nativeQuery) {
				int pos = 0;
				
				it.govpay.orm.VistaRendicontazione vo = new VistaRendicontazione();
				vo.setFrId(BasicBD.getValueOrNull(row.get(pos++), Long.class));
				vo.setFrCodBicRiversamento(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrCodDominio(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrCodFlusso(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrCodPsp(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrDataAcquisizione(BasicBD.getValueOrNull(row.get(pos++), Date.class));
				vo.setFrDataOraFlusso(BasicBD.getValueOrNull(row.get(pos++), Date.class));
				vo.setFrDataRegolamento(BasicBD.getValueOrNull(row.get(pos++), Date.class));
				if(!this.getJdbcProperties().getDatabase().equals(TipiDatabase.ORACLE))
					vo.setFrDescrizioneStato(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrImportoTotalePagamenti(BasicBD.getValueOrNull(row.get(pos++), Double.class));
				vo.setFrIur(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrNumeroPagamenti(BasicBD.getValueOrNull(row.get(pos++), Long.class));
				vo.setFrObsoleto(BasicBD.getValueOrNull(row.get(pos++), Boolean.class));
				vo.setFrRagioneSocialeDominio(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrRagioneSocialePsp(BasicBD.getValueOrNull(row.get(pos++), String.class));
				vo.setFrStato(BasicBD.getValueOrNull(row.get(pos++), String.class));
				
				Long idIncassoLong = BasicBD.getValueOrNull(row.get(pos++), Long.class);
				if(idIncassoLong != null) {
					IdIncasso idIncasso = new IdIncasso();
					idIncasso.setId(idIncassoLong);
					vo.setFrIdIncasso(idIncasso );
				}
				
				rendicontazioneVOLst.add(vo);
			}
			
//			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = this.getVistaRendicontazioneServiceSearch().findAll(filter.toPaginatedExpression());
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException | SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<>();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	/**
	 * Recupera la rendicontazione identificata dalla chiave fisica
	 */
	public Rendicontazione getRendicontazione(long id) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.VistaRendicontazione rendicontazione = ((JDBCVistaRendicontazioneServiceSearch)this.getVistaRendicontazioneServiceSearch()).get(id);
			return RendicontazioneConverter.toDTO(rendicontazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<it.govpay.bd.viste.model.Rendicontazione> getFr(String codDominio, String codFlusso, Date dataOraFlusso, Boolean obsoleto) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			IExpression expr = this.getVistaRendicontazioneServiceSearch().newExpression();
			expr.equals(VistaRendicontazione.model().FR_COD_FLUSSO, codFlusso);
			
			if(obsoleto != null) {
				expr.equals(VistaRendicontazione.model().FR_OBSOLETO, obsoleto);
			}
			
			if(codDominio != null) {
				expr.equals(VistaRendicontazione.model().FR_COD_DOMINIO, codDominio);
			}
			
			if(dataOraFlusso != null) {
				// controllo millisecondi
				Calendar cDataDa = Calendar.getInstance();
				cDataDa.setTime(dataOraFlusso);
				int currentMillis = cDataDa.get(Calendar.MILLISECOND);

				// in questo caso posso avere una data dove non sono stati impostati i millisecondi oppure millisecondi == 0, faccio una ricerca su un intervallo di un secondo
				if(currentMillis == 0) {
					Calendar cDataA = Calendar.getInstance();
					cDataA.setTime(dataOraFlusso);
					cDataA.set(Calendar.MILLISECOND, 999);
					Date dataA = cDataA.getTime();
					
					IExpression exprFR =  this.getFrService().newExpression();
					exprFR.equals(FR.model().COD_FLUSSO, codFlusso);
					
					if(obsoleto != null) {
						exprFR.equals(FR.model().OBSOLETO, obsoleto);
					}

					exprFR.greaterEquals(FR.model().DATA_ORA_FLUSSO, dataOraFlusso).and().lessEquals(FR.model().DATA_ORA_FLUSSO, dataA);
					IPaginatedExpression pagExpr = this.getFrService().toPaginatedExpression(exprFR);
					pagExpr.offset(0).limit(1);
					pagExpr.addOrder(FR.model().DATA_ORA_FLUSSO, SortOrder.DESC); // prendo il piu' recente
					
					FRFieldConverter converter = new FRFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
					FRModel model = it.govpay.orm.FR.model();
					CustomField cf = new CustomField("id", Long.class, "id", converter.toAliasTable(model));
					
					try {
						List<Object> select = this.getFrService().select(pagExpr, cf);
						
						Long idFR = 0L;
						for (Object obj : select) {
							idFR = (Long) obj;
						}
						expr.equals(VistaRendicontazione.model().FR_ID, idFR);
					} catch (NotFoundException e) {
						throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");
					}
				} else {
					expr.equals(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO, dataOraFlusso);
				}
			}
			
			IPaginatedExpression pagExpr = this.getVistaRendicontazioneServiceSearch().toPaginatedExpression(expr);
			pagExpr.offset(0);
			
			VistaRendicontazioneFieldConverter converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
			CustomField cf = new CustomField("id", Long.class, "id", converter.toAliasTable(model));
			pagExpr.addOrder(cf, SortOrder.ASC);
			
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = this.getVistaRendicontazioneServiceSearch().findAll(pagExpr);
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
			
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<it.govpay.bd.viste.model.Rendicontazione> ricercaRiscossioniDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza, Integer offset, Integer limit) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
			IExpression exp = this.getVistaRendicontazioneServiceSearch().newExpression();
			exp.equals(model.FR_COD_DOMINIO, codDominio).and();
			exp.equals(model.FR_OBSOLETO, false).and();
			if(dataRtDa != null) {
				exp.greaterEquals(model.FR_DATA_ACQUISIZIONE, dataRtDa);
			}
			exp.lessEquals(model.FR_DATA_ACQUISIZIONE, dataRtA);
			exp.equals(model.RND_STATO, StatoRendicontazione.OK.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(model.VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			
			IPaginatedExpression pagExp = this.getVistaRendicontazioneServiceSearch().toPaginatedExpression(exp);
			if(offset != null)
				pagExp.offset(offset);
			
			if(limit != null)
				pagExp.limit(limit);
			pagExp.addOrder(model.PAG_DATA_PAGAMENTO, SortOrder.ASC);
			
			List<Rendicontazione> entratePrevisteLst = new ArrayList<>();
			List<VistaRendicontazione> riscossioniVOLst = this.getVistaRendicontazioneServiceSearch().findAll(pagExp);
			for(it.govpay.orm.VistaRendicontazione riscossioneVO: riscossioniVOLst) {
				entratePrevisteLst.add(RendicontazioneConverter.toDTO(riscossioneVO));
			}
			return entratePrevisteLst;
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
	
	public long countRiscossioniDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
			IExpression exp = this.getVistaRendicontazioneServiceSearch().newExpression();
			exp.equals(model.FR_COD_DOMINIO, codDominio).and();
			exp.equals(model.FR_OBSOLETO, false).and();
			if(dataRtDa != null) {
				exp.greaterEquals(model.RND_DATA, dataRtDa);
			}
			exp.lessEquals(model.RND_DATA, dataRtA);
			exp.equals(model.RND_STATO, StatoRendicontazione.OK.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(model.VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			
			NonNegativeNumber count = this.getVistaRendicontazioneServiceSearch().count(exp);
			
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
}
