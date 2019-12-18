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

import it.govpay.orm.VistaPagamentoPortale;


/**     
 * VistaPagamentoPortaleFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaPagamentoPortaleFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(VistaPagamentoPortale.model())){
				VistaPagamentoPortale object = new VistaPagamentoPortale();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodCanale", VistaPagamentoPortale.model().COD_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_canale", VistaPagamentoPortale.model().COD_CANALE.getFieldType()));
				setParameter(object, "setNome", VistaPagamentoPortale.model().NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome", VistaPagamentoPortale.model().NOME.getFieldType()));
				setParameter(object, "setImporto", VistaPagamentoPortale.model().IMPORTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo", VistaPagamentoPortale.model().IMPORTO.getFieldType()));
				setParameter(object, "setVersanteIdentificativo", VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "versante_identificativo", VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setIdSessione", VistaPagamentoPortale.model().ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione", VistaPagamentoPortale.model().ID_SESSIONE.getFieldType()));
				setParameter(object, "setIdSessionePortale", VistaPagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione_portale", VistaPagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType()));
				setParameter(object, "setIdSessionePsp", VistaPagamentoPortale.model().ID_SESSIONE_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione_psp", VistaPagamentoPortale.model().ID_SESSIONE_PSP.getFieldType()));
				setParameter(object, "setStato", VistaPagamentoPortale.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", VistaPagamentoPortale.model().STATO.getFieldType()));
				setParameter(object, "setCodiceStato", VistaPagamentoPortale.model().CODICE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_stato", VistaPagamentoPortale.model().CODICE_STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", VistaPagamentoPortale.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", VistaPagamentoPortale.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setPspRedirectURL", VistaPagamentoPortale.model().PSP_REDIRECT_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_redirect_url", VistaPagamentoPortale.model().PSP_REDIRECT_URL.getFieldType()));
				setParameter(object, "setPspEsito", VistaPagamentoPortale.model().PSP_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_esito", VistaPagamentoPortale.model().PSP_ESITO.getFieldType()));
				setParameter(object, "setJsonRequest", VistaPagamentoPortale.model().JSON_REQUEST.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "json_request", VistaPagamentoPortale.model().JSON_REQUEST.getFieldType()));
				setParameter(object, "setDataRichiesta", VistaPagamentoPortale.model().DATA_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_richiesta", VistaPagamentoPortale.model().DATA_RICHIESTA.getFieldType()));
				setParameter(object, "setUrlRitorno", VistaPagamentoPortale.model().URL_RITORNO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "url_ritorno", VistaPagamentoPortale.model().URL_RITORNO.getFieldType()));
				setParameter(object, "setCodPsp", VistaPagamentoPortale.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", VistaPagamentoPortale.model().COD_PSP.getFieldType()));
				setParameter(object, "setTipoVersamento", VistaPagamentoPortale.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", VistaPagamentoPortale.model().TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setMultiBeneficiario", VistaPagamentoPortale.model().MULTI_BENEFICIARIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "multi_beneficiario", VistaPagamentoPortale.model().MULTI_BENEFICIARIO.getFieldType()));
				setParameter(object, "setAck", VistaPagamentoPortale.model().ACK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ack", VistaPagamentoPortale.model().ACK.getFieldType()));
				setParameter(object, "setTipo", VistaPagamentoPortale.model().TIPO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo", VistaPagamentoPortale.model().TIPO.getFieldType()));
				setParameter(object, "setPrincipal", VistaPagamentoPortale.model().PRINCIPAL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal", VistaPagamentoPortale.model().PRINCIPAL.getFieldType()));
				setParameter(object, "setTipoUtenza", VistaPagamentoPortale.model().TIPO_UTENZA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_utenza", VistaPagamentoPortale.model().TIPO_UTENZA.getFieldType()));
				setParameter(object, "setDebitoreIdentificativo", VistaPagamentoPortale.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "debitore_identificativo", VistaPagamentoPortale.model().DEBITORE_IDENTIFICATIVO.getFieldType()));
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

			if(model.equals(VistaPagamentoPortale.model())){
				VistaPagamentoPortale object = new VistaPagamentoPortale();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodCanale", VistaPagamentoPortale.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				setParameter(object, "setNome", VistaPagamentoPortale.model().NOME.getFieldType(),
					this.getObjectFromMap(map,"nome"));
				setParameter(object, "setImporto", VistaPagamentoPortale.model().IMPORTO.getFieldType(),
					this.getObjectFromMap(map,"importo"));
				setParameter(object, "setVersanteIdentificativo", VistaPagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"versanteIdentificativo"));
				setParameter(object, "setIdSessione", VistaPagamentoPortale.model().ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"idSessione"));
				setParameter(object, "setIdSessionePortale", VistaPagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType(),
					this.getObjectFromMap(map,"idSessionePortale"));
				setParameter(object, "setIdSessionePsp", VistaPagamentoPortale.model().ID_SESSIONE_PSP.getFieldType(),
					this.getObjectFromMap(map,"idSessionePsp"));
				setParameter(object, "setStato", VistaPagamentoPortale.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setCodiceStato", VistaPagamentoPortale.model().CODICE_STATO.getFieldType(),
					this.getObjectFromMap(map,"codiceStato"));
				setParameter(object, "setDescrizioneStato", VistaPagamentoPortale.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setPspRedirectURL", VistaPagamentoPortale.model().PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"pspRedirectURL"));
				setParameter(object, "setPspEsito", VistaPagamentoPortale.model().PSP_ESITO.getFieldType(),
					this.getObjectFromMap(map,"pspEsito"));
				setParameter(object, "setJsonRequest", VistaPagamentoPortale.model().JSON_REQUEST.getFieldType(),
					this.getObjectFromMap(map,"jsonRequest"));
				setParameter(object, "setDataRichiesta", VistaPagamentoPortale.model().DATA_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"dataRichiesta"));
				setParameter(object, "setUrlRitorno", VistaPagamentoPortale.model().URL_RITORNO.getFieldType(),
					this.getObjectFromMap(map,"urlRitorno"));
				setParameter(object, "setCodPsp", VistaPagamentoPortale.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				setParameter(object, "setTipoVersamento", VistaPagamentoPortale.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				setParameter(object, "setMultiBeneficiario", VistaPagamentoPortale.model().MULTI_BENEFICIARIO.getFieldType(),
					this.getObjectFromMap(map,"multiBeneficiario"));
				setParameter(object, "setAck", VistaPagamentoPortale.model().ACK.getFieldType(),
					this.getObjectFromMap(map,"ack"));
				setParameter(object, "setTipo", VistaPagamentoPortale.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				setParameter(object, "setPrincipal", VistaPagamentoPortale.model().PRINCIPAL.getFieldType(),
					this.getObjectFromMap(map,"principal"));
				setParameter(object, "setTipoUtenza", VistaPagamentoPortale.model().TIPO_UTENZA.getFieldType(),
					this.getObjectFromMap(map,"tipo_utenza"));
				setParameter(object, "setDebitoreIdentificativo", VistaPagamentoPortale.model().DEBITORE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"debitoreIdentificativo"));
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

			if(model.equals(VistaPagamentoPortale.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("v_pagamenti_portale_ext","id","seq_v_pagamenti_portale_ext","v_pagamenti_portale_ext_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
