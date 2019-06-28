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
import it.govpay.orm.IdPromemoria;
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

import it.govpay.orm.Promemoria;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCPromemoriaServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPromemoriaServiceImpl extends JDBCPromemoriaServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Promemoria, IdPromemoria, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Promemoria promemoria, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _versamento
		Long id_versamento = null;
		it.govpay.orm.IdVersamento idLogic_versamento = null;
		idLogic_versamento = promemoria.getIdVersamento();
		if(idLogic_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento = idLogic_versamento.getId();
				if(id_versamento==null || id_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object promemoria
		sqlQueryObjectInsert.addInsertTable(this.getPromemoriaFieldConverter().toTable(Promemoria.model()));
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DATA_CREAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DEBITORE_EMAIL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().ALLEGA_PDF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DATA_AGGIORNAMENTO_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DATA_PROSSIMA_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().TENTATIVI_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField("id_versamento","?");

		// Insert promemoria
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getPromemoriaFetch().getKeyGeneratorObject(Promemoria.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getTipo(),Promemoria.model().TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getDataCreazione(),Promemoria.model().DATA_CREAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getStato(),Promemoria.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getDescrizioneStato(),Promemoria.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getDebitoreEmail(),Promemoria.model().DEBITORE_EMAIL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getOggetto(),Promemoria.model().OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getMessaggio(),Promemoria.model().MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getAllegaPdf(),Promemoria.model().ALLEGA_PDF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getDataAggiornamentoStato(),Promemoria.model().DATA_AGGIORNAMENTO_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getDataProssimaSpedizione(),Promemoria.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(promemoria.getTentativiSpedizione(),Promemoria.model().TENTATIVI_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_versamento,Long.class)
		);
		promemoria.setId(id);
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria oldId, Promemoria promemoria, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdPromemoria(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = promemoria.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: promemoria.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			promemoria.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, promemoria, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Promemoria promemoria, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _promemoria_versamento
		Long id_promemoria_versamento = null;
		it.govpay.orm.IdVersamento idLogic_promemoria_versamento = null;
		idLogic_promemoria_versamento = promemoria.getIdVersamento();
		if(idLogic_promemoria_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_promemoria_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_promemoria_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_promemoria_versamento = idLogic_promemoria_versamento.getId();
				if(id_promemoria_versamento==null || id_promemoria_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object promemoria
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getPromemoriaFieldConverter().toTable(Promemoria.model()));
		boolean isUpdate_promemoria = true;
		java.util.List<JDBCObject> lstObjects_promemoria = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().TIPO,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getTipo(), Promemoria.model().TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DATA_CREAZIONE,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getDataCreazione(), Promemoria.model().DATA_CREAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().STATO,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getStato(), Promemoria.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getDescrizioneStato(), Promemoria.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DEBITORE_EMAIL,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getDebitoreEmail(), Promemoria.model().DEBITORE_EMAIL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().OGGETTO,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getOggetto(), Promemoria.model().OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().MESSAGGIO,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getMessaggio(), Promemoria.model().MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().ALLEGA_PDF,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getAllegaPdf(), Promemoria.model().ALLEGA_PDF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DATA_AGGIORNAMENTO_STATO,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getDataAggiornamentoStato(), Promemoria.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().DATA_PROSSIMA_SPEDIZIONE,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getDataProssimaSpedizione(), Promemoria.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPromemoriaFieldConverter().toColumn(Promemoria.model().TENTATIVI_SPEDIZIONE,false), "?");
		lstObjects_promemoria.add(new JDBCObject(promemoria.getTentativiSpedizione(), Promemoria.model().TENTATIVI_SPEDIZIONE.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_promemoria.add(new JDBCObject(id_promemoria_versamento, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_promemoria.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_promemoria) {
			// Update promemoria
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_promemoria.toArray(new JDBCObject[]{}));
		}
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPromemoriaFieldConverter().toTable(Promemoria.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPromemoriaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPromemoriaFieldConverter().toTable(Promemoria.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPromemoriaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPromemoriaFieldConverter().toTable(Promemoria.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPromemoriaFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPromemoriaFieldConverter().toTable(Promemoria.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPromemoriaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPromemoriaFieldConverter().toTable(Promemoria.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPromemoriaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPromemoriaFieldConverter().toTable(Promemoria.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPromemoriaFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria oldId, Promemoria promemoria, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, promemoria,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, promemoria,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Promemoria promemoria, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, promemoria,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, promemoria,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Promemoria promemoria) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (promemoria.getId()!=null) && (promemoria.getId()>0) ){
			longId = promemoria.getId();
		}
		else{
			IdPromemoria idPromemoria = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,promemoria);
			longId = this.findIdPromemoria(jdbcProperties,log,connection,sqlQueryObject,idPromemoria,false);
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
		

		// Object promemoria
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getPromemoriaFieldConverter().toTable(Promemoria.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete promemoria
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPromemoria idPromemoria) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdPromemoria(jdbcProperties, log, connection, sqlQueryObject, idPromemoria, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getPromemoriaFieldConverter()));

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
