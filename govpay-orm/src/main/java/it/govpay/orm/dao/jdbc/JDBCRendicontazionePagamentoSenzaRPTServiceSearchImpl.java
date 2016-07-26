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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.generic_project.dao.jdbc.utils.IJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceSearchWithoutId;
import org.openspcoop2.generic_project.utils.UtilsTemplate;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.InUse;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UnionExpression;
import org.openspcoop2.generic_project.beans.Union;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;

import it.govpay.orm.dao.jdbc.converter.RendicontazionePagamentoSenzaRPTFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.RendicontazionePagamentoSenzaRPTFetch;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;
import it.govpay.orm.RendicontazioneSenzaRPT;
import it.govpay.orm.Versamento;
import it.govpay.orm.FR;
import it.govpay.orm.RendicontazionePagamentoSenzaRPT;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.FrApplicazione;

/**     
 * JDBCRendicontazionePagamentoSenzaRPTServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRendicontazionePagamentoSenzaRPTServiceSearchImpl implements IJDBCServiceSearchWithoutId<RendicontazionePagamentoSenzaRPT, JDBCServiceManager> {

	private RendicontazionePagamentoSenzaRPTFieldConverter _rendicontazionePagamentoSenzaRPTFieldConverter = null;
	public RendicontazionePagamentoSenzaRPTFieldConverter getRendicontazionePagamentoSenzaRPTFieldConverter() {
		if(this._rendicontazionePagamentoSenzaRPTFieldConverter==null){
			this._rendicontazionePagamentoSenzaRPTFieldConverter = new RendicontazionePagamentoSenzaRPTFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._rendicontazionePagamentoSenzaRPTFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getRendicontazionePagamentoSenzaRPTFieldConverter();
	}
	
	private RendicontazionePagamentoSenzaRPTFetch rendicontazionePagamentoSenzaRPTFetch = new RendicontazionePagamentoSenzaRPTFetch();
	public RendicontazionePagamentoSenzaRPTFetch getRendicontazionePagamentoSenzaRPTFetch() {
		return this.rendicontazionePagamentoSenzaRPTFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return getRendicontazionePagamentoSenzaRPTFetch();
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
	public List<RendicontazionePagamentoSenzaRPT> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {


        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
        List<RendicontazionePagamentoSenzaRPT> list = new ArrayList<RendicontazionePagamentoSenzaRPT>();
        
        try{
			List<IField> fields = new ArrayList<IField>();
			fields.add(new CustomField("FR.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().FR)));
			fields.add(new CustomField("id_psp", Long.class, "id_psp", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().FR)));
			fields.add(new CustomField("id_dominio", Long.class, "id_dominio", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().FR)));
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.COD_FLUSSO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.STATO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.DESCRIZIONE_STATO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.IUR);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.ANNO_RIFERIMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.DATA_ORA_FLUSSO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.DATA_REGOLAMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.DATA_ACQUISIZIONE);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.NUMERO_PAGAMENTI);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.IMPORTO_TOTALE_PAGAMENTI);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.COD_BIC_RIVERSAMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR.XML);
        
			fields.add(new CustomField("FrApplicazione.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE)));
			fields.add(new CustomField("id_applicazione", Long.class, "id_applicazione", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE)));
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.NUMERO_PAGAMENTI);
			fields.add(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.IMPORTO_TOTALE_PAGAMENTI);

			fields.add(new CustomField("RendicontazioneSenzaRPT.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT)));
			fields.add(new CustomField("id_iuv", Long.class, "id_iuv", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT)));
			fields.add(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IMPORTO_PAGATO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.IUR);
			fields.add(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.RENDICONTAZIONE_DATA);

			fields.add(new CustomField("SingoloVersamento.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO)));
			fields.add(new CustomField("id_tributo", Long.class, "id_tributo", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO)));
			fields.add(new CustomField("id_iban_accredito", Long.class, "id_iban_accredito", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO)));
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_BOLLO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA);
			fields.add(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.NOTE);

			fields.add(new CustomField("Versamento.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO)));
			fields.add(new CustomField("id_uo", Long.class, "id_uo", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO)));
			fields.add(new CustomField("versamento_id_applicazione", Long.class, "id_applicazione", this.getFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO)));
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_ENTE);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.IMPORTO_TOTALE);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.STATO_VERSAMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DESCRIZIONE_STATO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.AGGIORNABILE);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_CREAZIONE);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_SCADENZA);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.CAUSALE_VERSAMENTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_ANAGRAFICA);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_INDIRIZZO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CIVICO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_CAP);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_LOCALITA);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_PROVINCIA);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.DEBITORE_NAZIONE);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_LOTTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_VERSAMENTO_LOTTO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_ANNO_TRIBUTARIO);
			fields.add(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.COD_BUNDLEKEY);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {

				Long idFK_fr_psp = (Long) map.remove("id_psp");
				Long idFK_fr_dominio = (Long) map.remove("id_dominio");
				Long idFK_frApplicazione_applicazione = (Long) map.remove("id_applicazione");
				Object idFK_rendicontazionePagamento_singoloVersamento_tributoObj = map.remove("id_tributo");
				Object idFK_rendicontazionePagamento_singoloVersamento_ibanAccreditoObj = map.remove("id_iban_accredito");
				
				Long idFK_rendicontazionePagamento_versamento_uo = (Long) map.remove("id_uo");
				Long idFK_rendicontazionePagamento_versamento_applicazione = (Long) map.remove("versamento_id_applicazione");

				RendicontazionePagamentoSenzaRPT rendicontazionePagamentoSenzaRPT = new RendicontazionePagamentoSenzaRPT(); 
						
				FR fr = (FR) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamentoSenzaRPT.model().FR, map);
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						
						it.govpay.orm.IdPsp id_frFiltroApp_fr_psp = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_frFiltroApp_fr_psp = ((JDBCPspServiceSearch)(this.getServiceManager().getPspServiceSearch())).findId(idFK_fr_psp, false);
						}else{
							id_frFiltroApp_fr_psp = new it.govpay.orm.IdPsp();
						}
						id_frFiltroApp_fr_psp.setId(idFK_fr_psp);
						fr.setIdPsp(id_frFiltroApp_fr_psp);
					}

					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdDominio id_frFiltroApp_fr_dominio = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_frFiltroApp_fr_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findId(idFK_fr_dominio, false);
						}else{
							id_frFiltroApp_fr_dominio = new it.govpay.orm.IdDominio();
						}
						id_frFiltroApp_fr_dominio.setId(idFK_fr_dominio);
						fr.setIdDominio(id_frFiltroApp_fr_dominio);
					}

				rendicontazionePagamentoSenzaRPT.setFr(fr);
				
				FrApplicazione frApplicazione = (FrApplicazione) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE, map);
				

				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdApplicazione id_frFiltroApp_frApplicazione_applicazione = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_frFiltroApp_frApplicazione_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idFK_frApplicazione_applicazione, false);
						}else{
							id_frFiltroApp_frApplicazione_applicazione = new it.govpay.orm.IdApplicazione();
						}
						id_frFiltroApp_frApplicazione_applicazione.setId(idFK_frApplicazione_applicazione);
						frApplicazione.setIdApplicazione(id_frFiltroApp_frApplicazione_applicazione);
					}

					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						
						it.govpay.orm.IdFr id_frFiltroApp_frApplicazione_fr = null;
						Long idFK_frFiltroApp_frApplicazione_fr = fr.getId();
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_frFiltroApp_frApplicazione_fr = ((JDBCFRServiceSearch)(this.getServiceManager().getFRServiceSearch())).findId(idFK_frFiltroApp_frApplicazione_fr , false);
						}else{
							id_frFiltroApp_frApplicazione_fr = new it.govpay.orm.IdFr();
						}
						id_frFiltroApp_frApplicazione_fr.setId(idFK_frFiltroApp_frApplicazione_fr);
						frApplicazione.setIdFr(id_frFiltroApp_frApplicazione_fr);

					}

				rendicontazionePagamentoSenzaRPT.setFrApplicazione(frApplicazione);

				
				Versamento versamento = (Versamento) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamentoSenzaRPT.model().VERSAMENTO, map);

				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdUo id_rendicontazionePagamento_versamento_uo = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazionePagamento_versamento_uo = ((JDBCUoServiceSearch)(this.getServiceManager().getUoServiceSearch())).findId(idFK_rendicontazionePagamento_versamento_uo, false);
						}else{
							id_rendicontazionePagamento_versamento_uo = new it.govpay.orm.IdUo();
						}
						id_rendicontazionePagamento_versamento_uo.setId(idFK_rendicontazionePagamento_versamento_uo);
						versamento.setIdUo(id_rendicontazionePagamento_versamento_uo);
					}

					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdApplicazione id_rendicontazionePagamento_versamento_applicazione = null;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazionePagamento_versamento_applicazione = ((JDBCApplicazioneServiceSearch)(this.getServiceManager().getApplicazioneServiceSearch())).findId(idFK_rendicontazionePagamento_versamento_applicazione, false);
						}else{
							id_rendicontazionePagamento_versamento_applicazione = new it.govpay.orm.IdApplicazione();
						}
						id_rendicontazionePagamento_versamento_applicazione.setId(idFK_rendicontazionePagamento_versamento_applicazione);
						versamento.setIdApplicazione(id_rendicontazionePagamento_versamento_applicazione);
					}

				rendicontazionePagamentoSenzaRPT.setVersamento(versamento);

				SingoloVersamento singoloVersamento = (SingoloVersamento) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO, map);
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						
						it.govpay.orm.IdVersamento id_rendicontazionePagamento_singoloVersamento_versamento = null;
						Long idFK_rendicontazionePagamento_singoloVersamento_versamento = versamento.getId();
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazionePagamento_singoloVersamento_versamento = ((JDBCVersamentoServiceSearch)(this.getServiceManager().getVersamentoServiceSearch())).findId(idFK_rendicontazionePagamento_singoloVersamento_versamento, false);
						}else{
							id_rendicontazionePagamento_singoloVersamento_versamento = new it.govpay.orm.IdVersamento();
						}
						id_rendicontazionePagamento_singoloVersamento_versamento.setId(idFK_rendicontazionePagamento_singoloVersamento_versamento);
						singoloVersamento.setIdVersamento(id_rendicontazionePagamento_singoloVersamento_versamento);
					}

					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						if(idFK_rendicontazionePagamento_singoloVersamento_tributoObj instanceof Long) {
							it.govpay.orm.IdTributo id_rendicontazionePagamento_singoloVersamento_tributo = null;
							Long idFK_rendicontazionePagamento_singoloVersamento_tributo = (Long) idFK_rendicontazionePagamento_singoloVersamento_tributoObj;
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_rendicontazionePagamento_singoloVersamento_tributo = ((JDBCTributoServiceSearch)(this.getServiceManager().getTributoServiceSearch())).findId(idFK_rendicontazionePagamento_singoloVersamento_tributo, false);
							}else{
								id_rendicontazionePagamento_singoloVersamento_tributo = new it.govpay.orm.IdTributo();
							}
							id_rendicontazionePagamento_singoloVersamento_tributo.setId(idFK_rendicontazionePagamento_singoloVersamento_tributo);
							singoloVersamento.setIdTributo(id_rendicontazionePagamento_singoloVersamento_tributo);
						}
					}

					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						if(idFK_rendicontazionePagamento_singoloVersamento_ibanAccreditoObj instanceof Long) {
							it.govpay.orm.IdIbanAccredito id_rendicontazionePagamento_singoloVersamento_ibanAccredito = null;
							Long idFK_rendicontazionePagamento_singoloVersamento_ibanAccredito = (Long) idFK_rendicontazionePagamento_singoloVersamento_ibanAccreditoObj;
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_rendicontazionePagamento_singoloVersamento_ibanAccredito = ((JDBCIbanAccreditoServiceSearch)(this.getServiceManager().getIbanAccreditoServiceSearch())).findId(idFK_rendicontazionePagamento_singoloVersamento_ibanAccredito, false);
							}else{
								id_rendicontazionePagamento_singoloVersamento_ibanAccredito = new it.govpay.orm.IdIbanAccredito();
							}
							id_rendicontazionePagamento_singoloVersamento_ibanAccredito.setId(idFK_rendicontazionePagamento_singoloVersamento_ibanAccredito);
							singoloVersamento.setIdIbanAccredito(id_rendicontazionePagamento_singoloVersamento_ibanAccredito);
						}
					}
				rendicontazionePagamentoSenzaRPT.setSingoloVersamento(singoloVersamento);

				RendicontazioneSenzaRPT rendicontazioneSenzaRPT = (RendicontazioneSenzaRPT) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT, map);
				
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdIuv id_rendicontazionePagamento_pagamento_iuv = null;
						Long idFK_rendicontazionePagamento_pagamento_iuv = (Long) map.remove("id_iuv");
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazionePagamento_pagamento_iuv = ((JDBCIUVServiceSearch)(this.getServiceManager().getIUVServiceSearch())).findId(idFK_rendicontazionePagamento_pagamento_iuv, false);
						}else{
							id_rendicontazionePagamento_pagamento_iuv = new it.govpay.orm.IdIuv();
						}
						id_rendicontazionePagamento_pagamento_iuv.setId(idFK_rendicontazionePagamento_pagamento_iuv);
						rendicontazioneSenzaRPT.setIdIuv(id_rendicontazionePagamento_pagamento_iuv);
					}
				
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdFrApplicazione id_rendicontazionePagamento_pagamento_iuv = null;
						Long idFK_rendicontazionePagamento_pagamento_iuv = (Long) map.remove("id_iuv");
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazionePagamento_pagamento_iuv = ((JDBCFrApplicazioneServiceSearch)(this.getServiceManager().getFrApplicazioneServiceSearch())).findId(idFK_rendicontazionePagamento_pagamento_iuv, false);
						}else{
							id_rendicontazionePagamento_pagamento_iuv = new it.govpay.orm.IdFrApplicazione();
						}
						id_rendicontazionePagamento_pagamento_iuv.setId(idFK_rendicontazionePagamento_pagamento_iuv);
						rendicontazioneSenzaRPT.setIdFrApplicazione(id_rendicontazionePagamento_pagamento_iuv);
					}

					if(idMappingResolutionBehaviour==null ||
							(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
						){
							it.govpay.orm.IdSingoloVersamento id_rendicontazionePagamento_pagamento_singoloVersamento = null;
							Long idFK_rendicontazionePagamento_pagamento_singoloVersamento = singoloVersamento.getId();
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_rendicontazionePagamento_pagamento_singoloVersamento = ((JDBCSingoloVersamentoServiceSearch)(this.getServiceManager().getSingoloVersamentoServiceSearch())).findId(idFK_rendicontazionePagamento_pagamento_singoloVersamento, false);
							}else{
								id_rendicontazionePagamento_pagamento_singoloVersamento = new it.govpay.orm.IdSingoloVersamento();
							}
							id_rendicontazionePagamento_pagamento_singoloVersamento.setId(idFK_rendicontazionePagamento_pagamento_singoloVersamento);
							rendicontazioneSenzaRPT.setIdSingoloVersamento(id_rendicontazionePagamento_pagamento_singoloVersamento);
						}

				rendicontazionePagamentoSenzaRPT.setRendicontazioneSenzaRPT(rendicontazioneSenzaRPT);

				list.add(rendicontazionePagamentoSenzaRPT);
			}
		} catch(NotFoundException e) {}

        return list;      
	}
	
	@Override
	public RendicontazionePagamentoSenzaRPT find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
		throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException,Exception {

        Object id = this._findObjectId(jdbcProperties, log, connection, sqlQueryObject, expression);
        if(id!=null){
        	return this._get(jdbcProperties, log, connection, sqlQueryObject, id, idMappingResolutionBehaviour);
        }else{
        	throw new NotFoundException("Entry with id["+id+"] not found");
        }
		
	}
	
	@Override
	public NonNegativeNumber count(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareCount(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model());
		
		sqlQueryObject.addSelectCountField("tot");
		
		_join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(),listaQuery);
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
												this.getRendicontazionePagamentoSenzaRPTFieldConverter(), field);

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
        						expression, this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), 
        						listaQuery,listaParams);
		
		_join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(),
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
        						this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), 
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
        						this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				_join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), 
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
			return new JDBCExpression(this.getRendicontazionePagamentoSenzaRPTFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getRendicontazionePagamentoSenzaRPTFieldConverter());
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
	public RendicontazionePagamentoSenzaRPT get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		throw new NotImplementedException("Table without long id column PK");
	}
	
	protected RendicontazionePagamentoSenzaRPT _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Object objectId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {

		// Get non implementata, usare la find
        throw new NotImplementedException("NotImplemented");
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		throw new NotImplementedException("Table without long id column PK");
	}
	
	protected boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Object objectId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	

		//exists NON IMPLEMENTATA, usare la search
        throw new NotImplementedException("NotImplemented");
	
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
	
		String fr = this.getRendicontazionePagamentoSenzaRPTFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR);
		String frApp = this.getRendicontazionePagamentoSenzaRPTFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE);
		String rendicontazioneSenzaRPT = this.getRendicontazionePagamentoSenzaRPTFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT);
		String versamento = this.getRendicontazionePagamentoSenzaRPTFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO);
		String singoloVersamento = this.getRendicontazionePagamentoSenzaRPTFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO);
		
		
		sqlQueryObject.addWhereCondition(fr+".id="+frApp+".id_fr");
		sqlQueryObject.addWhereCondition(frApp+".id="+rendicontazioneSenzaRPT+".id_fr_applicazione");
		sqlQueryObject.addWhereCondition(singoloVersamento+".id="+rendicontazioneSenzaRPT+".id_singolo_versamento");
		sqlQueryObject.addWhereCondition(versamento+".id="+singoloVersamento+".id_versamento");
		
		
		if(expression.inUseModel(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE);
			String tableName2 = this.getFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE);
			sqlQueryObject.addWhereCondition(tableName1+".id_applicazione="+tableName2+".id");
		}

		if(expression.inUseModel(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR);
			String tableName2 = this.getFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO);
			sqlQueryObject.addWhereCondition(tableName1+".id_dominio="+tableName2+".id");
		}

		if(expression.inUseModel(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP,false)){
			String tableName1 = this.getFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR);
			String tableName2 = this.getFieldConverter().toAliasTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP);
			sqlQueryObject.addWhereCondition(tableName1+".id_psp="+tableName2+".id");
		}
        
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazionePagamentoSenzaRPT rendicontazionePagamentoSenzaRPT) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<Object>();
		rootTableIdValues.add(rendicontazionePagamentoSenzaRPT.getId());
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		RendicontazionePagamentoSenzaRPTFieldConverter converter = this.getRendicontazionePagamentoSenzaRPTFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<String, List<IField>>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<IField>();

		//		  If a table doesn't have a primary key, don't add it to this map

		// RendicontazionePagamentoSenzaRPT.model().FR
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR))
			));

		// RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_PSP))
			));

		// RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR.ID_DOMINIO))
			));

		// RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE))
			));

		// RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_APPLICAZIONE))
			));

		// RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().FR_APPLICAZIONE.ID_FR))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_FR))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_FR_APPLICAZIONE.ID_APPLICAZIONE))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_IUV.ID_DOMINIO))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO))
			));

		// RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().RENDICONTAZIONE_SENZA_RPT.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO))
			));

		// RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO))
			));

		// RendicontazionePagamentoSenzaRPT.model().VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO))
			));

		// RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO))
			));

		// RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamentoSenzaRPT.model().VERSAMENTO.ID_APPLICAZIONE))
			));


        // Delete this line when you have verified the method
		int throwNotImplemented = 1;
		if(throwNotImplemented==1){
		        throw new NotImplementedException("NotImplemented");
		}
		// Delete this line when you have verified the method
        
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<Long>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getRendicontazionePagamentoSenzaRPTFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().FR)+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;

		
	}
	public List<Object> _findAllObjectIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getRendicontazionePagamentoSenzaRPTFieldConverter().toColumn(RendicontazionePagamentoSenzaRPT.model().FR.ANNO_RIFERIMENTO,true));
		Class<?> objectIdClass = Object.class; 
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model());
		
		_join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), objectIdClass, listaQuery);
        return listObjects;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		throw new NotImplementedException("Table without long id column PK");
		
	}
	
	public Object _findObjectId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
		
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getRendicontazionePagamentoSenzaRPTFieldConverter().toTable(RendicontazionePagamentoSenzaRPT.model().FR)+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model());
		
		_join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getRendicontazionePagamentoSenzaRPTFieldConverter(), RendicontazionePagamentoSenzaRPT.model(), objectIdClass, listaQuery);
		if(res!=null && (((Long) res).longValue()>0) ){
			return ((Long) res).longValue();
		}
		else{
			throw new NotFoundException("Not Found");
		}
		
	}

	@Override
	public InUse inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotFoundException, NotImplementedException, Exception {
		throw new NotImplementedException("Table without long id column PK");
	}

	protected InUse _inUse(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Object objectId) throws ServiceException, NotFoundException, NotImplementedException, Exception {

		InUse inUse = new InUse();
		inUse.setInUse(false);
		
		/* 
		 * TODO: implement code that checks whether the object identified by the id parameter is used by other objects
		*/
		
		// Delete this line when you have implemented the method
		int throwNotImplemented = 1;
		if(throwNotImplemented==1){
		        throw new NotImplementedException("NotImplemented");
		}
		// Delete this line when you have implemented the method

        return inUse;

	}
	

	
	@Override
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
}
