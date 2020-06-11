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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.InUse;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.Union;
import org.openspcoop2.generic_project.beans.UnionExpression;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceSearchWithId;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.dao.jdbc.utils.IJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.generic_project.utils.UtilsTemplate;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;

import it.govpay.orm.IdRpt;
import it.govpay.orm.VistaRptVersamento;
import it.govpay.orm.dao.jdbc.converter.VistaRptVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.VistaRptVersamentoFetch;
import it.govpay.orm.model.VistaRptVersamentoModel;

/**     
 * JDBCVistaRptVersamentoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVistaRptVersamentoServiceSearchImpl implements IJDBCServiceSearchWithId<VistaRptVersamento, IdRpt, JDBCServiceManager> {

	private VistaRptVersamentoFieldConverter _vistaRptVersamentoFieldConverter = null;
	public VistaRptVersamentoFieldConverter getVistaRptVersamentoFieldConverter() {
		if(this._vistaRptVersamentoFieldConverter==null){
			this._vistaRptVersamentoFieldConverter = new VistaRptVersamentoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._vistaRptVersamentoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getVistaRptVersamentoFieldConverter();
	}
	
	private VistaRptVersamentoFetch vistaRptVersamentoFetch = new VistaRptVersamentoFetch();
	public VistaRptVersamentoFetch getVistaRptVersamentoFetch() {
		return this.vistaRptVersamentoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getVistaRptVersamentoFetch();
	}
	
	
	private JDBCServiceManager jdbcServiceManager = null;

	@Override
	public void setServiceManager(JDBCServiceManager serviceManager) throws ServiceException{
		this.jdbcServiceManager = serviceManager;
	}
	
	@Override
	public JDBCServiceManager getServiceManager() throws ServiceException{
		return this.jdbcServiceManager;
	}
	

	@Override
	public IdRpt convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaRptVersamento vistaRptVersamento) throws NotImplementedException, ServiceException, Exception{
	
		IdRpt idVistaRptVersamento = new IdRpt();
		idVistaRptVersamento.setCodMsgRichiesta(vistaRptVersamento.getCodMsgRichiesta());
		return idVistaRptVersamento;
	}
	
	@Override
	public VistaRptVersamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_vistaRptVersamento = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdVistaRptVersamento(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_vistaRptVersamento,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_vistaRptVersamento = this.findIdVistaRptVersamento(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_vistaRptVersamento != null && id_vistaRptVersamento > 0;
		
	}
	
	@Override
	public List<IdRpt> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {
		// default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
        
		List<IdRpt> list = new ArrayList<IdRpt>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(VistaRptVersamento.model().COD_MSG_RICHIESTA);

        
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (VistaRptVersamento)this.getVistaRptVersamentoFetch().fetch(jdbcProperties.getDatabase(), VistaRptVersamento.model(), map)));
        }
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	private Long getNullableValueFromMap(String key, Map<String, Object> map) {
		if(map.containsKey(key)) {
			Object idObj = map.remove(key);
			if(idObj instanceof Long) {
				return (Long) idObj;
			}
		}

		return null;
	}
	
	@Override
	public List<VistaRptVersamento> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
        
        List<VistaRptVersamento> list = new ArrayList<VistaRptVersamento>();
        
        try{

			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(new CustomField("id_pagamento_portale", Long.class, "id_pagamento_portale", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento_dominio", Long.class, "vrs_id_tipo_versamento_dominio", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento", Long.class, "vrs_id_tipo_versamento", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(new CustomField("vrs_id_dominio", Long.class, "vrs_id_dominio", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(new CustomField("vrs_id_uo", Long.class, "vrs_id_uo", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(new CustomField("vrs_id_applicazione", Long.class, "vrs_id_applicazione", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(new CustomField("vrs_id_documento", Long.class, "vrs_id_documento", this.getFieldConverter().toTable(VistaRptVersamento.model())));
			fields.add(VistaRptVersamento.model().COD_CARRELLO);
			fields.add(VistaRptVersamento.model().IUV);
			fields.add(VistaRptVersamento.model().CCP);
			fields.add(VistaRptVersamento.model().COD_DOMINIO);
			fields.add(VistaRptVersamento.model().COD_MSG_RICHIESTA);
			fields.add(VistaRptVersamento.model().DATA_MSG_RICHIESTA);
			fields.add(VistaRptVersamento.model().STATO);
			fields.add(VistaRptVersamento.model().DESCRIZIONE_STATO);
			fields.add(VistaRptVersamento.model().COD_SESSIONE);
			fields.add(VistaRptVersamento.model().COD_SESSIONE_PORTALE);
			fields.add(VistaRptVersamento.model().PSP_REDIRECT_URL);
			fields.add(VistaRptVersamento.model().XML_RPT);
			fields.add(VistaRptVersamento.model().DATA_AGGIORNAMENTO_STATO);
			fields.add(VistaRptVersamento.model().CALLBACK_URL);
			fields.add(VistaRptVersamento.model().MODELLO_PAGAMENTO);
			fields.add(VistaRptVersamento.model().CALLBACK_URL);
			fields.add(VistaRptVersamento.model().COD_MSG_RICEVUTA);
			fields.add(VistaRptVersamento.model().DATA_MSG_RICEVUTA);
			fields.add(VistaRptVersamento.model().COD_ESITO_PAGAMENTO);
			fields.add(VistaRptVersamento.model().IMPORTO_TOTALE_PAGATO);
			fields.add(VistaRptVersamento.model().XML_RT);
			fields.add(VistaRptVersamento.model().COD_CANALE);
			fields.add(VistaRptVersamento.model().COD_PSP);
			fields.add(VistaRptVersamento.model().COD_INTERMEDIARIO_PSP);
			fields.add(VistaRptVersamento.model().TIPO_VERSAMENTO);
			fields.add(VistaRptVersamento.model().TIPO_IDENTIFICATIVO_ATTESTANTE);
			fields.add(VistaRptVersamento.model().IDENTIFICATIVO_ATTESTANTE);
			fields.add(VistaRptVersamento.model().DENOMINAZIONE_ATTESTANTE);
			fields.add(VistaRptVersamento.model().COD_STAZIONE);
			fields.add(VistaRptVersamento.model().COD_TRANSAZIONE_RPT);
			fields.add(VistaRptVersamento.model().COD_TRANSAZIONE_RT);
			fields.add(VistaRptVersamento.model().STATO_CONSERVAZIONE);
			fields.add(VistaRptVersamento.model().DESCRIZIONE_STATO_CONS);
			fields.add(VistaRptVersamento.model().DATA_CONSERVAZIONE);
			fields.add(VistaRptVersamento.model().BLOCCANTE);
			
			fields.add(VistaRptVersamento.model().VRS_ID);
			fields.add(VistaRptVersamento.model().VRS_COD_VERSAMENTO_ENTE);
			fields.add(VistaRptVersamento.model().VRS_NOME);
			fields.add(VistaRptVersamento.model().VRS_IMPORTO_TOTALE);
			fields.add(VistaRptVersamento.model().VRS_STATO_VERSAMENTO);
			fields.add(VistaRptVersamento.model().VRS_DESCRIZIONE_STATO);
			fields.add(VistaRptVersamento.model().VRS_AGGIORNABILE);
			fields.add(VistaRptVersamento.model().VRS_DATA_CREAZIONE);
			fields.add(VistaRptVersamento.model().VRS_DATA_VALIDITA);
			fields.add(VistaRptVersamento.model().VRS_DATA_SCADENZA);
			fields.add(VistaRptVersamento.model().VRS_DATA_ORA_ULTIMO_AGG);
			fields.add(VistaRptVersamento.model().VRS_CAUSALE_VERSAMENTO);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_TIPO);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_IDENTIFICATIVO);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_ANAGRAFICA);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_INDIRIZZO);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_CIVICO);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_CAP);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_LOCALITA);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_PROVINCIA);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_NAZIONE);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_EMAIL);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_TELEFONO);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_CELLULARE);
			fields.add(VistaRptVersamento.model().VRS_DEBITORE_FAX);
			fields.add(VistaRptVersamento.model().VRS_TASSONOMIA_AVVISO);
			fields.add(VistaRptVersamento.model().VRS_TASSONOMIA);
			fields.add(VistaRptVersamento.model().VRS_COD_LOTTO);
			fields.add(VistaRptVersamento.model().VRS_COD_VERSAMENTO_LOTTO);
			fields.add(VistaRptVersamento.model().VRS_COD_ANNO_TRIBUTARIO);
			fields.add(VistaRptVersamento.model().VRS_COD_BUNDLEKEY);
			fields.add(VistaRptVersamento.model().VRS_DATI_ALLEGATI);
			fields.add(VistaRptVersamento.model().VRS_INCASSO);
			fields.add(VistaRptVersamento.model().VRS_ANOMALIE);
			fields.add(VistaRptVersamento.model().VRS_IUV_VERSAMENTO);
			fields.add(VistaRptVersamento.model().VRS_NUMERO_AVVISO);
			fields.add(VistaRptVersamento.model().VRS_ACK);
			fields.add(VistaRptVersamento.model().VRS_ANOMALO);
			fields.add(VistaRptVersamento.model().VRS_DIVISIONE);
			fields.add(VistaRptVersamento.model().VRS_DIREZIONE);
			fields.add(VistaRptVersamento.model().VRS_ID_SESSIONE); 
			fields.add(VistaRptVersamento.model().VRS_DATA_PAGAMENTO);
			fields.add(VistaRptVersamento.model().VRS_IMPORTO_PAGATO);
			fields.add(VistaRptVersamento.model().VRS_IMPORTO_INCASSATO);
			fields.add(VistaRptVersamento.model().VRS_STATO_PAGAMENTO);
			fields.add(VistaRptVersamento.model().VRS_IUV_PAGAMENTO);
			fields.add(VistaRptVersamento.model().VRS_SRC_DEBITORE_IDENTIFICATIVO);
			fields.add(VistaRptVersamento.model().VRS_COD_RATA);
			
        
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Long idPagamentoPortale = this.getNullableValueFromMap("id_pagamento_portale", map);

				VistaRptVersamento vistaRptVersamento = (VistaRptVersamento)this.getVistaRptVersamentoFetch().fetch(jdbcProperties.getDatabase(), VistaRptVersamento.model(), map);
				
				Long idApplicazione = (Long)map.remove("vrs_id_applicazione");
				Long idDominio = (Long)map.remove("vrs_id_dominio");
				
				Long idUO = null;
				Object idUoObject = map.remove("vrs_id_uo");
				if(idUoObject instanceof Long) {
					idUO = (Long) idUoObject;
				}
				Long idTipoVersamento = null;
				Object idTipoVersamentoObject = map.remove("vrs_id_tipo_versamento");
				if(idTipoVersamentoObject instanceof Long) {
					idTipoVersamento = (Long) idTipoVersamentoObject;
				}
				Long idTipoVersamentoDominio = null;
				Object idTipoVersamentoDominioObject = map.remove("vrs_id_tipo_versamento_dominio");
				if(idTipoVersamentoDominioObject instanceof Long) {
					idTipoVersamentoDominio = (Long) idTipoVersamentoDominioObject;
				}
				
				Long idDocumento = null;
				Object idDocumentoObject = map.remove("vrs_id_documento");
				if(idDocumentoObject instanceof Long) {
					idDocumento = (Long) idDocumentoObject;
				}
				
				it.govpay.orm.IdApplicazione id_versamento_applicazione = null;
				if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
					id_versamento_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idApplicazione, false);
				}else{
					id_versamento_applicazione = new it.govpay.orm.IdApplicazione();
				}
				id_versamento_applicazione.setId(idApplicazione);
				vistaRptVersamento.setVrsIdApplicazione(id_versamento_applicazione);

				it.govpay.orm.IdDominio id_versamento_dominio = null;
				if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
					id_versamento_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findId(idDominio, false);
				}else{
					id_versamento_dominio = new it.govpay.orm.IdDominio();
				}
				id_versamento_dominio.setId(idDominio);
				vistaRptVersamento.setVrsIdDominio(id_versamento_dominio);

				if(idUO != null) {
					it.govpay.orm.IdUo id_versamento_ente = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_ente = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findId(idUO, false);
					}else{
						id_versamento_ente = new it.govpay.orm.IdUo();
					}
					id_versamento_ente.setId(idUO);
					vistaRptVersamento.setVrsIdUo(id_versamento_ente);
				}

				if(idTipoVersamento != null) {
					it.govpay.orm.IdTipoVersamento id_versamento_tipoVersamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_tipoVersamento = ((JDBCTipoVersamentoServiceSearch)(this.getServiceManager().getTipoVersamentoServiceSearch())).findId(idTipoVersamento, false);
					}else{
						id_versamento_tipoVersamento = new it.govpay.orm.IdTipoVersamento();
					}
					id_versamento_tipoVersamento.setId(idTipoVersamento);
					vistaRptVersamento.setVrsIdTipoVersamento(id_versamento_tipoVersamento);
				}
				
				if(idTipoVersamentoDominio != null) {
					it.govpay.orm.IdTipoVersamentoDominio id_versamento_tipoVersamentoDominio = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_tipoVersamentoDominio = ((JDBCTipoVersamentoDominioServiceSearch)(this.getServiceManager().getTipoVersamentoDominioServiceSearch())).findId(idTipoVersamentoDominio, false);
					}else{
						id_versamento_tipoVersamentoDominio = new it.govpay.orm.IdTipoVersamentoDominio();
					}
					id_versamento_tipoVersamentoDominio.setId(idTipoVersamentoDominio);
					vistaRptVersamento.setVrsIdTipoVersamentoDominio(id_versamento_tipoVersamentoDominio);
				}
				
				if(idPagamentoPortale != null && idPagamentoPortale > 0){
					it.govpay.orm.IdPagamentoPortale id_rpt_idPagamentoPortale = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_rpt_idPagamentoPortale = ((JDBCPagamentoPortaleServiceSearch)(this.getServiceManager().getPagamentoPortaleServiceSearch())).findId(idPagamentoPortale, false);
					}else{
						id_rpt_idPagamentoPortale = new it.govpay.orm.IdPagamentoPortale();
					}
					id_rpt_idPagamentoPortale.setId(idPagamentoPortale);
					vistaRptVersamento.setIdPagamentoPortale(id_rpt_idPagamentoPortale);
				}
				
				if(idDocumento != null && idDocumento > 0) {
					it.govpay.orm.IdDocumento id_rpt_documentoVersamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_rpt_documentoVersamento = ((JDBCDocumentoServiceSearch)(this.getServiceManager().getDocumentoServiceSearch())).findId(idDocumento, false);
					}else{
						id_rpt_documentoVersamento = new it.govpay.orm.IdDocumento();
					}
					id_rpt_documentoVersamento.setId(idDocumento);
					vistaRptVersamento.setVrsIdDocumento(id_rpt_documentoVersamento);
				}

				list.add(vistaRptVersamento);
			}
		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public VistaRptVersamento find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		VistaRptVersamentoModel model = VistaRptVersamento.model();

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		pagExpr.offset(0);
		pagExpr.limit(2);
		pagExpr.addOrder(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(model)), SortOrder.ASC);
		
		List<VistaRptVersamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model());
		
		sqlQueryObject.addSelectCountField(this.getVistaRptVersamentoFieldConverter().toTable(VistaRptVersamento.model())+".id","tot",true);
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_vistaRptVersamento = this.findIdVistaRptVersamento(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_vistaRptVersamento);
		
	}

	@Override
	public List<Object> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, IField field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this.select(jdbcProperties, log, connection, sqlQueryObject,
								paginatedExpression, false, field);
	}
	
	@Override
	public List<Object> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, boolean distinct, IField field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		List<Map<String,Object>> map = 
			this.select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, distinct, new IField[]{field});
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.selectSingleObject(map);
	}
	
	@Override
	public List<Map<String,Object>> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, IField ... field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this.select(jdbcProperties, log, connection, sqlQueryObject,
								paginatedExpression, false, field);
	}
	
	@Override
	public List<Map<String,Object>> select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, boolean distinct, IField ... field) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,paginatedExpression,field);
		try{
		
			ISQLQueryObject sqlQueryObjectDistinct = 
						org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(distinct,sqlQueryObject, paginatedExpression, log,
												this.getVistaRptVersamentoFieldConverter(), field);

			return _select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, sqlQueryObjectDistinct);
			
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,field);
		}
	}

	@Override
	public Object aggregate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		Map<String,Object> map = 
			this.aggregate(jdbcProperties, log, connection, sqlQueryObject, expression, new FunctionField[]{functionField});
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.selectAggregateObject(map,functionField);
	}
	
	@Override
	public Map<String,Object> aggregate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {													
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,expression,functionField);
		try{
			List<Map<String,Object>> list = _select(jdbcProperties, log, connection, sqlQueryObject, expression);
			return list.get(0);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,expression,functionField);
		}
	}

	@Override
	public List<Map<String,Object>> groupBy(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		if(expression.getGroupByFields().size()<=0){
			throw new ServiceException("GroupBy conditions not found in expression");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,expression,functionField);
		try{
			return _select(jdbcProperties, log, connection, sqlQueryObject, expression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,expression,functionField);
		}
	}
	

	@Override
	public List<Map<String,Object>> groupBy(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
													JDBCPaginatedExpression paginatedExpression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		if(paginatedExpression.getGroupByFields().size()<=0){
			throw new ServiceException("GroupBy conditions not found in expression");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.setFields(sqlQueryObject,paginatedExpression,functionField);
		try{
			return _select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,functionField);
		}
	}
	
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return _select(jdbcProperties, log, connection, sqlQueryObject, expression, null);
	}
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression, ISQLQueryObject sqlQueryObjectDistinct) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		List<Object> listaQuery = new ArrayList<Object>();
		List<JDBCObject> listaParams = new ArrayList<JDBCObject>();
		List<Object> returnField = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSelect(jdbcProperties, log, connection, sqlQueryObject, 
        						expression, this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(),
        								listaQuery,listaParams,returnField);
		if(list!=null && list.size()>0){
			return list;
		}
		else{
			throw new NotFoundException("Not Found");
		}
	}
	
	@Override
	public List<Map<String,Object>> union(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException,Exception {		
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<ISQLQueryObject>();
		List<JDBCObject> jdbcObjects = new ArrayList<JDBCObject>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnion(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(), 
        								sqlQueryObjectInnerList, jdbcObjects, returnClassTypes, union, unionExpression);
        if(list!=null && list.size()>0){
			return list;
		}
		else{
			throw new NotFoundException("Not Found");
		}								
	}
	
	@Override
	public NonNegativeNumber unionCount(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException,Exception {		
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<ISQLQueryObject>();
		List<JDBCObject> jdbcObjects = new ArrayList<JDBCObject>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnionCount(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(), 
        								sqlQueryObjectInnerList, jdbcObjects, returnClassTypes, union, unionExpression);
        if(number!=null && number.longValue()>=0){
			return number;
		}
		else{
			throw new NotFoundException("Not Found");
		}
	}



	// -- ConstructorExpression	

	@Override
	public JDBCExpression newExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCExpression(this.getVistaRptVersamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getVistaRptVersamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public JDBCExpression toExpression(JDBCPaginatedExpression paginatedExpression, Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCExpression(paginatedExpression);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public JDBCPaginatedExpression toPaginatedExpression(JDBCExpression expression, Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(expression);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	
	
	// -- DB

	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id, VistaRptVersamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, VistaRptVersamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaRptVersamento obj, VistaRptVersamento imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getIdPagamentoPortale()!=null && 
				imgSaved.getIdPagamentoPortale()!=null){
			obj.getIdPagamentoPortale().setId(imgSaved.getIdPagamentoPortale().getId());
			if(obj.getIdPagamentoPortale().getIdApplicazione()!=null && 
					imgSaved.getIdPagamentoPortale().getIdApplicazione()!=null){
				obj.getIdPagamentoPortale().getIdApplicazione().setId(imgSaved.getIdPagamentoPortale().getIdApplicazione().getId());
			}
		}
		if(obj.getVrsIdTipoVersamentoDominio()!=null && 
				imgSaved.getVrsIdTipoVersamentoDominio()!=null){
			obj.getVrsIdTipoVersamentoDominio().setId(imgSaved.getVrsIdTipoVersamentoDominio().getId());
			if(obj.getVrsIdTipoVersamentoDominio().getIdDominio()!=null && 
					imgSaved.getVrsIdTipoVersamentoDominio().getIdDominio()!=null){
				obj.getVrsIdTipoVersamentoDominio().getIdDominio().setId(imgSaved.getVrsIdTipoVersamentoDominio().getIdDominio().getId());
			}
			if(obj.getVrsIdTipoVersamentoDominio().getIdTipoVersamento()!=null && 
					imgSaved.getVrsIdTipoVersamentoDominio().getIdTipoVersamento()!=null){
				obj.getVrsIdTipoVersamentoDominio().getIdTipoVersamento().setId(imgSaved.getVrsIdTipoVersamentoDominio().getIdTipoVersamento().getId());
			}
		}
		if(obj.getVrsIdTipoVersamento()!=null && 
				imgSaved.getVrsIdTipoVersamento()!=null){
			obj.getVrsIdTipoVersamento().setId(imgSaved.getVrsIdTipoVersamento().getId());
		}
		if(obj.getVrsIdDominio()!=null && 
				imgSaved.getVrsIdDominio()!=null){
			obj.getVrsIdDominio().setId(imgSaved.getVrsIdDominio().getId());
		}
		if(obj.getVrsIdUo()!=null && 
				imgSaved.getVrsIdUo()!=null){
			obj.getVrsIdUo().setId(imgSaved.getVrsIdUo().getId());
			if(obj.getVrsIdUo().getIdDominio()!=null && 
					imgSaved.getVrsIdUo().getIdDominio()!=null){
				obj.getVrsIdUo().getIdDominio().setId(imgSaved.getVrsIdUo().getIdDominio().getId());
			}
		}
		if(obj.getVrsIdApplicazione()!=null && 
				imgSaved.getVrsIdApplicazione()!=null){
			obj.getVrsIdApplicazione().setId(imgSaved.getVrsIdApplicazione().getId());
		}

	}
	
	@Override
	public VistaRptVersamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private VistaRptVersamento _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		IField idField = new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaRptVersamento.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<VistaRptVersamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
		if(lst.size() <=0)
			throw new NotFoundException("Id ["+tableId+"]");
				
		if(lst.size() > 1)
			throw new MultipleResultException("Id ["+tableId+"]");
		

		return lst.get(0);
	
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._exists(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
	
	private boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
					new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
				
		boolean existsVistaRptVersamento = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getVistaRptVersamentoFieldConverter().toTable(VistaRptVersamento.model()));
		sqlQueryObject.addSelectField(this.getVistaRptVersamentoFieldConverter().toColumn(VistaRptVersamento.model().COD_CARRELLO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists vistaRptVersamento
		existsVistaRptVersamento = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsVistaRptVersamento;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		String tableRpt = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model());
		String tablePagamentiPortale = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE);
		String tableApplicazioni = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_APPLICAZIONE);
		String tableTipiVersamento = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO);
		String tableTipiVersamentoDominio = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO);
		String tableDomini = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_DOMINIO);
		String tableUO = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_UO);
		String tableDocumenti = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_DOCUMENTO);
	
		if(expression.inUseModel(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".id_pagamento_portale="+tablePagamentiPortale+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE,false)){
			if(!expression.inUseModel(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE,false)){
				sqlQueryObject.addFromTable(tablePagamentiPortale);
				sqlQueryObject.addWhereCondition(tableRpt+".id_pagamento_portale="+tablePagamentiPortale+".id");
			}
			
			String tableApplicazioni2 = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tablePagamentiPortale+".id_applicazione="+tableApplicazioni2+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_APPLICAZIONE,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_applicazione="+tableApplicazioni+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento="+tableTipiVersamento+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_dominio="+tableDomini+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_UO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_uo="+tableUO+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_DOCUMENTO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_documento="+tableDocumenti+".id");
		}
	
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaRptVersamento.model().VRS_ID_UO,false)){
				sqlQueryObject.addFromTable(tableUO);
				sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_uo="+tableUO+".id");
			}

			String tableDomini2 = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableUO+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableDomini2 = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO,false)){
			if(!expression.inUseModel(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableTipiVeramento2 = this.getVistaRptVersamentoFieldConverter().toAliasTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_tipo_versamento="+tableTipiVeramento2+".id");
		}
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdVistaRptVersamento(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		VistaRptVersamentoFieldConverter converter = this.getVistaRptVersamentoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// VistaRptVersamento.model()
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model()))
			));

		// VistaRptVersamento.model().ID_PAGAMENTO_PORTALE
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE))
			));

		// VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE))
			));

		// VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO))
			));

		// VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO))
			));

		// VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO))
			));

		// VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_TIPO_VERSAMENTO))
			));

		// VistaRptVersamento.model().VRS_ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_DOMINIO))
			));

		// VistaRptVersamento.model().VRS_ID_UO
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_UO))
			));

		// VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_UO.ID_DOMINIO))
			));

		// VistaRptVersamento.model().VRS_ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VistaRptVersamento.model().VRS_ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRptVersamento.model().VRS_ID_APPLICAZIONE))
			));

		return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaRptVersamentoFieldConverter().toTable(VistaRptVersamento.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaRptVersamentoFieldConverter().toTable(VistaRptVersamento.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getVistaRptVersamentoFieldConverter(), VistaRptVersamento.model(), objectIdClass, listaQuery);
		if(res!=null && (((Long) res).longValue()>0) ){
			return ((Long) res).longValue();
		}
		else{
			throw new NotFoundException("Not Found");
		}
		
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {
		return this._inUse(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}

	private InUse _inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {

		InUse inUse = new InUse();
		inUse.setInUse(false);
		
		// Delete this line when you have implemented the method
		int throwNotImplemented = 1;
		if(throwNotImplemented==1){
		        throw new NotImplementedException("NotImplemented");
		}
		// Delete this line when you have implemented the method

        return inUse;

	}
	
	@Override
	public IdRpt findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaRptVersamento
		sqlQueryObjectGet.addFromTable(this.getVistaRptVersamentoFieldConverter().toTable(VistaRptVersamento.model()));
		sqlQueryObjectGet.addSelectField(this.getFieldConverter().toColumn(VistaRptVersamento.model().COD_MSG_RICHIESTA,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaRptVersamento
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaRptVersamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_vistaRptVersamento = new ArrayList<Class<?>>();
		listaFieldIdReturnType_vistaRptVersamento.add(VistaRptVersamento.model().COD_MSG_RICHIESTA.getFieldType());
		it.govpay.orm.IdRpt id_vistaRptVersamento = null;
		List<Object> listaFieldId_vistaRptVersamento = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_vistaRptVersamento, searchParams_vistaRptVersamento);
		if(listaFieldId_vistaRptVersamento==null || listaFieldId_vistaRptVersamento.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _vistaRptVersamento
			id_vistaRptVersamento = new it.govpay.orm.IdRpt();
			id_vistaRptVersamento.setCodMsgRichiesta((String) listaFieldId_vistaRptVersamento.get(0));
		}
		
		return id_vistaRptVersamento;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdVistaRptVersamento(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdVistaRptVersamento(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRpt id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaRptVersamento
		sqlQueryObjectGet.addFromTable(this.getVistaRptVersamentoFieldConverter().toTable(VistaRptVersamento.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition(this.getFieldConverter().toColumn(VistaRptVersamento.model().COD_MSG_RICHIESTA,true)+"=?");

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaRptVersamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getCodMsgRichiesta(),VistaRptVersamento.model().COD_MSG_RICHIESTA.getFieldType())
		};
		Long id_vistaRptVersamento = null;
		try{
			id_vistaRptVersamento = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_vistaRptVersamento);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_vistaRptVersamento==null || id_vistaRptVersamento<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_vistaRptVersamento;
	}
}
