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
import it.govpay.orm.IdAnagrafica;
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

import it.govpay.orm.Anagrafica;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCAnagraficaServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCAnagraficaServiceImpl extends JDBCAnagraficaServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Anagrafica, IdAnagrafica, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object anagrafica
		sqlQueryObjectInsert.addInsertTable(this.getAnagraficaFieldConverter().toTable(Anagrafica.model()));
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().RAGIONE_SOCIALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().COD_UNIVOCO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().INDIRIZZO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().CIVICO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().CAP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().LOCALITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().PROVINCIA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().NAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().EMAIL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().TELEFONO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().CELLULARE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().FAX,false),"?");

		// Insert anagrafica
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getAnagraficaFetch().getKeyGeneratorObject(Anagrafica.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getRagioneSociale(),Anagrafica.model().RAGIONE_SOCIALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getCodUnivoco(),Anagrafica.model().COD_UNIVOCO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getIndirizzo(),Anagrafica.model().INDIRIZZO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getCivico(),Anagrafica.model().CIVICO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getCap(),Anagrafica.model().CAP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getLocalita(),Anagrafica.model().LOCALITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getProvincia(),Anagrafica.model().PROVINCIA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getNazione(),Anagrafica.model().NAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getEmail(),Anagrafica.model().EMAIL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getTelefono(),Anagrafica.model().TELEFONO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getCellulare(),Anagrafica.model().CELLULARE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(anagrafica.getFax(),Anagrafica.model().FAX.getFieldType())
		);
		anagrafica.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAnagrafica oldId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdAnagrafica(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = anagrafica.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: anagrafica.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			anagrafica.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, anagrafica, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
		

		// Object anagrafica
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getAnagraficaFieldConverter().toTable(Anagrafica.model()));
		boolean isUpdate_anagrafica = true;
		java.util.List<JDBCObject> lstObjects_anagrafica = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().RAGIONE_SOCIALE,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getRagioneSociale(), Anagrafica.model().RAGIONE_SOCIALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().COD_UNIVOCO,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getCodUnivoco(), Anagrafica.model().COD_UNIVOCO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().INDIRIZZO,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getIndirizzo(), Anagrafica.model().INDIRIZZO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().CIVICO,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getCivico(), Anagrafica.model().CIVICO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().CAP,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getCap(), Anagrafica.model().CAP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().LOCALITA,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getLocalita(), Anagrafica.model().LOCALITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().PROVINCIA,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getProvincia(), Anagrafica.model().PROVINCIA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().NAZIONE,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getNazione(), Anagrafica.model().NAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().EMAIL,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getEmail(), Anagrafica.model().EMAIL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().TELEFONO,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getTelefono(), Anagrafica.model().TELEFONO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().CELLULARE,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getCellulare(), Anagrafica.model().CELLULARE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getAnagraficaFieldConverter().toColumn(Anagrafica.model().FAX,false), "?");
		lstObjects_anagrafica.add(new JDBCObject(anagrafica.getFax(), Anagrafica.model().FAX.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_anagrafica.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_anagrafica) {
			// Update anagrafica
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_anagrafica.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAnagrafica id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAnagraficaFieldConverter().toTable(Anagrafica.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getAnagraficaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAnagrafica id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAnagraficaFieldConverter().toTable(Anagrafica.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getAnagraficaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAnagrafica id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAnagraficaFieldConverter().toTable(Anagrafica.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getAnagraficaFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAnagraficaFieldConverter().toTable(Anagrafica.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getAnagraficaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAnagraficaFieldConverter().toTable(Anagrafica.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getAnagraficaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getAnagraficaFieldConverter().toTable(Anagrafica.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getAnagraficaFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAnagrafica oldId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, anagrafica,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, anagrafica,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, anagrafica,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, anagrafica,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Anagrafica anagrafica) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (anagrafica.getId()!=null) && (anagrafica.getId()>0) ){
			longId = anagrafica.getId();
		}
		else{
			IdAnagrafica idAnagrafica = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,anagrafica);
			longId = this.findIdAnagrafica(jdbcProperties,log,connection,sqlQueryObject,idAnagrafica,false);
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
		

		// Object anagrafica
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getAnagraficaFieldConverter().toTable(Anagrafica.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete anagrafica
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAnagrafica idAnagrafica) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdAnagrafica(jdbcProperties, log, connection, sqlQueryObject, idAnagrafica, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getAnagraficaFieldConverter()));

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
