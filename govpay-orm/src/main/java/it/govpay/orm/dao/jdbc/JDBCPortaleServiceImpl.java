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
import it.govpay.orm.IdPortale;
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

import it.govpay.orm.PortaleApplicazione;
import it.govpay.orm.Portale;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCPortaleServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPortaleServiceImpl extends JDBCPortaleServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Portale, IdPortale, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Portale portale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _stazione
		Long id_stazione = null;
		it.govpay.orm.IdStazione idLogic_stazione = null;
		idLogic_stazione = portale.getIdStazione();
		if(idLogic_stazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_stazione = ((JDBCStazioneServiceSearch)(this.getServiceManager().getStazioneServiceSearch())).findTableId(idLogic_stazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_stazione = idLogic_stazione.getId();
				if(id_stazione==null || id_stazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object portale
		sqlQueryObjectInsert.addInsertTable(this.getPortaleFieldConverter().toTable(Portale.model()));
		sqlQueryObjectInsert.addInsertField(this.getPortaleFieldConverter().toColumn(Portale.model().COD_PORTALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPortaleFieldConverter().toColumn(Portale.model().DEFAULT_CALLBACK_URL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPortaleFieldConverter().toColumn(Portale.model().PRINCIPAL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPortaleFieldConverter().toColumn(Portale.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_stazione","?");

		// Insert portale
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getPortaleFetch().getKeyGeneratorObject(Portale.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(portale.getCodPortale(),Portale.model().COD_PORTALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(portale.getDefaultCallbackURL(),Portale.model().DEFAULT_CALLBACK_URL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(portale.getPrincipal(),Portale.model().PRINCIPAL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(portale.getAbilitato(),Portale.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_stazione,Long.class)
		);
		portale.setId(id);

		// for portale
		for (int i = 0; i < portale.getPortaleApplicazioneList().size(); i++) {

			// Object _portaleApplicazione_applicazione
			Long id_portaleApplicazione_applicazione = null;
			it.govpay.orm.IdApplicazione idLogic_portaleApplicazione_applicazione = null;
			idLogic_portaleApplicazione_applicazione = portale.getPortaleApplicazioneList().get(i).getIdApplicazione();
			if(idLogic_portaleApplicazione_applicazione!=null){
				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
					id_portaleApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_portaleApplicazione_applicazione, false);
				}
				else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
					id_portaleApplicazione_applicazione = idLogic_portaleApplicazione_applicazione.getId();
					if(id_portaleApplicazione_applicazione==null || id_portaleApplicazione_applicazione<=0){
						throw new Exception("Logic id not contains table id");
					}
				}
			}


			// Object portale.getPortaleApplicazioneList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_portaleApplicazione = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_portaleApplicazione.addInsertTable(this.getPortaleFieldConverter().toTable(Portale.model().PORTALE_APPLICAZIONE));
			sqlQueryObjectInsert_portaleApplicazione.addInsertField("id_applicazione","?");
			sqlQueryObjectInsert_portaleApplicazione.addInsertField("id_portale","?");

			// Insert portale.getPortaleApplicazioneList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_portaleApplicazione = this.getPortaleFetch().getKeyGeneratorObject(Portale.model().PORTALE_APPLICAZIONE);
			long id_portaleApplicazione = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_portaleApplicazione, keyGenerator_portaleApplicazione, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_portaleApplicazione_applicazione,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			portale.getPortaleApplicazioneList().get(i).setId(id_portaleApplicazione);
		} // fine for 

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPortale oldId, Portale portale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdPortale(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = portale.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: portale.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			portale.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, portale, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Portale portale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _portale_stazione
		Long id_portale_stazione = null;
		it.govpay.orm.IdStazione idLogic_portale_stazione = null;
		idLogic_portale_stazione = portale.getIdStazione();
		if(idLogic_portale_stazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_portale_stazione = ((JDBCStazioneServiceSearch)(this.getServiceManager().getStazioneServiceSearch())).findTableId(idLogic_portale_stazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_portale_stazione = idLogic_portale_stazione.getId();
				if(id_portale_stazione==null || id_portale_stazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object portale
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getPortaleFieldConverter().toTable(Portale.model()));
		boolean isUpdate_portale = true;
		java.util.List<JDBCObject> lstObjects_portale = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getPortaleFieldConverter().toColumn(Portale.model().COD_PORTALE,false), "?");
		lstObjects_portale.add(new JDBCObject(portale.getCodPortale(), Portale.model().COD_PORTALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPortaleFieldConverter().toColumn(Portale.model().DEFAULT_CALLBACK_URL,false), "?");
		lstObjects_portale.add(new JDBCObject(portale.getDefaultCallbackURL(), Portale.model().DEFAULT_CALLBACK_URL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPortaleFieldConverter().toColumn(Portale.model().PRINCIPAL,false), "?");
		lstObjects_portale.add(new JDBCObject(portale.getPrincipal(), Portale.model().PRINCIPAL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPortaleFieldConverter().toColumn(Portale.model().ABILITATO,false), "?");
		lstObjects_portale.add(new JDBCObject(portale.getAbilitato(), Portale.model().ABILITATO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_stazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_portale.add(new JDBCObject(id_portale_stazione, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_portale.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_portale) {
			// Update portale
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_portale.toArray(new JDBCObject[]{}));
		}
		// for portale_portaleApplicazione

		java.util.List<Long> ids_portale_portaleApplicazione_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object portale_portaleApplicazione_object : portale.getPortaleApplicazioneList()) {
			PortaleApplicazione portale_portaleApplicazione = (PortaleApplicazione) portale_portaleApplicazione_object;
			if(portale_portaleApplicazione.getId() == null || portale_portaleApplicazione.getId().longValue() <= 0) {

				long id = portale.getId();			
				// Object _portale_portaleApplicazione_applicazione
				Long id_portale_portaleApplicazione_applicazione = null;
				it.govpay.orm.IdApplicazione idLogic_portale_portaleApplicazione_applicazione = null;
				idLogic_portale_portaleApplicazione_applicazione = portale_portaleApplicazione.getIdApplicazione();
				if(idLogic_portale_portaleApplicazione_applicazione!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_portale_portaleApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_portale_portaleApplicazione_applicazione, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_portale_portaleApplicazione_applicazione = idLogic_portale_portaleApplicazione_applicazione.getId();
						if(id_portale_portaleApplicazione_applicazione==null || id_portale_portaleApplicazione_applicazione<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object portale_portaleApplicazione
				ISQLQueryObject sqlQueryObjectInsert_portale_portaleApplicazione = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_portale_portaleApplicazione.addInsertTable(this.getPortaleFieldConverter().toTable(Portale.model().PORTALE_APPLICAZIONE));
				sqlQueryObjectInsert_portale_portaleApplicazione.addInsertField("id_applicazione","?");
				sqlQueryObjectInsert_portale_portaleApplicazione.addInsertField("id_portale","?");

				// Insert portale_portaleApplicazione
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_portale_portaleApplicazione = this.getPortaleFetch().getKeyGeneratorObject(Portale.model().PORTALE_APPLICAZIONE);
				long id_portale_portaleApplicazione = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_portale_portaleApplicazione, keyGenerator_portale_portaleApplicazione, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_portale_portaleApplicazione_applicazione,Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				portale_portaleApplicazione.setId(id_portale_portaleApplicazione);

				ids_portale_portaleApplicazione_da_non_eliminare.add(portale_portaleApplicazione.getId());
			} else {

				// Object _portale_portaleApplicazione_applicazione
				Long id_portale_portaleApplicazione_applicazione = null;
				it.govpay.orm.IdApplicazione idLogic_portale_portaleApplicazione_applicazione = null;
				idLogic_portale_portaleApplicazione_applicazione = portale_portaleApplicazione.getIdApplicazione();
				if(idLogic_portale_portaleApplicazione_applicazione!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_portale_portaleApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_portale_portaleApplicazione_applicazione, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_portale_portaleApplicazione_applicazione = idLogic_portale_portaleApplicazione_applicazione.getId();
						if(id_portale_portaleApplicazione_applicazione==null || id_portale_portaleApplicazione_applicazione<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object portale_portaleApplicazione
				ISQLQueryObject sqlQueryObjectUpdate_portale_portaleApplicazione = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_portale_portaleApplicazione.setANDLogicOperator(true);
				sqlQueryObjectUpdate_portale_portaleApplicazione.addUpdateTable(this.getPortaleFieldConverter().toTable(Portale.model().PORTALE_APPLICAZIONE));
				boolean isUpdate_portale_portaleApplicazione = true;
				java.util.List<JDBCObject> lstObjects_portale_portaleApplicazione = new java.util.ArrayList<JDBCObject>();
				if(setIdMappingResolutionBehaviour){
					sqlQueryObjectUpdate_portale_portaleApplicazione.addUpdateField("id_applicazione","?");
				}
				if(setIdMappingResolutionBehaviour){
					lstObjects_portale_portaleApplicazione.add(new JDBCObject(id_portale_portaleApplicazione_applicazione, Long.class));
				}
				sqlQueryObjectUpdate_portale_portaleApplicazione.addWhereCondition("id=?");
				ids_portale_portaleApplicazione_da_non_eliminare.add(portale_portaleApplicazione.getId());
				lstObjects_portale_portaleApplicazione.add(new JDBCObject(new Long(portale_portaleApplicazione.getId()),Long.class));

				if(isUpdate_portale_portaleApplicazione) {
					// Update portale_portaleApplicazione
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_portale_portaleApplicazione.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_portale_portaleApplicazione.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for portale_portaleApplicazione

		// elimino tutte le occorrenze di portale_portaleApplicazione non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_portaleApplicazione_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_portaleApplicazione_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_portaleApplicazione_deleteList.addDeleteTable(this.getPortaleFieldConverter().toTable(Portale.model().PORTALE_APPLICAZIONE));
		java.util.List<JDBCObject> jdbcObjects_portale_portaleApplicazione_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_portaleApplicazione_deleteList.addWhereCondition("id_portale=?");
		jdbcObjects_portale_portaleApplicazione_delete.add(new JDBCObject(portale.getId(), Long.class));

		StringBuffer marks_portale_portaleApplicazione = new StringBuffer();
		if(ids_portale_portaleApplicazione_da_non_eliminare.size() > 0) {
			for(Long ids : ids_portale_portaleApplicazione_da_non_eliminare) {
				if(marks_portale_portaleApplicazione.length() > 0) {
					marks_portale_portaleApplicazione.append(",");
				}
				marks_portale_portaleApplicazione.append("?");
				jdbcObjects_portale_portaleApplicazione_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_portaleApplicazione_deleteList.addWhereCondition("id NOT IN ("+marks_portale_portaleApplicazione.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_portaleApplicazione_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_portale_portaleApplicazione_delete.toArray(new JDBCObject[]{}));

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPortale id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPortaleFieldConverter().toTable(Portale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPortaleFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPortale id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPortaleFieldConverter().toTable(Portale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPortaleFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPortale id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPortaleFieldConverter().toTable(Portale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPortaleFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPortaleFieldConverter().toTable(Portale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPortaleFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPortaleFieldConverter().toTable(Portale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPortaleFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPortaleFieldConverter().toTable(Portale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPortaleFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPortale oldId, Portale portale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, portale,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, portale,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Portale portale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, portale,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, portale,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Portale portale) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (portale.getId()!=null) && (portale.getId()>0) ){
			longId = portale.getId();
		}
		else{
			IdPortale idPortale = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,portale);
			longId = this.findIdPortale(jdbcProperties,log,connection,sqlQueryObject,idPortale,false);
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
		

		//Recupero oggetto _portale_portaleApplicazione
		ISQLQueryObject sqlQueryObjectDelete_portale_portaleApplicazione_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_portale_portaleApplicazione_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_portale_portaleApplicazione_getToDelete.addFromTable(this.getPortaleFieldConverter().toTable(Portale.model().PORTALE_APPLICAZIONE));
		sqlQueryObjectDelete_portale_portaleApplicazione_getToDelete.addWhereCondition("id_portale=?");
		java.util.List<Object> portale_portaleApplicazione_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_portale_portaleApplicazione_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Portale.model().PORTALE_APPLICAZIONE, this.getPortaleFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for portale_portaleApplicazione
		for (Object portale_portaleApplicazione_object : portale_portaleApplicazione_toDelete_list) {
			PortaleApplicazione portale_portaleApplicazione = (PortaleApplicazione) portale_portaleApplicazione_object;

			// Object portale_portaleApplicazione
			ISQLQueryObject sqlQueryObjectDelete_portale_portaleApplicazione = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_portale_portaleApplicazione.setANDLogicOperator(true);
			sqlQueryObjectDelete_portale_portaleApplicazione.addDeleteTable(this.getPortaleFieldConverter().toTable(Portale.model().PORTALE_APPLICAZIONE));
			sqlQueryObjectDelete_portale_portaleApplicazione.addWhereCondition("id=?");

			// Delete portale_portaleApplicazione
			jdbcUtilities.execute(sqlQueryObjectDelete_portale_portaleApplicazione.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(portale_portaleApplicazione.getId()),Long.class));
		} // fine for portale_portaleApplicazione

		// Object portale
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getPortaleFieldConverter().toTable(Portale.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete portale
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPortale idPortale) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdPortale(jdbcProperties, log, connection, sqlQueryObject, idPortale, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getPortaleFieldConverter()));

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
