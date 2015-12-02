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
import it.govpay.orm.IdMail;
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

import it.govpay.orm.Mail;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCMailServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCMailServiceImpl extends JDBCMailServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Mail, IdMail, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Mail mail, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _tracciatoXML
		Long id_tracciatoXML = null;
		it.govpay.orm.IdTracciato idLogic_tracciatoXML = null;
		idLogic_tracciatoXML = mail.getIdTracciatoRPT();
		if(idLogic_tracciatoXML!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciatoXML = ((JDBCTracciatoXMLServiceSearch)(this.getServiceManager().getTracciatoXMLServiceSearch())).findTableId(idLogic_tracciatoXML, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciatoXML = idLogic_tracciatoXML.getId();
				if(id_tracciatoXML==null || id_tracciatoXML<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tracciatoXMLInstance2
		Long id_tracciatoXMLInstance2 = null;
		it.govpay.orm.IdTracciato idLogic_tracciatoXMLInstance2 = null;
		idLogic_tracciatoXMLInstance2 = mail.getIdTracciatoRT();
		if(idLogic_tracciatoXMLInstance2!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tracciatoXMLInstance2 = ((JDBCTracciatoXMLServiceSearch)(this.getServiceManager().getTracciatoXMLServiceSearch())).findTableId(idLogic_tracciatoXMLInstance2, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tracciatoXMLInstance2 = idLogic_tracciatoXMLInstance2.getId();
				if(id_tracciatoXMLInstance2==null || id_tracciatoXMLInstance2<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object mail
		sqlQueryObjectInsert.addInsertTable(this.getMailFieldConverter().toTable(Mail.model()));
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().TIPO_MAIL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().BUNDLE_KEY,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().ID_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().MITTENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().DESTINATARIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().CC,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().STATO_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getMailFieldConverter().toColumn(Mail.model().TENTATIVI_RISPEDIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField("id_tracciato_rpt","?");
		sqlQueryObjectInsert.addInsertField("id_tracciato_rt","?");

		// Insert mail
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getMailFetch().getKeyGeneratorObject(Mail.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getTipoMail(),Mail.model().TIPO_MAIL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getBundleKey(),Mail.model().BUNDLE_KEY.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getIdVersamento(),Mail.model().ID_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getMittente(),Mail.model().MITTENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getDestinatario(),Mail.model().DESTINATARIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getCc(),Mail.model().CC.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getOggetto(),Mail.model().OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getMessaggio(),Mail.model().MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getStatoSpedizione(),Mail.model().STATO_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getDettaglioErroreSpedizione(),Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getDataOraUltimaSpedizione(),Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(mail.getTentativiRispedizione(),Mail.model().TENTATIVI_RISPEDIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tracciatoXML,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tracciatoXMLInstance2,Long.class)
		);
		mail.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMail oldId, Mail mail, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdMail(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = mail.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: mail.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			mail.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, mail, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Mail mail, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
			

		// Object _mail_tracciatoXML
		Long id_mail_tracciatoXML = null;
		it.govpay.orm.IdTracciato idLogic_mail_tracciatoXML = null;
		idLogic_mail_tracciatoXML = mail.getIdTracciatoRPT();
		if(idLogic_mail_tracciatoXML!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_mail_tracciatoXML = ((JDBCTracciatoXMLServiceSearch)(this.getServiceManager().getTracciatoXMLServiceSearch())).findTableId(idLogic_mail_tracciatoXML, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_mail_tracciatoXML = idLogic_mail_tracciatoXML.getId();
				if(id_mail_tracciatoXML==null || id_mail_tracciatoXML<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _mail_tracciatoXMLInstance2
		Long id_mail_tracciatoXMLInstance2 = null;
		it.govpay.orm.IdTracciato idLogic_mail_tracciatoXMLInstance2 = null;
		idLogic_mail_tracciatoXMLInstance2 = mail.getIdTracciatoRT();
		if(idLogic_mail_tracciatoXMLInstance2!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_mail_tracciatoXMLInstance2 = ((JDBCTracciatoXMLServiceSearch)(this.getServiceManager().getTracciatoXMLServiceSearch())).findTableId(idLogic_mail_tracciatoXMLInstance2, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_mail_tracciatoXMLInstance2 = idLogic_mail_tracciatoXMLInstance2.getId();
				if(id_mail_tracciatoXMLInstance2==null || id_mail_tracciatoXMLInstance2<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object mail
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getMailFieldConverter().toTable(Mail.model()));
		boolean isUpdate_mail = true;
		java.util.List<JDBCObject> lstObjects_mail = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().TIPO_MAIL,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getTipoMail(), Mail.model().TIPO_MAIL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().BUNDLE_KEY,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getBundleKey(), Mail.model().BUNDLE_KEY.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().ID_VERSAMENTO,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getIdVersamento(), Mail.model().ID_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().MITTENTE,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getMittente(), Mail.model().MITTENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().DESTINATARIO,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getDestinatario(), Mail.model().DESTINATARIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().CC,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getCc(), Mail.model().CC.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().OGGETTO,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getOggetto(), Mail.model().OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().MESSAGGIO,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getMessaggio(), Mail.model().MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().STATO_SPEDIZIONE,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getStatoSpedizione(), Mail.model().STATO_SPEDIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getDettaglioErroreSpedizione(), Mail.model().DETTAGLIO_ERRORE_SPEDIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getDataOraUltimaSpedizione(), Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getMailFieldConverter().toColumn(Mail.model().TENTATIVI_RISPEDIZIONE,false), "?");
		lstObjects_mail.add(new JDBCObject(mail.getTentativiRispedizione(), Mail.model().TENTATIVI_RISPEDIZIONE.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tracciato_rpt","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tracciato_rt","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_mail.add(new JDBCObject(id_mail_tracciatoXML, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_mail.add(new JDBCObject(id_mail_tracciatoXMLInstance2, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_mail.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_mail) {
			// Update mail
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_mail.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMail id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMailFieldConverter().toTable(Mail.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getMailFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMail id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMailFieldConverter().toTable(Mail.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getMailFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMail id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMailFieldConverter().toTable(Mail.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getMailFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMailFieldConverter().toTable(Mail.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getMailFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMailFieldConverter().toTable(Mail.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getMailFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getMailFieldConverter().toTable(Mail.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getMailFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMail oldId, Mail mail, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, mail,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, mail,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Mail mail, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, mail,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, mail,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Mail mail) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (mail.getId()!=null) && (mail.getId()>0) ){
			longId = mail.getId();
		}
		else{
			IdMail idMail = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,mail);
			longId = this.findIdMail(jdbcProperties,log,connection,sqlQueryObject,idMail,false);
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
		

		// Object mail
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getMailFieldConverter().toTable(Mail.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete mail
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdMail idMail) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdMail(jdbcProperties, log, connection, sqlQueryObject, idMail, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getMailFieldConverter()));

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
