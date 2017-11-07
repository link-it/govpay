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
import it.govpay.orm.IdIncasso;
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

import it.govpay.orm.Incasso;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCIncassoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCIncassoServiceImpl extends JDBCIncassoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Incasso, IdIncasso, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Incasso incasso, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

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
		idLogic_applicazione = incasso.getIdApplicazione();
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

		// Object _operatore
		Long id_operatore = null;
		it.govpay.orm.IdOperatore idLogic_operatore = null;
		// TODO Impostare il corretto metodo che contiene l'identificativo logico
		idLogic_operatore = incasso.getIdOperatore();
		if(idLogic_operatore!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_operatore = ((JDBCOperatoreServiceSearch)(this.getServiceManager().getOperatoreServiceSearch())).findTableId(idLogic_operatore, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_operatore = idLogic_operatore.getId();
				if(id_operatore==null || id_operatore<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object incasso
		sqlQueryObjectInsert.addInsertTable(this.getIncassoFieldConverter().toTable(Incasso.model()));
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().TRN,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().CAUSALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().IMPORTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().DATA_VALUTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().DATA_CONTABILE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().DATA_ORA_INCASSO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIncassoFieldConverter().toColumn(Incasso.model().NOME_DISPOSITIVO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_applicazione","?");
		sqlQueryObjectInsert.addInsertField("id_operatore","?");

		// Insert incasso
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getIncassoFetch().getKeyGeneratorObject(Incasso.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getTrn(),Incasso.model().TRN.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getCodDominio(),Incasso.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getCausale(),Incasso.model().CAUSALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getImporto(),Incasso.model().IMPORTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getDataValuta(),Incasso.model().DATA_VALUTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getDataContabile(),Incasso.model().DATA_CONTABILE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getDataOraIncasso(),Incasso.model().DATA_ORA_INCASSO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(incasso.getNomeDispositivo(),Incasso.model().NOME_DISPOSITIVO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore,Long.class)
		);
		incasso.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIncasso oldId, Incasso incasso, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdIncasso(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = incasso.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: incasso.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			incasso.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, incasso, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Incasso incasso, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _incasso_applicazione
		Long id_incasso_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_incasso_applicazione = null;
		idLogic_incasso_applicazione = incasso.getIdApplicazione();
		if(idLogic_incasso_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_incasso_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_incasso_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_incasso_applicazione = idLogic_incasso_applicazione.getId();
				if(id_incasso_applicazione==null || id_incasso_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _incasso_operatore
		Long id_incasso_operatore = null;
		it.govpay.orm.IdOperatore idLogic_incasso_operatore = null;
		idLogic_incasso_operatore = incasso.getIdOperatore();
		if(idLogic_incasso_operatore!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_incasso_operatore = ((JDBCOperatoreServiceSearch)(this.getServiceManager().getOperatoreServiceSearch())).findTableId(idLogic_incasso_operatore, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_incasso_operatore = idLogic_incasso_operatore.getId();
				if(id_incasso_operatore==null || id_incasso_operatore<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object incasso
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getIncassoFieldConverter().toTable(Incasso.model()));
		boolean isUpdate_incasso = true;
		java.util.List<JDBCObject> lstObjects_incasso = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().TRN,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getTrn(), Incasso.model().TRN.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().COD_DOMINIO,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getCodDominio(), Incasso.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().CAUSALE,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getCausale(), Incasso.model().CAUSALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().IMPORTO,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getImporto(), Incasso.model().IMPORTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().DATA_VALUTA,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getDataValuta(), Incasso.model().DATA_VALUTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().DATA_CONTABILE,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getDataContabile(), Incasso.model().DATA_CONTABILE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().DATA_ORA_INCASSO,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getDataOraIncasso(), Incasso.model().DATA_ORA_INCASSO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIncassoFieldConverter().toColumn(Incasso.model().NOME_DISPOSITIVO,false), "?");
		lstObjects_incasso.add(new JDBCObject(incasso.getNomeDispositivo(), Incasso.model().NOME_DISPOSITIVO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_operatore","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_incasso.add(new JDBCObject(id_incasso_applicazione, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_incasso.add(new JDBCObject(id_incasso_operatore, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_incasso.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_incasso) {
			// Update incasso
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_incasso.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIncasso id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIncassoFieldConverter().toTable(Incasso.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getIncassoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIncasso id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIncassoFieldConverter().toTable(Incasso.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getIncassoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIncasso id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIncassoFieldConverter().toTable(Incasso.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getIncassoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIncassoFieldConverter().toTable(Incasso.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getIncassoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIncassoFieldConverter().toTable(Incasso.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getIncassoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIncassoFieldConverter().toTable(Incasso.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getIncassoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIncasso oldId, Incasso incasso, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, incasso,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, incasso,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Incasso incasso, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, incasso,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, incasso,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Incasso incasso) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (incasso.getId()!=null) && (incasso.getId()>0) ){
			longId = incasso.getId();
		}
		else{
			IdIncasso idIncasso = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,incasso);
			longId = this.findIdIncasso(jdbcProperties,log,connection,sqlQueryObject,idIncasso,false);
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
		

		// Object incasso
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getIncassoFieldConverter().toTable(Incasso.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete incasso
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIncasso idIncasso) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdIncasso(jdbcProperties, log, connection, sqlQueryObject, idIncasso, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getIncassoFieldConverter()));

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
