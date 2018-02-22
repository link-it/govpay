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

import it.govpay.orm.PagamentoPortale;


/**     
 * PagamentoPortaleFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoPortaleFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(PagamentoPortale.model())){
				PagamentoPortale object = new PagamentoPortale();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodPortale", PagamentoPortale.model().COD_PORTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_portale", PagamentoPortale.model().COD_PORTALE.getFieldType()));
				setParameter(object, "setCodCanale", PagamentoPortale.model().COD_CANALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_canale", PagamentoPortale.model().COD_CANALE.getFieldType()));
				setParameter(object, "setNome", PagamentoPortale.model().NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome", PagamentoPortale.model().NOME.getFieldType()));
				setParameter(object, "setImporto", PagamentoPortale.model().IMPORTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo", PagamentoPortale.model().IMPORTO.getFieldType()));
				setParameter(object, "setVersanteIdentificativo", PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "versante_identificativo", PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setIdSessione", PagamentoPortale.model().ID_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione", PagamentoPortale.model().ID_SESSIONE.getFieldType()));
				setParameter(object, "setIdSessionePortale", PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione_portale", PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType()));
				setParameter(object, "setIdSessionePsp", PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_sessione_psp", PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType()));
				setParameter(object, "setStato", PagamentoPortale.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", PagamentoPortale.model().STATO.getFieldType()));
				setParameter(object, "setCodiceStato", PagamentoPortale.model().CODICE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_stato", PagamentoPortale.model().CODICE_STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setPspRedirectURL", PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_redirect_url", PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType()));
				setParameter(object, "setPspEsito", PagamentoPortale.model().PSP_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_esito", PagamentoPortale.model().PSP_ESITO.getFieldType()));
				setParameter(object, "setJsonRequest", PagamentoPortale.model().JSON_REQUEST.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "json_request", PagamentoPortale.model().JSON_REQUEST.getFieldType()));
				setParameter(object, "setWispIdDominio", PagamentoPortale.model().WISP_ID_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "wisp_id_dominio", PagamentoPortale.model().WISP_ID_DOMINIO.getFieldType()));
				setParameter(object, "setWispKeyPA", PagamentoPortale.model().WISP_KEY_PA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "wisp_key_pa", PagamentoPortale.model().WISP_KEY_PA.getFieldType()));
				setParameter(object, "setWispKeyWisp", PagamentoPortale.model().WISP_KEY_WISP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "wisp_key_wisp", PagamentoPortale.model().WISP_KEY_WISP.getFieldType()));
				setParameter(object, "setWispHtml", PagamentoPortale.model().WISP_HTML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "wisp_html", PagamentoPortale.model().WISP_HTML.getFieldType()));
				setParameter(object, "setDataRichiesta", PagamentoPortale.model().DATA_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_richiesta", PagamentoPortale.model().DATA_RICHIESTA.getFieldType()));
				setParameter(object, "setUrlRitorno", PagamentoPortale.model().URL_RITORNO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "url_ritorno", PagamentoPortale.model().URL_RITORNO.getFieldType()));
				setParameter(object, "setCodPsp", PagamentoPortale.model().COD_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_psp", PagamentoPortale.model().COD_PSP.getFieldType()));
				setParameter(object, "setTipoVersamento", PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType()));
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

			if(model.equals(PagamentoPortale.model())){
				PagamentoPortale object = new PagamentoPortale();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodPortale", PagamentoPortale.model().COD_PORTALE.getFieldType(),
					this.getObjectFromMap(map,"codPortale"));
				setParameter(object, "setCodCanale", PagamentoPortale.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				setParameter(object, "setNome", PagamentoPortale.model().NOME.getFieldType(),
					this.getObjectFromMap(map,"nome"));
				setParameter(object, "setImporto", PagamentoPortale.model().IMPORTO.getFieldType(),
					this.getObjectFromMap(map,"importo"));
				setParameter(object, "setVersanteIdentificativo", PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"versanteIdentificativo"));
				setParameter(object, "setIdSessione", PagamentoPortale.model().ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"idSessione"));
				setParameter(object, "setIdSessionePortale", PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType(),
					this.getObjectFromMap(map,"idSessionePortale"));
				setParameter(object, "setIdSessionePsp", PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType(),
					this.getObjectFromMap(map,"idSessionePsp"));
				setParameter(object, "setStato", PagamentoPortale.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setCodiceStato", PagamentoPortale.model().CODICE_STATO.getFieldType(),
					this.getObjectFromMap(map,"codiceStato"));
				setParameter(object, "setDescrizioneStato", PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setPspRedirectURL", PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"pspRedirectURL"));
				setParameter(object, "setPspEsito", PagamentoPortale.model().PSP_ESITO.getFieldType(),
					this.getObjectFromMap(map,"pspEsito"));
				setParameter(object, "setJsonRequest", PagamentoPortale.model().JSON_REQUEST.getFieldType(),
					this.getObjectFromMap(map,"jsonRequest"));
				setParameter(object, "setWispIdDominio", PagamentoPortale.model().WISP_ID_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"wispIdDominio"));
				setParameter(object, "setWispKeyPA", PagamentoPortale.model().WISP_KEY_PA.getFieldType(),
					this.getObjectFromMap(map,"wispKeyPA"));
				setParameter(object, "setWispKeyWisp", PagamentoPortale.model().WISP_KEY_WISP.getFieldType(),
					this.getObjectFromMap(map,"wispKeyWisp"));
				setParameter(object, "setWispHtml", PagamentoPortale.model().WISP_HTML.getFieldType(),
					this.getObjectFromMap(map,"wispHtml"));
				setParameter(object, "setDataRichiesta", PagamentoPortale.model().DATA_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"dataRichiesta"));
				setParameter(object, "setUrlRitorno", PagamentoPortale.model().URL_RITORNO.getFieldType(),
					this.getObjectFromMap(map,"urlRitorno"));
				setParameter(object, "setCodPsp", PagamentoPortale.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				setParameter(object, "setTipoVersamento", PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
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

			if(model.equals(PagamentoPortale.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("pagamenti_portale","id","seq_pagamenti_portale","pagamenti_portale_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
