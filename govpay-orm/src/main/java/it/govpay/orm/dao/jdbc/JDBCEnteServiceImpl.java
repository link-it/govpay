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
import it.govpay.orm.IdEnte;
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

import it.govpay.orm.Ente;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCEnteServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCEnteServiceImpl extends JDBCEnteServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Ente, IdEnte, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Ente ente, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _anagrafica
		Long id_anagrafica = null;
		it.govpay.orm.IdAnagrafica idLogic_anagrafica = null;
		idLogic_anagrafica = ente.getIdAnagraficaEnte();
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

		// Object _dominio
		Long id_dominio = null;
		it.govpay.orm.IdDominio idLogic_dominio = null;
		idLogic_dominio = ente.getIdDominio();
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

		// Object _mailTemplate
		Long id_mailTemplate = null;
		it.govpay.orm.IdMailTemplate idLogic_mailTemplate = null;
		idLogic_mailTemplate = ente.getIdTemplateRPT();
		if(idLogic_mailTemplate!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_mailTemplate = ((JDBCMailTemplateServiceSearch)(this.getServiceManager().getMailTemplateServiceSearch())).findTableId(idLogic_mailTemplate, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_mailTemplate = idLogic_mailTemplate.getId();
				if(id_mailTemplate==null || id_mailTemplate<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _mailTemplateInstance2
		Long id_mailTemplateInstance2 = null;
		it.govpay.orm.IdMailTemplate idLogic_mailTemplateInstance2 = null;
		idLogic_mailTemplateInstance2 = ente.getIdTemplateRT();
		if(idLogic_mailTemplateInstance2!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_mailTemplateInstance2 = ((JDBCMailTemplateServiceSearch)(this.getServiceManager().getMailTemplateServiceSearch())).findTableId(idLogic_mailTemplateInstance2, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_mailTemplateInstance2 = idLogic_mailTemplateInstance2.getId();
				if(id_mailTemplateInstance2==null || id_mailTemplateInstance2<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object ente
		sqlQueryObjectInsert.addInsertTable(this.getEnteFieldConverter().toTable(Ente.model()));
		sqlQueryObjectInsert.addInsertField(this.getEnteFieldConverter().toColumn(Ente.model().COD_ENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEnteFieldConverter().toColumn(Ente.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEnteFieldConverter().toColumn(Ente.model().INVIO_MAIL_RPTABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEnteFieldConverter().toColumn(Ente.model().INVIO_MAIL_RTABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_anagrafica_ente","?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");
		sqlQueryObjectInsert.addInsertField("id_template_rpt","?");
		sqlQueryObjectInsert.addInsertField("id_template_rt","?");

		// Insert ente
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getEnteFetch().getKeyGeneratorObject(Ente.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ente.getCodEnte(),Ente.model().COD_ENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ente.getAbilitato(),Ente.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ente.getInvioMailRPTAbilitato(),Ente.model().INVIO_MAIL_RPTABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(ente.getInvioMailRTAbilitato(),Ente.model().INVIO_MAIL_RTABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_anagrafica,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_mailTemplate,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_mailTemplateInstance2,Long.class)
		);
		ente.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEnte oldId, Ente ente, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdEnte(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = ente.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: ente.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			ente.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, ente, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Ente ente, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _ente_anagrafica
		Long id_ente_anagrafica = null;
		it.govpay.orm.IdAnagrafica idLogic_ente_anagrafica = null;
		idLogic_ente_anagrafica = ente.getIdAnagraficaEnte();
		if(idLogic_ente_anagrafica!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ente_anagrafica = ((JDBCAnagraficaServiceSearch)(this.getServiceManager().getAnagraficaServiceSearch())).findTableId(idLogic_ente_anagrafica, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ente_anagrafica = idLogic_ente_anagrafica.getId();
				if(id_ente_anagrafica==null || id_ente_anagrafica<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _ente_dominio
		Long id_ente_dominio = null;
		it.govpay.orm.IdDominio idLogic_ente_dominio = null;
		idLogic_ente_dominio = ente.getIdDominio();
		if(idLogic_ente_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ente_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_ente_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ente_dominio = idLogic_ente_dominio.getId();
				if(id_ente_dominio==null || id_ente_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _ente_mailTemplate
		Long id_ente_mailTemplate = null;
		it.govpay.orm.IdMailTemplate idLogic_ente_mailTemplate = null;
		idLogic_ente_mailTemplate = ente.getIdTemplateRPT();
		if(idLogic_ente_mailTemplate!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ente_mailTemplate = ((JDBCMailTemplateServiceSearch)(this.getServiceManager().getMailTemplateServiceSearch())).findTableId(idLogic_ente_mailTemplate, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ente_mailTemplate = idLogic_ente_mailTemplate.getId();
				if(id_ente_mailTemplate==null || id_ente_mailTemplate<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _ente_mailTemplateInstance2
		Long id_ente_mailTemplateInstance2 = null;
		it.govpay.orm.IdMailTemplate idLogic_ente_mailTemplateInstance2 = null;
		idLogic_ente_mailTemplateInstance2 = ente.getIdTemplateRT();
		if(idLogic_ente_mailTemplateInstance2!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ente_mailTemplateInstance2 = ((JDBCMailTemplateServiceSearch)(this.getServiceManager().getMailTemplateServiceSearch())).findTableId(idLogic_ente_mailTemplateInstance2, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ente_mailTemplateInstance2 = idLogic_ente_mailTemplateInstance2.getId();
				if(id_ente_mailTemplateInstance2==null || id_ente_mailTemplateInstance2<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object ente
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getEnteFieldConverter().toTable(Ente.model()));
		boolean isUpdate_ente = true;
		java.util.List<JDBCObject> lstObjects_ente = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getEnteFieldConverter().toColumn(Ente.model().COD_ENTE,false), "?");
		lstObjects_ente.add(new JDBCObject(ente.getCodEnte(), Ente.model().COD_ENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEnteFieldConverter().toColumn(Ente.model().ABILITATO,false), "?");
		lstObjects_ente.add(new JDBCObject(ente.getAbilitato(), Ente.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEnteFieldConverter().toColumn(Ente.model().INVIO_MAIL_RPTABILITATO,false), "?");
		lstObjects_ente.add(new JDBCObject(ente.getInvioMailRPTAbilitato(), Ente.model().INVIO_MAIL_RPTABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEnteFieldConverter().toColumn(Ente.model().INVIO_MAIL_RTABILITATO,false), "?");
		lstObjects_ente.add(new JDBCObject(ente.getInvioMailRTAbilitato(), Ente.model().INVIO_MAIL_RTABILITATO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_anagrafica_ente","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_template_rpt","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_template_rt","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_ente.add(new JDBCObject(id_ente_anagrafica, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_ente.add(new JDBCObject(id_ente_dominio, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_ente.add(new JDBCObject(id_ente_mailTemplate, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_ente.add(new JDBCObject(id_ente_mailTemplateInstance2, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_ente.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_ente) {
			// Update ente
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_ente.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEnte id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEnteFieldConverter().toTable(Ente.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getEnteFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEnte id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEnteFieldConverter().toTable(Ente.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getEnteFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEnte id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEnteFieldConverter().toTable(Ente.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getEnteFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEnteFieldConverter().toTable(Ente.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEnteFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEnteFieldConverter().toTable(Ente.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEnteFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEnteFieldConverter().toTable(Ente.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEnteFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEnte oldId, Ente ente, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, ente,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, ente,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Ente ente, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, ente,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, ente,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Ente ente) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (ente.getId()!=null) && (ente.getId()>0) ){
			longId = ente.getId();
		}
		else{
			IdEnte idEnte = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,ente);
			longId = this.findIdEnte(jdbcProperties,log,connection,sqlQueryObject,idEnte,false);
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
		

		// Object ente
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getEnteFieldConverter().toTable(Ente.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete ente
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEnte idEnte) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdEnte(jdbcProperties, log, connection, sqlQueryObject, idEnte, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getEnteFieldConverter()));

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
