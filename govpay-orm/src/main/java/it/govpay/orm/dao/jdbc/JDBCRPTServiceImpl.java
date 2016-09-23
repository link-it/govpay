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
import it.govpay.orm.IdRpt;
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

import it.govpay.orm.RPT;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCRPTServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRPTServiceImpl extends JDBCRPTServiceSearchImpl
	implements IJDBCServiceCRUDWithId<RPT, IdRpt, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RPT rpt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

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
		idLogic_versamento = rpt.getIdVersamento();
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

		// Object _canale
		Long id_canale = null;
		it.govpay.orm.IdCanale idLogic_canale = null;
		idLogic_canale = rpt.getIdCanale();
		if(idLogic_canale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_canale = ((JDBCCanaleServiceSearch)(this.getServiceManager().getCanaleServiceSearch())).findTableId(idLogic_canale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_canale = idLogic_canale.getId();
				if(id_canale==null || id_canale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _portale
		Long id_portale = null;
		it.govpay.orm.IdPortale idLogic_portale = null;
		idLogic_portale = rpt.getIdPortale();
		if(idLogic_portale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findTableId(idLogic_portale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_portale = idLogic_portale.getId();
				if(id_portale==null || id_portale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object rpt
		sqlQueryObjectInsert.addInsertTable(this.getRPTFieldConverter().toTable(RPT.model()));
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_CARRELLO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().CCP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_MSG_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().DATA_MSG_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().DESCRIZIONE_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_SESSIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_SESSIONE_PORTALE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().PSP_REDIRECT_URL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().XML_RPT,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().DATA_AGGIORNAMENTO_STATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().CALLBACK_URL,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().MODELLO_PAGAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_MSG_RICEVUTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().DATA_MSG_RICEVUTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().FIRMA_RICEVUTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_ESITO_PAGAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().IMPORTO_TOTALE_PAGATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().XML_RT,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_STAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_TRANSAZIONE_RPT,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getRPTFieldConverter().toColumn(RPT.model().COD_TRANSAZIONE_RT,false),"?");
		sqlQueryObjectInsert.addInsertField("id_versamento","?");
		sqlQueryObjectInsert.addInsertField("id_canale","?");
		sqlQueryObjectInsert.addInsertField("id_portale","?");

		// Insert rpt
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getRPTFetch().getKeyGeneratorObject(RPT.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodCarrello(),RPT.model().COD_CARRELLO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getIuv(),RPT.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCcp(),RPT.model().CCP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodDominio(),RPT.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodMsgRichiesta(),RPT.model().COD_MSG_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getDataMsgRichiesta(),RPT.model().DATA_MSG_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getStato(),RPT.model().STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getDescrizioneStato(),RPT.model().DESCRIZIONE_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodSessione(),RPT.model().COD_SESSIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodSessionePortale(),RPT.model().COD_SESSIONE_PORTALE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getPspRedirectURL(),RPT.model().PSP_REDIRECT_URL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getXmlRPT(),RPT.model().XML_RPT.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getDataAggiornamentoStato(),RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCallbackURL(),RPT.model().CALLBACK_URL.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getModelloPagamento(),RPT.model().MODELLO_PAGAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodMsgRicevuta(),RPT.model().COD_MSG_RICEVUTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getDataMsgRicevuta(),RPT.model().DATA_MSG_RICEVUTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getFirmaRicevuta(),RPT.model().FIRMA_RICEVUTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodEsitoPagamento(),RPT.model().COD_ESITO_PAGAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getImportoTotalePagato(),RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getXmlRT(),RPT.model().XML_RT.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodStazione(),RPT.model().COD_STAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodTransazioneRPT(),RPT.model().COD_TRANSAZIONE_RPT.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(rpt.getCodTransazioneRT(),RPT.model().COD_TRANSAZIONE_RT.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_versamento,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_canale,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_portale,Long.class)
		);
		rpt.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt oldId, RPT rpt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdRPT(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = rpt.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: rpt.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			rpt.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rpt, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, RPT rpt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
			

		// Object _rpt_versamento
		Long id_rpt_versamento = null;
		it.govpay.orm.IdVersamento idLogic_rpt_versamento = null;
		idLogic_rpt_versamento = rpt.getIdVersamento();
		if(idLogic_rpt_versamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rpt_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findTableId(idLogic_rpt_versamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rpt_versamento = idLogic_rpt_versamento.getId();
				if(id_rpt_versamento==null || id_rpt_versamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rpt_canale
		Long id_rpt_canale = null;
		it.govpay.orm.IdCanale idLogic_rpt_canale = null;
		idLogic_rpt_canale = rpt.getIdCanale();
		if(idLogic_rpt_canale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rpt_canale = ((JDBCCanaleServiceSearch)(this.getServiceManager().getCanaleServiceSearch())).findTableId(idLogic_rpt_canale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rpt_canale = idLogic_rpt_canale.getId();
				if(id_rpt_canale==null || id_rpt_canale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _rpt_portale
		Long id_rpt_portale = null;
		it.govpay.orm.IdPortale idLogic_rpt_portale = null;
		idLogic_rpt_portale = rpt.getIdPortale();
		if(idLogic_rpt_portale!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_rpt_portale = ((JDBCPortaleServiceSearch)(this.getServiceManager().getPortaleServiceSearch())).findTableId(idLogic_rpt_portale, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_rpt_portale = idLogic_rpt_portale.getId();
				if(id_rpt_portale==null || id_rpt_portale<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object rpt
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getRPTFieldConverter().toTable(RPT.model()));
		boolean isUpdate_rpt = true;
		java.util.List<JDBCObject> lstObjects_rpt = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_CARRELLO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodCarrello(), RPT.model().COD_CARRELLO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().IUV,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getIuv(), RPT.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().CCP,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCcp(), RPT.model().CCP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_DOMINIO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodDominio(), RPT.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_MSG_RICHIESTA,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodMsgRichiesta(), RPT.model().COD_MSG_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().DATA_MSG_RICHIESTA,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getDataMsgRichiesta(), RPT.model().DATA_MSG_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().STATO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getStato(), RPT.model().STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().DESCRIZIONE_STATO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getDescrizioneStato(), RPT.model().DESCRIZIONE_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_SESSIONE,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodSessione(), RPT.model().COD_SESSIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_SESSIONE_PORTALE,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodSessionePortale(), RPT.model().COD_SESSIONE_PORTALE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().PSP_REDIRECT_URL,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getPspRedirectURL(), RPT.model().PSP_REDIRECT_URL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().XML_RPT,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getXmlRPT(), RPT.model().XML_RPT.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().DATA_AGGIORNAMENTO_STATO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getDataAggiornamentoStato(), RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().CALLBACK_URL,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCallbackURL(), RPT.model().CALLBACK_URL.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().MODELLO_PAGAMENTO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getModelloPagamento(), RPT.model().MODELLO_PAGAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_MSG_RICEVUTA,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodMsgRicevuta(), RPT.model().COD_MSG_RICEVUTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().DATA_MSG_RICEVUTA,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getDataMsgRicevuta(), RPT.model().DATA_MSG_RICEVUTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().FIRMA_RICEVUTA,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getFirmaRicevuta(), RPT.model().FIRMA_RICEVUTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_ESITO_PAGAMENTO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodEsitoPagamento(), RPT.model().COD_ESITO_PAGAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().IMPORTO_TOTALE_PAGATO,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getImportoTotalePagato(), RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().XML_RT,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getXmlRT(), RPT.model().XML_RT.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_STAZIONE,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodStazione(), RPT.model().COD_STAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_TRANSAZIONE_RPT,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodTransazioneRPT(), RPT.model().COD_TRANSAZIONE_RPT.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getRPTFieldConverter().toColumn(RPT.model().COD_TRANSAZIONE_RT,false), "?");
		lstObjects_rpt.add(new JDBCObject(rpt.getCodTransazioneRT(), RPT.model().COD_TRANSAZIONE_RT.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_canale","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_portale","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rpt.add(new JDBCObject(id_rpt_versamento, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rpt.add(new JDBCObject(id_rpt_canale, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_rpt.add(new JDBCObject(id_rpt_portale, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_rpt.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_rpt) {
			// Update rpt
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_rpt.toArray(new JDBCObject[]{}));
		}

	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRPTFieldConverter().toTable(RPT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRPTFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRPTFieldConverter().toTable(RPT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRPTFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRPTFieldConverter().toTable(RPT.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getRPTFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRPTFieldConverter().toTable(RPT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRPTFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRPTFieldConverter().toTable(RPT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRPTFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getRPTFieldConverter().toTable(RPT.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getRPTFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt oldId, RPT rpt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, rpt,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rpt,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, RPT rpt, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, rpt,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, rpt,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RPT rpt) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (rpt.getId()!=null) && (rpt.getId()>0) ){
			longId = rpt.getId();
		}
		else{
			IdRpt idRPT = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,rpt);
			longId = this.findIdRPT(jdbcProperties,log,connection,sqlQueryObject,idRPT,false);
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
		

		// Object rpt
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getRPTFieldConverter().toTable(RPT.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete rpt
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt idRPT) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdRPT(jdbcProperties, log, connection, sqlQueryObject, idRPT, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getRPTFieldConverter()));

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
