/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
import org.openspcoop2.generic_project.dao.jdbc.utils.GenericJDBCParameterUtilities;
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
			GenericJDBCParameterUtilities GenericJDBCParameterUtilities =  
					new GenericJDBCParameterUtilities(tipoDatabase);

			if(model.equals(PagamentoPortale.model())){
				PagamentoPortale object = new PagamentoPortale();
				this.setParameter(object, "setId", Long.class,
					GenericJDBCParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodCanale", PagamentoPortale.model().COD_CANALE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "cod_canale", PagamentoPortale.model().COD_CANALE.getFieldType()));
				this.setParameter(object, "setNome", PagamentoPortale.model().NOME.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "nome", PagamentoPortale.model().NOME.getFieldType()));
				this.setParameter(object, "setImporto", PagamentoPortale.model().IMPORTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "importo", PagamentoPortale.model().IMPORTO.getFieldType()));
				this.setParameter(object, "setVersanteIdentificativo", PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "versante_identificativo", PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType()));
				this.setParameter(object, "setIdSessione", PagamentoPortale.model().ID_SESSIONE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "id_sessione", PagamentoPortale.model().ID_SESSIONE.getFieldType()));
				this.setParameter(object, "setIdSessionePortale", PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "id_sessione_portale", PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType()));
				this.setParameter(object, "setIdSessionePsp", PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "id_sessione_psp", PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType()));
				this.setParameter(object, "setStato", PagamentoPortale.model().STATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "stato", PagamentoPortale.model().STATO.getFieldType()));
				this.setParameter(object, "setCodiceStato", PagamentoPortale.model().CODICE_STATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "codice_stato", PagamentoPortale.model().CODICE_STATO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "descrizione_stato", PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setPspRedirectURL", PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "psp_redirect_url", PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType()));
				this.setParameter(object, "setPspEsito", PagamentoPortale.model().PSP_ESITO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "psp_esito", PagamentoPortale.model().PSP_ESITO.getFieldType()));
				this.setParameter(object, "setJsonRequest", PagamentoPortale.model().JSON_REQUEST.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "json_request", PagamentoPortale.model().JSON_REQUEST.getFieldType()));
				setParameter(object, "setDataRichiesta", PagamentoPortale.model().DATA_RICHIESTA.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "data_richiesta", PagamentoPortale.model().DATA_RICHIESTA.getFieldType()));
				this.setParameter(object, "setUrlRitorno", PagamentoPortale.model().URL_RITORNO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "url_ritorno", PagamentoPortale.model().URL_RITORNO.getFieldType()));
				this.setParameter(object, "setCodPsp", PagamentoPortale.model().COD_PSP.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "cod_psp", PagamentoPortale.model().COD_PSP.getFieldType()));
				this.setParameter(object, "setTipoVersamento", PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "tipo_versamento", PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType()));
				this.setParameter(object, "setMultiBeneficiario", PagamentoPortale.model().MULTI_BENEFICIARIO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "multi_beneficiario", PagamentoPortale.model().MULTI_BENEFICIARIO.getFieldType()));
				this.setParameter(object, "setAck", PagamentoPortale.model().ACK.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "ack", PagamentoPortale.model().ACK.getFieldType()));
				this.setParameter(object, "setTipo", PagamentoPortale.model().TIPO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "tipo", PagamentoPortale.model().TIPO.getFieldType()));
				this.setParameter(object, "setPrincipal", PagamentoPortale.model().PRINCIPAL.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "principal", PagamentoPortale.model().PRINCIPAL.getFieldType()));
				this.setParameter(object, "setTipoUtenza", PagamentoPortale.model().TIPO_UTENZA.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "tipo_utenza", PagamentoPortale.model().TIPO_UTENZA.getFieldType()));
				setParameter(object, "setSrcVersanteIdentificativo", PagamentoPortale.model().SRC_VERSANTE_IDENTIFICATIVO.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "src_versante_identificativo", PagamentoPortale.model().SRC_VERSANTE_IDENTIFICATIVO.getFieldType()));
				setParameter(object, "setSeverita", PagamentoPortale.model().SEVERITA.getFieldType(),
					GenericJDBCParameterUtilities.readParameter(rs, "severita", PagamentoPortale.model().SEVERITA.getFieldType()));
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
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodCanale", PagamentoPortale.model().COD_CANALE.getFieldType(),
					this.getObjectFromMap(map,"codCanale"));
				this.setParameter(object, "setNome", PagamentoPortale.model().NOME.getFieldType(),
					this.getObjectFromMap(map,"nome"));
				this.setParameter(object, "setImporto", PagamentoPortale.model().IMPORTO.getFieldType(),
					this.getObjectFromMap(map,"importo"));
				this.setParameter(object, "setVersanteIdentificativo", PagamentoPortale.model().VERSANTE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"versanteIdentificativo"));
				this.setParameter(object, "setIdSessione", PagamentoPortale.model().ID_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"idSessione"));
				this.setParameter(object, "setIdSessionePortale", PagamentoPortale.model().ID_SESSIONE_PORTALE.getFieldType(),
					this.getObjectFromMap(map,"idSessionePortale"));
				this.setParameter(object, "setIdSessionePsp", PagamentoPortale.model().ID_SESSIONE_PSP.getFieldType(),
					this.getObjectFromMap(map,"idSessionePsp"));
				this.setParameter(object, "setStato", PagamentoPortale.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				this.setParameter(object, "setCodiceStato", PagamentoPortale.model().CODICE_STATO.getFieldType(),
					this.getObjectFromMap(map,"codiceStato"));
				this.setParameter(object, "setDescrizioneStato", PagamentoPortale.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				this.setParameter(object, "setPspRedirectURL", PagamentoPortale.model().PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"pspRedirectURL"));
				this.setParameter(object, "setPspEsito", PagamentoPortale.model().PSP_ESITO.getFieldType(),
					this.getObjectFromMap(map,"pspEsito"));
				this.setParameter(object, "setJsonRequest", PagamentoPortale.model().JSON_REQUEST.getFieldType(),
					this.getObjectFromMap(map,"jsonRequest"));
				setParameter(object, "setDataRichiesta", PagamentoPortale.model().DATA_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"dataRichiesta"));
				this.setParameter(object, "setUrlRitorno", PagamentoPortale.model().URL_RITORNO.getFieldType(),
					this.getObjectFromMap(map,"urlRitorno"));
				this.setParameter(object, "setCodPsp", PagamentoPortale.model().COD_PSP.getFieldType(),
					this.getObjectFromMap(map,"codPsp"));
				this.setParameter(object, "setTipoVersamento", PagamentoPortale.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				this.setParameter(object, "setMultiBeneficiario", PagamentoPortale.model().MULTI_BENEFICIARIO.getFieldType(),
					this.getObjectFromMap(map,"multiBeneficiario"));
				this.setParameter(object, "setAck", PagamentoPortale.model().ACK.getFieldType(),
					this.getObjectFromMap(map,"ack"));
				this.setParameter(object, "setTipo", PagamentoPortale.model().TIPO.getFieldType(),
					this.getObjectFromMap(map,"tipo"));
				this.setParameter(object, "setPrincipal", PagamentoPortale.model().PRINCIPAL.getFieldType(),
					this.getObjectFromMap(map,"principal"));
				this.setParameter(object, "setTipoUtenza", PagamentoPortale.model().TIPO_UTENZA.getFieldType(),
					this.getObjectFromMap(map,"tipo_utenza"));
				setParameter(object, "setSrcVersanteIdentificativo", PagamentoPortale.model().SRC_VERSANTE_IDENTIFICATIVO.getFieldType(),
					this.getObjectFromMap(map,"srcVersanteIdentificativo"));
				setParameter(object, "setSeverita", PagamentoPortale.model().SEVERITA.getFieldType(),
					this.getObjectFromMap(map,"severita"));
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
