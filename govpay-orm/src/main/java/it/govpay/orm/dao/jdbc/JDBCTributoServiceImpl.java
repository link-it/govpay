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
import it.govpay.orm.IdTributo;
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

import it.govpay.orm.Tributo;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCTributoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTributoServiceImpl extends JDBCTributoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Tributo, IdTributo, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tributo tributo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _dominio
		Long id_dominio = null;
		it.govpay.orm.IdDominio idLogic_dominio = null;
		idLogic_dominio = tributo.getIdDominio();
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

		// Object _ibanAccredito
		Long id_ibanAccredito = null;
		it.govpay.orm.IdIbanAccredito idLogic_ibanAccredito = null;
		idLogic_ibanAccredito = tributo.getIdIbanAccredito();
		if(idLogic_ibanAccredito!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ibanAccredito = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findTableId(idLogic_ibanAccredito, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ibanAccredito = idLogic_ibanAccredito.getId();
				if(id_ibanAccredito==null || id_ibanAccredito<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _ibanAccreditoInstance2
		Long id_ibanAccreditoInstance2 = null;
		it.govpay.orm.IdIbanAccredito idLogic_ibanAccreditoInstance2 = null;
		idLogic_ibanAccreditoInstance2 = tributo.getIdIbanAppoggio();
		if(idLogic_ibanAccreditoInstance2!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_ibanAccreditoInstance2 = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findTableId(idLogic_ibanAccreditoInstance2, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_ibanAccreditoInstance2 = idLogic_ibanAccreditoInstance2.getId();
				if(id_ibanAccreditoInstance2==null || id_ibanAccreditoInstance2<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tipoTributo
		Long id_tipoTributo = null;
		it.govpay.orm.IdTipoTributo idLogic_tipoTributo = new it.govpay.orm.IdTipoTributo();
		idLogic_tipoTributo.setCodTributo(tributo.getTipoTributo().getCodTributo());
		idLogic_tipoTributo.setId(tributo.getTipoTributo().getId());
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


		// Object tributo
		sqlQueryObjectInsert.addInsertTable(this.getTributoFieldConverter().toTable(Tributo.model()));
		sqlQueryObjectInsert.addInsertField(this.getTributoFieldConverter().toColumn(Tributo.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTributoFieldConverter().toColumn(Tributo.model().TIPO_CONTABILITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTributoFieldConverter().toColumn(Tributo.model().CODICE_CONTABILITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTributoFieldConverter().toColumn(Tributo.model().COD_TRIBUTO_IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTributoFieldConverter().toColumn(Tributo.model().ON_LINE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTributoFieldConverter().toColumn(Tributo.model().PAGA_TERZI,false),"?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");
		sqlQueryObjectInsert.addInsertField("id_iban_accredito","?");
		sqlQueryObjectInsert.addInsertField("id_iban_appoggio","?");
		sqlQueryObjectInsert.addInsertField("id_tipo_tributo","?");

		// Insert tributo
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getTributoFetch().getKeyGeneratorObject(Tributo.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tributo.getAbilitato(),Tributo.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tributo.getTipoContabilita(),Tributo.model().TIPO_CONTABILITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tributo.getCodiceContabilita(),Tributo.model().CODICE_CONTABILITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tributo.getCodTributoIuv(),Tributo.model().COD_TRIBUTO_IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tributo.getOnLine(),Tributo.model().ON_LINE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tributo.getPagaTerzi(),Tributo.model().PAGA_TERZI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_ibanAccredito,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_ibanAccreditoInstance2,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tipoTributo,Long.class)
		);
		tributo.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo oldId, Tributo tributo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdTributo(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = tributo.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: tributo.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			tributo.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tributo, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Tributo tributo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _tributo_dominio
		Long id_tributo_dominio = null;
		it.govpay.orm.IdDominio idLogic_tributo_dominio = null;
		idLogic_tributo_dominio = tributo.getIdDominio();
		if(idLogic_tributo_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tributo_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_tributo_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tributo_dominio = idLogic_tributo_dominio.getId();
				if(id_tributo_dominio==null || id_tributo_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tributo_ibanAccredito
		Long id_tributo_ibanAccredito = null;
		it.govpay.orm.IdIbanAccredito idLogic_tributo_ibanAccredito = null;
		idLogic_tributo_ibanAccredito = tributo.getIdIbanAccredito();
		if(idLogic_tributo_ibanAccredito!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tributo_ibanAccredito = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findTableId(idLogic_tributo_ibanAccredito, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tributo_ibanAccredito = idLogic_tributo_ibanAccredito.getId();
				if(id_tributo_ibanAccredito==null || id_tributo_ibanAccredito<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tributo_ibanAccreditoInstance2
		Long id_tributo_ibanAccreditoInstance2 = null;
		it.govpay.orm.IdIbanAccredito idLogic_tributo_ibanAccreditoInstance2 = null;
		idLogic_tributo_ibanAccreditoInstance2 = tributo.getIdIbanAppoggio();
		if(idLogic_tributo_ibanAccreditoInstance2!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tributo_ibanAccreditoInstance2 = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findTableId(idLogic_tributo_ibanAccreditoInstance2, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tributo_ibanAccreditoInstance2 = idLogic_tributo_ibanAccreditoInstance2.getId();
				if(id_tributo_ibanAccreditoInstance2==null || id_tributo_ibanAccreditoInstance2<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tributo_tipoTributo
		Long id_tributo_tipoTributo = null;
		it.govpay.orm.IdTipoTributo idLogic_tributo_tipoTributo = new it.govpay.orm.IdTipoTributo();
		idLogic_tributo_tipoTributo.setCodTributo(tributo.getTipoTributo().getCodTributo());
		idLogic_tributo_tipoTributo.setId(tributo.getTipoTributo().getId());
		if(idLogic_tributo_tipoTributo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tributo_tipoTributo = ((JDBCTipoTributoServiceSearch)(this.getServiceManager().getTipoTributoServiceSearch())).findTableId(idLogic_tributo_tipoTributo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tributo_tipoTributo = idLogic_tributo_tipoTributo.getId();
				if(id_tributo_tipoTributo==null || id_tributo_tipoTributo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		if(idLogic_tributo_tipoTributo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tributo_tipoTributo = ((JDBCTipoTributoServiceSearch)(this.getServiceManager().getTipoTributoServiceSearch())).findTableId(idLogic_tributo_tipoTributo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tributo_tipoTributo = idLogic_tributo_tipoTributo.getId();
				if(id_tributo_tipoTributo==null || id_tributo_tipoTributo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object tributo
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getTributoFieldConverter().toTable(Tributo.model()));
		boolean isUpdate_tributo = true;
		java.util.List<JDBCObject> lstObjects_tributo = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getTributoFieldConverter().toColumn(Tributo.model().ABILITATO,false), "?");
		lstObjects_tributo.add(new JDBCObject(tributo.getAbilitato(), Tributo.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTributoFieldConverter().toColumn(Tributo.model().TIPO_CONTABILITA,false), "?");
		lstObjects_tributo.add(new JDBCObject(tributo.getTipoContabilita(), Tributo.model().TIPO_CONTABILITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTributoFieldConverter().toColumn(Tributo.model().CODICE_CONTABILITA,false), "?");
		lstObjects_tributo.add(new JDBCObject(tributo.getCodiceContabilita(), Tributo.model().CODICE_CONTABILITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTributoFieldConverter().toColumn(Tributo.model().COD_TRIBUTO_IUV,false), "?");
		lstObjects_tributo.add(new JDBCObject(tributo.getCodTributoIuv(), Tributo.model().COD_TRIBUTO_IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTributoFieldConverter().toColumn(Tributo.model().ON_LINE,false), "?");
		lstObjects_tributo.add(new JDBCObject(tributo.getOnLine(), Tributo.model().ON_LINE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTributoFieldConverter().toColumn(Tributo.model().PAGA_TERZI,false), "?");
		lstObjects_tributo.add(new JDBCObject(tributo.getPagaTerzi(), Tributo.model().PAGA_TERZI.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_iban_accredito","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_iban_appoggio","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tipo_tributo","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tributo.add(new JDBCObject(id_tributo_dominio, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tributo.add(new JDBCObject(id_tributo_ibanAccredito, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tributo.add(new JDBCObject(id_tributo_ibanAccreditoInstance2, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tributo.add(new JDBCObject(id_tributo_tipoTributo, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_tributo.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_tributo) {
			// Update tributo
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_tributo.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTributoFieldConverter().toTable(Tributo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTributoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTributoFieldConverter().toTable(Tributo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTributoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTributoFieldConverter().toTable(Tributo.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTributoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTributoFieldConverter().toTable(Tributo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTributoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTributoFieldConverter().toTable(Tributo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTributoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTributoFieldConverter().toTable(Tributo.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTributoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo oldId, Tributo tributo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, tributo,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tributo,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Tributo tributo, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tributo,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tributo,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Tributo tributo) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (tributo.getId()!=null) && (tributo.getId()>0) ){
			longId = tributo.getId();
		}
		else{
			IdTributo idTributo = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,tributo);
			longId = this.findIdTributo(jdbcProperties,log,connection,sqlQueryObject,idTributo,false);
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
		

		// Object tributo
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getTributoFieldConverter().toTable(Tributo.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete tributo
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTributo idTributo) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdTributo(jdbcProperties, log, connection, sqlQueryObject, idTributo, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getTributoFieldConverter()));

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
