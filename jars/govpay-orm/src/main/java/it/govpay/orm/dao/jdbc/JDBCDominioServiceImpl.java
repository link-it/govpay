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
import it.govpay.orm.IdDominio;
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

import it.govpay.orm.Dominio;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCDominioServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCDominioServiceImpl extends JDBCDominioServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Dominio, IdDominio, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _stazione
		Long id_stazione = null;
		it.govpay.orm.IdStazione idLogic_stazione = null;
		idLogic_stazione = dominio.getIdStazione();
		if(idLogic_stazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_stazione = ((JDBCStazioneServiceSearch)(this.getServiceManager().getStazioneServiceSearch())).findTableId(idLogic_stazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_stazione = idLogic_stazione.getId();
				if(id_stazione==null || id_stazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _applicazione
		Long id_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_applicazione = null;
		idLogic_applicazione = dominio.getIdApplicazioneDefault();
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


		// Object dominio
		sqlQueryObjectInsert.addInsertTable(this.getDominioFieldConverter().toTable(Dominio.model()));
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().GLN,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().RAGIONE_SOCIALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().AUX_DIGIT,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().IUV_PREFIX,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().SEGREGATION_CODE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_OPERAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_DESCRIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_DATA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().LOGO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().CBILL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getDominioFieldConverter().toColumn(Dominio.model().AUT_STAMPA_POSTE,false),"?");
		sqlQueryObjectInsert.addInsertField("id_stazione","?");
		sqlQueryObjectInsert.addInsertField("id_applicazione_default","?");

		// Insert dominio
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getDominioFetch().getKeyGeneratorObject(Dominio.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getCodDominio(),Dominio.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getGln(),Dominio.model().GLN.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getAbilitato(),Dominio.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getRagioneSociale(),Dominio.model().RAGIONE_SOCIALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getAuxDigit(),Dominio.model().AUX_DIGIT.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getIuvPrefix(),Dominio.model().IUV_PREFIX.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getSegregationCode(),Dominio.model().SEGREGATION_CODE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getNdpStato(),Dominio.model().NDP_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getNdpOperazione(),Dominio.model().NDP_OPERAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getNdpDescrizione(),Dominio.model().NDP_DESCRIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getNdpData(),Dominio.model().NDP_DATA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getLogo(),Dominio.model().LOGO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getCbill(),Dominio.model().CBILL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(dominio.getAutStampaPoste(),Dominio.model().AUT_STAMPA_POSTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_stazione,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class)
		);
		dominio.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio oldId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdDominio(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = dominio.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: dominio.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			dominio.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, dominio, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _dominio_stazione
		Long id_dominio_stazione = null;
		it.govpay.orm.IdStazione idLogic_dominio_stazione = null;
		idLogic_dominio_stazione = dominio.getIdStazione();
		if(idLogic_dominio_stazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_dominio_stazione = ((JDBCStazioneServiceSearch)(this.getServiceManager().getStazioneServiceSearch())).findTableId(idLogic_dominio_stazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_dominio_stazione = idLogic_dominio_stazione.getId();
				if(id_dominio_stazione==null || id_dominio_stazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _dominio_applicazione
		Long id_dominio_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_dominio_applicazione = null;
		idLogic_dominio_applicazione = dominio.getIdApplicazioneDefault();
		if(idLogic_dominio_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_dominio_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_dominio_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_dominio_applicazione = idLogic_dominio_applicazione.getId();
				if(id_dominio_applicazione==null || id_dominio_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object dominio
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getDominioFieldConverter().toTable(Dominio.model()));
		boolean isUpdate_dominio = true;
		java.util.List<JDBCObject> lstObjects_dominio = new java.util.ArrayList<>();
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().COD_DOMINIO,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getCodDominio(), Dominio.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().GLN,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getGln(), Dominio.model().GLN.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().ABILITATO,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getAbilitato(), Dominio.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().RAGIONE_SOCIALE,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getRagioneSociale(), Dominio.model().RAGIONE_SOCIALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().AUX_DIGIT,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getAuxDigit(), Dominio.model().AUX_DIGIT.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().IUV_PREFIX,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getIuvPrefix(), Dominio.model().IUV_PREFIX.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().SEGREGATION_CODE,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getSegregationCode(), Dominio.model().SEGREGATION_CODE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_STATO,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getNdpStato(), Dominio.model().NDP_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_OPERAZIONE,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getNdpOperazione(), Dominio.model().NDP_OPERAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_DESCRIZIONE,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getNdpDescrizione(), Dominio.model().NDP_DESCRIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().NDP_DATA,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getNdpData(), Dominio.model().NDP_DATA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().LOGO,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getLogo(), Dominio.model().LOGO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().CBILL,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getCbill(), Dominio.model().CBILL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getDominioFieldConverter().toColumn(Dominio.model().AUT_STAMPA_POSTE,false), "?");
		lstObjects_dominio.add(new JDBCObject(dominio.getAutStampaPoste(), Dominio.model().AUT_STAMPA_POSTE.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_stazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione_default","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_dominio.add(new JDBCObject(id_dominio_stazione, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_dominio.add(new JDBCObject(id_dominio_applicazione, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_dominio.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_dominio) {
			// Update dominio
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_dominio.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getDominioFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getDominioFieldConverter().toTable(Dominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getDominioFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio oldId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, dominio,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, dominio,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Dominio dominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, dominio,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, dominio,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Dominio dominio) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (dominio.getId()!=null) && (dominio.getId()>0) ){
			longId = dominio.getId();
		}
		else{
			IdDominio idDominio = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,dominio);
			longId = this.findIdDominio(jdbcProperties,log,connection,sqlQueryObject,idDominio,false);
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
		

		// Object dominio
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getDominioFieldConverter().toTable(Dominio.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete dominio
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdDominio idDominio) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdDominio(jdbcProperties, log, connection, sqlQueryObject, idDominio, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getDominioFieldConverter()));

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
