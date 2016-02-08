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
import it.govpay.orm.IdRt;
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

import it.govpay.orm.RT;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCRTServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRTServiceImpl extends JDBCRTServiceSearchImpl
	implements IJDBCServiceCRUDWithId<RT, IdRt, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RT rt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _rpt
		Long id_rpt = null;
		it.govpay.orm.IdRpt idLogic_rpt = null;
		idLogic_rpt = rt.getIdRPT();
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

		// Object _tracciatoXML
		Long id_tracciatoXML = null;
		it.govpay.orm.IdTracciato idLogic_tracciatoXML = null;
		idLogic_tracciatoXML = rt.getIdTracciato();
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

		// Object _anagrafica
		Long id_anagrafica = null;
		it.govpay.orm.IdAnagrafica idLogic_anagrafica = null;
		idLogic_anagrafica = rt.getIdAnagraficaAttestante();
		if(idLogic_anagrafica!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_anagrafica = ((JDBCAnagraficaServiceSearch)(this.getServiceManager().getAnagraficaServiceSearch())).findTableId(idLogic_anagrafica, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_anagrafica = idLogic_anagrafica.getId();
				if(id_anagrafica==null || id_anagrafica<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object rt
		sqlQueryObjectInsert.addInsertTable(this.getRTFieldConverter().toTable(RT.model()));
		sqlQueryObjectInsert.addInsertField(this.getRTFieldConverter().toColumn(RT.model().COD_MSG_RICEVUTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRTFieldConverter().toColumn(RT.model().DATA_ORA_MSG_RICEVUTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRTFieldConverter().toColumn(RT.model().COD_ESITO_PAGAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRTFieldConverter().toColumn(RT.model().IMPORTO_TOTALE_PAGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRTFieldConverter().toColumn(RT.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRTFieldConverter().toColumn(RT.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_rpt","?");
		sqlQueryObjectInsert.addInsertField("id_tracciato_xml","?");
		sqlQueryObjectInsert.addInsertField("id_anagrafica_attestante","?");

		// Insert rt
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getRTFetch().getKeyGeneratorObject(RT.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rt.getCodMsgRicevuta(),RT.model().COD_MSG_RICEVUTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rt.getDataOraMsgRicevuta(),RT.model().DATA_ORA_MSG_RICEVUTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rt.getCodEsitoPagamento(),RT.model().COD_ESITO_PAGAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rt.getImportoTotalePagato(),RT.model().IMPORTO_TOTALE_PAGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rt.getStato(),RT.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rt.getDescrizioneStato(),RT.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_rpt,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tracciatoXML,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_anagrafica,Long.class)
		);
		rt.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRt oldId, RT rt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdRT(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = rt.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: rt.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			rt.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rt, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, RT rt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _rt_rpt
		Long id_rt_rpt = null;
		it.govpay.orm.IdRpt idLogic_rt_rpt = null;
		idLogic_rt_rpt = rt.getIdRPT();
		if(idLogic_rt_rpt!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rt_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findTableId(idLogic_rt_rpt, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rt_rpt = idLogic_rt_rpt.getId();
				if(id_rt_rpt==null || id_rt_rpt<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rt_tracciatoXML
		Long id_rt_tracciatoXML = null;
		it.govpay.orm.IdTracciato idLogic_rt_tracciatoXML = null;
		idLogic_rt_tracciatoXML = rt.getIdTracciato();
		if(idLogic_rt_tracciatoXML!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rt_tracciatoXML = ((JDBCTracciatoXMLServiceSearch)(this.getServiceManager().getTracciatoXMLServiceSearch())).findTableId(idLogic_rt_tracciatoXML, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rt_tracciatoXML = idLogic_rt_tracciatoXML.getId();
				if(id_rt_tracciatoXML==null || id_rt_tracciatoXML<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rt_anagrafica
		Long id_rt_anagrafica = null;
		it.govpay.orm.IdAnagrafica idLogic_rt_anagrafica = null;
		idLogic_rt_anagrafica = rt.getIdAnagraficaAttestante();
		if(idLogic_rt_anagrafica!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rt_anagrafica = ((JDBCAnagraficaServiceSearch)(this.getServiceManager().getAnagraficaServiceSearch())).findTableId(idLogic_rt_anagrafica, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rt_anagrafica = idLogic_rt_anagrafica.getId();
				if(id_rt_anagrafica==null || id_rt_anagrafica<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}
		

		// Object rt
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getRTFieldConverter().toTable(RT.model()));
		boolean isUpdate_rt = true;
		java.util.List<JDBCObject> lstObjects_rt = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getRTFieldConverter().toColumn(RT.model().COD_MSG_RICEVUTA,false), "?");
		lstObjects_rt.add(new JDBCObject(rt.getCodMsgRicevuta(), RT.model().COD_MSG_RICEVUTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRTFieldConverter().toColumn(RT.model().DATA_ORA_MSG_RICEVUTA,false), "?");
		lstObjects_rt.add(new JDBCObject(rt.getDataOraMsgRicevuta(), RT.model().DATA_ORA_MSG_RICEVUTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRTFieldConverter().toColumn(RT.model().COD_ESITO_PAGAMENTO,false), "?");
		lstObjects_rt.add(new JDBCObject(rt.getCodEsitoPagamento(), RT.model().COD_ESITO_PAGAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRTFieldConverter().toColumn(RT.model().IMPORTO_TOTALE_PAGATO,false), "?");
		lstObjects_rt.add(new JDBCObject(rt.getImportoTotalePagato(), RT.model().IMPORTO_TOTALE_PAGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRTFieldConverter().toColumn(RT.model().STATO,false), "?");
		lstObjects_rt.add(new JDBCObject(rt.getStato(), RT.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRTFieldConverter().toColumn(RT.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_rt.add(new JDBCObject(rt.getDescrizioneStato(), RT.model().DESCRIZIONE_STATO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_rpt","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tracciato_xml","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_anagrafica_attestante","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rt.add(new JDBCObject(id_rt_rpt, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rt.add(new JDBCObject(id_rt_tracciatoXML, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rt.add(new JDBCObject(id_rt_anagrafica, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_rt.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_rt) {
			// Update rt
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_rt.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRt id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRTFieldConverter().toTable(RT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRTFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRt id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRTFieldConverter().toTable(RT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRTFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRt id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRTFieldConverter().toTable(RT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRTFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRTFieldConverter().toTable(RT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRTFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRTFieldConverter().toTable(RT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRTFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRTFieldConverter().toTable(RT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRTFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRt oldId, RT rt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, rt,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rt,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, RT rt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rt,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rt,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RT rt) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (rt.getId()!=null) && (rt.getId()>0) ){
			longId = rt.getId();
		}
		else{
			IdRt idRT = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,rt);
			longId = this.findIdRT(jdbcProperties,log,connection,sqlQueryObject,idRT,false);
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
		

		// Object rt
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getRTFieldConverter().toTable(RT.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete rt
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRt idRT) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdRT(jdbcProperties, log, connection, sqlQueryObject, idRT, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getRTFieldConverter()));

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
