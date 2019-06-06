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

import it.govpay.orm.Notifica;


/**     
 * NotificaFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class NotificaFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Notifica.model())){
				Notifica object = new Notifica();
				this.setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				this.setParameter(object, "setTipoEsito", Notifica.model().TIPO_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_esito", Notifica.model().TIPO_ESITO.getFieldType()));
				this.setParameter(object, "setDataCreazione", Notifica.model().DATA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_creazione", Notifica.model().DATA_CREAZIONE.getFieldType()));
				this.setParameter(object, "setStato", Notifica.model().STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "stato", Notifica.model().STATO.getFieldType()));
				this.setParameter(object, "setDescrizioneStato", Notifica.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", Notifica.model().DESCRIZIONE_STATO.getFieldType()));
				this.setParameter(object, "setDataAggiornamentoStato", Notifica.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_aggiornamento_stato", Notifica.model().DATA_AGGIORNAMENTO_STATO.getFieldType()));
				this.setParameter(object, "setDataProssimaSpedizione", Notifica.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_prossima_spedizione", Notifica.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType()));
				this.setParameter(object, "setTentativiSpedizione", Notifica.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tentativi_spedizione", Notifica.model().TENTATIVI_SPEDIZIONE.getFieldType()));
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

			if(model.equals(Notifica.model())){
				Notifica object = new Notifica();
				this.setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				this.setParameter(object, "setTipoEsito", Notifica.model().TIPO_ESITO.getFieldType(),
					this.getObjectFromMap(map,"tipoEsito"));
				this.setParameter(object, "setDataCreazione", Notifica.model().DATA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataCreazione"));
				this.setParameter(object, "setStato", Notifica.model().STATO.getFieldType(),
					this.getObjectFromMap(map,"stato"));
				this.setParameter(object, "setDescrizioneStato", Notifica.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				this.setParameter(object, "setDataAggiornamentoStato", Notifica.model().DATA_AGGIORNAMENTO_STATO.getFieldType(),
					this.getObjectFromMap(map,"dataAggiornamentoStato"));
				this.setParameter(object, "setDataProssimaSpedizione", Notifica.model().DATA_PROSSIMA_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataProssimaSpedizione"));
				this.setParameter(object, "setTentativiSpedizione", Notifica.model().TENTATIVI_SPEDIZIONE.getFieldType(),
					this.getObjectFromMap(map,"tentativiSpedizione"));
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

			if(model.equals(Notifica.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("notifiche","id","seq_notifiche","notifiche_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
