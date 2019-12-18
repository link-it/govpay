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

import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.VistaPagamentoPortale;
import it.govpay.orm.dao.jdbc.converter.VistaPagamentoPortaleFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.VistaPagamentoPortaleFetch;

/**     
 * JDBCVistaPagamentoPortaleServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVistaPagamentoPortaleServiceSearchImpl implements IJDBCServiceSearchWithId<VistaPagamentoPortale, IdPagamentoPortale, JDBCServiceManager> {

	private VistaPagamentoPortaleFieldConverter _vistaPagamentoPortaleFieldConverter = null;
	public VistaPagamentoPortaleFieldConverter getVistaPagamentoPortaleFieldConverter() {
		if(this._vistaPagamentoPortaleFieldConverter==null){
			this._vistaPagamentoPortaleFieldConverter = new VistaPagamentoPortaleFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._vistaPagamentoPortaleFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getVistaPagamentoPortaleFieldConverter();
	}

	private VistaPagamentoPortaleFetch vistaPagamentoPortaleFetch = new VistaPagamentoPortaleFetch();
	public VistaPagamentoPortaleFetch getVistaPagamentoPortaleFetch() {
		return this.vistaPagamentoPortaleFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getVistaPagamentoPortaleFetch();
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
	public IdPagamentoPortale convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaPagamentoPortale vistaPagamentoPortale) throws NotImplementedException, ServiceException, Exception{

		IdPagamentoPortale idPagamentoPortale = new IdPagamentoPortale();
		idPagamentoPortale.setIdSessione(vistaPagamentoPortale.getIdSessione());

		return idPagamentoPortale;
	}

	@Override
	public VistaPagamentoPortale get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_vistaPagamentoPortale = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdVistaPagamentoPortale(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_vistaPagamentoPortale,idMappingResolutionBehaviour);


	}

	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_vistaPagamentoPortale = this.findIdVistaPagamentoPortale(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_vistaPagamentoPortale != null && id_vistaPagamentoPortale > 0;

	}

	@Override
	public List<IdPagamentoPortale> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		List<IdPagamentoPortale> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(VistaPagamentoPortale.model().ID_SESSIONE);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (VistaPagamentoPortale)this.getFetch().fetch(jdbcProperties.getDatabase(), VistaPagamentoPortale.model(), map)));
			}
		} catch(NotFoundException e) {}

		return list;      

	}

	@Override
	public List<VistaPagamentoPortale> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		List<VistaPagamentoPortale> list = new ArrayList<>();

		
		try{
			List<IField> fields = new ArrayList<>();

			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaPagamentoPortale.model())));
			fields.add(VistaPagamentoPortale.model().COD_CANALE);
			fields.add(VistaPagamentoPortale.model().NOME);
			fields.add(VistaPagamentoPortale.model().IMPORTO);
			fields.add(VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO);
			fields.add(VistaPagamentoPortale.model().ID_SESSIONE);
			fields.add(VistaPagamentoPortale.model().ID_SESSIONE_PORTALE);
			fields.add(VistaPagamentoPortale.model().ID_SESSIONE_PSP);
			fields.add(VistaPagamentoPortale.model().STATO);
			fields.add(VistaPagamentoPortale.model().CODICE_STATO);
			fields.add(VistaPagamentoPortale.model().DESCRIZIONE_STATO);
			fields.add(VistaPagamentoPortale.model().PSP_REDIRECT_URL);
			fields.add(VistaPagamentoPortale.model().PSP_ESITO);
			fields.add(VistaPagamentoPortale.model().JSON_REQUEST);
			fields.add(VistaPagamentoPortale.model().WISP_ID_DOMINIO);
			fields.add(VistaPagamentoPortale.model().WISP_KEY_PA);
			fields.add(VistaPagamentoPortale.model().WISP_KEY_WISP);
			fields.add(VistaPagamentoPortale.model().WISP_HTML);
			fields.add(VistaPagamentoPortale.model().DATA_RICHIESTA);
			fields.add(VistaPagamentoPortale.model().URL_RITORNO);
			fields.add(VistaPagamentoPortale.model().COD_PSP);
			fields.add(VistaPagamentoPortale.model().TIPO_VERSAMENTO);
			fields.add(VistaPagamentoPortale.model().MULTI_BENEFICIARIO);
			fields.add(VistaPagamentoPortale.model().TIPO);
			fields.add(VistaPagamentoPortale.model().ACK);
			fields.add(VistaPagamentoPortale.model().PRINCIPAL);
			fields.add(VistaPagamentoPortale.model().TIPO_UTENZA);
			fields.add(VistaPagamentoPortale.model().DEBITORE_IDENTIFICATIVO);
			fields.add(new CustomField("id_applicazione", Long.class, "id_applicazione", this.getVistaPagamentoPortaleFieldConverter().toTable(VistaPagamentoPortale.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Object idApplicazioneObject = map.remove("id_applicazione");
				
				VistaPagamentoPortale pagamentoPortale = (VistaPagamentoPortale)this.getFetch().fetch(jdbcProperties.getDatabase(), VistaPagamentoPortale.model(), map); 
				
				if(idApplicazioneObject instanceof Long) {
					Long idApplicazione = (Long) idApplicazioneObject;
					
					it.govpay.orm.IdApplicazione id_pagamentoPortale_applicazione = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_pagamentoPortale_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idApplicazione, false);
					}else{
						id_pagamentoPortale_applicazione = new it.govpay.orm.IdApplicazione();
					}
					id_pagamentoPortale_applicazione.setId(idApplicazione);
					pagamentoPortale.setIdApplicazione(id_pagamentoPortale_applicazione);
				}
				
				list.add(pagamentoPortale);
			}
		} catch(NotFoundException e) {}

		return list;  

	}

	@Override
	public VistaPagamentoPortale find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
			throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		long id = this.findTableId(jdbcProperties, log, connection, sqlQueryObject, expression);
		if(id>0){
			return this.get(jdbcProperties, log, connection, sqlQueryObject, id, idMappingResolutionBehaviour);
		}else{
			throw new NotFoundException("Entry with id["+id+"] not found");
		}

	}

	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model());

		sqlQueryObject.addSelectCountField(this.getVistaPagamentoPortaleFieldConverter().toTable(VistaPagamentoPortale.model())+".id","tot",true);

		this._join(expression,sqlQueryObject);

		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id) throws NotFoundException, NotImplementedException, ServiceException,Exception {

		Long id_vistaPagamentoPortale = this.findIdVistaPagamentoPortale(jdbcProperties, log, connection, sqlQueryObject, id, true);
		return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_vistaPagamentoPortale);

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
							this.getVistaPagamentoPortaleFieldConverter(), field);

			return this._select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, sqlQueryObjectDistinct);

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
			List<Map<String,Object>> list = this._select(jdbcProperties, log, connection, sqlQueryObject, expression);
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
			return this._select(jdbcProperties, log, connection, sqlQueryObject, expression);
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
			return this._select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,functionField);
		}
	}

	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
			IExpression expression) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this._select(jdbcProperties, log, connection, sqlQueryObject, expression, null);
	}
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
			IExpression expression, ISQLQueryObject sqlQueryObjectDistinct) throws ServiceException,NotFoundException,NotImplementedException,Exception {

		List<Object> listaQuery = new ArrayList<>();
		List<JDBCObject> listaParams = new ArrayList<>();
		List<Object> returnField = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSelect(jdbcProperties, log, connection, sqlQueryObject, 
				expression, this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(), 
				listaQuery,listaParams);

		this._join(expression,sqlQueryObject);

		List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
				org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
				expression, this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(),
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

		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<>();
		List<JDBCObject> jdbcObjects = new ArrayList<>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnion(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(), 
				sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);

		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}

		List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(), 
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

		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<>();
		List<JDBCObject> jdbcObjects = new ArrayList<>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnionCount(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(), 
				sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);

		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}

		NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(), 
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
			return new JDBCExpression(this.getVistaPagamentoPortaleFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getVistaPagamentoPortaleFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id, VistaPagamentoPortale obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}

	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, VistaPagamentoPortale obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaPagamentoPortale obj, VistaPagamentoPortale imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdApplicazione()!=null && 
				imgSaved.getIdApplicazione()!=null){
			obj.getIdApplicazione().setId(imgSaved.getIdApplicazione().getId());
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

	}

	@Override
	public VistaPagamentoPortale get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}

	private VistaPagamentoPortale _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {

		IField idField = new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaPagamentoPortale.model()));

		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);

		expression.equals(idField, tableId);
		List<VistaPagamentoPortale> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);

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

		boolean existsVistaPagamentoPortale = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getVistaPagamentoPortaleFieldConverter().toTable(VistaPagamentoPortale.model()));
		sqlQueryObject.addSelectField(this.getVistaPagamentoPortaleFieldConverter().toColumn(VistaPagamentoPortale.model().COD_CANALE,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists vistaPagamentoPortale
		existsVistaPagamentoPortale = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
				new JDBCObject(tableId,Long.class));


		return existsVistaPagamentoPortale;

	}

	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{

		if(expression.inUseModel(VistaPagamentoPortale.model().ID_UO,false)){
			String tableName1 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model());
			String tableName2 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model().ID_UO);
			sqlQueryObject.addWhereCondition(tableName1+".id_uo="+tableName2+".id");
		}
		
		if(expression.inUseModel(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaPagamentoPortale.model().ID_UO,false)){
				String tableName1 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model());
				String tableName2 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model().ID_UO);
				sqlQueryObject.addFromTable(tableName2);
				sqlQueryObject.addWhereCondition(tableName1+".id_uo="+tableName2+".id");
			}

			String tableName1 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model().ID_UO);
			String tableName2 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
			
		}
		
		if(expression.inUseModel(VistaPagamentoPortale.model().ID_DOMINIO,false)){
			String tableName1 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model());
			String tableName2 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model().ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}
		
		if(expression.inUseModel(VistaPagamentoPortale.model().ID_APPLICAZIONE,false)){
			String tableName1 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model());
			String tableName2 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model().ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}
		
		if(expression.inUseModel(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO,false)){
			String tableName1 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model());
			String tableName2 = this.getVistaPagamentoPortaleFieldConverter().toAliasTable(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento="+tableName2+".id");
		}
        
	}

	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
		// Identificativi
		java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		Long longId = this.findIdVistaPagamentoPortale(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);

		return rootTableIdValues;
	}

	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		VistaPagamentoPortaleFieldConverter converter = this.getVistaPagamentoPortaleFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// VistaPagamentoPortale.model()
		mapTableToPKColumn.put(converter.toTable(VistaPagamentoPortale.model()),
				utilities.newList(
						new CustomField("id", Long.class, "id", converter.toTable(VistaPagamentoPortale.model()))
						));

		// VistaPagamentoPortale.model().ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VistaPagamentoPortale.model().ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamentoPortale.model().ID_APPLICAZIONE))
			));

		// VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamentoPortale.model().ID_TIPO_VERSAMENTO))
			));

		// VistaPagamentoPortale.model().ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaPagamentoPortale.model().ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamentoPortale.model().ID_DOMINIO))
			));

		// VistaPagamentoPortale.model().ID_UO
		mapTableToPKColumn.put(converter.toTable(VistaPagamentoPortale.model().ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamentoPortale.model().ID_UO))
			));

		// VistaPagamentoPortale.model().ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamentoPortale.model().ID_UO.ID_DOMINIO))
			));

		return mapTableToPKColumn;		
	}

	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {

		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaPagamentoPortaleFieldConverter().toTable(VistaPagamentoPortale.model())+".id");
		Class<?> objectIdClass = Long.class;

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model());

		this._join(paginatedExpression,sqlQueryObject);

		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

		return list;

	}

	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaPagamentoPortaleFieldConverter().toTable(VistaPagamentoPortale.model())+".id");
		Class<?> objectIdClass = Long.class;

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model());

		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getVistaPagamentoPortaleFieldConverter(), VistaPagamentoPortale.model(), objectIdClass, listaQuery);
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

		return inUse;

	}

	@Override
	public IdPagamentoPortale findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaPagamentoPortale
		sqlQueryObjectGet.addFromTable(this.getVistaPagamentoPortaleFieldConverter().toTable(VistaPagamentoPortale.model()));
		sqlQueryObjectGet.addSelectField(this.getVistaPagamentoPortaleFieldConverter().toColumn(VistaPagamentoPortale.model().ID_SESSIONE,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaPagamentoPortale
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaPagamentoPortale = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_vistaPagamentoPortale = new ArrayList<Class<?>>();
		listaFieldIdReturnType_vistaPagamentoPortale.add(VistaPagamentoPortale.model().ID_SESSIONE.getFieldType());

		it.govpay.orm.IdPagamentoPortale id_vistaPagamentoPortale = null;
		List<Object> listaFieldId_vistaPagamentoPortale = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_vistaPagamentoPortale, searchParams_vistaPagamentoPortale);
		if(listaFieldId_vistaPagamentoPortale==null || listaFieldId_vistaPagamentoPortale.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _vistaPagamentoPortale
			id_vistaPagamentoPortale = new it.govpay.orm.IdPagamentoPortale();
			id_vistaPagamentoPortale.setIdSessione((String)listaFieldId_vistaPagamentoPortale.get(0));
		}

		return id_vistaPagamentoPortale;

	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {

		return this.findIdVistaPagamentoPortale(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);

	}

	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
			String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{

		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
				sql,returnClassTypes,param);

	}

	protected Long findIdVistaPagamentoPortale(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
		
		if((id!=null && id.getId()!=null && id.getId()>0))
			return id.getId();

		// Object _vistaPagamentoPortale
		sqlQueryObjectGet.addFromTable(this.getVistaPagamentoPortaleFieldConverter().toTable(VistaPagamentoPortale.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition(this.getVistaPagamentoPortaleFieldConverter().toColumn(VistaPagamentoPortale.model().ID_SESSIONE,true)+"=?");

		// Recupero _vistaPagamentoPortale
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaPagamentoPortale = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdSessione(),VistaPagamentoPortale.model().ID_SESSIONE.getFieldType()),
		};
		Long id_vistaPagamentoPortale = null;
		try{
			id_vistaPagamentoPortale = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
					Long.class, searchParams_vistaPagamentoPortale);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_vistaPagamentoPortale==null || id_vistaPagamentoPortale<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_vistaPagamentoPortale;
	}
}
