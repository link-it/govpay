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

import org.openspcoop2.utils.sql.ISQLQueryObject;

import org.apache.log4j.Logger;

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdCanale;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.beans.UpdateModel;

import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;

import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;

import it.govpay.orm.Canale;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCCanaleServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCCanaleServiceImpl extends JDBCCanaleServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Canale, IdCanale, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Canale canale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _psp
		Long id_psp = null;
		it.govpay.orm.IdPsp idLogic_psp = null;
		idLogic_psp = canale.getIdPsp();
		if(idLogic_psp!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_psp = ((JDBCPspServiceSearch)(this.getServiceManager().getPspServiceSearch())).findTableId(idLogic_psp, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_psp = idLogic_psp.getId();
				if(id_psp==null || id_psp<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object canale
		sqlQueryObjectInsert.addInsertTable(this.getCanaleFieldConverter().toTable(Canale.model()));
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().COD_CANALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().COD_INTERMEDIARIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().TIPO_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().MODELLO_PAGAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().DISPONIBILITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().DESCRIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().CONDIZIONI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().URL_INFO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getCanaleFieldConverter().toColumn(Canale.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_psp","?");

		// Insert canale
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getCanaleFetch().getKeyGeneratorObject(Canale.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getCodCanale(),Canale.model().COD_CANALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getCodIntermediario(),Canale.model().COD_INTERMEDIARIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getTipoVersamento(),Canale.model().TIPO_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getModelloPagamento(),Canale.model().MODELLO_PAGAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getDisponibilita(),Canale.model().DISPONIBILITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getDescrizione(),Canale.model().DESCRIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getCondizioni(),Canale.model().CONDIZIONI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getUrlInfo(),Canale.model().URL_INFO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(canale.getAbilitato(),Canale.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_psp,Long.class)
		);
		canale.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdCanale oldId, Canale canale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdCanale(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = canale.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: canale.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			canale.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, canale, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Canale canale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObjectInsert.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectGet = sqlQueryObjectDelete.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObjectGet.newSQLQueryObject();
		
		boolean setIdMappingResolutionBehaviour = 
			(idMappingResolutionBehaviour==null) ||
			org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) ||
			org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour);
			

		// Object _canale_psp
		Long id_canale_psp = null;
		it.govpay.orm.IdPsp idLogic_canale_psp = null;
		idLogic_canale_psp = canale.getIdPsp();
		if(idLogic_canale_psp!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_canale_psp = ((JDBCPspServiceSearch)(this.getServiceManager().getPspServiceSearch())).findTableId(idLogic_canale_psp, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_canale_psp = idLogic_canale_psp.getId();
				if(id_canale_psp==null || id_canale_psp<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object canale
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getCanaleFieldConverter().toTable(Canale.model()));
		boolean isUpdate_canale = true;
		java.util.List<JDBCObject> lstObjects_canale = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().COD_CANALE,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getCodCanale(), Canale.model().COD_CANALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().COD_INTERMEDIARIO,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getCodIntermediario(), Canale.model().COD_INTERMEDIARIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().TIPO_VERSAMENTO,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getTipoVersamento(), Canale.model().TIPO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().MODELLO_PAGAMENTO,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getModelloPagamento(), Canale.model().MODELLO_PAGAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().DISPONIBILITA,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getDisponibilita(), Canale.model().DISPONIBILITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().DESCRIZIONE,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getDescrizione(), Canale.model().DESCRIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().CONDIZIONI,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getCondizioni(), Canale.model().CONDIZIONI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().URL_INFO,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getUrlInfo(), Canale.model().URL_INFO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getCanaleFieldConverter().toColumn(Canale.model().ABILITATO,false), "?");
		lstObjects_canale.add(new JDBCObject(canale.getAbilitato(), Canale.model().ABILITATO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_psp","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_canale.add(new JDBCObject(id_canale_psp, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_canale.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_canale) {
			// Update canale
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_canale.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdCanale id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getCanaleFieldConverter().toTable(Canale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getCanaleFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdCanale id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getCanaleFieldConverter().toTable(Canale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getCanaleFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdCanale id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getCanaleFieldConverter().toTable(Canale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getCanaleFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getCanaleFieldConverter().toTable(Canale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getCanaleFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getCanaleFieldConverter().toTable(Canale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getCanaleFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getCanaleFieldConverter().toTable(Canale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getCanaleFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdCanale oldId, Canale canale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, canale,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, canale,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Canale canale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, canale,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, canale,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Canale canale) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (canale.getId()!=null) && (canale.getId()>0) ){
			longId = canale.getId();
		}
		else{
			IdCanale idCanale = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,canale);
			longId = this.findIdCanale(jdbcProperties,log,connection,sqlQueryObject,idCanale,false);
			if(longId == null){
				return; // entry not exists
			}
		}		
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object canale
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getCanaleFieldConverter().toTable(Canale.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete canale
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdCanale idCanale) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdCanale(jdbcProperties, log, connection, sqlQueryObject, idCanale, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getCanaleFieldConverter()));

	}

	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {

		java.util.List<Long> lst = this.findAllTableIds(jdbcProperties, log, connection, sqlQueryObject, new JDBCPaginatedExpression(expression));
		
		for(Long id : lst) {
			this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		}
		
		return new NonNegativeNumber(lst.size());
	
	}



	// -- DB
	
	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotImplementedException, Exception {
		this._delete(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
}
