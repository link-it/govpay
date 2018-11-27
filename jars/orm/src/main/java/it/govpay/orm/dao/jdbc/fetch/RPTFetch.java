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
package it.govpay.orm.dao.jdbc.fetch;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;

import it.govpay.orm.RPT;


/**     
 * RPTFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RPTFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(RPT.model())){
				RPT object = new RPT();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodCarrello", RPT.model().COD_CARRELLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_carrello", RPT.model().COD_CARRELLO.getFieldType()));
				this.setParameter(object, "setIuv", RPT.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RPT.model().IUV.getFieldType()));
				this.setParameter(object, "setCcp", RPT.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", RPT.model().CCP.getFieldType()));
				this.setParameter(object, "setCodDominio", RPT.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RPT.model().COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setCodMsgRichiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_richiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType()));
				this.setParameter(object, "setDataMsgRichiesta", RPT.model().DATA_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_richiesta", RPT.model().DATA_MSG_RICHIESTA.getFieldType()));
				this.setParameter(object, "setStato", RPT.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RPT.model().STATO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", RPT.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RPT.model().DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setCodSessione", RPT.model().COD_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione", RPT.model().COD_SESSIONE.getFieldType()));
				this.setParameter(object, "setCodSessionePortale", RPT.model().COD_SESSIONE_PORTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione_portale", RPT.model().COD_SESSIONE_PORTALE.getFieldType()));
				this.setParameter(object, "setPspRedirectURL", RPT.model().PSP_REDIRECT_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_redirect_url", RPT.model().PSP_REDIRECT_URL.getFieldType()));
				this.setParameter(object, "setXmlRPT", RPT.model().XML_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rpt", RPT.model().XML_RPT.getFieldType()));
				this.setParameter(object, "setDataAggiornamentoStato", RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_aggiornamento_stato", RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
				this.setParameter(object, "setCallbackURL", RPT.model().CALLBACK_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "callback_url", RPT.model().CALLBACK_URL.getFieldType()));
				this.setParameter(object, "setModelloPagamento", RPT.model().MODELLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "modello_pagamento", RPT.model().MODELLO_PAGAMENTO.getFieldType()));
				this.setParameter(object, "setCodMsgRicevuta", RPT.model().COD_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_ricevuta", RPT.model().COD_MSG_RICEVUTA.getFieldType()));
				this.setParameter(object, "setDataMsgRicevuta", RPT.model().DATA_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_ricevuta", RPT.model().DATA_MSG_RICEVUTA.getFieldType()));
				this.setParameter(object, "setCodEsitoPagamento", RPT.model().COD_ESITO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_esito_pagamento", RPT.model().COD_ESITO_PAGAMENTO.getFieldType()));
				this.setParameter(object, "setImportoTotalePagato", RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagato", RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType()));
				this.setParameter(object, "setXmlRT", RPT.model().XML_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rt", RPT.model().XML_RT.getFieldType()));
				this.setParameter(object, "setCodCanale", RPT.model().COD_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_canale", RPT.model().COD_CANALE.getFieldType()));
				this.setParameter(object, "setCodPsp", RPT.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", RPT.model().COD_PSP.getFieldType()));
				this.setParameter(object, "setCodIntermediarioPsp", RPT.model().COD_INTERMEDIARIO_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_intermediario_psp", RPT.model().COD_INTERMEDIARIO_PSP.getFieldType()));
				this.setParameter(object, "setTipoVersamento", RPT.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", RPT.model().TIPO_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setTipoIdentificativoAttestante", RPT.model().TIPO_IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_identificativo_attestante", RPT.model().TIPO_IDENTIFICATIVO_ATTESTANTE.getFieldType()));
				this.setParameter(object, "setIdentificativoAttestante", RPT.model().IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "identificativo_attestante", RPT.model().IDENTIFICATIVO_ATTESTANTE.getFieldType()));
				this.setParameter(object, "setDenominazioneAttestante", RPT.model().DENOMINAZIONE_ATTESTANTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "denominazione_attestante", RPT.model().DENOMINAZIONE_ATTESTANTE.getFieldType()));
				this.setParameter(object, "setCodStazione", RPT.model().COD_STAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_stazione", RPT.model().COD_STAZIONE.getFieldType()));
				this.setParameter(object, "setCodTransazioneRPT", RPT.model().COD_TRANSAZIONE_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rpt", RPT.model().COD_TRANSAZIONE_RPT.getFieldType()));
				this.setParameter(object, "setCodTransazioneRT", RPT.model().COD_TRANSAZIONE_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rt", RPT.model().COD_TRANSAZIONE_RT.getFieldType()));
				this.setParameter(object, "setStatoConservazione", RPT.model().STATO_CONSERVAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato_conservazione", RPT.model().STATO_CONSERVAZIONE.getFieldType()));
				this.setParameter(object, "setDescrizioneStatoCons", RPT.model().DESCRIZIONE_STATO_CONS.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato_cons", RPT.model().DESCRIZIONE_STATO_CONS.getFieldType()));
				this.setParameter(object, "setDataConservazione", RPT.model().DATA_CONSERVAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_conservazione", RPT.model().DATA_CONSERVAZIONE.getFieldType()));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , Map<String,Object> map ) throws ServiceException {
		
		try{

			if(model.equals(RPT.model())){
				RPT object = new RPT();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodCarrello", RPT.model().COD_CARRELLO.getFieldType(),
					this.getObjectFromMap(map,"codCarrello"));
				this.setParameter(object, "setIuv", RPT.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				this.setParameter(object, "setCcp", RPT.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				this.setParameter(object, "setCodDominio", RPT.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				this.setParameter(object, "setCodMsgRichiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRichiesta"));
				this.setParameter(object, "setDataMsgRichiesta", RPT.model().DATA_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"dataMsgRichiesta"));
				this.setParameter(object, "setStato", RPT.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				this.setParameter(object, "setDescrizioneStato", RPT.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				this.setParameter(object, "setCodSessione", RPT.model().COD_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"codSessione"));
				this.setParameter(object, "setCodSessionePortale", RPT.model().COD_SESSIONE_PORTALE.getFieldType(),
					this.getObjectFromMap(map,"codSessionePortale"));
				this.setParameter(object, "setPspRedirectURL", RPT.model().PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"pspRedirectURL"));
				this.setParameter(object, "setXmlRPT", RPT.model().XML_RPT.getFieldType(),
					this.getObjectFromMap(map,"xmlRPT"));
				this.setParameter(object, "setDataAggiornamentoStato", RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					this.getObjectFromMap(map,"dataAggiornamentoStato"));
				this.setParameter(object, "setCallbackURL", RPT.model().CALLBACK_URL.getFieldType(),
					this.getObjectFromMap(map,"callbackURL"));
				this.setParameter(object, "setModelloPagamento", RPT.model().MODELLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"modelloPagamento"));
				this.setParameter(object, "setCodMsgRicevuta", RPT.model().COD_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRicevuta"));
				this.setParameter(object, "setDataMsgRicevuta", RPT.model().DATA_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"dataMsgRicevuta"));
				this.setParameter(object, "setCodEsitoPagamento", RPT.model().COD_ESITO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"codEsitoPagamento"));
				this.setParameter(object, "setImportoTotalePagato", RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoTotalePagato"));
				this.setParameter(object, "setXmlRT", RPT.model().XML_RT.getFieldType(),
					this.getObjectFromMap(map,"xmlRT"));
				this.setParameter(object, "setCodCanale", RPT.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				this.setParameter(object, "setCodPsp", RPT.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				this.setParameter(object, "setCodIntermediarioPsp", RPT.model().COD_INTERMEDIARIO_PSP.getFieldType(),
					this.getObjectFromMap(map,"codIntermediarioPsp"));
				this.setParameter(object, "setTipoVersamento", RPT.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				this.setParameter(object, "setTipoIdentificativoAttestante", RPT.model().TIPO_IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					this.getObjectFromMap(map,"tipoIdentificativoAttestante"));
				this.setParameter(object, "setIdentificativoAttestante", RPT.model().IDENTIFICATIVO_ATTESTANTE.getFieldType(),
					this.getObjectFromMap(map,"identificativoAttestante"));
				this.setParameter(object, "setDenominazioneAttestante", RPT.model().DENOMINAZIONE_ATTESTANTE.getFieldType(),
					this.getObjectFromMap(map,"denominazioneAttestante"));
				this.setParameter(object, "setCodStazione", RPT.model().COD_STAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codStazione"));
				this.setParameter(object, "setCodTransazioneRPT", RPT.model().COD_TRANSAZIONE_RPT.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneRPT"));
				this.setParameter(object, "setCodTransazioneRT", RPT.model().COD_TRANSAZIONE_RT.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneRT"));
				this.setParameter(object, "setStatoConservazione", RPT.model().STATO_CONSERVAZIONE.getFieldType(),
					this.getObjectFromMap(map,"statoConservazione"));
				this.setParameter(object, "setDescrizioneStatoCons", RPT.model().DESCRIZIONE_STATO_CONS.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStatoCons"));
				this.setParameter(object, "setDataConservazione", RPT.model().DATA_CONSERVAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataConservazione"));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	
	@Override
	public IKeyGeneratorObject getKeyGeneratorObject( IModel<?> model )  throws ServiceException {
		
		try{

			if(model.equals(RPT.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("rpt","id","seq_rpt","rpt_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
