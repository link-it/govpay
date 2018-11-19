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

import it.govpay.orm.Operazione;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCOperazioneServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCOperazioneServiceImpl extends JDBCOperazioneServiceSearchImpl
	implements IJDBCServiceCRUDWithoutId<Operazione, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _tracciato
		Long id_tracciato = null;
		it.govpay.orm.IdTracciato idLogic_tracciato = null;
		idLogic_tracciato = operazione.getIdTracciato();
		if(idLogic_tracciato!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciato = ((JDBCTracciatoServiceSearch)(this.getServiceManager().getTracciatoServiceSearch())).findTableId(idLogic_tracciato, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciato = idLogic_tracciato.getId();
				if(id_tracciato==null || id_tracciato<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _applicazione
		Long id_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_applicazione = null;
		idLogic_applicazione = operazione.getIdApplicazione();
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


		// Object operazione
		sqlQueryObjectInsert.addInsertTable(this.getOperazioneFieldConverter().toTable(Operazione.model()));
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().TIPO_OPERAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().LINEA_ELABORAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().DATI_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().DATI_RISPOSTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().DETTAGLIO_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().COD_VERSAMENTO_ENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperazioneFieldConverter().toColumn(Operazione.model().TRN,false),"?");
		sqlQueryObjectInsert.addInsertField("id_tracciato","?");
		sqlQueryObjectInsert.addInsertField("id_applicazione","?");

		// Insert operazione
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getOperazioneFetch().getKeyGeneratorObject(Operazione.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getTipoOperazione(),Operazione.model().TIPO_OPERAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getLineaElaborazione(),Operazione.model().LINEA_ELABORAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getStato(),Operazione.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getDatiRichiesta(),Operazione.model().DATI_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getDatiRisposta(),Operazione.model().DATI_RISPOSTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getDettaglioEsito(),Operazione.model().DETTAGLIO_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getCodVersamentoEnte(),Operazione.model().COD_VERSAMENTO_ENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getCodDominio(),Operazione.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getIuv(),Operazione.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operazione.getTrn(),Operazione.model().TRN.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tracciato,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class)
		);
		operazione.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		Long tableId = operazione.getId();
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, operazione, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Operazione operazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _operazione_tracciato
		Long id_operazione_tracciato = null;
		it.govpay.orm.IdTracciato idLogic_operazione_tracciato = null;
		idLogic_operazione_tracciato = operazione.getIdTracciato();
		if(idLogic_operazione_tracciato!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_operazione_tracciato = ((JDBCTracciatoServiceSearch)(this.getServiceManager().getTracciatoServiceSearch())).findTableId(idLogic_operazione_tracciato, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_operazione_tracciato = idLogic_operazione_tracciato.getId();
				if(id_operazione_tracciato==null || id_operazione_tracciato<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _operazione_applicazione
		Long id_operazione_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_operazione_applicazione = null;
		idLogic_operazione_applicazione = operazione.getIdApplicazione();
		if(idLogic_operazione_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_operazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_operazione_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_operazione_applicazione = idLogic_operazione_applicazione.getId();
				if(id_operazione_applicazione==null || id_operazione_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object operazione
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getOperazioneFieldConverter().toTable(Operazione.model()));
		boolean isUpdate_operazione = true;
		java.util.List<JDBCObject> lstObjects_operazione = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().TIPO_OPERAZIONE,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getTipoOperazione(), Operazione.model().TIPO_OPERAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().LINEA_ELABORAZIONE,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getLineaElaborazione(), Operazione.model().LINEA_ELABORAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().STATO,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getStato(), Operazione.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().DATI_RICHIESTA,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getDatiRichiesta(), Operazione.model().DATI_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().DATI_RISPOSTA,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getDatiRisposta(), Operazione.model().DATI_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().DETTAGLIO_ESITO,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getDettaglioEsito(), Operazione.model().DETTAGLIO_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().COD_VERSAMENTO_ENTE,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getCodVersamentoEnte(), Operazione.model().COD_VERSAMENTO_ENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().COD_DOMINIO,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getCodDominio(), Operazione.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().IUV,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getIuv(), Operazione.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperazioneFieldConverter().toColumn(Operazione.model().TRN,false), "?");
		lstObjects_operazione.add(new JDBCObject(operazione.getTrn(), Operazione.model().TRN.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tracciato","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_operazione.add(new JDBCObject(id_operazione_tracciato, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_operazione.add(new JDBCObject(id_operazione_applicazione, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_operazione.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_operazione) {
			// Update operazione
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_operazione.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperazioneFieldConverter().toTable(Operazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, operazione),
				this.getOperazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperazioneFieldConverter().toTable(Operazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, operazione),
				this.getOperazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperazioneFieldConverter().toTable(Operazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, operazione),
				this.getOperazioneFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperazioneFieldConverter().toTable(Operazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getOperazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperazioneFieldConverter().toTable(Operazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getOperazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperazioneFieldConverter().toTable(Operazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getOperazioneFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		Long id = operazione.getId();
		if(id != null && this.exists(jdbcProperties, log, connection, sqlQueryObject, id)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, operazione,idMappingResolutionBehaviour);		
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, operazione,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Operazione operazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, operazione,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, operazione,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operazione operazione) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if(operazione.getId()==null){
			throw new Exception("Parameter "+operazione.getClass().getName()+".id is null");
		}
		if(operazione.getId()<=0){
			throw new Exception("Parameter "+operazione.getClass().getName()+".id is less equals 0");
		}
		longId = operazione.getId();
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object operazione
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getOperazioneFieldConverter().toTable(Operazione.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete operazione
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getOperazioneFieldConverter()));

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
