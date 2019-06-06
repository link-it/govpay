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

import it.govpay.orm.RR;


/**     
 * RRFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RRFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(RR.model())){
				RR object = new RR();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setCodDominio", RR.model().COD_DOMINIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_dominio", RR.model().COD_DOMINIO.getFieldType()));
				this.setParameter(object, "setIuv", RR.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", RR.model().IUV.getFieldType()));
				this.setParameter(object, "setCcp", RR.model().CCP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ccp", RR.model().CCP.getFieldType()));
				this.setParameter(object, "setCodMsgRevoca", RR.model().COD_MSG_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_revoca", RR.model().COD_MSG_REVOCA.getFieldType()));
				this.setParameter(object, "setDataMsgRevoca", RR.model().DATA_MSG_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_revoca", RR.model().DATA_MSG_REVOCA.getFieldType()));
				this.setParameter(object, "setDataMsgEsito", RR.model().DATA_MSG_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_msg_esito", RR.model().DATA_MSG_ESITO.getFieldType()));
				this.setParameter(object, "setStato", RR.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RR.model().STATO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", RR.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RR.model().DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setImportoTotaleRichiesto", RR.model().IMPORTO_TOTALE_RICHIESTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_richiesto", RR.model().IMPORTO_TOTALE_RICHIESTO.getFieldType()));
				this.setParameter(object, "setCodMsgEsito", RR.model().COD_MSG_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_esito", RR.model().COD_MSG_ESITO.getFieldType()));
				this.setParameter(object, "setImportoTotaleRevocato", RR.model().IMPORTO_TOTALE_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_revocato", RR.model().IMPORTO_TOTALE_REVOCATO.getFieldType()));
				this.setParameter(object, "setXmlRR", RR.model().XML_RR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_rr", RR.model().XML_RR.getFieldType()));
				this.setParameter(object, "setXmlER", RR.model().XML_ER.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml_er", RR.model().XML_ER.getFieldType()));
				this.setParameter(object, "setCodTransazioneRR", RR.model().COD_TRANSAZIONE_RR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_rr", RR.model().COD_TRANSAZIONE_RR.getFieldType()));
				this.setParameter(object, "setCodTransazioneER", RR.model().COD_TRANSAZIONE_ER.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_transazione_er", RR.model().COD_TRANSAZIONE_ER.getFieldType()));
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

			if(model.equals(RR.model())){
				RR object = new RR();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setCodDominio", RR.model().COD_DOMINIO.getFieldType(),
					this.getObjectFromMap(map,"codDominio"));
				this.setParameter(object, "setIuv", RR.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				this.setParameter(object, "setCcp", RR.model().CCP.getFieldType(),
					this.getObjectFromMap(map,"ccp"));
				this.setParameter(object, "setCodMsgRevoca", RR.model().COD_MSG_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRevoca"));
				this.setParameter(object, "setDataMsgRevoca", RR.model().DATA_MSG_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"dataMsgRevoca"));
				this.setParameter(object, "setDataMsgEsito", RR.model().DATA_MSG_ESITO.getFieldType(),
					this.getObjectFromMap(map,"dataMsgEsito"));
				this.setParameter(object, "setStato", RR.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				this.setParameter(object, "setDescrizioneStato", RR.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				this.setParameter(object, "setImportoTotaleRichiesto", RR.model().IMPORTO_TOTALE_RICHIESTO.getFieldType(),
					this.getObjectFromMap(map,"importoTotaleRichiesto"));
				this.setParameter(object, "setCodMsgEsito", RR.model().COD_MSG_ESITO.getFieldType(),
					this.getObjectFromMap(map,"codMsgEsito"));
				this.setParameter(object, "setImportoTotaleRevocato", RR.model().IMPORTO_TOTALE_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"importoTotaleRevocato"));
				this.setParameter(object, "setXmlRR", RR.model().XML_RR.getFieldType(),
					this.getObjectFromMap(map,"xmlRR"));
				this.setParameter(object, "setXmlER", RR.model().XML_ER.getFieldType(),
					this.getObjectFromMap(map,"xmlER"));
				this.setParameter(object, "setCodTransazioneRR", RR.model().COD_TRANSAZIONE_RR.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneRR"));
				this.setParameter(object, "setCodTransazioneER", RR.model().COD_TRANSAZIONE_ER.getFieldType(),
					this.getObjectFromMap(map,"codTransazioneER"));
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

			if(model.equals(RR.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("rr","id","seq_rr","rr_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
