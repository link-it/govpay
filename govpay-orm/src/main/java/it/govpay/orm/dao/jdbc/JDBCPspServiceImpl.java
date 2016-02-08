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

import it.govpay.orm.IdPsp;
import it.govpay.orm.Psp;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.beans.UpdateModel;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;

/**     
 * JDBCPspServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPspServiceImpl extends JDBCPspServiceSearchImpl
implements IJDBCServiceCRUDWithId<Psp, IdPsp, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Psp psp, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}

		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();


		// Object _tracciatoXML
		Long id_tracciatoXML = null;
		it.govpay.orm.IdTracciato idLogic_tracciatoXML = null;
		idLogic_tracciatoXML = psp.getIdTracciato();
		if(idLogic_tracciatoXML!=null){
			if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciatoXML = ((JDBCTracciatoXMLServiceSearch)(this.getServiceManager().getTracciatoXMLServiceSearch())).findTableId(idLogic_tracciatoXML, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciatoXML = idLogic_tracciatoXML.getId();
				if(id_tracciatoXML==null || id_tracciatoXML<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object psp
		sqlQueryObjectInsert.addInsertTable(this.getPspFieldConverter().toTable(Psp.model()));
		sqlQueryObjectInsert.addInsertField(this.getPspFieldConverter().toColumn(Psp.model().COD_PSP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPspFieldConverter().toColumn(Psp.model().RAGIONE_SOCIALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPspFieldConverter().toColumn(Psp.model().COD_FLUSSO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPspFieldConverter().toColumn(Psp.model().URL_INFO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPspFieldConverter().toColumn(Psp.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPspFieldConverter().toColumn(Psp.model().STORNO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPspFieldConverter().toColumn(Psp.model().MARCA_BOLLO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_tracciato_xml","?");

		// Insert psp
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getPspFetch().getKeyGeneratorObject(Psp.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(psp.getCodPsp(),Psp.model().COD_PSP.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(psp.getRagioneSociale(),Psp.model().RAGIONE_SOCIALE.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(psp.getCodFlusso(),Psp.model().COD_FLUSSO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(psp.getUrlInfo(),Psp.model().URL_INFO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(psp.getAbilitato(),Psp.model().ABILITATO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(psp.getStorno(),Psp.model().STORNO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(psp.getMarcaBollo(),Psp.model().MARCA_BOLLO.getFieldType()),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tracciatoXML,Long.class)
				);
		psp.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPsp oldId, Psp psp, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdPsp(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = psp.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: psp.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			psp.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, psp, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Psp psp, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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


		// Object _psp_tracciatoXML
		Long id_psp_tracciatoXML = null;
		it.govpay.orm.IdTracciato idLogic_psp_tracciatoXML = null;
		idLogic_psp_tracciatoXML = psp.getIdTracciato();
		if(idLogic_psp_tracciatoXML!=null){
			if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_psp_tracciatoXML = ((JDBCTracciatoXMLServiceSearch)(this.getServiceManager().getTracciatoXMLServiceSearch())).findTableId(idLogic_psp_tracciatoXML, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_psp_tracciatoXML = idLogic_psp_tracciatoXML.getId();
				if(id_psp_tracciatoXML==null || id_psp_tracciatoXML<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object psp
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getPspFieldConverter().toTable(Psp.model()));
		boolean isUpdate_psp = true;
		java.util.List<JDBCObject> lstObjects_psp = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getPspFieldConverter().toColumn(Psp.model().COD_PSP,false), "?");
		lstObjects_psp.add(new JDBCObject(psp.getCodPsp(), Psp.model().COD_PSP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPspFieldConverter().toColumn(Psp.model().RAGIONE_SOCIALE,false), "?");
		lstObjects_psp.add(new JDBCObject(psp.getRagioneSociale(), Psp.model().RAGIONE_SOCIALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPspFieldConverter().toColumn(Psp.model().COD_FLUSSO,false), "?");
		lstObjects_psp.add(new JDBCObject(psp.getCodFlusso(), Psp.model().COD_FLUSSO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPspFieldConverter().toColumn(Psp.model().URL_INFO,false), "?");
		lstObjects_psp.add(new JDBCObject(psp.getUrlInfo(), Psp.model().URL_INFO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPspFieldConverter().toColumn(Psp.model().ABILITATO,false), "?");
		lstObjects_psp.add(new JDBCObject(psp.getAbilitato(), Psp.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPspFieldConverter().toColumn(Psp.model().STORNO,false), "?");
		lstObjects_psp.add(new JDBCObject(psp.getStorno(), Psp.model().STORNO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPspFieldConverter().toColumn(Psp.model().MARCA_BOLLO,false), "?");
		lstObjects_psp.add(new JDBCObject(psp.getMarcaBollo(), Psp.model().MARCA_BOLLO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tracciato_xml","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_psp.add(new JDBCObject(id_psp_tracciatoXML, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_psp.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_psp) {
			// Update psp
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
					lstObjects_psp.toArray(new JDBCObject[]{}));
		}

	}

	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPsp id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {

		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPspFieldConverter().toTable(Psp.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPspFieldConverter(), this, null, updateFields);
	}

	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPsp id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {

		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPspFieldConverter().toTable(Psp.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPspFieldConverter(), this, condition, updateFields);
	}

	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPsp id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {

		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPspFieldConverter().toTable(Psp.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPspFieldConverter(), this, updateModels);
	}	

	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPspFieldConverter().toTable(Psp.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPspFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPspFieldConverter().toTable(Psp.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPspFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPspFieldConverter().toTable(Psp.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPspFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPsp oldId, Psp psp, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}

		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, psp,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, psp,idMappingResolutionBehaviour);
		}

	}

	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Psp psp, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, psp,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, psp,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Psp psp) throws NotImplementedException,ServiceException,Exception {


		Long longId = null;
		if( (psp.getId()!=null) && (psp.getId()>0) ){
			longId = psp.getId();
		}
		else{
			IdPsp idPsp = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,psp);
			longId = this.findIdPsp(jdbcProperties,log,connection,sqlQueryObject,idPsp,false);
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

		// Object psp
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getPspFieldConverter().toTable(Psp.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete psp
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
				new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPsp idPsp) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdPsp(jdbcProperties, log, connection, sqlQueryObject, idPsp, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);

	}

	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {

		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getPspFieldConverter()));

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
