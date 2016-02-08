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

import it.govpay.orm.TracciatoXML;


/**     
 * TracciatoXMLFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoXMLFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(TracciatoXML.model())){
				TracciatoXML object = new TracciatoXML();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setTipoTracciato", TracciatoXML.model().TIPO_TRACCIATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_tracciato", TracciatoXML.model().TIPO_TRACCIATO.getFieldType()));
				setParameter(object, "setCodMessaggio", TracciatoXML.model().COD_MESSAGGIO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_messaggio", TracciatoXML.model().COD_MESSAGGIO.getFieldType()));
				setParameter(object, "setDataOraCreazione", TracciatoXML.model().DATA_ORA_CREAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_ora_creazione", TracciatoXML.model().DATA_ORA_CREAZIONE.getFieldType()));
				setParameter(object, "setXml", TracciatoXML.model().XML.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "xml", TracciatoXML.model().XML.getFieldType()));
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

			if(model.equals(TracciatoXML.model())){
				TracciatoXML object = new TracciatoXML();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setTipoTracciato", TracciatoXML.model().TIPO_TRACCIATO.getFieldType(),
					this.getObjectFromMap(map,"tipoTracciato"));
				setParameter(object, "setCodMessaggio", TracciatoXML.model().COD_MESSAGGIO.getFieldType(),
					this.getObjectFromMap(map,"codMessaggio"));
				setParameter(object, "setDataOraCreazione", TracciatoXML.model().DATA_ORA_CREAZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataOraCreazione"));
				setParameter(object, "setXml", TracciatoXML.model().XML.getFieldType(),
					this.getObjectFromMap(map,"xml"));
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

			if(model.equals(TracciatoXML.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("tracciatixml","id","seq_tracciatixml","tracciatixml_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
