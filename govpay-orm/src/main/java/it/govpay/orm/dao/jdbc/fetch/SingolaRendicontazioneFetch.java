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

import it.govpay.orm.SingolaRendicontazione;


/**     
 * SingolaRendicontazioneFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingolaRendicontazioneFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(SingolaRendicontazione.model())){
				SingolaRendicontazione object = new SingolaRendicontazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setIuv", SingolaRendicontazione.model().IUV.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iuv", SingolaRendicontazione.model().IUV.getFieldType()));
				setParameter(object, "setIur", SingolaRendicontazione.model().IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", SingolaRendicontazione.model().IUR.getFieldType()));
				setParameter(object, "setSingoloImporto", SingolaRendicontazione.model().SINGOLO_IMPORTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "singolo_importo", SingolaRendicontazione.model().SINGOLO_IMPORTO.getFieldType()));
				setParameter(object, "setCodiceEsito", SingolaRendicontazione.model().CODICE_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codice_esito", SingolaRendicontazione.model().CODICE_ESITO.getFieldType()));
				setParameter(object, "setDataEsito", SingolaRendicontazione.model().DATA_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_esito", SingolaRendicontazione.model().DATA_ESITO.getFieldType()));
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

			if(model.equals(SingolaRendicontazione.model())){
				SingolaRendicontazione object = new SingolaRendicontazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setIuv", SingolaRendicontazione.model().IUV.getFieldType(),
					this.getObjectFromMap(map,"iuv"));
				setParameter(object, "setIur", SingolaRendicontazione.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
				setParameter(object, "setSingoloImporto", SingolaRendicontazione.model().SINGOLO_IMPORTO.getFieldType(),
					this.getObjectFromMap(map,"singoloImporto"));
				setParameter(object, "setCodiceEsito", SingolaRendicontazione.model().CODICE_ESITO.getFieldType(),
					this.getObjectFromMap(map,"codiceEsito"));
				setParameter(object, "setDataEsito", SingolaRendicontazione.model().DATA_ESITO.getFieldType(),
					this.getObjectFromMap(map,"dataEsito"));
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

			if(model.equals(SingolaRendicontazione.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("singole_rendicontazioni","id","seq_singole_rendicontazioni","singole_rendicontazioni_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
