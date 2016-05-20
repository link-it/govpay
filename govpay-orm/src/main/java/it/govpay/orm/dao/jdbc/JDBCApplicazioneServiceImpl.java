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
import it.govpay.orm.IdApplicazione;
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

import it.govpay.orm.ApplicazioneDominio;
import it.govpay.orm.ApplicazioneTributo;
import it.govpay.orm.Applicazione;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCApplicazioneServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCApplicazioneServiceImpl extends JDBCApplicazioneServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Applicazione, IdApplicazione, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Applicazione applicazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object applicazione
		sqlQueryObjectInsert.addInsertTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model()));
		sqlQueryObjectInsert.addInsertField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().COD_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().PRINCIPAL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().FIRMA_RICEVUTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().COD_CONNETTORE_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().COD_CONNETTORE_VERIFICA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().TRUSTED,false),"?");

		// Insert applicazione
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getApplicazioneFetch().getKeyGeneratorObject(Applicazione.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(applicazione.getCodApplicazione(),Applicazione.model().COD_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(applicazione.getAbilitato(),Applicazione.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(applicazione.getPrincipal(),Applicazione.model().PRINCIPAL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(applicazione.getFirmaRicevuta(),Applicazione.model().FIRMA_RICEVUTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(applicazione.getCodConnettoreEsito(),Applicazione.model().COD_CONNETTORE_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(applicazione.getCodConnettoreVerifica(),Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(applicazione.getTrusted(),Applicazione.model().TRUSTED.getFieldType())
		);
		applicazione.setId(id);

		// for applicazione
		for (int i = 0; i < applicazione.getApplicazioneDominioList().size(); i++) {

			// Object _applicazioneDominio_dominio
			Long id_applicazioneDominio_dominio = null;
			it.govpay.orm.IdDominio idLogic_applicazioneDominio_dominio = null;
			idLogic_applicazioneDominio_dominio = applicazione.getApplicazioneDominioList().get(i).getIdDominio();
			if(idLogic_applicazioneDominio_dominio!=null){
				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
					id_applicazioneDominio_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_applicazioneDominio_dominio, false);
				}
				else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
					id_applicazioneDominio_dominio = idLogic_applicazioneDominio_dominio.getId();
					if(id_applicazioneDominio_dominio==null || id_applicazioneDominio_dominio<=0){
						throw new Exception("Logic id not contains table id");
					}
				}
			}


			// Object applicazione.getApplicazioneDominioList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_applicazioneDominio = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_applicazioneDominio.addInsertTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_DOMINIO));
			sqlQueryObjectInsert_applicazioneDominio.addInsertField("id_dominio","?");
			sqlQueryObjectInsert_applicazioneDominio.addInsertField("id_applicazione","?");

			// Insert applicazione.getApplicazioneDominioList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_applicazioneDominio = this.getApplicazioneFetch().getKeyGeneratorObject(Applicazione.model().APPLICAZIONE_DOMINIO);
			long id_applicazioneDominio = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_applicazioneDominio, keyGenerator_applicazioneDominio, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazioneDominio_dominio,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			applicazione.getApplicazioneDominioList().get(i).setId(id_applicazioneDominio);
		} // fine for 

		// for applicazione
		for (int i = 0; i < applicazione.getApplicazioneTributoList().size(); i++) {

			// Object _applicazioneTributo_tributo
			Long id_applicazioneTributo_tributo = null;
			it.govpay.orm.IdTributo idLogic_applicazioneTributo_tributo = null;
			idLogic_applicazioneTributo_tributo = applicazione.getApplicazioneTributoList().get(i).getIdTributo();
			if(idLogic_applicazioneTributo_tributo!=null){
				if(idMappingResolutionBehaviour==null ||
					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
					id_applicazioneTributo_tributo = ((JDBCTributoServiceSearch)(this.getServiceManager().getTributoServiceSearch())).findTableId(idLogic_applicazioneTributo_tributo, false);
				}
				else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
					id_applicazioneTributo_tributo = idLogic_applicazioneTributo_tributo.getId();
					if(id_applicazioneTributo_tributo==null || id_applicazioneTributo_tributo<=0){
						throw new Exception("Logic id not contains table id");
					}
				}
			}


			// Object applicazione.getApplicazioneTributoList().get(i)
			ISQLQueryObject sqlQueryObjectInsert_applicazioneTributo = sqlQueryObjectInsert.newSQLQueryObject();
			sqlQueryObjectInsert_applicazioneTributo.addInsertTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_TRIBUTO));
			sqlQueryObjectInsert_applicazioneTributo.addInsertField("id_tributo","?");
			sqlQueryObjectInsert_applicazioneTributo.addInsertField("id_applicazione","?");

			// Insert applicazione.getApplicazioneTributoList().get(i)
			org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_applicazioneTributo = this.getApplicazioneFetch().getKeyGeneratorObject(Applicazione.model().APPLICAZIONE_TRIBUTO);
			long id_applicazioneTributo = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_applicazioneTributo, keyGenerator_applicazioneTributo, jdbcProperties.isShowSql(),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazioneTributo_tributo,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
			);
			applicazione.getApplicazioneTributoList().get(i).setId(id_applicazioneTributo);
		} // fine for 

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdApplicazione oldId, Applicazione applicazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdApplicazione(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = applicazione.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: applicazione.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			applicazione.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, applicazione, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Applicazione applicazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			


		// Object applicazione
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model()));
		boolean isUpdate_applicazione = true;
		java.util.List<JDBCObject> lstObjects_applicazione = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().COD_APPLICAZIONE,false), "?");
		lstObjects_applicazione.add(new JDBCObject(applicazione.getCodApplicazione(), Applicazione.model().COD_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().ABILITATO,false), "?");
		lstObjects_applicazione.add(new JDBCObject(applicazione.getAbilitato(), Applicazione.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().PRINCIPAL,false), "?");
		lstObjects_applicazione.add(new JDBCObject(applicazione.getPrincipal(), Applicazione.model().PRINCIPAL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().FIRMA_RICEVUTA,false), "?");
		lstObjects_applicazione.add(new JDBCObject(applicazione.getFirmaRicevuta(), Applicazione.model().FIRMA_RICEVUTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().COD_CONNETTORE_ESITO,false), "?");
		lstObjects_applicazione.add(new JDBCObject(applicazione.getCodConnettoreEsito(), Applicazione.model().COD_CONNETTORE_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().COD_CONNETTORE_VERIFICA,false), "?");
		lstObjects_applicazione.add(new JDBCObject(applicazione.getCodConnettoreVerifica(), Applicazione.model().COD_CONNETTORE_VERIFICA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getApplicazioneFieldConverter().toColumn(Applicazione.model().TRUSTED,false), "?");
		lstObjects_applicazione.add(new JDBCObject(applicazione.getTrusted(), Applicazione.model().TRUSTED.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_applicazione.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_applicazione) {
			// Update applicazione
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_applicazione.toArray(new JDBCObject[]{}));
		}
		// for applicazione_applicazioneTributo

		java.util.List<Long> ids_applicazione_applicazioneTributo_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object applicazione_applicazioneTributo_object : applicazione.getApplicazioneTributoList()) {
			ApplicazioneTributo applicazione_applicazioneTributo = (ApplicazioneTributo) applicazione_applicazioneTributo_object;
			if(applicazione_applicazioneTributo.getId() == null || applicazione_applicazioneTributo.getId().longValue() <= 0) {

				long id = applicazione.getId();			
				// Object _applicazione_applicazioneTributo_tributo
				Long id_applicazione_applicazioneTributo_tributo = null;
				it.govpay.orm.IdTributo idLogic_applicazione_applicazioneTributo_tributo = null;
				idLogic_applicazione_applicazioneTributo_tributo = applicazione_applicazioneTributo.getIdTributo();
				if(idLogic_applicazione_applicazioneTributo_tributo!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_applicazione_applicazioneTributo_tributo = ((JDBCTributoServiceSearch)(this.getServiceManager().getTributoServiceSearch())).findTableId(idLogic_applicazione_applicazioneTributo_tributo, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_applicazione_applicazioneTributo_tributo = idLogic_applicazione_applicazioneTributo_tributo.getId();
						if(id_applicazione_applicazioneTributo_tributo==null || id_applicazione_applicazioneTributo_tributo<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object applicazione_applicazioneTributo
				ISQLQueryObject sqlQueryObjectInsert_applicazione_applicazioneTributo = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_applicazione_applicazioneTributo.addInsertTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_TRIBUTO));
				sqlQueryObjectInsert_applicazione_applicazioneTributo.addInsertField("id_tributo","?");
				sqlQueryObjectInsert_applicazione_applicazioneTributo.addInsertField("id_applicazione","?");

				// Insert applicazione_applicazioneTributo
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_applicazione_applicazioneTributo = this.getApplicazioneFetch().getKeyGeneratorObject(Applicazione.model().APPLICAZIONE_TRIBUTO);
				long id_applicazione_applicazioneTributo = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_applicazione_applicazioneTributo, keyGenerator_applicazione_applicazioneTributo, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione_applicazioneTributo_tributo,Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				applicazione_applicazioneTributo.setId(id_applicazione_applicazioneTributo);

				ids_applicazione_applicazioneTributo_da_non_eliminare.add(applicazione_applicazioneTributo.getId());
			} else {

				// Object _applicazione_applicazioneTributo_tributo
				Long id_applicazione_applicazioneTributo_tributo = null;
				it.govpay.orm.IdTributo idLogic_applicazione_applicazioneTributo_tributo = null;
				idLogic_applicazione_applicazioneTributo_tributo = applicazione_applicazioneTributo.getIdTributo();
				if(idLogic_applicazione_applicazioneTributo_tributo!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_applicazione_applicazioneTributo_tributo = ((JDBCTributoServiceSearch)(this.getServiceManager().getTributoServiceSearch())).findTableId(idLogic_applicazione_applicazioneTributo_tributo, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_applicazione_applicazioneTributo_tributo = idLogic_applicazione_applicazioneTributo_tributo.getId();
						if(id_applicazione_applicazioneTributo_tributo==null || id_applicazione_applicazioneTributo_tributo<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object applicazione_applicazioneTributo
				ISQLQueryObject sqlQueryObjectUpdate_applicazione_applicazioneTributo = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_applicazione_applicazioneTributo.setANDLogicOperator(true);
				sqlQueryObjectUpdate_applicazione_applicazioneTributo.addUpdateTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_TRIBUTO));
				boolean isUpdate_applicazione_applicazioneTributo = true;
				java.util.List<JDBCObject> lstObjects_applicazione_applicazioneTributo = new java.util.ArrayList<JDBCObject>();
				if(setIdMappingResolutionBehaviour){
					sqlQueryObjectUpdate_applicazione_applicazioneTributo.addUpdateField("id_tributo","?");
				}
				if(setIdMappingResolutionBehaviour){
					lstObjects_applicazione_applicazioneTributo.add(new JDBCObject(id_applicazione_applicazioneTributo_tributo, Long.class));
				}
				sqlQueryObjectUpdate_applicazione_applicazioneTributo.addWhereCondition("id=?");
				ids_applicazione_applicazioneTributo_da_non_eliminare.add(applicazione_applicazioneTributo.getId());
				lstObjects_applicazione_applicazioneTributo.add(new JDBCObject(new Long(applicazione_applicazioneTributo.getId()),Long.class));

				if(isUpdate_applicazione_applicazioneTributo) {
					// Update applicazione_applicazioneTributo
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_applicazione_applicazioneTributo.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_applicazione_applicazioneTributo.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for applicazione_applicazioneTributo

		// elimino tutte le occorrenze di applicazione_applicazioneTributo non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_applicazioneTributo_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_applicazioneTributo_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_applicazioneTributo_deleteList.addDeleteTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_TRIBUTO));
		java.util.List<JDBCObject> jdbcObjects_applicazione_applicazioneTributo_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_applicazioneTributo_deleteList.addWhereCondition("id_applicazione=?");
		jdbcObjects_applicazione_applicazioneTributo_delete.add(new JDBCObject(applicazione.getId(), Long.class));

		StringBuffer marks_applicazione_applicazioneTributo = new StringBuffer();
		if(ids_applicazione_applicazioneTributo_da_non_eliminare.size() > 0) {
			for(Long ids : ids_applicazione_applicazioneTributo_da_non_eliminare) {
				if(marks_applicazione_applicazioneTributo.length() > 0) {
					marks_applicazione_applicazioneTributo.append(",");
				}
				marks_applicazione_applicazioneTributo.append("?");
				jdbcObjects_applicazione_applicazioneTributo_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_applicazioneTributo_deleteList.addWhereCondition("id NOT IN ("+marks_applicazione_applicazioneTributo.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_applicazioneTributo_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_applicazione_applicazioneTributo_delete.toArray(new JDBCObject[]{}));

		// for applicazione_applicazioneDominio

		java.util.List<Long> ids_applicazione_applicazioneDominio_da_non_eliminare = new java.util.ArrayList<Long>();
		for (Object applicazione_applicazioneDominio_object : applicazione.getApplicazioneDominioList()) {
			ApplicazioneDominio applicazione_applicazioneDominio = (ApplicazioneDominio) applicazione_applicazioneDominio_object;
			if(applicazione_applicazioneDominio.getId() == null || applicazione_applicazioneDominio.getId().longValue() <= 0) {

				long id = applicazione.getId();			
				// Object _applicazione_applicazioneDominio_dominio
				Long id_applicazione_applicazioneDominio_dominio = null;
				it.govpay.orm.IdDominio idLogic_applicazione_applicazioneDominio_dominio = null;
				idLogic_applicazione_applicazioneDominio_dominio = applicazione_applicazioneDominio.getIdDominio();
				if(idLogic_applicazione_applicazioneDominio_dominio!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_applicazione_applicazioneDominio_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_applicazione_applicazioneDominio_dominio, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_applicazione_applicazioneDominio_dominio = idLogic_applicazione_applicazioneDominio_dominio.getId();
						if(id_applicazione_applicazioneDominio_dominio==null || id_applicazione_applicazioneDominio_dominio<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object applicazione_applicazioneDominio
				ISQLQueryObject sqlQueryObjectInsert_applicazione_applicazioneDominio = sqlQueryObjectInsert.newSQLQueryObject();
				sqlQueryObjectInsert_applicazione_applicazioneDominio.addInsertTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_DOMINIO));
				sqlQueryObjectInsert_applicazione_applicazioneDominio.addInsertField("id_dominio","?");
				sqlQueryObjectInsert_applicazione_applicazioneDominio.addInsertField("id_applicazione","?");

				// Insert applicazione_applicazioneDominio
				org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator_applicazione_applicazioneDominio = this.getApplicazioneFetch().getKeyGeneratorObject(Applicazione.model().APPLICAZIONE_DOMINIO);
				long id_applicazione_applicazioneDominio = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert_applicazione_applicazioneDominio, keyGenerator_applicazione_applicazioneDominio, jdbcProperties.isShowSql(),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione_applicazioneDominio_dominio,Long.class),
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class)
				);
				applicazione_applicazioneDominio.setId(id_applicazione_applicazioneDominio);

				ids_applicazione_applicazioneDominio_da_non_eliminare.add(applicazione_applicazioneDominio.getId());
			} else {

				// Object _applicazione_applicazioneDominio_dominio
				Long id_applicazione_applicazioneDominio_dominio = null;
				it.govpay.orm.IdDominio idLogic_applicazione_applicazioneDominio_dominio = null;
				idLogic_applicazione_applicazioneDominio_dominio = applicazione_applicazioneDominio.getIdDominio();
				if(idLogic_applicazione_applicazioneDominio_dominio!=null){
					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
						id_applicazione_applicazioneDominio_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_applicazione_applicazioneDominio_dominio, false);
					}
					else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
						id_applicazione_applicazioneDominio_dominio = idLogic_applicazione_applicazioneDominio_dominio.getId();
						if(id_applicazione_applicazioneDominio_dominio==null || id_applicazione_applicazioneDominio_dominio<=0){
							throw new Exception("Logic id not contains table id");
						}
					}
				}


				// Object applicazione_applicazioneDominio
				ISQLQueryObject sqlQueryObjectUpdate_applicazione_applicazioneDominio = sqlQueryObjectUpdate.newSQLQueryObject();
				sqlQueryObjectUpdate_applicazione_applicazioneDominio.setANDLogicOperator(true);
				sqlQueryObjectUpdate_applicazione_applicazioneDominio.addUpdateTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_DOMINIO));
				boolean isUpdate_applicazione_applicazioneDominio = true;
				java.util.List<JDBCObject> lstObjects_applicazione_applicazioneDominio = new java.util.ArrayList<JDBCObject>();
				if(setIdMappingResolutionBehaviour){
					sqlQueryObjectUpdate_applicazione_applicazioneDominio.addUpdateField("id_dominio","?");
				}
				if(setIdMappingResolutionBehaviour){
					lstObjects_applicazione_applicazioneDominio.add(new JDBCObject(id_applicazione_applicazioneDominio_dominio, Long.class));
				}
				sqlQueryObjectUpdate_applicazione_applicazioneDominio.addWhereCondition("id=?");
				ids_applicazione_applicazioneDominio_da_non_eliminare.add(applicazione_applicazioneDominio.getId());
				lstObjects_applicazione_applicazioneDominio.add(new JDBCObject(new Long(applicazione_applicazioneDominio.getId()),Long.class));

				if(isUpdate_applicazione_applicazioneDominio) {
					// Update applicazione_applicazioneDominio
					jdbcUtilities.executeUpdate(sqlQueryObjectUpdate_applicazione_applicazioneDominio.createSQLUpdate(), jdbcProperties.isShowSql(), 
						lstObjects_applicazione_applicazioneDominio.toArray(new JDBCObject[]{}));
				}
			}
		} // fine for applicazione_applicazioneDominio

		// elimino tutte le occorrenze di applicazione_applicazioneDominio non presenti nell'update

		ISQLQueryObject sqlQueryObjectUpdate_applicazioneDominio_deleteList = sqlQueryObjectUpdate.newSQLQueryObject();
		sqlQueryObjectUpdate_applicazioneDominio_deleteList.setANDLogicOperator(true);
		sqlQueryObjectUpdate_applicazioneDominio_deleteList.addDeleteTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_DOMINIO));
		java.util.List<JDBCObject> jdbcObjects_applicazione_applicazioneDominio_delete = new java.util.ArrayList<JDBCObject>();

		sqlQueryObjectUpdate_applicazioneDominio_deleteList.addWhereCondition("id_applicazione=?");
		jdbcObjects_applicazione_applicazioneDominio_delete.add(new JDBCObject(applicazione.getId(), Long.class));

		StringBuffer marks_applicazione_applicazioneDominio = new StringBuffer();
		if(ids_applicazione_applicazioneDominio_da_non_eliminare.size() > 0) {
			for(Long ids : ids_applicazione_applicazioneDominio_da_non_eliminare) {
				if(marks_applicazione_applicazioneDominio.length() > 0) {
					marks_applicazione_applicazioneDominio.append(",");
				}
				marks_applicazione_applicazioneDominio.append("?");
				jdbcObjects_applicazione_applicazioneDominio_delete.add(new JDBCObject(ids, Long.class));

			}
			sqlQueryObjectUpdate_applicazioneDominio_deleteList.addWhereCondition("id NOT IN ("+marks_applicazione_applicazioneDominio.toString()+")");
		}

		jdbcUtilities.execute(sqlQueryObjectUpdate_applicazioneDominio_deleteList.createSQLDelete(), jdbcProperties.isShowSql(), jdbcObjects_applicazione_applicazioneDominio_delete.toArray(new JDBCObject[]{}));


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdApplicazione id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getApplicazioneFieldConverter().toTable(Applicazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getApplicazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdApplicazione id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getApplicazioneFieldConverter().toTable(Applicazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getApplicazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdApplicazione id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getApplicazioneFieldConverter().toTable(Applicazione.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getApplicazioneFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getApplicazioneFieldConverter().toTable(Applicazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getApplicazioneFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getApplicazioneFieldConverter().toTable(Applicazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getApplicazioneFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getApplicazioneFieldConverter().toTable(Applicazione.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getApplicazioneFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdApplicazione oldId, Applicazione applicazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, applicazione,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, applicazione,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Applicazione applicazione, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, applicazione,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, applicazione,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Applicazione applicazione) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (applicazione.getId()!=null) && (applicazione.getId()>0) ){
			longId = applicazione.getId();
		}
		else{
			IdApplicazione idApplicazione = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,applicazione);
			longId = this.findIdApplicazione(jdbcProperties,log,connection,sqlQueryObject,idApplicazione,false);
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
		

		//Recupero oggetto _applicazione_applicazioneTributo
		ISQLQueryObject sqlQueryObjectDelete_applicazione_applicazioneTributo_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_applicazione_applicazioneTributo_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_applicazione_applicazioneTributo_getToDelete.addFromTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_TRIBUTO));
		sqlQueryObjectDelete_applicazione_applicazioneTributo_getToDelete.addWhereCondition("id_applicazione=?");
		java.util.List<Object> applicazione_applicazioneTributo_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_applicazione_applicazioneTributo_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Applicazione.model().APPLICAZIONE_TRIBUTO, this.getApplicazioneFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for applicazione_applicazioneTributo
		for (Object applicazione_applicazioneTributo_object : applicazione_applicazioneTributo_toDelete_list) {
			ApplicazioneTributo applicazione_applicazioneTributo = (ApplicazioneTributo) applicazione_applicazioneTributo_object;

			// Object applicazione_applicazioneTributo
			ISQLQueryObject sqlQueryObjectDelete_applicazione_applicazioneTributo = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_applicazione_applicazioneTributo.setANDLogicOperator(true);
			sqlQueryObjectDelete_applicazione_applicazioneTributo.addDeleteTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_TRIBUTO));
			sqlQueryObjectDelete_applicazione_applicazioneTributo.addWhereCondition("id=?");

			// Delete applicazione_applicazioneTributo
			if(applicazione_applicazioneTributo != null){
				jdbcUtilities.execute(sqlQueryObjectDelete_applicazione_applicazioneTributo.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(applicazione_applicazioneTributo.getId()),Long.class));
			}
		} // fine for applicazione_applicazioneTributo

		//Recupero oggetto _applicazione_applicazioneDominio
		ISQLQueryObject sqlQueryObjectDelete_applicazione_applicazioneDominio_getToDelete = sqlQueryObjectDelete.newSQLQueryObject();
		sqlQueryObjectDelete_applicazione_applicazioneDominio_getToDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete_applicazione_applicazioneDominio_getToDelete.addFromTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_DOMINIO));
		sqlQueryObjectDelete_applicazione_applicazioneDominio_getToDelete.addWhereCondition("id_applicazione=?");
		java.util.List<Object> applicazione_applicazioneDominio_toDelete_list = (java.util.List<Object>) jdbcUtilities.executeQuery(sqlQueryObjectDelete_applicazione_applicazioneDominio_getToDelete.createSQLQuery(), jdbcProperties.isShowSql(), Applicazione.model().APPLICAZIONE_DOMINIO, this.getApplicazioneFetch(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(id),Long.class));

		// for applicazione_applicazioneDominio
		for (Object applicazione_applicazioneDominio_object : applicazione_applicazioneDominio_toDelete_list) {
			ApplicazioneDominio applicazione_applicazioneDominio = (ApplicazioneDominio) applicazione_applicazioneDominio_object;

			// Object applicazione_applicazioneDominio
			ISQLQueryObject sqlQueryObjectDelete_applicazione_applicazioneDominio = sqlQueryObjectDelete.newSQLQueryObject();
			sqlQueryObjectDelete_applicazione_applicazioneDominio.setANDLogicOperator(true);
			sqlQueryObjectDelete_applicazione_applicazioneDominio.addDeleteTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model().APPLICAZIONE_DOMINIO));
			sqlQueryObjectDelete_applicazione_applicazioneDominio.addWhereCondition("id=?");

			// Delete applicazione_applicazioneDominio
			if(applicazione_applicazioneDominio != null){
				jdbcUtilities.execute(sqlQueryObjectDelete_applicazione_applicazioneDominio.createSQLDelete(), jdbcProperties.isShowSql(), 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(new Long(applicazione_applicazioneDominio.getId()),Long.class));
			}
		} // fine for applicazione_applicazioneDominio

		// Object applicazione
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getApplicazioneFieldConverter().toTable(Applicazione.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete applicazione
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdApplicazione idApplicazione) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdApplicazione(jdbcProperties, log, connection, sqlQueryObject, idApplicazione, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getApplicazioneFieldConverter()));

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
