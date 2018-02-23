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
import it.govpay.orm.IdPagamentoPortale;
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

import it.govpay.orm.PagamentoPortale;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCPagamentoPortaleServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPagamentoPortaleServiceImpl extends JDBCPagamentoPortaleServiceSearchImpl
	implements IJDBCServiceCRUDWithId<PagamentoPortale, IdPagamentoPortale, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, PagamentoPortale pagamentoPortale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object pagamentoPortale
		sqlQueryObjectInsert.addInsertTable(this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()));
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().COD_PORTALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().COD_CANALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().NOME,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().IMPORTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().VERSANTE_IDENTIFICATIVO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().ID_SESSIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().ID_SESSIONE_PORTALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().ID_SESSIONE_PSP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().CODICE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().PSP_REDIRECT_URL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().PSP_ESITO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().JSON_REQUEST,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_ID_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_KEY_PA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_KEY_WISP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_HTML,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().DATA_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().URL_RITORNO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().COD_PSP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().TIPO_VERSAMENTO,false),"?");

		// Insert pagamentoPortale
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getPagamentoPortaleFetch().getKeyGeneratorObject(PagamentoPortale.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getCodPortale(),PagamentoPortale.model().COD_PORTALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getCodCanale(),PagamentoPortale.model().COD_CANALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getNome(),PagamentoPortale.model().NOME.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getImporto(),PagamentoPortale.model().IMPORTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getVersanteIdentificativo(),PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getIdSessione(),PagamentoPortale.model().ID_SESSIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getIdSessionePortale(),PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getIdSessionePsp(),PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getStato(),PagamentoPortale.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getCodiceStato(),PagamentoPortale.model().CODICE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getDescrizioneStato(),PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getPspRedirectURL(),PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getPspEsito(),PagamentoPortale.model().PSP_ESITO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getJsonRequest(),PagamentoPortale.model().JSON_REQUEST.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getWispIdDominio(),PagamentoPortale.model().WISP_ID_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getWispKeyPA(),PagamentoPortale.model().WISP_KEY_PA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getWispKeyWisp(),PagamentoPortale.model().WISP_KEY_WISP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getWispHtml(),PagamentoPortale.model().WISP_HTML.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getDataRichiesta(),PagamentoPortale.model().DATA_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getUrlRitorno(),PagamentoPortale.model().URL_RITORNO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getCodPsp(),PagamentoPortale.model().COD_PSP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamentoPortale.getTipoVersamento(),PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType())
		);
		pagamentoPortale.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale oldId, PagamentoPortale pagamentoPortale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdPagamentoPortale(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = pagamentoPortale.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: pagamentoPortale.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			pagamentoPortale.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, pagamentoPortale, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, PagamentoPortale pagamentoPortale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			


		// Object pagamentoPortale
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()));
		boolean isUpdate_pagamentoPortale = true;
		java.util.List<JDBCObject> lstObjects_pagamentoPortale = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().COD_PORTALE,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getCodPortale(), PagamentoPortale.model().COD_PORTALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().COD_CANALE,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getCodCanale(), PagamentoPortale.model().COD_CANALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().NOME,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getNome(), PagamentoPortale.model().NOME.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().IMPORTO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getImporto(), PagamentoPortale.model().IMPORTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().VERSANTE_IDENTIFICATIVO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getVersanteIdentificativo(), PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().ID_SESSIONE,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getIdSessione(), PagamentoPortale.model().ID_SESSIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().ID_SESSIONE_PORTALE,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getIdSessionePortale(), PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().ID_SESSIONE_PSP,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getIdSessionePsp(), PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().STATO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getStato(), PagamentoPortale.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().CODICE_STATO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getCodiceStato(), PagamentoPortale.model().CODICE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getDescrizioneStato(), PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().PSP_REDIRECT_URL,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getPspRedirectURL(), PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().PSP_ESITO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getPspEsito(), PagamentoPortale.model().PSP_ESITO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().JSON_REQUEST,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getJsonRequest(), PagamentoPortale.model().JSON_REQUEST.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_ID_DOMINIO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getWispIdDominio(), PagamentoPortale.model().WISP_ID_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_KEY_PA,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getWispKeyPA(), PagamentoPortale.model().WISP_KEY_PA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_KEY_WISP,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getWispKeyWisp(), PagamentoPortale.model().WISP_KEY_WISP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().WISP_HTML,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getWispHtml(), PagamentoPortale.model().WISP_HTML.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().DATA_RICHIESTA,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getDataRichiesta(), PagamentoPortale.model().DATA_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().URL_RITORNO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getUrlRitorno(), PagamentoPortale.model().URL_RITORNO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().COD_PSP,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getCodPsp(), PagamentoPortale.model().COD_PSP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoPortaleFieldConverter().toColumn(PagamentoPortale.model().TIPO_VERSAMENTO,false), "?");
		lstObjects_pagamentoPortale.add(new JDBCObject(pagamentoPortale.getTipoVersamento(), PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_pagamentoPortale.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_pagamentoPortale) {
			// Update pagamentoPortale
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_pagamentoPortale.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPagamentoPortaleFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPagamentoPortaleFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPagamentoPortaleFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPagamentoPortaleFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPagamentoPortaleFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPagamentoPortaleFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale oldId, PagamentoPortale pagamentoPortale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, pagamentoPortale,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, pagamentoPortale,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, PagamentoPortale pagamentoPortale, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, pagamentoPortale,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, pagamentoPortale,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, PagamentoPortale pagamentoPortale) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (pagamentoPortale.getId()!=null) && (pagamentoPortale.getId()>0) ){
			longId = pagamentoPortale.getId();
		}
		else{
			IdPagamentoPortale idPagamentoPortale = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,pagamentoPortale);
			longId = this.findIdPagamentoPortale(jdbcProperties,log,connection,sqlQueryObject,idPagamentoPortale,false);
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
		

		// Object pagamentoPortale
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getPagamentoPortaleFieldConverter().toTable(PagamentoPortale.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete pagamentoPortale
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamentoPortale idPagamentoPortale) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdPagamentoPortale(jdbcProperties, log, connection, sqlQueryObject, idPagamentoPortale, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getPagamentoPortaleFieldConverter()));

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
