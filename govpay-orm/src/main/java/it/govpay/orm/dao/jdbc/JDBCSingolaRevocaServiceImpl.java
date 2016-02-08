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
import it.govpay.orm.IdSingolaRevoca;
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

import it.govpay.orm.SingolaRevoca;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCSingolaRevocaServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCSingolaRevocaServiceImpl extends JDBCSingolaRevocaServiceSearchImpl
	implements IJDBCServiceCRUDWithId<SingolaRevoca, IdSingolaRevoca, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingolaRevoca singolaRevoca, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _rr
		Long id_rr = null;
		it.govpay.orm.IdRr idLogic_rr = null;
		idLogic_rr = singolaRevoca.getIdRR();
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


		// Object _singoloVersamento
		Long id_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_singoloVersamento = null;
		idLogic_singoloVersamento = singolaRevoca.getIdSingoloVersamento();
		if(idLogic_singoloVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findTableId(idLogic_singoloVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singoloVersamento = idLogic_singoloVersamento.getId();
				if(id_singoloVersamento==null || id_singoloVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object singolaRevoca
		sqlQueryObjectInsert.addInsertTable(this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()));
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().ID_ER,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().CAUSALE_REVOCA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().SINGOLO_IMPORTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().CAUSALE_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_rr","?");
		sqlQueryObjectInsert.addInsertField("id_singolo_versamento","?");

		// Insert singolaRevoca
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getSingolaRevocaFetch().getKeyGeneratorObject(SingolaRevoca.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getIdER(),SingolaRevoca.model().ID_ER.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getCausaleRevoca(),SingolaRevoca.model().CAUSALE_REVOCA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getDatiAggiuntiviRevoca(),SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getSingoloImporto(),SingolaRevoca.model().SINGOLO_IMPORTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getSingoloImportoRevocato(),SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getCausaleEsito(),SingolaRevoca.model().CAUSALE_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getDatiAggiuntiviEsito(),SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getStato(),SingolaRevoca.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singolaRevoca.getDescrizioneStato(),SingolaRevoca.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_rr,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_singoloVersamento,Long.class)
		);
		singolaRevoca.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRevoca oldId, SingolaRevoca singolaRevoca, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdSingolaRevoca(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = singolaRevoca.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: singolaRevoca.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			singolaRevoca.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, singolaRevoca, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SingolaRevoca singolaRevoca, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _singolaRevoca_rr
		Long id_singolaRevoca_rr = null;
		it.govpay.orm.IdRr idLogic_singolaRevoca_rr = null;
		idLogic_singolaRevoca_rr = singolaRevoca.getIdRR();
		if(idLogic_singolaRevoca_rr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singolaRevoca_rr = ((JDBCRRServiceSearch)(this.getServiceManager().getRRServiceSearch())).findTableId(idLogic_singolaRevoca_rr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singolaRevoca_rr = idLogic_singolaRevoca_rr.getId();
				if(id_singolaRevoca_rr==null || id_singolaRevoca_rr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object _singolaRevoca_singoloVersamento
		Long id_singolaRevoca_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_singolaRevoca_singoloVersamento = null;
		idLogic_singolaRevoca_singoloVersamento = singolaRevoca.getIdSingoloVersamento();
		if(idLogic_singolaRevoca_singoloVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singolaRevoca_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findTableId(idLogic_singolaRevoca_singoloVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singolaRevoca_singoloVersamento = idLogic_singolaRevoca_singoloVersamento.getId();
				if(id_singolaRevoca_singoloVersamento==null || id_singolaRevoca_singoloVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object singolaRevoca
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()));
		boolean isUpdate_singolaRevoca = true;
		java.util.List<JDBCObject> lstObjects_singolaRevoca = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().ID_ER,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getIdER(), SingolaRevoca.model().ID_ER.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().CAUSALE_REVOCA,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getCausaleRevoca(), SingolaRevoca.model().CAUSALE_REVOCA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getDatiAggiuntiviRevoca(), SingolaRevoca.model().DATI_AGGIUNTIVI_REVOCA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().SINGOLO_IMPORTO,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getSingoloImporto(), SingolaRevoca.model().SINGOLO_IMPORTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getSingoloImportoRevocato(), SingolaRevoca.model().SINGOLO_IMPORTO_REVOCATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().CAUSALE_ESITO,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getCausaleEsito(), SingolaRevoca.model().CAUSALE_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getDatiAggiuntiviEsito(), SingolaRevoca.model().DATI_AGGIUNTIVI_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().STATO,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getStato(), SingolaRevoca.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingolaRevocaFieldConverter().toColumn(SingolaRevoca.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_singolaRevoca.add(new JDBCObject(singolaRevoca.getDescrizioneStato(), SingolaRevoca.model().DESCRIZIONE_STATO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_rr","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_singolo_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singolaRevoca.add(new JDBCObject(id_singolaRevoca_rr, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singolaRevoca.add(new JDBCObject(id_singolaRevoca_singoloVersamento, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_singolaRevoca.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_singolaRevoca) {
			// Update singolaRevoca
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_singolaRevoca.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRevoca id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingolaRevocaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRevoca id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingolaRevocaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRevoca id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingolaRevocaFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingolaRevocaFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingolaRevocaFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingolaRevocaFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRevoca oldId, SingolaRevoca singolaRevoca, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, singolaRevoca,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, singolaRevoca,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SingolaRevoca singolaRevoca, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, singolaRevoca,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, singolaRevoca,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingolaRevoca singolaRevoca) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (singolaRevoca.getId()!=null) && (singolaRevoca.getId()>0) ){
			longId = singolaRevoca.getId();
		}
		else{
			IdSingolaRevoca idSingolaRevoca = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,singolaRevoca);
			longId = this.findIdSingolaRevoca(jdbcProperties,log,connection,sqlQueryObject,idSingolaRevoca,false);
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
		

		// Object singolaRevoca
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getSingolaRevocaFieldConverter().toTable(SingolaRevoca.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete singolaRevoca
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingolaRevoca idSingolaRevoca) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdSingolaRevoca(jdbcProperties, log, connection, sqlQueryObject, idSingolaRevoca, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getSingolaRevocaFieldConverter()));

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
