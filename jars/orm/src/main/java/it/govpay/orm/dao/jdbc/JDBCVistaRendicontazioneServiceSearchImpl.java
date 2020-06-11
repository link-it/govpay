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

import it.govpay.orm.IdRendicontazione;
import it.govpay.orm.VistaRendicontazione;
import it.govpay.orm.dao.jdbc.converter.VistaRendicontazioneFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.VistaRendicontazioneFetch;
import it.govpay.orm.model.VistaRendicontazioneModel;

/**     
 * JDBCVistaRendicontazioneServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVistaRendicontazioneServiceSearchImpl implements IJDBCServiceSearchWithId<VistaRendicontazione, IdRendicontazione, JDBCServiceManager> {

	private VistaRendicontazioneFieldConverter _vistaRendicontazioneFieldConverter = null;
	public VistaRendicontazioneFieldConverter getVistaRendicontazioneFieldConverter() {
		if(this._vistaRendicontazioneFieldConverter==null){
			this._vistaRendicontazioneFieldConverter = new VistaRendicontazioneFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._vistaRendicontazioneFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getVistaRendicontazioneFieldConverter();
	}
	
	private VistaRendicontazioneFetch vistaRendicontazioneFetch = new VistaRendicontazioneFetch();
	public VistaRendicontazioneFetch getVistaRendicontazioneFetch() {
		return this.vistaRendicontazioneFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getVistaRendicontazioneFetch();
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
	public IdRendicontazione convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaRendicontazione vistaRendicontazione) throws NotImplementedException, ServiceException, Exception{
	
		IdRendicontazione idVistaRendicontazione = new IdRendicontazione();
		idVistaRendicontazione.setIdRendicontazione(vistaRendicontazione.getId());
		return idVistaRendicontazione;
	}
	
	@Override
	public VistaRendicontazione get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_vistaRendicontazione = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdVistaRendicontazione(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_vistaRendicontazione,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_vistaRendicontazione = this.findIdVistaRendicontazione(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_vistaRendicontazione != null && id_vistaRendicontazione > 0;
		
	}
	
	@Override
	public List<IdRendicontazione> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		List<IdRendicontazione> list = new ArrayList<IdRendicontazione>();
		
		try{
			List<IField> fields = new ArrayList<>();

			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaRendicontazione.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (VistaRendicontazione)this.getVistaRendicontazioneFetch().fetch(jdbcProperties.getDatabase(), VistaRendicontazione.model(), map)));
			}
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<VistaRendicontazione> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        List<VistaRendicontazione> list = new ArrayList<VistaRendicontazione>();
        
        try{
			List<IField> fields = new ArrayList<>();

			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("fr_id_incasso", Long.class, "fr_id_incasso", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("rnd_id_pagamento", Long.class, "rnd_id_pagamento", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("sng_id_tributo", Long.class, "sng_id_tributo", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento_dominio", Long.class, "vrs_id_tipo_versamento_dominio", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento", Long.class, "vrs_id_tipo_versamento", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("vrs_id_dominio", Long.class, "vrs_id_dominio", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("vrs_id_uo", Long.class, "vrs_id_uo", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("vrs_id_applicazione", Long.class, "vrs_id_applicazione", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			fields.add(new CustomField("vrs_id_documento", Long.class, "vrs_id_documento", this.getFieldConverter().toTable(VistaRendicontazione.model())));
			
			fields.add(VistaRendicontazione.model().FR_COD_PSP);
			fields.add(VistaRendicontazione.model().FR_COD_DOMINIO);
			fields.add(VistaRendicontazione.model().FR_COD_FLUSSO);
			fields.add(VistaRendicontazione.model().FR_STATO);
			fields.add(VistaRendicontazione.model().FR_DESCRIZIONE_STATO);
			fields.add(VistaRendicontazione.model().FR_IUR);
			fields.add(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO);
			fields.add(VistaRendicontazione.model().FR_DATA_REGOLAMENTO);
			fields.add(VistaRendicontazione.model().FR_DATA_ACQUISIZIONE);
			fields.add(VistaRendicontazione.model().FR_NUMERO_PAGAMENTI);
			fields.add(VistaRendicontazione.model().FR_IMPORTO_TOTALE_PAGAMENTI);
			fields.add(VistaRendicontazione.model().FR_COD_BIC_RIVERSAMENTO);
			fields.add(VistaRendicontazione.model().FR_ID);
			fields.add(VistaRendicontazione.model().FR_RAGIONE_SOCIALE_DOMINIO);
			fields.add(VistaRendicontazione.model().FR_RAGIONE_SOCIALE_PSP);
			
			fields.add(VistaRendicontazione.model().RND_IUV);
			fields.add(VistaRendicontazione.model().RND_IUR);
			fields.add(VistaRendicontazione.model().RND_INDICE_DATI);
			fields.add(VistaRendicontazione.model().RND_IMPORTO_PAGATO);
			fields.add(VistaRendicontazione.model().RND_ESITO);
			fields.add(VistaRendicontazione.model().RND_DATA);
			fields.add(VistaRendicontazione.model().RND_STATO);
			fields.add(VistaRendicontazione.model().RND_ANOMALIE);
			
			fields.add(VistaRendicontazione.model().SNG_COD_SING_VERS_ENTE);
			fields.add(VistaRendicontazione.model().SNG_STATO_SINGOLO_VERSAMENTO);
			fields.add(VistaRendicontazione.model().SNG_IMPORTO_SINGOLO_VERSAMENTO);
			fields.add(VistaRendicontazione.model().SNG_DESCRIZIONE);
			fields.add(VistaRendicontazione.model().SNG_DATI_ALLEGATI);
			fields.add(VistaRendicontazione.model().SNG_INDICE_DATI);
			fields.add(VistaRendicontazione.model().SNG_DESCRIZIONE_CAUSALE_RPT);
			fields.add(VistaRendicontazione.model().VRS_ID);
			fields.add(VistaRendicontazione.model().VRS_COD_VERSAMENTO_ENTE);
			fields.add(VistaRendicontazione.model().VRS_NOME);
			
			fields.add(VistaRendicontazione.model().VRS_IMPORTO_TOTALE);
			fields.add(VistaRendicontazione.model().VRS_STATO_VERSAMENTO);
			fields.add(VistaRendicontazione.model().VRS_DESCRIZIONE_STATO);
			fields.add(VistaRendicontazione.model().VRS_AGGIORNABILE);
			fields.add(VistaRendicontazione.model().VRS_DATA_CREAZIONE);
			fields.add(VistaRendicontazione.model().VRS_DATA_VALIDITA);
			fields.add(VistaRendicontazione.model().VRS_DATA_SCADENZA);
			fields.add(VistaRendicontazione.model().VRS_DATA_ORA_ULTIMO_AGG);
			fields.add(VistaRendicontazione.model().VRS_CAUSALE_VERSAMENTO);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_TIPO);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_IDENTIFICATIVO);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_ANAGRAFICA);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_INDIRIZZO);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_CIVICO);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_CAP);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_LOCALITA);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_PROVINCIA);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_NAZIONE);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_EMAIL);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_TELEFONO);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_CELLULARE);
			fields.add(VistaRendicontazione.model().VRS_DEBITORE_FAX);
			fields.add(VistaRendicontazione.model().VRS_TASSONOMIA_AVVISO);
			fields.add(VistaRendicontazione.model().VRS_TASSONOMIA);
			fields.add(VistaRendicontazione.model().VRS_COD_LOTTO);
			fields.add(VistaRendicontazione.model().VRS_COD_VERSAMENTO_LOTTO);
			fields.add(VistaRendicontazione.model().VRS_COD_ANNO_TRIBUTARIO);
			fields.add(VistaRendicontazione.model().VRS_COD_BUNDLEKEY);
			fields.add(VistaRendicontazione.model().VRS_DATI_ALLEGATI);
			fields.add(VistaRendicontazione.model().VRS_INCASSO);
			fields.add(VistaRendicontazione.model().VRS_ANOMALIE);
			fields.add(VistaRendicontazione.model().VRS_IUV_VERSAMENTO);
			fields.add(VistaRendicontazione.model().VRS_NUMERO_AVVISO);
			fields.add(VistaRendicontazione.model().VRS_ACK);
			fields.add(VistaRendicontazione.model().VRS_ANOMALO);
			fields.add(VistaRendicontazione.model().VRS_DIVISIONE);
			fields.add(VistaRendicontazione.model().VRS_DIREZIONE);
			fields.add(VistaRendicontazione.model().VRS_ID_SESSIONE); 
			fields.add(VistaRendicontazione.model().VRS_DATA_PAGAMENTO);
			fields.add(VistaRendicontazione.model().VRS_IMPORTO_PAGATO);
			fields.add(VistaRendicontazione.model().VRS_IMPORTO_INCASSATO);
			fields.add(VistaRendicontazione.model().VRS_STATO_PAGAMENTO);
			fields.add(VistaRendicontazione.model().VRS_IUV_PAGAMENTO);
			fields.add(VistaRendicontazione.model().VRS_COD_RATA);
			fields.add(VistaRendicontazione.model().VRS_TIPO);
			
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				VistaRendicontazione rendicontazione = (VistaRendicontazione) this.getVistaRendicontazioneFetch().fetch(jdbcProperties.getDatabase(), VistaRendicontazione.model(), map);
				
				// risoluzione ID
				Long idIncasso = null;
 				Object idIncassoObj = map.remove("fr_id_incasso");
				if(idIncassoObj instanceof Long)
					idIncasso = (Long) idIncassoObj;
				
				Long id_pagamento = null;
				Object idPagamentoObj = map.remove("rnd_id_pagamento");
				if(idPagamentoObj instanceof Long)
					id_pagamento = (Long) idPagamentoObj;
				
				Object idTributoObj = map.remove("sng_id_tributo");
				Long idTributo = null;
				if(idTributoObj instanceof Long) {
					idTributo = (Long) idTributoObj;
				}
				
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
				
				
				if(idIncasso != null) {
					if(idMappingResolutionBehaviour==null ||
							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
							){
						it.govpay.orm.IdIncasso id_pagamento_incasso = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_pagamento_incasso = ((JDBCIncassoServiceSearch)(this.getServiceManager().getIncassoServiceSearch())).findId(idIncasso, false);
						}else{
							id_pagamento_incasso = new it.govpay.orm.IdIncasso();
						}
						id_pagamento_incasso.setId(idIncasso);
						rendicontazione.setFrIdIncasso(id_pagamento_incasso);
					}
				}
				
				if(id_pagamento != null) {
					if(idMappingResolutionBehaviour==null ||
							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
							it.govpay.orm.IdPagamento id_rendicontazione_pagamento = null;
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_rendicontazione_pagamento = ((JDBCPagamentoServiceSearch)(this.getServiceManager().getPagamentoServiceSearch())).findId(id_pagamento, false);
							}else{
								id_rendicontazione_pagamento = new it.govpay.orm.IdPagamento();
							}
							id_rendicontazione_pagamento.setId(id_pagamento);
							rendicontazione.setRndIdPagamento(id_rendicontazione_pagamento);
						}
				}
				
				if(idTributo != null) {
					it.govpay.orm.IdTributo id_singoloVersamento_tributo = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_singoloVersamento_tributo = ((JDBCTributoServiceSearch)(this.getServiceManager().getTributoServiceSearch())).findId(idTributo, false);
					}else{
						id_singoloVersamento_tributo = new it.govpay.orm.IdTributo();
					}
					id_singoloVersamento_tributo.setId(idTributo);
					rendicontazione.setSngIdTributo(id_singoloVersamento_tributo);
				}
				
				it.govpay.orm.IdApplicazione id_versamento_applicazione = null;
				if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
					id_versamento_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idApplicazione, false);
				}else{
					id_versamento_applicazione = new it.govpay.orm.IdApplicazione();
				}
				id_versamento_applicazione.setId(idApplicazione);
				rendicontazione.setVrsIdApplicazione(id_versamento_applicazione);

				it.govpay.orm.IdDominio id_versamento_dominio = null;
				if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
					id_versamento_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findId(idDominio, false);
				}else{
					id_versamento_dominio = new it.govpay.orm.IdDominio();
				}
				id_versamento_dominio.setId(idDominio);
				rendicontazione.setVrsIdDominio(id_versamento_dominio);

				if(idUO != null) {
					it.govpay.orm.IdUo id_versamento_ente = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_ente = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findId(idUO, false);
					}else{
						id_versamento_ente = new it.govpay.orm.IdUo();
					}
					id_versamento_ente.setId(idUO);
					rendicontazione.setVrsIdUo(id_versamento_ente);
				}

				if(idTipoVersamento != null) {
					it.govpay.orm.IdTipoVersamento id_versamento_tipoVersamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_tipoVersamento = ((JDBCTipoVersamentoServiceSearch)(this.getServiceManager().getTipoVersamentoServiceSearch())).findId(idTipoVersamento, false);
					}else{
						id_versamento_tipoVersamento = new it.govpay.orm.IdTipoVersamento();
					}
					id_versamento_tipoVersamento.setId(idTipoVersamento);
					rendicontazione.setVrsIdTipoVersamento(id_versamento_tipoVersamento);
				}
				
				if(idTipoVersamentoDominio != null) {
					it.govpay.orm.IdTipoVersamentoDominio id_versamento_tipoVersamentoDominio = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_tipoVersamentoDominio = ((JDBCTipoVersamentoDominioServiceSearch)(this.getServiceManager().getTipoVersamentoDominioServiceSearch())).findId(idTipoVersamentoDominio, false);
					}else{
						id_versamento_tipoVersamentoDominio = new it.govpay.orm.IdTipoVersamentoDominio();
					}
					id_versamento_tipoVersamentoDominio.setId(idTipoVersamentoDominio);
					rendicontazione.setVrsIdTipoVersamentoDominio(id_versamento_tipoVersamentoDominio);
				}
				
				if(idDocumento != null && idDocumento > 0) {
					it.govpay.orm.IdDocumento id_versamento_documento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_versamento_documento = ((JDBCDocumentoServiceSearch)(this.getServiceManager().getDocumentoServiceSearch())).findId(idDocumento, false);
					}else{
						id_versamento_documento = new it.govpay.orm.IdDocumento();
					}
					id_versamento_documento.setId(idDocumento);
					rendicontazione.setVrsIdDocumento(id_versamento_documento);
				}
				
				
				list.add(rendicontazione);
			}
		} catch(NotFoundException e) {}
        
        
        return list;      
		
	}
	
	@Override
	public VistaRendicontazione find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

		VistaRendicontazioneModel model = VistaRendicontazione.model();

		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		pagExpr.offset(0);
		pagExpr.limit(2);
		pagExpr.addOrder(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(model)), SortOrder.ASC);
		
		List<VistaRendicontazione> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model());
		
		sqlQueryObject.addSelectCountField(this.getVistaRendicontazioneFieldConverter().toTable(VistaRendicontazione.model())+".id","tot");
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_vistaRendicontazione = this.findIdVistaRendicontazione(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_vistaRendicontazione);
		
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
												this.getVistaRendicontazioneFieldConverter(), field);

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
        						expression, this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(),
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
        						this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(), 
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
        						this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(), 
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
			return new JDBCExpression(this.getVistaRendicontazioneFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getVistaRendicontazioneFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id, VistaRendicontazione obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, VistaRendicontazione obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaRendicontazione obj, VistaRendicontazione imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getFrIdIncasso()!=null && 
				imgSaved.getFrIdIncasso()!=null){
			obj.getFrIdIncasso().setId(imgSaved.getFrIdIncasso().getId());
		}
		if(obj.getRndIdPagamento()!=null && 
				imgSaved.getRndIdPagamento()!=null){
			obj.getRndIdPagamento().setId(imgSaved.getRndIdPagamento().getId());
		}
		if(obj.getSngIdTributo()!=null && 
				imgSaved.getSngIdTributo()!=null){
			obj.getSngIdTributo().setId(imgSaved.getSngIdTributo().getId());
			if(obj.getSngIdTributo().getIdDominio()!=null && 
					imgSaved.getSngIdTributo().getIdDominio()!=null){
				obj.getSngIdTributo().getIdDominio().setId(imgSaved.getSngIdTributo().getIdDominio().getId());
			}
			if(obj.getSngIdTributo().getIdTipoTributo()!=null && 
					imgSaved.getSngIdTributo().getIdTipoTributo()!=null){
				obj.getSngIdTributo().getIdTipoTributo().setId(imgSaved.getSngIdTributo().getIdTipoTributo().getId());
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
	public VistaRendicontazione get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private VistaRendicontazione _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		IField idField = new CustomField("id", Long.class, "id", this.getVistaRendicontazioneFieldConverter().toTable(VistaRendicontazione.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<VistaRendicontazione> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsVistaRendicontazione = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getVistaRendicontazioneFieldConverter().toTable(VistaRendicontazione.model()));
		sqlQueryObject.addSelectField(this.getVistaRendicontazioneFieldConverter().toColumn(VistaRendicontazione.model().FR_COD_PSP,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists vistaRendicontazione
		existsVistaRendicontazione = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsVistaRendicontazione;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		String tableVistaRendicontazione = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model());
		String tableIncassi = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().FR_ID_INCASSO);
		String tableTributi = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().SNG_ID_TRIBUTO);
		String tablePagamenti = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().RND_ID_PAGAMENTO);
		String tableApplicazioni = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_APPLICAZIONE);
		String tableTipiVersamento = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO);
		String tableTipiVersamentoDominio = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO);
		String tableDomini = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_DOMINIO);
		String tableUO = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_UO);

		
		if(expression.inUseModel(VistaRendicontazione.model().FR_ID_INCASSO,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".fr_id_incasso="+tableIncassi+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().SNG_ID_TRIBUTO,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".sng_id_tributo="+tableTributi+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().RND_ID_PAGAMENTO,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".rnd_id_pagamento="+tablePagamenti+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_APPLICAZIONE,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_applicazione="+tableApplicazioni+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_tipo_versamento="+tableTipiVersamento+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_dominio="+tableDomini+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_UO,false)){
			sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_uo="+tableUO+".id");
		}
	
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaRendicontazione.model().VRS_ID_UO,false)){
				sqlQueryObject.addFromTable(tableUO);
				sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_uo="+tableUO+".id");
			}

			String tableDomini2 = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableUO+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableDomini2 = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO,false)){
			if(!expression.inUseModel(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableTipiVeramento2 = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_tipo_versamento="+tableTipiVeramento2+".id");
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO,false)){
			if(!expression.inUseModel(VistaRendicontazione.model().SNG_ID_TRIBUTO,false)){
				sqlQueryObject.addFromTable(tableTributi);
				sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".sng_id_tributo="+tableTributi+".id");
			}

			String tableTipiTributo = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO);
			sqlQueryObject.addWhereCondition(tableTributi+".id_tipo_tributo="+tableTipiTributo+".id");
			
		}
		
		if(expression.inUseModel(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaRendicontazione.model().SNG_ID_TRIBUTO,false)){
				sqlQueryObject.addFromTable(tableTributi);
				sqlQueryObject.addWhereCondition(tableVistaRendicontazione+".sng_id_tributo="+tableTributi+".id");
			}

			String tableDomini2 = this.getVistaRendicontazioneFieldConverter().toAliasTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableDomini2+".id="+tableTributi+".id_dominio");
		}
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdVistaRendicontazione(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		VistaRendicontazioneFieldConverter converter = this.getVistaRendicontazioneFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// VistaRendicontazione.model()
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model()))
			));

		// VistaRendicontazione.model().FR_ID_INCASSO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().FR_ID_INCASSO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().FR_ID_INCASSO))
			));

		// VistaRendicontazione.model().RND_ID_PAGAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().RND_ID_PAGAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().RND_ID_PAGAMENTO))
			));

		// VistaRendicontazione.model().SNG_ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO))
			));

		// VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_DOMINIO))
			));

		// VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO))
			));

		// VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO))
			));

		// VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO))
			));

		// VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_TIPO_VERSAMENTO))
			));

		// VistaRendicontazione.model().VRS_ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_DOMINIO))
			));

		// VistaRendicontazione.model().VRS_ID_UO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_UO))
			));

		// VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_UO.ID_DOMINIO))
			));

		// VistaRendicontazione.model().VRS_ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VistaRendicontazione.model().VRS_ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaRendicontazione.model().VRS_ID_APPLICAZIONE))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaRendicontazioneFieldConverter().toTable(VistaRendicontazione.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaRendicontazioneFieldConverter().toTable(VistaRendicontazione.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getVistaRendicontazioneFieldConverter(), VistaRendicontazione.model(), objectIdClass, listaQuery);
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
	public IdRendicontazione findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaRendicontazione
		sqlQueryObjectGet.addFromTable(this.getVistaRendicontazioneFieldConverter().toTable(VistaRendicontazione.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaRendicontazione
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaRendicontazione = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_vistaRendicontazione = new ArrayList<Class<?>>();
		listaFieldIdReturnType_vistaRendicontazione.add(Long.class);
		it.govpay.orm.IdRendicontazione id_vistaRendicontazione = null;
		List<Object> listaFieldId_vistaRendicontazione = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_vistaRendicontazione, searchParams_vistaRendicontazione);
		if(listaFieldId_vistaRendicontazione==null || listaFieldId_vistaRendicontazione.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _vistaRendicontazione
			id_vistaRendicontazione = new it.govpay.orm.IdRendicontazione();
			id_vistaRendicontazione.setIdRendicontazione((Long)listaFieldId_vistaRendicontazione.get(0));
		}
		
		return id_vistaRendicontazione;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdVistaRendicontazione(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdVistaRendicontazione(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdRendicontazione id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaRendicontazione
		sqlQueryObjectGet.addFromTable(this.getVistaRendicontazioneFieldConverter().toTable(VistaRendicontazione.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaRendicontazione
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaRendicontazione = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdRendicontazione(),Long.class),
		};
		Long id_vistaRendicontazione = null;
		try{
			id_vistaRendicontazione = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_vistaRendicontazione);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_vistaRendicontazione==null || id_vistaRendicontazione<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_vistaRendicontazione;
	}
}
