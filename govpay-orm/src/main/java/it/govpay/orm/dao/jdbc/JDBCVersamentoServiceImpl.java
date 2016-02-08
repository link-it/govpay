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
import it.govpay.orm.IdVersamento;
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

import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCVersamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVersamentoServiceImpl extends JDBCVersamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Versamento, IdVersamento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _ente
		Long id_ente = null;
		it.govpay.orm.IdEnte idLogic_ente = null;
		idLogic_ente = versamento.getIdEnte();
		if(idLogic_ente!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findTableId(idLogic_ente, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ente = idLogic_ente.getId();
				if(id_ente==null || id_ente<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _applicazione
		Long id_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_applicazione = null;
		idLogic_applicazione = versamento.getIdApplicazione();
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

		// Object _anagrafica
		Long id_anagrafica = null;
		it.govpay.orm.IdAnagrafica idLogic_anagrafica = null;
		idLogic_anagrafica = versamento.getIdAnagraficaDebitore();
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


		// Object versamento
		sqlQueryObjectInsert.addInsertTable(this.getVersamentoFieldConverter().toTable(Versamento.model()));
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_VERSAMENTO_ENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IMPORTO_TOTALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().STATO_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().STATO_RENDICONTAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IMPORTO_PAGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_SCADENZA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_ente","?");
		sqlQueryObjectInsert.addInsertField("id_applicazione","?");
		sqlQueryObjectInsert.addInsertField("id_anagrafica_debitore","?");

		// Insert versamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getVersamentoFetch().getKeyGeneratorObject(Versamento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCodVersamentoEnte(),Versamento.model().COD_VERSAMENTO_ENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCodDominio(),Versamento.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getIuv(),Versamento.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getImportoTotale(),Versamento.model().IMPORTO_TOTALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getStatoVersamento(),Versamento.model().STATO_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDescrizioneStato(),Versamento.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getStatoRendicontazione(),Versamento.model().STATO_RENDICONTAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getImportoPagato(),Versamento.model().IMPORTO_PAGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDataScadenza(),Versamento.model().DATA_SCADENZA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDataOraUltimoAggiornamento(),Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_ente,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_anagrafica,Long.class)
		);
		versamento.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento oldId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdVersamento(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = versamento.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: versamento.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			versamento.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, versamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _versamento_ente
		Long id_versamento_ente = null;
		it.govpay.orm.IdEnte idLogic_versamento_ente = null;
		idLogic_versamento_ente = versamento.getIdEnte();
		if(idLogic_versamento_ente!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findTableId(idLogic_versamento_ente, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento_ente = idLogic_versamento_ente.getId();
				if(id_versamento_ente==null || id_versamento_ente<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _versamento_applicazione
		Long id_versamento_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_versamento_applicazione = null;
		idLogic_versamento_applicazione = versamento.getIdApplicazione();
		if(idLogic_versamento_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_versamento_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento_applicazione = idLogic_versamento_applicazione.getId();
				if(id_versamento_applicazione==null || id_versamento_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _versamento_anagrafica
		Long id_versamento_anagrafica = null;
		it.govpay.orm.IdAnagrafica idLogic_versamento_anagrafica = null;
		idLogic_versamento_anagrafica = versamento.getIdAnagraficaDebitore();
		if(idLogic_versamento_anagrafica!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento_anagrafica = ((JDBCAnagraficaServiceSearch)(this.getServiceManager().getAnagraficaServiceSearch())).findTableId(idLogic_versamento_anagrafica, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento_anagrafica = idLogic_versamento_anagrafica.getId();
				if(id_versamento_anagrafica==null || id_versamento_anagrafica<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object versamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getVersamentoFieldConverter().toTable(Versamento.model()));
		boolean isUpdate_versamento = true;
		java.util.List<JDBCObject> lstObjects_versamento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_VERSAMENTO_ENTE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCodVersamentoEnte(), Versamento.model().COD_VERSAMENTO_ENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_DOMINIO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCodDominio(), Versamento.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IUV,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getIuv(), Versamento.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IMPORTO_TOTALE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getImportoTotale(), Versamento.model().IMPORTO_TOTALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().STATO_VERSAMENTO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getStatoVersamento(), Versamento.model().STATO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDescrizioneStato(), Versamento.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().STATO_RENDICONTAZIONE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getStatoRendicontazione(), Versamento.model().STATO_RENDICONTAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IMPORTO_PAGATO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getImportoPagato(), Versamento.model().IMPORTO_PAGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_SCADENZA,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDataScadenza(), Versamento.model().DATA_SCADENZA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDataOraUltimoAggiornamento(), Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_ente","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_anagrafica_debitore","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_versamento.add(new JDBCObject(id_versamento_ente, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_versamento.add(new JDBCObject(id_versamento_applicazione, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_versamento.add(new JDBCObject(id_versamento_anagrafica, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_versamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_versamento) {
			// Update versamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_versamento.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getVersamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVersamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento oldId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, versamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, versamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, versamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, versamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Versamento versamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (versamento.getId()!=null) && (versamento.getId()>0) ){
			longId = versamento.getId();
		}
		else{
			IdVersamento idVersamento = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,versamento);
			longId = this.findIdVersamento(jdbcProperties,log,connection,sqlQueryObject,idVersamento,false);
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
		

		// Object versamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getVersamentoFieldConverter().toTable(Versamento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete versamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento idVersamento) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdVersamento(jdbcProperties, log, connection, sqlQueryObject, idVersamento, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getVersamentoFieldConverter()));

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
