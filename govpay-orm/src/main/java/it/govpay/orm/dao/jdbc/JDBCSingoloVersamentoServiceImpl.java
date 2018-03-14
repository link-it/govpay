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
import it.govpay.orm.IdSingoloVersamento;
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

import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCSingoloVersamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCSingoloVersamentoServiceImpl extends JDBCSingoloVersamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<SingoloVersamento, IdSingoloVersamento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingoloVersamento singoloVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _versamento
		Long id_versamento = null;
		it.govpay.orm.IdVersamento idLogic_versamento = null;
		idLogic_versamento = singoloVersamento.getIdVersamento();
		if(idLogic_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_versamento = idLogic_versamento.getId();
				if(id_versamento==null || id_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tributo
		Long id_tributo = null;
		it.govpay.orm.IdTributo idLogic_tributo = null;
		idLogic_tributo = singoloVersamento.getIdTributo();
		if(idLogic_tributo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tributo = ((JDBCTributoServiceSearch)(this.getServiceManager().getTributoServiceSearch())).findTableId(idLogic_tributo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tributo = idLogic_tributo.getId();
				if(id_tributo==null || id_tributo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _ibanAccredito
		Long id_ibanAccredito = null;
		it.govpay.orm.IdIbanAccredito idLogic_ibanAccredito = null;
		idLogic_ibanAccredito = singoloVersamento.getIdIbanAccredito();
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
		idLogic_ibanAccreditoInstance2 = singoloVersamento.getIdIbanAppoggio();
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


		// Object singoloVersamento
		sqlQueryObjectInsert.addInsertTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().ANNO_RIFERIMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().TIPO_BOLLO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().HASH_DOCUMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().PROVINCIA_RESIDENZA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().TIPO_CONTABILITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().CODICE_CONTABILITA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().DESCRIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().DATI_ALLEGATI,false),"?");
		sqlQueryObjectInsert.addInsertField("id_versamento","?");
		sqlQueryObjectInsert.addInsertField("id_tributo","?");
		sqlQueryObjectInsert.addInsertField("id_iban_accredito","?");
		sqlQueryObjectInsert.addInsertField("id_iban_appoggio","?");

		// Insert singoloVersamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getSingoloVersamentoFetch().getKeyGeneratorObject(SingoloVersamento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getCodSingoloVersamentoEnte(),SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getStatoSingoloVersamento(),SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getImportoSingoloVersamento(),SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getAnnoRiferimento(),SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getTipoBollo(),SingoloVersamento.model().TIPO_BOLLO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getHashDocumento(),SingoloVersamento.model().HASH_DOCUMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getProvinciaResidenza(),SingoloVersamento.model().PROVINCIA_RESIDENZA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getTipoContabilita(),SingoloVersamento.model().TIPO_CONTABILITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getCodiceContabilita(),SingoloVersamento.model().CODICE_CONTABILITA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getDescrizione(),SingoloVersamento.model().DESCRIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(singoloVersamento.getDatiAllegati(),SingoloVersamento.model().DATI_ALLEGATI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_versamento,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tributo,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_ibanAccredito,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_ibanAccreditoInstance2,Long.class)
		);
		singoloVersamento.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento oldId, SingoloVersamento singoloVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdSingoloVersamento(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = singoloVersamento.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: singoloVersamento.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			singoloVersamento.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, singoloVersamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SingoloVersamento singoloVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _singoloVersamento_versamento
		Long id_singoloVersamento_versamento = null;
		it.govpay.orm.IdVersamento idLogic_singoloVersamento_versamento = null;
		idLogic_singoloVersamento_versamento = singoloVersamento.getIdVersamento();
		if(idLogic_singoloVersamento_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singoloVersamento_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_singoloVersamento_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singoloVersamento_versamento = idLogic_singoloVersamento_versamento.getId();
				if(id_singoloVersamento_versamento==null || id_singoloVersamento_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _singoloVersamento_tributo
		Long id_singoloVersamento_tributo = null;
		it.govpay.orm.IdTributo idLogic_singoloVersamento_tributo = null;
		idLogic_singoloVersamento_tributo = singoloVersamento.getIdTributo();
		if(idLogic_singoloVersamento_tributo!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singoloVersamento_tributo = ((JDBCTributoServiceSearch)(this.getServiceManager().getTributoServiceSearch())).findTableId(idLogic_singoloVersamento_tributo, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singoloVersamento_tributo = idLogic_singoloVersamento_tributo.getId();
				if(id_singoloVersamento_tributo==null || id_singoloVersamento_tributo<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _singoloVersamento_ibanAccredito
		Long id_singoloVersamento_ibanAccredito = null;
		it.govpay.orm.IdIbanAccredito idLogic_singoloVersamento_ibanAccredito = null;

		idLogic_singoloVersamento_ibanAccredito = singoloVersamento.getIdIbanAccredito();
		if(idLogic_singoloVersamento_ibanAccredito!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singoloVersamento_ibanAccredito = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findTableId(idLogic_singoloVersamento_ibanAccredito, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singoloVersamento_ibanAccredito = idLogic_singoloVersamento_ibanAccredito.getId();
				if(id_singoloVersamento_ibanAccredito==null || id_singoloVersamento_ibanAccredito<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _singoloVersamento_ibanAccreditoInstance2
		Long id_singoloVersamento_ibanAccreditoInstance2 = null;
		it.govpay.orm.IdIbanAccredito idLogic_singoloVersamento_ibanAccreditoInstance2 = null;
		idLogic_singoloVersamento_ibanAccreditoInstance2 = singoloVersamento.getIdIbanAppoggio();
		if(idLogic_singoloVersamento_ibanAccreditoInstance2!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singoloVersamento_ibanAccreditoInstance2 = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findTableId(idLogic_singoloVersamento_ibanAccreditoInstance2, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singoloVersamento_ibanAccreditoInstance2 = idLogic_singoloVersamento_ibanAccreditoInstance2.getId();
				if(id_singoloVersamento_ibanAccreditoInstance2==null || id_singoloVersamento_ibanAccreditoInstance2<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object singoloVersamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
		boolean isUpdate_singoloVersamento = true;
		java.util.List<JDBCObject> lstObjects_singoloVersamento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getCodSingoloVersamentoEnte(), SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getStatoSingoloVersamento(), SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getImportoSingoloVersamento(), SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().ANNO_RIFERIMENTO,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getAnnoRiferimento(), SingoloVersamento.model().ANNO_RIFERIMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().TIPO_BOLLO,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getTipoBollo(), SingoloVersamento.model().TIPO_BOLLO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().HASH_DOCUMENTO,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getHashDocumento(), SingoloVersamento.model().HASH_DOCUMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().PROVINCIA_RESIDENZA,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getProvinciaResidenza(), SingoloVersamento.model().PROVINCIA_RESIDENZA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().TIPO_CONTABILITA,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getTipoContabilita(), SingoloVersamento.model().TIPO_CONTABILITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().CODICE_CONTABILITA,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getCodiceContabilita(), SingoloVersamento.model().CODICE_CONTABILITA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().DESCRIZIONE,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getDescrizione(), SingoloVersamento.model().DESCRIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getSingoloVersamentoFieldConverter().toColumn(SingoloVersamento.model().DATI_ALLEGATI,false), "?");
		lstObjects_singoloVersamento.add(new JDBCObject(singoloVersamento.getDatiAllegati(), SingoloVersamento.model().DATI_ALLEGATI.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tributo","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_iban_accredito","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_iban_appoggio","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singoloVersamento.add(new JDBCObject(id_singoloVersamento_versamento, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singoloVersamento.add(new JDBCObject(id_singoloVersamento_tributo, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singoloVersamento.add(new JDBCObject(id_singoloVersamento_ibanAccredito, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_singoloVersamento.add(new JDBCObject(id_singoloVersamento_ibanAccreditoInstance2, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_singoloVersamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_singoloVersamento) {
			// Update singoloVersamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_singoloVersamento.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingoloVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingoloVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getSingoloVersamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingoloVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingoloVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getSingoloVersamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento oldId, SingoloVersamento singoloVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, singoloVersamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, singoloVersamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, SingoloVersamento singoloVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, singoloVersamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, singoloVersamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, SingoloVersamento singoloVersamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (singoloVersamento.getId()!=null) && (singoloVersamento.getId()>0) ){
			longId = singoloVersamento.getId();
		}
		else{
			IdSingoloVersamento idSingoloVersamento = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,singoloVersamento);
			longId = this.findIdSingoloVersamento(jdbcProperties,log,connection,sqlQueryObject,idSingoloVersamento,false);
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
		

		// Object singoloVersamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getSingoloVersamentoFieldConverter().toTable(SingoloVersamento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete singoloVersamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdSingoloVersamento idSingoloVersamento) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdSingoloVersamento(jdbcProperties, log, connection, sqlQueryObject, idSingoloVersamento, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getSingoloVersamentoFieldConverter()));

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
