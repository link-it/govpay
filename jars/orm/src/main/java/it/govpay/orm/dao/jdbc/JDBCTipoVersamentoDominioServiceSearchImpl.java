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

import org.openspcoop2.generic_project.beans.AliasField;
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
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.generic_project.utils.UtilsTemplate;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;

import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTipoVersamento;
import it.govpay.orm.IdTipoVersamentoDominio;
import it.govpay.orm.TipoVersamento;
import it.govpay.orm.TipoVersamentoDominio;
import it.govpay.orm.dao.jdbc.converter.TipoVersamentoDominioFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.TipoVersamentoDominioFetch;

/**     
 * JDBCTipoVersamentoDominioServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTipoVersamentoDominioServiceSearchImpl implements IJDBCServiceSearchWithId<TipoVersamentoDominio, IdTipoVersamentoDominio, JDBCServiceManager> {

	private TipoVersamentoDominioFieldConverter _tipoVersamentoDominioFieldConverter = null;
	public TipoVersamentoDominioFieldConverter getTipoVersamentoDominioFieldConverter() {
		if(this._tipoVersamentoDominioFieldConverter==null){
			this._tipoVersamentoDominioFieldConverter = new TipoVersamentoDominioFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._tipoVersamentoDominioFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getTipoVersamentoDominioFieldConverter();
	}
	
	private TipoVersamentoDominioFetch tipoVersamentoDominioFetch = new TipoVersamentoDominioFetch();
	public TipoVersamentoDominioFetch getTipoVersamentoDominioFetch() {
		return this.tipoVersamentoDominioFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getTipoVersamentoDominioFetch();
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
	public IdTipoVersamentoDominio convertToId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TipoVersamentoDominio tipoVersamentoDominio) throws NotImplementedException, ServiceException, Exception{
	
		IdTipoVersamentoDominio idTipoVersamentoDominio = new IdTipoVersamentoDominio();
		IdTipoVersamento idTipoVersamento = new IdTipoVersamento();
		idTipoVersamento.setCodTipoVersamento(tipoVersamentoDominio.getTipoVersamento().getCodTipoVersamento());
		idTipoVersamento.setId(tipoVersamentoDominio.getTipoVersamento().getId());
		idTipoVersamentoDominio.setIdTipoVersamento(idTipoVersamento);
		idTipoVersamentoDominio.setIdDominio(tipoVersamentoDominio.getIdDominio());
	
		return idTipoVersamentoDominio;
	}
	
	@Override
	public TipoVersamentoDominio get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {
		Long id_tipoVersamentoDominio = ( (id!=null && id.getId()!=null && id.getId()>0) ? id.getId() : this.findIdTipoVersamentoDominio(jdbcProperties, log, connection, sqlQueryObject, id, true));
		return this._get(jdbcProperties, log, connection, sqlQueryObject, id_tipoVersamentoDominio,idMappingResolutionBehaviour);
		
		
	}
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id) throws MultipleResultException, NotImplementedException, ServiceException,Exception {

		Long id_tipoVersamentoDominio = this.findIdTipoVersamentoDominio(jdbcProperties, log, connection, sqlQueryObject, id, false);
		return id_tipoVersamentoDominio != null && id_tipoVersamentoDominio > 0;
		
	}
	
	@Override
	public List<IdTipoVersamentoDominio> findAllIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

		List<IdTipoVersamentoDominio> list = new ArrayList<IdTipoVersamentoDominio>();

		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}

		try{
			List<IField> fields = new ArrayList<>();
			fields.add(TipoVersamentoDominio.model().ID_DOMINIO.COD_DOMINIO);
			fields.add(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				list.add(this.convertToId(jdbcProperties, log, connection, sqlQueryObject, (TipoVersamentoDominio)this.getTipoVersamentoDominioFetch().fetch(jdbcProperties.getDatabase(), TipoVersamentoDominio.model(), map)));
			}
		} catch(NotFoundException e) {}
        
        
        return list;
		
	}
	
	@Override
	public List<TipoVersamentoDominio> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        List<TipoVersamentoDominio> list = new ArrayList<TipoVersamentoDominio>();
        
        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
        	idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
     		
        try{  
			List<IField> fields = new ArrayList<>();
			AliasField tributoId = new AliasField(new CustomField("id", Long.class, "id", this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())), "id");
			fields.add(tributoId);
			fields.add(TipoVersamentoDominio.model().ABILITATO);
			fields.add(TipoVersamentoDominio.model().CODIFICA_IUV);
			fields.add(TipoVersamentoDominio.model().TIPO);
			fields.add(TipoVersamentoDominio.model().PAGA_TERZI);
			fields.add(TipoVersamentoDominio.model().FORM_DEFINIZIONE);
			fields.add(TipoVersamentoDominio.model().FORM_TIPO);
			fields.add(TipoVersamentoDominio.model().VALIDAZIONE_DEFINIZIONE);
			fields.add(TipoVersamentoDominio.model().TRASFORMAZIONE_DEFINIZIONE);
			fields.add(TipoVersamentoDominio.model().TRASFORMAZIONE_TIPO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_AVVISO_ABILITATO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_AVVISO_TIPO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_AVVISO_PDF);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_AVVISO_MESSAGGIO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_AVVISO_OGGETTO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_ABILITATO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_TIPO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_PDF);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_MESSAGGIO);
			fields.add(TipoVersamentoDominio.model().PROMEMORIA_RICEVUTA_OGGETTO);
			fields.add(TipoVersamentoDominio.model().COD_APPLICAZIONE);
			fields.add(TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE);
			fields.add(TipoVersamentoDominio.model().TRAC_CSV_TIPO);
			fields.add(TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA);
			fields.add(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA);
			fields.add(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA);

			AliasField tipoTributoId = new AliasField(new CustomField("tipoVersamento.id", Long.class, "id", this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO)),
					this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO)+"_id");
			fields.add(tipoTributoId);
			AliasField codiceTributoIuvAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.CODIFICA_IUV);
			fields.add(codiceTributoIuvAlias);
			AliasField onlineAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TIPO);
			fields.add(onlineAlias);
			AliasField pagaTerziAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAGA_TERZI);
			fields.add(pagaTerziAlias);
			AliasField abilitatoAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO);
			fields.add(abilitatoAlias);
			AliasField formDefinizioneAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_DEFINIZIONE);
			fields.add(formDefinizioneAlias);
			AliasField formTipoAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.FORM_TIPO);
			fields.add(formTipoAlias);
			AliasField validazioneDefinizioneAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.VALIDAZIONE_DEFINIZIONE);
			fields.add(validazioneDefinizioneAlias);
			AliasField trasformazioneDefinizioneAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_DEFINIZIONE);
			fields.add(trasformazioneDefinizioneAlias);
			AliasField trasformazioneTipoAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRASFORMAZIONE_TIPO);
			fields.add(trasformazioneTipoAlias);
			fields.add(this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_ABILITATO));
			AliasField promemoriaAvvisoTipoAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_TIPO);
			fields.add(promemoriaAvvisoTipoAlias);
			AliasField promemoriaAvvisoPdfAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_PDF);
			fields.add(promemoriaAvvisoPdfAlias);
			AliasField promemoriaAvvisoMessaggioAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_MESSAGGIO);
			fields.add(promemoriaAvvisoMessaggioAlias);
			AliasField promemoriaAvvisoOggettoAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_AVVISO_OGGETTO);
			fields.add(promemoriaAvvisoOggettoAlias);
			fields.add(this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_ABILITATO));
			AliasField promemoriaRicevutaTipoAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_TIPO);
			fields.add(promemoriaRicevutaTipoAlias);
			AliasField promemoriaRicevutaPdfAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_PDF);
			fields.add(promemoriaRicevutaPdfAlias);
			AliasField promemoriaRicevutaMessaggioAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_MESSAGGIO);
			fields.add(promemoriaRicevutaMessaggioAlias);
			AliasField promemoriaRicevutaOggettoAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.PROMEMORIA_RICEVUTA_OGGETTO);
			fields.add(promemoriaRicevutaOggettoAlias);
			AliasField codApplicazioneAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_APPLICAZIONE);
			fields.add(codApplicazioneAlias);
			AliasField visualizzazioneDefinizioneAlias = this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.VISUALIZZAZIONE_DEFINIZIONE);
			fields.add(visualizzazioneDefinizioneAlias);
			fields.add(this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO));
			fields.add(this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA));
			fields.add(this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA));
			fields.add(this.getAliasField(TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA));
			
			fields.add(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO);
			fields.add(TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE);
			

			fields.add(new CustomField("id_dominio", Long.class, "id_dominio", this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())));
			
			sqlQueryObject.setANDLogicOperator(true);
			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));
			
			for(Map<String, Object> map: returnMap) {
				Long idDominio = (Long) map.remove("id_dominio");
				
				TipoVersamentoDominio tipoVersamentoDominio = (TipoVersamentoDominio)this.getTipoVersamentoDominioFetch().fetch(jdbcProperties.getDatabase(), TipoVersamentoDominio.model(), map);
				
				tipoVersamentoDominio.setTipoVersamento((TipoVersamento)this.getTipoVersamentoDominioFetch().fetch(jdbcProperties.getDatabase(), TipoVersamentoDominio.model().TIPO_VERSAMENTO, map));
				
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdDominio id_tipoVersamentoDominio_Dominio = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_tipoVersamentoDominio_Dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findId(idDominio, false);
						}else{
							id_tipoVersamentoDominio_Dominio = new it.govpay.orm.IdDominio();
						}
						id_tipoVersamentoDominio_Dominio.setId(idDominio);
						tipoVersamentoDominio.setIdDominio(id_tipoVersamentoDominio_Dominio);
				}

				list.add(tipoVersamentoDominio);
			
			}
        } catch(NotFoundException e) {}

        return list;      
		
	}
	
	private AliasField getAliasField(IField field) throws ExpressionException {
		String toColumnFalse = this.getFieldConverter().toColumn(field,false);
		String toTable = this.getFieldConverter().toTable(field);
//		String toColumnTrue = this.getFieldConverter().toColumn(field,true);
		IField customField = new CustomField("tipoVersamento." + field.getFieldName(), field.getFieldType(), toColumnFalse, toTable);
		return new AliasField(customField, toTable + "_" + toColumnFalse);
	}
	
	@Override
	public TipoVersamentoDominio find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
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
												this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model());
		
		sqlQueryObject.addSelectCountField(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())+".id","tot",true);
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(),listaQuery);
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id) throws NotFoundException, NotImplementedException, ServiceException,Exception {
		
		Long id_tipoVersamentoDominio = this.findIdTipoVersamentoDominio(jdbcProperties, log, connection, sqlQueryObject, id, true);
        return this._inUse(jdbcProperties, log, connection, sqlQueryObject, id_tipoVersamentoDominio);
		
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
												this.getTipoVersamentoDominioFieldConverter(), field);

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
        						expression, this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(),
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
        						this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(), 
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
        						this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(), 
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
			return new JDBCExpression(this.getTipoVersamentoDominioFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getTipoVersamentoDominioFieldConverter());
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
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id, TipoVersamentoDominio obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,id,null));
	}
	
	@Override
	public void mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TipoVersamentoDominio obj) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		_mappingTableIds(jdbcProperties,log,connection,sqlQueryObject,obj,
				this.get(jdbcProperties,log,connection,sqlQueryObject,tableId,null));
	}
	private void _mappingTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TipoVersamentoDominio obj, TipoVersamentoDominio imgSaved) throws NotFoundException,NotImplementedException,ServiceException,Exception{
		if(imgSaved==null){
			return;
		}
		obj.setId(imgSaved.getId());
		if(obj.getTipoVersamento()!=null && 
				imgSaved.getTipoVersamento()!=null){
			obj.getTipoVersamento().setId(imgSaved.getTipoVersamento().getId());
		}
		if(obj.getIdDominio()!=null && 
				imgSaved.getIdDominio()!=null){
			obj.getIdDominio().setId(imgSaved.getIdDominio().getId());
		}

	}
	
	@Override
	public TipoVersamentoDominio get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private TipoVersamentoDominio _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	
		IField idField = new CustomField("id", Long.class, "id", this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()));

		JDBCPaginatedExpression expression = this.newPaginatedExpression(log);

		expression.equals(idField, tableId);
		List<TipoVersamentoDominio> lst = this.findAll(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), expression, idMappingResolutionBehaviour);

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
				
		boolean existsTipoVersamentoDominio = false;

		sqlQueryObject = sqlQueryObject.newSQLQueryObject();
		sqlQueryObject.setANDLogicOperator(true);

		sqlQueryObject.addFromTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()));
		sqlQueryObject.addSelectField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TIPO,true));
		sqlQueryObject.addWhereCondition("id=?");


		// Exists tipoVersamentoDominio
		existsTipoVersamentoDominio = jdbcUtilities.exists(sqlQueryObject.createSQLQuery(), jdbcProperties.isShowSql(),
			new JDBCObject(tableId,Long.class));

		
        return existsTipoVersamentoDominio;
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
		
		if(expression.inUseModel(TipoVersamentoDominio.model().ID_DOMINIO, false)) {
			String tableName1 = this.getTipoVersamentoDominioFieldConverter().toAliasTable(TipoVersamentoDominio.model());
			String tableName2 = this.getTipoVersamentoDominioFieldConverter().toAliasTable(TipoVersamentoDominio.model().ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}
		
		if(expression.inUseModel(TipoVersamentoDominio.model().TIPO_VERSAMENTO, false)) {
			String tableName1 = this.getTipoVersamentoDominioFieldConverter().toAliasTable(TipoVersamentoDominio.model());
			String tableName2 = this.getTipoVersamentoDominioFieldConverter().toAliasTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO);
			sqlQueryObject.addWhereCondition(tableName1+".id_tipo_versamento="+tableName2+".id");
		}
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		Long longId = this.findIdTipoVersamentoDominio(jdbcProperties, log, connection, sqlQueryObject.newSQLQueryObject(), id, true);
		rootTableIdValues.add(longId);
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		TipoVersamentoDominioFieldConverter converter = this.getTipoVersamentoDominioFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		// TipoVersamentoDominio.model()
		mapTableToPKColumn.put(converter.toTable(TipoVersamentoDominio.model()),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(TipoVersamentoDominio.model()))
			));

		// TipoVersamentoDominio.model().TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO))
			));

		// TipoVersamentoDominio.model().ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(TipoVersamentoDominio.model().ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(TipoVersamentoDominio.model().ID_DOMINIO))
			));

        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getTipoVersamentoDominioFieldConverter(), TipoVersamentoDominio.model(), objectIdClass, listaQuery);
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
	public IdTipoVersamentoDominio findId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		// Object _tipoVersamentoDominio
		sqlQueryObjectGet.addFromTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()));
		sqlQueryObjectGet.addFromTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model().ID_DOMINIO));
		sqlQueryObjectGet.addFromTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO));
		sqlQueryObjectGet.addSelectField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO,true));
		sqlQueryObjectGet.addSelectField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().ID_DOMINIO.COD_DOMINIO,true));
		sqlQueryObjectGet.setANDLogicOperator(true);
		sqlQueryObjectGet.addWhereCondition(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())+".id=?");
		sqlQueryObjectGet.addWhereCondition(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model().ID_DOMINIO)+".id="+this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())+".id_dominio");
		sqlQueryObjectGet.addWhereCondition(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model().TIPO_VERSAMENTO)+".id="+this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model())+".id_tipo_versamento");

		// Recupero _tipoVersamentoDominio
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_tipoVersamentoDominio = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tableId,Long.class)
		};
		List<Class<?>> listaFieldIdReturnType_tipoVersamentoDominio = new ArrayList<Class<?>>();
		listaFieldIdReturnType_tipoVersamentoDominio.add(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO.getFieldType());
		listaFieldIdReturnType_tipoVersamentoDominio.add(TipoVersamentoDominio.model().ID_DOMINIO.COD_DOMINIO.getFieldType());
		it.govpay.orm.IdTipoVersamentoDominio id_tipoVersamentoDominio = null;
		List<Object> listaFieldId_tipoVersamentoDominio = jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
				listaFieldIdReturnType_tipoVersamentoDominio, searchParams_tipoVersamentoDominio);
		if(listaFieldId_tipoVersamentoDominio==null || listaFieldId_tipoVersamentoDominio.size()<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		else{
			// set _tipoVersamentoDominio
			id_tipoVersamentoDominio = new it.govpay.orm.IdTipoVersamentoDominio();
			IdTipoVersamento idTipoVersamento = new IdTipoVersamento();
			idTipoVersamento.setCodTipoVersamento((String)listaFieldId_tipoVersamentoDominio.get(0));
			id_tipoVersamentoDominio.setIdTipoVersamento(idTipoVersamento);
			IdDominio idUo = new IdDominio();
			idUo.setCodDominio((String)listaFieldId_tipoVersamentoDominio.get(1));
			
			id_tipoVersamentoDominio.setIdDominio(idUo);
		}
		
		return id_tipoVersamentoDominio;
		
	}

	@Override
	public Long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException, Exception {
	
		return this.findIdTipoVersamentoDominio(jdbcProperties,log,connection,sqlQueryObject,id,throwNotFound);
			
	}
	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
	protected Long findIdTipoVersamentoDominio(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id, boolean throwNotFound) throws NotFoundException, ServiceException, NotImplementedException, Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);

		ISQLQueryObject sqlQueryObjectGet = sqlQueryObject.newSQLQueryObject();

		Long idDominio = id.getIdDominio().getId();
		if(idDominio == null || idDominio<=0) {
			idDominio = this.getServiceManager().getDominioServiceSearch().get(id.getIdDominio()).getId();
		}
		
		Long idTipoVersamento = id.getIdTipoVersamento().getId();
		if(idTipoVersamento == null || idTipoVersamento<=0) {
			idTipoVersamento = this.getServiceManager().getTipoVersamentoServiceSearch().get(id.getIdTipoVersamento()).getId();
		}

		// Object _tipoVersamentoDominio
		sqlQueryObjectGet.addFromTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()));
		sqlQueryObjectGet.addSelectField("id");
		sqlQueryObjectGet.setANDLogicOperator(true);
//		sqlQueryObjectGet.setSelectDistinct(true);
		sqlQueryObjectGet.addWhereCondition("id_tipo_versamento=?");
		sqlQueryObjectGet.addWhereCondition("id_dominio=?");

		// Recupero _tipoVersamentoDominio
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] searchParams_tipoVersamentoDominio = new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject [] { 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(idTipoVersamento,Long.class),
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(idDominio,Long.class)
		};
		Long id_tipoVersamentoDominio = null;
		try{
			id_tipoVersamentoDominio = (Long) jdbcUtilities.executeQuerySingleResult(sqlQueryObjectGet.createSQLQuery(), jdbcProperties.isShowSql(),
						Long.class, searchParams_tipoVersamentoDominio);
		}catch(NotFoundException notFound){
			if(throwNotFound){
				throw new NotFoundException(notFound);
			}
		}
		if(id_tipoVersamentoDominio==null || id_tipoVersamentoDominio<=0){
			if(throwNotFound){
				throw new NotFoundException("Not Found");
			}
		}
		
		return id_tipoVersamentoDominio;
	}
}
