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
import it.govpay.orm.IdIbanAccredito;
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

import it.govpay.orm.IbanAccredito;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCIbanAccreditoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCIbanAccreditoServiceImpl extends JDBCIbanAccreditoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<IbanAccredito, IdIbanAccredito, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IbanAccredito ibanAccredito, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _dominio
		Long id_dominio = null;
		it.govpay.orm.IdDominio idLogic_dominio = null;
		idLogic_dominio = ibanAccredito.getIdDominio();
		if(idLogic_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_dominio = idLogic_dominio.getId();
				if(id_dominio==null || id_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object ibanAccredito
		sqlQueryObjectInsert.addInsertTable(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()));
		sqlQueryObjectInsert.addInsertField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().COD_IBAN,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().BIC_ACCREDITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().POSTALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().DESCRIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().INTESTATARIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().AUT_STAMPA_POSTE,false),"?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");

		// Insert ibanAccredito
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getIbanAccreditoFetch().getKeyGeneratorObject(IbanAccredito.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ibanAccredito.getCodIban(),IbanAccredito.model().COD_IBAN.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ibanAccredito.getBicAccredito(),IbanAccredito.model().BIC_ACCREDITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ibanAccredito.getPostale(),IbanAccredito.model().POSTALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ibanAccredito.getAbilitato(),IbanAccredito.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ibanAccredito.getDescrizione(),IbanAccredito.model().DESCRIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ibanAccredito.getIntestatario(),IbanAccredito.model().INTESTATARIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ibanAccredito.getAutStampaPoste(),IbanAccredito.model().AUT_STAMPA_POSTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class)
		);
		ibanAccredito.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito oldId, IbanAccredito ibanAccredito, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdIbanAccredito(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = ibanAccredito.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: ibanAccredito.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			ibanAccredito.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, ibanAccredito, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IbanAccredito ibanAccredito, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _ibanAccredito_dominio
		Long id_ibanAccredito_dominio = null;
		it.govpay.orm.IdDominio idLogic_ibanAccredito_dominio = null;
		idLogic_ibanAccredito_dominio = ibanAccredito.getIdDominio();
		if(idLogic_ibanAccredito_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ibanAccredito_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_ibanAccredito_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ibanAccredito_dominio = idLogic_ibanAccredito_dominio.getId();
				if(id_ibanAccredito_dominio==null || id_ibanAccredito_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object ibanAccredito
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()));
		boolean isUpdate_ibanAccredito = true;
		java.util.List<JDBCObject> lstObjects_ibanAccredito = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().COD_IBAN,false), "?");
		lstObjects_ibanAccredito.add(new JDBCObject(ibanAccredito.getCodIban(), IbanAccredito.model().COD_IBAN.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().BIC_ACCREDITO,false), "?");
		lstObjects_ibanAccredito.add(new JDBCObject(ibanAccredito.getBicAccredito(), IbanAccredito.model().BIC_ACCREDITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().POSTALE,false), "?");
		lstObjects_ibanAccredito.add(new JDBCObject(ibanAccredito.getPostale(), IbanAccredito.model().POSTALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().ABILITATO,false), "?");
		lstObjects_ibanAccredito.add(new JDBCObject(ibanAccredito.getAbilitato(), IbanAccredito.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().DESCRIZIONE,false), "?");
		lstObjects_ibanAccredito.add(new JDBCObject(ibanAccredito.getDescrizione(), IbanAccredito.model().DESCRIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().INTESTATARIO,false), "?");
		lstObjects_ibanAccredito.add(new JDBCObject(ibanAccredito.getIntestatario(), IbanAccredito.model().INTESTATARIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getIbanAccreditoFieldConverter().toColumn(IbanAccredito.model().AUT_STAMPA_POSTE,false), "?");
		lstObjects_ibanAccredito.add(new JDBCObject(ibanAccredito.getAutStampaPoste(), IbanAccredito.model().AUT_STAMPA_POSTE.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_ibanAccredito.add(new JDBCObject(id_ibanAccredito_dominio, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_ibanAccredito.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_ibanAccredito) {
			// Update ibanAccredito
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_ibanAccredito.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getIbanAccreditoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getIbanAccreditoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getIbanAccreditoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getIbanAccreditoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getIbanAccreditoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getIbanAccreditoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito oldId, IbanAccredito ibanAccredito, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, ibanAccredito,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, ibanAccredito,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IbanAccredito ibanAccredito, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, ibanAccredito,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, ibanAccredito,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IbanAccredito ibanAccredito) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (ibanAccredito.getId()!=null) && (ibanAccredito.getId()>0) ){
			longId = ibanAccredito.getId();
		}
		else{
			IdIbanAccredito idIbanAccredito = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,ibanAccredito);
			longId = this.findIdIbanAccredito(jdbcProperties,log,connection,sqlQueryObject,idIbanAccredito,false);
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
		

		// Object ibanAccredito
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getIbanAccreditoFieldConverter().toTable(IbanAccredito.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete ibanAccredito
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdIbanAccredito idIbanAccredito) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdIbanAccredito(jdbcProperties, log, connection, sqlQueryObject, idIbanAccredito, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getIbanAccreditoFieldConverter()));

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
