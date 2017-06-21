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

import org.apache.log4j.Logger;

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdAcl;
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

import it.govpay.orm.ACL;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCACLServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCACLServiceImpl extends JDBCACLServiceSearchImpl
	implements IJDBCServiceCRUDWithId<ACL, IdAcl, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, ACL acl, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _applicazione
		Long id_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_applicazione = null;
		idLogic_applicazione = acl.getIdApplicazione();
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

		// Object _portale
		Long id_portale = null;
		it.govpay.orm.IdPortale idLogic_portale = null;
		idLogic_portale = acl.getIdPortale();
		if(idLogic_portale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findTableId(idLogic_portale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_portale = idLogic_portale.getId();
				if(id_portale==null || id_portale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _operatore
		Long id_operatore = null;
		it.govpay.orm.IdOperatore idLogic_operatore = null;
		idLogic_operatore = acl.getIdOperatore();
		if(idLogic_operatore!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_operatore = ((JDBCOperatoreServiceSearch)(this.getServiceManager().getOperatoreServiceSearch())).findTableId(idLogic_operatore, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_operatore = idLogic_operatore.getId();
				if(id_operatore==null || id_operatore<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _ruolo
		Long id_ruolo = null;
		it.govpay.orm.IdRuolo idLogic_ruolo = null;
		idLogic_ruolo = acl.getIdRuolo();
		if(idLogic_ruolo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ruolo = ((JDBCRuoloServiceSearch)(this.getServiceManager().getRuoloServiceSearch())).findTableId(idLogic_ruolo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ruolo = idLogic_ruolo.getId();
				if(id_ruolo==null || id_ruolo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _dominio
		Long id_dominio = null;
		it.govpay.orm.IdDominio idLogic_dominio = null;
		idLogic_dominio = acl.getIdDominio();
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

		// Object _tipoTributo
		Long id_tipoTributo = null;
		it.govpay.orm.IdTipoTributo idLogic_tipoTributo = null;
		idLogic_tipoTributo = acl.getIdTipoTributo();
		if(idLogic_tipoTributo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tipoTributo = ((JDBCTipoTributoServiceSearch)(this.getServiceManager().getTipoTributoServiceSearch())).findTableId(idLogic_tipoTributo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tipoTributo = idLogic_tipoTributo.getId();
				if(id_tipoTributo==null || id_tipoTributo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object acl
		sqlQueryObjectInsert.addInsertTable(this.getACLFieldConverter().toTable(ACL.model()));
		sqlQueryObjectInsert.addInsertField(this.getACLFieldConverter().toColumn(ACL.model().COD_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getACLFieldConverter().toColumn(ACL.model().DIRITTI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getACLFieldConverter().toColumn(ACL.model().COD_SERVIZIO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_applicazione","?");
		sqlQueryObjectInsert.addInsertField("id_portale","?");
		sqlQueryObjectInsert.addInsertField("id_operatore","?");
		sqlQueryObjectInsert.addInsertField("id_ruolo","?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");
		sqlQueryObjectInsert.addInsertField("id_tipo_tributo","?");

		// Insert acl
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getACLFetch().getKeyGeneratorObject(ACL.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(acl.getCodTipo(),ACL.model().COD_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(acl.getDiritti(),ACL.model().DIRITTI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(acl.getCodServizio(),ACL.model().COD_SERVIZIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_portale,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_ruolo,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tipoTributo,Long.class)
		);
		acl.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAcl oldId, ACL acl, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdACL(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = acl.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: acl.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			acl.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, acl, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, ACL acl, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _acl_applicazione
		Long id_acl_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_acl_applicazione = null;
		idLogic_acl_applicazione = acl.getIdApplicazione();
		if(idLogic_acl_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_acl_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_acl_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_acl_applicazione = idLogic_acl_applicazione.getId();
				if(id_acl_applicazione==null || id_acl_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _acl_portale
		Long id_acl_portale = null;
		it.govpay.orm.IdPortale idLogic_acl_portale = null;
		idLogic_acl_portale = acl.getIdPortale();
		if(idLogic_acl_portale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_acl_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findTableId(idLogic_acl_portale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_acl_portale = idLogic_acl_portale.getId();
				if(id_acl_portale==null || id_acl_portale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _acl_operatore
		Long id_acl_operatore = null;
		it.govpay.orm.IdOperatore idLogic_acl_operatore = null;
		idLogic_acl_operatore = acl.getIdOperatore();
		if(idLogic_acl_operatore!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_acl_operatore = ((JDBCOperatoreServiceSearch)(this.getServiceManager().getOperatoreServiceSearch())).findTableId(idLogic_acl_operatore, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_acl_operatore = idLogic_acl_operatore.getId();
				if(id_acl_operatore==null || id_acl_operatore<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _acl_ruolo
		Long id_acl_ruolo = null;
		it.govpay.orm.IdRuolo idLogic_acl_ruolo = null;
		idLogic_acl_ruolo = acl.getIdRuolo();
		if(idLogic_acl_ruolo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_acl_ruolo = ((JDBCRuoloServiceSearch)(this.getServiceManager().getRuoloServiceSearch())).findTableId(idLogic_acl_ruolo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_acl_ruolo = idLogic_acl_ruolo.getId();
				if(id_acl_ruolo==null || id_acl_ruolo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _acl_dominio
		Long id_acl_dominio = null;
		it.govpay.orm.IdDominio idLogic_acl_dominio = null;
		idLogic_acl_dominio = acl.getIdDominio();
		if(idLogic_acl_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_acl_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_acl_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_acl_dominio = idLogic_acl_dominio.getId();
				if(id_acl_dominio==null || id_acl_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _acl_tipoTributo
		Long id_acl_tipoTributo = null;
		it.govpay.orm.IdTipoTributo idLogic_acl_tipoTributo = null;
		idLogic_acl_tipoTributo = acl.getIdTipoTributo();
		if(idLogic_acl_tipoTributo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_acl_tipoTributo = ((JDBCTipoTributoServiceSearch)(this.getServiceManager().getTipoTributoServiceSearch())).findTableId(idLogic_acl_tipoTributo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_acl_tipoTributo = idLogic_acl_tipoTributo.getId();
				if(id_acl_tipoTributo==null || id_acl_tipoTributo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object acl
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getACLFieldConverter().toTable(ACL.model()));
		boolean isUpdate_acl = true;
		java.util.List<JDBCObject> lstObjects_acl = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getACLFieldConverter().toColumn(ACL.model().COD_TIPO,false), "?");
		lstObjects_acl.add(new JDBCObject(acl.getCodTipo(), ACL.model().COD_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getACLFieldConverter().toColumn(ACL.model().DIRITTI,false), "?");
		lstObjects_acl.add(new JDBCObject(acl.getDiritti(), ACL.model().DIRITTI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getACLFieldConverter().toColumn(ACL.model().COD_SERVIZIO,false), "?");
		lstObjects_acl.add(new JDBCObject(acl.getCodServizio(), ACL.model().COD_SERVIZIO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_portale","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_operatore","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_ruolo","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tipo_tributo","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_acl.add(new JDBCObject(id_acl_applicazione, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_acl.add(new JDBCObject(id_acl_portale, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_acl.add(new JDBCObject(id_acl_operatore, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_acl.add(new JDBCObject(id_acl_ruolo, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_acl.add(new JDBCObject(id_acl_dominio, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_acl.add(new JDBCObject(id_acl_tipoTributo, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_acl.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_acl) {
			// Update acl
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_acl.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAcl id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getACLFieldConverter().toTable(ACL.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getACLFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAcl id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getACLFieldConverter().toTable(ACL.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getACLFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAcl id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getACLFieldConverter().toTable(ACL.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getACLFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getACLFieldConverter().toTable(ACL.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getACLFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getACLFieldConverter().toTable(ACL.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getACLFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getACLFieldConverter().toTable(ACL.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getACLFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAcl oldId, ACL acl, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, acl,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, acl,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, ACL acl, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, acl,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, acl,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, ACL acl) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (acl.getId()!=null) && (acl.getId()>0) ){
			longId = acl.getId();
		}
		else{
			IdAcl idACL = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,acl);
			longId = this.findIdACL(jdbcProperties,log,connection,sqlQueryObject,idACL,false);
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
		

		// Object acl
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getACLFieldConverter().toTable(ACL.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete acl
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdAcl idACL) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdACL(jdbcProperties, log, connection, sqlQueryObject, idACL, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getACLFieldConverter()));

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
