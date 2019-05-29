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
 * JDBCEventoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCEventoServiceImpl extends JDBCEventoServiceSearchImpl
	implements IJDBCServiceCRUDWithoutId<Evento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _versamento
		Long id_versamento = null;
		it.govpay.orm.IdVersamento idLogic_versamento = evento.getIdVersamento();
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

		// Object _pagamentoPortale
		Long id_pagamentoPortale = null;
		it.govpay.orm.IdPagamentoPortale idLogic_pagamentoPortale = evento.getIdPagamentoPortale();
		if(idLogic_pagamentoPortale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_pagamentoPortale = ((JDBCPagamentoPortaleServiceSearch)(this.getServiceManager().getPagamentoPortaleServiceSearch())).findTableId(idLogic_pagamentoPortale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_pagamentoPortale = idLogic_pagamentoPortale.getId();
				if(id_pagamentoPortale==null || id_pagamentoPortale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rpt
		Long id_rpt = null;
		it.govpay.orm.IdRpt idLogic_rpt = null;
		idLogic_rpt = evento.getIdRpt();
		if(idLogic_rpt!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findTableId(idLogic_rpt, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rpt = idLogic_rpt.getId();
				if(id_rpt==null || id_rpt<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object evento
		sqlQueryObjectInsert.addInsertTable(this.getEventoFieldConverter().toTable(Evento.model()));
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().COMPONENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().RUOLO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().CATEGORIA_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().TIPO_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().SOTTOTIPO_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().DATA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().INTERVALLO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().SOTTOTIPO_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().DETTAGLIO_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().PARAMETRI_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().PARAMETRI_RISPOSTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().DATI_CONTROPARTE,false),"?");
		sqlQueryObjectInsert.addInsertField("id_versamento","?");
		sqlQueryObjectInsert.addInsertField("id_pagamento_portale","?");
		sqlQueryObjectInsert.addInsertField("id_rpt","?");

		// Insert evento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getEventoFetch().getKeyGeneratorObject(Evento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getComponente(),Evento.model().COMPONENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getRuolo(),Evento.model().RUOLO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCategoriaEvento(),Evento.model().CATEGORIA_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getTipoEvento(),Evento.model().TIPO_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getSottotipoEvento(),Evento.model().SOTTOTIPO_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getData(),Evento.model().DATA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getIntervallo(),Evento.model().INTERVALLO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getEsito(),Evento.model().ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getSottotipoEsito(),Evento.model().SOTTOTIPO_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getDettaglioEsito(),Evento.model().DETTAGLIO_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getParametriRichiesta(),Evento.model().PARAMETRI_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getParametriRisposta(),Evento.model().PARAMETRI_RISPOSTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getDatiControparte(),Evento.model().DATI_CONTROPARTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_versamento,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_pagamentoPortale,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_rpt,Long.class)
		);
		evento.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		Long tableId = evento.getId();
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, evento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _evento_versamento
		Long id_evento_versamento = null;
		it.govpay.orm.IdVersamento idLogic_evento_versamento = evento.getIdVersamento();
		if(idLogic_evento_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_evento_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_evento_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_evento_versamento = idLogic_evento_versamento.getId();
				if(id_evento_versamento==null || id_evento_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _evento_pagamentoPortale
		Long id_evento_pagamentoPortale = null;
		it.govpay.orm.IdPagamentoPortale idLogic_evento_pagamentoPortale = evento.getIdPagamentoPortale();
		if(idLogic_evento_pagamentoPortale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_evento_pagamentoPortale = ((JDBCPagamentoPortaleServiceSearch)(this.getServiceManager().getPagamentoPortaleServiceSearch())).findTableId(idLogic_evento_pagamentoPortale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_evento_pagamentoPortale = idLogic_evento_pagamentoPortale.getId();
				if(id_evento_pagamentoPortale==null || id_evento_pagamentoPortale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _evento_rpt
		Long id_evento_rpt = null;
		it.govpay.orm.IdRpt idLogic_evento_rpt = null;
		idLogic_evento_rpt = evento.getIdRpt();
		if(idLogic_evento_rpt!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_evento_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findTableId(idLogic_evento_rpt, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_evento_rpt = idLogic_evento_rpt.getId();
				if(id_evento_rpt==null || id_evento_rpt<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object evento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getEventoFieldConverter().toTable(Evento.model()));
		boolean isUpdate_evento = true;
		java.util.List<JDBCObject> lstObjects_evento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().COMPONENTE,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getComponente(), Evento.model().COMPONENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().RUOLO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getRuolo(), Evento.model().RUOLO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().CATEGORIA_EVENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCategoriaEvento(), Evento.model().CATEGORIA_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().TIPO_EVENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getTipoEvento(), Evento.model().TIPO_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().SOTTOTIPO_EVENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getSottotipoEvento(), Evento.model().SOTTOTIPO_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().DATA,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getData(), Evento.model().DATA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().INTERVALLO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getIntervallo(), Evento.model().INTERVALLO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().ESITO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getEsito(), Evento.model().ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().SOTTOTIPO_ESITO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getSottotipoEsito(), Evento.model().SOTTOTIPO_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().DETTAGLIO_ESITO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getDettaglioEsito(), Evento.model().DETTAGLIO_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().PARAMETRI_RICHIESTA,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getParametriRichiesta(), Evento.model().PARAMETRI_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().PARAMETRI_RISPOSTA,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getParametriRisposta(), Evento.model().PARAMETRI_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().DATI_CONTROPARTE,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getDatiControparte(), Evento.model().DATI_CONTROPARTE.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_pagamento_portale","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_rpt","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_evento.add(new JDBCObject(id_evento_versamento, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_evento.add(new JDBCObject(id_evento_pagamentoPortale, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_evento.add(new JDBCObject(id_evento_rpt, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_evento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_evento) {
			// Update evento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_evento.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, evento),
				this.getEventoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, evento),
				this.getEventoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, evento),
				this.getEventoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEventoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEventoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEventoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		Long id = evento.getId();
		if(id != null && this.exists(jdbcProperties, log, connection, sqlQueryObject, id)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, evento,idMappingResolutionBehaviour);		
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, evento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, evento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, evento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if(evento.getId()==null){
			throw new Exception("Parameter "+evento.getClass().getName()+".id is null");
		}
		if(evento.getId()<=0){
			throw new Exception("Parameter "+evento.getClass().getName()+".id is less equals 0");
		}
		longId = evento.getId();
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object evento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getEventoFieldConverter().toTable(Evento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete evento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getEventoFieldConverter()));

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
