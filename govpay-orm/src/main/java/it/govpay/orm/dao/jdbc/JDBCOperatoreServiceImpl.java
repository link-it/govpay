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

import it.govpay.orm.Operatore;
import it.govpay.orm.OperatoreApplicazione;
import it.govpay.orm.OperatoreEnte;
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
		for (int i = 0; i < operatore.getOperatoreEnteList().size(); i++) {

			// Object _operatoreEnte_ente
			Long id_operatoreEnte_ente = null;
			it.govpay.orm.IdEnte idLogic_operatoreEnte_ente = null;
			idLogic_operatoreEnte_ente = operatore.getOperatoreEnteList().get(i).getIdEnte();
			if(idLogic_operatoreEnte_ente!=null){
				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
					id_operatoreEnte_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findTableId(idLogic_operatoreEnte_ente, false);
				}
				else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
					id_operatoreEnte_ente = idLogic_operatoreEnte_ente.getId();
					if(id_operatoreEnte_ente==null || id_operatoreEnte_ente<=0){
						throw new Exception("Logic id not contains table id");
					}
				}
			}


			// Object operatore.getOperatoreEnteList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_operatoreEnte = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_operatoreEnte.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_ENTE));
			sqlQueryObjectInsert_operatoreEnte.addInsertField("id_ente","?");
			sqlQueryObjectInsert_operatoreEnte.addInsertField("id_operatore","?");

			// Insert operatore.getOperatoreEnteList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatoreEnte = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_ENTE);
			long id_operatoreEnte = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatoreEnte, keyGenerator_operatoreEnte, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatoreEnte_ente,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			operatore.getOperatoreEnteList().get(i).setId(id_operatoreEnte);
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
		// for operatore_operatoreEnte

		java.util.List<Long> ids_operatore_operatoreEnte_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object operatore_operatoreEnte_object : operatore.getOperatoreEnteList()) {
			OperatoreEnte operatore_operatoreEnte = (OperatoreEnte) operatore_operatoreEnte_object;
			if(operatore_operatoreEnte.getId() == null || operatore_operatoreEnte.getId().longValue() <= 0) {

				long id = operatore.getId();			
				// Object _operatore_operatoreEnte_ente
				Long id_operatore_operatoreEnte_ente = null;
				it.govpay.orm.IdEnte idLogic_operatore_operatoreEnte_ente = null;
				idLogic_operatore_operatoreEnte_ente = operatore_operatoreEnte.getIdEnte();
				if(idLogic_operatore_operatoreEnte_ente!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatoreEnte_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findTableId(idLogic_operatore_operatoreEnte_ente, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreEnte_ente = idLogic_operatore_operatoreEnte_ente.getId();
						if(id_operatore_operatoreEnte_ente==null || id_operatore_operatoreEnte_ente<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatoreEnte
				ISQLQueryObject sqlQueryObjectInsert_operatore_operatoreEnte = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_operatore_operatoreEnte.addInsertTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_ENTE));
				sqlQueryObjectInsert_operatore_operatoreEnte.addInsertField("id_ente","?");
				sqlQueryObjectInsert_operatore_operatoreEnte.addInsertField("id_operatore","?");

				// Insert operatore_operatoreEnte
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_operatore_operatoreEnte = this.getOperatoreFetch().getKeyGeneratorObject(Operatore.model().OPERATORE_ENTE);
				long id_operatore_operatoreEnte = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_operatore_operatoreEnte, keyGenerator_operatore_operatoreEnte, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_operatore_operatoreEnte_ente,Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				operatore_operatoreEnte.setId(id_operatore_operatoreEnte);

				ids_operatore_operatoreEnte_da_non_eliminare.add(operatore_operatoreEnte.getId());
			} else {

				// Object _operatore_operatoreEnte_ente
				Long id_operatore_operatoreEnte_ente = null;
				it.govpay.orm.IdEnte idLogic_operatore_operatoreEnte_ente = null;
				idLogic_operatore_operatoreEnte_ente = operatore_operatoreEnte.getIdEnte();
				if(idLogic_operatore_operatoreEnte_ente!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_operatore_operatoreEnte_ente = ((JDBCEnteServiceSearch)(this.getServiceManager().getEnteServiceSearch())).findTableId(idLogic_operatore_operatoreEnte_ente, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_operatore_operatoreEnte_ente = idLogic_operatore_operatoreEnte_ente.getId();
						if(id_operatore_operatoreEnte_ente==null || id_operatore_operatoreEnte_ente<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object operatore_operatoreEnte
				ISQLQueryObject sqlQueryObjectUpdate_operatore_operatoreEnte = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_operatore_operatoreEnte.setANDLogicOperator(true);
				sqlQueryObjectUpdate_operatore_operatoreEnte.addUpdateTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_ENTE));
				boolean isUpdate_operatore_operatoreEnte = true;
				java.util.List<JDBCObject> lstObjects_operatore_operatoreEnte = new java.util.ArrayList<JDBCObject>();
				if(setIdMappingResolutionBehaviour){
					sqlQueryObjectUpdate_operatore_operatoreEnte.addUpdateField("id_ente","?");
				}
				if(setIdMappingResolutionBehaviour){
					lstObjects_operatore_operatoreEnte.add(new JDBCObject(id_operatore_operatoreEnte_ente, Long.class));
				}
				sqlQueryObjectUpdate_operatore_operatoreEnte.addWhereCondition("id=?");
				ids_operatore_operatoreEnte_da_non_eliminare.add(operatore_operatoreEnte.getId());
				lstObjects_operatore_operatoreEnte.add(new JDBCObject(new Long(operatore_operatoreEnte.getId()),Long.class));

				if(isUpdate_operatore_operatoreEnte) {
					// Update operatore_operatoreEnte
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_operatore_operatoreEnte.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_operatore_operatoreEnte.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for operatore_operatoreEnte

		// elimino tutte le occorrenze di operatore_operatoreEnte non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_operatoreEnte_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_operatoreEnte_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_operatoreEnte_deleteList.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_ENTE));
		java.util.List<JDBCObject> jdbcObjects_operatore_operatoreEnte_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_operatoreEnte_deleteList.addWhereCondition("id_operatore=?");
		jdbcObjects_operatore_operatoreEnte_delete.add(new JDBCObject(operatore.getId(), Long.class));

		StringBuffer marks_operatore_operatoreEnte = new StringBuffer();
		if(ids_operatore_operatoreEnte_da_non_eliminare.size() > 0) {
			for(Long ids : ids_operatore_operatoreEnte_da_non_eliminare) {
				if(marks_operatore_operatoreEnte.length() > 0) {
					marks_operatore_operatoreEnte.append(",");
				}
				marks_operatore_operatoreEnte.append("?");
				jdbcObjects_operatore_operatoreEnte_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_operatoreEnte_deleteList.addWhereCondition("id NOT IN ("+marks_operatore_operatoreEnte.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_operatoreEnte_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_operatore_operatoreEnte_delete.toArray(new JDBCObject[]{}));

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
		

		//Recupero oggetto _operatore_operatoreEnte
		ISQLQueryObject sqlQueryObjectDelete_operatore_operatoreEnte_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_operatore_operatoreEnte_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_operatore_operatoreEnte_getToDelete.addFromTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_ENTE));
		sqlQueryObjectDelete_operatore_operatoreEnte_getToDelete.addWhereCondition("id_operatore=?");
		java.util.List<Object> operatore_operatoreEnte_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_operatore_operatoreEnte_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Operatore.model().OPERATORE_ENTE, this.getOperatoreFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for operatore_operatoreEnte
		for (Object operatore_operatoreEnte_object : operatore_operatoreEnte_toDelete_list) {
			OperatoreEnte operatore_operatoreEnte = (OperatoreEnte) operatore_operatoreEnte_object;

			// Object operatore_operatoreEnte
			ISQLQueryObject sqlQueryObjectDelete_operatore_operatoreEnte = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_operatore_operatoreEnte.setANDLogicOperator(true);
			sqlQueryObjectDelete_operatore_operatoreEnte.addDeleteTable(this.getOperatoreFieldConverter().toTable(Operatore.model().OPERATORE_ENTE));
			sqlQueryObjectDelete_operatore_operatoreEnte.addWhereCondition("id=?");

			// Delete operatore_operatoreEnte
			jdbcUtilities.execute(sqlQueryObjectDelete_operatore_operatoreEnte.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(operatore_operatoreEnte.getId()),Long.class));
		} // fine for operatore_operatoreEnte
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
