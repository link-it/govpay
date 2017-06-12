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

import it.govpay.orm.Tracciato;


/**     
 * TracciatoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Tracciato.model())){
				Tracciato object = new Tracciato();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setDataCaricamento", Tracciato.model().DATA_CARICAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_caricamento", Tracciato.model().DATA_CARICAMENTO.getFieldType()));
				setParameter(object, "setDataUltimoAggiornamento", Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ultimo_aggiornamento", Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO.getFieldType()));
				setParameter(object, "set_value_stato", String.class,
					jdbcParameterUtilities.readParameter(rs, "stato", Tracciato.model().STATO.getFieldType())+"");
				setParameter(object, "setLineaElaborazione", Tracciato.model().LINEA_ELABORAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "linea_elaborazione", Tracciato.model().LINEA_ELABORAZIONE.getFieldType()));
				setParameter(object, "setDescrizioneStato", Tracciato.model().DESCRIZIONE_STATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "descrizione_stato", Tracciato.model().DESCRIZIONE_STATO.getFieldType()));
				setParameter(object, "setNumLineeTotali", Tracciato.model().NUM_LINEE_TOTALI.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "num_linee_totali", Tracciato.model().NUM_LINEE_TOTALI.getFieldType()));
				setParameter(object, "setNumOperazioniOk", Tracciato.model().NUM_OPERAZIONI_OK.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "num_operazioni_ok", Tracciato.model().NUM_OPERAZIONI_OK.getFieldType()));
				setParameter(object, "setNumOperazioniKo", Tracciato.model().NUM_OPERAZIONI_KO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "num_operazioni_ko", Tracciato.model().NUM_OPERAZIONI_KO.getFieldType()));
				setParameter(object, "setNomeFile", Tracciato.model().NOME_FILE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome_file", Tracciato.model().NOME_FILE.getFieldType()));
				setParameter(object, "setRawData", Tracciato.model().RAW_DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "raw_data", Tracciato.model().RAW_DATA.getFieldType()));
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

			if(model.equals(Tracciato.model())){
				Tracciato object = new Tracciato();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setDataCaricamento", Tracciato.model().DATA_CARICAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataCaricamento"));
				setParameter(object, "setDataUltimoAggiornamento", Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataUltimoAggiornamento"));
				setParameter(object, "set_value_stato", String.class,
					this.getObjectFromMap(map,"stato"));
				setParameter(object, "setLineaElaborazione", Tracciato.model().LINEA_ELABORAZIONE.getFieldType(),
					this.getObjectFromMap(map,"lineaElaborazione"));
				setParameter(object, "setDescrizioneStato", Tracciato.model().DESCRIZIONE_STATO.getFieldType(),
					this.getObjectFromMap(map,"descrizioneStato"));
				setParameter(object, "setNumLineeTotali", Tracciato.model().NUM_LINEE_TOTALI.getFieldType(),
					this.getObjectFromMap(map,"numLineeTotali"));
				setParameter(object, "setNumOperazioniOk", Tracciato.model().NUM_OPERAZIONI_OK.getFieldType(),
					this.getObjectFromMap(map,"numOperazioniOk"));
				setParameter(object, "setNumOperazioniKo", Tracciato.model().NUM_OPERAZIONI_KO.getFieldType(),
					this.getObjectFromMap(map,"numOperazioniKo"));
				setParameter(object, "setNomeFile", Tracciato.model().NOME_FILE.getFieldType(),
					this.getObjectFromMap(map,"nomeFile"));
				setParameter(object, "setRawData", Tracciato.model().RAW_DATA.getFieldType(),
					this.getObjectFromMap(map,"rawData"));
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

			if(model.equals(Tracciato.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tracciati","id","seq_tracciati","tracciati_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
