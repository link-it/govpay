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
import it.govpay.orm.IdUo;
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

import it.govpay.orm.Uo;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCUoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCUoServiceImpl extends JDBCUoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Uo, IdUo, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Uo uo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

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
		idLogic_dominio = uo.getIdDominio();
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


		// Object uo
		sqlQueryObjectInsert.addInsertTable(this.getUoFieldConverter().toTable(Uo.model()));
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().COD_UO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_CODICE_IDENTIFICATIVO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_DENOMINAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_INDIRIZZO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_CIVICO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_CAP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_LOCALITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_PROVINCIA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_NAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_AREA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_URL_SITO_WEB,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_EMAIL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getUoFieldConverter().toColumn(Uo.model().UO_PEC,false),"?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");

		// Insert uo
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getUoFetch().getKeyGeneratorObject(Uo.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getCodUo(),Uo.model().COD_UO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getAbilitato(),Uo.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoCodiceIdentificativo(),Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoDenominazione(),Uo.model().UO_DENOMINAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoIndirizzo(),Uo.model().UO_INDIRIZZO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoCivico(),Uo.model().UO_CIVICO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoCap(),Uo.model().UO_CAP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoLocalita(),Uo.model().UO_LOCALITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoProvincia(),Uo.model().UO_PROVINCIA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoNazione(),Uo.model().UO_NAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoArea(),Uo.model().UO_AREA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoUrlSitoWeb(),Uo.model().UO_URL_SITO_WEB.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoEmail(),Uo.model().UO_EMAIL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(uo.getUoPec(),Uo.model().UO_PEC.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class)
		);
		uo.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdUo oldId, Uo uo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdUo(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = uo.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: uo.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			uo.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, uo, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Uo uo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _uo_dominio
		Long id_uo_dominio = null;
		it.govpay.orm.IdDominio idLogic_uo_dominio = null;
		idLogic_uo_dominio = uo.getIdDominio();
		if(idLogic_uo_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_uo_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_uo_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_uo_dominio = idLogic_uo_dominio.getId();
				if(id_uo_dominio==null || id_uo_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object uo
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getUoFieldConverter().toTable(Uo.model()));
		boolean isUpdate_uo = true;
		java.util.List<JDBCObject> lstObjects_uo = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().COD_UO,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getCodUo(), Uo.model().COD_UO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().ABILITATO,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getAbilitato(), Uo.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_CODICE_IDENTIFICATIVO,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoCodiceIdentificativo(), Uo.model().UO_CODICE_IDENTIFICATIVO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_DENOMINAZIONE,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoDenominazione(), Uo.model().UO_DENOMINAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_INDIRIZZO,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoIndirizzo(), Uo.model().UO_INDIRIZZO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_CIVICO,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoCivico(), Uo.model().UO_CIVICO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_CAP,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoCap(), Uo.model().UO_CAP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_LOCALITA,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoLocalita(), Uo.model().UO_LOCALITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_PROVINCIA,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoProvincia(), Uo.model().UO_PROVINCIA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_NAZIONE,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoNazione(), Uo.model().UO_NAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_AREA,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoArea(), Uo.model().UO_AREA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_URL_SITO_WEB,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoUrlSitoWeb(), Uo.model().UO_URL_SITO_WEB.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_EMAIL,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoEmail(), Uo.model().UO_EMAIL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getUoFieldConverter().toColumn(Uo.model().UO_PEC,false), "?");
		lstObjects_uo.add(new JDBCObject(uo.getUoPec(), Uo.model().UO_PEC.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_uo.add(new JDBCObject(id_uo_dominio, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_uo.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_uo) {
			// Update uo
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_uo.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdUo id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUoFieldConverter().toTable(Uo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getUoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdUo id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUoFieldConverter().toTable(Uo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getUoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdUo id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUoFieldConverter().toTable(Uo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getUoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUoFieldConverter().toTable(Uo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUoFieldConverter().toTable(Uo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getUoFieldConverter().toTable(Uo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getUoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdUo oldId, Uo uo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, uo,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, uo,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Uo uo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, uo,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, uo,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Uo uo) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (uo.getId()!=null) && (uo.getId()>0) ){
			longId = uo.getId();
		}
		else{
			IdUo idUo = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,uo);
			longId = this.findIdUo(jdbcProperties,log,connection,sqlQueryObject,idUo,false);
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
		

		// Object uo
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getUoFieldConverter().toTable(Uo.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete uo
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdUo idUo) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdUo(jdbcProperties, log, connection, sqlQueryObject, idUo, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getUoFieldConverter()));

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
