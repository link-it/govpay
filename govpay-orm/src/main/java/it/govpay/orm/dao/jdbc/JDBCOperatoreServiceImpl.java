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

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdOperatore;
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

import it.govpay.orm.OperatoreUo;
import it.govpay.orm.Operatore;
import it.govpay.orm.OperatoreApplicazione;
import it.govpay.orm.OperatorePortale;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCOperatoreServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCOperatoreServiceImpl extends JDBCOperatoreServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Operatore, IdOperatore, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operatore operatore, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object operatore
		sqlQueryObjectInsert.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model()));
		sqlQueryObjectInsert.addInsertField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PRINCIPAL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperatoreFieldConverter().toColumn(Operatore.model().NOME,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PROFILO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getOperatoreFieldConverter().toColumn(Operatore.model().ABILITATO,false),"?");

		// Insert operatore
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operatore.getPrincipal(),Operatore.model().PRINCIPAL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operatore.getNome(),Operatore.model().NOME.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operatore.getProfilo(),Operatore.model().PROFILO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(operatore.getAbilitato(),Operatore.model().ABILITATO.getFieldType())
		);
		operatore.setId(id);

		// for operatore
		for (int i = 0; i < operatore.getOperatoreApplicazioneList().size(); i++) {

			// Object _operatoreApplicazione_applicazione
			Long id_operatoreApplicazione_applicazione = null;
			it.govpay.orm.IdApplicazione idLogic_operatoreApplicazione_applicazione = null;
			idLogic_operatoreApplicazione_applicazione = operatore.getOperatoreApplicazioneList().get(i).getIdApplicazione();
			if(idLogic_operatoreApplicazione_applicazione!=null){
				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
					id_operatoreApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_operatoreApplicazione_applicazione, false);
				}
				else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
					id_operatoreApplicazione_applicazione = idLogic_operatoreApplicazione_applicazione.getId();
					if(id_operatoreApplicazione_applicazione==null || id_operatoreApplicazione_applicazione<=0){
						throw new Exception("Logic id not contains table id");
					}
				}
			}


			// Object operatore.getOperatoreApplicazioneList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_operatoreApplicazione = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_operatoreApplicazione.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
			sqlQueryObjectInsert_operatoreApplicazione.addInsertField("id_applicazione","?");
			sqlQueryObjectInsert_operatoreApplicazione.addInsertField("id_operatore","?");

			// Insert operatore.getOperatoreApplicazioneList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatoreApplicazione = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_APPLICAZIONE);
			long id_operatoreApplicazione = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatoreApplicazione, keyGenerator_operatoreApplicazione, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatoreApplicazione_applicazione,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			operatore.getOperatoreApplicazioneList().get(i).setId(id_operatoreApplicazione);
		} // fine for 

		// for operatore
		for (int i = 0; i < operatore.getOperatorePortaleList().size(); i++) {

			// Object _operatorePortale_portale
			Long id_operatorePortale_portale = null;
			it.govpay.orm.IdPortale idLogic_operatorePortale_portale = null;
			idLogic_operatorePortale_portale = operatore.getOperatorePortaleList().get(i).getIdPortale();
			if(idLogic_operatorePortale_portale!=null){
				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
					id_operatorePortale_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findTableId(idLogic_operatorePortale_portale, false);
				}
				else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
					id_operatorePortale_portale = idLogic_operatorePortale_portale.getId();
					if(id_operatorePortale_portale==null || id_operatorePortale_portale<=0){
						throw new Exception("Logic id not contains table id");
					}
				}
			}


			// Object operatore.getOperatorePortaleList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_operatorePortale = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_operatorePortale.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
			sqlQueryObjectInsert_operatorePortale.addInsertField("id_portale","?");
			sqlQueryObjectInsert_operatorePortale.addInsertField("id_operatore","?");

			// Insert operatore.getOperatorePortaleList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatorePortale = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_PORTALE);
			long id_operatorePortale = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatorePortale, keyGenerator_operatorePortale, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatorePortale_portale,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			operatore.getOperatorePortaleList().get(i).setId(id_operatorePortale);
		} // fine for 

		// for operatore
		for (int i = 0; i < operatore.getOperatoreUoList().size(); i++) {

			// Object _operatoreUo_uo
			Long id_operatoreUo_uo = null;
			it.govpay.orm.IdUo idLogic_operatoreUo_uo = null;
			idLogic_operatoreUo_uo = operatore.getOperatoreUoList().get(i).getIdUo();
			if(idLogic_operatoreUo_uo!=null){
				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
					id_operatoreUo_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findTableId(idLogic_operatoreUo_uo, false);
				}
				else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
					id_operatoreUo_uo = idLogic_operatoreUo_uo.getId();
					if(id_operatoreUo_uo==null || id_operatoreUo_uo<=0){
						throw new Exception("Logic id not contains table id");
					}
				}
			}


			// Object operatore.getOperatoreUoList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_operatoreUo = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_operatoreUo.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
			sqlQueryObjectInsert_operatoreUo.addInsertField("id_uo","?");
			sqlQueryObjectInsert_operatoreUo.addInsertField("id_operatore","?");

			// Insert operatore.getOperatoreUoList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatoreUo = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_UO);
			long id_operatoreUo = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatoreUo, keyGenerator_operatoreUo, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatoreUo_uo,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			operatore.getOperatoreUoList().get(i).setId(id_operatoreUo);
		} // fine for 

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore oldId, Operatore operatore, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdOperatore(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = operatore.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: operatore.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			operatore.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, operatore, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Operatore operatore, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			


		// Object operatore
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getOperatoreFieldConverter().toTable(Operatore.model()));
		boolean isUpdate_operatore = true;
		java.util.List<JDBCObject> lstObjects_operatore = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PRINCIPAL,false), "?");
		lstObjects_operatore.add(new JDBCObject(operatore.getPrincipal(), Operatore.model().PRINCIPAL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperatoreFieldConverter().toColumn(Operatore.model().NOME,false), "?");
		lstObjects_operatore.add(new JDBCObject(operatore.getNome(), Operatore.model().NOME.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperatoreFieldConverter().toColumn(Operatore.model().PROFILO,false), "?");
		lstObjects_operatore.add(new JDBCObject(operatore.getProfilo(), Operatore.model().PROFILO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getOperatoreFieldConverter().toColumn(Operatore.model().ABILITATO,false), "?");
		lstObjects_operatore.add(new JDBCObject(operatore.getAbilitato(), Operatore.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_operatore.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_operatore) {
			// Update operatore
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_operatore.toArray(new JDBCObject[]{}));
		}
		// for operatore_operatoreUo

		java.util.List<Long> ids_operatore_operatoreUo_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object operatore_operatoreUo_object : operatore.getOperatoreUoList()) {
			OperatoreUo operatore_operatoreUo = (OperatoreUo) operatore_operatoreUo_object;
			if(operatore_operatoreUo.getId() == null || operatore_operatoreUo.getId().longValue() <= 0) {

				long id = operatore.getId();			
				// Object _operatore_operatoreUo_uo
				Long id_operatore_operatoreUo_uo = null;
				it.govpay.orm.IdUo idLogic_operatore_operatoreUo_uo = null;
				idLogic_operatore_operatoreUo_uo = operatore_operatoreUo.getIdUo();
				if(idLogic_operatore_operatoreUo_uo!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatoreUo_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findTableId(idLogic_operatore_operatoreUo_uo, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreUo_uo = idLogic_operatore_operatoreUo_uo.getId();
						if(id_operatore_operatoreUo_uo==null || id_operatore_operatoreUo_uo<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatoreUo
				ISQLQueryObject sqlQueryObjectInsert_operatore_operatoreUo = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_operatore_operatoreUo.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
				sqlQueryObjectInsert_operatore_operatoreUo.addInsertField("id_uo","?");
				sqlQueryObjectInsert_operatore_operatoreUo.addInsertField("id_operatore","?");

				// Insert operatore_operatoreUo
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatore_operatoreUo = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_UO);
				long id_operatore_operatoreUo = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatore_operatoreUo, keyGenerator_operatore_operatoreUo, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore_operatoreUo_uo,Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				operatore_operatoreUo.setId(id_operatore_operatoreUo);

				ids_operatore_operatoreUo_da_non_eliminare.add(operatore_operatoreUo.getId());
			} else {

				// Object _operatore_operatoreUo_uo
				Long id_operatore_operatoreUo_uo = null;
				it.govpay.orm.IdUo idLogic_operatore_operatoreUo_uo = null;
				idLogic_operatore_operatoreUo_uo = operatore_operatoreUo.getIdUo();
				if(idLogic_operatore_operatoreUo_uo!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatoreUo_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findTableId(idLogic_operatore_operatoreUo_uo, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreUo_uo = idLogic_operatore_operatoreUo_uo.getId();
						if(id_operatore_operatoreUo_uo==null || id_operatore_operatoreUo_uo<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatoreUo
				ISQLQueryObject sqlQueryObjectUpdate_operatore_operatoreUo = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_operatore_operatoreUo.setANDLogicOperator(true);
				sqlQueryObjectUpdate_operatore_operatoreUo.addUpdateTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
				boolean isUpdate_operatore_operatoreUo = true;
				java.util.List<JDBCObject> lstObjects_operatore_operatoreUo = new java.util.ArrayList<JDBCObject>();
				if(setIdMappingResolutionBehaviour){
					sqlQueryObjectUpdate_operatore_operatoreUo.addUpdateField("id_uo","?");
				}
				if(setIdMappingResolutionBehaviour){
					lstObjects_operatore_operatoreUo.add(new JDBCObject(id_operatore_operatoreUo_uo, Long.class));
				}
				sqlQueryObjectUpdate_operatore_operatoreUo.addWhereCondition("id=?");
				ids_operatore_operatoreUo_da_non_eliminare.add(operatore_operatoreUo.getId());
				lstObjects_operatore_operatoreUo.add(new JDBCObject(new Long(operatore_operatoreUo.getId()),Long.class));

				if(isUpdate_operatore_operatoreUo) {
					// Update operatore_operatoreUo
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_operatore_operatoreUo.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_operatore_operatoreUo.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for operatore_operatoreUo

		// elimino tutte le occorrenze di operatore_operatoreUo non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_operatoreUo_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_operatoreUo_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_operatoreUo_deleteList.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
		java.util.List<JDBCObject> jdbcObjects_operatore_operatoreUo_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_operatoreUo_deleteList.addWhereCondition("id_operatore=?");
		jdbcObjects_operatore_operatoreUo_delete.add(new JDBCObject(operatore.getId(), Long.class));

		StringBuffer marks_operatore_operatoreUo = new StringBuffer();
		if(ids_operatore_operatoreUo_da_non_eliminare.size() > 0) {
			for(Long ids : ids_operatore_operatoreUo_da_non_eliminare) {
				if(marks_operatore_operatoreUo.length() > 0) {
					marks_operatore_operatoreUo.append(",");
				}
				marks_operatore_operatoreUo.append("?");
				jdbcObjects_operatore_operatoreUo_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_operatoreUo_deleteList.addWhereCondition("id NOT IN ("+marks_operatore_operatoreUo.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_operatoreUo_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_operatore_operatoreUo_delete.toArray(new JDBCObject[]{}));

		// for operatore_operatoreApplicazione

		java.util.List<Long> ids_operatore_operatoreApplicazione_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object operatore_operatoreApplicazione_object : operatore.getOperatoreApplicazioneList()) {
			OperatoreApplicazione operatore_operatoreApplicazione = (OperatoreApplicazione) operatore_operatoreApplicazione_object;
			if(operatore_operatoreApplicazione.getId() == null || operatore_operatoreApplicazione.getId().longValue() <= 0) {

				long id = operatore.getId();			
				// Object _operatore_operatoreApplicazione_applicazione
				Long id_operatore_operatoreApplicazione_applicazione = null;
				it.govpay.orm.IdApplicazione idLogic_operatore_operatoreApplicazione_applicazione = null;
				idLogic_operatore_operatoreApplicazione_applicazione = operatore_operatoreApplicazione.getIdApplicazione();
				if(idLogic_operatore_operatoreApplicazione_applicazione!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatoreApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_operatore_operatoreApplicazione_applicazione, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreApplicazione_applicazione = idLogic_operatore_operatoreApplicazione_applicazione.getId();
						if(id_operatore_operatoreApplicazione_applicazione==null || id_operatore_operatoreApplicazione_applicazione<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatoreApplicazione
				ISQLQueryObject sqlQueryObjectInsert_operatore_operatoreApplicazione = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_operatore_operatoreApplicazione.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
				sqlQueryObjectInsert_operatore_operatoreApplicazione.addInsertField("id_applicazione","?");
				sqlQueryObjectInsert_operatore_operatoreApplicazione.addInsertField("id_operatore","?");

				// Insert operatore_operatoreApplicazione
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatore_operatoreApplicazione = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_APPLICAZIONE);
				long id_operatore_operatoreApplicazione = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatore_operatoreApplicazione, keyGenerator_operatore_operatoreApplicazione, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore_operatoreApplicazione_applicazione,Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				operatore_operatoreApplicazione.setId(id_operatore_operatoreApplicazione);

				ids_operatore_operatoreApplicazione_da_non_eliminare.add(operatore_operatoreApplicazione.getId());
			} else {

				// Object _operatore_operatoreApplicazione_applicazione
				Long id_operatore_operatoreApplicazione_applicazione = null;
				it.govpay.orm.IdApplicazione idLogic_operatore_operatoreApplicazione_applicazione = null;
				idLogic_operatore_operatoreApplicazione_applicazione = operatore_operatoreApplicazione.getIdApplicazione();
				if(idLogic_operatore_operatoreApplicazione_applicazione!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatoreApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_operatore_operatoreApplicazione_applicazione, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreApplicazione_applicazione = idLogic_operatore_operatoreApplicazione_applicazione.getId();
						if(id_operatore_operatoreApplicazione_applicazione==null || id_operatore_operatoreApplicazione_applicazione<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatoreApplicazione
				ISQLQueryObject sqlQueryObjectUpdate_operatore_operatoreApplicazione = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_operatore_operatoreApplicazione.setANDLogicOperator(true);
				sqlQueryObjectUpdate_operatore_operatoreApplicazione.addUpdateTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
				boolean isUpdate_operatore_operatoreApplicazione = true;
				java.util.List<JDBCObject> lstObjects_operatore_operatoreApplicazione = new java.util.ArrayList<JDBCObject>();
				if(setIdMappingResolutionBehaviour){
					sqlQueryObjectUpdate_operatore_operatoreApplicazione.addUpdateField("id_applicazione","?");
				}
				if(setIdMappingResolutionBehaviour){
					lstObjects_operatore_operatoreApplicazione.add(new JDBCObject(id_operatore_operatoreApplicazione_applicazione, Long.class));
				}
				sqlQueryObjectUpdate_operatore_operatoreApplicazione.addWhereCondition("id=?");
				ids_operatore_operatoreApplicazione_da_non_eliminare.add(operatore_operatoreApplicazione.getId());
				lstObjects_operatore_operatoreApplicazione.add(new JDBCObject(new Long(operatore_operatoreApplicazione.getId()),Long.class));

				if(isUpdate_operatore_operatoreApplicazione) {
					// Update operatore_operatoreApplicazione
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_operatore_operatoreApplicazione.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_operatore_operatoreApplicazione.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for operatore_operatoreApplicazione

		// elimino tutte le occorrenze di operatore_operatoreApplicazione non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_operatoreApplicazione_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_operatoreApplicazione_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_operatoreApplicazione_deleteList.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
		java.util.List<JDBCObject> jdbcObjects_operatore_operatoreApplicazione_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_operatoreApplicazione_deleteList.addWhereCondition("id_operatore=?");
		jdbcObjects_operatore_operatoreApplicazione_delete.add(new JDBCObject(operatore.getId(), Long.class));

		StringBuffer marks_operatore_operatoreApplicazione = new StringBuffer();
		if(ids_operatore_operatoreApplicazione_da_non_eliminare.size() > 0) {
			for(Long ids : ids_operatore_operatoreApplicazione_da_non_eliminare) {
				if(marks_operatore_operatoreApplicazione.length() > 0) {
					marks_operatore_operatoreApplicazione.append(",");
				}
				marks_operatore_operatoreApplicazione.append("?");
				jdbcObjects_operatore_operatoreApplicazione_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_operatoreApplicazione_deleteList.addWhereCondition("id NOT IN ("+marks_operatore_operatoreApplicazione.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_operatoreApplicazione_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_operatore_operatoreApplicazione_delete.toArray(new JDBCObject[]{}));

		// for operatore_operatorePortale

		java.util.List<Long> ids_operatore_operatorePortale_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object operatore_operatorePortale_object : operatore.getOperatorePortaleList()) {
			OperatorePortale operatore_operatorePortale = (OperatorePortale) operatore_operatorePortale_object;
			if(operatore_operatorePortale.getId() == null || operatore_operatorePortale.getId().longValue() <= 0) {

				long id = operatore.getId();			
				// Object _operatore_operatorePortale_portale
				Long id_operatore_operatorePortale_portale = null;
				it.govpay.orm.IdPortale idLogic_operatore_operatorePortale_portale = null;
				idLogic_operatore_operatorePortale_portale = operatore_operatorePortale.getIdPortale();
				if(idLogic_operatore_operatorePortale_portale!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatorePortale_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findTableId(idLogic_operatore_operatorePortale_portale, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatorePortale_portale = idLogic_operatore_operatorePortale_portale.getId();
						if(id_operatore_operatorePortale_portale==null || id_operatore_operatorePortale_portale<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatorePortale
				ISQLQueryObject sqlQueryObjectInsert_operatore_operatorePortale = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_operatore_operatorePortale.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
				sqlQueryObjectInsert_operatore_operatorePortale.addInsertField("id_portale","?");
				sqlQueryObjectInsert_operatore_operatorePortale.addInsertField("id_operatore","?");

				// Insert operatore_operatorePortale
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatore_operatorePortale = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_PORTALE);
				long id_operatore_operatorePortale = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatore_operatorePortale, keyGenerator_operatore_operatorePortale, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore_operatorePortale_portale,Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				operatore_operatorePortale.setId(id_operatore_operatorePortale);

				ids_operatore_operatorePortale_da_non_eliminare.add(operatore_operatorePortale.getId());
			} else {

				// Object _operatore_operatorePortale_portale
				Long id_operatore_operatorePortale_portale = null;
				it.govpay.orm.IdPortale idLogic_operatore_operatorePortale_portale = null;
				idLogic_operatore_operatorePortale_portale = operatore_operatorePortale.getIdPortale();
				if(idLogic_operatore_operatorePortale_portale!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatorePortale_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findTableId(idLogic_operatore_operatorePortale_portale, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatorePortale_portale = idLogic_operatore_operatorePortale_portale.getId();
						if(id_operatore_operatorePortale_portale==null || id_operatore_operatorePortale_portale<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatorePortale
				ISQLQueryObject sqlQueryObjectUpdate_operatore_operatorePortale = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_operatore_operatorePortale.setANDLogicOperator(true);
				sqlQueryObjectUpdate_operatore_operatorePortale.addUpdateTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
				boolean isUpdate_operatore_operatorePortale = true;
				java.util.List<JDBCObject> lstObjects_operatore_operatorePortale = new java.util.ArrayList<JDBCObject>();
				if(setIdMappingResolutionBehaviour){
					sqlQueryObjectUpdate_operatore_operatorePortale.addUpdateField("id_portale","?");
				}
				if(setIdMappingResolutionBehaviour){
					lstObjects_operatore_operatorePortale.add(new JDBCObject(id_operatore_operatorePortale_portale, Long.class));
				}
				sqlQueryObjectUpdate_operatore_operatorePortale.addWhereCondition("id=?");
				ids_operatore_operatorePortale_da_non_eliminare.add(operatore_operatorePortale.getId());
				lstObjects_operatore_operatorePortale.add(new JDBCObject(new Long(operatore_operatorePortale.getId()),Long.class));

				if(isUpdate_operatore_operatorePortale) {
					// Update operatore_operatorePortale
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_operatore_operatorePortale.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_operatore_operatorePortale.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for operatore_operatorePortale

		// elimino tutte le occorrenze di operatore_operatorePortale non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_operatorePortale_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_operatorePortale_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_operatorePortale_deleteList.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
		java.util.List<JDBCObject> jdbcObjects_operatore_operatorePortale_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_operatorePortale_deleteList.addWhereCondition("id_operatore=?");
		jdbcObjects_operatore_operatorePortale_delete.add(new JDBCObject(operatore.getId(), Long.class));

		StringBuffer marks_operatore_operatorePortale = new StringBuffer();
		if(ids_operatore_operatorePortale_da_non_eliminare.size() > 0) {
			for(Long ids : ids_operatore_operatorePortale_da_non_eliminare) {
				if(marks_operatore_operatorePortale.length() > 0) {
					marks_operatore_operatorePortale.append(",");
				}
				marks_operatore_operatorePortale.append("?");
				jdbcObjects_operatore_operatorePortale_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_operatorePortale_deleteList.addWhereCondition("id NOT IN ("+marks_operatore_operatorePortale.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_operatorePortale_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_operatore_operatorePortale_delete.toArray(new JDBCObject[]{}));


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperatoreFieldConverter().toTable(Operatore.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getOperatoreFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperatoreFieldConverter().toTable(Operatore.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getOperatoreFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperatoreFieldConverter().toTable(Operatore.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getOperatoreFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperatoreFieldConverter().toTable(Operatore.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getOperatoreFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperatoreFieldConverter().toTable(Operatore.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getOperatoreFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getOperatoreFieldConverter().toTable(Operatore.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getOperatoreFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore oldId, Operatore operatore, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, operatore,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, operatore,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Operatore operatore, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, operatore,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, operatore,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Operatore operatore) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (operatore.getId()!=null) && (operatore.getId()>0) ){
			longId = operatore.getId();
		}
		else{
			IdOperatore idOperatore = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,operatore);
			longId = this.findIdOperatore(jdbcProperties,log,connection,sqlQueryObject,idOperatore,false);
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
		

		//Recupero oggetto _operatore_operatoreUo
		ISQLQueryObject sqlQueryObjectDelete_operatore_operatoreUo_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_operatore_operatoreUo_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_operatore_operatoreUo_getToDelete.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
		sqlQueryObjectDelete_operatore_operatoreUo_getToDelete.addWhereCondition("id_operatore=?");
		java.util.List<Object> operatore_operatoreUo_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_operatore_operatoreUo_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_UO, this.getOperatoreFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for operatore_operatoreUo
		for (Object operatore_operatoreUo_object : operatore_operatoreUo_toDelete_list) {
			OperatoreUo operatore_operatoreUo = (OperatoreUo) operatore_operatoreUo_object;

			// Object operatore_operatoreUo
			ISQLQueryObject sqlQueryObjectDelete_operatore_operatoreUo = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_operatore_operatoreUo.setANDLogicOperator(true);
			sqlQueryObjectDelete_operatore_operatoreUo.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_UO));
			sqlQueryObjectDelete_operatore_operatoreUo.addWhereCondition("id=?");

			// Delete operatore_operatoreUo
			jdbcUtilities.execute(sqlQueryObjectDelete_operatore_operatoreUo.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(operatore_operatoreUo.getId()),Long.class));
		} // fine for operatore_operatoreUo

		//Recupero oggetto _operatore_operatoreApplicazione
		ISQLQueryObject sqlQueryObjectDelete_operatore_operatoreApplicazione_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_operatore_operatoreApplicazione_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_operatore_operatoreApplicazione_getToDelete.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
		sqlQueryObjectDelete_operatore_operatoreApplicazione_getToDelete.addWhereCondition("id_operatore=?");
		java.util.List<Object> operatore_operatoreApplicazione_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_operatore_operatoreApplicazione_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_APPLICAZIONE, this.getOperatoreFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for operatore_operatoreApplicazione
		for (Object operatore_operatoreApplicazione_object : operatore_operatoreApplicazione_toDelete_list) {
			OperatoreApplicazione operatore_operatoreApplicazione = (OperatoreApplicazione) operatore_operatoreApplicazione_object;

			// Object operatore_operatoreApplicazione
			ISQLQueryObject sqlQueryObjectDelete_operatore_operatoreApplicazione = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_operatore_operatoreApplicazione.setANDLogicOperator(true);
			sqlQueryObjectDelete_operatore_operatoreApplicazione.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_APPLICAZIONE));
			sqlQueryObjectDelete_operatore_operatoreApplicazione.addWhereCondition("id=?");

			// Delete operatore_operatoreApplicazione
			jdbcUtilities.execute(sqlQueryObjectDelete_operatore_operatoreApplicazione.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(operatore_operatoreApplicazione.getId()),Long.class));
		} // fine for operatore_operatoreApplicazione

		//Recupero oggetto _operatore_operatorePortale
		ISQLQueryObject sqlQueryObjectDelete_operatore_operatorePortale_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_operatore_operatorePortale_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_operatore_operatorePortale_getToDelete.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
		sqlQueryObjectDelete_operatore_operatorePortale_getToDelete.addWhereCondition("id_operatore=?");
		java.util.List<Object> operatore_operatorePortale_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_operatore_operatorePortale_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_PORTALE, this.getOperatoreFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for operatore_operatorePortale
		for (Object operatore_operatorePortale_object : operatore_operatorePortale_toDelete_list) {
			OperatorePortale operatore_operatorePortale = (OperatorePortale) operatore_operatorePortale_object;

			// Object operatore_operatorePortale
			ISQLQueryObject sqlQueryObjectDelete_operatore_operatorePortale = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_operatore_operatorePortale.setANDLogicOperator(true);
			sqlQueryObjectDelete_operatore_operatorePortale.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_PORTALE));
			sqlQueryObjectDelete_operatore_operatorePortale.addWhereCondition("id=?");

			// Delete operatore_operatorePortale
			jdbcUtilities.execute(sqlQueryObjectDelete_operatore_operatorePortale.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(operatore_operatorePortale.getId()),Long.class));
		} // fine for operatore_operatorePortale

		// Object operatore
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete operatore
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdOperatore idOperatore) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdOperatore(jdbcProperties, log, connection, sqlQueryObject, idOperatore, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getOperatoreFieldConverter()));

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
