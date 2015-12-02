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
import it.govpay.orm.IdDominio;
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

import it.govpay.orm.Dominio;
import it.govpay.orm.Disponibilita;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCDominioServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCDominioServiceImpl extends JDBCDominioServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Dominio, IdDominio, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

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
		idLogic_stazione = dominio.getIdStazione();
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


		// Object dominio
		sqlQueryObjectInsert.addInsertTable(this.getDominioFieldConverter().toTable(Dominio.model()));
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().RAGIONE_SOCIALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().GLN,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().PLUGIN_CLASS,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_stazione","?");

		// Insert dominio
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getDominioFetch().getKeyGeneratorObject(Dominio.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getCodDominio(),Dominio.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getRagioneSociale(),Dominio.model().RAGIONE_SOCIALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getGln(),Dominio.model().GLN.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getPluginClass(),Dominio.model().PLUGIN_CLASS.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getAbilitato(),Dominio.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_stazione,Long.class)
		);
		dominio.setId(id);

		// for dominio
		for (int i = 0; i < dominio.getDisponibilitaList().size(); i++) {
		
			// Object dominio.getDisponibilitaList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_disponibilita = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_disponibilita.addInsertTable(this.getDominioFieldConverter().toTable(Dominio.model().DISPONIBILITA));
			sqlQueryObjectInsert_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.TIPO_PERIODO,false),"?");
			sqlQueryObjectInsert_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.GIORNO,false),"?");
			sqlQueryObjectInsert_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.FASCE_ORARIE,false),"?");
			sqlQueryObjectInsert_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA,false),"?");
			sqlQueryObjectInsert_disponibilita.addInsertField("id_dominio","?");

			// Insert dominio.getDisponibilitaList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_disponibilita = this.getDominioFetch().getKeyGeneratorObject(Dominio.model().DISPONIBILITA);
			long id_disponibilita = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_disponibilita, keyGenerator_disponibilita, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getDisponibilitaList().get(i).getTipoPeriodo(),Dominio.model().DISPONIBILITA.TIPO_PERIODO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getDisponibilitaList().get(i).getGiorno(),Dominio.model().DISPONIBILITA.GIORNO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getDisponibilitaList().get(i).getFasceOrarie(),Dominio.model().DISPONIBILITA.FASCE_ORARIE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getDisponibilitaList().get(i).getTipoDisponibilita(),Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			dominio.getDisponibilitaList().get(i).setId(id_disponibilita);
		} // fine for 

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio oldId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdDominio(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = dominio.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: dominio.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			dominio.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, dominio, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _dominio_stazione
		Long id_dominio_stazione = null;
		it.govpay.orm.IdStazione idLogic_dominio_stazione = null;
		idLogic_dominio_stazione = dominio.getIdStazione();
		if(idLogic_dominio_stazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_dominio_stazione = ((JDBCStazioneServiceSearch)(this.getServiceManager().getStazioneServiceSearch())).findTableId(idLogic_dominio_stazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_dominio_stazione = idLogic_dominio_stazione.getId();
				if(id_dominio_stazione==null || id_dominio_stazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object dominio
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getDominioFieldConverter().toTable(Dominio.model()));
		boolean isUpdate_dominio = true;
		java.util.List<JDBCObject> lstObjects_dominio = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().COD_DOMINIO,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getCodDominio(), Dominio.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().RAGIONE_SOCIALE,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getRagioneSociale(), Dominio.model().RAGIONE_SOCIALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().GLN,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getGln(), Dominio.model().GLN.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().PLUGIN_CLASS,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getPluginClass(), Dominio.model().PLUGIN_CLASS.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().ABILITATO,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getAbilitato(), Dominio.model().ABILITATO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_stazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_dominio.add(new JDBCObject(id_dominio_stazione, Long.class));
		}

		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_dominio.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_dominio) {
			// Update dominio
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_dominio.toArray(new JDBCObject[]{}));
		}
		// for dominio_disponibilita

		java.util.List<Long> ids_dominio_disponibilita_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object dominio_disponibilita_object : dominio.getDisponibilitaList()) {
			Disponibilita dominio_disponibilita = (Disponibilita) dominio_disponibilita_object;
			if(dominio_disponibilita.getId() == null || dominio_disponibilita.getId().longValue() <= 0) {

				long id = dominio.getId();			

				// Object dominio_disponibilita
				ISQLQueryObject sqlQueryObjectInsert_dominio_disponibilita = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_dominio_disponibilita.addInsertTable(this.getDominioFieldConverter().toTable(Dominio.model().DISPONIBILITA));
				sqlQueryObjectInsert_dominio_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.TIPO_PERIODO,false),"?");
				sqlQueryObjectInsert_dominio_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.GIORNO,false),"?");
				sqlQueryObjectInsert_dominio_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.FASCE_ORARIE,false),"?");
				sqlQueryObjectInsert_dominio_disponibilita.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA,false),"?");
				sqlQueryObjectInsert_dominio_disponibilita.addInsertField("id_dominio","?");

				// Insert dominio_disponibilita
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_dominio_disponibilita = this.getDominioFetch().getKeyGeneratorObject(Dominio.model().DISPONIBILITA);
				long id_dominio_disponibilita = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_dominio_disponibilita, keyGenerator_dominio_disponibilita, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio_disponibilita.getTipoPeriodo(),Dominio.model().DISPONIBILITA.TIPO_PERIODO.getFieldType()),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio_disponibilita.getGiorno(),Dominio.model().DISPONIBILITA.GIORNO.getFieldType()),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio_disponibilita.getFasceOrarie(),Dominio.model().DISPONIBILITA.FASCE_ORARIE.getFieldType()),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio_disponibilita.getTipoDisponibilita(),Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA.getFieldType()),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				dominio_disponibilita.setId(id_dominio_disponibilita);

				ids_dominio_disponibilita_da_non_eliminare.add(dominio_disponibilita.getId());
			} else {


				// Object dominio_disponibilita
				ISQLQueryObject sqlQueryObjectUpdate_dominio_disponibilita = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_dominio_disponibilita.setANDLogicOperator(true);
				sqlQueryObjectUpdate_dominio_disponibilita.addUpdateTable(this.getDominioFieldConverter().toTable(Dominio.model().DISPONIBILITA));
				boolean isUpdate_dominio_disponibilita = true;
				java.util.List<JDBCObject> lstObjects_dominio_disponibilita = new java.util.ArrayList<JDBCObject>();
				sqlQueryObjectUpdate_dominio_disponibilita.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.TIPO_PERIODO,false), "?");
				lstObjects_dominio_disponibilita.add(new JDBCObject(dominio_disponibilita.getTipoPeriodo(), Dominio.model().DISPONIBILITA.TIPO_PERIODO.getFieldType()));
				sqlQueryObjectUpdate_dominio_disponibilita.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.GIORNO,false), "?");
				lstObjects_dominio_disponibilita.add(new JDBCObject(dominio_disponibilita.getGiorno(), Dominio.model().DISPONIBILITA.GIORNO.getFieldType()));
				sqlQueryObjectUpdate_dominio_disponibilita.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.FASCE_ORARIE,false), "?");
				lstObjects_dominio_disponibilita.add(new JDBCObject(dominio_disponibilita.getFasceOrarie(), Dominio.model().DISPONIBILITA.FASCE_ORARIE.getFieldType()));
				sqlQueryObjectUpdate_dominio_disponibilita.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA,false), "?");
				lstObjects_dominio_disponibilita.add(new JDBCObject(dominio_disponibilita.getTipoDisponibilita(), Dominio.model().DISPONIBILITA.TIPO_DISPONIBILITA.getFieldType()));
				sqlQueryObjectUpdate_dominio_disponibilita.addWhereCondition("id=?");
				ids_dominio_disponibilita_da_non_eliminare.add(dominio_disponibilita.getId());
				lstObjects_dominio_disponibilita.add(new JDBCObject(new Long(dominio_disponibilita.getId()),Long.class));

				if(isUpdate_dominio_disponibilita) {
					// Update dominio_disponibilita
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_dominio_disponibilita.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_dominio_disponibilita.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for dominio_disponibilita

		// elimino tutte le occorrenze di dominio_disponibilita non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_disponibilita_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_disponibilita_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_disponibilita_deleteList.addDeleteTable(this.getDominioFieldConverter().toTable(Dominio.model().DISPONIBILITA));
		java.util.List<JDBCObject> jdbcObjects_dominio_disponibilita_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_disponibilita_deleteList.addWhereCondition("id_dominio=?");
		jdbcObjects_dominio_disponibilita_delete.add(new JDBCObject(dominio.getId(), Long.class));

		StringBuffer marks_dominio_disponibilita = new StringBuffer();
		if(ids_dominio_disponibilita_da_non_eliminare.size() > 0) {
			for(Long ids : ids_dominio_disponibilita_da_non_eliminare) {
				if(marks_dominio_disponibilita.length() > 0) {
					marks_dominio_disponibilita.append(",");
				}
				marks_dominio_disponibilita.append("?");
				jdbcObjects_dominio_disponibilita_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_disponibilita_deleteList.addWhereCondition("id NOT IN ("+marks_dominio_disponibilita.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_disponibilita_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_dominio_disponibilita_delete.toArray(new JDBCObject[]{}));



	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getDominioFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getDominioFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio oldId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, dominio,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, dominio,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, dominio,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, dominio,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Dominio dominio) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (dominio.getId()!=null) && (dominio.getId()>0) ){
			longId = dominio.getId();
		}
		else{
			IdDominio idDominio = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,dominio);
			longId = this.findIdDominio(jdbcProperties,log,connection,sqlQueryObject,idDominio,false);
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
		
		//Recupero oggetto _dominio_disponibilita
		ISQLQueryObject sqlQueryObjectDelete_dominio_disponibilita_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_dominio_disponibilita_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_dominio_disponibilita_getToDelete.addFromTable(this.getDominioFieldConverter().toTable(Dominio.model().DISPONIBILITA));
		sqlQueryObjectDelete_dominio_disponibilita_getToDelete.addWhereCondition("id_dominio=?");
		java.util.List<Object> dominio_disponibilita_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_dominio_disponibilita_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Dominio.model().DISPONIBILITA, this.getDominioFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for dominio_disponibilita
		for (Object dominio_disponibilita_object : dominio_disponibilita_toDelete_list) {
			Disponibilita dominio_disponibilita = (Disponibilita) dominio_disponibilita_object;

			// Object dominio_disponibilita
			ISQLQueryObject sqlQueryObjectDelete_dominio_disponibilita = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_dominio_disponibilita.setANDLogicOperator(true);
			sqlQueryObjectDelete_dominio_disponibilita.addDeleteTable(this.getDominioFieldConverter().toTable(Dominio.model().DISPONIBILITA));
			sqlQueryObjectDelete_dominio_disponibilita.addWhereCondition("id=?");

			// Delete dominio_disponibilita
			if(dominio_disponibilita != null){
				jdbcUtilities.execute(sqlQueryObjectDelete_dominio_disponibilita.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(dominio_disponibilita.getId()),Long.class));
			}
		} // fine for dominio_disponibilita

		// Object dominio
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getDominioFieldConverter().toTable(Dominio.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete dominio
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio idDominio) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdDominio(jdbcProperties, log, connection, sqlQueryObject, idDominio, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getDominioFieldConverter()));

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
