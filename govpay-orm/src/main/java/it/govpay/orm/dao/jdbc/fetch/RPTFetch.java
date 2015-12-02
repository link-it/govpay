/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
				setParameter(object, "setTipoVersamento", RPT.model().TIPO_VERSAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_versamento", RPT.model().TIPO_VERSAMENTO.getFieldType()));
				setParameter(object, "setDataOraMsgRichiesta", RPT.model().DATA_ORA_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_msg_richiesta", RPT.model().DATA_ORA_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setDataOraCreazione", RPT.model().DATA_ORA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_creazione", RPT.model().DATA_ORA_CREAZIONE.getFieldType()));
				setParameter(object, "setCodMsgRichiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_richiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType()));
				setParameter(object, "setIbanAddebito", RPT.model().IBAN_ADDEBITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iban_addebito", RPT.model().IBAN_ADDEBITO.getFieldType()));
				setParameter(object, "setAutenticazioneSoggetto", RPT.model().AUTENTICAZIONE_SOGGETTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "autenticazione_soggetto", RPT.model().AUTENTICAZIONE_SOGGETTO.getFieldType()));
				setParameter(object, "setFirmaRT", RPT.model().FIRMA_RT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "firma_rt", RPT.model().FIRMA_RT.getFieldType()));
				setParameter(object, "setStato", RPT.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RPT.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RPT.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RPT.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setCodFault", RPT.model().COD_FAULT.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_fault", RPT.model().COD_FAULT.getFieldType()));
				setParameter(object, "setCallbackURL", RPT.model().CALLBACK_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "callback_url", RPT.model().CALLBACK_URL.getFieldType()));
				setParameter(object, "setCodSessione", RPT.model().COD_SESSIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_sessione", RPT.model().COD_SESSIONE.getFieldType()));
				setParameter(object, "setPspRedirectURL", RPT.model().PSP_REDIRECT_URL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "psp_redirect_url", RPT.model().PSP_REDIRECT_URL.getFieldType()));
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
				setParameter(object, "setTipoVersamento", RPT.model().TIPO_VERSAMENTO.getFieldType(),
					this.getObjectFromMap(map,"tipoVersamento"));
				setParameter(object, "setDataOraMsgRichiesta", RPT.model().DATA_ORA_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"dataOraMsgRichiesta"));
				setParameter(object, "setDataOraCreazione", RPT.model().DATA_ORA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOraCreazione"));
				setParameter(object, "setCodMsgRichiesta", RPT.model().COD_MSG_RICHIESTA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRichiesta"));
				setParameter(object, "setIbanAddebito", RPT.model().IBAN_ADDEBITO.getFieldType(),
					this.getObjectFromMap(map,"ibanAddebito"));
				setParameter(object, "setAutenticazioneSoggetto", RPT.model().AUTENTICAZIONE_SOGGETTO.getFieldType(),
					this.getObjectFromMap(map,"autenticazioneSoggetto"));
				setParameter(object, "setFirmaRT", RPT.model().FIRMA_RT.getFieldType(),
					this.getObjectFromMap(map,"firmaRT"));
				setParameter(object, "setStato", RPT.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", RPT.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setCodFault", RPT.model().COD_FAULT.getFieldType(),
					this.getObjectFromMap(map,"codFault"));
				setParameter(object, "setCallbackURL", RPT.model().CALLBACK_URL.getFieldType(),
					this.getObjectFromMap(map,"callbackURL"));
				setParameter(object, "setCodSessione", RPT.model().COD_SESSIONE.getFieldType(),
					this.getObjectFromMap(map,"codSessione"));
				setParameter(object, "setPspRedirectURL", RPT.model().PSP_REDIRECT_URL.getFieldType(),
					this.getObjectFromMap(map,"pspRedirectURL"));
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
