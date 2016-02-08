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

import it.govpay.orm.MediaRilevamento;


/**     
 * MediaRilevamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MediaRilevamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(MediaRilevamento.model())){
				MediaRilevamento object = new MediaRilevamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setIdApplicazione", MediaRilevamento.model().ID_APPLICAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "id_applicazione", MediaRilevamento.model().ID_APPLICAZIONE.getFieldType()));
				setParameter(object, "setDataOsservazione", MediaRilevamento.model().DATA_OSSERVAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_osservazione", MediaRilevamento.model().DATA_OSSERVAZIONE.getFieldType()));
				setParameter(object, "setNumRilevamentiA", MediaRilevamento.model().NUM_RILEVAMENTI_A.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "num_rilevamenti_a", MediaRilevamento.model().NUM_RILEVAMENTI_A.getFieldType()));
				setParameter(object, "setPercentualeA", MediaRilevamento.model().PERCENTUALE_A.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "percentuale_a", MediaRilevamento.model().PERCENTUALE_A.getFieldType()));
				setParameter(object, "setNumRilevamentiB", MediaRilevamento.model().NUM_RILEVAMENTI_B.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "num_rilevamenti_b", MediaRilevamento.model().NUM_RILEVAMENTI_B.getFieldType()));
				setParameter(object, "setPercentualeB", MediaRilevamento.model().PERCENTUALE_B.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "percentuale_b", MediaRilevamento.model().PERCENTUALE_B.getFieldType()));
				setParameter(object, "setNumRilevamentiOver", MediaRilevamento.model().NUM_RILEVAMENTI_OVER.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "num_rilevamenti_over", MediaRilevamento.model().NUM_RILEVAMENTI_OVER.getFieldType()));
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

			if(model.equals(MediaRilevamento.model())){
				MediaRilevamento object = new MediaRilevamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setIdApplicazione", MediaRilevamento.model().ID_APPLICAZIONE.getFieldType(),
					this.getObjectFromMap(map,"idApplicazione"));
				setParameter(object, "setDataOsservazione", MediaRilevamento.model().DATA_OSSERVAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOsservazione"));
				setParameter(object, "setNumRilevamentiA", MediaRilevamento.model().NUM_RILEVAMENTI_A.getFieldType(),
					this.getObjectFromMap(map,"numRilevamentiA"));
				setParameter(object, "setPercentualeA", MediaRilevamento.model().PERCENTUALE_A.getFieldType(),
					this.getObjectFromMap(map,"percentualeA"));
				setParameter(object, "setNumRilevamentiB", MediaRilevamento.model().NUM_RILEVAMENTI_B.getFieldType(),
					this.getObjectFromMap(map,"numRilevamentiB"));
				setParameter(object, "setPercentualeB", MediaRilevamento.model().PERCENTUALE_B.getFieldType(),
					this.getObjectFromMap(map,"percentualeB"));
				setParameter(object, "setNumRilevamentiOver", MediaRilevamento.model().NUM_RILEVAMENTI_OVER.getFieldType(),
					this.getObjectFromMap(map,"numRilevamentiOver"));
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

			if(model.equals(MediaRilevamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("medie_rilevamenti","id","seq_medie_rilevamenti","medie_rilevamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
