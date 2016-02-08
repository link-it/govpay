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
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodMsgRevoca", RR.model().COD_MSG_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_msg_revoca", RR.model().COD_MSG_REVOCA.getFieldType()));
				setParameter(object, "setDataOraMsgRevoca", RR.model().DATA_ORA_MSG_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_msg_revoca", RR.model().DATA_ORA_MSG_REVOCA.getFieldType()));
				setParameter(object, "setImportoTotaleRevocato", RR.model().IMPORTO_TOTALE_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_totale_revocato", RR.model().IMPORTO_TOTALE_REVOCATO.getFieldType()));
				setParameter(object, "setStato", RR.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", RR.model().STATO.getFieldType()));
				setParameter(object, "setDescrizioneStato", RR.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", RR.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setDataOraCreazione", RR.model().DATA_ORA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_creazione", RR.model().DATA_ORA_CREAZIONE.getFieldType()));
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
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodMsgRevoca", RR.model().COD_MSG_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"codMsgRevoca"));
				setParameter(object, "setDataOraMsgRevoca", RR.model().DATA_ORA_MSG_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"dataOraMsgRevoca"));
				setParameter(object, "setImportoTotaleRevocato", RR.model().IMPORTO_TOTALE_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"importoTotaleRevocato"));
				setParameter(object, "setStato", RR.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setDescrizioneStato", RR.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setDataOraCreazione", RR.model().DATA_ORA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOraCreazione"));
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
