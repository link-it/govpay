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

import it.govpay.orm.IdPagamento;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.PagamentoFetch;

/**     
 * JDBCPagamentoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPagamentoServiceSearchImpl implements IJDBCServiceSearchWithId<Pagamento, IdPagamento, JDBCServiceManager> {

	private PagamentoFieldConverter _pagamentoFieldConverter = null;
	public PagamentoFieldConverter getPagamentoFieldConverter() {
		if(this._pagamentoFieldConverter==null){
			this._pagamentoFieldConverter = new PagamentoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._pagamentoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getPagamentoFieldConverter();
	}

	private PagamentoFetch pagamentoFetch = new PagamentoFetch();
	public PagamentoFetch getPagamentoFetch() {
		return this.pagamentoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return this.getPagamentoFetch();
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
	public IdPagamento convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Pagamento pagamento) throws NotImplementedException, ServiceException, Exception{

		IdPagamento idPagamento = new IdPagamento();
		idPagamento.setIdPagamento(pagamento.getId());

		return idPagamento;
	}

	@Override
	public Pagamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_pagamento = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdPagamento(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_pagamento,idMappingResolutionBehaviour);


	}

	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_pagamento = this.findIdPagamento(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_pagamento != null && id_pagamento > 0;

	}

	@Override
	public List<IdPagamento> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		List<IdPagamento> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getPagamentoFieldConverter().toTable(Pagamento.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (Pagamento)this.getPagamentoFetch().fetch(jdbcProperties.getDatabase(), Pagamento.model(), map)));
			}
		} catch(NotFoundException e) {}

		return list;

	}

	@Override
	public List<Pagamento> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		List<Pagamento> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getPagamentoFieldConverter().toTable(Pagamento.model())));
			fields.add(new CustomField("id_rpt", Long.class, "id_rpt", this.getPagamentoFieldConverter().toTable(Pagamento.model())));
			fields.add(new CustomField("id_singolo_versamento", Long.class, "id_singolo_versamento", this.getPagamentoFieldConverter().toTable(Pagamento.model())));
			fields.add(new CustomField("id_rr", Long.class, "id_rr", this.getPagamentoFieldConverter().toTable(Pagamento.model())));
			fields.add(new CustomField("id_incasso", Long.class, "id_incasso", this.getPagamentoFieldConverter().toTable(Pagamento.model())));
			fields.add(Pagamento.model().COD_DOMINIO);
			fields.add(Pagamento.model().STATO);
			fields.add(Pagamento.model().IUV);
			fields.add(Pagamento.model().INDICE_DATI);
			fields.add(Pagamento.model().IMPORTO_PAGATO);
			fields.add(Pagamento.model().DATA_ACQUISIZIONE);
			fields.add(Pagamento.model().IUR);
			fields.add(Pagamento.model().DATA_PAGAMENTO);
			fields.add(Pagamento.model().COMMISSIONI_PSP);
			fields.add(Pagamento.model().TIPO_ALLEGATO);
			fields.add(Pagamento.model().ALLEGATO);
			fields.add(Pagamento.model().DATA_ACQUISIZIONE_REVOCA);
			fields.add(Pagamento.model().CAUSALE_REVOCA);
			fields.add(Pagamento.model().DATI_REVOCA);
			fields.add(Pagamento.model().IMPORTO_REVOCATO);
			fields.add(Pagamento.model().ESITO_REVOCA);
			fields.add(Pagamento.model().DATI_ESITO_REVOCA);
			fields.add(Pagamento.model().TIPO);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Object id_rptObject = map.remove("id_rpt");

				Long id_singolo_versamento = null;
				Object idSingoloVersamentoObj = map.remove("id_singolo_versamento");

				if(idSingoloVersamentoObj instanceof Long)
					id_singolo_versamento = (Long) idSingoloVersamentoObj;

				Long idRR = null;
				Object idRRObj = map.remove("id_rr");

				if(idRRObj instanceof Long)
					idRR = (Long) idRRObj;

				Long idIncasso = null;
				Object idIncassoObj = map.remove("id_incasso");

				if(idIncassoObj instanceof Long)
					idIncasso = (Long) idIncassoObj;

				Pagamento pagamento = (Pagamento)this.getPagamentoFetch().fetch(jdbcProperties.getDatabase(), Pagamento.model(), map);

				if(id_rptObject instanceof Long){
					Long id_rpt = (Long) id_rptObject;
					it.govpay.orm.IdRpt id_pagamento_rpt = new it.govpay.orm.IdRpt();
					id_pagamento_rpt.setId(id_rpt);
					pagamento.setIdRPT(id_pagamento_rpt);
				}

				if(id_singolo_versamento != null){
					it.govpay.orm.IdSingoloVersamento id_pagamento_singoloVersamento = new it.govpay.orm.IdSingoloVersamento();
					id_pagamento_singoloVersamento.setId(id_singolo_versamento);
					pagamento.setIdSingoloVersamento(id_pagamento_singoloVersamento);
				}

				if(idRR != null) {
					it.govpay.orm.IdRr id_pagamento_rr = new it.govpay.orm.IdRr();
					id_pagamento_rr.setId(idRR);
					pagamento.setIdRr(id_pagamento_rr);
				}

				if(idIncasso != null) {
					it.govpay.orm.IdIncasso id_pagamento_incasso = new it.govpay.orm.IdIncasso();
					id_pagamento_incasso.setId(idIncasso);
					pagamento.setIdIncasso(id_pagamento_incasso);
				}


				list.add(pagamento);
			}
		} catch(NotFoundException e) {}

		return list;      

	}

	@Override
	public Pagamento find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
			throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<Pagamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);

	}

	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoFieldConverter(), Pagamento.model());

		sqlQueryObject.addSelectCountField(this.getPagamentoFieldConverter().toTable(Pagamento.model())+".id","tot");

		this._join(expression,sqlQueryObject);

		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoFieldConverter(), Pagamento.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws NotFoundException, NotImplementedException, ServiceException,Exception {

		Long id_pagamento = this.findIdPagamento(jdbcProperties, log, connection, sqlQueryObject, id, true);
		return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_pagamento);

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
							this.getPagamentoFieldConverter(), field);

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
				expression, this.getPagamentoFieldConverter(), Pagamento.model(), 
				listaQuery,listaParams);

		this._join(expression,sqlQueryObject);

		List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
				org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
				expression, this.getPagamentoFieldConverter(), Pagamento.model(),
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
				this.getPagamentoFieldConverter(), Pagamento.model(), 
				sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);

		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}

		List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter(), Pagamento.model(), 
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
				this.getPagamentoFieldConverter(), Pagamento.model(), 
				sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);

		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}

		NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter(), Pagamento.model(), 
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
			return new JDBCExpression(this.getPagamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getPagamentoFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, Pagamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}

	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Pagamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		this._mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Pagamento obj, Pagamento imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdRPT()!=null && 
				imgSaved.getIdRPT()!=null){
			obj.getIdRPT().setId(imgSaved.getIdRPT().getId());
		}
		if(obj.getIdSingoloVersamento()!=null && 
				imgSaved.getIdSingoloVersamento()!=null){
			obj.getIdSingoloVersamento().setId(imgSaved.getIdSingoloVersamento().getId());
			if(obj.getIdSingoloVersamento().getIdVersamento()!=null && 
					imgSaved.getIdSingoloVersamento().getIdVersamento()!=null){
				obj.getIdSingoloVersamento().getIdVersamento().setId(imgSaved.getIdSingoloVersamento().getIdVersamento().getId());
				if(obj.getIdSingoloVersamento().getIdVersamento().getIdApplicazione()!=null && 
						imgSaved.getIdSingoloVersamento().getIdVersamento().getIdApplicazione()!=null){
					obj.getIdSingoloVersamento().getIdVersamento().getIdApplicazione().setId(imgSaved.getIdSingoloVersamento().getIdVersamento().getIdApplicazione().getId());
				}
			}
			if(obj.getIdSingoloVersamento().getIdTributo()!=null && 
					imgSaved.getIdSingoloVersamento().getIdTributo()!=null){
				obj.getIdSingoloVersamento().getIdTributo().setId(imgSaved.getIdSingoloVersamento().getIdTributo().getId());
				if(obj.getIdSingoloVersamento().getIdTributo().getIdDominio()!=null && 
						imgSaved.getIdSingoloVersamento().getIdTributo().getIdDominio()!=null){
					obj.getIdSingoloVersamento().getIdTributo().getIdDominio().setId(imgSaved.getIdSingoloVersamento().getIdTributo().getIdDominio().getId());
				}
				if(obj.getIdSingoloVersamento().getIdTributo().getIdTipoTributo()!=null && 
						imgSaved.getIdSingoloVersamento().getIdTributo().getIdTipoTributo()!=null){
					obj.getIdSingoloVersamento().getIdTributo().getIdTipoTributo().setId(imgSaved.getIdSingoloVersamento().getIdTributo().getIdTipoTributo().getId());
				}
			}
		}
		if(obj.getIdRr()!=null && 
				imgSaved.getIdRr()!=null){
			obj.getIdRr().setId(imgSaved.getIdRr().getId());
		}
		if(obj.getIdIncasso()!=null && 
				imgSaved.getIdIncasso()!=null){
			obj.getIdIncasso().setId(imgSaved.getIdIncasso().getId());
		}

	}

	@Override
	public Pagamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}

	private Pagamento _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {

		IField idField = new CustomField("id", Long.class, "id", this.getPagamentoFieldConverter().toTable(Pagamento.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);

		expression.equals(idField, tableId);
		List<Pagamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);

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

		boolean existsPagamento = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getPagamentoFieldConverter().toTable(Pagamento.model()));
		sqlQueryObject.addSelectField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IMPORTO_PAGATO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists pagamento
		existsPagamento = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
				new JDBCObject(tableId,Long.class));


		return existsPagamento;

	}

	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{

		if(expression.inUseModel(Pagamento.model().ID_RPT,false)){
			String tableName1 = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model());
			String tableName2 = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_RPT);
			sqlQueryObject.addWhereCondition(tableName1+".id_rpt="+tableName2+".id");

		}

		if(expression.inUseModel(Pagamento.model().ID_RR,false)){
			String tableName1 = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model());
			String tableName2 = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_RR);
			sqlQueryObject.addWhereCondition(tableName1+".id_rr="+tableName2+".id");

		}
		
		if(expression.inUseModel(Pagamento.model().ID_INCASSO,false)){
			String tableName1 = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model());
			String tableName2 = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_INCASSO);
			sqlQueryObject.addWhereCondition(tableName1+".id_incasso="+tableName2+".id");

		}
		
		String tablePagamenti = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model());
		String tableSingoliVersamenti = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_SINGOLO_VERSAMENTO);
		String tableVersamenti = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO);
		String tableApplicazioni = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE);
		String tableUo = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO);
		String tableDomini = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO);
		String tableTipoVersamento = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO);
		String tableTributi = this.getPagamentoFieldConverter().toAliasTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO);
		
		boolean joinSV = false;
		boolean joinV = false;
		boolean joinUO = false;
		
		
		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
			if(!sqlQueryObject.getTablesName().contains(tableSingoliVersamenti)) {
				sqlQueryObject.addFromTable(tableSingoliVersamenti);
			}
			
			if(!joinSV) {
				sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
				joinSV = true;
			}
		}
		
		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO,false)){
			if(!sqlQueryObject.getTablesName().contains(tableVersamenti)) {
				sqlQueryObject.addFromTable(tableVersamenti);
			}
			
			if(!joinV) {
				sqlQueryObject.addWhereCondition(tableSingoliVersamenti+".id_versamento="+tableVersamenti+".id");
				joinV = true;
			}

			if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
				if(!sqlQueryObject.getTablesName().contains(tableSingoliVersamenti)) {
					sqlQueryObject.addFromTable(tableSingoliVersamenti);
				}
				
				if(!joinSV) {
					sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
					joinSV = true;
				}
			}
		}
		
		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE,false) ||
			expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO,false) ||
			expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO,false)){
		
			if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE,false)){
				sqlQueryObject.addWhereCondition(tableVersamenti+".id_applicazione="+tableApplicazioni+".id");
			}
			
			if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO,false)){
				if(!joinUO) {
					sqlQueryObject.addWhereCondition(tableVersamenti+".id_uo="+tableUo+".id");
					joinUO = true;
				}
			} 
			
			if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO,false)){
				sqlQueryObject.addWhereCondition(tableVersamenti+".id_tipo_versamento="+tableTipoVersamento+".id");
			}
			
			if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO,false)){
				if(!sqlQueryObject.getTablesName().contains(tableVersamenti)) {
					sqlQueryObject.addFromTable(tableVersamenti);
				}
				if(!joinV) {
					sqlQueryObject.addWhereCondition(tableSingoliVersamenti+".id_versamento="+tableVersamenti+".id");
					joinV = true;
				}

				if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
					if(!sqlQueryObject.getTablesName().contains(tableSingoliVersamenti)) {
						sqlQueryObject.addFromTable(tableSingoliVersamenti);
					}
					if(!joinSV) {
						sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
						joinSV = true;
					}
				}
			}
		}
		
		
//		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE,false)){
//			sqlQueryObject.addWhereCondition(tableVersamenti+".id_applicazione="+tableApplicazioni+".id");
//
//			
//			if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO,false)){
//				sqlQueryObject.addFromTable(tableVersamenti);
//				sqlQueryObject.addWhereCondition(tableSingoliVersamenti+".id_versamento="+tableVersamenti+".id");
//
//				if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
//					sqlQueryObject.addFromTable(tableSingoliVersamenti);
//					sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
//
//				}
//			}
//		}
//		
//		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO,false)){
//			sqlQueryObject.addWhereCondition(tableVersamenti+".id_uo="+tableUo+".id");
//			
//			if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO,false)){
//				sqlQueryObject.addFromTable(tableVersamenti);
//				sqlQueryObject.addWhereCondition(tableSingoliVersamenti+".id_versamento="+tableVersamenti+".id");
//
//				if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
//					sqlQueryObject.addFromTable(tableSingoliVersamenti);
//					sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
//
//				}
//			}
//		}
		
		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableUo+".id_dominio="+tableDomini+".id");
			
			if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO,false)){
				if(!sqlQueryObject.getTablesName().contains(tableUo)) {
					sqlQueryObject.addFromTable(tableUo);
				}
				if(!joinUO) {
					sqlQueryObject.addWhereCondition(tableVersamenti+".id_uo="+tableUo+".id");
					joinUO = true;
				}
				
				if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO,false)){
					if(!sqlQueryObject.getTablesName().contains(tableVersamenti)) {
						sqlQueryObject.addFromTable(tableVersamenti);
					}
					if(!joinV) {
						sqlQueryObject.addWhereCondition(tableSingoliVersamenti+".id_versamento="+tableVersamenti+".id");
						joinV = true;
					}
					
					if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
						if(!sqlQueryObject.getTablesName().contains(tableSingoliVersamenti)) {
							sqlQueryObject.addFromTable(tableSingoliVersamenti);
						}
						if(!joinSV) {
							sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
							joinSV = true;
						}
					}
				}
			}
		}
		
		
//		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO,false)){
//			sqlQueryObject.addWhereCondition(tableVersamenti+".id_tipo_versamento="+tableTipoVersamento+".id");
//			
//			if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO,false)){
//				sqlQueryObject.addFromTable(tableVersamenti);
//				sqlQueryObject.addWhereCondition(tableSingoliVersamenti+".id_versamento="+tableVersamenti+".id");
//
//				if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
//					sqlQueryObject.addFromTable(tableSingoliVersamenti);
//					sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
//
//				}
//			}
//		}

		if(expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO,false)){
			sqlQueryObject.addWhereCondition(tableSingoliVersamenti+".id_tributo="+tableTributi+".id");
			
			if(!expression.inUseModel(Pagamento.model().ID_SINGOLO_VERSAMENTO,false)){
				if(!sqlQueryObject.getTablesName().contains(tableSingoliVersamenti)) {
					sqlQueryObject.addFromTable(tableSingoliVersamenti);
				}
				if(!joinSV) {
					sqlQueryObject.addWhereCondition(tablePagamenti+".id_singolo_versamento="+tableSingoliVersamenti+".id");
					joinSV = true;
				}
			}
		}

	}

	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
		// Identificativi
		java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		Long longId = this.findIdPagamento(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);

		return rootTableIdValues;
	}

	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{

		PagamentoFieldConverter converter = this.getPagamentoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// Pagamento.model()
		mapTableToPKColumn.put(converter.toTable(Pagamento.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model()))
			));

		// Pagamento.model().ID_RPT
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_RPT),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_RPT))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO))
			));

		// Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// Pagamento.model().ID_RR
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_RR),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_RR))
			));

		// Pagamento.model().ID_INCASSO
		mapTableToPKColumn.put(converter.toTable(Pagamento.model().ID_INCASSO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(Pagamento.model().ID_INCASSO))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {

		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getPagamentoFieldConverter().toTable(Pagamento.model())+".id");
		Class<?> objectIdClass = Long.class;

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
				this.getPagamentoFieldConverter(), Pagamento.model());

		this._join(paginatedExpression,sqlQueryObject);

		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
				this.getPagamentoFieldConverter(), Pagamento.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

		return list;

	}

	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getPagamentoFieldConverter().toTable(Pagamento.model())+".id");
		Class<?> objectIdClass = Long.class;

		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoFieldConverter(), Pagamento.model());

		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
				this.getPagamentoFieldConverter(), Pagamento.model(), objectIdClass, listaQuery);
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
	public IdPagamento findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _pagamento
		sqlQueryObjectGet.addFromTable(this.getPagamentoFieldConverter().toTable(Pagamento.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _pagamento
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_pagamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_pagamento = new ArrayList<>();
		listaFieldIdReturnType_pagamento.add(Long.class);

		it.govpay.orm.IdPagamento id_pagamento = null;
		List<Object> listaFieldId_pagamento = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_pagamento, searchParams_pagamento);
		if(listaFieldId_pagamento==null || listaFieldId_pagamento.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _pagamento
			id_pagamento = new it.govpay.orm.IdPagamento();
			id_pagamento.setIdPagamento((Long)listaFieldId_pagamento.get(0));
		}

		return id_pagamento;

	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {

		return this.findIdPagamento(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);

	}

	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
			String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{

		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
				sql,returnClassTypes,param);

	}

	protected Long findIdPagamento(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		if(id.getId() != null && id.getId() > 0)
			return id.getId();

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _pagamento
		sqlQueryObjectGet.addFromTable(this.getPagamentoFieldConverter().toTable(Pagamento.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _pagamento
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_pagamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdPagamento(),Long.class),
		};
		Long id_pagamento = null;
		try{
			id_pagamento = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
					Long.class, searchParams_pagamento);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_pagamento==null || id_pagamento<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}

		return id_pagamento;
	}
}
