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

import org.slf4j.Logger;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdTracciato;
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

import it.govpay.orm.Tracciato;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCTracciatoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTracciatoServiceImpl extends JDBCTracciatoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Tracciato, IdTracciato, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object tracciato
		sqlQueryObjectInsert.addInsertTable(this.getTracciatoFieldConverter().toTable(Tracciato.model()));
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_CARICAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_COMPLETAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().BEAN_DATI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().FILE_NAME_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().FILE_NAME_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_ESITO,false),"?");

		// Insert tracciato
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getTracciatoFetch().getKeyGeneratorObject(Tracciato.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getCodDominio(),Tracciato.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getTipo(),Tracciato.model().TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getStato(),Tracciato.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getDescrizioneStato(),Tracciato.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getDataCaricamento(),Tracciato.model().DATA_CARICAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getDataCompletamento(),Tracciato.model().DATA_COMPLETAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getBeanDati(),Tracciato.model().BEAN_DATI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getFileNameRichiesta(),Tracciato.model().FILE_NAME_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getRawRichiesta(),Tracciato.model().RAW_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getFileNameEsito(),Tracciato.model().FILE_NAME_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tracciato.getRawEsito(),Tracciato.model().RAW_ESITO.getFieldType())
		);
		tracciato.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato oldId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdTracciato(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = tracciato.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: tracciato.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			tracciato.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciato, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			


		// Object tracciato
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getTracciatoFieldConverter().toTable(Tracciato.model()));
		boolean isUpdate_tracciato = true;
		java.util.List<JDBCObject> lstObjects_tracciato = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().COD_DOMINIO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getCodDominio(), Tracciato.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().TIPO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getTipo(), Tracciato.model().TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().STATO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getStato(), Tracciato.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getDescrizioneStato(), Tracciato.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_CARICAMENTO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getDataCaricamento(), Tracciato.model().DATA_CARICAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().DATA_COMPLETAMENTO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getDataCompletamento(), Tracciato.model().DATA_COMPLETAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().BEAN_DATI,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getBeanDati(), Tracciato.model().BEAN_DATI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().FILE_NAME_RICHIESTA,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getFileNameRichiesta(), Tracciato.model().FILE_NAME_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_RICHIESTA,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getRawRichiesta(), Tracciato.model().RAW_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().FILE_NAME_ESITO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getFileNameEsito(), Tracciato.model().FILE_NAME_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTracciatoFieldConverter().toColumn(Tracciato.model().RAW_ESITO,false), "?");
		lstObjects_tracciato.add(new JDBCObject(tracciato.getRawEsito(), Tracciato.model().RAW_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_tracciato.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_tracciato) {
			// Update tracciato
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_tracciato.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTracciatoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTracciatoFieldConverter().toTable(Tracciato.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTracciatoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato oldId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, tracciato,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciato,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Tracciato tracciato, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tracciato,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tracciato,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tracciato tracciato) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (tracciato.getId()!=null) && (tracciato.getId()>0) ){
			longId = tracciato.getId();
		}
		else{
			IdTracciato idTracciato = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,tracciato);
			longId = this.findIdTracciato(jdbcProperties,log,connection,sqlQueryObject,idTracciato,false);
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
		

		// Object tracciato
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getTracciatoFieldConverter().toTable(Tracciato.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete tracciato
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTracciato idTracciato) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdTracciato(jdbcProperties, log, connection, sqlQueryObject, idTracciato, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getTracciatoFieldConverter()));

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
