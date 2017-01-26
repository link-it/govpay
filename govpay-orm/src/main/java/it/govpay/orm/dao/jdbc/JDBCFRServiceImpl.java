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

import it.govpay.orm.FR;
import it.govpay.orm.IdFr;

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
 * JDBCFRServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCFRServiceImpl extends JDBCFRServiceSearchImpl
	implements IJDBCServiceCRUDWithId<FR, IdFr, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, FR fr, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object fr
		sqlQueryObjectInsert.addInsertTable(this.getFRFieldConverter().toTable(FR.model()));
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().COD_PSP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().COD_FLUSSO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().IUR,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().DATA_ORA_FLUSSO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().DATA_REGOLAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().DATA_ACQUISIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().NUMERO_PAGAMENTI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().IMPORTO_TOTALE_PAGAMENTI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().COD_BIC_RIVERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getFRFieldConverter().toColumn(FR.model().XML,false),"?");

		// Insert fr
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getFRFetch().getKeyGeneratorObject(FR.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getCodPsp(),FR.model().COD_PSP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getCodDominio(),FR.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getCodFlusso(),FR.model().COD_FLUSSO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getStato(),FR.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getDescrizioneStato(),FR.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getIur(),FR.model().IUR.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getDataOraFlusso(),FR.model().DATA_ORA_FLUSSO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getDataRegolamento(),FR.model().DATA_REGOLAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getDataAcquisizione(),FR.model().DATA_ACQUISIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getNumeroPagamenti(),FR.model().NUMERO_PAGAMENTI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getImportoTotalePagamenti(),FR.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getCodBicRiversamento(),FR.model().COD_BIC_RIVERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(fr.getXml(),FR.model().XML.getFieldType())
		);
		fr.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr oldId, FR fr, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdFR(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = fr.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: fr.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			fr.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, fr, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, FR fr, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			


		// Object fr
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getFRFieldConverter().toTable(FR.model()));
		boolean isUpdate_fr = true;
		java.util.List<JDBCObject> lstObjects_fr = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().COD_PSP,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getCodPsp(), FR.model().COD_PSP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().COD_DOMINIO,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getCodDominio(), FR.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().COD_FLUSSO,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getCodFlusso(), FR.model().COD_FLUSSO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().STATO,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getStato(), FR.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getDescrizioneStato(), FR.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().IUR,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getIur(), FR.model().IUR.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().DATA_ORA_FLUSSO,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getDataOraFlusso(), FR.model().DATA_ORA_FLUSSO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().DATA_REGOLAMENTO,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getDataRegolamento(), FR.model().DATA_REGOLAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().DATA_ACQUISIZIONE,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getDataAcquisizione(), FR.model().DATA_ACQUISIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().NUMERO_PAGAMENTI,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getNumeroPagamenti(), FR.model().NUMERO_PAGAMENTI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().IMPORTO_TOTALE_PAGAMENTI,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getImportoTotalePagamenti(), FR.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().COD_BIC_RIVERSAMENTO,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getCodBicRiversamento(), FR.model().COD_BIC_RIVERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getFRFieldConverter().toColumn(FR.model().XML,false), "?");
		lstObjects_fr.add(new JDBCObject(fr.getXml(), FR.model().XML.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_fr.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_fr) {
			// Update fr
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_fr.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getFRFieldConverter().toTable(FR.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getFRFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getFRFieldConverter().toTable(FR.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getFRFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getFRFieldConverter().toTable(FR.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getFRFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getFRFieldConverter().toTable(FR.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getFRFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getFRFieldConverter().toTable(FR.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getFRFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getFRFieldConverter().toTable(FR.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getFRFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr oldId, FR fr, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, fr,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, fr,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, FR fr, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, fr,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, fr,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, FR fr) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (fr.getId()!=null) && (fr.getId()>0) ){
			longId = fr.getId();
		}
		else{
			IdFr idFR = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,fr);
			longId = this.findIdFR(jdbcProperties,log,connection,sqlQueryObject,idFR,false);
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
		

		// Object fr
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getFRFieldConverter().toTable(FR.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete fr
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdFr idFR) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdFR(jdbcProperties, log, connection, sqlQueryObject, idFR, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getFRFieldConverter()));

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
