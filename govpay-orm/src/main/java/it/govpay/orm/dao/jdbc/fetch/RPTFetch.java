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
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodCarrello", RPT.model().COD_CARRELLO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_carrello", RPT.model().COD_CARRELLO.getFieldType()));
				setParameter(object, "setIuv", RPT.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RPT.model().IUV.getFieldType()));
				setParameter(object, "setCcp", RPT.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", RPT.model().CCP.getFieldType()));
				setParameter(object, "setCodDominio", RPT.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RPT.model().COD_DOMINIO.getFieldType()));
				setParameter(object, "setCodMsgRichiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_richiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setDataMsgRichiesta", RPT.model().DATA_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_richiesta", RPT.model().DATA_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setStato", RPT.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RPT.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RPT.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RPT.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setCodSessione", RPT.model().COD_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione", RPT.model().COD_SESSIONE.getFieldType()));
				setParameter(object, "setCodSessionePortale", RPT.model().COD_SESSIONE_PORTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione_portale", RPT.model().COD_SESSIONE_PORTALE.getFieldType()));
				setParameter(object, "setPspRedirectURL", RPT.model().PSP_REDIRECT_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_redirect_url", RPT.model().PSP_REDIRECT_URL.getFieldType()));
				setParameter(object, "setXmlRPT", RPT.model().XML_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rpt", RPT.model().XML_RPT.getFieldType()));
				setParameter(object, "setDataAggiornamentoStato", RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_aggiornamento_stato", RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
				setParameter(object, "setCallbackURL", RPT.model().CALLBACK_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "callback_url", RPT.model().CALLBACK_URL.getFieldType()));
				setParameter(object, "setModelloPagamento", RPT.model().MODELLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "modello_pagamento", RPT.model().MODELLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setCodMsgRicevuta", RPT.model().COD_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_ricevuta", RPT.model().COD_MSG_RICEVUTA.getFieldType()));
				setParameter(object, "setDataMsgRicevuta", RPT.model().DATA_MSG_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_ricevuta", RPT.model().DATA_MSG_RICEVUTA.getFieldType()));
				setParameter(object, "setFirmaRicevuta", RPT.model().FIRMA_RICEVUTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "firma_ricevuta", RPT.model().FIRMA_RICEVUTA.getFieldType()));
				setParameter(object, "setCodEsitoPagamento", RPT.model().COD_ESITO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_esito_pagamento", RPT.model().COD_ESITO_PAGAMENTO.getFieldType()));
				setParameter(object, "setImportoTotalePagato", RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_pagato", RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType()));
				setParameter(object, "setXmlRT", RPT.model().XML_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rt", RPT.model().XML_RT.getFieldType()));
				setParameter(object, "setCodStazione", RPT.model().COD_STAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_stazione", RPT.model().COD_STAZIONE.getFieldType()));
				setParameter(object, "setCodTransazioneRPT", RPT.model().COD_TRANSAZIONE_RPT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rpt", RPT.model().COD_TRANSAZIONE_RPT.getFieldType()));
				setParameter(object, "setCodTransazioneRT", RPT.model().COD_TRANSAZIONE_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rt", RPT.model().COD_TRANSAZIONE_RT.getFieldType()));
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
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodCarrello", RPT.model().COD_CARRELLO.getFieldType(),
					this.getObjectFromMap(map,"codCarrello"));
				setParameter(object, "setIuv", RPT.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setCcp", RPT.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				setParameter(object, "setCodDominio", RPT.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				setParameter(object, "setCodMsgRichiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRichiesta"));
				setParameter(object, "setDataMsgRichiesta", RPT.model().DATA_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"dataMsgRichiesta"));
				setParameter(object, "setStato", RPT.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", RPT.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setCodSessione", RPT.model().COD_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"codSessione"));
				setParameter(object, "setCodSessionePortale", RPT.model().COD_SESSIONE_PORTALE.getFieldType(),
					this.getObjectFromMap(map,"codSessionePortale"));
				setParameter(object, "setPspRedirectURL", RPT.model().PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"pspRedirectURL"));
				setParameter(object, "setXmlRPT", RPT.model().XML_RPT.getFieldType(),
					this.getObjectFromMap(map,"xmlRPT"));
				setParameter(object, "setDataAggiornamentoStato", RPT.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					this.getObjectFromMap(map,"dataAggiornamentoStato"));
				setParameter(object, "setCallbackURL", RPT.model().CALLBACK_URL.getFieldType(),
					this.getObjectFromMap(map,"callbackURL"));
				setParameter(object, "setModelloPagamento", RPT.model().MODELLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"modelloPagamento"));
				setParameter(object, "setCodMsgRicevuta", RPT.model().COD_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRicevuta"));
				setParameter(object, "setDataMsgRicevuta", RPT.model().DATA_MSG_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"dataMsgRicevuta"));
				setParameter(object, "setFirmaRicevuta", RPT.model().FIRMA_RICEVUTA.getFieldType(),
					this.getObjectFromMap(map,"firmaRicevuta"));
				setParameter(object, "setCodEsitoPagamento", RPT.model().COD_ESITO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"codEsitoPagamento"));
				setParameter(object, "setImportoTotalePagato", RPT.model().IMPORTO_TOTALE_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoTotalePagato"));
				setParameter(object, "setXmlRT", RPT.model().XML_RT.getFieldType(),
					this.getObjectFromMap(map,"xmlRT"));
				setParameter(object, "setCodStazione", RPT.model().COD_STAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codStazione"));
				setParameter(object, "setCodTransazioneRPT", RPT.model().COD_TRANSAZIONE_RPT.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneRPT"));
				setParameter(object, "setCodTransazioneRT", RPT.model().COD_TRANSAZIONE_RT.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneRT"));
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
