/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.orm.dao.jdbc;

import java.sql.Connection;

import org.openspcoop2.utils.sql.ISQLQueryObject;

import org.apache.log4j.Logger;

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdSla;
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

import it.govpay.orm.SLA;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCSLAServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCSLAServiceImpl extends JDBCSLAServiceSearchImpl
	implements IJDBCServiceCRUDWithId<SLA, IdSla, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SLA sla, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object sla
		sqlQueryObjectInsert.addInsertTable(this.getSLAFieldConverter().toTable(SLA.model()));
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().DESCRIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().TIPO_EVENTO_INIZIALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().SOTTOTIPO_EVENTO_INIZIALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().TIPO_EVENTO_FINALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().SOTTOTIPO_EVENTO_FINALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().TEMPO_A,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().TEMPO_B,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().TOLLERANZA_A,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSLAFieldConverter().toColumn(SLA.model().TOLLERANZA_B,false),"?");

		// Insert sla
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getSLAFetch().getKeyGeneratorObject(SLA.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getDescrizione(),SLA.model().DESCRIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getTipoEventoIniziale(),SLA.model().TIPO_EVENTO_INIZIALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getSottotipoEventoIniziale(),SLA.model().SOTTOTIPO_EVENTO_INIZIALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getTipoEventoFinale(),SLA.model().TIPO_EVENTO_FINALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getSottotipoEventoFinale(),SLA.model().SOTTOTIPO_EVENTO_FINALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getTempoA(),SLA.model().TEMPO_A.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getTempoB(),SLA.model().TEMPO_B.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getTolleranzaA(),SLA.model().TOLLERANZA_A.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(sla.getTolleranzaB(),SLA.model().TOLLERANZA_B.getFieldType())
		);
		sla.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSla oldId, SLA sla, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdSLA(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = sla.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: sla.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			sla.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, sla, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SLA sla, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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


		// Object sla
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getSLAFieldConverter().toTable(SLA.model()));
		boolean isUpdate_sla = true;
		java.util.List<JDBCObject> lstObjects_sla = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().DESCRIZIONE,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getDescrizione(), SLA.model().DESCRIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().TIPO_EVENTO_INIZIALE,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getTipoEventoIniziale(), SLA.model().TIPO_EVENTO_INIZIALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().SOTTOTIPO_EVENTO_INIZIALE,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getSottotipoEventoIniziale(), SLA.model().SOTTOTIPO_EVENTO_INIZIALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().TIPO_EVENTO_FINALE,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getTipoEventoFinale(), SLA.model().TIPO_EVENTO_FINALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().SOTTOTIPO_EVENTO_FINALE,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getSottotipoEventoFinale(), SLA.model().SOTTOTIPO_EVENTO_FINALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().TEMPO_A,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getTempoA(), SLA.model().TEMPO_A.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().TEMPO_B,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getTempoB(), SLA.model().TEMPO_B.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().TOLLERANZA_A,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getTolleranzaA(), SLA.model().TOLLERANZA_A.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSLAFieldConverter().toColumn(SLA.model().TOLLERANZA_B,false), "?");
		lstObjects_sla.add(new JDBCObject(sla.getTolleranzaB(), SLA.model().TOLLERANZA_B.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_sla.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_sla) {
			// Update sla
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_sla.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSla id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSLAFieldConverter().toTable(SLA.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSLAFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSla id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSLAFieldConverter().toTable(SLA.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSLAFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSla id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSLAFieldConverter().toTable(SLA.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSLAFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSLAFieldConverter().toTable(SLA.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSLAFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSLAFieldConverter().toTable(SLA.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSLAFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSLAFieldConverter().toTable(SLA.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSLAFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSla oldId, SLA sla, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, sla,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, sla,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SLA sla, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, sla,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, sla,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SLA sla) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (sla.getId()!=null) && (sla.getId()>0) ){
			longId = sla.getId();
		}
		else{
			IdSla idSLA = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,sla);
			longId = this.findIdSLA(jdbcProperties,log,connection,sqlQueryObject,idSLA,false);
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
		

		// Object sla
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getSLAFieldConverter().toTable(SLA.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete sla
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSla idSLA) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdSLA(jdbcProperties, log, connection, sqlQueryObject, idSLA, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getSLAFieldConverter()));

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
