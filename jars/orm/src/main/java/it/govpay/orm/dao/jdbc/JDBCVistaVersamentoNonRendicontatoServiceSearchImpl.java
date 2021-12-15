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
import it.govpay.orm.VistaVersamentoNonRendicontato;
import it.govpay.orm.VistaVersamentoNonRendicontato;
import it.govpay.orm.dao.jdbc.converter.VistaVersamentoNonRendicontatoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.VistaVersamentoNonRendicontatoFetch;

/**     
 * JDBCVistaVersamentoNonRendicontatoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCVistaVersamentoNonRendicontatoServiceSearchImpl implements IJDBCServiceSearchWithId<VistaVersamentoNonRendicontato, IdPagamento, JDBCServiceManager> {

	private VistaVersamentoNonRendicontatoFieldConverter _vistaVersamentoNonRendicontatoFieldConverter = null;
	public VistaVersamentoNonRendicontatoFieldConverter getVistaVersamentoNonRendicontatoFieldConverter() {
		if(this._vistaVersamentoNonRendicontatoFieldConverter==null){
			this._vistaVersamentoNonRendicontatoFieldConverter = new VistaVersamentoNonRendicontatoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._vistaVersamentoNonRendicontatoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getVistaVersamentoNonRendicontatoFieldConverter();
	}
	
	private VistaVersamentoNonRendicontatoFetch vistaVersamentoNonRendicontatoFetch = new VistaVersamentoNonRendicontatoFetch();
	public VistaVersamentoNonRendicontatoFetch getVistaVersamentoNonRendicontatoFetch() {
		return this.vistaVersamentoNonRendicontatoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getVistaVersamentoNonRendicontatoFetch();
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
	public IdPagamento convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaVersamentoNonRendicontato vistaVersamentoNonRendicontato) throws NotImplementedException, ServiceException, Exception{
	
		IdPagamento idVistaVersamentoNonRendicontato = new IdPagamento();
		idVistaVersamentoNonRendicontato.setId(vistaVersamentoNonRendicontato.getId());
	
		return idVistaVersamentoNonRendicontato;
	}
	
	@Override
	public VistaVersamentoNonRendicontato get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_vistaVersamentoNonRendicontato = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdVistaVersamentoNonRendicontato(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_vistaVersamentoNonRendicontato,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_vistaVersamentoNonRendicontato = this.findIdVistaVersamentoNonRendicontato(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_vistaVersamentoNonRendicontato != null && id_vistaVersamentoNonRendicontato > 0;
		
	}
	
	@Override
	public List<IdPagamento> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		List<IdPagamento> list = new ArrayList<IdPagamento>();
		
		try{
			List<IField> fields = new ArrayList<>();

			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (VistaVersamentoNonRendicontato)this.getVistaVersamentoNonRendicontatoFetch().fetch(jdbcProperties.getDatabase(), VistaVersamentoNonRendicontato.model(), map)));
			}
		} catch(NotFoundException e) {}

        return list;
		
	}
	
	@Override
	public List<VistaVersamentoNonRendicontato> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        List<VistaVersamentoNonRendicontato> list = new ArrayList<VistaVersamentoNonRendicontato>();
        
        try{
			List<IField> fields = new ArrayList<>();

			fields.add(new CustomField("id", Long.class, "id", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("pag_id_incasso", Long.class, "pag_id_incasso", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("sng_id_tributo", Long.class, "sng_id_tributo", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento_dominio", Long.class, "vrs_id_tipo_versamento_dominio", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("vrs_id_tipo_versamento", Long.class, "vrs_id_tipo_versamento", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("vrs_id_dominio", Long.class, "vrs_id_dominio", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("vrs_id_uo", Long.class, "vrs_id_uo", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("vrs_id_applicazione", Long.class, "vrs_id_applicazione", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			fields.add(new CustomField("vrs_id_documento", Long.class, "vrs_id_documento", this.getFieldConverter().toTable(VistaVersamentoNonRendicontato.model())));
			
			fields.add(VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE);
			fields.add(VistaVersamentoNonRendicontato.model().SNG_STATO_SINGOLO_VERSAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().SNG_IMPORTO_SINGOLO_VERSAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE);
			fields.add(VistaVersamentoNonRendicontato.model().SNG_DATI_ALLEGATI);
			fields.add(VistaVersamentoNonRendicontato.model().SNG_INDICE_DATI);
			fields.add(VistaVersamentoNonRendicontato.model().SNG_DESCRIZIONE_CAUSALE_RPT);
			fields.add(VistaVersamentoNonRendicontato.model().SNG_CONTABILITA);
			
			fields.add(VistaVersamentoNonRendicontato.model().VRS_ID);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_ENTE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_NOME);
			
			fields.add(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_TOTALE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_STATO_VERSAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DESCRIZIONE_STATO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_AGGIORNABILE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DATA_CREAZIONE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DATA_VALIDITA);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DATA_SCADENZA);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DATA_ORA_ULTIMO_AGG);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_CAUSALE_VERSAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TIPO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_IDENTIFICATIVO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_ANAGRAFICA);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_INDIRIZZO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CIVICO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CAP);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_LOCALITA);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_PROVINCIA);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_NAZIONE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_EMAIL);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_TELEFONO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_CELLULARE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DEBITORE_FAX);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA_AVVISO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_TASSONOMIA);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_COD_LOTTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_COD_VERSAMENTO_LOTTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_COD_ANNO_TRIBUTARIO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_COD_BUNDLEKEY);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DATI_ALLEGATI);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_INCASSO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_ANOMALIE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_IUV_VERSAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_NUMERO_AVVISO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_ACK);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_ANOMALO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DIVISIONE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DIREZIONE);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_ID_SESSIONE); 
			fields.add(VistaVersamentoNonRendicontato.model().VRS_DATA_PAGAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_PAGATO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_IMPORTO_INCASSATO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_STATO_PAGAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_IUV_PAGAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_COD_RATA);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_TIPO);
			fields.add(VistaVersamentoNonRendicontato.model().VRS_PROPRIETA);
			
			fields.add(VistaVersamentoNonRendicontato.model().PAG_ALLEGATO);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_CAUSALE_REVOCA);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_COD_DOMINIO);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_COMMISSIONI_PSP);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_DATA_ACQUISIZIONE_REVOCA);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_DATA_PAGAMENTO);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_DATI_ESITO_REVOCA);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_DATI_REVOCA);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_ESITO_REVOCA);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_IMPORTO_PAGATO);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_IMPORTO_REVOCATO);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_INDICE_DATI);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_IUR);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_IUV);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_STATO);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_TIPO);
			fields.add(VistaVersamentoNonRendicontato.model().PAG_TIPO_ALLEGATO);
			
			fields.add(VistaVersamentoNonRendicontato.model().RPT_CCP);
			fields.add(VistaVersamentoNonRendicontato.model().RPT_IUV);
			
			fields.add(VistaVersamentoNonRendicontato.model().RNC_TRN);
			
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				VistaVersamentoNonRendicontato rendicontazione = (VistaVersamentoNonRendicontato) this.getVistaVersamentoNonRendicontatoFetch().fetch(jdbcProperties.getDatabase(), VistaVersamentoNonRendicontato.model(), map);
				
				// risoluzione ID
				Long idIncasso = null;
 				Object idIncassoObj = map.remove("pag_id_incasso");
				if(idIncassoObj instanceof Long)
					idIncasso = (Long) idIncassoObj;
				
				Long idTributo = null;
				Object idTributoObj = map.remove("sng_id_tributo");
				if(idTributoObj instanceof Long) {
					idTributo = (Long) idTributoObj;
				}
				
				Long idApplicazione = null;
				Object idApplicazioneObj = map.remove("vrs_id_applicazione");
				if(idApplicazioneObj instanceof Long) {
					idApplicazione = (Long) idApplicazioneObj;
				}
				
				Long idDominio = null;
				Object idDominioObj = map.remove("vrs_id_dominio");
				if(idDominioObj instanceof Long) {
					idDominio = (Long) idDominioObj;
				}
				
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
					it.govpay.orm.IdIncasso id_pagamento_incasso = new it.govpay.orm.IdIncasso();
					id_pagamento_incasso.setId(idIncasso);
					rendicontazione.setPagIdIncasso(id_pagamento_incasso);
				}
				
				if(idTributo != null) {
					it.govpay.orm.IdTributo id_singoloVersamento_tributo = new it.govpay.orm.IdTributo();
					id_singoloVersamento_tributo.setId(idTributo);
					rendicontazione.setSngIdTributo(id_singoloVersamento_tributo);
				}
				
				if(idApplicazione != null) {
					it.govpay.orm.IdApplicazione id_versamento_applicazione = new it.govpay.orm.IdApplicazione();
					id_versamento_applicazione.setId(idApplicazione);
					rendicontazione.setVrsIdApplicazione(id_versamento_applicazione);
				}

				if(idDominio != null) {
					it.govpay.orm.IdDominio id_versamento_dominio = new it.govpay.orm.IdDominio();
					id_versamento_dominio.setId(idDominio);
					rendicontazione.setVrsIdDominio(id_versamento_dominio);
				}

				if(idUO != null) {
					it.govpay.orm.IdUo id_versamento_ente = new it.govpay.orm.IdUo();
					id_versamento_ente.setId(idUO);
					rendicontazione.setVrsIdUo(id_versamento_ente);
				}

				if(idTipoVersamento != null) {
					it.govpay.orm.IdTipoVersamento id_versamento_tipoVersamento = new it.govpay.orm.IdTipoVersamento();
					id_versamento_tipoVersamento.setId(idTipoVersamento);
					rendicontazione.setVrsIdTipoVersamento(id_versamento_tipoVersamento);
				}
				
				if(idTipoVersamentoDominio != null) {
					it.govpay.orm.IdTipoVersamentoDominio id_versamento_tipoVersamentoDominio = new it.govpay.orm.IdTipoVersamentoDominio();
					id_versamento_tipoVersamentoDominio.setId(idTipoVersamentoDominio);
					rendicontazione.setVrsIdTipoVersamentoDominio(id_versamento_tipoVersamentoDominio);
				}
				
				if(idDocumento != null && idDocumento > 0) {
					it.govpay.orm.IdDocumento id_versamento_documento = new it.govpay.orm.IdDocumento();
					id_versamento_documento.setId(idDocumento);
					rendicontazione.setVrsIdDocumento(id_versamento_documento);
				}
				
				list.add(rendicontazione);
			}
		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public VistaVersamentoNonRendicontato find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		
		JDBCPaginatedExpression pagExpr = this.toPaginatedExpression(expression,log);
		
		List<VistaVersamentoNonRendicontato> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject, pagExpr, idMappingResolutionBehaviour);

		if(lst.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(lst.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return lst.get(0);
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model());
		
		sqlQueryObject.addSelectCountField(this.getVistaVersamentoNonRendicontatoFieldConverter().toTable(VistaVersamentoNonRendicontato.model())+".id","tot",true);
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_vistaVersamentoNonRendicontato = this.findIdVistaVersamentoNonRendicontato(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_vistaVersamentoNonRendicontato);
		
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
												this.getVistaVersamentoNonRendicontatoFieldConverter(), field);

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
        						expression, this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(),
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
        						this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(), 
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
        						this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(), 
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
			return new JDBCExpression(this.getVistaVersamentoNonRendicontatoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getVistaVersamentoNonRendicontatoFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, VistaVersamentoNonRendicontato obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, VistaVersamentoNonRendicontato obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, VistaVersamentoNonRendicontato obj, VistaVersamentoNonRendicontato imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
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
		if(obj.getVrsIdDocumento()!=null && 
				imgSaved.getVrsIdDocumento()!=null){
			obj.getVrsIdDocumento().setId(imgSaved.getVrsIdDocumento().getId());
			if(obj.getVrsIdDocumento().getIdApplicazione()!=null && 
					imgSaved.getVrsIdDocumento().getIdApplicazione()!=null){
				obj.getVrsIdDocumento().getIdApplicazione().setId(imgSaved.getVrsIdDocumento().getIdApplicazione().getId());
			}
			if(obj.getVrsIdDocumento().getIdDominio()!=null && 
					imgSaved.getVrsIdDocumento().getIdDominio()!=null){
				obj.getVrsIdDocumento().getIdDominio().setId(imgSaved.getVrsIdDocumento().getIdDominio().getId());
			}
		}
		if(obj.getPagIdIncasso()!=null && 
				imgSaved.getPagIdIncasso()!=null){
			obj.getPagIdIncasso().setId(imgSaved.getPagIdIncasso().getId());
		}

	}
	
	@Override
	public VistaVersamentoNonRendicontato get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private VistaVersamentoNonRendicontato _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		IField idField = new CustomField("id", Long.class, "id", this.getVistaVersamentoNonRendicontatoFieldConverter().toTable(VistaVersamentoNonRendicontato.model()));
		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);
		
		expression.equals(idField, tableId);
		List<VistaVersamentoNonRendicontato> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);
		
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
				
		boolean existsVistaVersamentoNonRendicontato = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getVistaVersamentoNonRendicontatoFieldConverter().toTable(VistaVersamentoNonRendicontato.model()));
		sqlQueryObject.addSelectField(this.getVistaVersamentoNonRendicontatoFieldConverter().toColumn(VistaVersamentoNonRendicontato.model().SNG_COD_SING_VERS_ENTE,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists vistaVersamentoNonRendicontato
		existsVistaVersamentoNonRendicontato = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsVistaVersamentoNonRendicontato;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		String tableVistaVersamentiNonRendicontati = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model());
		String tableIncassi = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO);
		String tableTributi = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO);
		String tableApplicazioni = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE);
		String tableTipiVersamento = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO);
		String tableTipiVersamentoDominio = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO);
		String tableDomini = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO);
		String tableUO = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO);

		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO,false)){
			sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".pag_id_incasso="+tableIncassi+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO,false)){
			sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".sng_id_tributo="+tableTributi+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE,false)){
			sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_applicazione="+tableApplicazioni+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO,false)){
			sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_tipo_versamento="+tableTipiVersamento+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO,false)){
			sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_dominio="+tableDomini+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_UO,false)){
			sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_uo="+tableUO+".id");
		}
	
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_UO,false)){
				sqlQueryObject.addFromTable(tableUO);
				sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_uo="+tableUO+".id");
			}

			String tableDomini2 = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableUO+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableDomini2 = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_dominio="+tableDomini2+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO,false)){
			if(!expression.inUseModel(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO,false)){
				sqlQueryObject.addFromTable(tableTipiVersamentoDominio);
				sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".vrs_id_tipo_versamento_dominio="+tableTipiVersamentoDominio+".id");
			}

			String tableTipiVeramento2 = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableTipiVersamentoDominio+".id_tipo_versamento="+tableTipiVeramento2+".id");
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO,false)){
			if(!expression.inUseModel(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO,false)){
				sqlQueryObject.addFromTable(tableTributi);
				sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".sng_id_tributo="+tableTributi+".id");
			}

			String tableTipiTributo = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO);
			sqlQueryObject.addWhereCondition(tableTributi+".id_tipo_tributo="+tableTipiTributo+".id");
			
		}
		
		if(expression.inUseModel(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO,false)){
			if(!expression.inUseModel(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO,false)){
				sqlQueryObject.addFromTable(tableTributi);
				sqlQueryObject.addWhereCondition(tableVistaVersamentiNonRendicontati+".sng_id_tributo="+tableTributi+".id");
			}

			String tableDomini2 = this.getVistaVersamentoNonRendicontatoFieldConverter().toAliasTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableDomini2+".id="+tableTributi+".id_dominio");
		}
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdVistaVersamentoNonRendicontato(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		VistaVersamentoNonRendicontatoFieldConverter converter = this.getVistaVersamentoNonRendicontatoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// VistaVersamentoNonRendicontato.model()
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model()))
			));

		// VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO))
			));

		// VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_DOMINIO))
			));

		// VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().SNG_ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_TIPO_VERSAMENTO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOMINIO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_UO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_UO.ID_DOMINIO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_APPLICAZIONE))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_APPLICAZIONE))
			));

		// VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().VRS_ID_DOCUMENTO.ID_DOMINIO))
			));

		// VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO
		mapTableToPKColumn.put(converter.toTable(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(VistaVersamentoNonRendicontato.model().PAG_ID_INCASSO))
			));
        
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaVersamentoNonRendicontatoFieldConverter().toTable(VistaVersamentoNonRendicontato.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getVistaVersamentoNonRendicontatoFieldConverter().toTable(VistaVersamentoNonRendicontato.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getVistaVersamentoNonRendicontatoFieldConverter(), VistaVersamentoNonRendicontato.model(), objectIdClass, listaQuery);
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

		// Object _vistaVersamentoNonRendicontato
		sqlQueryObjectGet.addFromTable(this.getVistaVersamentoNonRendicontatoFieldConverter().toTable(VistaVersamentoNonRendicontato.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaVersamentoNonRendicontato
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaVersamentoNonRendicontato = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_vistaVersamentoNonRendicontato = new ArrayList<Class<?>>();
		listaFieldIdReturnType_vistaVersamentoNonRendicontato.add(Long.class);
		it.govpay.orm.IdPagamento id_vistaVersamentoNonRendicontato = null;
		List<Object> listaFieldId_vistaVersamentoNonRendicontato = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_vistaVersamentoNonRendicontato, searchParams_vistaVersamentoNonRendicontato);
		if(listaFieldId_vistaVersamentoNonRendicontato==null || listaFieldId_vistaVersamentoNonRendicontato.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _vistaVersamentoNonRendicontato
			id_vistaVersamentoNonRendicontato = new it.govpay.orm.IdPagamento();
			id_vistaVersamentoNonRendicontato.setId((Long) listaFieldId_vistaVersamentoNonRendicontato.get(0));
		}
		
		return id_vistaVersamentoNonRendicontato;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdVistaVersamentoNonRendicontato(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdVistaVersamentoNonRendicontato(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdPagamento id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _vistaVersamentoNonRendicontato
		sqlQueryObjectGet.addFromTable(this.getVistaVersamentoNonRendicontatoFieldConverter().toTable(VistaVersamentoNonRendicontato.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition("id=?");

		// Recupero _vistaVersamentoNonRendicontato
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_vistaVersamentoNonRendicontato = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id.getIdPagamento(),Long.class),
		};
		Long id_vistaVersamentoNonRendicontato = null;
		try{
			id_vistaVersamentoNonRendicontato = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_vistaVersamentoNonRendicontato);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_vistaVersamentoNonRendicontato==null || id_vistaVersamentoNonRendicontato<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_vistaVersamentoNonRendicontato;
	}
}
