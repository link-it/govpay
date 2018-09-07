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
import it.govpay.orm.IdRendicontazione;
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

import it.govpay.orm.Rendicontazione;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCRendicontazioneServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRendicontazioneServiceImpl extends JDBCRendicontazioneServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Rendicontazione, IdRendicontazione, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Rendicontazione rendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _fr
		Long id_fr = null;
		it.govpay.orm.IdFr idLogic_fr = null;
		idLogic_fr = rendicontazione.getIdFR();
		if(idLogic_fr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_fr = ((JDBCFRServiceSearch)(this.getServiceManager().getFRServiceSearch())).findTableId(idLogic_fr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_fr = idLogic_fr.getId();
				if(id_fr==null || id_fr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _pagamento
		Long id_pagamento = null;
		it.govpay.orm.IdPagamento idLogic_pagamento = null;
		idLogic_pagamento = rendicontazione.getIdPagamento();
		if(idLogic_pagamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_pagamento = ((JDBCPagamentoServiceSearch)(this.getServiceManager().getPagamentoServiceSearch())).findTableId(idLogic_pagamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_pagamento = idLogic_pagamento.getId();
				if(id_pagamento==null || id_pagamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object rendicontazione
		sqlQueryObjectInsert.addInsertTable(this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()));
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().IUR,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().INDICE_DATI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().IMPORTO_PAGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().DATA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().ANOMALIE,false),"?");
		sqlQueryObjectInsert.addInsertField("id_fr","?");
		sqlQueryObjectInsert.addInsertField("id_pagamento","?");

		// Insert rendicontazione
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getRendicontazioneFetch().getKeyGeneratorObject(Rendicontazione.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getIuv(),Rendicontazione.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getIur(),Rendicontazione.model().IUR.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getIndiceDati(),Rendicontazione.model().INDICE_DATI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getImportoPagato(),Rendicontazione.model().IMPORTO_PAGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getEsito(),Rendicontazione.model().ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getData(),Rendicontazione.model().DATA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getStato(),Rendicontazione.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazione.getAnomalie(),Rendicontazione.model().ANOMALIE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_fr,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_pagamento,Long.class)
		);
		rendicontazione.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione oldId, Rendicontazione rendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdRendicontazione(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = rendicontazione.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: rendicontazione.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			rendicontazione.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rendicontazione, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Rendicontazione rendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _rendicontazione_fr
		Long id_rendicontazione_fr = null;
		it.govpay.orm.IdFr idLogic_rendicontazione_fr = null;
		idLogic_rendicontazione_fr = rendicontazione.getIdFR();
		if(idLogic_rendicontazione_fr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rendicontazione_fr = ((JDBCFRServiceSearch)(this.getServiceManager().getFRServiceSearch())).findTableId(idLogic_rendicontazione_fr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rendicontazione_fr = idLogic_rendicontazione_fr.getId();
				if(id_rendicontazione_fr==null || id_rendicontazione_fr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rendicontazione_pagamento
		Long id_rendicontazione_pagamento = null;
		it.govpay.orm.IdPagamento idLogic_rendicontazione_pagamento = null;
		idLogic_rendicontazione_pagamento = rendicontazione.getIdPagamento();
		if(idLogic_rendicontazione_pagamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rendicontazione_pagamento = ((JDBCPagamentoServiceSearch)(this.getServiceManager().getPagamentoServiceSearch())).findTableId(idLogic_rendicontazione_pagamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rendicontazione_pagamento = idLogic_rendicontazione_pagamento.getId();
				if(id_rendicontazione_pagamento==null || id_rendicontazione_pagamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object rendicontazione
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()));
		boolean isUpdate_rendicontazione = true;
		java.util.List<JDBCObject> lstObjects_rendicontazione = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().IUV,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getIuv(), Rendicontazione.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().IUR,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getIur(), Rendicontazione.model().IUR.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().INDICE_DATI,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getIndiceDati(), Rendicontazione.model().INDICE_DATI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().IMPORTO_PAGATO,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getImportoPagato(), Rendicontazione.model().IMPORTO_PAGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().ESITO,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getEsito(), Rendicontazione.model().ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().DATA,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getData(), Rendicontazione.model().DATA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().STATO,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getStato(), Rendicontazione.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneFieldConverter().toColumn(Rendicontazione.model().ANOMALIE,false), "?");
		lstObjects_rendicontazione.add(new JDBCObject(rendicontazione.getAnomalie(), Rendicontazione.model().ANOMALIE.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_fr","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_pagamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rendicontazione.add(new JDBCObject(id_rendicontazione_fr, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rendicontazione.add(new JDBCObject(id_rendicontazione_pagamento, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_rendicontazione.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_rendicontazione) {
			// Update rendicontazione
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_rendicontazione.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRendicontazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRendicontazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRendicontazioneFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRendicontazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRendicontazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRendicontazioneFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione oldId, Rendicontazione rendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, rendicontazione,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rendicontazione,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Rendicontazione rendicontazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rendicontazione,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rendicontazione,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Rendicontazione rendicontazione) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (rendicontazione.getId()!=null) && (rendicontazione.getId()>0) ){
			longId = rendicontazione.getId();
		}
		else{
			IdRendicontazione idRendicontazione = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,rendicontazione);
			longId = this.findIdRendicontazione(jdbcProperties,log,connection,sqlQueryObject,idRendicontazione,false);
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
		

		// Object rendicontazione
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getRendicontazioneFieldConverter().toTable(Rendicontazione.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete rendicontazione
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione idRendicontazione) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdRendicontazione(jdbcProperties, log, connection, sqlQueryObject, idRendicontazione, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getRendicontazioneFieldConverter()));

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
