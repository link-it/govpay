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

import it.govpay.orm.FR;
import it.govpay.orm.Pagamento;
import it.govpay.orm.Rendicontazione;
import it.govpay.orm.RendicontazionePagamento;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.converter.RendicontazionePagamentoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.RendicontazionePagamentoFetch;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.InUse;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.Union;
import org.openspcoop2.generic_project.beans.UnionExpression;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceSearchWithoutId;
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

/**     
 * JDBCRendicontazionePagamentoServiceSearchImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCRendicontazionePagamentoServiceSearchImpl implements IJDBCServiceSearchWithoutId<RendicontazionePagamento, JDBCServiceManager> {

	private RendicontazionePagamentoFieldConverter _rendicontazionePagamentoFieldConverter = null;
	public RendicontazionePagamentoFieldConverter getRendicontazionePagamentoFieldConverter() {
		if(this._rendicontazionePagamentoFieldConverter==null){
			this._rendicontazionePagamentoFieldConverter = new RendicontazionePagamentoFieldConverter(this.jdbcServiceManager.getJdbcProperties().getDatabaseType());
		}		
		return this._rendicontazionePagamentoFieldConverter;
	}
	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.getRendicontazionePagamentoFieldConverter();
	}
	
	private RendicontazionePagamentoFetch rendicontazionePagamentoFetch = new RendicontazionePagamentoFetch();
	public RendicontazionePagamentoFetch getRendicontazionePagamentoFetch() {
		return this.rendicontazionePagamentoFetch;
	}
	@Override
	public IJDBCFetch getFetch() {
		return this.getRendicontazionePagamentoFetch();
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
	public List<RendicontazionePagamento> findAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException, ServiceException,Exception {

        // default behaviour (id-mapping)
        if(idMappingResolutionBehaviour==null){
                idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
        }
        List<RendicontazionePagamento> list = new ArrayList<>();
        
        try{
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("FR.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamento.model().FR)));
			fields.add(RendicontazionePagamento.model().FR.COD_DOMINIO);
			fields.add(RendicontazionePagamento.model().FR.COD_PSP);
			fields.add(RendicontazionePagamento.model().FR.COD_FLUSSO);
			fields.add(RendicontazionePagamento.model().FR.STATO);
			fields.add(RendicontazionePagamento.model().FR.DESCRIZIONE_STATO);
			fields.add(RendicontazionePagamento.model().FR.IUR);
			fields.add(RendicontazionePagamento.model().FR.DATA_ORA_FLUSSO);
			fields.add(RendicontazionePagamento.model().FR.DATA_REGOLAMENTO);
			fields.add(RendicontazionePagamento.model().FR.DATA_ACQUISIZIONE);
			fields.add(RendicontazionePagamento.model().FR.NUMERO_PAGAMENTI);
			fields.add(RendicontazionePagamento.model().FR.IMPORTO_TOTALE_PAGAMENTI);
			fields.add(RendicontazionePagamento.model().FR.COD_BIC_RIVERSAMENTO);
			fields.add(RendicontazionePagamento.model().FR.XML);
        
			fields.add(new CustomField("Rendicontazione.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamento.model().RENDICONTAZIONE)));
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.IUV);
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.IUR);
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.IMPORTO_PAGATO);
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.ESITO);
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.DATA);
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.STATO);
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.ANOMALIE);
			fields.add(RendicontazionePagamento.model().RENDICONTAZIONE.IUV);

			fields.add(new CustomField("Pagamento.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamento.model().PAGAMENTO)));
			fields.add(new CustomField("id_rr", Long.class, "id_rr", this.getFieldConverter().toTable(RendicontazionePagamento.model().PAGAMENTO)));
			fields.add(new CustomField("id_rpt", Long.class, "id_rpt", this.getFieldConverter().toTable(RendicontazionePagamento.model().PAGAMENTO)));
			fields.add(RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.IUV);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.IMPORTO_PAGATO);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.IUR);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.COMMISSIONI_PSP);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.TIPO_ALLEGATO);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.ALLEGATO);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.DATA_ACQUISIZIONE_REVOCA);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.CAUSALE_REVOCA);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.DATI_REVOCA);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.IMPORTO_REVOCATO);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.ESITO_REVOCA);
			fields.add(RendicontazionePagamento.model().PAGAMENTO.DATI_ESITO_REVOCA);

			fields.add(new CustomField("SingoloVersamento.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)));
			fields.add(new CustomField("id_tributo", Long.class, "id_tributo", this.getFieldConverter().toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)));
			fields.add(new CustomField("id_iban_accredito", Long.class, "id_iban_accredito", this.getFieldConverter().toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO)));
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.STATO_SINGOLO_VERSAMENTO);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.IMPORTO_SINGOLO_VERSAMENTO);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ANNO_RIFERIMENTO);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_BOLLO);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.HASH_DOCUMENTO);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.PROVINCIA_RESIDENZA);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.TIPO_CONTABILITA);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.CODICE_CONTABILITA);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DESCRIZIONE);
			fields.add(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.DATI_ALLEGATI);

			fields.add(new CustomField("Versamento.id", Long.class, "id", this.getFieldConverter().toTable(RendicontazionePagamento.model().VERSAMENTO)));
			fields.add(new CustomField("id_uo", Long.class, "id_uo", this.getFieldConverter().toTable(RendicontazionePagamento.model().VERSAMENTO)));
			fields.add(new CustomField("versamento_id_applicazione", Long.class, "id_applicazione", this.getFieldConverter().toTable(RendicontazionePagamento.model().VERSAMENTO)));
			fields.add(RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_ENTE);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.IMPORTO_TOTALE);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DESCRIZIONE_STATO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.AGGIORNABILE);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DATA_CREAZIONE);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DATA_SCADENZA);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.CAUSALE_VERSAMENTO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_IDENTIFICATIVO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_ANAGRAFICA);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_INDIRIZZO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CIVICO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_CAP);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_LOCALITA);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_PROVINCIA);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DEBITORE_NAZIONE);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.COD_LOTTO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.COD_VERSAMENTO_LOTTO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.COD_ANNO_TRIBUTARIO);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.COD_BUNDLEKEY);
			fields.add(RendicontazionePagamento.model().VERSAMENTO.DATI_ALLEGATI);

			List<Map<String, Object>> returnMap = this.select(jdbcProperties, log, connection, sqlQueryObject, expression, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {

				Object idFK_rendicontazionePagamento_pagamento_rrObj = map.remove("id_rr");
				Object idFK_rendicontazionePagamento_pagamento_rptObj = map.remove("id_rpt");
				Object idFK_rendicontazionePagamento_singoloVersamento_tributoObj = map.remove("id_tributo");
				Object idFK_rendicontazionePagamento_singoloVersamento_ibanAccreditoObj = map.remove("id_iban_accredito");
				
				Long idFK_rendicontazionePagamento_versamento_uo = (Long) map.remove("id_uo");
				Long idFK_rendicontazionePagamento_versamento_applicazione = (Long) map.remove("versamento_id_applicazione");

				RendicontazionePagamento rendicontazionePagamento = new RendicontazionePagamento(); 
						
				FR fr = (FR) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamento.model().FR, map);
				rendicontazionePagamento.setFr(fr);
				
				Versamento versamento = (Versamento) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamento.model().VERSAMENTO, map);

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

				rendicontazionePagamento.setVersamento(versamento);

				SingoloVersamento singoloVersamento = (SingoloVersamento) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamento.model().SINGOLO_VERSAMENTO, map);
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
				rendicontazionePagamento.setSingoloVersamento(singoloVersamento);

				Pagamento pagamento = (Pagamento) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamento.model().PAGAMENTO, map);
				
				if(idFK_rendicontazionePagamento_pagamento_rptObj instanceof Long)
				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdRpt id_rendicontazionePagamento_pagamento_rpt = null;
						Long idFK_rendicontazionePagamento_pagamento_rpt = (Long) idFK_rendicontazionePagamento_pagamento_rptObj;
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazionePagamento_pagamento_rpt = ((JDBCRPTServiceSearch)(this.getServiceManager().getRPTServiceSearch())).findId(idFK_rendicontazionePagamento_pagamento_rpt, false);
						}else{
							id_rendicontazionePagamento_pagamento_rpt = new it.govpay.orm.IdRpt();
						}
						id_rendicontazionePagamento_pagamento_rpt.setId(idFK_rendicontazionePagamento_pagamento_rpt);
						pagamento.setIdRPT(id_rendicontazionePagamento_pagamento_rpt);
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
						pagamento.setIdSingoloVersamento(id_rendicontazionePagamento_pagamento_singoloVersamento);
					}

					if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						if(idFK_rendicontazionePagamento_pagamento_rrObj instanceof Long) {
							it.govpay.orm.IdRr id_rendicontazionePagamento_pagamento_rr = null;
							Long idFK_rendicontazionePagamento_pagamento_rr = (Long) idFK_rendicontazionePagamento_pagamento_rrObj; 
							if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
								id_rendicontazionePagamento_pagamento_rr = ((JDBCRRServiceSearch)(this.getServiceManager().getRRServiceSearch())).findId(idFK_rendicontazionePagamento_pagamento_rr, false);
							}else{
								id_rendicontazionePagamento_pagamento_rr = new it.govpay.orm.IdRr();
							}
							id_rendicontazionePagamento_pagamento_rr.setId(idFK_rendicontazionePagamento_pagamento_rr);
							pagamento.setIdRr(id_rendicontazionePagamento_pagamento_rr);
						}
					}

				rendicontazionePagamento.setPagamento(pagamento);

				Rendicontazione rendicontazione = (Rendicontazione) this.getFetch().fetch(jdbcProperties.getDatabase(), RendicontazionePagamento.model().RENDICONTAZIONE, map);

				if(idMappingResolutionBehaviour==null ||
						(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) || org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour))
					){
						it.govpay.orm.IdPagamento id_rendicontazionePagamento = null;
						Long idFK_rendicontazionePagamento = pagamento.getId();
						if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
							id_rendicontazionePagamento = ((JDBCPagamentoServiceSearch)(this.getServiceManager().getPagamentoServiceSearch())).findId(idFK_rendicontazionePagamento, false);
						}else{
							id_rendicontazionePagamento = new it.govpay.orm.IdPagamento();
						}
						id_rendicontazionePagamento.setId(idFK_rendicontazionePagamento);
						rendicontazione.setIdPagamento(id_rendicontazionePagamento);
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
						rendicontazione.setIdFR(id_frFiltroApp_frApplicazione_fr);

					}

				rendicontazionePagamento.setRendicontazione(rendicontazione);


				list.add(rendicontazionePagamento);
			}
		} catch(NotFoundException e) {}

        return list;      
		
	}
	
	@Override
	public RendicontazionePagamento find(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) 
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
												this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model().FR);
		
		sqlQueryObject.addSelectCountField(this.getRendicontazionePagamentoFieldConverter().toTable(RendicontazionePagamento.model().FR)+".id","tot");
		
		this._join(expression,sqlQueryObject);
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.count(jdbcProperties, log, connection, sqlQueryObject, expression,
																			this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model().FR,listaQuery);
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
												this.getRendicontazionePagamentoFieldConverter(), field);

			return this._select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression, sqlQueryObjectDistinct);
			
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
			List<Map<String,Object>> list = this._select(jdbcProperties, log, connection, sqlQueryObject, expression);
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
			return this._select(jdbcProperties, log, connection, sqlQueryObject, expression);
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
			return this._select(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression);
		}finally{
			org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.removeFields(sqlQueryObject,paginatedExpression,functionField);
		}
	}
	
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		return this._select(jdbcProperties, log, connection, sqlQueryObject, expression, null);
	}
	protected List<Map<String,Object>> _select(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
												IExpression expression, ISQLQueryObject sqlQueryObjectDistinct) throws ServiceException,NotFoundException,NotImplementedException,Exception {
		
		List<Object> listaQuery = new ArrayList<>();
		List<JDBCObject> listaParams = new ArrayList<>();
		List<Object> returnField = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSelect(jdbcProperties, log, connection, sqlQueryObject, 
        						expression, this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(), 
        						listaQuery,listaParams);
		
		this._join(expression,sqlQueryObject);
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.select(jdbcProperties, log, connection,
        								org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareSqlQueryObjectForSelectDistinct(sqlQueryObject,sqlQueryObjectDistinct), 
        								expression, this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(),
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
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<>();
		List<JDBCObject> jdbcObjects = new ArrayList<>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnion(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        List<Map<String,Object>> list = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.union(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(), 
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
		
		List<ISQLQueryObject> sqlQueryObjectInnerList = new ArrayList<>();
		List<JDBCObject> jdbcObjects = new ArrayList<>();
		List<Class<?>> returnClassTypes = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareUnionCount(jdbcProperties, log, connection, sqlQueryObject, 
        						this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(), 
        						sqlQueryObjectInnerList, jdbcObjects, union, unionExpression);
		
		if(unionExpression!=null){
			for (int i = 0; i < unionExpression.length; i++) {
				UnionExpression ue = unionExpression[i];
				IExpression expression = ue.getExpression();
				this._join(expression,sqlQueryObjectInnerList.get(i));
			}
		}
        
        NonNegativeNumber number = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.unionCount(jdbcProperties, log, connection, sqlQueryObject, 
        								this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(), 
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
			return new JDBCExpression(this.getRendicontazionePagamentoFieldConverter());
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public JDBCPaginatedExpression newPaginatedExpression(Logger log) throws NotImplementedException, ServiceException {
		try{
			return new JDBCPaginatedExpression(this.getRendicontazionePagamentoFieldConverter());
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
	public RendicontazionePagamento get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._get(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId), idMappingResolutionBehaviour);
	}
	
	private RendicontazionePagamento _get(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, MultipleResultException, NotImplementedException, ServiceException, Exception {
	

		//GET NON IMPLEMENTATA, usare la search
        throw new NotImplementedException("NotImplemented");
	
	} 
	
	@Override
	public boolean exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
		return this._exists(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
	
	private boolean _exists(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long tableId) throws MultipleResultException, NotImplementedException, ServiceException, Exception {
	

		//exists NON IMPLEMENTATA, usare la search
        throw new NotImplementedException("NotImplemented");
	
	
	}
	
	private void _join(IExpression expression, ISQLQueryObject sqlQueryObject) throws NotImplementedException, ServiceException, Exception{
	
		String fr = this.getRendicontazionePagamentoFieldConverter().toAliasTable(RendicontazionePagamento.model().FR);
		String rendicontazione = this.getRendicontazionePagamentoFieldConverter().toAliasTable(RendicontazionePagamento.model().RENDICONTAZIONE);
		String pagamento = this.getRendicontazionePagamentoFieldConverter().toAliasTable(RendicontazionePagamento.model().PAGAMENTO);
		String versamento = this.getRendicontazionePagamentoFieldConverter().toAliasTable(RendicontazionePagamento.model().VERSAMENTO);
		String singoloVersamento = this.getRendicontazionePagamentoFieldConverter().toAliasTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO);
		
		sqlQueryObject.addWhereCondition(fr+".id="+rendicontazione+".id_fr");
		sqlQueryObject.addWhereCondition(rendicontazione+".id_pagamento="+pagamento+".id");
		sqlQueryObject.addWhereCondition(singoloVersamento+".id="+pagamento+".id_singolo_versamento");
		sqlQueryObject.addWhereCondition(versamento+".id="+singoloVersamento+".id_versamento");
		
		
	}
	
	protected java.util.List<Object> _getRootTablePrimaryKeyValues(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, RendicontazionePagamento rendicontazionePagamento) throws NotFoundException, ServiceException, NotImplementedException, Exception{
	    // Identificativi
        java.util.List<Object> rootTableIdValues = new java.util.ArrayList<>();
		rootTableIdValues.add(rendicontazionePagamento.getId());
        
        return rootTableIdValues;
	}
	
	protected Map<String, List<IField>> _getMapTableToPKColumn() throws NotImplementedException, Exception{
	
		RendicontazionePagamentoFieldConverter converter = this.getRendicontazionePagamentoFieldConverter();
		Map<String, List<IField>> mapTableToPKColumn = new java.util.Hashtable<>();
		UtilsTemplate<IField> utilities = new UtilsTemplate<>();

		// RendicontazionePagamento.model().PAGAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_RPT
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RPT),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RPT))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_RR
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RR),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_RR))
			));

		// RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().PAGAMENTO.ID_INCASSO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_APPLICAZIONE))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_TRIBUTO.ID_TIPO_TRIBUTO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_ACCREDITO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO))
			));

		// RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().SINGOLO_VERSAMENTO.ID_IBAN_APPOGGIO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO_DOMINIO.ID_TIPO_VERSAMENTO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TIPO_VERSAMENTO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_UO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_UO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_UO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_UO.ID_DOMINIO
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_UO.ID_DOMINIO),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_UO.ID_DOMINIO))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE))
			));

		// RendicontazionePagamento.model().VERSAMENTO.IUV
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.IUV),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.IUV))
			));

		// RendicontazionePagamento.model().VERSAMENTO.ID_TRACCIATO_AVVISATURA
		mapTableToPKColumn.put(converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TRACCIATO_AVVISATURA),
			utilities.newList(
				new CustomField("id", Long.class, "id", converter.toTable(RendicontazionePagamento.model().VERSAMENTO.ID_TRACCIATO_AVVISATURA))
			));
        
        return mapTableToPKColumn;		
	}
	
	@Override
	public List<Long> findAllTableIds(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCPaginatedExpression paginatedExpression) throws ServiceException, NotImplementedException, Exception {
		
		List<Long> list = new ArrayList<>();

		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getRendicontazionePagamentoFieldConverter().toTable(RendicontazionePagamento.model().FR)+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFindAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
												this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model());
		
		this._join(paginatedExpression,sqlQueryObject);
		
		List<Object> listObjects = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.findAll(jdbcProperties, log, connection, sqlQueryObject, paginatedExpression,
																			this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(), objectIdClass, listaQuery);
		for(Object object: listObjects) {
			list.add((Long)object);
		}

        return list;
		
	}
	
	@Override
	public long findTableId(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException, Exception {
	
		sqlQueryObject.setSelectDistinct(true);
		sqlQueryObject.setANDLogicOperator(true);
		sqlQueryObject.addSelectField(this.getRendicontazionePagamentoFieldConverter().toTable(RendicontazionePagamento.model().FR)+".id");
		Class<?> objectIdClass = Long.class;
		
		List<Object> listaQuery = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.prepareFind(jdbcProperties, log, connection, sqlQueryObject, expression,
												this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model());
		
		this._join(expression,sqlQueryObject);

		Object res = org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.find(jdbcProperties, log, connection, sqlQueryObject, expression,
														this.getRendicontazionePagamentoFieldConverter(), RendicontazionePagamento.model(), objectIdClass, listaQuery);
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
	public List<List<Object>> nativeQuery(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, 
											String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException,Exception{
		
		return org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities.nativeQuery(jdbcProperties, log, connection, sqlQueryObject,
																							sql,returnClassTypes,param);
														
	}
	
}
