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
package it.govpay.orm.dao.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.InUse;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.Union;
import org.openspcoop2.generic_project.beans.UnionExpression;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceSearchWithId;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.dao.jdbc.utils.IJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.generic_project.utils.UtilsTemplate;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;

import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.VersamentoIncasso;
import it.govpay.orm.dao.jdbc.converter.VersamentoIncassoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.VersamentoIncassoFetch;

/**     
 * JDBCVersamentoIncassoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVersamentoIncassoServiceSearchImpl implements IJDBCServiceSearchWithId<VersamentoIncasso, IdVersamento, JDBCServiceManager> {

	private VersamentoIncassoFieldConverter _versamentoIncassoFieldConverter = null;
	public VersamentoIncassoFieldConverter getVersamentoIncassoFieldConverter() {
		if(this._versamentoIncassoFieldConverter==null){
			this._versamentoIncassoFieldConverter = new VersamentoIncassoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._versamentoIncassoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getVersamentoIncassoFieldConverter();
	}
	
	private VersamentoIncassoFetch versamentoIncassoFetch = new VersamentoIncassoFetch();
	public VersamentoIncassoFetch getVersamentoIncassoFetch() {
		return this.versamentoIncassoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getVersamentoIncassoFetch();
	}
	
	
	private JDBCServiceManager jdbcServiceManager = null;

	@Override
	public void setServiceManager(JDBCServiceManager serviceManager) throws ServiceException{
		this.jdbcServiceManager = serviceManager;
	}
	
	@Override
	public JDBCServiceManager getServiceManager() throws ServiceException{
		return this.jdbcServiceManager;
	}
	

	@Override
	public IdVersamento convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VersamentoIncasso versamentoIncasso) throws NotImplementedException, ServiceException, Exception{
	
		IdVersamento idVersamentoIncasso = new IdVersamento();
		idVersamentoIncasso.setIdApplicazione(versamentoIncasso.getIdApplicazione());
		idVersamentoIncasso.setCodVersamentoEnte(versamentoIncasso.getCodVersamentoEnte());
		idVersamentoIncasso.setId(versamentoIncasso.getId());
	
		return idVersamentoIncasso;
	}
	
	@Override
	public VersamentoIncasso get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_versamentoIncasso = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdVersamentoIncasso(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_versamentoIncasso,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_versamentoIncasso = this.findIdVersamentoIncasso(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_versamentoIncasso != null && id_versamentoIncasso > 0;
		
	}
	
	@Override
	public List<IdVersamento> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}

		List<IdVersamento> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE);
			fields.add(VersamentoIncasso.model().COD_VERSAMENTO_ENTE);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (VersamentoIncasso)this.getVersamentoIncassoFetch().fetch(jdbcProperties.getDatabase(), VersamentoIncasso.model(), map)));
			}
		} catch(NotFoundException e) {}

		return list;
		
	}
	
	@Override
	public List<VersamentoIncasso> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		List<VersamentoIncasso> list = new ArrayList<>();
		try{
			List<IField> fields = new ArrayList<>();
			IField idField = new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VersamentoIncasso.model()));
	
			fields.add(idField);
			fields.add(VersamentoIncasso.model().COD_VERSAMENTO_ENTE);
			fields.add(VersamentoIncasso.model().NOME);
			fields.add(VersamentoIncasso.model().IMPORTO_TOTALE);
			fields.add(VersamentoIncasso.model().STATO_VERSAMENTO);
			fields.add(VersamentoIncasso.model().DESCRIZIONE_STATO);
			fields.add(VersamentoIncasso.model().AGGIORNABILE);
			fields.add(VersamentoIncasso.model().TASSONOMIA);
			fields.add(VersamentoIncasso.model().TASSONOMIA_AVVISO);
			fields.add(VersamentoIncasso.model().DATA_CREAZIONE);
			fields.add(VersamentoIncasso.model().DATA_VALIDITA);
			fields.add(VersamentoIncasso.model().DATA_SCADENZA);
			fields.add(VersamentoIncasso.model().DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fields.add(VersamentoIncasso.model().CAUSALE_VERSAMENTO);
			fields.add(VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO);
			fields.add(VersamentoIncasso.model().DEBITORE_TIPO);
			fields.add(VersamentoIncasso.model().DEBITORE_ANAGRAFICA);
			fields.add(VersamentoIncasso.model().DEBITORE_INDIRIZZO);
			fields.add(VersamentoIncasso.model().DEBITORE_CIVICO);
			fields.add(VersamentoIncasso.model().DEBITORE_CAP);
			fields.add(VersamentoIncasso.model().DEBITORE_LOCALITA);
			fields.add(VersamentoIncasso.model().DEBITORE_PROVINCIA);
			fields.add(VersamentoIncasso.model().DEBITORE_NAZIONE);
			fields.add(VersamentoIncasso.model().DEBITORE_EMAIL);
			fields.add(VersamentoIncasso.model().DEBITORE_TELEFONO);
			fields.add(VersamentoIncasso.model().DEBITORE_CELLULARE);
			fields.add(VersamentoIncasso.model().DEBITORE_FAX);
			fields.add(VersamentoIncasso.model().COD_LOTTO);
			fields.add(VersamentoIncasso.model().COD_VERSAMENTO_LOTTO);
			fields.add(VersamentoIncasso.model().COD_ANNO_TRIBUTARIO);
			fields.add(VersamentoIncasso.model().COD_BUNDLEKEY);
			fields.add(VersamentoIncasso.model().DATI_ALLEGATI);
			fields.add(VersamentoIncasso.model().INCASSO);
			fields.add(VersamentoIncasso.model().ANOMALIE);
			fields.add(VersamentoIncasso.model().IUV_VERSAMENTO);
			fields.add(VersamentoIncasso.model().NUMERO_AVVISO);
			fields.add(VersamentoIncasso.model().ACK);
			fields.add(VersamentoIncasso.model().ANOMALO);
			fields.add(VersamentoIncasso.model().IUV_PAGAMENTO);
			fields.add(VersamentoIncasso.model().DATA_PAGAMENTO);
			fields.add(VersamentoIncasso.model().IMPORTO_PAGATO);
			fields.add(VersamentoIncasso.model().IMPORTO_INCASSATO);
			fields.add(VersamentoIncasso.model().STATO_PAGAMENTO);
			fields.add(VersamentoIncasso.model().DIREZIONE);
			fields.add(VersamentoIncasso.model().DIVISIONE);
			fields.add(VersamentoIncasso.model().SMART_ORDER_DATE);
			fields.add(VersamentoIncasso.model().SMART_ORDER_RANK);
			fields.add(VersamentoIncasso.model().ID_SESSIONE);
			fields.add(VersamentoIncasso.model().SRC_DEBITORE_IDENTIFICATIVO);
			fields.add(VersamentoIncasso.model().SRC_IUV);
			fields.add(VersamentoIncasso.model().COD_RATA);
			fields.add(VersamentoIncasso.model().COD_DOCUMENTO);
			fields.add(VersamentoIncasso.model().TIPO);
			fields.add(VersamentoIncasso.model().DOC_DESCRIZIONE);
			fields.add(VersamentoIncasso.model().PROPRIETA);
	
			fields.add(new CustomField("id_applicazione", Long.class, "id_applicazione", this.getFieldConverter().toTable(VersamentoIncasso.model())));
			fields.add(new CustomField("id_dominio", Long.class, "id_dominio", this.getFieldConverter().toTable(VersamentoIncasso.model())));
			fields.add(new CustomField("id_uo", Long.class, "id_uo", this.getFieldConverter().toTable(VersamentoIncasso.model())));
			fields.add(new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", this.getFieldConverter().toTable(VersamentoIncasso.model())));
			fields.add(new CustomField("id_tipo_versamento_dominio", Long.class, "id_tipo_versamento_dominio", this.getFieldConverter().toTable(VersamentoIncasso.model())));
			
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
	
			for(Map<String, Object> map: returnMap) {
				Long idApplicazione = (Long)map.remove("id_applicazione");
				Long idDominio = (Long)map.remove("id_dominio");
				Object idUoObject = map.remove("id_uo");
				Object idTipoVersamentoObject = map.remove("id_tipo_versamento");
				Object idTipoVersamentoDominioObject = map.remove("id_tipo_versamento_dominio");
	
				VersamentoIncasso versamento = (VersamentoIncasso)this.getFetch().fetch(jdbcProperties.getDatabase(), VersamentoIncasso.model(), map);
	
				it.govpay.orm.IdApplicazione id_versamento_applicazione = new it.govpay.orm.IdApplicazione();
				id_versamento_applicazione.setId(idApplicazione);
				versamento.setIdApplicazione(id_versamento_applicazione);
	
				it.govpay.orm.IdDominio id_versamento_dominio = new it.govpay.orm.IdDominio();
				id_versamento_dominio.setId(idDominio);
				versamento.setIdDominio(id_versamento_dominio);
	
				if(idUoObject instanceof Long) {
					Long idUo = (Long) idUoObject;
					it.govpay.orm.IdUo id_versamento_ente = new it.govpay.orm.IdUo();
					id_versamento_ente.setId(idUo);
					versamento.setIdUo(id_versamento_ente);
				}
	
				if(idTipoVersamentoObject instanceof Long) {
					Long idTipoVersamento = (Long) idTipoVersamentoObject;
					it.govpay.orm.IdTipoVersamento id_versamento_tipoVersamento = new it.govpay.orm.IdTipoVersamento();
					id_versamento_tipoVersamento.setId(idTipoVersamento);
					versamento.setIdTipoVersamento(id_versamento_tipoVersamento);
				}
				
				if(idTipoVersamentoDominioObject instanceof Long) {
					Long idTipoVersamentoDominio = (Long) idTipoVersamentoDominioObject;
					it.govpay.orm.IdTipoVersamentoDominio id_versamento_tipoVersamentoDominio = new it.govpay.orm.IdTipoVersamentoDominio();
					id_versamento_tipoVersamentoDominio.setId(idTipoVersamentoDominio);
					versamento.setIdTipoVersamentoDominio(id_versamento_tipoVersamentoDominio);
				}
	
				list.add(versamento);
			}
		} catch(NotFoundException e) {}
	
		return list;
		
	}
	
	@Override
	public VersamentoIncasso find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<VersamentoIncasso> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model());
		
		sqlQueryObject.addSelectCountField(this.getVersamentoIncassoFieldConverter().toTable(VersamentoIncasso.model())+".id","tot");
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_versamentoIncasso = this.findIdVersamentoIncasso(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_versamentoIncasso);
		
	}

	@Override
	public List<Object> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, IField field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this.select(jdbcProperties, log, connection, sqlQueryObject,
								paginatedExpression, false, field);
	}
	
	@Override
	public List<Object> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, boolean distinct, IField field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		List<Map<String,Object>> map = 
			this.select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, distinct, new IField[]{field});
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.selectSingleObject(map);
	}
	
	@Override
	public List<Map<String,Object>> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, IField ... field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this.select(jdbcProperties, log, connection, sqlQueryObject,
								paginatedExpression, false, field);
	}
	
	@Override
	public List<Map<String,Object>> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, boolean distinct, IField ... field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,paginatedExpression,field);
		try{
		
			ISQLQueryObject sqlQueryObjectDistinct = 
						org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(distinct,sqlQueryObject, paginatedExpression, log,
												this.getVersamentoIncassoFieldConverter(), field);

			return _select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, sqlQueryObjectDistinct);
			
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,field);
		}
	}

	@Override
	public Object aggregate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		Map<String,Object> map = 
			this.aggregate(jdbcProperties, log, connection, sqlQueryObject, expression, new FunctionField[]{functionField});
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.selectAggregateObject(map,functionField);
	}
	
	@Override
	public Map<String,Object> aggregate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {													
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,expression,functionField);
		try{
			List<Map<String,Object>> list = _select(jdbcProperties, log, connection, sqlQueryObject, expression);
			return list.get(0);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,expression,functionField);
		}
	}

	@Override
	public List<Map<String,Object>> groupBy(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		if(expression.getGroupByFields().size()<=0){
			throw new ServiceException("GroupBy conditions not found in expression");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,expression,functionField);
		try{
			return _select(jdbcProperties, log, connection, sqlQueryObject, expression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,expression,functionField);
		}
	}
	

	@Override
	public List<Map<String,Object>> groupBy(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		if(paginatedExpression.getGroupByFields().size()<=0){
			throw new ServiceException("GroupBy conditions not found in expression");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,paginatedExpression,functionField);
		try{
			return _select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,functionField);
		}
	}
	
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return _select(jdbcProperties, log, connection, sqlQueryObject, expression, null);
	}
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression, ISQLQueryObject sqlQueryObjectDistinct) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		List<Object> listaQuery = new ArrayList<Object>();
		List<JDBCObject> listaParams = new ArrayList<JDBCObject>();
		List<Object> returnField = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSelect(jdbcProperties, log, connection, sqlQueryObject, 
        						expression, this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(),
        								listaQuery,listaParams,returnField);
		if(list!=null && list.size()>0){
			return list;
		}
		else{
			throw new NotFoundException("Not Found");
		}
	}
	
	@Override
	public List<Map<String,Object>> union(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException,Exception {		
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<ISQLQueryObject>();
		List<JDBCObject> jdbcObjects = new ArrayList<JDBCObject>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnion(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(), 
        								sqlQueryObjectInnerList, jdbcObjects, returnClassTypes, union, unionExpression);
        if(list!=null && list.size()>0){
			return list;
		}
		else{
			throw new NotFoundException("Not Found");
		}								
	}
	
	@Override
	public NonNegativeNumber unionCount(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException,Exception {		
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<ISQLQueryObject>();
		List<JDBCObject> jdbcObjects = new ArrayList<JDBCObject>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnionCount(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(), 
        								sqlQueryObjectInnerList, jdbcObjects, returnClassTypes, union, unionExpression);
        if(number!=null && number.longValue()>=0){
			return number;
		}
		else{
			throw new NotFoundException("Not Found");
		}
	}



	// -- ConstructorExpression	

	@Override
	public JDBCExpression newExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCExpression(this.getVersamentoIncassoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getVersamentoIncassoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public JDBCExpression toExpression(JDBCPaginatedExpression paginatedExpression, Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCExpression(paginatedExpression);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public JDBCPaginatedExpression toPaginatedExpression(JDBCExpression expression, Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(expression);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	
	
	// -- DB

	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, VersamentoIncasso obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, VersamentoIncasso obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VersamentoIncasso obj, VersamentoIncasso imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdTipoVersamentoDominio()!=null && 
				imgSaved.getIdTipoVersamentoDominio()!=null){
			obj.getIdTipoVersamentoDominio().setId(imgSaved.getIdTipoVersamentoDominio().getId());
			if(obj.getIdTipoVersamentoDominio().getIdDominio()!=null && 
					imgSaved.getIdTipoVersamentoDominio().getIdDominio()!=null){
				obj.getIdTipoVersamentoDominio().getIdDominio().setId(imgSaved.getIdTipoVersamentoDominio().getIdDominio().getId());
			}
			if(obj.getIdTipoVersamentoDominio().getIdTipoVersamento()!=null && 
					imgSaved.getIdTipoVersamentoDominio().getIdTipoVersamento()!=null){
				obj.getIdTipoVersamentoDominio().getIdTipoVersamento().setId(imgSaved.getIdTipoVersamentoDominio().getIdTipoVersamento().getId());
			}
		}
		if(obj.getIdTipoVersamento()!=null && 
				imgSaved.getIdTipoVersamento()!=null){
			obj.getIdTipoVersamento().setId(imgSaved.getIdTipoVersamento().getId());
		}
		if(obj.getIdDominio()!=null && 
				imgSaved.getIdDominio()!=null){
			obj.getIdDominio().setId(imgSaved.getIdDominio().getId());
		}
		if(obj.getIdUo()!=null && 
				imgSaved.getIdUo()!=null){
			obj.getIdUo().setId(imgSaved.getIdUo().getId());
			if(obj.getIdUo().getIdDominio()!=null && 
					imgSaved.getIdUo().getIdDominio()!=null){
				obj.getIdUo().getIdDominio().setId(imgSaved.getIdUo().getIdDominio().getId());
			}
		}
		if(obj.getIdApplicazione()!=null && 
				imgSaved.getIdApplicazione()!=null){
			obj.getIdApplicazione().setId(imgSaved.getIdApplicazione().getId());
		}
		if(obj.getIdPagamentoPortale()!=null && 
				imgSaved.getIdPagamentoPortale()!=null){
			obj.getIdPagamentoPortale().setId(imgSaved.getIdPagamentoPortale().getId());
		}
		if(obj.getIuv()!=null && 
				imgSaved.getIuv()!=null){
			obj.getIuv().setId(imgSaved.getIuv().getId());
		}
	}
	
	@Override
	public VersamentoIncasso get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private VersamentoIncasso _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		IField idField = new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VersamentoIncasso.model()));

		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);

		expression.equals(idField, tableId);
		List<VersamentoIncasso> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Id ["+tableId+"]");

		if(lst.size() > 1)
			throw new MultipleResultException("Id ["+tableId+"]");


		return lst.get(0);
	
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._exists(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
	
	private boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
				
		boolean existsVersamentoIncasso = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getVersamentoIncassoFieldConverter().toTable(VersamentoIncasso.model()));
		sqlQueryObject.addSelectField(this.getVersamentoIncassoFieldConverter().toColumn(VersamentoIncasso.model().COD_VERSAMENTO_ENTE,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists versamentoIncasso
		existsVersamentoIncasso = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsVersamentoIncasso;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
	
		if(expression.inUseModel(VersamentoIncasso.model().ID_UO,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_UO);
			sqlQueryObject.addWhereCondition(tableName1+".id_uo="+tableName2+".id");
		}
		
		if(expression.inUseModel(VersamentoIncasso.model().ID_UO.ID_DOMINIO,false)){
			
			if(!expression.inUseModel(VersamentoIncasso.model().ID_UO,false)){
				String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
				String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_UO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_uo="+tableName2+".id");
			}

			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_UO);
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_UO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
			
		}
		
		if(expression.inUseModel(VersamentoIncasso.model().ID_DOMINIO,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}

		if(expression.inUseModel(VersamentoIncasso.model().ID_APPLICAZIONE,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}

		if(expression.inUseModel(VersamentoIncasso.model().IUV,false)){
			String versamenti = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
			String iuv = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().IUV);
			sqlQueryObject.addWhereCondition(versamenti+".id_applicazione="+iuv+".id_applicazione");
			sqlQueryObject.addWhereCondition(versamenti+".cod_versamento_ente="+iuv+".cod_versamento_ente");
		}

		if(expression.inUseModel(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE,false)){
			String versamenti = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
			String pagPortVers = "pag_port_versamenti";
			String pagPort = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE);
			sqlQueryObject.addFromTable(pagPortVers);
			sqlQueryObject.addWhereCondition(versamenti+".id="+pagPortVers+".id_versamento");
			
			sqlQueryObject.addWhereCondition(pagPortVers+".id_pagamento_portale="+pagPort+".id");
		}
        
		if(expression.inUseModel(VersamentoIncasso.model().ID_TIPO_VERSAMENTO,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento="+tableName2+".id");
		}
		
		if(expression.inUseModel(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento_dominio="+tableName2+".id");
		}
		
		if(expression.inUseModel(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO,false)){
				String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
				String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento_dominio="+tableName2+".id");
			}

			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO);
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}
		
		if(expression.inUseModel(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO,false)){
			if(!expression.inUseModel(VersamentoIncasso.model().ID_TIPO_VERSAMENTO,false)){
				String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model());
				String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento_dominio="+tableName2+".id");
			}

			String tableName1 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO);
			String tableName2 = this.getFieldConverter().toAliasTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento="+tableName2+".id");
		}
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdVersamentoIncasso(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		VersamentoIncassoFieldConverter converter = this.getVersamentoIncassoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// VersamentoIncasso.model()
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model()))
			));

		// VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO))
			));

		// VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO))
			));

		// VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO))
			));

		// VersamentoIncasso.model().ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_TIPO_VERSAMENTO))
			));

		// VersamentoIncasso.model().ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_DOMINIO))
			));

		// VersamentoIncasso.model().ID_UO
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_UO))
			));

		// VersamentoIncasso.model().ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_UO.ID_DOMINIO))
			));

		// VersamentoIncasso.model().ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_APPLICAZIONE))
			));

		// VersamentoIncasso.model().ID_PAGAMENTO_PORTALE
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().ID_PAGAMENTO_PORTALE))
			));

		// VersamentoIncasso.model().IUV
		mapTableToPKColumn.put(converter.toTable(VersamentoIncasso.model().IUV),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VersamentoIncasso.model().IUV))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVersamentoIncassoFieldConverter().toTable(VersamentoIncasso.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVersamentoIncassoFieldConverter().toTable(VersamentoIncasso.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getVersamentoIncassoFieldConverter(), VersamentoIncasso.model(), objectIdClass, listaQuery);
		if(res!=null && (((Long) res).longValue()>0) ){
			return ((Long) res).longValue();
		}
		else{
			throw new NotFoundException("Not Found");
		}
		
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {
		return this._inUse(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}

	private InUse _inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {

		InUse inUse = new InUse();
		inUse.setInUse(false);
		
		// Delete this line when you have implemented the method
		int throwNotImplemented = 1;
		if(throwNotImplemented==1){
		        throw new NotImplementedException("NotImplemented");
		}
		// Delete this line when you have implemented the method

        return inUse;

	}
	
	@Override
	public IdVersamento findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
		
		// Object _versamento
		sqlQueryObjectGet.addFromTable(this.getFieldConverter().toTable(VersamentoIncasso.model()));
		sqlQueryObjectGet.addFromTable(this.getFieldConverter().toTable(VersamentoIncasso.model().ID_APPLICAZIONE));
		sqlQueryObjectGet.addSelectField(this.getFieldConverter().toColumn(VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE,true));
		sqlQueryObjectGet.addSelectField(this.getFieldConverter().toColumn(VersamentoIncasso.model().COD_VERSAMENTO_ENTE,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition(this.getFieldConverter().toTable(VersamentoIncasso.model())+".id=?");
		sqlQueryObjectGet.addWhereCondition(this.getFieldConverter().toTable(VersamentoIncasso.model())+".id_applicazione="+this.getFieldConverter().toTable(VersamentoIncasso.model().ID_APPLICAZIONE) + ".id");

		// Recupero _versamento
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_versamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_versamento = new ArrayList<>();
		listaFieldIdReturnType_versamento.add(VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE.getFieldType());
		listaFieldIdReturnType_versamento.add(VersamentoIncasso.model().COD_VERSAMENTO_ENTE.getFieldType());
		it.govpay.orm.IdVersamento id_versamento = null;
		List<Object> listaFieldId_versamento = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_versamento, searchParams_versamento);
		if(listaFieldId_versamento==null || listaFieldId_versamento.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			id_versamento = new it.govpay.orm.IdVersamento();
			IdApplicazione idApplicazione = new IdApplicazione();
			idApplicazione.setCodApplicazione((String)listaFieldId_versamento.get(0));
			id_versamento.setIdApplicazione(idApplicazione);
			id_versamento.setCodVersamentoEnte((String)listaFieldId_versamento.get(1));
		}
		
		return id_versamento;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdVersamentoIncasso(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdVersamentoIncasso(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_versamento = null;
		
		if((id!=null && id.getId()!=null && id.getId()>0))
			return id.getId();
		
		if(id.getIdApplicazione().getId() != null && id.getIdApplicazione().getId() > 0) {
			// Object _versamento
			sqlQueryObjectGet.addFromTable(this.getFieldConverter().toTable(VersamentoIncasso.model()));
			sqlQueryObjectGet.addSelectField("id");
			sqlQueryObjectGet.setANDLogicOperator(true);
//			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition("id_applicazione=?");
			sqlQueryObjectGet.addWhereCondition(this.getFieldConverter().toColumn(VersamentoIncasso.model().COD_VERSAMENTO_ENTE, true)+"=?");

			// Recupero _versamento
			searchParams_versamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdApplicazione().getId(),Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodVersamentoEnte(),VersamentoIncasso.model().COD_VERSAMENTO_ENTE.getFieldType())
			};


		} else {

			// Object _versamento
			sqlQueryObjectGet.addFromTable(this.getFieldConverter().toTable(VersamentoIncasso.model()));
			sqlQueryObjectGet.addFromTable(this.getFieldConverter().toTable(VersamentoIncasso.model().ID_APPLICAZIONE));
			sqlQueryObjectGet.addSelectField(this.getFieldConverter().toTable(VersamentoIncasso.model())+".id");
			sqlQueryObjectGet.setANDLogicOperator(true);
			sqlQueryObjectGet.setSelectDistinct(true);
			sqlQueryObjectGet.addWhereCondition(this.getFieldConverter().toColumn(VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE, true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getFieldConverter().toColumn(VersamentoIncasso.model().COD_VERSAMENTO_ENTE, true)+"=?");
			sqlQueryObjectGet.addWhereCondition(this.getFieldConverter().toTable(VersamentoIncasso.model())+".id_applicazione="+this.getFieldConverter().toTable(VersamentoIncasso.model().ID_APPLICAZIONE) + ".id");

			// Recupero _versamento
			searchParams_versamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdApplicazione().getCodApplicazione(),VersamentoIncasso.model().ID_APPLICAZIONE.COD_APPLICAZIONE.getFieldType()),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodVersamentoEnte(),VersamentoIncasso.model().COD_VERSAMENTO_ENTE.getFieldType())
			};

		}

		Long id_versamento = null;
		try{
			id_versamento = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
					Long.class, searchParams_versamento);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_versamento==null || id_versamento<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}

		return id_versamento;
	}
}
