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
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.generic_project.utils.UtilsTemplate;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;

import it.govpay.orm.IdPagamento;
import it.govpay.orm.VistaPagamento;
import it.govpay.orm.dao.jdbc.converter.VistaPagamentoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.VistaPagamentoFetch;

/**     
 * JDBCVistaPagamentoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVistaPagamentoServiceSearchImpl implements IJDBCServiceSearchWithId<VistaPagamento, IdPagamento, JDBCServiceManager> {

	private VistaPagamentoFieldConverter _vistaPagamentoFieldConverter = null;
	public VistaPagamentoFieldConverter getVistaPagamentoFieldConverter() {
		if(this._vistaPagamentoFieldConverter==null){
			this._vistaPagamentoFieldConverter = new VistaPagamentoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._vistaPagamentoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getVistaPagamentoFieldConverter();
	}
	
	private VistaPagamentoFetch vistaPagamentoFetch = new VistaPagamentoFetch();
	public VistaPagamentoFetch getVistaPagamentoFetch() {
		return this.vistaPagamentoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getVistaPagamentoFetch();
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
	public IdPagamento convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaPagamento vistaPagamento) throws NotImplementedException, ServiceException, Exception{
	
		IdPagamento idVistaPagamento = new IdPagamento();
		idVistaPagamento.setIdPagamento(vistaPagamento.getId());
	
		return idVistaPagamento;
	}
	
	@Override
	public VistaPagamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_vistaPagamento = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdVistaPagamento(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_vistaPagamento,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_vistaPagamento = this.findIdVistaPagamento(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_vistaPagamento != null && id_vistaPagamento > 0;
		
	}
	
	@Override
	public List<IdPagamento> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		List<IdPagamento> list = new ArrayList<>();

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getVistaPagamentoFieldConverter().toTable(VistaPagamento.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (VistaPagamento)this.getVistaPagamentoFetch().fetch(jdbcProperties.getDatabase(), VistaPagamento.model(), map)));
			}
		} catch(NotFoundException e) {}

		return list;

	}
	
	@Override
	public List<VistaPagamento> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        List<VistaPagamento> list = new ArrayList<VistaPagamento>();

        // default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaPagamento.model())));
//			fields.add(new CustomField("id_rpt", Long.class, "id_rpt", this.getFieldConverter().toTable(VistaPagamento.model())));
//			fields.add(new CustomField("id_singolo_versamento", Long.class, "id_singolo_versamento", this.getFieldConverter().toTable(VistaPagamento.model())));
//			fields.add(new CustomField("id_rr", Long.class, "id_rr", this.getFieldConverter().toTable(VistaPagamento.model())));
//			fields.add(new CustomField("id_incasso", Long.class, "id_incasso", this.getFieldConverter().toTable(VistaPagamento.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento_dominio", Long.class, "vrs_id_tipo_versamento_dominio", this.getFieldConverter().toTable(VistaPagamento.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento", Long.class, "vrs_id_tipo_versamento", this.getFieldConverter().toTable(VistaPagamento.model())));
			fields.add(new CustomField("vrs_id_dominio", Long.class, "vrs_id_dominio", this.getFieldConverter().toTable(VistaPagamento.model())));
			fields.add(new CustomField("vrs_id_uo", Long.class, "vrs_id_uo", this.getFieldConverter().toTable(VistaPagamento.model())));
			fields.add(new CustomField("vrs_id_applicazione", Long.class, "vrs_id_applicazione", this.getFieldConverter().toTable(VistaPagamento.model())));
//			fields.add(new CustomField("vrs_id_documento", Long.class, "vrs_id_documento", this.getFieldConverter().toTable(VistaPagamento.model())));
			fields.add(VistaPagamento.model().COD_DOMINIO);
			fields.add(VistaPagamento.model().STATO);
			fields.add(VistaPagamento.model().IUV);
			fields.add(VistaPagamento.model().INDICE_DATI);
			fields.add(VistaPagamento.model().IMPORTO_PAGATO);
			fields.add(VistaPagamento.model().DATA_ACQUISIZIONE);
			fields.add(VistaPagamento.model().IUR);
			fields.add(VistaPagamento.model().DATA_PAGAMENTO);
			fields.add(VistaPagamento.model().COMMISSIONI_PSP);
			fields.add(VistaPagamento.model().TIPO_ALLEGATO);
			fields.add(VistaPagamento.model().ALLEGATO);
			fields.add(VistaPagamento.model().DATA_ACQUISIZIONE_REVOCA);
			fields.add(VistaPagamento.model().CAUSALE_REVOCA);
			fields.add(VistaPagamento.model().DATI_REVOCA);
			fields.add(VistaPagamento.model().IMPORTO_REVOCATO);
			fields.add(VistaPagamento.model().ESITO_REVOCA);
			fields.add(VistaPagamento.model().DATI_ESITO_REVOCA);
			fields.add(VistaPagamento.model().TIPO);
			
			fields.add(VistaPagamento.model().VRS_ID);
			fields.add(VistaPagamento.model().VRS_COD_VERSAMENTO_ENTE);
			fields.add(VistaPagamento.model().VRS_TASSONOMIA);
			fields.add(VistaPagamento.model().VRS_DIVISIONE);
			fields.add(VistaPagamento.model().VRS_DIREZIONE);
			
			fields.add(VistaPagamento.model().SNG_COD_SING_VERS_ENTE);
			
			fields.add(VistaPagamento.model().RPT_CCP);
			fields.add(VistaPagamento.model().RPT_IUV);
			
			fields.add(VistaPagamento.model().RNC_TRN);
			
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
//				Object id_rptObject = map.remove("id_rpt");
//
//				Long id_singolo_versamento = null;
//				Object idSingoloVersamentoObj = map.remove("id_singolo_versamento");
//
//				if(idSingoloVersamentoObj instanceof Long)
//					id_singolo_versamento = (Long) idSingoloVersamentoObj;
//
//				Long idRR = null;
//				Object idRRObj = map.remove("id_rr");
//
//				if(idRRObj instanceof Long)
//					idRR = (Long) idRRObj;
//
//				Long idIncasso = null;
//				Object idIncassoObj = map.remove("id_incasso");
//
//				if(idIncassoObj instanceof Long)
//					idIncasso = (Long) idIncassoObj;

				VistaPagamento vistaPagamento = (VistaPagamento)this.getVistaPagamentoFetch().fetch(jdbcProperties.getDatabase(), VistaPagamento.model(), map);
				
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
				
//				Long idDocumento = null;
//				Object idDocumentoObject = map.remove("vrs_id_documento");
//				if(idDocumentoObject instanceof Long) {
//					idDocumento = (Long) idDocumentoObject;
//				}
				
				it.govpay.orm.IdApplicazione id_versamento_applicazione = null;
				if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
					id_versamento_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idApplicazione, false);
				}else{
					id_versamento_applicazione = new it.govpay.orm.IdApplicazione();
				}
				id_versamento_applicazione.setId(idApplicazione);
				vistaPagamento.setVrsIdApplicazione(id_versamento_applicazione);

				it.govpay.orm.IdDominio id_versamento_dominio = null;
				if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
					id_versamento_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findId(idDominio, false);
				}else{
					id_versamento_dominio = new it.govpay.orm.IdDominio();
				}
				id_versamento_dominio.setId(idDominio);
				vistaPagamento.setVrsIdDominio(id_versamento_dominio);

				if(idUO != null) {
					it.govpay.orm.IdUo id_versamento_ente = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_ente = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findId(idUO, false);
					}else{
						id_versamento_ente = new it.govpay.orm.IdUo();
					}
					id_versamento_ente.setId(idUO);
					vistaPagamento.setVrsIdUo(id_versamento_ente);
				}

				if(idTipoVersamento != null) {
					it.govpay.orm.IdTipoVersamento id_versamento_tipoVersamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_tipoVersamento = ((JDBCTipoVersamentoServiceSearch)(this.getServiceManager().getTipoVersamentoServiceSearch())).findId(idTipoVersamento, false);
					}else{
						id_versamento_tipoVersamento = new it.govpay.orm.IdTipoVersamento();
					}
					id_versamento_tipoVersamento.setId(idTipoVersamento);
					vistaPagamento.setVrsIdTipoVersamento(id_versamento_tipoVersamento);
				}
				
				if(idTipoVersamentoDominio != null) {
					it.govpay.orm.IdTipoVersamentoDominio id_versamento_tipoVersamentoDominio = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_tipoVersamentoDominio = ((JDBCTipoVersamentoDominioServiceSearch)(this.getServiceManager().getTipoVersamentoDominioServiceSearch())).findId(idTipoVersamentoDominio, false);
					}else{
						id_versamento_tipoVersamentoDominio = new it.govpay.orm.IdTipoVersamentoDominio();
					}
					id_versamento_tipoVersamentoDominio.setId(idTipoVersamentoDominio);
					vistaPagamento.setVrsIdTipoVersamentoDominio(id_versamento_tipoVersamentoDominio);
				}
				
//				if(idDocumento != null && idDocumento > 0) {
//					it.govpay.orm.IdDocumento id_rpt_documentoVersamento = null;
//					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
//						id_rpt_documentoVersamento = ((JDBCDocumentoServiceSearch)(this.getServiceManager().getDocumentoServiceSearch())).findId(idDocumento, false);
//					}else{
//						id_rpt_documentoVersamento = new it.govpay.orm.IdDocumento();
//					}
//					id_rpt_documentoVersamento.setId(idDocumento);
//					vistaPagamento.setVrsIdDocumento(id_rpt_documentoVersamento);
//				}

//				if(id_rptObject instanceof Long)
//					if(idMappingResolutionBehaviour==null ||
//					(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
//							){
//						Long id_rpt = (Long) id_rptObject;
//						it.govpay.orm.IdRpt id_pagamento_rpt = null;
//						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
//							id_pagamento_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findId(id_rpt, false);
//						}else{
//							id_pagamento_rpt = new it.govpay.orm.IdRpt();
//						}
//						id_pagamento_rpt.setId(id_rpt);
//						pagamento.setIdRPT(id_pagamento_rpt);
//					}
//
//				if(id_singolo_versamento != null){
//					if(idMappingResolutionBehaviour==null ||
//							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
//							){
//						it.govpay.orm.IdSingoloVersamento id_pagamento_singoloVersamento = null;
//						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
//							id_pagamento_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findId(id_singolo_versamento, false);
//						}else{
//							id_pagamento_singoloVersamento = new it.govpay.orm.IdSingoloVersamento();
//						}
//						id_pagamento_singoloVersamento.setId(id_singolo_versamento);
//						pagamento.setIdSingoloVersamento(id_pagamento_singoloVersamento);
//					}
//				}
//
//				if(idRR != null) {
//					if(idMappingResolutionBehaviour==null ||
//							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
//							){
//
//						it.govpay.orm.IdRr id_pagamento_rr = null;
//						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
//							id_pagamento_rr = ((JDBCRRServiceSearch)(this.getServiceManager().getRRServiceSearch())).findId(idRR, false);
//						}else{
//							id_pagamento_rr = new it.govpay.orm.IdRr();
//						}
//						id_pagamento_rr.setId(idRR);
//						pagamento.setIdRr(id_pagamento_rr);
//					}
//				}
//
//				if(idIncasso != null) {
//					if(idMappingResolutionBehaviour==null ||
//							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
//							){
//						it.govpay.orm.IdIncasso id_pagamento_incasso = null;
//						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
//							id_pagamento_incasso = ((JDBCIncassoServiceSearch)(this.getServiceManager().getIncassoServiceSearch())).findId(idIncasso, false);
//						}else{
//							id_pagamento_incasso = new it.govpay.orm.IdIncasso();
//						}
//						id_pagamento_incasso.setId(idIncasso);
//						pagamento.setIdIncasso(id_pagamento_incasso);
//					}
//				}


				list.add(vistaPagamento);
			}
		} catch(NotFoundException e) {}

		return list;   
		
	}
	
	@Override
	public VistaPagamento find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

        long id = this.findTableId(jdbcProperties, log, connection, sqlQueryObject, expression);
        if(id>0){
        	return this.get(jdbcProperties, log, connection, sqlQueryObject, id, idMappingResolutionBehaviour);
        }else{
        	throw new NotFoundException("Entry with id["+id+"] not found");
        }
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaPagamentoFieldConverter(), VistaPagamento.model());
		
		sqlQueryObject.addSelectCountField(this.getVistaPagamentoFieldConverter().toTable(VistaPagamento.model())+".id","tot",true);
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getVistaPagamentoFieldConverter(), VistaPagamento.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_vistaPagamento = this.findIdVistaPagamento(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_vistaPagamento);
		
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
												this.getVistaPagamentoFieldConverter(), field);

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
        						expression, this.getVistaPagamentoFieldConverter(), VistaPagamento.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getVistaPagamentoFieldConverter(), VistaPagamento.model(),
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
        						this.getVistaPagamentoFieldConverter(), VistaPagamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaPagamentoFieldConverter(), VistaPagamento.model(), 
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
        						this.getVistaPagamentoFieldConverter(), VistaPagamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaPagamentoFieldConverter(), VistaPagamento.model(), 
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
			return new JDBCExpression(this.getVistaPagamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getVistaPagamentoFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, VistaPagamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, VistaPagamento obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaPagamento obj, VistaPagamento imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
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
	public VistaPagamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private VistaPagamento _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		IField idField = new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaPagamento.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<VistaPagamento> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsVistaPagamento = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getVistaPagamentoFieldConverter().toTable(VistaPagamento.model()));
		sqlQueryObject.addSelectField(this.getVistaPagamentoFieldConverter().toColumn(VistaPagamento.model().COD_DOMINIO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists vistaPagamento
		existsVistaPagamento = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsVistaPagamento;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
	
		String tableRpt = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model());
//		String tablePagamentiPortale = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().ID_PAGAMENTO_PORTALE);
		String tableApplicazioni = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_APPLICAZIONE);
		String tableTipiVersamento = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO);
		String tableTipiVersamentoDominio = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO);
		String tableDomini = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_DOMINIO);
		String tableUO = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_UO);
//		String tableDocumenti = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_DOCUMENTO);
	
//		if(expression.inUseModel(VistaPagamento.model().ID_PAGAMENTO_PORTALE,false)){
//			sqlQueryObject.addWhereCondition(tableRpt+".id_pagamento_portale="+tablePagamentiPortale+".id");
//		}
//		
//		if(expression.inUseModel(VistaPagamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE,false)){
//			if(!expression.inUseModel(VistaPagamento.model().ID_PAGAMENTO_PORTALE,false)){
//				sqlQueryObject.addFromTable(tablePagamentiPortale);
//				sqlQueryObject.addWhereCondition(tableRpt+".id_pagamento_portale="+tablePagamentiPortale+".id");
//			}
//			
//			String tableApplicazioni2 = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE);
//			sqlQueryObject.addWhereCondition(tablePagamentiPortale+".id_applicazione="+tableApplicazioni2+".id");
//		}
		
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_APPLICAZIONE,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_applicazione="+tableApplicazioni+".id");
		}
		
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento="+tableTipiVersamento+".id");
		}
		
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
		}
		
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_dominio="+tableDomini+".id");
		}
		
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_UO,false)){
			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_uo="+tableUO+".id");
		}
		
//		if(expression.inUseModel(VistaPagamento.model().VRS_ID_DOCUMENTO,false)){
//			sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_documento="+tableDocumenti+".id");
//		}
	
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaPagamento.model().VRS_ID_UO,false)){
				sqlQueryObject.addFromTable(tableUO);
				sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_uo="+tableUO+".id");
			}

			String tableDomini2 = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableUO+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableDomini2 = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO,false)){
			if(!expression.inUseModel(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableRpt+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableTipiVeramento2 = this.getVistaPagamentoFieldConverter().toAliasTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_tipo_versamento="+tableTipiVeramento2+".id");
		}
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdVistaPagamento(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		VistaPagamentoFieldConverter converter = this.getVistaPagamentoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// VistaPagamento.model()
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model()))
			));

		// VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO))
			));

		// VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO))
			));

		// VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO))
			));

		// VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_TIPO_VERSAMENTO))
			));

		// VistaPagamento.model().VRS_ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_DOMINIO))
			));

		// VistaPagamento.model().VRS_ID_UO
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_UO))
			));

		// VistaPagamento.model().VRS_ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_UO.ID_DOMINIO))
			));

		// VistaPagamento.model().VRS_ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VistaPagamento.model().VRS_ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaPagamento.model().VRS_ID_APPLICAZIONE))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaPagamentoFieldConverter().toTable(VistaPagamento.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getVistaPagamentoFieldConverter(), VistaPagamento.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getVistaPagamentoFieldConverter(), VistaPagamento.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaPagamentoFieldConverter().toTable(VistaPagamento.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaPagamentoFieldConverter(), VistaPagamento.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getVistaPagamentoFieldConverter(), VistaPagamento.model(), objectIdClass, listaQuery);
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
        return inUse;

	}
	
	@Override
	public IdPagamento findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();
		// Object _vistaPagamento

		sqlQueryObjectGet.addFromTable(this.getVistaPagamentoFieldConverter().toTable(VistaPagamento.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaPagamento
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaPagamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_vistaPagamento = new ArrayList<Class<?>>();
		listaFieldIdReturnType_vistaPagamento.add(Long.class);
		it.govpay.orm.IdPagamento id_vistaPagamento = null;
		List<Object> listaFieldId_vistaPagamento = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_vistaPagamento, searchParams_vistaPagamento);
		if(listaFieldId_vistaPagamento==null || listaFieldId_vistaPagamento.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _vistaPagamento
			id_vistaPagamento = new it.govpay.orm.IdPagamento();
			id_vistaPagamento.setIdPagamento((Long)listaFieldId_vistaPagamento.get(0));
		}
		
		return id_vistaPagamento;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdVistaPagamento(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdVistaPagamento(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaPagamento
		sqlQueryObjectGet.addFromTable(this.getVistaPagamentoFieldConverter().toTable(VistaPagamento.model()));
		sqlQueryObjectGet.addSelectField("id");
		// Devono essere mappati nella where condition i metodi dell'oggetto id.getXXX
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaPagamento
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaPagamento = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdPagamento(),Long.class),
		};
		Long id_vistaPagamento = null;
		try{
			id_vistaPagamento = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_vistaPagamento);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_vistaPagamento==null || id_vistaPagamento<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_vistaPagamento;
	}
}
