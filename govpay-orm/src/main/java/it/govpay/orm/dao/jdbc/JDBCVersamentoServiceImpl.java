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
import it.govpay.orm.IdVersamento;
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

import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCVersamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVersamentoServiceImpl extends JDBCVersamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Versamento, IdVersamento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _uo
		Long id_uo = null;
		it.govpay.orm.IdUo idLogic_uo = null;
		idLogic_uo = versamento.getIdUo();
		if(idLogic_uo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findTableId(idLogic_uo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_uo = idLogic_uo.getId();
				if(id_uo==null || id_uo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _applicazione
		Long id_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_applicazione = null;
		idLogic_applicazione = versamento.getIdApplicazione();
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


		// Object versamento
		sqlQueryObjectInsert.addInsertTable(this.getVersamentoFieldConverter().toTable(Versamento.model()));
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_VERSAMENTO_ENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IMPORTO_TOTALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().STATO_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().AGGIORNABILE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_CREAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_SCADENZA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().CAUSALE_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_IDENTIFICATIVO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_ANAGRAFICA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_INDIRIZZO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_CIVICO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_CAP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_LOCALITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_PROVINCIA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_NAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_EMAIL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_TELEFONO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_CELLULARE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_FAX,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_LOTTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_VERSAMENTO_LOTTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_ANNO_TRIBUTARIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_BUNDLEKEY,false),"?");
		sqlQueryObjectInsert.addInsertField("id_uo","?");
		sqlQueryObjectInsert.addInsertField("id_applicazione","?");

		// Insert versamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getVersamentoFetch().getKeyGeneratorObject(Versamento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCodVersamentoEnte(),Versamento.model().COD_VERSAMENTO_ENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getImportoTotale(),Versamento.model().IMPORTO_TOTALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getStatoVersamento(),Versamento.model().STATO_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDescrizioneStato(),Versamento.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getAggiornabile(),Versamento.model().AGGIORNABILE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDataCreazione(),Versamento.model().DATA_CREAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDataScadenza(),Versamento.model().DATA_SCADENZA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDataOraUltimoAggiornamento(),Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCausaleVersamento(),Versamento.model().CAUSALE_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreIdentificativo(),Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreAnagrafica(),Versamento.model().DEBITORE_ANAGRAFICA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreIndirizzo(),Versamento.model().DEBITORE_INDIRIZZO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreCivico(),Versamento.model().DEBITORE_CIVICO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreCap(),Versamento.model().DEBITORE_CAP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreLocalita(),Versamento.model().DEBITORE_LOCALITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreProvincia(),Versamento.model().DEBITORE_PROVINCIA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreNazione(),Versamento.model().DEBITORE_NAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreEmail(),Versamento.model().DEBITORE_EMAIL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreTelefono(),Versamento.model().DEBITORE_TELEFONO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreCellulare(),Versamento.model().DEBITORE_CELLULARE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getDebitoreFax(),Versamento.model().DEBITORE_FAX.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCodLotto(),Versamento.model().COD_LOTTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCodVersamentoLotto(),Versamento.model().COD_VERSAMENTO_LOTTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCodAnnoTributario(),Versamento.model().COD_ANNO_TRIBUTARIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(versamento.getCodBundlekey(),Versamento.model().COD_BUNDLEKEY.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_uo,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_applicazione,Long.class)
		);
		versamento.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento oldId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdVersamento(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = versamento.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: versamento.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			versamento.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, versamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _versamento_uo
		Long id_versamento_uo = null;
		it.govpay.orm.IdUo idLogic_versamento_uo = null;
		idLogic_versamento_uo = versamento.getIdUo();
		if(idLogic_versamento_uo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findTableId(idLogic_versamento_uo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento_uo = idLogic_versamento_uo.getId();
				if(id_versamento_uo==null || id_versamento_uo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _versamento_applicazione
		Long id_versamento_applicazione = null;
		it.govpay.orm.IdApplicazione idLogic_versamento_applicazione = null;
		idLogic_versamento_applicazione = versamento.getIdApplicazione();
		if(idLogic_versamento_applicazione!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findTableId(idLogic_versamento_applicazione, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento_applicazione = idLogic_versamento_applicazione.getId();
				if(id_versamento_applicazione==null || id_versamento_applicazione<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object versamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getVersamentoFieldConverter().toTable(Versamento.model()));
		boolean isUpdate_versamento = true;
		java.util.List<JDBCObject> lstObjects_versamento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_VERSAMENTO_ENTE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCodVersamentoEnte(), Versamento.model().COD_VERSAMENTO_ENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().IMPORTO_TOTALE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getImportoTotale(), Versamento.model().IMPORTO_TOTALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().STATO_VERSAMENTO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getStatoVersamento(), Versamento.model().STATO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDescrizioneStato(), Versamento.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().AGGIORNABILE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getAggiornabile(), Versamento.model().AGGIORNABILE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_CREAZIONE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDataCreazione(), Versamento.model().DATA_CREAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_SCADENZA,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDataScadenza(), Versamento.model().DATA_SCADENZA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDataOraUltimoAggiornamento(), Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().CAUSALE_VERSAMENTO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCausaleVersamento(), Versamento.model().CAUSALE_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_IDENTIFICATIVO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreIdentificativo(), Versamento.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_ANAGRAFICA,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreAnagrafica(), Versamento.model().DEBITORE_ANAGRAFICA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_INDIRIZZO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreIndirizzo(), Versamento.model().DEBITORE_INDIRIZZO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_CIVICO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreCivico(), Versamento.model().DEBITORE_CIVICO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_CAP,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreCap(), Versamento.model().DEBITORE_CAP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_LOCALITA,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreLocalita(), Versamento.model().DEBITORE_LOCALITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_PROVINCIA,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreProvincia(), Versamento.model().DEBITORE_PROVINCIA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_NAZIONE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreNazione(), Versamento.model().DEBITORE_NAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_EMAIL,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreEmail(), Versamento.model().DEBITORE_EMAIL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_TELEFONO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreTelefono(), Versamento.model().DEBITORE_TELEFONO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_CELLULARE,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreCellulare(), Versamento.model().DEBITORE_CELLULARE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().DEBITORE_FAX,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getDebitoreFax(), Versamento.model().DEBITORE_FAX.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_LOTTO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCodLotto(), Versamento.model().COD_LOTTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_VERSAMENTO_LOTTO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCodVersamentoLotto(), Versamento.model().COD_VERSAMENTO_LOTTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_ANNO_TRIBUTARIO,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCodAnnoTributario(), Versamento.model().COD_ANNO_TRIBUTARIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVersamentoFieldConverter().toColumn(Versamento.model().COD_BUNDLEKEY,false), "?");
		lstObjects_versamento.add(new JDBCObject(versamento.getCodBundlekey(), Versamento.model().COD_BUNDLEKEY.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_uo","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_applicazione","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_versamento.add(new JDBCObject(id_versamento_uo, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_versamento.add(new JDBCObject(id_versamento_applicazione, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_versamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_versamento) {
			// Update versamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_versamento.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getVersamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVersamentoFieldConverter().toTable(Versamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVersamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento oldId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, versamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, versamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Versamento versamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, versamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, versamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Versamento versamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (versamento.getId()!=null) && (versamento.getId()>0) ){
			longId = versamento.getId();
		}
		else{
			IdVersamento idVersamento = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,versamento);
			longId = this.findIdVersamento(jdbcProperties,log,connection,sqlQueryObject,idVersamento,false);
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
		

		// Object versamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getVersamentoFieldConverter().toTable(Versamento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete versamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdVersamento idVersamento) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdVersamento(jdbcProperties, log, connection, sqlQueryObject, idVersamento, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getVersamentoFieldConverter()));

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
