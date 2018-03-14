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
import it.govpay.orm.IdPagamento;
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

import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCPagamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCPagamentoServiceImpl extends JDBCPagamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Pagamento, IdPagamento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Pagamento pagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _rpt
		Long id_rpt = null;
		it.govpay.orm.IdRpt idLogic_rpt = null;
		idLogic_rpt = pagamento.getIdRPT();
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

		// Object _singoloVersamento
		Long id_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_singoloVersamento = null;
		idLogic_singoloVersamento = pagamento.getIdSingoloVersamento();
		if(idLogic_singoloVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findTableId(idLogic_singoloVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_singoloVersamento = idLogic_singoloVersamento.getId();
				if(id_singoloVersamento==null || id_singoloVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rr
		Long id_rr = null;
		it.govpay.orm.IdRr idLogic_rr = null;
		idLogic_rr = pagamento.getIdRr();
		if(idLogic_rr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rr = ((JDBCRRServiceSearch)(this.getServiceManager().getRRServiceSearch())).findTableId(idLogic_rr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rr = idLogic_rr.getId();
				if(id_rr==null || id_rr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _incasso
		Long id_incasso = null;
		it.govpay.orm.IdIncasso idLogic_incasso = null;
		idLogic_incasso = pagamento.getIdIncasso();
		if(idLogic_incasso!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_incasso = ((JDBCIncassoServiceSearch)(this.getServiceManager().getIncassoServiceSearch())).findTableId(idLogic_incasso, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_incasso = idLogic_incasso.getId();
				if(id_incasso==null || id_incasso<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object pagamento
		sqlQueryObjectInsert.addInsertTable(this.getPagamentoFieldConverter().toTable(Pagamento.model()));
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().INDICE_DATI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IMPORTO_PAGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATA_ACQUISIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IUR,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATA_PAGAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().COMMISSIONI_PSP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().TIPO_ALLEGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().ALLEGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATA_ACQUISIZIONE_REVOCA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().CAUSALE_REVOCA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATI_REVOCA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IMPORTO_REVOCATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().ESITO_REVOCA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATI_ESITO_REVOCA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_rpt","?");
		sqlQueryObjectInsert.addInsertField("id_singolo_versamento","?");
		sqlQueryObjectInsert.addInsertField("id_rr","?");
		sqlQueryObjectInsert.addInsertField("id_incasso","?");

		// Insert pagamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getPagamentoFetch().getKeyGeneratorObject(Pagamento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getCodDominio(),Pagamento.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getIuv(),Pagamento.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getIndiceDati(),Pagamento.model().INDICE_DATI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getImportoPagato(),Pagamento.model().IMPORTO_PAGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getDataAcquisizione(),Pagamento.model().DATA_ACQUISIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getIur(),Pagamento.model().IUR.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getDataPagamento(),Pagamento.model().DATA_PAGAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getCommissioniPsp(),Pagamento.model().COMMISSIONI_PSP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getTipoAllegato(),Pagamento.model().TIPO_ALLEGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getAllegato(),Pagamento.model().ALLEGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getDataAcquisizioneRevoca(),Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getCausaleRevoca(),Pagamento.model().CAUSALE_REVOCA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getDatiRevoca(),Pagamento.model().DATI_REVOCA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getImportoRevocato(),Pagamento.model().IMPORTO_REVOCATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getEsitoRevoca(),Pagamento.model().ESITO_REVOCA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getDatiEsitoRevoca(),Pagamento.model().DATI_ESITO_REVOCA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getStato(),Pagamento.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(pagamento.getTipo(),Pagamento.model().TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_rpt,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_singoloVersamento,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_rr,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_incasso,Long.class)
		);
		pagamento.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento oldId, Pagamento pagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdPagamento(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = pagamento.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: pagamento.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			pagamento.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, pagamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Pagamento pagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _pagamento_rpt
		Long id_pagamento_rpt = null;
		it.govpay.orm.IdRpt idLogic_pagamento_rpt = null;
		idLogic_pagamento_rpt = pagamento.getIdRPT();
		if(idLogic_pagamento_rpt!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_pagamento_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findTableId(idLogic_pagamento_rpt, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_pagamento_rpt = idLogic_pagamento_rpt.getId();
				if(id_pagamento_rpt==null || id_pagamento_rpt<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _pagamento_singoloVersamento
		Long id_pagamento_singoloVersamento = null;
		it.govpay.orm.IdSingoloVersamento idLogic_pagamento_singoloVersamento = null;
		idLogic_pagamento_singoloVersamento = pagamento.getIdSingoloVersamento();
		if(idLogic_pagamento_singoloVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_pagamento_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findTableId(idLogic_pagamento_singoloVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_pagamento_singoloVersamento = idLogic_pagamento_singoloVersamento.getId();
				if(id_pagamento_singoloVersamento==null || id_pagamento_singoloVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _pagamento_rr
		Long id_pagamento_rr = null;
		it.govpay.orm.IdRr idLogic_pagamento_rr = null;
		idLogic_pagamento_rr = pagamento.getIdRr();
		if(idLogic_pagamento_rr!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_pagamento_rr = ((JDBCRRServiceSearch)(this.getServiceManager().getRRServiceSearch())).findTableId(idLogic_pagamento_rr, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_pagamento_rr = idLogic_pagamento_rr.getId();
				if(id_pagamento_rr==null || id_pagamento_rr<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _pagamento_incasso
		Long id_pagamento_incasso = null;
		it.govpay.orm.IdIncasso idLogic_pagamento_incasso = null;
		idLogic_pagamento_incasso = pagamento.getIdIncasso();
		if(idLogic_pagamento_incasso!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_pagamento_incasso = ((JDBCIncassoServiceSearch)(this.getServiceManager().getIncassoServiceSearch())).findTableId(idLogic_pagamento_incasso, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_pagamento_incasso = idLogic_pagamento_incasso.getId();
				if(id_pagamento_incasso==null || id_pagamento_incasso<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object pagamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getPagamentoFieldConverter().toTable(Pagamento.model()));
		boolean isUpdate_pagamento = true;
		java.util.List<JDBCObject> lstObjects_pagamento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().COD_DOMINIO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getCodDominio(), Pagamento.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IUV,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getIuv(), Pagamento.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().INDICE_DATI,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getIndiceDati(), Pagamento.model().INDICE_DATI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IMPORTO_PAGATO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getImportoPagato(), Pagamento.model().IMPORTO_PAGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATA_ACQUISIZIONE,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getDataAcquisizione(), Pagamento.model().DATA_ACQUISIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IUR,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getIur(), Pagamento.model().IUR.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATA_PAGAMENTO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getDataPagamento(), Pagamento.model().DATA_PAGAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().COMMISSIONI_PSP,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getCommissioniPsp(), Pagamento.model().COMMISSIONI_PSP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().TIPO_ALLEGATO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getTipoAllegato(), Pagamento.model().TIPO_ALLEGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().ALLEGATO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getAllegato(), Pagamento.model().ALLEGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATA_ACQUISIZIONE_REVOCA,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getDataAcquisizioneRevoca(), Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().CAUSALE_REVOCA,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getCausaleRevoca(), Pagamento.model().CAUSALE_REVOCA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATI_REVOCA,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getDatiRevoca(), Pagamento.model().DATI_REVOCA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().IMPORTO_REVOCATO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getImportoRevocato(), Pagamento.model().IMPORTO_REVOCATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().ESITO_REVOCA,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getEsitoRevoca(), Pagamento.model().ESITO_REVOCA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().DATI_ESITO_REVOCA,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getDatiEsitoRevoca(), Pagamento.model().DATI_ESITO_REVOCA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().STATO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getStato(), Pagamento.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getPagamentoFieldConverter().toColumn(Pagamento.model().TIPO,false), "?");
		lstObjects_pagamento.add(new JDBCObject(pagamento.getTipo(), Pagamento.model().TIPO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_rpt","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_singolo_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_rr","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_incasso","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_pagamento.add(new JDBCObject(id_pagamento_rpt, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_pagamento.add(new JDBCObject(id_pagamento_singoloVersamento, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_pagamento.add(new JDBCObject(id_pagamento_rr, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_pagamento.add(new JDBCObject(id_pagamento_incasso, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_pagamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_pagamento) {
			// Update pagamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_pagamento.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter().toTable(Pagamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPagamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter().toTable(Pagamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPagamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter().toTable(Pagamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getPagamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter().toTable(Pagamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPagamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter().toTable(Pagamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPagamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getPagamentoFieldConverter().toTable(Pagamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getPagamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento oldId, Pagamento pagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, pagamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, pagamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Pagamento pagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, pagamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, pagamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Pagamento pagamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (pagamento.getId()!=null) && (pagamento.getId()>0) ){
			longId = pagamento.getId();
		}
		else{
			IdPagamento idPagamento = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,pagamento);
			longId = this.findIdPagamento(jdbcProperties,log,connection,sqlQueryObject,idPagamento,false);
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
		

		// Object pagamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getPagamentoFieldConverter().toTable(Pagamento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete pagamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento idPagamento) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdPagamento(jdbcProperties, log, connection, sqlQueryObject, idPagamento, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getPagamentoFieldConverter()));

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
