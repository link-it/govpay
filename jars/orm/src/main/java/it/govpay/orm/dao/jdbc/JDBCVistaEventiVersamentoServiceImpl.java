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

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithoutId;
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

import it.govpay.orm.Evento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCVistaEventiVersamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVistaEventiVersamentoServiceImpl extends JDBCVistaEventiVersamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithoutId<Evento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento vistaEventiVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object vistaEventiVersamento
		sqlQueryObjectInsert.addInsertTable(this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()));
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COMPONENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().RUOLO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().CATEGORIA_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().TIPO_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().SOTTOTIPO_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().DATA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().INTERVALLO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().SOTTOTIPO_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().DETTAGLIO_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().PARAMETRI_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().PARAMETRI_RISPOSTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().DATI_PAGO_PA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COD_VERSAMENTO_ENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COD_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().CCP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().ID_SESSIONE,false),"?");

		// Insert vistaEventiVersamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getVistaEventiVersamentoFetch().getKeyGeneratorObject(Evento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getComponente(),Evento.model().COMPONENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getRuolo(),Evento.model().RUOLO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getCategoriaEvento(),Evento.model().CATEGORIA_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getTipoEvento(),Evento.model().TIPO_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getSottotipoEvento(),Evento.model().SOTTOTIPO_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getData(),Evento.model().DATA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getIntervallo(),Evento.model().INTERVALLO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getEsito(),Evento.model().ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getSottotipoEsito(),Evento.model().SOTTOTIPO_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getDettaglioEsito(),Evento.model().DETTAGLIO_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getParametriRichiesta(),Evento.model().PARAMETRI_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getParametriRisposta(),Evento.model().PARAMETRI_RISPOSTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getDatiPagoPA(),Evento.model().DATI_PAGO_PA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getCodVersamentoEnte(),Evento.model().COD_VERSAMENTO_ENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getCodApplicazione(),Evento.model().COD_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getIuv(),Evento.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getCcp(),Evento.model().CCP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getCodDominio(),Evento.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(vistaEventiVersamento.getIdSessione(),Evento.model().ID_SESSIONE.getFieldType())
		);
		vistaEventiVersamento.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento vistaEventiVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		Long tableId = vistaEventiVersamento.getId();
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, vistaEventiVersamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Evento vistaEventiVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
		
//		boolean setIdMappingResolutionBehaviour = 
//			(idMappingResolutionBehaviour==null) ||
//			org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) ||
//			org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour);
//			


		// Object vistaEventiVersamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()));
		boolean isUpdate_vistaEventiVersamento = true;
		java.util.List<JDBCObject> lstObjects_vistaEventiVersamento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COMPONENTE,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getComponente(), Evento.model().COMPONENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().RUOLO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getRuolo(), Evento.model().RUOLO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().CATEGORIA_EVENTO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getCategoriaEvento(), Evento.model().CATEGORIA_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().TIPO_EVENTO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getTipoEvento(), Evento.model().TIPO_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().SOTTOTIPO_EVENTO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getSottotipoEvento(), Evento.model().SOTTOTIPO_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().DATA,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getData(), Evento.model().DATA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().INTERVALLO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getIntervallo(), Evento.model().INTERVALLO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().ESITO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getEsito(), Evento.model().ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().SOTTOTIPO_ESITO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getSottotipoEsito(), Evento.model().SOTTOTIPO_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().DETTAGLIO_ESITO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getDettaglioEsito(), Evento.model().DETTAGLIO_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().PARAMETRI_RICHIESTA,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getParametriRichiesta(), Evento.model().PARAMETRI_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().PARAMETRI_RISPOSTA,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getParametriRisposta(), Evento.model().PARAMETRI_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().DATI_PAGO_PA,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getDatiPagoPA(), Evento.model().DATI_PAGO_PA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COD_VERSAMENTO_ENTE,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getCodVersamentoEnte(), Evento.model().COD_VERSAMENTO_ENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COD_APPLICAZIONE,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getCodApplicazione(), Evento.model().COD_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().IUV,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getIuv(), Evento.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().CCP,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getCcp(), Evento.model().CCP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().COD_DOMINIO,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getCodDominio(), Evento.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getVistaEventiVersamentoFieldConverter().toColumn(Evento.model().ID_SESSIONE,false), "?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(vistaEventiVersamento.getIdSessione(), Evento.model().ID_SESSIONE.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_vistaEventiVersamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_vistaEventiVersamento) {
			// Update vistaEventiVersamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_vistaEventiVersamento.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento vistaEventiVersamento, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, vistaEventiVersamento),
				this.getVistaEventiVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento vistaEventiVersamento, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, vistaEventiVersamento),
				this.getVistaEventiVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento vistaEventiVersamento, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, vistaEventiVersamento),
				this.getVistaEventiVersamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVistaEventiVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVistaEventiVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getVistaEventiVersamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento vistaEventiVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		Long id = vistaEventiVersamento.getId();
		if(id != null && this.exists(jdbcProperties, log, connection, sqlQueryObject, id)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, vistaEventiVersamento,idMappingResolutionBehaviour);		
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, vistaEventiVersamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Evento vistaEventiVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, vistaEventiVersamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, vistaEventiVersamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento vistaEventiVersamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if(vistaEventiVersamento.getId()==null){
			throw new Exception("Parameter "+vistaEventiVersamento.getClass().getName()+".id is null");
		}
		if(vistaEventiVersamento.getId()<=0){
			throw new Exception("Parameter "+vistaEventiVersamento.getClass().getName()+".id is less equals 0");
		}
		longId = vistaEventiVersamento.getId();
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object vistaEventiVersamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getVistaEventiVersamentoFieldConverter().toTable(Evento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete vistaEventiVersamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getVistaEventiVersamentoFieldConverter()));

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
