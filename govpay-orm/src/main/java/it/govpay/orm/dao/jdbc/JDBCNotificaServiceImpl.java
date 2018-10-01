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
import it.govpay.orm.IdNotifica;
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

import it.govpay.orm.Notifica;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCNotificaServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCNotificaServiceImpl extends JDBCNotificaServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Notifica, IdNotifica, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Notifica notifica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _applicazione
		Long id_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_applicazione = null;
		idLogic_applicazione = notifica.getIdApplicazione();
		if(idLogic_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_applicazione = idLogic_applicazione.getId();
				if(id_applicazione==null || id_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rpt
		Long id_rpt = null;
		it.govpay.orm.IdRpt idLogic_rpt = null;
		idLogic_rpt = notifica.getIdRpt();
		if(idLogic_rpt!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findTableId(idLogic_rpt, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rpt = idLogic_rpt.getId();
				if(id_rpt==null || id_rpt<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rr
		Long id_rr = null;
		it.govpay.orm.IdRr idLogic_rr = null;
		idLogic_rr = notifica.getIdRr();
		if(idLogic_rr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rr = ((JDBCRRServiceSearch)(this.getServiceManager().getRRServiceSearch())).findTableId(idLogic_rr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rr = idLogic_rr.getId();
				if(id_rr==null || id_rr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object notifica
		sqlQueryObjectInsert.addInsertTable(this.getNotificaFieldConverter().toTable(Notifica.model()));
		sqlQueryObjectInsert.addInsertField(this.getNotificaFieldConverter().toColumn(Notifica.model().TIPO_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaFieldConverter().toColumn(Notifica.model().DATA_CREAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaFieldConverter().toColumn(Notifica.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaFieldConverter().toColumn(Notifica.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaFieldConverter().toColumn(Notifica.model().DATA_AGGIORNAMENTO_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaFieldConverter().toColumn(Notifica.model().DATA_PROSSIMA_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaFieldConverter().toColumn(Notifica.model().TENTATIVI_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField("id_applicazione","?");
		sqlQueryObjectInsert.addInsertField("id_rpt","?");
		sqlQueryObjectInsert.addInsertField("id_rr","?");

		// Insert notifica
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getNotificaFetch().getKeyGeneratorObject(Notifica.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notifica.getTipoEsito(),Notifica.model().TIPO_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notifica.getDataCreazione(),Notifica.model().DATA_CREAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notifica.getStato(),Notifica.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notifica.getDescrizioneStato(),Notifica.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notifica.getDataAggiornamentoStato(),Notifica.model().DATA_AGGIORNAMENTO_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notifica.getDataProssimaSpedizione(),Notifica.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notifica.getTentativiSpedizione(),Notifica.model().TENTATIVI_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_rpt,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_rr,Long.class)
		);
		notifica.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica oldId, Notifica notifica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdNotifica(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = notifica.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: notifica.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			notifica.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, notifica, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Notifica notifica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _notifica_applicazione
		Long id_notifica_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_notifica_applicazione = null;
		idLogic_notifica_applicazione = notifica.getIdApplicazione();
		if(idLogic_notifica_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_notifica_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_notifica_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_notifica_applicazione = idLogic_notifica_applicazione.getId();
				if(id_notifica_applicazione==null || id_notifica_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _notifica_rpt
		Long id_notifica_rpt = null;
		it.govpay.orm.IdRpt idLogic_notifica_rpt = null;
		idLogic_notifica_rpt = notifica.getIdRpt();
		if(idLogic_notifica_rpt!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_notifica_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findTableId(idLogic_notifica_rpt, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_notifica_rpt = idLogic_notifica_rpt.getId();
				if(id_notifica_rpt==null || id_notifica_rpt<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _notifica_rr
		Long id_notifica_rr = null;
		it.govpay.orm.IdRr idLogic_notifica_rr = null;
		idLogic_notifica_rr = notifica.getIdRr();
		if(idLogic_notifica_rr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_notifica_rr = ((JDBCRRServiceSearch)(this.getServiceManager().getRRServiceSearch())).findTableId(idLogic_notifica_rr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_notifica_rr = idLogic_notifica_rr.getId();
				if(id_notifica_rr==null || id_notifica_rr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object notifica
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getNotificaFieldConverter().toTable(Notifica.model()));
		boolean isUpdate_notifica = true;
		java.util.List<JDBCObject> lstObjects_notifica = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaFieldConverter().toColumn(Notifica.model().TIPO_ESITO,false), "?");
		lstObjects_notifica.add(new JDBCObject(notifica.getTipoEsito(), Notifica.model().TIPO_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaFieldConverter().toColumn(Notifica.model().DATA_CREAZIONE,false), "?");
		lstObjects_notifica.add(new JDBCObject(notifica.getDataCreazione(), Notifica.model().DATA_CREAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaFieldConverter().toColumn(Notifica.model().STATO,false), "?");
		lstObjects_notifica.add(new JDBCObject(notifica.getStato(), Notifica.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaFieldConverter().toColumn(Notifica.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_notifica.add(new JDBCObject(notifica.getDescrizioneStato(), Notifica.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaFieldConverter().toColumn(Notifica.model().DATA_AGGIORNAMENTO_STATO,false), "?");
		lstObjects_notifica.add(new JDBCObject(notifica.getDataAggiornamentoStato(), Notifica.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaFieldConverter().toColumn(Notifica.model().DATA_PROSSIMA_SPEDIZIONE,false), "?");
		lstObjects_notifica.add(new JDBCObject(notifica.getDataProssimaSpedizione(), Notifica.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaFieldConverter().toColumn(Notifica.model().TENTATIVI_SPEDIZIONE,false), "?");
		lstObjects_notifica.add(new JDBCObject(notifica.getTentativiSpedizione(), Notifica.model().TENTATIVI_SPEDIZIONE.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_rpt","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_rr","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_notifica.add(new JDBCObject(id_notifica_applicazione, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_notifica.add(new JDBCObject(id_notifica_rpt, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_notifica.add(new JDBCObject(id_notifica_rr, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_notifica.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_notifica) {
			// Update notifica
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_notifica.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaFieldConverter().toTable(Notifica.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getNotificaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaFieldConverter().toTable(Notifica.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getNotificaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaFieldConverter().toTable(Notifica.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getNotificaFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaFieldConverter().toTable(Notifica.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getNotificaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaFieldConverter().toTable(Notifica.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getNotificaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaFieldConverter().toTable(Notifica.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getNotificaFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica oldId, Notifica notifica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, notifica,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, notifica,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Notifica notifica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, notifica,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, notifica,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Notifica notifica) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (notifica.getId()!=null) && (notifica.getId()>0) ){
			longId = notifica.getId();
		}
		else{
			IdNotifica idNotifica = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,notifica);
			longId = this.findIdNotifica(jdbcProperties,log,connection,sqlQueryObject,idNotifica,false);
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
		

		// Object notifica
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getNotificaFieldConverter().toTable(Notifica.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete notifica
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica idNotifica) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdNotifica(jdbcProperties, log, connection, sqlQueryObject, idNotifica, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getNotificaFieldConverter()));

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
