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
import it.govpay.orm.IdNotifica;
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

import it.govpay.orm.NotificaAppIO;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCNotificaAppIOServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCNotificaAppIOServiceImpl extends JDBCNotificaAppIOServiceSearchImpl
	implements IJDBCServiceCRUDWithId<NotificaAppIO, IdNotifica, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, NotificaAppIO notificaAppIO, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _versamento
		Long id_versamento = null;
		it.govpay.orm.IdVersamento idLogic_versamento = null;
		idLogic_versamento = notificaAppIO.getIdVersamento();
		if(idLogic_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento = idLogic_versamento.getId();
				if(id_versamento==null || id_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tipoVersamentoDominio
		Long id_tipoVersamentoDominio = null;
		it.govpay.orm.IdTipoVersamentoDominio idLogic_tipoVersamentoDominio = null;
		idLogic_tipoVersamentoDominio = notificaAppIO.getIdTipoVersamentoDominio();
		if(idLogic_tipoVersamentoDominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tipoVersamentoDominio = ((JDBCTipoVersamentoDominioServiceSearch)(this.getServiceManager().getTipoVersamentoDominioServiceSearch())).findTableId(idLogic_tipoVersamentoDominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tipoVersamentoDominio = idLogic_tipoVersamentoDominio.getId();
				if(id_tipoVersamentoDominio==null || id_tipoVersamentoDominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object notificaAppIO
		sqlQueryObjectInsert.addInsertTable(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()));
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DEBITORE_IDENTIFICATIVO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().COD_VERSAMENTO_ENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().COD_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().TIPO_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DATA_CREAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().TENTATIVI_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().ID_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().STATO_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_versamento","?");
		sqlQueryObjectInsert.addInsertField("id_tipo_versamento_dominio","?");

		// Insert notificaAppIO
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getNotificaAppIOFetch().getKeyGeneratorObject(NotificaAppIO.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getDebitoreIdentificativo(),NotificaAppIO.model().DEBITORE_IDENTIFICATIVO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getCodVersamentoEnte(),NotificaAppIO.model().COD_VERSAMENTO_ENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getCodApplicazione(),NotificaAppIO.model().COD_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getCodDominio(),NotificaAppIO.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getIuv(),NotificaAppIO.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getTipoEsito(),NotificaAppIO.model().TIPO_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getDataCreazione(),NotificaAppIO.model().DATA_CREAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getStato(),NotificaAppIO.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getDescrizioneStato(),NotificaAppIO.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getDataAggiornamentoStato(),NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getDataProssimaSpedizione(),NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getTentativiSpedizione(),NotificaAppIO.model().TENTATIVI_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getIdMessaggio(),NotificaAppIO.model().ID_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(notificaAppIO.getStatoMessaggio(),NotificaAppIO.model().STATO_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_versamento,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tipoVersamentoDominio,Long.class)
		);
		
		notificaAppIO.setId(id);
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica oldId, NotificaAppIO notificaAppIO, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdNotificaAppIO(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = notificaAppIO.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: notificaAppIO.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			notificaAppIO.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, notificaAppIO, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, NotificaAppIO notificaAppIO, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _notificaAppIO_versamento
		Long id_notificaAppIO_versamento = null;
		it.govpay.orm.IdVersamento idLogic_notificaAppIO_versamento = null;
		idLogic_notificaAppIO_versamento = notificaAppIO.getIdVersamento();
		if(idLogic_notificaAppIO_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_notificaAppIO_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_notificaAppIO_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_notificaAppIO_versamento = idLogic_notificaAppIO_versamento.getId();
				if(id_notificaAppIO_versamento==null || id_notificaAppIO_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _notificaAppIO_tipoVersamentoDominio
		Long id_notificaAppIO_tipoVersamentoDominio = null;
		it.govpay.orm.IdTipoVersamentoDominio idLogic_notificaAppIO_tipoVersamentoDominio = null;
		idLogic_notificaAppIO_tipoVersamentoDominio = notificaAppIO.getIdTipoVersamentoDominio();
		if(idLogic_notificaAppIO_tipoVersamentoDominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_notificaAppIO_tipoVersamentoDominio = ((JDBCTipoVersamentoDominioServiceSearch)(this.getServiceManager().getTipoVersamentoDominioServiceSearch())).findTableId(idLogic_notificaAppIO_tipoVersamentoDominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_notificaAppIO_tipoVersamentoDominio = idLogic_notificaAppIO_tipoVersamentoDominio.getId();
				if(id_notificaAppIO_tipoVersamentoDominio==null || id_notificaAppIO_tipoVersamentoDominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object notificaAppIO
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()));
		boolean isUpdate_notificaAppIO = true;
		java.util.List<JDBCObject> lstObjects_notificaAppIO = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DEBITORE_IDENTIFICATIVO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getDebitoreIdentificativo(), NotificaAppIO.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().COD_VERSAMENTO_ENTE,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getCodVersamentoEnte(), NotificaAppIO.model().COD_VERSAMENTO_ENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().COD_APPLICAZIONE,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getCodApplicazione(), NotificaAppIO.model().COD_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().COD_DOMINIO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getCodDominio(), NotificaAppIO.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().IUV,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getIuv(), NotificaAppIO.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().TIPO_ESITO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getTipoEsito(), NotificaAppIO.model().TIPO_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DATA_CREAZIONE,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getDataCreazione(), NotificaAppIO.model().DATA_CREAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().STATO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getStato(), NotificaAppIO.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getDescrizioneStato(), NotificaAppIO.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getDataAggiornamentoStato(), NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getDataProssimaSpedizione(), NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().TENTATIVI_SPEDIZIONE,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getTentativiSpedizione(), NotificaAppIO.model().TENTATIVI_SPEDIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().ID_MESSAGGIO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getIdMessaggio(), NotificaAppIO.model().ID_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getNotificaAppIOFieldConverter().toColumn(NotificaAppIO.model().STATO_MESSAGGIO,false), "?");
		lstObjects_notificaAppIO.add(new JDBCObject(notificaAppIO.getStatoMessaggio(), NotificaAppIO.model().STATO_MESSAGGIO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tipo_versamento_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_notificaAppIO.add(new JDBCObject(id_notificaAppIO_versamento, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_notificaAppIO.add(new JDBCObject(id_notificaAppIO_tipoVersamentoDominio, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_notificaAppIO.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_notificaAppIO) {
			// Update notificaAppIO
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_notificaAppIO.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getNotificaAppIOFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getNotificaAppIOFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getNotificaAppIOFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getNotificaAppIOFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getNotificaAppIOFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getNotificaAppIOFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica oldId, NotificaAppIO notificaAppIO, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, notificaAppIO,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, notificaAppIO,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, NotificaAppIO notificaAppIO, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, notificaAppIO,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, notificaAppIO,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, NotificaAppIO notificaAppIO) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (notificaAppIO.getId()!=null) && (notificaAppIO.getId()>0) ){
			longId = notificaAppIO.getId();
		}
		else{
			IdNotifica idNotificaAppIO = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,notificaAppIO);
			longId = this.findIdNotificaAppIO(jdbcProperties,log,connection,sqlQueryObject,idNotificaAppIO,false);
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
		

		// Object notificaAppIO
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getNotificaAppIOFieldConverter().toTable(NotificaAppIO.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete notificaAppIO
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdNotifica idNotificaAppIO) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdNotificaAppIO(jdbcProperties, log, connection, sqlQueryObject, idNotificaAppIO, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getNotificaAppIOFieldConverter()));

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
