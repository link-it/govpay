/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithoutId;
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

import it.govpay.orm.RendicontazioneSenzaRPT;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCRendicontazioneSenzaRPTServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRendicontazioneSenzaRPTServiceImpl extends JDBCRendicontazioneSenzaRPTServiceSearchImpl
	implements IJDBCServiceCRUDWithoutId<RendicontazioneSenzaRPT, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _frApplicazione
		Long id_frApplicazione = null;
		it.govpay.orm.IdFrApplicazione idLogic_frApplicazione = null;
		idLogic_frApplicazione = rendicontazioneSenzaRPT.getIdFrApplicazione();
		if(idLogic_frApplicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_frApplicazione = ((JDBCFrApplicazioneServiceSearch)(this.getServiceManager().getFrApplicazioneServiceSearch())).findTableId(idLogic_frApplicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_frApplicazione = idLogic_frApplicazione.getId();
				if(id_frApplicazione==null || id_frApplicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _iuv
		Long id_iuv = null;
		it.govpay.orm.IdIuv idLogic_iuv = null;
		idLogic_iuv = rendicontazioneSenzaRPT.getIdIuv();
		if(idLogic_iuv!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_iuv = ((JDBCIUVServiceSearch)(this.getServiceManager().getIUVServiceSearch())).findTableId(idLogic_iuv, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_iuv = idLogic_iuv.getId();
				if(id_iuv==null || id_iuv<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _singoloVersamento
		Long id_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_singoloVersamento = null;
		idLogic_singoloVersamento = rendicontazioneSenzaRPT.getIdSingoloVersamento();
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


		// Object rendicontazioneSenzaRPT
		sqlQueryObjectInsert.addInsertTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()));
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().IMPORTO_PAGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().IUR,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA,false),"?");
		sqlQueryObjectInsert.addInsertField("id_fr_applicazione","?");
		sqlQueryObjectInsert.addInsertField("id_iuv","?");
		sqlQueryObjectInsert.addInsertField("id_singolo_versamento","?");

		// Insert rendicontazioneSenzaRPT
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getRendicontazioneSenzaRPTFetch().getKeyGeneratorObject(RendicontazioneSenzaRPT.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazioneSenzaRPT.getImportoPagato(),RendicontazioneSenzaRPT.model().IMPORTO_PAGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazioneSenzaRPT.getIur(),RendicontazioneSenzaRPT.model().IUR.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rendicontazioneSenzaRPT.getRendicontazioneData(),RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_frApplicazione,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_iuv,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_singoloVersamento,Long.class)
		);
		rendicontazioneSenzaRPT.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		Long tableId = rendicontazioneSenzaRPT.getId();
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rendicontazioneSenzaRPT, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _rendicontazioneSenzaRPT_frApplicazione
		Long id_rendicontazioneSenzaRPT_frApplicazione = null;
		it.govpay.orm.IdFrApplicazione idLogic_rendicontazioneSenzaRPT_frApplicazione = null;
		idLogic_rendicontazioneSenzaRPT_frApplicazione = rendicontazioneSenzaRPT.getIdFrApplicazione();
		if(idLogic_rendicontazioneSenzaRPT_frApplicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rendicontazioneSenzaRPT_frApplicazione = ((JDBCFrApplicazioneServiceSearch)(this.getServiceManager().getFrApplicazioneServiceSearch())).findTableId(idLogic_rendicontazioneSenzaRPT_frApplicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rendicontazioneSenzaRPT_frApplicazione = idLogic_rendicontazioneSenzaRPT_frApplicazione.getId();
				if(id_rendicontazioneSenzaRPT_frApplicazione==null || id_rendicontazioneSenzaRPT_frApplicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rendicontazioneSenzaRPT_iuv
		Long id_rendicontazioneSenzaRPT_iuv = null;
		it.govpay.orm.IdIuv idLogic_rendicontazioneSenzaRPT_iuv = null;
		idLogic_rendicontazioneSenzaRPT_iuv = rendicontazioneSenzaRPT.getIdIuv();
		if(idLogic_rendicontazioneSenzaRPT_iuv!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rendicontazioneSenzaRPT_iuv = ((JDBCIUVServiceSearch)(this.getServiceManager().getIUVServiceSearch())).findTableId(idLogic_rendicontazioneSenzaRPT_iuv, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rendicontazioneSenzaRPT_iuv = idLogic_rendicontazioneSenzaRPT_iuv.getId();
				if(id_rendicontazioneSenzaRPT_iuv==null || id_rendicontazioneSenzaRPT_iuv<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rendicontazioneSenzaRPT_singoloVersamento
		Long id_rendicontazioneSenzaRPT_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_rendicontazioneSenzaRPT_singoloVersamento = null;
		idLogic_rendicontazioneSenzaRPT_singoloVersamento = rendicontazioneSenzaRPT.getIdSingoloVersamento();
		if(idLogic_rendicontazioneSenzaRPT_singoloVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rendicontazioneSenzaRPT_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findTableId(idLogic_rendicontazioneSenzaRPT_singoloVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rendicontazioneSenzaRPT_singoloVersamento = idLogic_rendicontazioneSenzaRPT_singoloVersamento.getId();
				if(id_rendicontazioneSenzaRPT_singoloVersamento==null || id_rendicontazioneSenzaRPT_singoloVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object rendicontazioneSenzaRPT
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()));
		boolean isUpdate_rendicontazioneSenzaRPT = true;
		java.util.List<JDBCObject> lstObjects_rendicontazioneSenzaRPT = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().IMPORTO_PAGATO,false), "?");
		lstObjects_rendicontazioneSenzaRPT.add(new JDBCObject(rendicontazioneSenzaRPT.getImportoPagato(), RendicontazioneSenzaRPT.model().IMPORTO_PAGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().IUR,false), "?");
		lstObjects_rendicontazioneSenzaRPT.add(new JDBCObject(rendicontazioneSenzaRPT.getIur(), RendicontazioneSenzaRPT.model().IUR.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRendicontazioneSenzaRPTFieldConverter().toColumn(RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA,false), "?");
		lstObjects_rendicontazioneSenzaRPT.add(new JDBCObject(rendicontazioneSenzaRPT.getRendicontazioneData(), RendicontazioneSenzaRPT.model().RENDICONTAZIONE_DATA.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_fr_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_iuv","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_singolo_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rendicontazioneSenzaRPT.add(new JDBCObject(id_rendicontazioneSenzaRPT_frApplicazione, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rendicontazioneSenzaRPT.add(new JDBCObject(id_rendicontazioneSenzaRPT_iuv, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rendicontazioneSenzaRPT.add(new JDBCObject(id_rendicontazioneSenzaRPT_singoloVersamento, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_rendicontazioneSenzaRPT.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_rendicontazioneSenzaRPT) {
			// Update rendicontazioneSenzaRPT
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_rendicontazioneSenzaRPT.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, rendicontazioneSenzaRPT),
				this.getRendicontazioneSenzaRPTFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, rendicontazioneSenzaRPT),
				this.getRendicontazioneSenzaRPTFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, rendicontazioneSenzaRPT),
				this.getRendicontazioneSenzaRPTFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRendicontazioneSenzaRPTFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRendicontazioneSenzaRPTFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRendicontazioneSenzaRPTFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		Long id = rendicontazioneSenzaRPT.getId();
		if(id != null && this.exists(jdbcProperties, log, connection, sqlQueryObject, id)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, rendicontazioneSenzaRPT,idMappingResolutionBehaviour);		
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rendicontazioneSenzaRPT,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, RendicontazioneSenzaRPT rendicontazioneSenzaRPT, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rendicontazioneSenzaRPT,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rendicontazioneSenzaRPT,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazioneSenzaRPT rendicontazioneSenzaRPT) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if(rendicontazioneSenzaRPT.getId()==null){
			throw new Exception("Parameter "+rendicontazioneSenzaRPT.getClass().getName()+".id is null");
		}
		if(rendicontazioneSenzaRPT.getId()<=0){
			throw new Exception("Parameter "+rendicontazioneSenzaRPT.getClass().getName()+".id is less equals 0");
		}
		longId = rendicontazioneSenzaRPT.getId();
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object rendicontazioneSenzaRPT
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getRendicontazioneSenzaRPTFieldConverter().toTable(RendicontazioneSenzaRPT.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete rendicontazioneSenzaRPT
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getRendicontazioneSenzaRPTFieldConverter()));

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
